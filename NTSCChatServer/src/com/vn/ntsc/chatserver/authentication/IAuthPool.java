/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.chatserver.authentication;

import com.vn.ntsc.chatserver.authentication.impl.UnAuthenticatedConnection;


/**
 *
 * @author tuannxv00804
 */
public interface IAuthPool {
    
    public void put( UnAuthenticatedConnection con , int flag);
    
    public UnAuthenticatedConnection poll();
    
    public void startAuthService();
    
    public int getAuthenNumber();
    
}
