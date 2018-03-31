/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.dao.impl;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.vn.ntsc.dao.DBLoader;
import eazycommon.constant.mongokey.BuzzdbKey;
import eazycommon.util.Util;
import org.bson.types.ObjectId;
import org.json.simple.JSONArray;

/**
 *
 * @author Phan Huy
 */
public class BuzzTagDAO {

    private static DBCollection coll;

    static {
        try {
            coll = DBLoader.getBuzzDB().getCollection(BuzzdbKey.BUZZ_TAG_COLLECTION);
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
    }
    
    public static JSONArray getListTagUser(String buzzId){
        JSONArray arrayListTag = new JSONArray();
        ObjectId id = new ObjectId(buzzId);
        BasicDBObject find = new BasicDBObject("_id", id);
        DBObject object = coll.findOne(find);
        if(object != null){
            BasicDBList listUserTag = (BasicDBList)object.get("tag_lst");
            for(Object listObject : listUserTag){
                DBObject userTag = (DBObject)listObject;
                String userId = (String)userTag.get("user_id");
                arrayListTag.add(userId);
            }
        }
        return arrayListTag;
    }
}
