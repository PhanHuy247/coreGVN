/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.dao.impl;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import eazycommon.constant.mongokey.JPNSdbKey;
import eazycommon.dao.CommonDAO;
import java.util.LinkedList;

import eazycommon.exception.EazyException;
import eazycommon.util.Util;
import com.vn.ntsc.eazyserver.server.Device;

/**
 *
 * @author tuannxv00804
 */
public class DeviceDAO {

    public static final String DBName = "jpns";
    public static final String UserCollectionName = "devices";

    private DB db;
    private DBCollection devices;

    public DeviceDAO() {
        try {
            db = CommonDAO.mongo.getDB( JPNSdbKey.DB_NAME );
            devices = db.getCollection(UserCollectionName);
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
    }

    public LinkedList<Device> getDevices() {
        BasicDBObject query = new BasicDBObject();
        DBCursor cursor = devices.find(query);
        LinkedList<Device> ll = new LinkedList<Device>();
        while (cursor.hasNext()) {
            BasicDBObject o = (BasicDBObject) cursor.next();
            ll.add(dbObjectToUser(o));
        }
        cursor.close();
        return ll;
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
            String deviceid = o.getString(Device.DeviceID);

            String checkid = o.getString(Device.CheckID);

            Device u = new Device(userid, username, deviceType, deviceToken, deviceid, checkid);
            return u;
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            return null;
        }
    }

}
