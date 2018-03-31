/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.backend.dao.user;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.vn.ntsc.backend.Config;
import com.vn.ntsc.backend.dao.DBManager;
import com.vn.ntsc.backend.dao.file.FileDAO;
import com.vn.ntsc.backend.dao.thumbnail.ThumbNailDAO;
import com.vn.ntsc.backend.entity.IEntity;
import com.vn.ntsc.backend.entity.impl.datarespond.SizedListData;
import com.vn.ntsc.backend.entity.impl.video.Video;
import com.vn.ntsc.backend.entity.impl.video.FileInfo;
import eazycommon.constant.Constant;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.FilesAndFolders;
import eazycommon.constant.mongokey.UserdbKey;
import eazycommon.exception.EazyException;
import eazycommon.util.DateFormat;
import eazycommon.util.Util;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author hoangnh
 */
public class VideoDAO {
    private static DBCollection coll;

    static {
        try {
            coll = DBManager.getUserDB().getCollection(UserdbKey.USER_VIDEO_COLLECTION);
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
    }
    
    public static SizedListData searchReportedVideo(Long type, Long sort, Long order, int skip, int take) throws EazyException {
        SizedListData result = new SizedListData();
        try {
            DBObject findObject = new BasicDBObject();
            if (type == null) {
                return null;
            }
            findObject.put(UserdbKey.USER_VIDEO.REPORT_FLAG, type.intValue());
            if (order == null) {
                return null;
            }
            int or = -1;
            if (order == Constant.FLAG.ON) {
                or = 1;
            }
            if (sort == null) {
                return null;
            }
//            DBObject neObj = new BasicDBObject("$ne", 0);
//            findObject.put(UserdbKey.USER_VIDEO.IMAGE_TYPE, neObj);
            findObject.put(UserdbKey.USER_VIDEO.FLAG, Constant.FLAG.ON);
            BasicDBObject sortObj = new BasicDBObject();
            if (sort == 1) {
                sortObj.append(UserdbKey.USER_VIDEO.REPORT_TIME, or);
            } else if (sort == 2) {
                sortObj.append(UserdbKey.USER_VIDEO.REPORT_NUMBER, or);
            }
            DBCursor cursor = coll.find(findObject).sort(sortObj);
            //set to list
            Integer number = cursor.size();
//            cursor = cursor.skip(skip).limit(take);
            cursor = cursor.limit(take+skip);
            List<IEntity> list = new ArrayList<>();
            while (cursor.hasNext()) {
                DBObject dbObject = cursor.next();
                Video video = new Video();
                String videoId = (String) dbObject.get(UserdbKey.USER_VIDEO.VIDEO_ID);
                video.videoId = videoId;
                String userId = (String) dbObject.get(UserdbKey.USER_VIDEO.USER_ID);
                video.userId = userId;
//                Integer imageType = (Integer) dbObject.get(UserdbKey.USER_VIDEO.IMAGE_TYPE);
//                image.imageType = imageType;
                Long uploadTime = (Long) dbObject.get(UserdbKey.USER_VIDEO.UPLOAD_TIME);
                if (uploadTime != null) {
                    video.uploadTimeStr = DateFormat.format(uploadTime);
                }
                Integer reportNumber = (Integer) dbObject.get(UserdbKey.USER_VIDEO.REPORT_NUMBER);
                video.reportNumber = reportNumber;
                Long reportTime = (Long) dbObject.get(UserdbKey.USER_VIDEO.REPORT_TIME);
                if (reportTime != null) {
                    video.reportTimeStr = DateFormat.format(reportTime);
                }
                video.fileType = "video";
                Integer videoPrivacy = (Integer) dbObject.get("video_type");
                video.privacy = videoPrivacy;
                String url = FileDAO.getFileUrl(videoId);

                if(url.contains("http")){
                    video.url = url;
                    if(url.contains("mp3")){
                        String thumnail = ImageDAO.getImageUrl(videoId);
                        if(thumnail != null){
                            video.thumbNail = thumnail;
                        }
                    }
                    if(url.contains("mp4")){
                        String thumnail = ThumbNailDAO.getThumbnailUrl(videoId);
                        if(thumnail != null){
                            video.thumbNail = thumnail;
                        }
                    }
                }else{
                    video.url = Config.STREAMING_HOST + FilesAndFolders.FOLDERS.FILE +url;
                    if(url.contains("mp3")){
                        String thumnail = ImageDAO.getImageUrl(videoId);
                        if(thumnail != null){
                            video.thumbNail = Config.STREAMING_HOST + FilesAndFolders.FOLDERS.THUMBNAIL_IMAGE+thumnail;
                        }
                    }
                    if(url.contains("mp4")){
                        String thumnail = ThumbNailDAO.getThumbnailUrl(videoId);
                        if(thumnail != null){
                            video.thumbNail = Config.STREAMING_HOST + FilesAndFolders.FOLDERS.THUMBNAIL_IMAGE+thumnail;
                        }
                    }
                }
                list.add(video);

            }
            result = new SizedListData(number, list);
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }
    
    public static FileInfo getVideoInfor(String videoId) throws EazyException {
        FileInfo result = new FileInfo();
        try {
            BasicDBObject seachObj = new BasicDBObject(UserdbKey.USER_VIDEO.VIDEO_ID, videoId);
            DBObject obj = coll.findOne(seachObj);
            if (obj != null) {
                String userId = (String) obj.get(UserdbKey.USER_VIDEO.USER_ID);
                Integer videoType = (Integer) obj.get(UserdbKey.USER_VIDEO.VIDEO_TYPE);
                Integer videoStatus = (Integer) obj.get(UserdbKey.USER_VIDEO.STATUS);
                Integer flag = (Integer) obj.get(UserdbKey.USER_VIDEO.FLAG);
                Long uploadTime = (Long) obj.get(UserdbKey.USER_VIDEO.UPLOAD_TIME);
                Long reportTime = (Long) obj.get(UserdbKey.USER_VIDEO.REPORT_TIME);
                Integer reportFlag = (Integer) obj.get(UserdbKey.USER_VIDEO.REPORT_FLAG);
                
                result.userId = userId;
                result.fileType = videoType;
                result.fileStatus = videoStatus;
                result.flag = flag;
                result.uploadTime = uploadTime;
                result.reportFlag = reportFlag;
            } else {
                Util.addInfoLog("Video not found it : " + videoId);
            }
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }
    
    public static boolean updateReportFlag(String videoId, int flag) throws EazyException{
        boolean result = false;
        try {
            BasicDBObject seachObj = new BasicDBObject(UserdbKey.USER_VIDEO.VIDEO_ID, videoId);
            BasicDBObject updateObj = new BasicDBObject(UserdbKey.USER_VIDEO.REPORT_FLAG, flag);
            BasicDBObject setObj = new BasicDBObject("$set", updateObj);
            coll.update(seachObj, setObj);
            result = true;
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
        
    }
}
