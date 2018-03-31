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
import com.vn.ntsc.backend.entity.impl.audio.Audio;
import com.vn.ntsc.backend.entity.impl.datarespond.SizedListData;
import com.vn.ntsc.backend.entity.impl.video.FileInfo;
import com.vn.ntsc.backend.entity.impl.video.Video;
import eazycommon.constant.Constant;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.FilesAndFolders;
import eazycommon.constant.mongokey.UserdbKey;
import eazycommon.exception.EazyException;
import eazycommon.util.DateFormat;
import eazycommon.util.Util;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author hoangnh
 */
public class AudioDAO {
    private static DBCollection coll;

    static {
        try {
            coll = DBManager.getUserDB().getCollection(UserdbKey.USER_AUDIO_COLLECTION);
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
    }
    
    public static SizedListData searchReportedAudio(Long type, Long sort, Long order, int skip, int take) throws EazyException {
        SizedListData result = new SizedListData();
        try {
            DBObject findObject = new BasicDBObject();
            if (type == null) {
                return null;
            }
            findObject.put(UserdbKey.USER_AUDIO.REPORT_FLAG, type.intValue());
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
            findObject.put(UserdbKey.USER_AUDIO.FLAG, Constant.FLAG.ON);
            BasicDBObject sortObj = new BasicDBObject();
            if (sort == 1) {
                sortObj.append(UserdbKey.USER_AUDIO.REPORT_TIME, or);
            } else if (sort == 2) {
                sortObj.append(UserdbKey.USER_AUDIO.REPORT_NUMBER, or);
            }
            DBCursor cursor = coll.find(findObject).sort(sortObj);
            //set to list
            Integer number = cursor.size();
//            cursor = cursor.skip(skip).limit(take);
            cursor = cursor.limit(take+skip);
            List<IEntity> list = new ArrayList<>();
            while (cursor.hasNext()) {
                DBObject dbObject = cursor.next();
                Audio audio = new Audio();
                String audioId = (String) dbObject.get(UserdbKey.USER_AUDIO.AUDIO_ID);
                audio.audioId = audioId;
                String userId = (String) dbObject.get(UserdbKey.USER_AUDIO.USER_ID);
                audio.userId = userId;
//                Integer imageType = (Integer) dbObject.get(UserdbKey.USER_VIDEO.IMAGE_TYPE);
//                image.imageType = imageType;
                Long uploadTime = (Long) dbObject.get(UserdbKey.USER_AUDIO.UPLOAD_TIME);
                if (uploadTime != null) {
                    audio.uploadTimeStr = DateFormat.format(uploadTime);
                }
                Integer reportNumber = (Integer) dbObject.get(UserdbKey.USER_AUDIO.REPORT_NUMBER);
                audio.reportNumber = reportNumber;
                Long reportTime = (Long) dbObject.get(UserdbKey.USER_AUDIO.REPORT_TIME);
                if (reportTime != null) {
                    audio.reportTimeStr = DateFormat.format(reportTime);
                }
                list.add(audio);

            }
            result = new SizedListData(number, list);
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }
    
    public static SizedListData searchReportedAudioWithVideo(Long type, Long sort, Long order, int skip, int take) throws EazyException {
        SizedListData result = new SizedListData();
        try {
            DBObject findObject = new BasicDBObject();
            if (type == null) {
                return null;
            }
            findObject.put(UserdbKey.USER_AUDIO.REPORT_FLAG, type.intValue());
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
            findObject.put(UserdbKey.USER_AUDIO.FLAG, Constant.FLAG.ON);
            BasicDBObject sortObj = new BasicDBObject();
            if (sort == 1) {
                sortObj.append(UserdbKey.USER_AUDIO.REPORT_TIME, or);
            } else if (sort == 2) {
                sortObj.append(UserdbKey.USER_AUDIO.REPORT_NUMBER, or);
            }
            DBCursor cursor = coll.find(findObject).sort(sortObj);
            //set to list
            Integer number = cursor.size();
//            cursor = cursor.skip(skip).limit(take);
            cursor = cursor.limit(take+skip);
            List<IEntity> list = new ArrayList<>();
            while (cursor.hasNext()) {
                DBObject dbObject = cursor.next();
                Video audio = new Video();
                String audioId = (String) dbObject.get(UserdbKey.USER_AUDIO.AUDIO_ID);
                Util.addDebugLog("audioId====="+audioId);
                audio.videoId = audioId;
                String userId = (String) dbObject.get(UserdbKey.USER_AUDIO.USER_ID);
                audio.userId = userId;
//                Integer imageType = (Integer) dbObject.get(UserdbKey.USER_VIDEO.IMAGE_TYPE);
//                image.imageType = imageType;
                Long uploadTime = (Long) dbObject.get(UserdbKey.USER_AUDIO.UPLOAD_TIME);
                if (uploadTime != null) {
                    audio.uploadTimeStr = DateFormat.format(uploadTime);
                }
                Integer reportNumber = (Integer) dbObject.get(UserdbKey.USER_AUDIO.REPORT_NUMBER);
                audio.reportNumber = reportNumber;
                Long reportTime = (Long) dbObject.get(UserdbKey.USER_AUDIO.REPORT_TIME);
                if (reportTime != null) {
                    audio.reportTimeStr = DateFormat.format(reportTime);
                }
                audio.fileType = "audio";
                Integer audioPrivacy = (Integer) dbObject.get("audio_type");
                audio.privacy = audioPrivacy;
                String url = FileDAO.getFileUrl(audioId);
                if(url != null && !url.equals("")){
                    audio.url = Config.STREAMING_HOST + FilesAndFolders.FOLDERS.FILE +url;
                    if(url.contains("mp3")){
                        String thumnail = ImageDAO.getImageUrl(audioId);
                        if(thumnail != null){
                            audio.thumbNail = Config.STREAMING_HOST + FilesAndFolders.FOLDERS.THUMBNAIL_IMAGE+thumnail;
                        }
                    }
                    if(url.contains("mp4")){
                        String thumnail = ThumbNailDAO.getThumbnailUrl(audioId);
                        if(thumnail != null){
                            audio.thumbNail = Config.STREAMING_HOST + FilesAndFolders.FOLDERS.THUMBNAIL_IMAGE+thumnail;
                        }
                    }
                    list.add(audio);
                }
            }
            result = new SizedListData(number, list);
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }
    
    public static FileInfo getAudioInfor(String audioId) throws EazyException {
        FileInfo result = new FileInfo();
        try {
            BasicDBObject seachObj = new BasicDBObject(UserdbKey.USER_AUDIO.AUDIO_ID, audioId);
            DBObject obj = coll.findOne(seachObj);
            if (obj != null) {
                String userId = (String) obj.get(UserdbKey.USER_AUDIO.USER_ID);
                Integer videoType = (Integer) obj.get(UserdbKey.USER_AUDIO.AUDIO_TYPE);
                Integer videoStatus = (Integer) obj.get(UserdbKey.USER_AUDIO.STATUS);
                Integer flag = (Integer) obj.get(UserdbKey.USER_AUDIO.FLAG);
                Long uploadTime = (Long) obj.get(UserdbKey.USER_AUDIO.UPLOAD_TIME);
                Long reportTime = (Long) obj.get(UserdbKey.USER_AUDIO.REPORT_TIME);
                Integer reportFlag = (Integer) obj.get(UserdbKey.USER_AUDIO.REPORT_FLAG);
                
                result.userId = userId;
                result.fileType = videoType;
                result.fileStatus = videoStatus;
                result.flag = flag;
                result.uploadTime = uploadTime;
                result.reportFlag = reportFlag;
            } else {
                Util.addInfoLog("Audio not found : " + audioId);
            }
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }
    
    public static boolean updateReportFlag(String audioId, int flag) throws EazyException{
        boolean result = false;
        try {
            BasicDBObject seachObj = new BasicDBObject(UserdbKey.USER_AUDIO.AUDIO_ID, audioId);
            BasicDBObject updateObj = new BasicDBObject(UserdbKey.USER_AUDIO.REPORT_FLAG, flag);
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
