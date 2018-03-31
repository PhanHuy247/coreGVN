/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.test.testclient.handlers;

import com.vn.ntsc.chatclient.listeners.IAuthenListener;
import com.vn.ntsc.chatserver.pojos.message.Message;
import com.vn.ntsc.chatserver.pojos.message.messagetype.MessageTypeValue;

/**
 *
 * @author tuannxv00804
 */
public class AuthenMessageHandler implements IAuthenListener{

    @Override
    public void handle( Message msg ) {
        msg.printStruct();
        if( msg.value.equals( MessageTypeValue.Auth_AutheSuccess ) ){
            System.out.println( msg.to + ", your authentication success!!!" );
        }else{
            System.out.println( msg.to + ", authen failure..." );
        }
        
    }
    
    
}
