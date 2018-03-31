/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.chatserver.pojos.user;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import javax.websocket.Session;
import com.vn.ntsc.chatserver.messageio.MessageIO;
import eazycommon.util.Util;
import com.vn.ntsc.Config;
import com.vn.ntsc.Core;
import com.vn.ntsc.chatserver.logging.impl.mongo.UnreadMessageUpdater;

/**
 *
 * @author tuannxv00804
 */
public class UserConnection {
    
    private static final long SOCKET_TIME_OUT_MILLISECONDS = (long)Config.SOCKET_TIMEOUT * 60 * 1000;
    
    public Socket soc;
    public Session webSocKet;
    
    public OutputStreamWriter writer;
    public InputStreamReader reader;
    
    public static final int ReadSocTimeOut = MessageIO.ReadSocLatency;
    public static final String CharSet = "UTF-8";

    public User user;
    public StringBuilder inboxBuffer;
    
    public String status;
    
    public boolean isSendReadMessage = false;
    
    public long expiredTime;
    
    public UserConnection() {
    }
    
    public UserConnection( Socket soc, User user ){
        this.soc = soc;
        this.user = user;
        this.inboxBuffer = new StringBuilder();
        try{
            if(soc != null){
                this.soc.setSoTimeout(ReadSocTimeOut);
                writer = new OutputStreamWriter( soc.getOutputStream(), CharSet );
                reader = new InputStreamReader( soc.getInputStream(), CharSet );
            }
        } catch( IOException ex ) {
            Util.addErrorLog(ex);            
        }
        this.expiredTime = Util.currentTime() + SOCKET_TIME_OUT_MILLISECONDS;
    }
    
    public UserConnection( Session soc, User user ){
        this.webSocKet = soc;
        this.user = user;
        this.expiredTime = Util.currentTime() + SOCKET_TIME_OUT_MILLISECONDS;
    }

    public UserConnection(String username){
        this.user = new User(username);
        this.soc = null;
        this.expiredTime = 0;
    }    
    
    public void resetExpiredDate(){
        this.expiredTime =  Util.currentTime() + SOCKET_TIME_OUT_MILLISECONDS;
    }
    
    public boolean isAlived(){
        return this.expiredTime >=  Util.currentTime();
    }    
    
    @Override
    public String toString() {
        return "UserConnection{" + "websocket=" + webSocKet + ", user=" + user + ", buffer=" + inboxBuffer + '}';
    }
    
    @Override
    public boolean equals( Object o ){
        if( o != null ){
            if( !(o instanceof UserConnection) ){
                return false;
            }
            if(this.soc != null)
                return this.soc.getPort() == ((UserConnection)o).soc.getPort();
        }
        return false;
    }

    public void destroy() {
        try{
            UserConnection uc = Core.getStoreEngine().get(this.user.username);
            User u = this.user;
            if(uc != null && uc.user != null)
                u = uc.user;
            String userId = u.username;
            
            UnreadMessageUpdater.add(userId);
        }catch(Exception ex){
            Util.addErrorLog(ex);
        }
        
    }
}
