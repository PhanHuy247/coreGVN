/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.chatserver.authentication;

/**
 *
 * @author tuannxv00804
 */
public interface IAuthThread extends Runnable{
    
    public boolean authen( String username, String passwor );
}
