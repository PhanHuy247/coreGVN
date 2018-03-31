/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test.db.userdb;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.mongokey.UserdbKey;
import eazycommon.exception.EazyException;
import eazycommon.util.Util;
import org.bson.types.ObjectId;
import static test.Userdb.db;

/**
 *
 * @author duyetpt
 */
public class FavoristedDB {

    public static DBCollection coll;

    static {
        coll = db.getCollection(UserdbKey.FAVORITED_COLLECTION);
    }

    public static void removeFavoristedById(String userId) {
        ObjectId id = new ObjectId(userId);
        coll.remove(new BasicDBObject("_id", id));
    }

    public static boolean addFavoristed(String userId, String friendId) throws EazyException {
        boolean result = false;
        try {
            ObjectId id = new ObjectId(userId);
            BasicDBObject updateQuery = new BasicDBObject(UserdbKey.FAVORIST.ID, id);
            BasicDBObject obj = new BasicDBObject(UserdbKey.FAVORITED.FAVOURITED_LIST, friendId);
            BasicDBObject updateCommand = new BasicDBObject("$push", obj);
            coll.update(updateQuery, updateCommand, true, false);
            result = true;
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }
}
