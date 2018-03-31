/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test.db.userdb;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import java.util.logging.Level;
import java.util.logging.Logger;
import eazycommon.constant.Constant;
import eazycommon.constant.mongokey.UserdbKey;
import eazycommon.exception.EazyException;
import eazycommon.util.Util;
import test.db.DBTest;
import com.vn.ntsc.backend.entity.impl.image.Image;

/**
 *
 * @author duyetpt
 */
public class ImageDB {

    private static DBCollection coll;
    private static DB db;

    static {
        try {
            db = DBTest.mongo.getDB(UserdbKey.DB_NAME);
            coll = db.getCollection(UserdbKey.IMAGE_COLLECTION);
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
    }

    public static String insertImage(Image image) {
        String result = null;
        BasicDBObject obj = new BasicDBObject();
        obj.put(UserdbKey.IMAGE.IMAGE_ID, image.imageId);
        obj.put(UserdbKey.IMAGE.APPEAR_FLAG, image.appearFlag);
        obj.put(UserdbKey.IMAGE.APPROVED_FLAG, image.appFlag);
        obj.put(UserdbKey.IMAGE.AVATAR_FLAG, image.avatarFlag);
        obj.put(UserdbKey.IMAGE.FLAG, image.flag);
        obj.put(UserdbKey.IMAGE.IMAGE_TYPE, image.imageType);
        obj.put(UserdbKey.IMAGE.REPORT_FLAG, image.reportFlag);
        obj.put(UserdbKey.IMAGE.STATUS, image.imageStatus);
        obj.put(UserdbKey.IMAGE.USER_ID, image.userId);
        obj.put(UserdbKey.IMAGE.UPLOAD_TIME, image.uploadTime);
        obj.put(UserdbKey.IMAGE.REPORT_TIME, image.reportTime);

        if (image.imageType == Constant.IMAGE_TYPE_VALUE.IMAGE_BACKSTAGE) {
            BackStageDB.addBackState(image.userId, image.imageId);
        }
        if (image.imageType == Constant.IMAGE_TYPE_VALUE.IMAGE_PUBLIC) {
            try {
                String buzzid = BuzzDetailDB.addBuzz(image.userId, image.imageId, Constant.BUZZ_TYPE_VALUE.IMAGE_BUZZ, System.currentTimeMillis(), 1, "127.0.0.1");
                UserBuzzDB.addUserBuzz(image.userId, buzzid, 0);
                PbImageDB.addPublicImage(image.userId, image.imageId, buzzid, 1);
                result = buzzid;
            } catch (EazyException ex) {
                Logger.getLogger(ImageDB.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        coll.insert(obj);

//        BasicDBObject find = new BasicDBObject(UserdbKey.IMAGE.IMAGE_ID, image.imageId);
//        DBObject found = coll.findOne(find);
//        if (found != null) {
//            System.out.println("found this :" + image.imageId);
//        }else{
//            System.out.println("not found this :" + image.imageId);
//        }

        return result;
    }

    public static Object getObject(String imageId, String key) {
        BasicDBObject find = new BasicDBObject(UserdbKey.IMAGE.IMAGE_ID, imageId);
        DBObject found = coll.findOne(find);
        return found.get(key);
    }

    public static void removeImage(String img_id) {
        BasicDBObject find = new BasicDBObject(UserdbKey.IMAGE.IMAGE_ID, img_id);
        System.out.println("remove image_id :" + img_id);
        coll.remove(find);
    }
}
