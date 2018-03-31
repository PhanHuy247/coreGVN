/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.test.testclient.handlers;

import com.vn.ntsc.chatclient.listeners.IPresenceListener;
import com.vn.ntsc.chatserver.pojos.message.Message;
import com.vn.ntsc.chatserver.pojos.message.messagetype.MessageTypeValue;

/**
 *
 * @author tuannxv00804
 */
public class PresenceMessageHandler implements IPresenceListener {

    @Override
    public void handle( Message msg ) {
        
//        System.out.println( "PresenceHandler.handle() -> MessageStructure: "  );
//        msg.printStruct();
        
        StringBuilder builder = new StringBuilder();
        
        String olStatus;
        if( msg.value.equals( MessageTypeValue.Presence_Online ) ){
            olStatus = "Online";
        }else if( msg.value.equals( MessageTypeValue.Presence_Offline ) ){
            olStatus = "Offline";
        }else if( msg.value.equals( MessageTypeValue.Presence_Writing ) ){
            olStatus = "Writing";
            
        }else{
            olStatus = msg.value;
        }
        
        builder.append( msg.from )
                .append( " is " )
                .append( olStatus );
        System.out.println( builder.toString() );
    }
    
}
