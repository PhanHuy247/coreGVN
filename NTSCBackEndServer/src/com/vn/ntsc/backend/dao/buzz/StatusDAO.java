/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.backend.dao.buzz;

import com.mongodb.*;
import com.mongodb.DBCollection;
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
public class StatusDAO {

    private static DBCollection coll;

    static {
        try {
            coll = DBManager.getBuzzDB().getCollection(BuzzdbKey.USER_STATUS_COLLECTION);
        } catch (Exception ex) {
            Util.addErrorLog(ex);            
        }
    }

    public static int checkStatus(String buzzId, String userId) throws EazyException {
        int result = Constant.FLAG.OFF;
        try {
            ObjectId id = new ObjectId(userId);
            DBObject query = new BasicDBObject(BuzzdbKey.USER_STATUS.ID, id);
            DBObject obj = coll.findOne(query);
            if (obj != null) {
                String status = (String) obj.get(BuzzdbKey.USER_BUZZ.BUZZ_ID);
                if (status != null && status.equals(buzzId)) {
                    result = Constant.FLAG.ON;
                }
            }
        }catch (Exception ex) {
            Util.addErrorLog(ex);            
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

}
