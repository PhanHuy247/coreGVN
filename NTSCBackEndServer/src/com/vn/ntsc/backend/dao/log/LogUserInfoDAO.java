/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.backend.dao.log;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import eazycommon.constant.mongokey.LogdbKey;
import eazycommon.constant.mongokey.SettingdbKey;
import eazycommon.exception.EazyException;
import eazycommon.util.DateFormat;
import eazycommon.util.Util;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bson.types.ObjectId;
import com.vn.ntsc.backend.dao.DBManager;
import com.vn.ntsc.backend.dao.admin.AdminDAO;
import com.vn.ntsc.backend.entity.IEntity;
import com.vn.ntsc.backend.entity.impl.datarespond.SizedListData;
import com.vn.ntsc.backend.entity.impl.log.LogUserInfo;
import com.vn.ntsc.backend.entity.impl.user.ReviewingUser;
import com.vn.ntsc.backend.entity.impl.user.User;

/**
 *
 * @author Administrator
 */
public class LogUserInfoDAO {
    private static DBCollection coll;

    static {
        try {
            coll = DBManager.getLogDB().getCollection(LogdbKey.USER_INFO_LOG_COLLECTION);
        } catch (Exception ex) {
            //Future logging here
            Util.addErrorLog(ex);            
           
        }
    }
       
    public static void insert(String id, Long type, Long time, Integer gender, String about,  String userDeny){
        BasicDBObject insertObj = new BasicDBObject();
//        if (id != null){
//            insertObj.append(LogdbKey.USER_INFO_LOG.ID, new ObjectId(id));
//        }
        insertObj.append(LogdbKey.USER_INFO_LOG.USER_ID, id);
        insertObj.append(LogdbKey.USER_INFO_LOG.APPROVED_FLAG, type);
        insertObj.append(LogdbKey.USER_INFO_LOG.UPDATE_TIME, time);
        insertObj.append(LogdbKey.USER_INFO_LOG.GENDER, gender);
        insertObj.append(LogdbKey.USER_INFO_LOG.ABOUT, about);
//        
//        IEntity userAdmin;
//            String userAdminName ="";
//            try {
//                if (userDeny!=null){
//                    userAdmin = AdminDAO.getDetail(userDeny);
//                    userAdminName=userAdmin.toJsonObject().get(SettingdbKey.ADMINISTRATOR.NAME).toString();
//                }
//            } catch (EazyException ex) {
//                Logger.getLogger(LogUserInfoDAO.class.getName()).log(Level.SEVERE, null, ex);
//            }
        insertObj.append(LogdbKey.USER_INFO_LOG.USER_DENY, userDeny);
        coll.insert(insertObj);
    }
    
    public static void update(ObjectId id, ReviewingUser user, Long about, Long hobby, Long typeOfMan, Long fetish){
        BasicDBObject find = new BasicDBObject(LogdbKey.USER_INFO_LOG.ID, id);
        BasicDBObject query = new BasicDBObject();
        if (user.about != null && about != null){
            query.append("about_flag", about.intValue());
        }
        if (user.hobby != null && hobby != null){
            query.append("hobby_flag", hobby.intValue());
        }
        if (user.typeOfMan != null && typeOfMan != null){
            query.append("typeOfMan_flag", typeOfMan.intValue());
        }
        if (user.fetish != null && fetish != null){
            query.append("fetish_flag", fetish.intValue());
        }
        BasicDBObject update = new BasicDBObject("$set", query);
        coll.update(find, update);
        
        BasicDBObject update2 = new BasicDBObject("$unset", LogdbKey.USER_INFO_LOG.APPROVED_FLAG);
        coll.update(find, update2);
    }

