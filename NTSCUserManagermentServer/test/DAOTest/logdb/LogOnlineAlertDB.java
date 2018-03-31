/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAOTest.logdb;

import DAOTest.DBTest;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import eazycommon.constant.mongokey.LogdbKey;
import eazycommon.util.Util;
import com.vn.ntsc.usermanagementserver.Config;

/**
 *
 * @author duyetpt
 */
public class LogOnlineAlertDB {

    private static DBCollection coll;
    private static DB db;

    static {
        try {
            db = DBTest.mongo.getDB(LogdbKey.DB_NAME);
            coll = db.getCollection(LogdbKey.LOG_ONLINE_ALERT_COLLECTION);
        } catch (Exception ex) {
            Util.addErrorLog(ex);

        }
    }

    public static void remove(String requestId) {
        BasicDBObject removeObj = new BasicDBObject(LogdbKey.LOG_ONLINE_ALERT.REQUEST_ID, requestId);
        coll.remove(removeObj);
    }
}
