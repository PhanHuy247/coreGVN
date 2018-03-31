/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.buzzserver.dao.impl;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.vn.ntsc.buzzserver.dao.DAO;
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
            coll = DAO.getUserDB().getCollection(UserdbKey.PB_VIDEO_COLLECTION);
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
    
    public static boolean updateFlag(String userId,String imageId, int flag) throws EazyException{
        boolean result = false;
        try{
            ObjectId id = new ObjectId(userId);
            BasicDBObject findObj = new BasicDBObject(UserdbKey.PB_IMAGE.ID, id);
            BasicDBObject imageObj = new BasicDBObject(UserdbKey.PB_VIDEO.VIDEO_ID, imageId);
            BasicDBObject elemMatch = new BasicDBObject("$elemMatch", imageObj);
            findObj.append(UserdbKey.PB_VIDEO.PB_VIDEO_LIST, elemMatch);
            DBObject obj = coll.findOne(findObj);
            if (obj != null) {
                String field = UserdbKey.PB_VIDEO.PB_VIDEO_LIST + ".$." + UserdbKey.PB_IMAGE.FLAG;
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
}
