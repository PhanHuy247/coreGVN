/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.backend.dao.buzz;

import com.mongodb.*;
import com.mongodb.DBCollection;
import java.util.ArrayList;
import java.util.List;
import eazycommon.constant.Constant;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.mongokey.BuzzdbKey;
import eazycommon.exception.EazyException;
import eazycommon.util.Util;
import org.bson.types.ObjectId;
import com.vn.ntsc.backend.dao.DBManager;

/**
 *
 * @author DuongLTD
 */
public class LikeDAO {

    private static DBCollection coll;

    static {
        try {
            coll = DBManager.getBuzzDB().getCollection(BuzzdbKey.BUZZ_LIKE_COLLECTION);
        } catch (Exception ex) {
            Util.addErrorLog(ex);            
        }
    }

    public static List<String> getLikeList(String buzzId) throws EazyException {
        List<String> result = new ArrayList<>();
        try {
            ObjectId id = new ObjectId(buzzId);
            BasicDBObject findObj = new BasicDBObject(BuzzdbKey.BUZZ_LIKE.ID, id);
            DBObject obj = coll.findOne(findObj);
            if (obj != null) {
                BasicDBList likeBuzz = (BasicDBList) obj.get(BuzzdbKey.BUZZ_LIKE.LIKE_LIST);
                if (likeBuzz != null) {
                    for (int i = 0; i < likeBuzz.size(); i++) {
                        BasicDBObject like = (BasicDBObject) likeBuzz.get(i);
                        String likeId = (String) like.get(BuzzdbKey.BUZZ_LIKE.USER_ID);
                        Integer flag = like.getInt(BuzzdbKey.BUZZ_LIKE.LIKE_FLAG);
                        if (flag == Constant.FLAG.ON) {
                            result.add(likeId);
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

    public boolean checkLikeExist(String buzzId, String userId) throws EazyException {
        boolean result = false;
        try {
            ObjectId id = new ObjectId(buzzId);
            BasicDBObject findObj = new BasicDBObject(BuzzdbKey.BUZZ_LIKE.ID, id);
            DBObject obj = coll.findOne(findObj);
            if (obj != null) {
                BasicDBList likeBuzz = (BasicDBList) obj.get(BuzzdbKey.BUZZ_LIKE.LIKE_LIST);
                if (likeBuzz != null) {
                    for (int i = 0; i < likeBuzz.size(); i++) {
                        BasicDBObject like = (BasicDBObject) likeBuzz.get(i);
                        String likeId = (String) like.get(BuzzdbKey.BUZZ_LIKE.USER_ID);
                        Integer flag = (Integer) like.get(BuzzdbKey.BUZZ_LIKE.LIKE_FLAG);
                        if (likeId.equals(userId) && flag == Constant.FLAG.ON) {
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

    public static void deleteBuzz(String buzzId) {
        try {
            ObjectId id = new ObjectId(buzzId);
            BasicDBObject deleteObj = new BasicDBObject(BuzzdbKey.BUZZ_LIKE.ID, id);
            coll.remove(deleteObj);

        }catch (Exception ex) {
            Util.addErrorLog(ex);            
           
        }
    }

}
