/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.chatclient.listeners;

import com.vn.ntsc.chatserver.pojos.message.Message;

/**
 *
 * @author tuannxv00804
 */
public interface IGenericMessagesListener {
    
    public void handle( Message msg );
}
