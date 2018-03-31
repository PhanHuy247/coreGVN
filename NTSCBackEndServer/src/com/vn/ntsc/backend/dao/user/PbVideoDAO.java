/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.backend.dao.user;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.vn.ntsc.backend.dao.DBManager;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.mongokey.UserdbKey;
import eazycommon.exception.EazyException;
import eazycommon.util.Util;
import org.bson.types.ObjectId;

/**
 *
 * @author hoangnh
 */
public class PbVideoDAO {
    private static DBCollection coll;

    static {
        try {
            coll = DBManager.getUserDB().getCollection(UserdbKey.PB_VIDEO_COLLECTION);
        } catch (Exception ex) {
            Util.addErrorLog(ex);            
        }
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
