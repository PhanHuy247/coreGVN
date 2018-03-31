/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.vn.ntsc.dao.impl;


import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.QueryBuilder;
import java.util.ArrayList;
import java.util.List;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.mongokey.UserdbKey;
import eazycommon.exception.EazyException;
import eazycommon.util.Util;
import org.bson.types.ObjectId;
import com.vn.ntsc.dao.DBLoader;
import com.vn.ntsc.eazyserver.server.session.Session;

/**
 *
 * @author DuongLTD
 */
public class UserSessionDAO {

    private static DBCollection coll;
  
    static{
         try{
             coll = DBLoader.getUserDB().getCollection( UserdbKey.USER_SESSION_COLLECTION );
        } catch( Exception ex ) {
            Util.addErrorLog(ex);            
        }
    }    

    public static boolean add(Session session)throws EazyException{
        boolean result = false;
        try {
            BasicDBObject insert = new BasicDBObject();
            insert.append(UserdbKey.USER_SESSION.USER_ID, session.userID);
            insert.append(UserdbKey.USER_SESSION.TOKEN, session.token);
            insert.append(UserdbKey.USER_SESSION.NORMARL_USER_FLAG, session.normalUser);
            insert.append(UserdbKey.USER_SESSION.APPLICATION_VERSION, session.applicationVersion);
            if(session.applicationType != null)
                insert.append(UserdbKey.USER_SESSION.APPLICATION_TYPE, session.applicationType);
            coll.insert(insert);
        }catch( Exception ex ) {
            Util.addErrorLog(ex);            
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }  
    public static boolean update(Session session)throws EazyException{
        boolean result = false;
        try {
            BasicDBObject find = new BasicDBObject();
            find.append(UserdbKey.USER_SESSION.USER_ID, session.userID);
            find.append(UserdbKey.USER_SESSION.TOKEN, session.token);
            BasicDBObject update = new BasicDBObject(UserdbKey.USER_SESSION.NORMARL_USER_FLAG, session.normalUser);
            BasicDBObject set = new BasicDBObject("$set", update);
            coll.update(find, set);
        }catch( Exception ex ) {
            Util.addErrorLog(ex);            
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }  
    
    public static boolean remove(Session session){
        boolean result = false;
        try {
            BasicDBObject remove = new BasicDBObject();
            remove.append(UserdbKey.USER_SESSION.TOKEN, session.token);
            coll.remove(remove);
            result = true;
        }catch( Exception ex ) {
            Util.addErrorLog(ex);            
        }
        return result;
    }    
    
    public static boolean removeByTokens(List<String> tokens){
        boolean result = false;
        try {
            DBObject query = QueryBuilder.start(UserdbKey.USER_SESSION.TOKEN).in(tokens).get();
            coll.remove(query);
            result = true;
        }catch( Exception ex ) {
            Util.addErrorLog(ex);            
        }
        return result;
    }        
    
    public static boolean remove(List<Session> list)throws EazyException{
        boolean result = false;
        try {
            for(Session session : list ){
                remove(session);
            }
        }catch( Exception ex ) {
            Util.addErrorLog(ex);            
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }  
    
    public static List<Session> getAll()throws EazyException{
        List<Session> result = new ArrayList<Session>();
        try {
            DBCursor cur = coll.find();
            while(cur.hasNext()){
                BasicDBObject obj = (BasicDBObject) cur.next();
                Boolean normalUser = obj.getBoolean(UserdbKey.USER_SESSION.NORMARL_USER_FLAG);
                String token = obj.getString(UserdbKey.USER_SESSION.TOKEN);
                String userId = obj.getString(UserdbKey.USER_SESSION.USER_ID);
                String applicationVersion = obj.getString(UserdbKey.USER_SESSION.APPLICATION_VERSION);
                Integer applicationType = (Integer) obj.get(UserdbKey.USER_SESSION.APPLICATION_TYPE);
                result.add(new Session(token, userId, normalUser, applicationVersion, applicationType));
            }
        }catch( Exception ex ) {
            Util.addErrorLog(ex);            
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }        
    
    public static String getAppVersionFromToken(String token) throws EazyException {
        Util.addDebugLog("GET APP VER FROM TOKEN "+ token);
        String  result = "'";
        try {            
            BasicDBObject obj = new BasicDBObject(UserdbKey.USER_SESSION.TOKEN, token);
            //search command
            DBObject dbObject = coll.findOne(obj);
            if(dbObject != null){                                
                String av = (String) dbObject.get(UserdbKey.USER_SESSION.APPLICATION_VERSION);   
                Util.addDebugLog("GET APP VER FROM TOKEN USER APP VERSION"+ av);
                result = av;               
            }
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        Util.addDebugLog("GET APP VER FROM TOKEN RESULT"+ result);
        return result;
    }
    
    public static String getUserIdFromToken(String token) throws EazyException {
        Util.addDebugLog("GET APP VER FROM TOKEN "+ token);
        String  result = "'";
        try {            
            BasicDBObject obj = new BasicDBObject(UserdbKey.USER_SESSION.TOKEN, token);
            //search command
            DBObject dbObject = coll.findOne(obj);
            if(dbObject != null){                                
                String av = (String) dbObject.get(UserdbKey.USER_SESSION.USER_ID);   
                Util.addDebugLog("GET APP VER FROM TOKEN USER APP VERSION"+ av);
                result = av;               
            }
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        Util.addDebugLog("GET APP VER FROM TOKEN RESULT"+ result);
        return result;
    }
      
}
