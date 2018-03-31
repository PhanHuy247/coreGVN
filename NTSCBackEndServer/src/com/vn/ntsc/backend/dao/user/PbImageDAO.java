/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.backend.dao.user;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import eazycommon.constant.Constant;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.mongokey.UserdbKey;
import eazycommon.exception.EazyException;
import eazycommon.util.Util;
import org.bson.types.ObjectId;
import com.vn.ntsc.backend.dao.DBManager;

/**
 *
 * @author RuAc0n
 */
public class PbImageDAO {

    private static DBCollection coll;

    static {
        try {
            coll = DBManager.getUserDB().getCollection(UserdbKey.PB_IMAGE_COLLECTION);
        } catch (Exception ex) {
            Util.addErrorLog(ex);            
        }
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
                        if (imageId.equals(imgId) && flag == Constant.AVAILABLE_FLAG_VALUE.AVAILABE_FLAG) {
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
