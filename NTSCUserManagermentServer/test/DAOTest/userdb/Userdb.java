/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAOTest.userdb;

import DAOTest.DBTest;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import java.io.IOException;
import java.util.Date;
import eazycommon.constant.Constant;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.mongokey.UserdbKey;
import eazycommon.exception.EazyException;
import eazycommon.util.Util;
import org.bson.types.ObjectId;
import org.json.simple.JSONObject;
import static org.testng.AssertJUnit.assertEquals;
import com.vn.ntsc.usermanagementserver.entity.impl.datarespond.LoginData;
import com.vn.ntsc.usermanagementserver.server.respond.impl.account.RegisterApi;
import com.vn.ntsc.usermanagementserver.server.respond.result.EntityRespond;
import com.vn.ntsc.usermanagementserver.setting.Setting;

/**
 *
 * @author Admin
 */
public class Userdb {

    public static DB db;
    public static DBCollection coll;
    public static JSONObject objRequest;
    
    public static DBCollection collFavoristed;
    public static DBCollection collFavorist;
    public static DBCollection collBlock;
    public static DBCollection collCheckout;
    static {
        db = DBTest.mongo.getDB(UserdbKey.DB_NAME);
        objRequest = new JSONObject();
        coll = db.getCollection(UserdbKey.USERS_COLLECTION);
        collFavorist = db.getCollection(UserdbKey.FAVORIST_COLLECTION);
        collFavoristed = db.getCollection(UserdbKey.FAVORITED_COLLECTION);
        collBlock = db.getCollection(UserdbKey.BLOCK_COLLECTION);
        collCheckout = db.getCollection(UserdbKey.CHECKOUT_COLLECTION);
        Setting.DISTANCE.add(100.1);
        Setting.DISTANCE.add(100.1);
        Setting.DISTANCE.add(100.1);
        Setting.DISTANCE.add(100.1);
    }

    public static JSONObject initRegistUser(Long gender,
            Long deviceType) throws IOException {
        JSONObject objUser = new JSONObject();
        objUser.put("device_type", deviceType);
        objUser.put("bir", "19930101");
        objUser.put("notify_token", "12313123");
        objUser.put("gender", gender);

        return objUser;
    }

    public static JSONObject initRegistUserByFace(Long gender,
            Long deviceType, String fbId) throws IOException {
        JSONObject objUser = new JSONObject();
        objUser.put("device_type", deviceType);
        objUser.put("bir", "19930101");
        objUser.put("notify_token", "12313123");
        objUser.put("gender", gender);
        objUser.put("fb_id", fbId);
        return objUser;
    }

    public static String registUser(JSONObject obj) {

        RegisterApi api = new RegisterApi();
        Date time = Util.getGMTTime();
        EntityRespond res = (EntityRespond) api.execute(obj, time);
        assertEquals("regist successfully !", ErrorCode.SUCCESS, res.code);
        LoginData data = (LoginData) res.data;

        return data.user.userId;
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
        objRequest.clear();
        objRequest = initRegistUser(Long.valueOf(Constant.GENDER.FEMALE), Long.valueOf(1));
        return registUser(objRequest);
    }

    public static String registMale() throws IOException {
        objRequest.clear();
        objRequest = initRegistUser(Long.valueOf(Constant.GENDER.MALE), Long.valueOf(1));
        return registUser(objRequest);
    }

    public static String registUserByFacebook(String fbId) throws IOException {
        objRequest.clear();
        objRequest = initRegistUserByFace(Long.valueOf(Constant.GENDER.MALE), Long.valueOf(1), fbId);
        return registUser(objRequest);
    }

