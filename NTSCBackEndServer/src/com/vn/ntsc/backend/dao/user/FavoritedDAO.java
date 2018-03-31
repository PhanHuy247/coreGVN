/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.backend.dao.user;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import java.util.ArrayList;
import java.util.List;
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
public class FavoritedDAO {

    private static DBCollection coll;

    static {
        try {
            coll = DBManager.getUserDB().getCollection(UserdbKey.FAVORITED_COLLECTION);
        } catch (Exception ex) {
            Util.addErrorLog(ex);            
        }
    }

    public static List<String> getFavouritedList(String userId) throws EazyException {
        List<String> result = new ArrayList<String>();
        try {
            ObjectId id = new ObjectId(userId);
            BasicDBObject updateQuery = new BasicDBObject(UserdbKey.FAVORITED.ID, id);
            DBObject respondObj = coll.findOne(updateQuery);
            if (respondObj != null) {
                BasicDBList favoritedList = (BasicDBList) respondObj.get(UserdbKey.FAVORITED.FAVOURITED_LIST);
                if (!favoritedList.isEmpty()) {
                    for (int i = 0; i < favoritedList.size(); i++) {
                        DBObject favDBO = (DBObject) favoritedList.get(i);
                        String fId = (String) favDBO.get("fvt_id");
                        result.add(fId);
                    }
                }
            }
        }catch (Exception ex) {
            Util.addErrorLog(ex);            
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

}
