/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import test.db.DBTest;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;
import eazycommon.constant.Constant;
import eazycommon.constant.mongokey.UserdbKey;
import eazycommon.util.Util;
import org.bson.types.ObjectId;
import org.json.simple.JSONObject;

/**
 *
 * @author Admin
 */
public class Userdb {

    public static DB db;
    public static DBCollection coll;
    public static JSONObject objRequest;
    public static DBCollection collNoti;
    public static DBCollection collFavoristed;
    public static DBCollection collBlock;
    public static DBCollection collCheckout;

    static {
        db = DBTest.mongo.getDB(UserdbKey.DB_NAME);
        objRequest = new JSONObject();
        coll = db.getCollection(UserdbKey.USERS_COLLECTION);
        collNoti = db.getCollection(UserdbKey.NOTIFICATION_COLLECTION);
        collFavoristed = db.getCollection(UserdbKey.FAVORITED_COLLECTION);
        collBlock = db.getCollection(UserdbKey.BLOCK_COLLECTION);
        collCheckout = db.getCollection(UserdbKey.CHECKOUT_COLLECTION);
    }

    public static String registUser(int gender) {
        try {
            BasicDBObject obj = new BasicDBObject();
            obj.put(Actor.TEST_FLAG, 1);
            obj.put(UserdbKey.USER.SYSTEM_ACCOUNT, 0);
            obj.put(UserdbKey.USER.ABOUT, "about me");
            obj.put(UserdbKey.USER.ACCOUNT_TYPE, Constant.ACCOUNT_TYPE_VALUE.EMAIL_TYPE);
            obj.put(UserdbKey.USER.AVATAR_ID, "avatar_ID");
            obj.put(UserdbKey.USER.BACKSTAGE_NUMBER, 10);
            obj.put(UserdbKey.USER.BACKSTAGE_PRICE, 100);
            obj.put(UserdbKey.USER.BIRTHDAY, "19930101");
            obj.put(UserdbKey.USER.CM_CODE, "CM_CODE");
            obj.put(UserdbKey.USER.EMAIL, "email" + System.currentTimeMillis() + "@gmail.com");
            obj.put(UserdbKey.USER.FLAG, 1);
            obj.put(UserdbKey.USER.GENDER, gender);
            obj.put(UserdbKey.USER.JOB, 22);
            obj.put(UserdbKey.USER.REGISTER_DATE, "19880909");
            obj.put(UserdbKey.USER.USERNAME, "user_name");
            obj.put(UserdbKey.USER.SORT_NAME, "email_name" + System.currentTimeMillis());
            obj.put(UserdbKey.USER.REGION, 1);
            obj.put(UserdbKey.USER.LAST_PURCHASE_TIME, "19990909");
            obj.put(UserdbKey.USER.POINT, 100);
            obj.put(UserdbKey.USER.PB_IMAGE_NUMBER, 0);
            String org_pass = "12345678";
            obj.put(UserdbKey.USER.SIP_PASSWORD, org_pass);
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] b = md.digest(org_pass.getBytes());
            String pass = Util.byteToString(b);
            obj.put(UserdbKey.USER.PASSWORD, pass);
//        obj.put(DBParamKey.USER.VERIFICATION_FLAG, Constant.YES);
            obj.put(UserdbKey.USER.VOICE_CALL_WAITING, true);
            obj.put(UserdbKey.USER.VIDEO_CALL_WAITING, true);
//        obj.put(DBParamKey.USER.FINISH_REGISTER_FLAG, Constant.YES);
            if (gender == Constant.GENDER.FEMALE) {
                obj.put(UserdbKey.USER.HIPS, 77);
                obj.put(UserdbKey.USER.BUST, 66);
                obj.put(UserdbKey.USER.CUTETYPE, 1);
                obj.put(UserdbKey.USER.CUP, 5);
                obj.put(UserdbKey.USER.FETISH, "FETISH");
//                obj.put(UserdbKey.USER.INDECENT, 5);
                obj.put(UserdbKey.USER.JOINT_HOURS, 3);
                obj.put(UserdbKey.USER.TYPEOFMANS, "funny");
                obj.put(UserdbKey.USER.WAIST, 55);
            } else {
                obj.put(UserdbKey.USER.HOBBY, "runing");
            }
            coll.insert(obj);
            String id = obj.getObjectId("_id").toString();
            return id;
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(Userdb.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public static String registUserByFb() {
        BasicDBObject obj = new BasicDBObject();
        obj.put(Actor.TEST_FLAG, 1);
        obj.put(UserdbKey.USER.SYSTEM_ACCOUNT, 0);
        obj.put(UserdbKey.USER.ABOUT, "about me");
        obj.put(UserdbKey.USER.ACCOUNT_TYPE, Constant.ACCOUNT_TYPE_VALUE.FB_TYPE);
        obj.put(UserdbKey.USER.AVATAR_ID, "avatar_ID");
        obj.put(UserdbKey.USER.BACKSTAGE_NUMBER, 10);
        obj.put(UserdbKey.USER.BACKSTAGE_PRICE, 100);
        obj.put(UserdbKey.USER.BIRTHDAY, "19930101");
        obj.put(UserdbKey.USER.CM_CODE, "CM_CODE");
        obj.put(UserdbKey.USER.EMAIL, "email" + System.currentTimeMillis() + "@facebook.com");
        obj.put(UserdbKey.USER.FB_ID, "FB_ID" + System.currentTimeMillis());
        obj.put(UserdbKey.USER.FLAG, 1);
        obj.put(UserdbKey.USER.GENDER, 0);
        obj.put(UserdbKey.USER.JOB, 22);
        obj.put(UserdbKey.USER.REGISTER_DATE, "19880909");
        obj.put(UserdbKey.USER.USERNAME, "fb_name");
        obj.put(UserdbKey.USER.SORT_NAME, "fb_name" + System.currentTimeMillis());
        obj.put(UserdbKey.USER.REGION, 1);
        obj.put(UserdbKey.USER.LAST_PURCHASE_TIME, "19990909");
        obj.put(UserdbKey.USER.POINT, 100);
//        obj.put(DBParamKey.USER.VERIFICATION_FLAG, Constant.YES);
        obj.put(UserdbKey.USER.VOICE_CALL_WAITING, true);
        obj.put(UserdbKey.USER.VIDEO_CALL_WAITING, true);
//        obj.put(DBParamKey.USER.FINISH_REGISTER_FLAG, Constant.YES);
        obj.put(UserdbKey.USER.HOBBY, "runing");
        coll.insert(obj);
        String id = obj.getObjectId("_id").toString();
        return id;
    }

    public static void removeUserByTestFlag() {
        BasicDBObject exist = new BasicDBObject("$exists", true);
        BasicDBObject find = new BasicDBObject(Actor.TEST_FLAG, exist);
        coll.remove(find);
    }

    public static boolean removeUserById(String id) {
        try {
            if (id != null) {
                coll.remove(new BasicDBObject("_id", new ObjectId(id)));
                return true;
            }
        } catch (Exception ex) {
            System.out.println(ex.getCause());
        }

        return false;
    }

    public static String registFemale() throws IOException {
        return registUser(Constant.GENDER.FEMALE);
    }

    public static String registMale() throws IOException {
        return registUser(Constant.GENDER.MALE);
    }

    public static void remove_log_block(DB db, DBCollection coll_user) {
        DBCollection coll_log = db.getCollection("log_block");
        DBCursor cursor = coll_log.find();
        while (cursor.hasNext()) {
            DBObject obj = cursor.next();
            String req_id = (String) obj.get("req_id");
            String partner_id = (String) obj.get("partner_id");

            BasicDBList list = new BasicDBList();
            list.add(new ObjectId(req_id));
            list.add(new ObjectId(partner_id));
            DBCursor objU = coll_user.find(new BasicDBObject("_id", new BasicDBObject("$in", list)));
            if (objU.size() < 2) {
                coll_log.remove(new BasicDBObject("req_id", req_id));
                coll_log.remove(new BasicDBObject("partner_id", req_id));

                coll_log.remove(new BasicDBObject("req_id", partner_id));
                coll_log.remove(new BasicDBObject("partner_id", partner_id));
            }
        }
    }

    public static void remove_log_CheckOut(DB db, DBCollection coll_user) {
        DBCollection coll_log = db.getCollection("log_check_out");
        DBCursor cursor = coll_log.find();
        int sizeD = 0;
        while (cursor.hasNext()) {
            System.out.println("size :" + cursor.size());
            DBObject obj = cursor.next();
            String req_id = (String) obj.get("req_id");
            String partner_id = (String) obj.get("partner_id");

            BasicDBList list = new BasicDBList();
            list.add(new ObjectId(req_id));
            list.add(new ObjectId(partner_id));
            DBCursor objU = coll_user.find(new BasicDBObject("_id", new BasicDBObject("$in", list)));
            System.out.println(new BasicDBObject("_id", new BasicDBObject("$in", list)).toString());
            if (objU.size() < 2) {
                coll_log.remove(new BasicDBObject("req_id", req_id));
                coll_log.remove(new BasicDBObject("partner_id", req_id));

                coll_log.remove(new BasicDBObject("req_id", partner_id));
                coll_log.remove(new BasicDBObject("partner_id", partner_id));
            }
        }
        System.out.println("size Delete :" + sizeD);
    }

    public static void remove_logLogin(DB db, DBCollection coll_user) {
        DBCollection coll_log = db.getCollection("log_login");
        DBCursor cursor = coll_log.find();
        while (cursor.hasNext()) {
            DBObject obj = cursor.next();
            String req_id = (String) obj.get("user_id");
            DBObject objU = coll_user.findOne(new BasicDBObject("_id", new ObjectId(req_id)));
            if (objU == null) {
                coll_log.remove(new BasicDBObject("user_id", req_id));
            }
        }
    }

    public static void remove_logPoint(DB db, DBCollection coll_user) {
        DBCollection coll_log = db.getCollection("log_point");
        DBCursor cursor = coll_log.find();
        while (cursor.hasNext()) {
            DBObject obj = cursor.next();
            String req_id = (String) obj.get("user_id");
            DBObject objU = coll_user.findOne(new BasicDBObject("_id", new ObjectId(req_id)));
            if (objU == null) {
                coll_log.remove(new BasicDBObject("user_id", req_id));
            }
        }
    }

    public static Object getObject(String userId, String key) {
        BasicDBObject obj = new BasicDBObject(UserdbKey.USER.ID, new ObjectId(userId));
        DBObject found = coll.findOne(obj);
        if (found != null) {
            return found.get(key);
        }
        return null;
    }

    public static int getPoint(String userId) {
        return (Integer) getObject(userId, UserdbKey.USER.POINT);
    }

    public static void removeCheckOutById(String userId) {
        ObjectId id = new ObjectId(userId);
        collCheckout.remove(new BasicDBObject("_id", id));
    }

    public static void removeFavoristedById(String userId) {
        ObjectId id = new ObjectId(userId);
        collFavoristed.remove(new BasicDBObject("_id", id));
    }

    public static void removeBlockListById(String userId) {
        ObjectId id = new ObjectId(userId);
        collBlock.remove(new BasicDBObject("_id", id));
    }

    public static void updateNumberOf(String userId, String key, int count) {
        BasicDBObject findObj = new BasicDBObject(UserdbKey.USER.ID, new ObjectId(userId));
        BasicDBObject updateObj = new BasicDBObject("$set", new BasicDBObject(key, count));

        coll.update(findObj, updateObj);
    }

}
