/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.vn.ntsc.chatserver.logging.pojos;

import com.vn.ntsc.chatserver.pojos.message.Message;

/**
 *
 * @author Rua
 */
public class LastChatPackage {
    
    public String userId;
    public String friendId;
    public Message msg;

    public LastChatPackage(String userId, String friendId, Message msg) {
        this.userId = userId;
        this.friendId = friendId;
        this.msg = msg;
    }
    
}
