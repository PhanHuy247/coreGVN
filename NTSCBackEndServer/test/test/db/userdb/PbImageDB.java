/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package test.db.userdb;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.mongokey.UserdbKey;
import eazycommon.exception.EazyException;
import eazycommon.util.Util;
import org.bson.types.ObjectId;
import test.db.DBTest;

/**
 *
 * @author RuAc0n
 */
public class PbImageDB {

    private static DBCollection coll;
    private static DB db;

    static {
        try {
            db = DBTest.mongo.getDB(UserdbKey.DB_NAME);
            coll = db.getCollection(UserdbKey.PB_IMAGE_COLLECTION);
        } catch (Exception ex) {
            //Future logging here
            Util.addErrorLog(ex);

        }
    }

    public static boolean addPublicImage(String userId, String imageId, String buzzId, int flag) throws EazyException {
        boolean result = false;
        try {
            ObjectId id = new ObjectId(userId);
            BasicDBObject updateQuery = new BasicDBObject(UserdbKey.PB_IMAGE.ID, id);
            BasicDBObject imageElement = new BasicDBObject(UserdbKey.PB_IMAGE.IMAGE_ID, imageId);
            imageElement.append(UserdbKey.PB_IMAGE.BUZZ_ID, buzzId);
            imageElement.append(UserdbKey.PB_IMAGE.FLAG, flag);
            BasicDBObject pbImage = new BasicDBObject(UserdbKey.PB_IMAGE.PB_IMAGE_LIST, imageElement);
            BasicDBObject updateCommand = new BasicDBObject("$push", pbImage);
            coll.update(updateQuery, updateCommand, true, false);
            result = true;
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static Integer getFlag(String userId, String imageId) throws EazyException {
        Integer result = null;
        try {
            ObjectId id = new ObjectId(userId);
            BasicDBObject updateQuery = new BasicDBObject(UserdbKey.PB_IMAGE.ID, id);
            DBObject respondObj = coll.findOne(updateQuery);
            if (respondObj != null) {
                BasicDBList imageList = (BasicDBList) respondObj.get(UserdbKey.PB_IMAGE.PB_IMAGE_LIST);
                if (!imageList.isEmpty()) {
                    for (int i = 0; i < imageList.size(); i++) {
                        BasicDBObject image = (BasicDBObject) imageList.get(i);
                        String iId = image.getString(UserdbKey.PB_IMAGE.IMAGE_ID);
                        Integer buId = (Integer) image.get(UserdbKey.PB_IMAGE.FLAG);
                        if (iId.equals(imageId)) {
                            result = buId;
                        }
                    }
                }
            }
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static void removeById(String userId) {
        BasicDBObject find = new BasicDBObject("_id", new ObjectId(userId));
        coll.remove(find);
    }

}
