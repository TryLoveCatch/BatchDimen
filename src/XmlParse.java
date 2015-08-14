import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.kxml2.io.KXmlParser;
import org.kxml2.io.KXmlSerializer;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlSerializer;


public class XmlParse {

    
    public List<InfoDimen> parseXml(InputStream is){
        List<InfoDimen> mSrcList = null;
        XmlPullParser tXmlParser = new KXmlParser();
        try {
            tXmlParser.setInput(is, "utf-8");
            int tEventType = tXmlParser.getEventType();
            InfoDimen tInfo = null;
            while(tEventType!=XmlPullParser.END_DOCUMENT){
                
                String tTagName = tXmlParser.getName();
                
                switch(tEventType){
                case XmlPullParser.START_DOCUMENT:
                    mSrcList = new ArrayList<InfoDimen>();
                    break;
                case XmlPullParser.START_TAG:
                    if("dimen".equals(tTagName)){
                        tInfo = new InfoDimen();
                        tInfo.name = tXmlParser.getAttributeValue(null, "name");
                        tInfo.value = tXmlParser.nextText();
                        mSrcList.add(tInfo);
                    }
                    break;
                case XmlPullParser.END_TAG:
//                    if("dimen".equals(tTagName)){
//                        mSrcList.add(tInfo);
//                    }
                    break;
                }
                
                tEventType = tXmlParser.next();
            }
            
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
            try {
                is.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
        return mSrcList;
    }
    
    
    public void createXml(List<InfoDimen> pData, String pPath){
        XmlSerializer tXmlSerializer = new KXmlSerializer();
        File tFile = new File(pPath);
        FileOutputStream tFos = null;
        try {
            tFos = new FileOutputStream(tFile);
            tXmlSerializer.setOutput(tFos, "utf-8");
            //缩进  
            tXmlSerializer.setFeature(  
                    "http://xmlpull.org/v1/doc/features.html#indent-output",  
                    true);
            tXmlSerializer.startDocument("UTF-8", true);
            tXmlSerializer.startTag(null, "resources");
            for(InfoDimen tInfo : pData){
                tXmlSerializer.startTag(null, "dimen");
                tXmlSerializer.attribute(null, "name", tInfo.name);
                tXmlSerializer.text(tInfo.value);
                tXmlSerializer.endTag(null, "dimen");
            }
            tXmlSerializer.endTag(null, "resources");
            tXmlSerializer.endDocument();
            tXmlSerializer.flush();
            tFos.close();
        } catch (Exception e) {
            e.printStackTrace();
            try {
                tFos.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
        
    }
}
