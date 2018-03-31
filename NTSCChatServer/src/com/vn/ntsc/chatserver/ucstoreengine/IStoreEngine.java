/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.chatserver.ucstoreengine;

import java.util.List;
import com.vn.ntsc.chatserver.pojos.user.UserConnection;
import javax.websocket.Session;

/**
 *
 * @author tuannxv00804
 */
public interface IStoreEngine {
    
    public void add( UserConnection uc );
    
    public void put( UserConnection uc );
    
    /**
     * Get UserConnection continuously, cycly. Notice: peek() has to thread-safe.
     * @return 
     */
    public UserConnection poll();
    
    public UserConnection get( String username );
    
    public List<UserConnection> gets( String username );
    
    public boolean remove( UserConnection uc );
 
    public String size();
    
    public void add(String buzzId,String userId);
    public void addUserNotiWebsocket(String buzzId,String userId);
    public List<String> getListUserTo(String buzzId);
    public boolean remove(String buzzId,String userId);
    public void websocketClose(Session session);
    public String websocketSate();
    public void expireToken();
    public void removeSessionSameToken(UserConnection uc );
    public boolean removeSessionTokenFromClient(String userId, String token);
}
