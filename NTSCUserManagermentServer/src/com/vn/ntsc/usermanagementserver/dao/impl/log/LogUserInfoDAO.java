/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.usermanagementserver.dao.impl.log;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import eazycommon.constant.mongokey.LogdbKey;
import eazycommon.util.Util;
import org.bson.types.ObjectId;
import com.vn.ntsc.usermanagementserver.dao.DatabaseLoader;
import com.vn.ntsc.usermanagementserver.entity.impl.user.User;

/**
 *
 * @author Administrator
 */
public class LogUserInfoDAO {
    private static DBCollection coll;

    static {
        try {
            coll = DatabaseLoader.getLogDB().getCollection(LogdbKey.USER_INFO_LOG_COLLECTION);
        } catch (Exception ex) {
            //Future logging here
            Util.addErrorLog(ex);            
           
        }
    }
    
    public static void insert(String id, User user, User beforeUser, Long time, int approvedFlag){
        BasicDBObject insertObj = new BasicDBObject();
        if (id != null){
            insertObj.append(LogdbKey.USER_INFO_LOG.ID, new ObjectId(id));
        }
        if(user.about!= null && !user.about.equalsIgnoreCase(beforeUser.about)){
            insertObj.append(LogdbKey.USER_INFO_LOG.ABOUT, user.about);
        }
        insertObj.append(LogdbKey.USER_INFO_LOG.USER_ID, beforeUser.userId);
        insertObj.append(LogdbKey.USER_INFO_LOG.GENDER, beforeUser.gender.intValue());
        insertObj.append(LogdbKey.USER_INFO_LOG.USER_TYPE, beforeUser.userType.intValue());
//        insertObj.append(LogdbKey.USER_INFO_LOG.APPLICATION_ID, new Integer(beforeUser.applicationId));
        insertObj.append(LogdbKey.USER_INFO_LOG.UPDATE_TIME, time);
        insertObj.append(LogdbKey.USER_INFO_LOG.APPROVED_FLAG, approvedFlag);
        
        coll.insert(insertObj);
    }
}