    public static boolean updateFinishFlag(String userId, int flag) throws EazyException {
        boolean result = false;
        try {
            //search by email
            ObjectId id = new ObjectId(userId);
            BasicDBObject query = new BasicDBObject(UserdbKey.USER.ID, id);
            //command add verification code
            BasicDBObject addition = new BasicDBObject(UserdbKey.USER.FINISH_REGISTER_FLAG, flag);
            BasicDBObject command = new BasicDBObject("$set", addition);
            coll.update(query, command);
            result = true;
        } catch (Exception ex) {
            Util.addErrorLog(ex);

            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }
    public static boolean updateFlagField(String userId, int flag, String key) throws EazyException {
        boolean result = false;
        try {
            //search by email
            ObjectId id = new ObjectId(userId);
            BasicDBObject query = new BasicDBObject(UserdbKey.USER.ID, id);
            //command add verification code
            BasicDBObject addition = new BasicDBObject(key, flag);
            BasicDBObject command = new BasicDBObject("$set", addition);
            coll.update(query, command);
            result = true;
        } catch (Exception ex) {
            Util.addErrorLog(ex);

            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }
    public static boolean updateFlag(String userId, int flag) throws EazyException {
        boolean result = false;
        try {
            //search by email
            ObjectId id = new ObjectId(userId);
            BasicDBObject query = new BasicDBObject(UserdbKey.USER.ID, id);
            //command add verification code
            BasicDBObject addition = new BasicDBObject(UserdbKey.USER.FLAG, flag);
            BasicDBObject command = new BasicDBObject("$set", addition);
            coll.update(query, command);
            result = true;
        } catch (Exception ex) {
            Util.addErrorLog(ex);

            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static boolean updateVerificationFlag(String userId, int flag) throws EazyException {
        boolean result = false;
        try {
            //search by email
            ObjectId id = new ObjectId(userId);
            BasicDBObject query = new BasicDBObject(UserdbKey.USER.ID, id);
            //command add verification code
            BasicDBObject addition = new BasicDBObject(UserdbKey.USER.VERIFICATION_FLAG, flag);
            BasicDBObject command = new BasicDBObject("$set", addition);
            coll.update(query, command);
            result = true;
        } catch (Exception ex) {
            Util.addErrorLog(ex);

            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }
    
    public static boolean updateEmailFlag(String userId, int flag) throws EazyException {
        boolean result = false;
        try {
            //search by email
            ObjectId id = new ObjectId(userId);
            BasicDBObject query = new BasicDBObject(UserdbKey.USER.ID, id);
            //command add verification code
            BasicDBObject addition = new BasicDBObject(UserdbKey.USER.UPDATE_EMAIL_FLAG, flag);
            BasicDBObject command = new BasicDBObject("$set", addition);
            coll.update(query, command);
            result = true;
        } catch (Exception ex) {
            Util.addErrorLog(ex);

            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static boolean updateEmail(String userId, String email) throws EazyException {
        boolean result = false;
        try {
            //search by email
            ObjectId id = new ObjectId(userId);
            BasicDBObject query = new BasicDBObject(UserdbKey.USER.ID, id);
            //command add verification code
            BasicDBObject addition = new BasicDBObject(UserdbKey.USER.EMAIL, email);
            BasicDBObject command = new BasicDBObject("$set", addition);
            coll.update(query, command);
            result = true;
        } catch (Exception ex) {
            Util.addErrorLog(ex);

            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static boolean updateUserName(String userId, String name) throws EazyException {
        boolean result = false;
        try {
            //search by email
            ObjectId id = new ObjectId(userId);
            BasicDBObject query = new BasicDBObject(UserdbKey.USER.ID, id);
            //command add verification code
            BasicDBObject addition = new BasicDBObject(UserdbKey.USER.USERNAME, name);
            BasicDBObject command = new BasicDBObject("$set", addition);
            coll.update(query, command);
            result = true;
        } catch (Exception ex) {
            Util.addErrorLog(ex);

            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static Integer getverificationFlag(String userId) {
        Integer result;
        BasicDBObject obj = new BasicDBObject("_id", new ObjectId(userId));
        DBObject found = coll.findOne(obj);
        result = (Integer) found.get(UserdbKey.USER.VERIFICATION_FLAG);
        return result;
    }

    public static Integer getFinishFlag(String userId) {
        Integer result;
        BasicDBObject obj = new BasicDBObject("_id", new ObjectId(userId));
        DBObject found = coll.findOne(obj);
        result = (Integer) found.get(UserdbKey.USER.FINISH_REGISTER_FLAG);
        return result;
    }

    public static String getPass(String userId) {
        String result;
        BasicDBObject obj = new BasicDBObject("_id", new ObjectId(userId));
        DBObject found = coll.findOne(obj);
        result = (String) found.get(UserdbKey.USER.SIP_PASSWORD);
        return result;
    }

    public static void removeFavoristById(String userId) {
        ObjectId id = new ObjectId(userId);
        collFavorist.remove(new BasicDBObject("_id", id));
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
     
    public static void setVerificationCode(String userId, String code) {
        ObjectId id = new ObjectId(userId);
        BasicDBObject updateObj = new BasicDBObject(UserdbKey.USER.VERIFICATION_CODE, code);
        BasicDBObject findObj = new BasicDBObject(UserdbKey.USER.ID, id);

        BasicDBObject update = new BasicDBObject("$set", updateObj);
        coll.update(findObj, update);
    }

    public static String getVerificationCode(String userId) {
        ObjectId id = new ObjectId(userId);
        BasicDBObject findObj = new BasicDBObject(UserdbKey.USER.ID, id);

        DBObject found = coll.findOne(findObj);
        String code = (String) found.get(UserdbKey.USER.VERIFICATION_CODE);
        return code;
    }
    
    public static Object getObject(String userId, String key){
        ObjectId id = new ObjectId(userId);
        BasicDBObject findObj = new BasicDBObject(UserdbKey.USER.ID, id);

        DBObject found = coll.findOne(findObj);
        return found.get(key);
    }
//    public static void main(String[] args) throws IOException, NoSuchAlgorithmException {
//        MessageDigest md = MessageDigest.getInstance("MD5");
//        String pass = "7c4a8d09ca3762af61e59520943dc26494f8941b";
//        byte[] b = md.digest(pass.getBytes());
//        String result = Util.byteToString(b);
//        BasicDBObject findObj = new BasicDBObject("user_name", "tamco");
//        BasicDBObject updateObj = new BasicDBObject("pwd", result)
//                .append("sip_pwd", pass);
//        BasicDBObject update = new BasicDBObject("$set", updateObj);
//        coll.update(findObj, update);
//    }
}
