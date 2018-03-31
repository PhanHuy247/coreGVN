/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.chatserver.pojos.user;

import java.util.HashMap;

/**
 *
 * @author Rua
 */
public class UnreadMessage {

    public String userId;
    public HashMap<String, Integer> unreadMessageMap;

    public UnreadMessage(String userId, HashMap<String, Integer> unreadMessageMap) {
        this.userId = userId;
        this.unreadMessageMap = unreadMessageMap;
    }
    
}
