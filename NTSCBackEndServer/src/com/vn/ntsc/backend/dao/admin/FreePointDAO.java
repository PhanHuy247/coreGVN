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
import com.vn.ntsc.backend.entity.impl.freepoint.FreePoint;

/**
 *
 * @author RuAc0n
 */
public class FreePointDAO {
    private static DBCollection coll;

    private static final int NAME_ERROR = 4;
    private static final int NUMBER_ERROR = 5;

    static{
         try{
             coll = DBManager.getSettingDB().getCollection(SettingdbKey.FREE_POINT_COLLECTION );
        } catch( Exception ex ) {
            Util.addErrorLog(ex);            
        }
    }
    
    public static String insert(int number, String name) throws EazyException {
        String result = null;
        try {
            DBObject insertObj = new BasicDBObject(SettingdbKey.FREE_POINT.FREE_POINT_NAME, name);
            insertObj.put(SettingdbKey.FREE_POINT.FREE_POINT_NUMBER, number);
            coll.insert(insertObj);
            result = insertObj.get(SettingdbKey.FREE_POINT.ID).toString();
        }catch (Exception ex) {
            Util.addErrorLog(ex);            
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    public static boolean update(String id , int number, String name) throws EazyException {
        boolean  result = false;
        try {
            ObjectId oId = new ObjectId(id);
            DBObject findObj = new BasicDBObject(SettingdbKey.FREE_POINT.ID, oId);
            DBObject updateObj = new BasicDBObject(SettingdbKey.FREE_POINT.FREE_POINT_NAME, name);
            updateObj.put(SettingdbKey.FREE_POINT.FREE_POINT_NUMBER, number);
            DBObject setObj = new BasicDBObject("$set", updateObj);
            coll.update(findObj, setObj);
            result = true;
        }catch (Exception ex) {
            Util.addErrorLog(ex);            
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }    
    
    public static int validate(String id, String name, int number) throws EazyException {
        int result = ErrorCode.SUCCESS;
        try {
            BasicDBObject[] ors = new BasicDBObject[2];
            ors[0] = new BasicDBObject(SettingdbKey.FREE_POINT.FREE_POINT_NAME, name);
            ors[1] = new BasicDBObject(SettingdbKey.FREE_POINT.FREE_POINT_NUMBER, number);
            BasicDBObject findObj = new BasicDBObject("$or", ors);
            if(id != null){
                findObj.append(SettingdbKey.FREE_POINT.ID, new BasicDBObject("$ne", new ObjectId(id)));
            }
            DBCursor cur = coll.find(findObj);
            while(cur.hasNext()){
                DBObject obj = cur.next();
                String pName = (String) obj.get(SettingdbKey.FREE_POINT.FREE_POINT_NAME);
                if(pName.equals(name)){
                    return NAME_ERROR;
                }
                int pNumber = (int) obj.get(SettingdbKey.FREE_POINT.FREE_POINT_NUMBER);
                if(pNumber == number){
                    return NUMBER_ERROR;
                }
            }
        }catch (Exception ex) {
            Util.addErrorLog(ex);            
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    } 
 
    public static SizedListData getList(int skip, int take) throws EazyException {
        SizedListData  result = new SizedListData();
        try {
            DBCursor cursor = null;
            BasicDBObject sort = new BasicDBObject(SettingdbKey.FREE_POINT.FREE_POINT_NUMBER, 1);
            cursor = coll.find().sort(sort);
            int size = cursor.size();
            if(skip != 0 || take != 0)
                cursor = cursor.skip(skip).limit(take);
            List<IEntity> list = new ArrayList<IEntity>();
            while(cursor.hasNext()){
                BasicDBObject obj = (BasicDBObject) cursor.next();
                String id = obj.getObjectId(SettingdbKey.FREE_POINT.ID).toString();
                String name = obj.getString(SettingdbKey.FREE_POINT.FREE_POINT_NAME);
                Integer number = obj.getInt(SettingdbKey.FREE_POINT.FREE_POINT_NUMBER);
                list.add(new FreePoint(id, name, number));
            }
            result = new SizedListData(size, list);
        }catch (Exception ex) {
            Util.addErrorLog(ex);            
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }   
    
}
