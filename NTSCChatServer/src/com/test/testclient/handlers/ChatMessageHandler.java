/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.test.testclient.handlers;

import com.test.testclient.TestSendMessage;
import com.vn.ntsc.chatclient.listeners.IChatListener;
import com.vn.ntsc.chatserver.pojos.message.Message;

/**
 *
 * @author tuannxv00804
 */
public class ChatMessageHandler implements IChatListener{
    
    @Override
    public void handle( Message msg ) {
        TestSendMessage.numberSend++;
//        System.out.println("lon dong dau " + TestSendMessage.numberSend);
        StringBuilder builder = new StringBuilder();
        builder.append( "(" )
                .append( msg.originTime )
                .append( ") ")
                .append( "Message from: " )
                .append( msg.from )
                .append( " : " )
                .append( msg.value );
        System.out.println( builder.toString() );
    }
    
}
