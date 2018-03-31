/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.buzzserver.dao.impl;

import eazycommon.util.Util;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.vn.ntsc.buzzserver.dao.DAO;
import java.util.ArrayList;
import java.util.List;
import eazycommon.constant.Constant;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.mongokey.UserdbKey;
import eazycommon.exception.EazyException;
import org.bson.types.ObjectId;

/**
 *
 * @author RuAc0n
 */
public class PbImageDAO {

    private static DBCollection coll;
    
    static {
        try {
            coll = DAO.getUserDB().getCollection(UserdbKey.PB_IMAGE_COLLECTION);
        } catch (Exception ex) {
            //Future logging here
            Util.addErrorLog(ex);            
           
        }
    }

    public static boolean addPublicImage(String userId, String imageId, String buzzId, int flag, int privacy) throws EazyException {
        boolean result = false;
        try {
            ObjectId id = new ObjectId(userId);
            BasicDBObject updateQuery = new BasicDBObject(UserdbKey.PB_IMAGE.ID, id);
            BasicDBObject imageElement = new BasicDBObject(UserdbKey.PB_IMAGE.IMAGE_ID, imageId);
            imageElement.append(UserdbKey.PB_IMAGE.BUZZ_ID, buzzId);
            imageElement.append(UserdbKey.PB_IMAGE.FLAG, flag);
            imageElement.append(UserdbKey.PB_IMAGE.PRIVACY, privacy);
            BasicDBObject pbImage = new BasicDBObject(UserdbKey.PB_IMAGE.PB_IMAGE_LIST, imageElement);
            BasicDBObject updateCommand = new BasicDBObject("$push", pbImage);
            coll.update(updateQuery, updateCommand, true, false);
            result = true;
        }catch (Exception ex) {
            Util.addErrorLog(ex);            
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static boolean removePublicImage(String userId, String buzzId) throws EazyException {
        boolean result = false;
        try {
            ObjectId id = new ObjectId(userId);
            BasicDBObject updateQuery = new BasicDBObject(UserdbKey.PB_IMAGE.ID, id);
            BasicDBObject obj = new BasicDBObject(UserdbKey.PB_IMAGE.BUZZ_ID, buzzId);
            BasicDBObject pbImage = new BasicDBObject(UserdbKey.PB_IMAGE.PB_IMAGE_LIST, obj);
            BasicDBObject updateCommand = new BasicDBObject("$pull", pbImage);
            coll.update(updateQuery, updateCommand);
            result = true;
        }catch (Exception ex) {
            Util.addErrorLog(ex);            
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static boolean removePublicImageByImageId(String userId, String imageId) throws EazyException {
        boolean result = false;
        try {
            ObjectId id = new ObjectId(userId);
            BasicDBObject updateQuery = new BasicDBObject(UserdbKey.PB_IMAGE.ID, id);
            BasicDBObject obj = new BasicDBObject(UserdbKey.PB_IMAGE.IMAGE_ID, imageId);
            BasicDBObject pbImage = new BasicDBObject(UserdbKey.PB_IMAGE.PB_IMAGE_LIST, obj);
            BasicDBObject updateCommand = new BasicDBObject("$pull", pbImage);
            coll.update(updateQuery, updateCommand);
            result = true;
        }catch (Exception ex) {
            Util.addErrorLog(ex);            
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }    
    
    public static boolean checkPbImageExist(String userId, String imageId) throws EazyException {
        boolean result = false;
        try {
            ObjectId id = new ObjectId(userId);
            BasicDBObject updateQuery = new BasicDBObject(UserdbKey.PB_IMAGE.ID, id);
            DBObject obj = coll.findOne(updateQuery);
            if (obj != null) {
                BasicDBList list = (BasicDBList) obj.get(UserdbKey.PB_IMAGE.PB_IMAGE_LIST);
                if (list != null) {
                    for (int i = 0; i < list.size(); i++) {
                        BasicDBObject image = (BasicDBObject) list.get((int) i);
                        String imgId = image.getString(UserdbKey.PB_IMAGE.IMAGE_ID);
                        int flag = image.getInt(UserdbKey.PB_IMAGE.FLAG);
                        if (imageId.equals(imgId) && flag == Constant.FLAG.ON) {
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

    public static String getImageId(String userId, String buzzId) throws EazyException {
        String result = null;
        try {
            ObjectId id = new ObjectId(userId);
            BasicDBObject updateQuery = new BasicDBObject(UserdbKey.PB_IMAGE.ID, id);
            DBObject respondObj = coll.findOne(updateQuery);
            if (respondObj != null) {
                BasicDBList imageList = (BasicDBList) respondObj.get(UserdbKey.PB_IMAGE.PB_IMAGE_LIST);
                if (!imageList.isEmpty()) {
                    for (int i = 0; i < imageList.size(); i++) {
                        BasicDBObject image = (BasicDBObject) imageList.get(i);
                        String imageId = image.getString(UserdbKey.PB_IMAGE.IMAGE_ID);
                        String buId = image.getString(UserdbKey.PB_IMAGE.BUZZ_ID);
                        if (buId.equals(buzzId)) {
                            result = imageId;
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
    
    public static String getBuzzId(String userId, String imageId) throws EazyException {
        String result = null;
        try {
            ObjectId id = new ObjectId(userId);
            BasicDBObject updateQuery = new BasicDBObject(UserdbKey.PB_IMAGE.ID, id);
            DBObject respondObj = coll.findOne(updateQuery);
            if (respondObj != null) {
                BasicDBList imageList = (BasicDBList) respondObj.get(UserdbKey.PB_IMAGE.PB_IMAGE_LIST);
                if (!imageList.isEmpty()) {
                    for (int i = 0; i < imageList.size(); i++) {
                        BasicDBObject image = (BasicDBObject) imageList.get(i);
                        String iId = image.getString(UserdbKey.PB_IMAGE.IMAGE_ID);
                        String buId = image.getString(UserdbKey.PB_IMAGE.BUZZ_ID);
                        if (iId.equals(imageId)) {
                            result = buId;
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
            BasicDBObject imageObj = new BasicDBObject(UserdbKey.PB_IMAGE.IMAGE_ID, imageId);
            BasicDBObject elemMatch = new BasicDBObject("$elemMatch", imageObj);
            findObj.append(UserdbKey.PB_IMAGE.PB_IMAGE_LIST, elemMatch);
            DBObject obj = coll.findOne(findObj);
            if (obj != null) {
                String field = UserdbKey.PB_IMAGE.PB_IMAGE_LIST + ".$." + UserdbKey.PB_IMAGE.FLAG;
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
