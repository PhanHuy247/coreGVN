/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.test.testclient.handlers;

import com.vn.ntsc.chatclient.listeners.IGenericMessagesListener;
import com.vn.ntsc.chatserver.pojos.message.Message;
import com.vn.ntsc.chatserver.pojos.message.messagetype.MessageType;

/**
 *
 * @author tuannxv00804
 */
public class GenericMessageHandler implements IGenericMessagesListener{

    @Override
    public void handle( Message msg ) {
        if( msg.msgType == MessageType.WINK ){
            System.out.println( msg.from + " has winked you!!!" );
        }else{
            System.out.println( "GenericMessageHandler: " + msg );
            
        }
    }
    
}
