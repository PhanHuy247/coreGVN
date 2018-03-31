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
import com.vn.ntsc.presentationserver.meetpeople.dao.IDAO;
import com.vn.ntsc.presentationserver.meetpeople.pojos.entity.QuerySetting;

/**
 *
 * @author RuAc0n
 */
public class UserSettingDAO implements IDAO{
    public static DB db;
    public static DBCollection coll;
    static{
        db = CommonDAO.mongo.getDB(UserdbKey.DB_NAME);
        coll = db.getCollection(UserdbKey.SETTING_COLLECTION);
    }
    @Override
    public QuerySetting getMeetPeopleSetting(String email) {
        DBObject dbO = coll.findOne(new BasicDBObject(UserdbKey.SETTING.USER_ID, email));
        if(dbO == null){
            return null;
        }
        else{
            ArrayList<Long> showme = (ArrayList<Long>)dbO.get(UserdbKey.SETTING.SHOW_ME);
//            Integer interest = (Integer)dbO.get(Constant.INTERESTED_IN);
            int lower_age = (Integer)dbO.get(UserdbKey.SETTING.LOWER_AGE);
            int upper_age = (Integer)dbO.get(UserdbKey.SETTING.UPPER_AGE);
            ArrayList<Long> ethnics = (ArrayList<Long>)dbO.get(UserdbKey.SETTING.ETHNICITY);
            int distance = (Integer)dbO.get(UserdbKey.SETTING.DISTANCE);
            ArrayList<Long> location = (ArrayList<Long>)dbO.get(UserdbKey.SETTING.LOCATION);            
            return new QuerySetting(showme, null, lower_age, upper_age, ethnics, distance, location);
        }       
    }

    @Override
    public void updateMeetPeopleSetting(String email, QuerySetting query) {
        DBObject dbO = coll.findOne(new BasicDBObject(UserdbKey.SETTING.USER_ID, email));
        DBObject res = new BasicDBObject();
        res.put(UserdbKey.SETTING.USER_ID, email);
        res.put(UserdbKey.SETTING.SHOW_ME, query.showme);
//        res.put(Constant.SETTING.INTERESTED_IN, query.interest);
        res.put(UserdbKey.SETTING.LOWER_AGE, query.lower_age);
        res.put(UserdbKey.SETTING.UPPER_AGE, query.upper_age);
        res.put(UserdbKey.SETTING.ETHNICITY, query.ethnics);
        res.put(UserdbKey.SETTING.DISTANCE, query.distance);
        res.put(UserdbKey.SETTING.LOCATION, query.location);
        if(dbO != null){
            coll.remove(dbO);
        }       
        coll.insert(res);        
    }
    
}
