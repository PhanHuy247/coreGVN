/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.chatclient.listeners.impl;

import com.vn.ntsc.chatclient.Client;
import com.vn.ntsc.chatclient.listeners.IGenericMessagesListener;
import com.vn.ntsc.chatserver.messageio.MessageIO;
import com.vn.ntsc.chatserver.pojos.message.Message;
import com.vn.ntsc.chatserver.pojos.message.messagetype.MessageType;
import com.vn.ntsc.chatserver.pojos.message.messagetype.MessageTypeValue;

/**
 *
 * @author tuannxv00804
 */
public class MessageConfirmer implements IGenericMessagesListener{

    Client client;
    public MessageConfirmer( Client client ){
        this.client = client;
    }
    
    @Override
    public void handle( Message msg ) {
        if( msg.msgType == MessageType.PP ){
            //String value = msg.id + "&" + MessageTypeValue.MsgStatus_Delivered;
            String value = MessageTypeValue.MsgStatus_getValue( msg, MessageTypeValue.MsgStatus_Delivered );
            Message confirmMsg = new Message( msg.to, msg.from, MessageType.MDS, value );
            
//            System.out.println( "MessageConfirmer.handle() -> printStruct: " );
            confirmMsg.printStruct();
            MessageIO.sendMessage( client, confirmMsg );
        }
    }
    
}
