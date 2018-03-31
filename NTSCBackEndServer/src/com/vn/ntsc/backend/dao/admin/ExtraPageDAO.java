/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.vn.ntsc.backend.dao.admin;


import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import java.util.ArrayList;
import java.util.List;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.mongokey.SettingdbKey;
import eazycommon.exception.EazyException;
import eazycommon.util.Util;
import org.bson.types.ObjectId;
import com.vn.ntsc.backend.dao.DBManager;
import com.vn.ntsc.backend.entity.IEntity;
import com.vn.ntsc.backend.entity.impl.datarespond.SizedListData;
import com.vn.ntsc.backend.entity.impl.extrapage.ExtraPage;

/**
 *
 * @author RuAc0n
 */
public class ExtraPageDAO {
    private static DBCollection coll;

    static{
         try{
             coll = DBManager.getSettingDB().getCollection( SettingdbKey.EXTRA_PAGE_COLLECTION );
        } catch( Exception ex ) {
            Util.addErrorLog(ex);            
        }
    }
    
    public static String insert(String title, String url) throws EazyException {
        String result = null;
        try {
            DBObject insertObj = new BasicDBObject(SettingdbKey.EXTRA_PAGE.PAGE_TITLE, title);
            insertObj.put(SettingdbKey.EXTRA_PAGE.URL, url);
            insertObj.put(SettingdbKey.EXTRA_PAGE.ORDER, System.currentTimeMillis());
            coll.insert(insertObj);
            result = insertObj.get(SettingdbKey.EXTRA_PAGE.ID).toString();
        }catch (Exception ex) {
            Util.addErrorLog(ex);            
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static boolean update(String id , String title, String url) throws EazyException {
        boolean  result = false;
        try {
            ObjectId oId = new ObjectId(id);
            DBObject findObj = new BasicDBObject(SettingdbKey.EXTRA_PAGE.ID, oId);
            DBObject updateObj = new BasicDBObject(SettingdbKey.EXTRA_PAGE.PAGE_TITLE, title);
            updateObj.put(SettingdbKey.EXTRA_PAGE.URL, url);
            DBObject setObj = new BasicDBObject("$set", updateObj);
            coll.update(findObj, setObj);
            result = true;
        }catch (Exception ex) {
            Util.addErrorLog(ex);            
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }    
    
    public static boolean delete(String id) throws EazyException {
        boolean  result = false;
        try {
            ObjectId oId = new ObjectId(id);
            DBObject findObj = new BasicDBObject(SettingdbKey.EXTRA_PAGE.ID, oId);
            coll.remove(findObj);
            result = true;
        }catch (Exception ex) {
            Util.addErrorLog(ex);            
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    } 
 
    public static SizedListData getList(Long skip, Long take) throws EazyException {
        SizedListData  result = new SizedListData();
        try {
            DBCursor cursor = null;
            BasicDBObject sort = new BasicDBObject(SettingdbKey.EXTRA_PAGE.ORDER, 1);
            cursor = coll.find().sort(sort);
            int size = cursor.size();
            if(skip != null && take != null)
                cursor = cursor.skip(skip.intValue()).limit(take.intValue());
            List<IEntity> list = new ArrayList<IEntity>();
            while(cursor.hasNext()){
                BasicDBObject obj = (BasicDBObject) cursor.next();
                String id = obj.getObjectId(SettingdbKey.EXTRA_PAGE.ID).toString();
                String title = obj.getString(SettingdbKey.EXTRA_PAGE.PAGE_TITLE);
                String url = obj.getString(SettingdbKey.EXTRA_PAGE.URL);
                list.add(new ExtraPage(id, title, url));
            }
            result = new SizedListData(size, list);
        }catch (Exception ex) {
            Util.addErrorLog(ex);            
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }       
}
