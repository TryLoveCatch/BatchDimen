import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JOptionPane;


public class Main {
//    private final static String SRC_PATH = "E:\\workspace\\TestTaobao\\res\\values\\dimens.xml";
//    private final static int SRC_DIMEN = 360;//单位为dp
//    private final static int[] DST_DIMEN = {300,600,720};//单位为dp
    
    private static String mPath ;
    private static int mSrcDimen ;
    private static List<Integer> mDstDimen;
    
    public static void main(String[] args) {
//        br = new Scanner(System.in); 
        mDstDimen = new ArrayList<Integer>();
//        try {
//            mPath = br.nextLine();
//            mSrcDimen = Integer.parseInt(br.nextLine());
//            String tDstDimen = br.nextLine();
//            String tArr[] = tDstDimen.split(",");
//            for(int i = 0;i<tArr.length;i++){
//                mDstDimen.add(Integer.parseInt(tArr[i]));
//            }
//        } catch (Exception e1) {
//            e1.printStackTrace();
//        }
        
        mPath = JOptionPane.showInputDialog("请输入原始dimen所在文件夹地址");
        if(mPath==null || "".equals(mPath.trim())){
            return;
        }
        String tSrcDimen =JOptionPane.showInputDialog( "输入原始的dp值" );
        mSrcDimen = Integer.parseInt(tSrcDimen);
        String tDstDimen =JOptionPane.showInputDialog( "输入目标的dp值，以逗号区分" );
        String tArrStr[] = tDstDimen.split(",");
          for(int i = 0;i<tArrStr.length;i++){
              mDstDimen.add(Integer.parseInt(tArrStr[i]));
          }
        
        
        File tFile = new File(mPath);
        
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
    
    private final static String REGEX_STR = "([0-9]+\\.?[0-9]*)";
    static Pattern pattern = Pattern.compile(REGEX_STR);
    private static float getValue(String pStr){
        Matcher matcher = pattern.matcher(pStr);   
        if(matcher.find()){
            return Float.parseFloat(matcher.group(1));
        }
        return -1;
    }
    
    private static void create(File pFile){
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
                File tFileDst = new File(pFile.getParentFile().getParentFile(), tDir);
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

}
