/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAOTest;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.mongokey.UserdbKey;
import eazycommon.exception.EazyException;
import eazycommon.util.Util;
import org.bson.types.ObjectId;

/**
 *
 * @author duyetpt
 */
public class NotificationSettingDB {

    private static DBCollection coll;
    private static DB db;

    static {
        try {
            db = DBTest.mongo.getDB(UserdbKey.DB_NAME);
            coll = db.getCollection(UserdbKey.NOTIFICATION_SETTING_COLLECTION);
        } catch (Exception ex) {
            //Future logging here
        }
    }

    public static boolean addNotificationSetting(String userId, int notiBuzz, int andgAlert, int notiChat, int notiCheck) throws EazyException {
        boolean result = false;
        try {
            ObjectId id = new ObjectId(userId);
            BasicDBObject insertObj = new BasicDBObject(UserdbKey.NOTIFICATION_SETTING.ID, id);
            insertObj.append(UserdbKey.NOTIFICATION_SETTING.NOTI_BUZZ, notiBuzz);
            insertObj.append(UserdbKey.NOTIFICATION_SETTING.EAZY_ALERT, andgAlert);
            insertObj.append(UserdbKey.NOTIFICATION_SETTING.NOTI_CHAT, notiChat);
            insertObj.append(UserdbKey.NOTIFICATION_SETTING.NOTI_CHECK_OUT, notiCheck);
            coll.save(insertObj);
            result = true;
        } catch (Exception ex) {
            Util.addErrorLog(ex);

            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static void remove(String userId) {
        coll.remove(new BasicDBObject("_id", new ObjectId(userId)));
    }
}
