/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.vn.ntsc.chatserver.logging.pojos;

import java.util.HashMap;

/**
 *
 * @author Rua
 */
public class UpdateUnreadPackage {
    
    public String userId;
    public HashMap<String, Integer> unreadMessageMap;

    public UpdateUnreadPackage(String userId, HashMap<String, Integer> unreadMessageMap) {
        this.userId = userId;
        this.unreadMessageMap = unreadMessageMap;
    }
    
    
}
