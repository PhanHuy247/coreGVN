/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAOTest.userdb;

import static DAOTest.userdb.Userdb.coll;
import static DAOTest.userdb.Userdb.db;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.WriteResult;
import eazycommon.constant.mongokey.UserdbKey;

/**
 *
 * @author duyetpt
 */
public class NotificationColl {

    public static DBCollection collNoti;

    static {
        collNoti = Userdb.db.getCollection(UserdbKey.NOTIFICATION_COLLECTION);
    }

    public static boolean isNoti(String fromUser, String toUser) {
        BasicDBObject obj = new BasicDBObject(UserdbKey.NOTIFICATION.FROM_NOTI_USER_ID, fromUser)
                .append(UserdbKey.NOTIFICATION.TO_USER_ID, toUser);
        DBObject found = collNoti.findOne(obj);
        if (found != null) {
            return true;
        }
        return false;
    }

    public static String insert(String toUser, String fromUser) {
        BasicDBObject obj = new BasicDBObject();
        obj.put(UserdbKey.NOTIFICATION.TO_USER_ID, toUser);
        obj.append(UserdbKey.NOTIFICATION.FROM_NOTI_USER_ID, fromUser);
        WriteResult wr = collNoti.insert(obj);
        String id = obj.getObjectId("_id").toString();
        return id;
    }

    public static void remove(String fromUserId) {
        BasicDBObject obj = new BasicDBObject();
        obj.append(UserdbKey.NOTIFICATION.FROM_NOTI_USER_ID, fromUserId);
        coll.remove(obj);
    }

}
