/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.chatserver.socketserver.websocket;

import com.vn.ntsc.chatserver.messageio.MessageIO;
import javax.websocket.Session;

/**
 *
 * @author Administrator
 */
public class UnAuthenticatedSession {
    public Session session;
    public static final long ReadSocTimeOut = MessageIO.ReadSocLatency;
    
    public UnAuthenticatedSession(Session session){
        this.session = session;
    }
}
