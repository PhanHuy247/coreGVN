/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.chatserver.socketserver.websocket;

import com.vn.ntsc.Config;
import com.vn.ntsc.chatserver.socketserver.IServer;
import eazycommon.util.Util;
import org.glassfish.tyrus.server.Server;
/**
 *
 * @author Administrator
 */
public class WebSocketServer implements IServer{
    
    @Override
    public void startListen() {
        Thread t = new Thread( this );
        t.start();
    }

    @Override
    public void run() {
        try {
            Server webSocketServer = new Server(Config.ChatServerIP, Config.WebSocketPort, "/ws", ServerEndPoint.class);
            webSocketServer.start();
        } catch (Exception e) {
            Util.addErrorLog(e);
        }
    }
    
    
}
