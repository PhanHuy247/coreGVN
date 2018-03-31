/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.dao.impl;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.vn.ntsc.dao.DBLoader;
import static com.vn.ntsc.dao.impl.UserSessionDAO.remove;
import com.vn.ntsc.eazyserver.server.session.Session;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.mongokey.UserdbKey;
import eazycommon.exception.EazyException;
import eazycommon.util.Util;
import java.util.List;

/**
 *
 * @author Phan Huy
 */
public class UserTokenDAO {
    private static DBCollection coll;
  
    static{
         try{
             coll = DBLoader.getUserDB().getCollection( UserdbKey.USER_TOKEN_COLLECTION );
        } catch( Exception ex ) {
            Util.addErrorLog(ex);            
        }
    }    
    
    public static boolean add(Session session)throws EazyException{
        boolean result = false;
        try {
            BasicDBObject find = new BasicDBObject();
            find.append(UserdbKey.USER_TOKEN.USER_ID, session.userID);
            find.append(UserdbKey.USER_TOKEN.DEVICE_ID, session.deviceId);
            DBObject object = coll.findOne(find);
            if(object == null){
                BasicDBObject insert = new BasicDBObject();
                insert.append(UserdbKey.USER_TOKEN.USER_ID, session.userID);
                insert.append(UserdbKey.USER_TOKEN.TOKEN, session.token);
                insert.append(UserdbKey.USER_TOKEN.DEVICE_ID, session.deviceId);
                insert.append(UserdbKey.USER_TOKEN.NORMARL_USER_FLAG, session.normalUser);
                insert.append(UserdbKey.USER_TOKEN.APPLICATION_VERSION, session.applicationVersion);
                if(session.applicationType != null)
                    insert.append(UserdbKey.USER_SESSION.APPLICATION_TYPE, session.applicationType);
                coll.insert(insert);
            }else{
                String oldToken = (String)object.get("token");
                BasicDBObject update = new BasicDBObject(UserdbKey.USER_TOKEN.OLD_TOKEN, oldToken);
                update.append(UserdbKey.USER_TOKEN.TOKEN, session.token);
                update.append(UserdbKey.USER_TOKEN.NORMARL_USER_FLAG, session.normalUser);
                update.append(UserdbKey.USER_TOKEN.APPLICATION_VERSION, session.applicationVersion);
                if(session.applicationType != null)
                    update.append(UserdbKey.USER_TOKEN.APPLICATION_TYPE, session.applicationType);
                BasicDBObject set = new BasicDBObject("$set", update);
                coll.update(find, set);
            }
        }catch( Exception ex ) {
            Util.addErrorLog(ex);            
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }  
    public static Session getInfoSession(String token)throws EazyException{
        Session result = null;
        try {
            DBObject newtoken = new BasicDBObject(UserdbKey.USER_TOKEN.TOKEN, token);  
//            DBObject oldToken = new BasicDBObject(UserdbKey.USER_TOKEN.OLD_TOKEN, token);    
//            BasicDBList or = new BasicDBList();
//            or.add(newtoken);
//            or.add(oldToken);
//            DBObject query = new BasicDBObject("$or", or);
            DBObject object = coll.findOne(newtoken);
            if(object != null){
                result = new Session();
                result.userID = (String) object.get("user_id");
                result.applicationType = (Integer) object.get(UserdbKey.USER_TOKEN.APPLICATION_TYPE);
                result.applicationVersion = (String) object.get(UserdbKey.USER_TOKEN.APPLICATION_VERSION);
                result.deviceId = (String) object.get(UserdbKey.USER_TOKEN.DEVICE_ID);
            }
        }catch( Exception ex ) {
            Util.addErrorLog(ex);            
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }  
    
    public static Session getInfoSessionOldToken(String token)throws EazyException{
        Session result = new Session();
        try {
            DBObject oldToken = new BasicDBObject(UserdbKey.USER_TOKEN.OLD_TOKEN, token);    
            DBObject object = coll.findOne(oldToken);
            if(object != null){
                result.userID = (String) object.get("user_id");
                result.applicationType = (Integer) object.get(UserdbKey.USER_TOKEN.APPLICATION_TYPE);
                result.applicationVersion = (String) object.get(UserdbKey.USER_TOKEN.APPLICATION_VERSION);
                result.deviceId = (String) object.get(UserdbKey.USER_TOKEN.DEVICE_ID);
                result.token = (String)object.get(UserdbKey.USER_TOKEN.TOKEN);
            }
        }catch( Exception ex ) {
            Util.addErrorLog(ex);            
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }  
    
    public static boolean remove(List<Session> listSession)throws EazyException{
        boolean result = false;
        try {
            for(Session session : listSession){
                BasicDBObject token = new BasicDBObject("token",session.token);
                BasicDBObject oldToken = new BasicDBObject("old_token",session.token);
                BasicDBList or = new BasicDBList();
                or.add(token);
                or.add(oldToken);
                BasicDBObject query = new BasicDBObject("$or", or);
                query.append("user_id",session.userID);
                DBObject cursor = coll.findOne(query);
                coll.remove(cursor);
            }
        }catch( Exception ex ) {
            Util.addErrorLog(ex);            
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }  
     
    public static boolean removeOldToken(Session session){
        boolean result = false;
        try {
            BasicDBObject remove = new BasicDBObject();
            remove.append(UserdbKey.USER_TOKEN.OLD_TOKEN, session.token);
            coll.remove(remove);
            result = true;
        }catch( Exception ex ) {
            Util.addErrorLog(ex);            
        }
        return result;
    }    
}
