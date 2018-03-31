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
import com.mongodb.QueryBuilder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.ListPosition;
import eazycommon.constant.mongokey.UserdbKey;
import eazycommon.exception.EazyException;
import org.bson.types.ObjectId;
import eazycommon.constant.ParamKey;
import eazycommon.constant.StatePosition;
import com.vn.ntsc.usermanagementserver.dao.DatabaseLoader;
import com.vn.ntsc.usermanagementserver.entity.IEntity;
import com.vn.ntsc.usermanagementserver.entity.impl.user.Status;
import com.vn.ntsc.usermanagementserver.entity.impl.user.User;
import com.vn.ntsc.usermanagementserver.setting.Setting;

/**
 *
 * @author DuongLTD
 */
public class UserActivityDAO {

    private static DBCollection coll;
    
    static {
        try {
            DatabaseLoader.getUserDB().createCollection(UserdbKey.USER_ACTIVITY_COLLECTION, null);
            coll = DatabaseLoader.getUserDB().getCollection(UserdbKey.USER_ACTIVITY_COLLECTION);
            DBObject dbObj = new BasicDBObject(UserdbKey.USER_ACTIVITY.LOCATION, "2d");
            coll.createIndex(dbObj);
        } catch (Exception ex) {
            //Future logging here
            Util.addErrorLog(ex);            
           
        }
    }

