/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.backend.dao.user;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.QueryBuilder;
import eazycommon.constant.Constant;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.mongokey.UserdbKey;
import eazycommon.exception.EazyException;
import eazycommon.util.DateFormat;
import eazycommon.util.Util;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.bson.types.ObjectId;
import com.vn.ntsc.backend.dao.DBManager;
import com.vn.ntsc.backend.entity.IEntity;



/**
 *
 * @author DuongLTD
 */
public class NotificationDAO {

    private static DBCollection coll;

    static {
        try {
            coll = DBManager.getUserDB().getCollection(UserdbKey.NOTIFICATION_COLLECTION);
            coll.createIndex(new BasicDBObject(UserdbKey.NOTIFICATION.TIME, 1));
        } catch (Exception ex) {
            //Future logging here
            Util.addErrorLog(ex);

        }
    }

//    public static String insert(String url, String message, String ip, String time, String toJSONString) {
//        String result = "";
//        try {
//            DBObject insertObj = new BasicDBObject();
//            insertObj.put(UserdbKey.NOTIFICATION.AUTO_NOTIFY.URL, url);
//            insertObj.put(SettingdbKey.AUTO_MESSAGE.MESSAGE, content);
//            insertObj.put(SettingdbKey.AUTO_MESSAGE.TIME, time);
//            insertObj.put(SettingdbKey.AUTO_MESSAGE.QUERY, query);
//            insertObj.put(SettingdbKey.AUTO_MESSAGE.AUTO_NOTIFY.IP, ip);
//            insertObj.put(SettingdbKey.AUTO_MESSAGE.FLAG, Constant.FLAG.ON);
//           
//            coll.insert(insertObj);
//            result = insertObj.get(SettingdbKey.AUTO_MESSAGE.ID).toString();
//           ///System.out.println("commonDAO : " + result);
//        } catch (Exception ex) {
//           // System.out.println("insert error");
//            Util.addErrorLog(ex);
//        }
//        return result;
//    }

    

}
