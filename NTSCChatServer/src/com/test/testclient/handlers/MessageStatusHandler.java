/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.test.testclient.handlers;

import com.vn.ntsc.chatclient.listeners.IMessageStatusListener;
import com.vn.ntsc.chatserver.pojos.message.Message;
import com.vn.ntsc.chatserver.pojos.message.messagetype.MessageTypeValue;

/**
 *
 * @author tuannxv00804
 */
public class MessageStatusHandler implements IMessageStatusListener{

    @Override
    public void handle( Message msg ) {
//        System.out.println( "MessageStatusHandler.hand() -> " ); 
//        msg.printStruct();
        
        String confirmValue = readConfirmString( msg );
        String originID = readID( msg );
        if( confirmValue.equals( MessageTypeValue.MsgStatus_Sent ) ){
            System.out.println( "Message: " + originID + " is sent to server." );
        }else if ( confirmValue.equals( MessageTypeValue.MsgStatus_Delivered ) ){
            System.out.println( "Message: " + originID + " is delivered." );
        }else{
            System.out.println( "MessageStatusHandler: " + msg );
        }
    }

    private String readConfirmString( Message msg ) {
        String str = msg.value;
        String[] eles = str.split( "&" );
        try{
            return eles[3];
        } catch( Exception ex ) {
            return null;
        }
    }

    private String readID( Message msg ) {
        String str = msg.value;
        String[] eles = str.split( "&" );
        try{
            String result = eles[0]
                    + "&" + eles[1]
                    + "&" + eles[2];
            return result;
        } catch( Exception ex ) {
            return null;
        }
    }
    
}
