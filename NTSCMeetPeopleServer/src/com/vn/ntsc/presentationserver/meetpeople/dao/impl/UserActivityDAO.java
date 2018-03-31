/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.vn.ntsc.presentationserver.meetpeople.dao.impl;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import java.util.ArrayList;
import eazycommon.constant.mongokey.UserdbKey;
import eazycommon.dao.CommonDAO;
import org.bson.types.ObjectId;

/**
 *
 * @author RuAc0n
 */
public class UserActivityDAO{
    public static DB db;
    public static DBCollection coll;
    static{
        db = CommonDAO.mongo.getDB(UserdbKey.DB_NAME);
        coll = db.getCollection(UserdbKey.USER_ACTIVITY_COLLECTION);
    }
    
    public static ArrayList<Double> getLocation(String userId){
        ArrayList<Double> location = new ArrayList<>();
        DBObject oDb_loc = coll.findOne(new BasicDBObject(UserdbKey.USER_ACTIVITY.ID, new ObjectId(userId)));
        if (oDb_loc != null) {
            ArrayList<Double> locs = (ArrayList<Double>) (oDb_loc.get(UserdbKey.USER_ACTIVITY.LOCATION));
            if (locs != null) {
                location.add(locs.get(0));
                location.add(locs.get(1));
            }
        }
        return location;
    }
}
