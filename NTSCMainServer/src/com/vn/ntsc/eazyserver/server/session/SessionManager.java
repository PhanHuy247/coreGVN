/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.eazyserver.server.session;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import eazycommon.util.Util;
import com.vn.ntsc.dao.impl.UserSessionDAO;
import com.vn.ntsc.eazyserver.adapter.impl.util.InterCommunicator;

/**
 *
 * @author tuannxv00804
 */
public class SessionManager {
    //public static Map<String, Session> SS = new HashMap<String, Session>( Config.MaxConcurrent );
    public static Map<String, Session> SS = new ConcurrentHashMap<>();
    
    public static void init(){
        try{
            List<Session> listSession = UserSessionDAO.getAll();
            for(Session session : listSession){
                putSession(session);
            }
            List<String> list = new ArrayList<>();
            for(Session session : listSession){
                list.add(session.userID);
            }
            InterCommunicator.updatePresentation(list);
        }catch(Exception ex){
            Util.addErrorLog(ex);
        }
    }
    
    public static boolean isTokenExist( String token ){
        return SS.containsKey( token );
    }
    
    public static Session getSession( String token ){
        Session session =  SS.get( token );
        if(session != null){
            if(!session.inUse)
                return null;
        }
        return session;
    }    
    
//    public static LinkedList<Session> getSessionsOfUser( String token ){
//        LinkedList<Session> ll = new LinkedList<Session>();
//        Session session = SS.get( token );
//        if( session == null ){
//            return ll;
//        }
//
//        String email = session.userEmail;
//        Iterator<Session> iter = SS.values().iterator();
//        while( iter.hasNext() ){
//            Session n = iter.next();
//            if( n.userEmail.equals( email ) ){
//                ll.add( n );
//            }
//        }
//
//        return ll;
//    }
    
    public static List<Session> removeSessionsOfUserExcudeToken( String token){
        //DuongLTD
        List<Session> result = new ArrayList<>();
        try{
            Session session = SS.get( token );
            String sessionEmail = session.userID;
            Iterator<Session> iter = SS.values().iterator();
            while( iter.hasNext() ){
                Session curSession = iter.next();
                if(token != null){
                    if( curSession.userID.equals( sessionEmail ) && !curSession.token.equals(token) ){
                        result.add(curSession);
                        SS.remove( curSession.token );
                    }
                }
            }            
        }catch(Exception ex){
            Util.addErrorLog(ex);            
           
        }
        return result;
    }
    
    public static List<Session> removeSessionsOfUserExcudeTokenInBackend( String token, String userId){
        //DuongLTD
        List<Session> result = new ArrayList<>();
        try{
            Session session = SS.get( token );
            Util.addDebugLog("Test for toke "+token);
            String sessionEmail = userId;
            Iterator<Session> iter = SS.values().iterator();
            while( iter.hasNext() ){
                Session curSession = iter.next();
                if(token != null){
                    if( curSession.userID.equals( sessionEmail ) ){
                        result.add(curSession);
                        SS.remove( curSession.token );
                    }
                }
            }            
        }catch(Exception ex){
            Util.addErrorLog(ex);            
           
        }
        return result;
    }

    public static List<Session> removeSessionsByUserId( String userId){
        //DuongLTD
        List<Session> result = new ArrayList<>();
        try{
            Iterator<Session> iter = SS.values().iterator();
            while( iter.hasNext() ){
                Session curSession = iter.next();
                if( curSession.userID.equals( userId ) ){
                    result.add(curSession);
                    SS.remove( curSession.token );
                }
            }            
        }catch(Exception ex){
            Util.addErrorLog(ex);            
           
        }
        return result;
    }
    
    public static Session getSessionHasToken( String oldToken){
        //DuongLTD
        Session ss = null;
        try{
            Iterator<Session> iter = SS.values().iterator();
            while( iter.hasNext() ){
                Session curSession = iter.next();
                if( oldToken.equals( curSession.oldToken ) ){
                    ss = curSession;
                    break;
                }
            }            
        }catch(Exception ex){
            Util.addErrorLog(ex);            
           
        }
        return ss;
    }
    
    public static List<Session> getSessionsByUserId( String userId){
        //DuongLTD
        List<Session> result = new ArrayList<>();
        if(userId != null && !userId.isEmpty()){
            try{
                Iterator<Session> iter = SS.values().iterator();
                while( iter.hasNext() ){
                    Session curSession = iter.next();
                    if( curSession.userID != null && curSession.userID.equals( userId ) ){
                        result.add(curSession);
                    }
                }            
            }catch(Exception ex){
                Util.addErrorLog(ex);            

            }
        }
        return result;
    }
    
    public static List<Session> clearSessionOfUser( String token){
        //DuongLTD
        List<Session> result = new ArrayList<>();
        try{
            Session session = SS.get( token );
            String sessionEmail = session.userID;
            Iterator<Session> iter = SS.values().iterator();
            while( iter.hasNext() ){
                Session curSession = iter.next();
                if(token != null){
                    if( curSession.userID.equals( sessionEmail ) ){
                        result.add(curSession);
                        SS.remove( curSession.token );
                    }
                }
            }            
        }catch(Exception ex){
            Util.addErrorLog(ex);            
           
        }
        return result;
    }    
    
    public static List<Session> removeSessionsOfGroupUser(String type){
        //DuongLTD
        List<Session> result = new ArrayList<>();
        try{
            Iterator<Session> iter = SS.values().iterator();
            while( iter.hasNext() ){
                Session curSession = iter.next();
                if(type != null){ // remove group admin
                    if( curSession.type != null && curSession.type.equals(type) ){
                        SS.remove( curSession.token );
                    }
                }else{ // remove all user
                    if( curSession.type == null ){
                        SS.remove( curSession.token );
                        result.add(curSession);
                    }                    
                }
            }            
        }catch(Exception ex){
            Util.addErrorLog(ex);            
           
        }
        return result;
    }   
    
    public static void updateInUseAllUser(boolean inUse){
        Iterator<Session> iter = SS.values().iterator();
        while( iter.hasNext() ){
            Session session = iter.next();
            if(session != null && session.type == null)
                session.inUse = inUse;
        }
    }    
    
//    public static void changeSettingAllUser(boolean changeSettingToken){
//        Iterator<Session> iter = SS.values().iterator();
//        while( iter.hasNext() ){
//            Session session = iter.next();
//            if(session != null && session.type == null)
//                session.changeSettingToken = changeSettingToken;
//        }
//    }    
    
    public static void updateChangeBackendSettingFlag( String token, boolean changeSettingToken){
        try{
            Session session = SS.get(token);
            if(session != null){
                session.changeSettingToken = changeSettingToken;
            }
        }catch(Exception ex){
            Util.addErrorLog(ex);            
           
        }
    }    
    
    public static void updateInUseByUserId( String userId, boolean inUse){
        try{
            Iterator<Session> iter = SS.values().iterator();
            while( iter.hasNext() ){
                Session curSession = iter.next();
                if( curSession.userID.equals( userId ) ){
                    curSession.inUse = inUse;
                }
            }            
        }catch(Exception ex){
            Util.addErrorLog(ex);            
           
        }
    }      
    
    public static void putSession( Session session ){
        SS.put( session.token, session);
    }
    
    public static void removeSession( String token ){
        SS.remove( token );
    }

    public static void clear(){
        SS.clear();
    }
    
    public static Collection<Session> getAllSession(){
        return SS.values();
    }
    
}
