/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.usermanagementserver.dao.impl.log;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.vn.ntsc.usermanagementserver.dao.DatabaseLoader;

/**
 *
 * @author Administrator
 */
public class ChatLogDAO {
    private static DBCollection coll;
    
    public static boolean isContacted(String userId, String friendId){
        boolean result = false;
        coll = DatabaseLoader.getChatLogExtensionDB().getCollection(userId);
        BasicDBObject query = new BasicDBObject("to", friendId);
        DBObject obj = coll.findOne(query);
        if (obj != null)
            result = true;
        return result;
    }
    
}
