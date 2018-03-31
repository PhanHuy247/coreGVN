/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc;

import com.vn.ntsc.blacklist.BlockUserManager;
import com.vn.ntsc.blacklist.DeactivateUserManager;
import eazycommon.CommonCore;
import eazycommon.util.Util;
import com.vn.ntsc.chatserver.authentication.IAuthPool;
import com.vn.ntsc.chatserver.authentication.impl.AuthPool;
import com.vn.ntsc.chatserver.logging.ILogger;
import com.vn.ntsc.chatserver.logging.impl.mongo.LastChatUpdater;
import com.vn.ntsc.chatserver.logging.impl.mongo.MongoLogger;
import com.vn.ntsc.chatserver.logging.impl.mongo.UnreadMessageUpdater;
import com.vn.ntsc.chatserver.pojos.user.LastMessageManager;
import com.vn.ntsc.chatserver.pojos.user.UnreadMessageManager;
import com.vn.ntsc.chatserver.socketserver.IServer;
import com.vn.ntsc.chatserver.socketserver.impl.Server;
import com.vn.ntsc.chatserver.socketserver.websocket.WebSocketServer;
import com.vn.ntsc.chatserver.ucstoreengine.IStoreEngine;
import com.vn.ntsc.chatserver.ucstoreengine.impl.XStoreEngine;
import com.vn.ntsc.chatserver.workerfactory.IFactory;
import com.vn.ntsc.chatserver.workerfactory.impl.WorkerExprieToken;
import com.vn.ntsc.chatserver.workerfactory.impl.WorkerFactory;
import com.vn.ntsc.dao.IDAO;
import com.vn.ntsc.dao.impl.MongoDAO;

/**
 *
 * @author duongltd
 */
public class Core {
    
    private static final IAuthPool authpoll;
    private static final IStoreEngine storeEngine;
//    private static final IServer socketServer;
    private static final IServer webServer;
    private static final IFactory factory;
    private static final ILogger logger;
    private static final IDAO dao;
    private static final UnreadMessageUpdater unreadMessageUpdater;
    private static final LastChatUpdater lastChatUpdater;
    private static final WorkerExprieToken exprireToken;
    
    static{
        Config.initConfig();
        CommonCore.init();
        authpoll = new AuthPool();
//        storeEngine = new SimpleStoreEngine();
        storeEngine = new XStoreEngine();
//        socketServer = new Server();
        
        webServer = new WebSocketServer();
        factory = new WorkerFactory();
        logger = new MongoLogger();
        dao = new MongoDAO();
        unreadMessageUpdater = new UnreadMessageUpdater();
        lastChatUpdater = new LastChatUpdater();
        exprireToken = new WorkerExprieToken();
    }
    
    public static IAuthPool getAuthPool(){
        return authpoll;
    }
    
    public static int getAuthPoolNumber(){
        return authpoll.getAuthenNumber();
    }
    public static IStoreEngine getStoreEngine(){
        return storeEngine;
    }
    
    public static IFactory getWorkerFactory(){
        return factory;
    }

    public static ILogger getLogger(){
        return logger;
    }
    
    public static IDAO getDAO(){
        return dao;
    }
    
    public static UnreadMessageUpdater getUnreadMessageUpdater(){
        return unreadMessageUpdater;
    }
    
    public static void main( String[] args ) {
//        Tool.reverstHistory("54c87ffbe4b04c9cc83f3f3e");
        UnreadMessageManager.init();
        LastMessageManager.init();
        BlockUserManager.init();
        DeactivateUserManager.init();
        initServer();
        Util.addInfoLog("Starting ChatLogService...");
        com.vn.ntsc.chatlogserver.Server.run();
        Util.addInfoLog("ChatLogServer out side confirm");        
    }
    
    private static void initServer() {
        
//        socketServer.startListen();
//        Util.addInfoLog("SocketServer started.");
        
        webServer.startListen();
        Util.addInfoLog("WebSocketServer started.");
        
//        authpoll.startAuthService();
//        Util.addInfoLog("Authen service started.");
        
        factory.startFactory();
        Util.addInfoLog("WorkerFactory started to work.");        
        
        logger.startService();
        Util.addInfoLog("Logger service is started.");  
        
        unreadMessageUpdater.startService();
        Util.addInfoLog("Update unread message service is started.");   
        
        lastChatUpdater.startService();
        Util.addInfoLog("Update last chat service is started.");   
        
        exprireToken.start();
        Util.addInfoLog("Expire Token service is started.");   
    }
    
}
