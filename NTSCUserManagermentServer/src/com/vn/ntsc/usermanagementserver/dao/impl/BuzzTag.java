/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.usermanagementserver.dao.impl;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.vn.ntsc.usermanagementserver.dao.DatabaseLoader;
import eazycommon.constant.mongokey.BuzzdbKey;
import eazycommon.util.Util;
import java.util.ArrayList;
import java.util.List;
import org.bson.types.ObjectId;

/**
 *
 * @author phanhuy
 */
public class BuzzTag {
    private static DBCollection coll;

    static {
        try {
            coll = DatabaseLoader.getBuzzDB().getCollection(BuzzdbKey.BUZZ_TAG_COLLECTION);
        } catch (Exception ex) {
            //Future logging here
            Util.addErrorLog(ex);

        }
    }
    
     public static List<String> getListUserIdTag(String buzzId){
        List<String> result = new ArrayList<>();
        try{
            ObjectId buzzIdObject = new ObjectId(buzzId);
            BasicDBObject findObj = new BasicDBObject();
            findObj.append("_id", buzzIdObject);
            DBObject cursor = coll.findOne(findObj);
            if(cursor != null){
                result = (List<String>)cursor.get("tag_lst");
            }
        }catch(Exception ex){
            Util.addErrorLog(ex);
        }
        return result;
    }
}
