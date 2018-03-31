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
public class PbAudioDAO {
     private static DBCollection coll;
    
    static {
        try {
            coll = DAO.getUserDB().getCollection(UserdbKey.PB_AUDIO_COLLECTION);
        } catch (Exception ex) {
            //Future logging here
            Util.addErrorLog(ex);            
           
        }
    }
    
    public static boolean addPublicAudio(String userId, String audioId, String buzzId, int flag,String coverId, int privacy) throws EazyException {
        boolean result = false;
        try {
            ObjectId id = new ObjectId(userId);
            BasicDBObject updateQuery = new BasicDBObject(UserdbKey.PB_AUDIO.ID, id);
            BasicDBObject audioElement = new BasicDBObject(UserdbKey.PB_AUDIO.AUDIO_ID, audioId);
            audioElement.append(UserdbKey.PB_AUDIO.BUZZ_ID, buzzId);
            audioElement.append(UserdbKey.PB_AUDIO.FLAG, flag);
            audioElement.append(UserdbKey.PB_AUDIO.COVER, coverId);
            audioElement.append(UserdbKey.PB_AUDIO.PRIVACY, privacy);
            BasicDBObject pbAudio = new BasicDBObject(UserdbKey.PB_AUDIO.PB_AUDIO_LIST, audioElement);
            BasicDBObject updateCommand = new BasicDBObject("$push", pbAudio);
            coll.update(updateQuery, updateCommand, true, false);
            result = true;
        }catch (Exception ex) {
            Util.addErrorLog(ex);            
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static boolean checkPbAudioExist(String userId, String audioId) throws EazyException {
        boolean result = false;
        try {
            ObjectId id = new ObjectId(userId);
            BasicDBObject updateQuery = new BasicDBObject(UserdbKey.PB_VIDEO.ID, id);
            DBObject obj = coll.findOne(updateQuery);
            if (obj != null) {
                BasicDBList list = (BasicDBList) obj.get(UserdbKey.PB_AUDIO.PB_AUDIO_LIST);
                if (list != null) {
                    for (int i = 0; i < list.size(); i++) {
                        BasicDBObject audio = (BasicDBObject) list.get((int) i);
                        String auId = audio.getString(UserdbKey.PB_AUDIO.AUDIO_ID);
                        int flag = audio.getInt(UserdbKey.PB_IMAGE.FLAG);
                        if (auId.equals(audioId) && flag == Constant.FLAG.ON) {
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
            BasicDBObject imageObj = new BasicDBObject(UserdbKey.PB_AUDIO.AUDIO_ID, imageId);
            BasicDBObject elemMatch = new BasicDBObject("$elemMatch", imageObj);
            findObj.append(UserdbKey.PB_AUDIO.PB_AUDIO_LIST, elemMatch);
            DBObject obj = coll.findOne(findObj);
            if (obj != null) {
                String field = UserdbKey.PB_AUDIO.PB_AUDIO_LIST + ".$." + UserdbKey.PB_IMAGE.FLAG;
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
    
    public static boolean removePublicAudioByAudioId(String userId, String audioId) throws EazyException {
        boolean result = false;
        try {
            ObjectId id = new ObjectId(userId);
            BasicDBObject updateQuery = new BasicDBObject(UserdbKey.PB_AUDIO.ID, id);
            BasicDBObject obj = new BasicDBObject(UserdbKey.PB_AUDIO.AUDIO_ID, audioId);
            BasicDBObject pbVideo = new BasicDBObject(UserdbKey.PB_AUDIO.PB_AUDIO_LIST, obj);
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
