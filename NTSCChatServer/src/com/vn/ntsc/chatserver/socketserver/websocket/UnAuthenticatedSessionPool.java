/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.chatserver.socketserver.websocket;

import java.util.HashMap;
import javax.websocket.Session;

/**
 *
 * @author Administrator
 */
public class UnAuthenticatedSessionPool {
    
    public static HashMap<String, UnAuthenticatedSession> unauthenSessionMap = new HashMap<>();
    
    public static void put(Session session){
        session.setMaxIdleTimeout(UnAuthenticatedSession.ReadSocTimeOut);
        unauthenSessionMap.put(session.getId(), new UnAuthenticatedSession(session));
    }
    
    public static void remove(String sessionId){
        unauthenSessionMap.remove(sessionId);
    }
    
    public static Session get(String sessionId){
        UnAuthenticatedSession UAS = unauthenSessionMap.get(sessionId);
        if (UAS == null){
            return null;
        }
        else {
            return UAS.session;
        }
    }
}
