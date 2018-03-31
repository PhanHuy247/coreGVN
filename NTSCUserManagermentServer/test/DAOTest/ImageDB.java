/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAOTest;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.WriteConcern;
import eazycommon.constant.Constant;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.mongokey.UserdbKey;
import eazycommon.exception.EazyException;
import eazycommon.util.Util;
import org.bson.types.ObjectId;
import usermanagement.All;
import com.vn.ntsc.usermanagementserver.entity.impl.image.Image;

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
            //Future logging here
            Util.addErrorLog(ex);

        }
    }

    public static String insertImage(Image image) throws EazyException {
        String result = null;
        try {
            com.vn.ntsc.usermanagementserver.dbentity.ImageDB imageDB = com.vn.ntsc.usermanagementserver.dbentity.ImageDB.fromImageEntity(image);
            DBObject obj = imageDB.toDBObject();
            coll.insert(obj, new WriteConcern(true));
            result = ((ObjectId)obj.get("_id")).toString();
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static String insertImage(int type, String userId) throws EazyException {
        String userId1 = "53b52d4fc4e44a16508f3a26"; // 53b52d66c4e44a16508f3a2c
        String userId2 = "53b52d66c4e44a16508f3a2c";
        if (userId == null) {
            if (System.currentTimeMillis() % 2 == 0) {
                userId = userId1;
            } else {
                userId = userId2;
            }
        }
        String imageId = All.createId();
        Integer imageType = type;
        Integer imageStatus = Constant.REVIEW_STATUS_FLAG.APPROVED;
        Integer avaterFlag = Constant.FLAG.OFF;
        Integer flag = Constant.FLAG.ON;
        long timeUpload = System.currentTimeMillis();
        Image image = new Image(userId, imageId, imageType, imageStatus, avaterFlag, flag, flag, timeUpload, flag);
        insertImage(image);
        return imageId;
    }

    public static void remove(String imageId, String userId) {
        coll.remove(new BasicDBObject("img_id", imageId).append("user_id", userId));
    }

}
