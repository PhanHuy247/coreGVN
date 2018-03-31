/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.jpns.dao.impl;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import java.util.LinkedList;
import eazycommon.constant.Constant;
import eazycommon.constant.mongokey.JPNSdbKey;
import eazycommon.dao.CommonDAO;
import eazycommon.util.Util;
import com.vn.ntsc.jpns.dao.IDAO;
import com.vn.ntsc.jpns.dao.pojos.Device;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author tuannxv00804
 */
public class MongoDAO implements IDAO {

    private DB db;
    private DBCollection devices;

    public MongoDAO() {
        try {
            db = CommonDAO.mongo.getDB(JPNSdbKey.DB_NAME);
            devices = db.getCollection(JPNSdbKey.DEVICE_COLLECTION);
            devices.createIndex(new BasicDBObject(Device.UserID, 1).append("_id", -1).append(time_field, -1));
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
    }

    private static final String time_field = "time";

    public static BasicDBObject userToDBObject(Device user) {
        if (user == null) {
            return null;
        }
        BasicDBObject o = new BasicDBObject(Device.UserID, user.userid)
                .append(Device.DeviceType, user.deviceType)
                .append(Device.Username, user.username);
        if (user.deviceToken != null) {
            o.append(Device.DeviceToken, user.deviceToken);
        }
        if (user.deviceId != null) {
            o.append(Device.DeviceID, user.deviceId);
        }
        if (user.checkid != null) {
            o.append(Device.CheckID, user.checkid);
        }
        if (user.iosApplicationType != null) {
            o.append(Device.IosApplicationType, user.iosApplicationType);
        }
        if (user.applicationId != null) {
            o.append(Device.ApplicationID, user.applicationId);
        }
        if (user.voip_notify_token != null) {
            o.append(Device.Voip_notify_token, user.voip_notify_token);
        }

        return o;
    }

    public static Device dbObjectToUser(BasicDBObject o) {
        if (o == null) {
            return null;
        }
        try {
            String userid = o.getString(Device.UserID);
            int deviceType = o.getInt(Device.DeviceType);
            String deviceToken = o.getString(Device.DeviceToken);
            String username = o.getString(Device.Username);
            Integer iosApplicationType = (Integer) o.get(Device.IosApplicationType);
            if (iosApplicationType == null && deviceType == Constant.DEVICE_TYPE.IOS) {
                iosApplicationType = Constant.APPLICATION_TYPE.IOS_ENTERPRISE_APPLICATION;
            }

            //HUNGDT add 16-04-2016
            String deviceId = o.getString(Device.DeviceID);
            String checkid = o.getString(Device.CheckID);
            //END
            String applicationId = o.getString(Device.ApplicationID);
            String voip_notify_token = o.getString(Device.Voip_notify_token);

            Device u = new Device(userid, username, deviceType, deviceToken, iosApplicationType, deviceId, checkid, applicationId,voip_notify_token);
            return u;
        } catch (Exception ex) {
            Util.addErrorLog(ex);

            return null;
        }
    }

    @Override
    public void add(Device user) {
        BasicDBObject query = new BasicDBObject(Device.UserID, user.userid)
                .append(Device.DeviceToken, user.deviceToken)
                .append(Device.DeviceID, user.deviceId);
        DBCursor cursor = devices.find(query);
        if (cursor.count() == 0) {
            BasicDBObject update = userToDBObject(user);
            update.append(time_field, Util.currentTime());
            devices.save(update);
        } else {
            devices.update(query, new BasicDBObject("$set", new BasicDBObject(Device.Username, user.username)), false, true);
//            while(cursor.hasNext()){
//                DBObject obj = cursor.next();
//                String userName = (String) obj.get(Device.Username);
//                if(!user.username.equals(userName)){
//                    BasicDBObject update = userToDBObject( user );
//                    devices.update( obj, update );
//                }
//            }
        }
        cursor.close();
    }

    private static final BasicDBObject sortByIdAndTimeDesc = new BasicDBObject("_id", -1).append(time_field, -1);

//    @Override
//    public LinkedList<Device> getDevices(String userid) {
//        BasicDBObject query = new BasicDBObject(Device.UserID, userid);
//        DBCursor cursor = devices.find(query).sort(sortByIdAndTimeDesc).limit(1);
//        LinkedList<Device> ll = new LinkedList<>();
//        while (cursor.hasNext()) {
//            BasicDBObject o = (BasicDBObject) cursor.next();
//            ll.add(dbObjectToUser(o));
//        }
//        cursor.close();
//        return ll;
//    }
    
    @Override
    public LinkedList<Device> getDevices(String userid) {
        BasicDBObject query = new BasicDBObject(Device.UserID, userid);
        DBCursor cursor = devices.find(query);
        LinkedList<Device> ll = new LinkedList<>();
        while (cursor.hasNext()) {
            BasicDBObject o = (BasicDBObject) cursor.next();
            ll.add(dbObjectToUser(o));
        }
        cursor.close();
        return ll;
    }
    
    @Override
    public Device getDevice(String deviceId) {
        BasicDBObject query = new BasicDBObject(Device.DeviceID, deviceId);
        DBCursor cursor = devices.find(query);
        BasicDBObject o = (BasicDBObject) cursor.next();
        return dbObjectToUser(o);
    }
    
    @Override
    public List<String> getListDeviceId(String userId){
        List<String> result = new ArrayList<>();
        BasicDBObject query = new BasicDBObject(Device.UserID, userId);
        DBCursor cursor = devices.find(query);
        while (cursor.hasNext()) {
            BasicDBObject o = (BasicDBObject) cursor.next();
            if (o == null) {
                continue;
            }
            String deviceid = (String)o.getString("deviceid");
            result.add(deviceid);
        }
        return result;
    }

    @Override
    public String getUsername(String userid) {
        BasicDBObject query = new BasicDBObject(Device.UserID, userid);
        DBCursor cursor = devices.find(query);
        String username = null;
        if (cursor.hasNext()) {
            BasicDBObject o = (BasicDBObject) cursor.next();
            username = (String) o.get(Device.Username);
        }
        cursor.close();
        return username;
    }

    @Override
    public void remove(Device user) {
        if (user == null || user.userid == null || user.userid.isEmpty()) {
            return;
        }
//        BasicDBObject query = userToDBObject( user );
        BasicDBObject query = new BasicDBObject(Device.UserID, user.userid)
                .append(Device.DeviceToken, user.deviceToken);
        devices.remove(query);
    }

    @Override
    public void removeDuplicatedOrEmtyTokenDevice(Device user) {
        if (user.deviceToken != null && !user.deviceToken.isEmpty()) {
            BasicDBObject query = new BasicDBObject(Device.DeviceToken, user.deviceToken);
            devices.remove(query);
//            if( cursor.count() > 0 ){
//                while(cursor.hasNext()){
//                    DBObject obj = cursor.next();
//                    devices.remove(obj);
//                }
//            }
//            cursor.close();
        }
        BasicDBObject query = new BasicDBObject(Device.UserID, user.userid);
        DBCursor cursor = devices.find(query);
        if (cursor.count() > 0) {
            while (cursor.hasNext()) {
                DBObject obj = cursor.next();
                String deviceToken = (String) obj.get(Device.DeviceToken);
                if (deviceToken == null || deviceToken.isEmpty()) {
                    devices.remove(obj);
                }
            }
        }
        cursor.close();
    }

    @Override
    public void removeDuplicatedDeviceId(Device user) {
        if (user.deviceId != null && !user.deviceId.isEmpty()) {
            BasicDBObject query = new BasicDBObject(Device.DeviceID, user.deviceId);
//            query.append(Device.DeviceType, Constant.DEVICE_TYPE.IOS);
            devices.remove(query);
//            if( cursor.count() > 0 ){
//                while(cursor.hasNext()){
//                    DBObject obj = cursor.next();
//                    devices.remove(obj);
//                }
//            }
//            cursor.close();
        }
    }

    @Override
    public void removeUser(String userId) {
        BasicDBObject query = new BasicDBObject(Device.UserID, userId);
        devices.remove(query);
    }

    @Override
    public void updateUserName(String userId, String userName) {
        BasicDBObject query = new BasicDBObject(Device.UserID, userId);
        DBObject updateObj = new BasicDBObject(Device.Username, userName);
        DBObject set = new BasicDBObject("$set", updateObj);
        devices.update(query, set, false, true);
//        DBCursor cursor = devices.find( query );
//        if( cursor.count() > 0 ){
//            while(cursor.hasNext()){
//                DBObject obj = cursor.next();
//                DBObject updateObj = new BasicDBObject(Device.Username, userName);
//                DBObject set = new BasicDBObject("$set", updateObj);
//                devices.update(obj, set);
//            }
//        }
//        cursor.close();      
    }

    @Override
    public boolean isExistDevice(String userId, String devicetoken) {
        boolean result = false;
        BasicDBObject query = new BasicDBObject(Device.UserID, userId);
        query.append(Device.DeviceToken, devicetoken);
        DBObject obj = devices.findOne(query);
        if (obj != null) {
            result = true;
        }
        return result;
    }

    @Override
    public void updateDeviceToken(Device device, String newDeviceToken) {
        BasicDBObject query = new BasicDBObject(Device.UserID, device.userid);
        query.append(Device.DeviceToken, device.deviceToken);
        BasicDBObject update = new BasicDBObject("$set", new BasicDBObject(Device.DeviceToken, newDeviceToken));
        devices.update(query, update);
    }

    @Override
    public void removeByToken(String token) {
        BasicDBObject query = new BasicDBObject(Device.DeviceToken, token);
        devices.remove(query);
    }

}
