/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.com.ntsc.staticfileserver.server.session;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author tuannxv00804
 */
public class SessionManager {
    //public static Map<String, Session> SS = new HashMap<String, Session>( Config.MaxConcurrent );
    public static Map<String, Session> SS = new ConcurrentHashMap<String, Session>();
    
    public static boolean isTokenExist( String token ){
        return SS.containsKey( token );
    }
    
    public static Session getSession( String token ){
        return SS.get( token );
    }
    
    public static LinkedList<Session> getSessionsOfUser( String token ){
        LinkedList<Session> ll = new LinkedList<Session>();
        Session session = SS.get( token );
        if( session == null ){
            return ll;
        }
        return ll;
    }
    
    public static void removeSessionsOfUser( String token ){
        Session session = SS.get( token );
        if( session == null ){
            return;
        }
    }
    
    public static void putSession( Session session ){
        SS.put( session.token, session);
    }
    
    public static void removeSession( String token ){
        SS.remove( token );
    }
    
    public static Collection<Session> getAllSession(){
        return SS.values();
    }
    
}
