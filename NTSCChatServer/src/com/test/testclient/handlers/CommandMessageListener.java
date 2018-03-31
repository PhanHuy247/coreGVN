/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.test.testclient.handlers;

import com.vn.ntsc.chatclient.listeners.ICommandMessageListener;
import com.vn.ntsc.chatserver.pojos.message.Message;
import com.vn.ntsc.chatserver.pojos.message.messagetype.MessageTypeValue;

/**
 *
 * @author tuannxv00804
 */
public class CommandMessageListener implements ICommandMessageListener{

    @Override
    public void handle( Message msg ) {
        String value = msg.value;
        String cmdName = MessageTypeValue.CMD_getCommandName( value );
        if( cmdName.equals( MessageTypeValue.CMD_name_PPFileTransfer ) ){
            String signal = MessageTypeValue.CMD_PPFilesTransfer_getSignal( value );
            
            if( signal.equals( MessageTypeValue.CMD_PPFileStransfer_signal_invite ) ){
               
                
            //}else if( signal.equals( MessageTypeValue.CMD_PPFileStransfer_signal_accept ) ){
                
                
            //}else if( signal.equals( MessageTypeValue.CMD_PPFileStransfer_signal_decline ) ){
                //do nothing :)
            }else if( signal.equals( MessageTypeValue.CMD_PPFileStransfer_signal_servercreated ) ){
                //
            }
        }
    }
    
}