    public static boolean updatedLocation(double longitude, double latitude, String userId) throws EazyException {
        boolean result = false;
        try {
            // search by userID
            ObjectId id = new ObjectId(userId);
            BasicDBObject query = new BasicDBObject(UserdbKey.USER_ACTIVITY.ID, id);

            //updated object
            BasicDBList location = new BasicDBList();
            location.add(longitude);
            location.add(latitude);
            BasicDBObject updateObject = new BasicDBObject(UserdbKey.USER_ACTIVITY.LOCATION, location);

            //command update
            BasicDBObject command = new BasicDBObject("$set", updateObject);
            coll.update(query, command, true, false);
            result = true;
        }catch (Exception ex) {
            Util.addErrorLog(ex);            
           
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static Map<String, Double> getLocation(String userId) throws EazyException {
        Map<String, Double> result = new TreeMap<>();
        try {

            //search by userId
            ObjectId id = new ObjectId(userId);
            BasicDBObject query = new BasicDBObject(UserdbKey.USER_ACTIVITY.ID, id);

            //command search
            DBObject obj = coll.findOne(query);

            BasicDBList location = (BasicDBList) obj.get(UserdbKey.USER_ACTIVITY.LOCATION);

            //get location
            if (location != null) {
                Double longitude = (Double) location.get(0);
                Double latitude = (Double) location.get(1);
                result.put(ParamKey.LONGITUDE, longitude);
                result.put(ParamKey.LATITUDE, latitude);
            } else {            
                result = null;
            }
        }catch (Exception ex) {
            Util.addErrorLog(ex);            
           
            result = null;
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static List<String> getListUser(double longitude, double latitude, int userNumber, List<String> listUser) throws EazyException {
        List<String> result = new ArrayList<>();
        ListPosition lp = ListPosition.getInstance();
        StatePosition sp = null;
        try {
            BasicDBObject sortObj = new BasicDBObject(UserdbKey.USER_ACTIVITY.LAST_ACTIVITY, -1);
            try{
                DBCursor cursor = coll.find().sort(sortObj).limit(userNumber * 3);
                int count = 0;
                if (cursor != null) {
                    while (cursor.hasNext()) {
                        DBObject obj = cursor.next();
                        ObjectId userId = (ObjectId) obj.get(UserdbKey.USER_ACTIVITY.ID);
                        if(listUser.contains(userId.toString())){
                            BasicDBList location = (BasicDBList) obj.get(UserdbKey.USER_ACTIVITY.LOCATION);
                            double distance = 0;
                            if(location != null){
                                distance = Util.calculateDistance(latitude, longitude, (Double)location.get(1), (Double)location.get(0));
                            }else{                                
                                // LongLT 11 Oct2016 ///////////////////////////  #4783 START   
                                sp = lp.getStatePosistion(UserDAO.getUserInfor(userId.toString()).region.intValue());       
                                if(sp!=null){
                                    distance = Util.calculateDistance(latitude, longitude, sp.latitude, sp.longtitude);
                                }else {
                                    distance = Util.calculateDistance(latitude, longitude, 0, 0);
                                }        
//                                distance = Util.calculateDistance(latitude, longitude, 0, 0);
                                // LongLT 11 Oct2016 /////////////////////////// #4783 END 
                            }
                            if(distance < Setting.LOCAL_DISTANCE){
                                result.add(userId.toString());
                                count ++;
                            }
                            if(count == userNumber)
                                break;
                        }
                    }
                }
            }catch(Exception ex){
                Util.addErrorLog(ex);
            }
        }catch (Exception ex) {
            Util.addErrorLog(ex);            
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static boolean updateLastActivity(String userId, long time) throws EazyException {
        boolean result = false;
        try {
            ObjectId id = new ObjectId(userId);
            BasicDBObject findObj = new BasicDBObject(UserdbKey.USER_ACTIVITY.ID, id);
//            DBObject obj = coll.findOne(findObj);
            BasicDBObject updateObj = new BasicDBObject(UserdbKey.USER_ACTIVITY.LAST_ACTIVITY, time);
            BasicDBObject setObj = new BasicDBObject("$set", updateObj);
            coll.update(findObj, setObj, true, false);
            result = true;
        }catch (Exception ex) {
            Util.addErrorLog(ex);            
           
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static boolean updateStatus(String userId, String status) throws EazyException {
        boolean result = false;
        try {
            ObjectId id = new ObjectId(userId);
            BasicDBObject findObj = new BasicDBObject(UserdbKey.USER_ACTIVITY.ID, id);
            BasicDBObject updateObj = new BasicDBObject(UserdbKey.USER_ACTIVITY.STATUS, status);
            BasicDBObject setObj = new BasicDBObject("$set", updateObj);
            coll.update(findObj, setObj, true, false);
            result = true;
        }catch (Exception ex) {
            Util.addErrorLog(ex);            
           
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static boolean updateLastOnline(String userId, long lastOnline, Date time) throws EazyException {
        boolean result = false;
        try {
            ObjectId id = new ObjectId(userId);
            BasicDBObject findObj = new BasicDBObject(UserdbKey.USER_ACTIVITY.ID, id);
            BasicDBObject updateObj = new BasicDBObject(UserdbKey.USER_ACTIVITY.LAST_ONLINE, lastOnline);
            updateObj.append(UserdbKey.USER_ACTIVITY.LAST_LOGIN, DateFormat.format(time));
            BasicDBObject setObj = new BasicDBObject("$set", updateObj);
            coll.update(findObj, setObj, true, false);
            result = true;
        }catch (Exception ex) {
            Util.addErrorLog(ex);            
           
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static String getStatus(String userId) throws EazyException {
        String result = null;
        try {
            ObjectId id = new ObjectId(userId);
            BasicDBObject findObj = new BasicDBObject(UserdbKey.USER_ACTIVITY.ID, id);
            DBObject obj = coll.findOne(findObj);
            if (obj != null) {
                String status = (String) obj.get(UserdbKey.USER_ACTIVITY.STATUS);
                result = status;
            }
        }catch (Exception ex) {
            Util.addErrorLog(ex);            
           
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static Date getLastOnline(String userId) throws EazyException {
        Date result = null;
        try {
            ObjectId id = new ObjectId(userId);
            BasicDBObject findObj = new BasicDBObject(UserdbKey.USER_ACTIVITY.ID, id);
            DBObject obj = coll.findOne(findObj);
            if (obj != null) {
                Long time = (Long) obj.get(UserdbKey.USER_ACTIVITY.LAST_ONLINE);
                if (time != null) {
                    result = new Date(time);
                }
            }
        }catch (Exception ex) {
            Util.addErrorLog(ex);            
           
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static void getListStatus(List<User> listUser) throws EazyException {
        try {
            List<ObjectId> listUserId = new ArrayList<>();
            for (User listUser1 : listUser) {
                String userId = listUser1.userId;
                listUserId.add(new ObjectId(userId));
            }
            DBObject query = QueryBuilder.start(UserdbKey.USER_ACTIVITY.ID).in(listUserId).get();
            DBCursor cursor = coll.find(query);
            while (cursor.hasNext()) {
                DBObject obj = cursor.next();
                ObjectId userId = (ObjectId) obj.get(UserdbKey.USER_ACTIVITY.ID);
                String status = (String) obj.get(UserdbKey.USER_ACTIVITY.STATUS);
                for(User user: listUser){
                    if(user.userId.equals(userId.toString())){
                        user.status = status;
                        break;
                    }
                }
            }
        }catch (Exception ex) {
            Util.addErrorLog(ex);            
           
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
    }    
    
    public static List<IEntity> getListStatusbyListUserId(List<String> listUserId) throws EazyException {
        List<IEntity> result = new ArrayList<IEntity>();
        try {
            List<ObjectId> listId = new ArrayList<ObjectId>();
            for (String listUserId1 : listUserId) {
                listId.add(new ObjectId(listUserId1));
            }
            DBObject query = QueryBuilder.start(UserdbKey.USER_ACTIVITY.ID).in(listId).get();
            DBCursor cursor = coll.find(query);
            while (cursor.hasNext()) {
                DBObject obj = cursor.next();
                ObjectId userId = (ObjectId) obj.get(UserdbKey.USER_ACTIVITY.ID);
                String status = (String) obj.get(UserdbKey.USER_ACTIVITY.STATUS);
                if (status != null) {
                    Status stt = new Status(userId.toString(), status);
                    result.add(stt);
                }
            }
        }catch (Exception ex) {
            Util.addErrorLog(ex);            
           
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static boolean removeStatus(String userId) throws EazyException {
        boolean result = false;
        try {
            ObjectId id = new ObjectId(userId);
            BasicDBObject findObj = new BasicDBObject(UserdbKey.USER_ACTIVITY.ID, id);
            BasicDBObject updateObj = new BasicDBObject(UserdbKey.USER_ACTIVITY.STATUS, 1);
            BasicDBObject unsetObj = new BasicDBObject("$unset", updateObj);
            coll.update(findObj, unsetObj);
            result = true;
        }catch (Exception ex) {
            Util.addErrorLog(ex);            
           
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static boolean deactivate(String userId) throws EazyException {
        boolean result = false;
        try {
            ObjectId id = new ObjectId(userId);
            BasicDBObject findObj = new BasicDBObject(UserdbKey.USER_ACTIVITY.ID, id);
            BasicDBObject updateObj = new BasicDBObject(UserdbKey.USER_ACTIVITY.LOCATION, 1);
            BasicDBObject unsetObj = new BasicDBObject("$unset", updateObj);
            coll.update(findObj, unsetObj);
            result = true;
        }catch (Exception ex) {
            Util.addErrorLog(ex);            
           
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

}
