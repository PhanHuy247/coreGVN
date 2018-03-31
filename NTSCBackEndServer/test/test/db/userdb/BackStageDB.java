/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test.db.userdb;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import eazycommon.constant.Constant;
import eazycommon.constant.mongokey.UserdbKey;
import eazycommon.util.Util;
import org.bson.types.ObjectId;
import test.db.DBTest;

/**
 *
 * @author duyetpt
 */
public class BackStageDB {

    private static DBCollection coll;
    private static DB db;

    static {
        try {
            db = DBTest.mongo.getDB(UserdbKey.DB_NAME);
            coll = db.getCollection(UserdbKey.BACKSTAGE_COLLECTION);
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
    }

    public static void addBackState(String userId, String imageId) {
        BasicDBObject find = new BasicDBObject("_id", new ObjectId(userId));
        DBObject obj = coll.findOne(find);
        BasicDBObject bs = new BasicDBObject(UserdbKey.BACKSTAGE.IMAGE_ID, imageId)
                .append(UserdbKey.BACKSTAGE.FLAG, Constant.FLAG.ON);
        if (obj != null) {
            BasicDBObject update = new BasicDBObject(UserdbKey.BACKSTAGE.BACKSTAGE_LIST, bs);
            BasicDBObject push = new BasicDBObject("$push", update);
            coll.update(find, push);
        } else {
            BasicDBList list = new BasicDBList();
            list.add(bs);
            find.append(UserdbKey.BACKSTAGE.BACKSTAGE_LIST, list);
            coll.insert(find);
        }
    }

    public static Integer getFlag(String userId, String imageId) {
        BasicDBObject find = new BasicDBObject("_id", new ObjectId(userId));
        DBObject obj = coll.findOne(find);
        BasicDBList list = (BasicDBList) obj.get(UserdbKey.BACKSTAGE.BACKSTAGE_LIST);
        for (Object o : list) {
            DBObject dbO = (DBObject) o;
            String imgId = (String) dbO.get(UserdbKey.BACKSTAGE.IMAGE_ID);
            if (imgId.equals(imageId)) {
                Integer f = (Integer) dbO.get(UserdbKey.BACKSTAGE.FLAG);
                return f;
            }
        }
        return null;
    }

    public static void removeBackStage(String userId) {
        BasicDBObject find = new BasicDBObject("_id", new ObjectId(userId));
        coll.remove(find);
    }
}
