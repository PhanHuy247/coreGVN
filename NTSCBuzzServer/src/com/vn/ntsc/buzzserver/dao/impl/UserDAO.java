/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.buzzserver.dao.impl;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import eazycommon.util.Util;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import java.util.ArrayList;
import java.util.List;
import eazycommon.constant.Constant;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.mongokey.UserdbKey;
import eazycommon.exception.EazyException;
import java.util.LinkedList;
import org.bson.types.ObjectId;
import com.vn.ntsc.buzzserver.dao.DAO;
import com.vn.ntsc.buzzserver.entity.impl.buzz.User;
import com.vn.ntsc.buzzserver.entity.impl.buzz.UserDB;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 *
 * @author DuongLTD
 */
public class UserDAO {

    private static DBCollection coll;

    static {
        try {
            coll = DAO.getUserDB().getCollection(UserdbKey.USERS_COLLECTION);
        } catch (Exception ex) {
            Util.addErrorLog(ex);

        }
    }

    public static List<String> getDeactivate() throws EazyException {
        List<String> result = new ArrayList<>();
        try {
            DBCursor cur = coll.find();
            while (cur.hasNext()) {
                DBObject dbO = cur.next();
                Integer flag = (Integer) dbO.get(UserdbKey.USER.FLAG);
                if (flag != null && (flag == Constant.USER_STATUS_FLAG.DEACITIVE || flag == Constant.USER_STATUS_FLAG.DISABLE)) {
                    String userId = dbO.get(UserdbKey.USER.ID).toString();
                    result.add(userId);
//                    continue;
                }
//                Integer gender = (Integer) dbO.get(UserdbKey.USER.GENDER);
//                Integer verifyFlag = (Integer) dbO.get(UserdbKey.USER.VERIFICATION_FLAG);
//                if(gender == Constant.FEMALE && verifyFlag != null && verifyFlag != Constant.APPROVED){
//                    String userId = ((ObjectId) dbO.get(UserdbKey.USER.ID)).toString();
//                    result.add(userId);
//                }
            }
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static User getUserInfor(String userId) throws EazyException {
        User result = new User();
        try {
            //search by id
            ObjectId id = new ObjectId(userId);
            BasicDBObject obj = new BasicDBObject(UserdbKey.USER.ID, id);

            //search command
            DBObject dbOject = coll.findOne(obj);
            UserDB user = UserDB.fromDBObject(dbOject);
            result = user.getUserInfor();
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static DBObject getUserInforJSON(String userId) throws EazyException {
        Util.addDebugLog("GET APP VER FROM TOKEN USER ID" + userId);
        DBObject result = null;
        try {
            //search by id
            ObjectId id = new ObjectId(userId);
            BasicDBObject obj = new BasicDBObject(UserdbKey.USER.ID, id);

            //search command
            DBObject dbObject = coll.findOne(obj);
            result = dbObject;
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static List<String> getSafeUser(String safaryVersion) throws EazyException {
        List<String> result = new ArrayList<>();
        try {
            BasicDBObject obj = new BasicDBObject(UserdbKey.USER.SAFE_USER, 1);
            obj.append(UserdbKey.USER.GENDER, 1);
            
            BasicDBObject obj2 = new BasicDBObject(UserdbKey.USER.APP_VERSION, safaryVersion);
            obj2.append(UserdbKey.USER.GENDER, 1);
            BasicDBObject[] or = new BasicDBObject[2];
            or[0] = obj;
            or[1] = obj2;
            BasicDBObject query = new BasicDBObject("$or", or);
            DBCursor cur = coll.find(query);

            while (cur.hasNext()) {
                DBObject dbO = cur.next();
                Integer flag = (Integer) dbO.get(UserdbKey.USER.FLAG);
                if (flag != null && (flag == Constant.USER_STATUS_FLAG.ACTIVE)) {
                    String userId = dbO.get(UserdbKey.USER.ID).toString();
                    result.add(userId);
//                    continue;
                }

            }

        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static List<String> getSafeUserFav(String userId) throws EazyException {
        List<String> result = new ArrayList<>();
        try {
            DBCollection coll1 = DAO.getUserDB().getCollection(UserdbKey.FAVORIST_COLLECTION);
            ObjectId id = new ObjectId(userId);
            BasicDBObject obj1 = new BasicDBObject(UserdbKey.USER.ID, id);
            DBObject obj = coll1.findOne(obj1);
            BasicDBList listFav = (BasicDBList) obj.get("fav_lst");
            ArrayList<String> lstUID = new ArrayList<>();
            for(Object uid : listFav){
                lstUID.add(uid.toString());
            }
            
//            BasicDBObject obj = new BasicDBObject(UserdbKey.USER.SAFE_USER, 1);
//            obj.append(UserdbKey.USER.GENDER, 1);
//
//            DBCursor cur = coll.find(obj);
//
//            while (cur.hasNext()) {
//                DBObject dbO = cur.next();
//                Integer flag = (Integer) dbO.get(UserdbKey.USER.FLAG);
//                if (flag != null && (flag == Constant.USER_STATUS_FLAG.ACTIVE)) {
//                    String userId = dbO.get(UserdbKey.USER.ID).toString();
//                    //result.add(userId);
//
//                    while (cur1.hasNext()) {
//                        result.add(userId);
//                    }
//                }
//
//            }

        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }
    
    public static Map<String, String> getListUserName(List<String> listUser) throws EazyException{
        Map<String, String> resutl = new TreeMap<>();
        try{
            BasicDBList arr = new BasicDBList();
            for (String user : listUser) {
                Boolean isValid = ObjectId.isValid(user);
                if(isValid){
                    ObjectId id = new ObjectId(user);
                    arr.add(id);
                }
            }
            BasicDBObject obj = new BasicDBObject("$in", arr);
            BasicDBObject query = new BasicDBObject(UserdbKey.USER.ID, obj);
            DBCursor cur = coll.find(query);
            while (cur.hasNext()) {
                DBObject dbO = cur.next();
                String userId = dbO.get(UserdbKey.USER.ID).toString();
                String userName = (String) dbO.get(UserdbKey.USER.USERNAME);
                resutl.put(userId, userName);
            }
        }catch(Exception ex){
            Util.addErrorLog(ex);            
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return resutl;
    }
    
}
