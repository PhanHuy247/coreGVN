/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.com.ntsc.staticfileserver.dao.impl;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import eazycommon.constant.mongokey.BuzzdbKey;
import eazycommon.constant.mongokey.UserdbKey;
import eazycommon.util.Util;
import org.bson.types.ObjectId;
import vn.com.ntsc.staticfileserver.dao.DAO;

/**
 *
 * @author Phan Huy
 */
public class AlbumDAO {
    private static DBCollection coll;

    static {
        try {
            coll = DAO.getUserDB().getCollection(UserdbKey.ALBUM_COLLECTION);
        } catch (Exception ex) {
            //Future logging here
            Util.addErrorLog(ex);

        }
    }
    
     public static Integer getNumberImage(String albumId) {
        int count = 0;
        try {
            Boolean isValid = ObjectId.isValid(albumId);
            if (isValid) {
                ObjectId id = new ObjectId(albumId);
                BasicDBObject findObj = new BasicDBObject();
                findObj.append(UserdbKey.ALBUM.ID, id);

                DBObject obj = coll.findOne(findObj);
                if (obj != null) {
                    count = (Integer) obj.get("number_image");
                }
            }
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
        return count;
    }
    
     public static void updateNumberImage(String albumId,Integer numberImage) {
        try {
            Boolean isValid = ObjectId.isValid(albumId);
            if (isValid) {
                ObjectId id = new ObjectId(albumId);
                BasicDBObject findObj = new BasicDBObject();
                findObj.append(UserdbKey.ALBUM.ID, id);

//                DBObject obj = coll.findOne(findObj);
//                if (obj != null) {
                    BasicDBObject newDocument =
                        new BasicDBObject().append("$inc",
                            new BasicDBObject().append("number_image", numberImage));
                    coll.update(findObj, newDocument);
//                }
            }
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
    }
    
}
