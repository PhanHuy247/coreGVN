/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.usermanagementserver.dao.impl;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.vn.ntsc.usermanagementserver.dao.DatabaseLoader;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.mongokey.BuzzdbKey;
import eazycommon.constant.mongokey.StaticFiledbKey;
import eazycommon.exception.EazyException;
import eazycommon.util.Util;
import java.util.ArrayList;
import java.util.List;
import org.bson.types.ObjectId;

/**
 *
 * @author Phan Huy
 */
public class ThumbnailDAO {
    private static DBCollection coll;

    static {
        try {
            coll = DatabaseLoader.getStaticDB().getCollection(StaticFiledbKey.THUMBNAIL_COLLECTION);
        } catch (Exception ex) {
            //Future logging here
            Util.addErrorLog(ex);

        }
    }
    
    public static String getImageUrl(String buzzId) throws EazyException {
        String result = null;
        try {
            ObjectId id = new ObjectId(buzzId);
            BasicDBObject findObj = new BasicDBObject(StaticFiledbKey.THUMBNAIL.ID, id);
            DBObject obj = coll.findOne(findObj);
            if (obj == null) {
                return null;
            }
            String url = (String) obj.get("url");
            result = url;
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }
    
    public static void delImage (List<String> listImage){
        try{
            List<ObjectId> listImageId = new ArrayList<>();
            for(String imageId: listImage){
                ObjectId id = new ObjectId(imageId);
                listImageId.add(id);
            }
            
            BasicDBObject findObj = new BasicDBObject();
            findObj.append(StaticFiledbKey.THUMBNAIL.ID, new BasicDBObject("$in", listImageId));
            coll.findAndRemove(findObj);
        }catch(Exception ex){
            Util.addErrorLog(ex);
        }
    }
}
