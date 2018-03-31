/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.com.ntsc.staticfileserver.server.session;

import java.util.UUID;

/**
 *
 * @author tuannxv00804
 */
public class Session {
    public String token;
    public String userID;

    public int timeToLive;
    
    public Session(String token, String userID ){
        this.token = token;
        this.userID = userID;    
        this.timeToLive = 0;
    }
}
