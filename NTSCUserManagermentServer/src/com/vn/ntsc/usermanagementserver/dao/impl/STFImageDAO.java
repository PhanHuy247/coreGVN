/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.usermanagementserver.dao.impl;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.vn.ntsc.usermanagementserver.dao.DatabaseLoader;
import eazycommon.constant.mongokey.StaticFiledbKey;
import eazycommon.util.Util;
import java.util.ArrayList;
import java.util.List;
import org.bson.types.ObjectId;

/**
 *
 * @author hoangnh
 */
public class STFImageDAO {
    private static DBCollection coll;

    static {
        try {
            coll = DatabaseLoader.getStaticDB().getCollection(StaticFiledbKey.IMAGE_COLLECTION);
        } catch (Exception ex) {
            //Future logging here
            Util.addErrorLog(ex);

        }
    }
    
    public static void delImage (List<String> listImage){
        try{
            List<ObjectId> listImageId = new ArrayList<>();
            for(String imageId: listImage){
                ObjectId id = new ObjectId(imageId);
                listImageId.add(id);
            }
            
            BasicDBObject findObj = new BasicDBObject();
            findObj.append(StaticFiledbKey.IMAGE.ID, new BasicDBObject("$in", listImageId));
            coll.findAndRemove(findObj);
        }catch(Exception ex){
            Util.addErrorLog(ex);
        }
    }
}
