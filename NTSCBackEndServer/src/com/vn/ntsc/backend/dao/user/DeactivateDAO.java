/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.backend.dao.user;

import com.mongodb.*;
import com.mongodb.DBCollection;
import java.util.ArrayList;
import java.util.List;
import eazycommon.constant.Constant;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.mongokey.UserdbKey;
import eazycommon.exception.EazyException;
import eazycommon.util.Util;
import org.bson.types.ObjectId;
import com.vn.ntsc.backend.dao.DBManager;

/**
 *
 * @author DuongLTD
 */
public class DeactivateDAO {

    private static DBCollection coll;
    
    static {
        try {
            coll = DBManager.getUserDB().getCollection(UserdbKey.DEACTIVATE_COLLECTION);
        } catch (Exception ex) {
            Util.addErrorLog(ex);            
        }
    }

    public static List<String> getListDeactivate() throws EazyException {
        List<String> result = new ArrayList<>();
        try {
            DBCursor cur = coll.find();
            while (cur.hasNext()) {
                DBObject dbO = cur.next();
                Integer flag = (Integer) dbO.get(UserdbKey.DEACTIVATE.FLAG);
                if (flag == Constant.FLAG.ON) {
                    String userId = ((ObjectId) dbO.get(UserdbKey.DEACTIVATE.ID)).toString();
                    result.add(userId);
                }
            }
        }catch (Exception ex) {
            Util.addErrorLog(ex);            
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

}
