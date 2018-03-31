/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.backend.dao.album;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.vn.ntsc.backend.dao.DBManager;
import eazycommon.constant.mongokey.UserdbKey;
import eazycommon.util.Util;

/**
 *
 * @author hoangnh
 */
public class AlbumImageDAO {
    private static DBCollection coll;
    private static DB db;

    static {
        coll = DBManager.getUserDB().getCollection(UserdbKey.ALBUM_IMAGE_COLLECTION);
    }
    
    public static Boolean isAlbumImage(String imgId){
        Boolean result = false;
        try{
            BasicDBObject findObj = new BasicDBObject();
            findObj.append(UserdbKey.ALBUM_IMAGE.IMAGE_ID, imgId);
            DBObject obj = coll.findOne(findObj);
            if(obj != null){
                result = true;
            }
        }catch(Exception ex){
            Util.addErrorLog(ex);
        }
        return result;
    }
    
    public static void updateFlag(String imgId, Integer flag){
        try{
            BasicDBObject findObj = new BasicDBObject();
            findObj.append(UserdbKey.ALBUM_IMAGE.IMAGE_ID, imgId);

            BasicDBObject updateObj = new BasicDBObject();
            updateObj.append(UserdbKey.ALBUM_IMAGE.FLAG, flag);

            coll.update(findObj, new BasicDBObject("$set", updateObj));
        }catch(Exception ex){
            Util.addErrorLog(ex);
        }
    }
}
