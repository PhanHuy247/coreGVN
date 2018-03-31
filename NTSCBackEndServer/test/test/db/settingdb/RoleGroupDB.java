/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test.db.settingdb;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import java.util.ArrayList;
import java.util.List;
import eazycommon.constant.mongokey.SettingdbKey;
import eazycommon.util.Util;
import test.db.DBTest;

/**
 *
 * @author duyetpt
 */
public class RoleGroupDB {

    private static DB db;
    private static DBCollection coll;

    static {
        try {
            db = DBTest.mongo.getDB(SettingdbKey.DB_NAME);
            coll = db.getCollection(SettingdbKey.ROLE_GROUP_COLLECTION);
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
    }

    public static List<String> getListGroupByRole(String role) {
        ArrayList<String> list = new ArrayList<String>();
        BasicDBObject find = new BasicDBObject(SettingdbKey.ROLE_GROUP.ROLE, role);
        DBCursor cursor = coll.find(find);
        while (cursor.hasNext()) {
            DBObject obj = cursor.next();
            String group = (String) obj.get(SettingdbKey.ROLE_GROUP.GROUP);
            list.add(group);
        }
        return list;
    }
}
