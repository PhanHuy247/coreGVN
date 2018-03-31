/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAOTest;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import eazycommon.constant.mongokey.LogdbKey;
import eazycommon.util.Util;

/**
 *
 * @author duyetpt
 */
public class ReportImageDB {

    private static DBCollection coll;
    private static DB db;

    static {
        try {
            db = DBTest.mongo.getDB(LogdbKey.DB_NAME);
        } catch (Exception ex) {
            //Future logging here
            Util.addErrorLog(ex);

        }
    }

    public static void removeImgR(String userId, String subjectId, int type) {
        coll = db.getCollection(LogdbKey.IMAGE_REPORT_COLLECTION);
        BasicDBObject obj = new BasicDBObject(LogdbKey.USER_REPORT.USER_ID, userId);
        obj.put(LogdbKey.USER_REPORT.REPORT_ID, subjectId);
        obj.put(LogdbKey.USER_REPORT.REPORT_TYPE, type);
        coll.remove(obj);
    }

    public static void removeBuzzR(String userId, String subjectId, int type) {
        coll = db.getCollection(LogdbKey.USER_REPORT_COLLECTION);
        BasicDBObject obj = new BasicDBObject(LogdbKey.USER_REPORT.USER_ID, userId);
        obj.put(LogdbKey.USER_REPORT.REPORT_ID, subjectId);
        obj.put(LogdbKey.USER_REPORT.REPORT_TYPE, type);
        coll.remove(obj);
    }

    public static void removeUserR(String userId, String subjectId, int type) {
        coll = db.getCollection(LogdbKey.USER_REPORT_COLLECTION);
        BasicDBObject obj = new BasicDBObject(LogdbKey.USER_REPORT.USER_ID, userId);
        obj.put(LogdbKey.USER_REPORT.REPORT_ID, subjectId);
        obj.put(LogdbKey.USER_REPORT.REPORT_TYPE, type);
        coll.remove(obj);
    }
}
