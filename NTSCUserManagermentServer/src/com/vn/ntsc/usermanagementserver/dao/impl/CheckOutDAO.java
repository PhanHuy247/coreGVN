/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.usermanagementserver.dao.impl;

import eazycommon.util.DateFormat;
import eazycommon.util.Util;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.mongokey.UserdbKey;
import eazycommon.exception.EazyException;
import org.bson.types.ObjectId;
import com.vn.ntsc.usermanagementserver.dao.DatabaseLoader;
import com.vn.ntsc.usermanagementserver.entity.impl.user.FollowingUser;

/**
 *
 * @author RuAc0n
 */
public class CheckOutDAO {

    private static DBCollection coll;

    static {
        try {
            coll = DatabaseLoader.getUserDB().getCollection(UserdbKey.CHECKOUT_COLLECTION);
        } catch (Exception ex) {
            //Future logging here
            Util.addErrorLog(ex);

        }
    }

    public static boolean addCheckOut(String userId, String friendId, Date time) throws EazyException {
        boolean result = false;
        if (friendId == null || friendId.isEmpty()){
            return result;
        }
        try {
            ObjectId id = new ObjectId(userId);
            BasicDBObject findObj = new BasicDBObject(UserdbKey.CHECKOUT.ID, id);
            BasicDBObject checkerObj = new BasicDBObject(UserdbKey.CHECKOUT.CHECKER_ID, friendId);
            BasicDBObject elemMatch = new BasicDBObject("$elemMatch", checkerObj);
            findObj.append(UserdbKey.CHECKOUT.CHECKER_LIST, elemMatch);
            DBObject obj = coll.findOne(findObj);
            if (obj != null) {
                String field = UserdbKey.CHECKOUT.CHECKER_LIST + ".$." + UserdbKey.CHECKOUT.CHECKER_TIME;
                BasicDBObject updateObj = new BasicDBObject(field, time.getTime());
                BasicDBObject setObj = new BasicDBObject("$set", updateObj);
                coll.update(findObj, setObj);
            } else {
                BasicDBObject updateQuery = new BasicDBObject(UserdbKey.CHECKOUT.ID, id);
                BasicDBObject checkerElement = new BasicDBObject(UserdbKey.CHECKOUT.CHECKER_ID, friendId);
                checkerElement.append(UserdbKey.CHECKOUT.CHECKER_TIME, time.getTime());
                BasicDBObject checker = new BasicDBObject(UserdbKey.CHECKOUT.CHECKER_LIST, checkerElement);
                BasicDBObject updateCommand = new BasicDBObject("$push", checker);
                coll.update(updateQuery, updateCommand, true, false);
            }
            result = true;
        } catch (Exception ex) {
            Util.addErrorLog(ex);

            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static Date getCheckOutTime(String userId, String friendId) throws EazyException {
        Date result = null;
        if (userId == null){
            return result;
        }
        try {
            ObjectId id = new ObjectId(userId);
            BasicDBObject updateQuery = new BasicDBObject(UserdbKey.CHECKOUT.ID, id);
            DBObject respondObj = coll.findOne(updateQuery);
            if (respondObj != null) {
                BasicDBList checkList = (BasicDBList) respondObj.get(UserdbKey.CHECKOUT.CHECKER_LIST);
                if (checkList != null && !checkList.isEmpty()) {
                    for (Object checkList1 : checkList) {
                        BasicDBObject check = (BasicDBObject) checkList1;
                        String checkId = check.getString(UserdbKey.CHECKOUT.CHECKER_ID);
                        if (checkId.equals(friendId)) {
                            Long checkTime = (Long) check.get(UserdbKey.CHECKOUT.CHECKER_TIME);
                            String strTime = DateFormat.format(checkTime);
                            result = DateFormat.parse(strTime);
                            break;
                        }
                    }
                }
            }
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static int getCheckOutNumber(String userId, List<String> blackList) throws EazyException {
        int result = 0;
        try {
            ObjectId id = new ObjectId(userId);
            BasicDBObject updateQuery = new BasicDBObject(UserdbKey.CHECKOUT.ID, id);
            DBObject respondObj = coll.findOne(updateQuery);
            if (respondObj != null) {
                BasicDBList checkList = (BasicDBList) respondObj.get(UserdbKey.CHECKOUT.CHECKER_LIST);
                if (checkList != null && !checkList.isEmpty()) {
                    for (Object checkList1 : checkList) {
                        BasicDBObject obj = (BasicDBObject) checkList1;
                        String uId = obj.getString(UserdbKey.CHECKOUT.CHECKER_ID);
                        if (!blackList.contains(uId)) {
                            result++;
                        }
                    }
                }
            }
        } catch (Exception ex) {
            Util.addErrorLog(ex);

            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static Map<String, FollowingUser> getCheckOutList(String userId, List<String> listCheckout) throws EazyException {
        Map<String, FollowingUser> result = new HashMap<>();
        try {
            ObjectId id = new ObjectId(userId);
            BasicDBObject updateQuery = new BasicDBObject(UserdbKey.CHECKOUT.ID, id);
            DBObject respondObj = coll.findOne(updateQuery);
            if (respondObj != null) {
                BasicDBList checkList = (BasicDBList) respondObj.get(UserdbKey.CHECKOUT.CHECKER_LIST);
                if (checkList != null && !checkList.isEmpty()) {
                    //get friend Id
                    for (int i = checkList.size() - 1; i >= 0; i--) {
                        BasicDBObject obj = (BasicDBObject) checkList.get(i);
                        FollowingUser user = new FollowingUser();
                        String checkId = obj.getString(UserdbKey.CHECKOUT.CHECKER_ID);
                        Long time = obj.getLong(UserdbKey.CHECKOUT.CHECKER_TIME);
                        user.userId = checkId;
                        user.checkoutTime = time;
                        listCheckout.add(checkId);
                        result.put(checkId, user);
                    }
                }
            }
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static List<String> getCheckOutListByUserId(String userId) throws EazyException {
        List<String> result = new ArrayList<>();
        try {
            ObjectId id = new ObjectId(userId);
            BasicDBObject updateQuery = new BasicDBObject(UserdbKey.CHECKOUT.ID, id);
            DBObject respondObj = coll.findOne(updateQuery);
            if (respondObj != null) {
                BasicDBList checkList = (BasicDBList) respondObj.get(UserdbKey.CHECKOUT.CHECKER_LIST);
                if (checkList != null && !checkList.isEmpty()) {
                    for (Object checkList1 : checkList) {
                        BasicDBObject obj = (BasicDBObject) checkList1;
                        String checkId = obj.getString(UserdbKey.CHECKOUT.CHECKER_ID);
                        result.add(checkId);
                    }
                }
            }
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static List<String> getNewCheckOutNumber(String userId, Long time) throws EazyException {
        List<String> result = new ArrayList<>();
        try {
            ObjectId id = new ObjectId(userId);
            BasicDBObject updateQuery = new BasicDBObject(UserdbKey.CHECKOUT.ID, id);
            DBObject respondObj = coll.findOne(updateQuery);
            if (respondObj != null) {
                BasicDBList checkList = (BasicDBList) respondObj.get(UserdbKey.CHECKOUT.CHECKER_LIST);
                if (checkList != null && !checkList.isEmpty()) {
                    for (Object checkList1 : checkList) {
                        BasicDBObject obj = (BasicDBObject) checkList1;
                        String checkId = obj.getString(UserdbKey.CHECKOUT.CHECKER_ID);
                        Long timeC = obj.getLong(UserdbKey.CHECKOUT.CHECKER_TIME);
                        if (timeC > time) {
                            result.add(checkId);
                        }
                    }
                }
            }
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static HashMap<String, HashMap<String, Long>> getAll() {
        HashMap<String, HashMap<String, Long>> result = new HashMap<>();
        try {
            DBCursor cursor = coll.find();
            while (cursor.hasNext()) {
                DBObject obj = cursor.next();
                String userId = obj.get(UserdbKey.CHECKOUT.ID).toString();
                BasicDBList checkoutList = (BasicDBList) obj.get(UserdbKey.CHECKOUT.CHECKER_LIST);
                if (checkoutList != null && !checkoutList.isEmpty()) {
                    HashMap<String, Long> checkoutMap = new  HashMap<>();
                    for (Object o : checkoutList) {
                        DBObject checkoutObj = (DBObject) o;
                        String friendId = (String) checkoutObj.get(UserdbKey.CHECKOUT.CHECKER_ID);
                        Long time = (Long) checkoutObj.get(UserdbKey.CHECKOUT.CHECKER_TIME);
                        checkoutMap.put(friendId, time);
                    }
                    result.put(userId, checkoutMap);
                }
            }
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
        return result;
    }
    //thanhdd add 23/01/2017
    public static boolean removeCheckOutFootprint(String userId, String chkId) throws EazyException {
        boolean result = false;
        try {
            
            ObjectId id = new ObjectId(userId);
            BasicDBObject query = new BasicDBObject(UserdbKey.CHECKOUT.ID, id);
            DBObject respondObj = coll.findOne(query);
            if (respondObj != null) {
                BasicDBList checkList = (BasicDBList) respondObj.get(UserdbKey.CHECKOUT.CHECKER_LIST);
                if (checkList != null && !checkList.isEmpty()) {
                    
                    for (int i = 0; i < checkList.size(); i++) {
                        BasicDBObject obj = (BasicDBObject) checkList.get(i);
                        String chk_Id = obj.getString(UserdbKey.CHECKOUT.CHECKER_ID);
                        Util.addDebugLog("====chk_Id:"+chk_Id);
                        if (chk_Id.equals(chkId)){
                           
//                            boolean check = checkList.remove(obj);
//                            Util.addDebugLog("removeCheckOutFootprint =================" + check);

                            BasicDBObject a = new BasicDBObject(UserdbKey.CHECKOUT.CHECKER_ID, chk_Id);
                            BasicDBObject command2 = new BasicDBObject(UserdbKey.CHECKOUT.CHECKER_LIST, a);
                            BasicDBObject command = new BasicDBObject("$pull", command2);
                            coll.update(query, command);
                            result = true;
                            
                            
                        }
                    }
                }
            }  
            
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }
}