    public static SizedListData getLog(String userId, Long gender, String fromTime, String toTime, Long skip, Long take) {
        SizedListData result = new SizedListData();
        DBObject query = new BasicDBObject();
        if (userId != null){
            query.put(LogdbKey.USER_INFO_LOG.USER_ID, userId);
        }
        if (gender != null){
            query.put(LogdbKey.USER_INFO_LOG.GENDER, gender);
        }
        if (fromTime != null){
            Long time = DateFormat.parse(fromTime).getTime();
            query.put(LogdbKey.USER_INFO_LOG.UPDATE_TIME, new BasicDBObject("$gte", time));
        }
        if (toTime != null){
            Long time = DateFormat.parse(toTime).getTime();
            query.put(LogdbKey.USER_INFO_LOG.UPDATE_TIME, new BasicDBObject("$lte", time));
        }
        BasicDBObject sort = new BasicDBObject(LogdbKey.USER_INFO_LOG.UPDATE_TIME, -1);
        Util.addDebugLog("============= query: "+query.toString());
        DBCursor cursor = coll.find(query).sort(sort);
        result.total = cursor.size();
        
        cursor.skip(skip.intValue()).limit(take.intValue());
        while (cursor.hasNext()){
            DBObject obj = cursor.next();
            LogUserInfo log = new LogUserInfo();
            log.userId = (String) obj.get(LogdbKey.USER_INFO_LOG.USER_ID);
            log.userType = (Integer) obj.get(LogdbKey.USER_INFO_LOG.USER_TYPE);
            log.appId = (Integer) obj.get(LogdbKey.USER_INFO_LOG.APPLICATION_ID);
            log.gender = (Integer) obj.get(LogdbKey.USER_INFO_LOG.GENDER);
            log.about = (String) obj.get(LogdbKey.USER_INFO_LOG.ABOUT);
//            log.app_flag = (Integer) obj.get(LogdbKey.USER_INFO_LOG.APPROVED_FLAG);
            Object appFlag = obj.get(LogdbKey.USER_INFO_LOG.APPROVED_FLAG);
            if (appFlag instanceof Long){
                log.app_flag = ((Long) appFlag).intValue();
            }
            else if (appFlag instanceof Integer){
                log.app_flag = (Integer) appFlag;
            }
            log.time = (Long) obj.get(LogdbKey.USER_INFO_LOG.UPDATE_TIME);
            log.timeStr = DateFormat.format_yyyyMMddHHmm(log.time);
//            log.timeStr = DateFormat.format_yyyyMMddHHmm(Util.getGMT(log.time));
            log.user_deny_name = (String) obj.get(LogdbKey.USER_INFO_LOG.USER_DENY);
            Util.addDebugLog("=====user_deny_name:"+log.user_deny_name);
            result.ll.add(log);
        }
        
        return result;
    }

    public static void update(ObjectId id, String userId, Long type, long time, Integer gender, String about, String userDeny) {
        BasicDBObject findObj = new BasicDBObject(LogdbKey.USER_INFO_LOG.ID, id);
        
        BasicDBObject update = new BasicDBObject();
        update.append(LogdbKey.USER_INFO_LOG.USER_ID, userId);
        update.append(LogdbKey.USER_INFO_LOG.APPROVED_FLAG, type);
        update.append(LogdbKey.USER_INFO_LOG.UPDATE_TIME, time);
        update.append(LogdbKey.USER_INFO_LOG.GENDER, gender);
        update.append(LogdbKey.USER_INFO_LOG.ABOUT, about);
        
//        IEntity userAdmin;
//            String userAdminName ="";
//            try {
//                if (userDeny!=null){
//                    userAdmin = AdminDAO.getDetail(userDeny);
//                    userAdminName=userAdmin.toJsonObject().get(SettingdbKey.ADMINISTRATOR.NAME).toString();
//                }
//            } catch (EazyException ex) {
//                Logger.getLogger(LogUserInfoDAO.class.getName()).log(Level.SEVERE, null, ex);
//            }
        update.append(LogdbKey.USER_INFO_LOG.USER_DENY, userDeny);
        
        BasicDBObject updateObject = new BasicDBObject("$set", update);
        coll.update(findObj, updateObject);
    }
}
