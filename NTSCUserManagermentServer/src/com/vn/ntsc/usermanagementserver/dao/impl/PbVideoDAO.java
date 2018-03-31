/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.usermanagementserver.dao.impl;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.vn.ntsc.usermanagementserver.dao.DatabaseLoader;
import com.vn.ntsc.usermanagementserver.entity.impl.datarespond.PublicFile;
import com.vn.ntsc.usermanagementserver.entity.impl.image.PublicImage;
import com.vn.ntsc.usermanagementserver.entity.impl.video.PublicVideo;
import eazycommon.constant.Constant;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.mongokey.UserdbKey;
import eazycommon.exception.EazyException;
import eazycommon.util.Util;
import java.util.ArrayList;
import java.util.List;
import org.bson.types.ObjectId;

/**
 *
 * @author Phan Huy
 */
public class PbVideoDAO {
     private static DBCollection coll;
    
    static {
        try {
            coll = DatabaseLoader.getUserDB().getCollection(UserdbKey.PB_VIDEO_COLLECTION);
        } catch (Exception ex) {
            //Future logging here
            Util.addErrorLog(ex);            
           
        }
    }
    
    public static boolean addPublicVideo(String userId, String videoId, String buzzId, int flag, int privacy) throws EazyException {
        boolean result = false;
        try {
            ObjectId id = new ObjectId(userId);
            BasicDBObject updateQuery = new BasicDBObject(UserdbKey.PB_VIDEO.ID, id);
            BasicDBObject videoElement = new BasicDBObject(UserdbKey.PB_VIDEO.VIDEO_ID, videoId);
            videoElement.append(UserdbKey.PB_VIDEO.BUZZ_ID, buzzId);
            videoElement.append(UserdbKey.PB_VIDEO.FLAG, flag);
            videoElement.append(UserdbKey.PB_VIDEO.PRIVACY, privacy);
            BasicDBObject pbVideo = new BasicDBObject(UserdbKey.PB_VIDEO.PB_VIDEO_LIST, videoElement);
            BasicDBObject updateCommand = new BasicDBObject("$push", pbVideo);
            coll.update(updateQuery, updateCommand, true, false);
            result = true;
        }catch (Exception ex) {
            Util.addErrorLog(ex);            
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }
    
    public static List<PublicVideo> getPublicVideo(String userId, long skip, long take) throws EazyException {
        List<PublicVideo> result = new ArrayList<>();
        try {
            ObjectId id = new ObjectId(userId);
            BasicDBObject updateQuery = new BasicDBObject(UserdbKey.PB_VIDEO.ID, id);
            DBObject respondObj = coll.findOne(updateQuery);
            if (respondObj != null) {
                BasicDBList videoList = (BasicDBList) respondObj.get(UserdbKey.PB_VIDEO.PB_VIDEO_LIST);
                if (!videoList.isEmpty()) {
                    List<PublicVideo> list = new ArrayList<>();
                    for (long i = videoList.size() -1; i > -1; i--) {
                        BasicDBObject video = (BasicDBObject) videoList.get((int) i);
                        String videoId = video.getString(UserdbKey.PB_VIDEO.VIDEO_ID);
                        String buzzId = video.getString(UserdbKey.PB_IMAGE.BUZZ_ID);
                        Integer flag = video.getInt(UserdbKey.PB_IMAGE.FLAG);
                        if(flag == Constant.FLAG.ON)
                            list.add(new PublicVideo(videoId, buzzId));
                    }
                    if (skip > list.size()) {
                        return result;
                    }
                    long startIndex = skip;
                    long endIndex = skip + take;
                    if (endIndex > list.size()) {
                        endIndex = list.size();
                    }
                    result = list.subList((int)startIndex, (int)endIndex);
                }
            }
        }catch (Exception ex) {
            Util.addErrorLog(ex);            
           
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }
    
    public static List<PublicFile> getTimelineVideo(String userId, Boolean isCurrentUser) throws EazyException{
        List<PublicFile> result = new ArrayList<>();
        try{
            ObjectId id = new ObjectId(userId);
            BasicDBObject findObj = new BasicDBObject();
            findObj.append(UserdbKey.PB_VIDEO.ID, id);
            DBObject data = coll.findOne(findObj);
            if(data != null){
                BasicDBList videoList = (BasicDBList) data.get(UserdbKey.PB_VIDEO.PB_VIDEO_LIST);
                if (!videoList.isEmpty()) {
                    for (long i = videoList.size() -1; i > -1; i--) {
                        BasicDBObject video = (BasicDBObject) videoList.get((int) i);
                        String videoId = video.getString(UserdbKey.PB_VIDEO.VIDEO_ID);
                        String buzzId = video.getString(UserdbKey.PB_VIDEO.BUZZ_ID);
                        Integer flag = video.getInt(UserdbKey.PB_VIDEO.FLAG);
                        Integer privacy = (Integer) video.get(UserdbKey.PB_VIDEO.PRIVACY);
                        Long time = 0L;
                        if(video.get(UserdbKey.PB_VIDEO.TIME) != null){
                            time = video.getLong(UserdbKey.PB_VIDEO.TIME);
                        }
                        if(flag == Constant.FLAG.ON && (privacy == null || privacy == Constant.POST_MODE.EVERYONE || isCurrentUser))
                            result.add(new PublicFile(videoId, buzzId, time));
                    }
                }
            }
        }catch(Exception ex){
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static boolean checkPbVideoExist(String userId, String vidIdvidId) throws EazyException {
        boolean result = false;
        try {
            ObjectId id = new ObjectId(userId);
            BasicDBObject updateQuery = new BasicDBObject(UserdbKey.PB_VIDEO.ID, id);
            DBObject obj = coll.findOne(updateQuery);
            if (obj != null) {
                BasicDBList list = (BasicDBList) obj.get(UserdbKey.PB_VIDEO.PB_VIDEO_LIST);
                if (list != null) {
                    for (int i = 0; i < list.size(); i++) {
                        BasicDBObject video = (BasicDBObject) list.get((int) i);
                        String vidId = video.getString(UserdbKey.PB_VIDEO.VIDEO_ID);
                        int flag = video.getInt(UserdbKey.PB_IMAGE.FLAG);
                        if (vidId.equals(vidId) && flag == Constant.FLAG.ON) {
                            result = true;
                            break;
                        }
                    }
                }
            }
        }catch (Exception ex) {
            Util.addErrorLog(ex);            
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }
    
    public static boolean removePublicVideoByVideoId(String userId, String videoId) throws EazyException {
        boolean result = false;
        try {
            ObjectId id = new ObjectId(userId);
            BasicDBObject updateQuery = new BasicDBObject(UserdbKey.PB_VIDEO.ID, id);
            BasicDBObject obj = new BasicDBObject(UserdbKey.PB_VIDEO.VIDEO_ID, videoId);
            BasicDBObject pbVideo = new BasicDBObject(UserdbKey.PB_VIDEO.PB_VIDEO_LIST, obj);
            BasicDBObject updateCommand = new BasicDBObject("$pull", pbVideo);
            coll.update(updateQuery, updateCommand);
            result = true;
        }catch (Exception ex) {
            Util.addErrorLog(ex);            
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }    
    
    public static boolean updateFlag(String userId,String videoId, int flag) throws EazyException{
        boolean result = false;
        try{
            ObjectId id = new ObjectId(userId);
            BasicDBObject findObj = new BasicDBObject(UserdbKey.PB_VIDEO.ID, id);
            BasicDBObject imageObj = new BasicDBObject(UserdbKey.PB_VIDEO.VIDEO_ID, videoId);
            BasicDBObject elemMatch = new BasicDBObject("$elemMatch", imageObj);
            findObj.append(UserdbKey.PB_VIDEO.PB_VIDEO_LIST, elemMatch);
            DBObject obj = coll.findOne(findObj);
            if (obj != null) {
                String field = UserdbKey.PB_VIDEO.PB_VIDEO_LIST + ".$." + UserdbKey.PB_VIDEO.FLAG;
                BasicDBObject updateObj = new BasicDBObject(field, flag);
                BasicDBObject setObj = new BasicDBObject("$set", updateObj);
                coll.update(findObj, setObj);
            }                       
            result = true;
        }catch(Exception ex){
            Util.addErrorLog(ex);            
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }   
}
