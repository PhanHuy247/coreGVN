/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test.db;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.Mongo;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;
import eazycommon.dao.CommonDAO;

/**
 *
 * @author Admin
 */
public class DBTest {

    public static Mongo mongo;

    static {
        mongo = new Mongo("localhost", 27017);
    }

    public static void main(String[] args) {
        DB db = mongo.getDB("sjflkj");
        DBCollection coll = db.getCollection("abcabc");
        coll.insert(new BasicDBObject("key", 1));

        coll.remove(new BasicDBObject("key", 1));

    }
}
