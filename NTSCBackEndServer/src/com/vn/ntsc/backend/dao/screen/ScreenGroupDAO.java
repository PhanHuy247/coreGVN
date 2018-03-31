/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.vn.ntsc.backend.dao.screen;

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
import com.vn.ntsc.backend.entity.impl.screen.ScreenGroup;

/**
 *
 * @author RuAc0n
 */
public class ScreenGroupDAO {

    private static DBCollection coll;

    static{
         try{
             coll = DBManager.getSettingDB().getCollection( SettingdbKey.SCREEN_GROUP_COLLECTION );
        } catch( Exception ex ) {
            Util.addErrorLog(ex);            
        }
    }       
    
      public static String insert(String title,int flag, String name, int order) throws EazyException{
        String result = null;
         try{
             DBObject insertObj = new BasicDBObject(SettingdbKey.SCREEN_GROUP.TITLE, title);
             insertObj.put(SettingdbKey.SCREEN_GROUP.FLAG, flag);
             insertObj.put(SettingdbKey.SCREEN_GROUP.NAME, name);
             insertObj.put(SettingdbKey.SCREEN_GROUP.ORDER, order);
             coll.insert(insertObj);
             result = insertObj.get(SettingdbKey.SCREEN_GROUP.ID).toString();
        }catch( Exception ex ){
            Util.addErrorLog(ex);            
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }
      
      public static boolean update(String id, String title, int flag, String name, int order) throws EazyException{
        boolean result = false;
         try{
             ObjectId oId = new ObjectId(id);
             DBObject findObj = new BasicDBObject(SettingdbKey.SCREEN_GROUP.ID, oId);
             DBObject updateObj = new BasicDBObject(SettingdbKey.SCREEN_GROUP.TITLE, title);        
             updateObj.put(SettingdbKey.SCREEN_GROUP.FLAG, flag);
             updateObj.put(SettingdbKey.SCREEN_GROUP.NAME, name);
             updateObj.put(SettingdbKey.SCREEN_GROUP.ORDER, order);
             BasicDBObject setObj = new BasicDBObject("$set", updateObj);
             coll.update(findObj, setObj);
             result = true;
        }catch( Exception ex ){
            Util.addErrorLog(ex);            
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }     

      public static boolean remove(String id) throws EazyException{
        boolean result = false;
         try{
             ObjectId oId = new ObjectId(id);
             DBObject findObj = new BasicDBObject(SettingdbKey.SCREEN_GROUP.ID, oId);
             coll.remove(findObj);
             result = true;
        }catch( Exception ex ){
            Util.addErrorLog(ex);            
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }      
      
    public static List<IEntity> getAll(Long flg) throws EazyException{
        List<IEntity> result = new ArrayList<IEntity>();
         try{
             DBCursor cursor = null;
             BasicDBObject sort = new BasicDBObject(SettingdbKey.SCREEN_GROUP.ORDER, 1);
             if(flg != null){
                 BasicDBObject findObj = new BasicDBObject(SettingdbKey.SCREEN_GROUP.FLAG, flg.intValue());
                 cursor = coll.find(findObj).sort(sort);
             }else{
                 cursor = coll.find().sort(sort);
             }
             while(cursor.hasNext()){
                 BasicDBObject obj = (BasicDBObject) cursor.next();
                 String id = obj.getObjectId(SettingdbKey.SCREEN_GROUP.ID).toString();
                 String title = obj.getString(SettingdbKey.SCREEN_GROUP.TITLE);
                 Integer flag = obj.getInt(SettingdbKey.SCREEN_GROUP.FLAG);
                 String name = obj.getString(SettingdbKey.SCREEN_GROUP.NAME);
                 Integer order = obj.getInt(SettingdbKey.SCREEN_GROUP.ORDER);
                 result.add(new ScreenGroup(id, title, flag, name, order));
             }
        }catch( Exception ex ){
            Util.addErrorLog(ex);            
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }
           
}
