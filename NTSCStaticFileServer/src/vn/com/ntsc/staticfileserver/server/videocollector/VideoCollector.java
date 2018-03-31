
package vn.com.ntsc.staticfileserver.server.videocollector;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import eazycommon.constant.Constant;
import eazycommon.util.Util;
import vn.com.ntsc.staticfileserver.Config;
import vn.com.ntsc.staticfileserver.dao.impl.FileDAO;
import vn.com.ntsc.staticfileserver.entity.file.FileInfo;
import vn.com.ntsc.staticfileserver.server.respond.common.Helper;

/**
 *
 * @author HuyDX
 */
public class VideoCollector extends Thread{
    private static final long EXPIRE_TIME = 30L * Constant.A_DAY * Config.VIDEO_EXPIRED_MONTHS;
    //private static final long EXPIRE_TIME_TEST_MODE = Constant.A_MINUTE * 10;
    private static final Map<String, FileInfo> VIDEO_MAP = new ConcurrentHashMap<>();
    
    public static void startVideoCollector(){
        try {
            Map<String, FileInfo> map = FileDAO.getAll();
            VIDEO_MAP.putAll(map);        
            VideoCollector collector = new VideoCollector();
            collector.start();
        } catch (Exception ex){
            Util.addErrorLog(ex);
        }
    }
    
    @Override
    public void run(){
        try {
            while (true){
                Util.addInfoLog("start Video Collector Service");
                collectExpiredVideo();
                Thread.sleep(Constant.AN_HOUR);                
//                if (Config.DELETED_VIDEO_TEST_MODE)
//                    Thread.sleep(Constant.A_MINUTE * 5);
//                else 
            }
        } catch (InterruptedException ex) {
            Util.addErrorLog(ex);
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
    }

    private void collectExpiredVideo() throws Exception {
        List<String> removeFiles = new ArrayList<>();
        for (Map.Entry<String,FileInfo> entry: VIDEO_MAP.entrySet()){
            String filePath = entry.getValue().getFilePath();
            if (Helper.isVideoFile(filePath)){
                if (isFileExpired(entry.getValue().getDateCreated())){
                    VIDEO_MAP.remove(entry.getKey());
                    removeFiles.add(entry.getKey());
                    Util.deleteFile(filePath);
                    Util.addInfoLog("VideoCollector thread removed file with Key = " + entry.getKey() + " filePath = " + filePath + " dateCreated = " + entry.getValue().getDateCreated());
                }
            } else {
                VIDEO_MAP.remove(entry.getKey());
            }
        }
        FileDAO.removeDocuments(removeFiles);
    }

    private boolean isFileExpired(long dateCreated) {    
        long timeInterval = System.currentTimeMillis() - dateCreated;
        return (timeInterval > EXPIRE_TIME);        
//        if (Config.DELETED_VIDEO_TEST_MODE)
//            return (timeInterval > EXPIRE_TIME_TEST_MODE);
//        else 
    }
    
    public static FileInfo put(String fileId, FileInfo fileInfo){
//        Util.addInfoLog("Put " + fileId + ", FilePath: " + fileInfo.getFilePath() + ", dateCreated: "+new Date(fileInfo.getDateCreated()));
        return VIDEO_MAP.put(fileId, fileInfo);
    }
}
