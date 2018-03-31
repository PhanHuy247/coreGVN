/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test.db.settingdb;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import java.security.MessageDigest;
import eazycommon.constant.mongokey.SettingdbKey;
import eazycommon.util.Util;
import org.bson.types.ObjectId;
import test.db.DBTest;

/**
 *
 * @author duyetpt
 */
public class AdminDB {

    private static DB db;
    private static DBCollection coll;

    private static MessageDigest md;

    static {
        try {
            md = MessageDigest.getInstance("MD5");
            db = DBTest.mongo.getDB(SettingdbKey.DB_NAME);
            coll = db.getCollection(SettingdbKey.ADMINISTRATOR_COLLECTION);
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
    }

    public static String registAdmin(String email, String pwd, String name, int flag, int spFlag, String role_id) {
        BasicDBObject obj = new BasicDBObject();
        obj.put(SettingdbKey.ADMINISTRATOR.EMAIL, email);
        byte[] b = pwd.getBytes();
        String pass = Util.byteToString(md.digest(b));
        obj.put(SettingdbKey.ADMINISTRATOR.PASSWORD, pass);
        obj.put(SettingdbKey.ADMINISTRATOR.SPECIAL_FLAG, spFlag);
        obj.put(SettingdbKey.ADMINISTRATOR.FLAG, flag);
        obj.put(SettingdbKey.ADMINISTRATOR.ROLE_ID, role_id);
        obj.put(SettingdbKey.ADMINISTRATOR.NAME, "name");
        coll.insert(obj);
        String id = obj.getObjectId("_id").toString();
        return id;
    }

    public static String getEmail(String userId) {
        BasicDBObject obj = new BasicDBObject(SettingdbKey.ADMINISTRATOR.ID, new ObjectId(userId));
        DBObject found = coll.findOne(obj);
        return (String) found.get(SettingdbKey.ADMINISTRATOR.EMAIL);
    }

    public static boolean comparePass(String pwd, String userId) {
        byte[] b = pwd.getBytes();
        String pass = Util.byteToString(md.digest(b));
        BasicDBObject obj = new BasicDBObject("_id", new ObjectId(userId));
        DBObject dbObj = coll.findOne(obj);
        String Adpass = (String) dbObj.get("pwd");
        return pass.equals(Adpass);
    }

    public static void remove(String UserId) {
        BasicDBObject find = new BasicDBObject("_id", new ObjectId(UserId));
        coll.remove(find);
    }

    public static boolean isExit(String userid) {
        BasicDBObject find = new BasicDBObject("_id", new ObjectId(userid));
        DBObject obj = coll.findOne(find);
        return obj != null;
    }

    public static int countRole(String roleid) {
        BasicDBObject find = new BasicDBObject(SettingdbKey.ADMINISTRATOR.ROLE_ID, roleid);
        return coll.find(find).count();
    }
}
