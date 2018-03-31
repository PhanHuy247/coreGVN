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
public class BackstageDAO {

    private static DBCollection coll;

    static {
        try {
            coll = DBManager.getUserDB().getCollection(UserdbKey.BACKSTAGE_COLLECTION);
        } catch (Exception ex) {
            Util.addErrorLog(ex);            
        }
    }

    public static boolean checkBackStageExist(String userId, String imageId) throws EazyException {
        boolean result = false;
        try {
            ObjectId id = new ObjectId(userId);
            BasicDBObject updateQuery = new BasicDBObject(UserdbKey.BACKSTAGE.ID, id);
            DBObject obj = coll.findOne(updateQuery);
            if (obj != null) {
                BasicDBList list = (BasicDBList) obj.get(UserdbKey.BACKSTAGE.BACKSTAGE_LIST);
                if (list != null) {
                    for (int i = 0; i < list.size(); i++) {
                        BasicDBObject image = (BasicDBObject) list.get((int) i);
                        String imgId = image.getString(UserdbKey.BACKSTAGE.IMAGE_ID);
                        int flag = image.getInt(UserdbKey.BACKSTAGE.FLAG);
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

    public static boolean removeBackStage(String userId, String imageId) throws EazyException {
        boolean result = false;
        try {
            ObjectId id = new ObjectId(userId);
            BasicDBObject updateQuery = new BasicDBObject(UserdbKey.BACKSTAGE.ID, id);
            BasicDBObject obj = new BasicDBObject(UserdbKey.BACKSTAGE.IMAGE_ID, imageId);
            BasicDBObject image = new BasicDBObject(UserdbKey.BACKSTAGE.BACKSTAGE_LIST, obj);
            BasicDBObject updateCommand = new BasicDBObject("$pull", image);
            coll.update(updateQuery, updateCommand);
            result = true;
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
            BasicDBObject findObj = new BasicDBObject(UserdbKey.BACKSTAGE.ID, id);
            BasicDBObject imageObj = new BasicDBObject(UserdbKey.BACKSTAGE.IMAGE_ID, imageId);
            BasicDBObject elemMatch = new BasicDBObject("$elemMatch", imageObj);
            findObj.append(UserdbKey.BACKSTAGE.BACKSTAGE_LIST, elemMatch);
            DBObject obj = coll.findOne(findObj);
            if (obj != null) {
                String field = UserdbKey.BACKSTAGE.BACKSTAGE_LIST + ".$." + UserdbKey.BACKSTAGE.FLAG;
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
