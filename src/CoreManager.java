import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class CoreManager {

    private String mSrcPath;
    private String mDstPath;
    private int mSrcDimen;
    private List<Integer> mDstDimen;
    
    public void startWork(String pSrcPath, String pDstPath, int pSrcDimen, List<Integer> pDstDimen){
        
        mSrcPath = pSrcPath;
        mDstPath = pDstPath;
        mSrcDimen = pSrcDimen;
        mDstDimen = pDstDimen;
        
        File tFile = new File(mSrcPath);
        
        if(!tFile.exists()) return;
        if(tFile.isFile()){
            create(tFile);
            return;
        }
        
        File[] tArrFiles = tFile.listFiles();
        for(File file : tArrFiles){
            if(file.isFile()){
                String tFileName = file.getName();
                if(tFileName.contains("dimen")){
                    create(file);
                }
            }
        }
    }
    
    private void create(File pFile){
        XmlParse tXmlParse = new XmlParse();
        if(!pFile.exists()) return;
        
        FileInputStream tFis = null;;
        try {
            tFis = new FileInputStream(pFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            tFis = null;
        }
        if(tFis == null) return;
        
        List<InfoDimen> tArrSrc = tXmlParse.parseXml(tFis);
        if(tArrSrc!=null && tArrSrc.size() > 0){
            for(int i=0;i<mDstDimen.size();i++){
                List<InfoDimen> tArr = new ArrayList<InfoDimen>();
                float tScale = 1.0f * mDstDimen.get(i) / mSrcDimen;
                for(InfoDimen tInfo : tArrSrc){
                    InfoDimen tInfoDst = new InfoDimen();
                    tInfoDst.name = tInfo.name;
                    String tValue = tInfo.value;
                    float tSrcValue = getValue(tValue); 
                    if(tSrcValue < 0){
                        tInfoDst.value = "***";//提醒用户
                    }else{
                        tInfoDst.value = tValue.replaceAll(REGEX_STR, (tSrcValue * tScale) + "");
                    }
                    tArr.add(tInfoDst);
                }
                
                String tDir = "values-sw" + mDstDimen.get(i) + "dp";
//                File tFileDst = new File(pFile.getParentFile().getParentFile(), tDir);
                File tFileDst = new File(mDstPath, tDir);
                tFileDst.mkdir();
                File tFileXml = new File(tFileDst, pFile.getName());
//                tFileXml.deleteOnExit();
                try {
                    tFileXml.createNewFile();
                    tXmlParse.createXml(tArr, tFileXml.getAbsolutePath());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
    
    private final static String REGEX_STR = "([0-9]+\\.?[0-9]*)";
    Pattern pattern = Pattern.compile(REGEX_STR);
    private float getValue(String pStr){
        Matcher matcher = pattern.matcher(pStr);   
        if(matcher.find()){
            return Float.parseFloat(matcher.group(1));
        }
        return -1;
    }
}
