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
import com.mongodb.QueryBuilder;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.mongokey.SettingdbKey;
import eazycommon.exception.EazyException;
import eazycommon.util.Util;
import com.vn.ntsc.backend.dao.DBManager;

/**
 *
 * @author RuAc0n
 */
public class ScreenAPIDAO {

    private static DBCollection coll;

    static{
         try{
             coll = DBManager.getSettingDB().getCollection( SettingdbKey.SCREEN_API_COLLECTION );
        } catch( Exception ex ) {
            Util.addErrorLog(ex);            
        }
    }       
    
    public static void update( String screenId, String api) throws EazyException{
         try{
             BasicDBObject updateObj = new BasicDBObject(SettingdbKey.SCREEN_API.SCREEN, screenId);
             updateObj.append(SettingdbKey.SCREEN_API.API, api);
             coll.update(updateObj, updateObj, true, false);
        }catch( Exception ex ){
            Util.addErrorLog(ex);            
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
    }

    public static void remove( String screenId, String api) throws EazyException{
         try{
             BasicDBObject findObj = new BasicDBObject(SettingdbKey.SCREEN_API.API, api);
             findObj.append(SettingdbKey.SCREEN_API.SCREEN, screenId);
             coll.remove(findObj);
        }catch( Exception ex ){
            Util.addErrorLog(ex);            
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
    }      
 
    public static void removeByScreen( List<String> listScreenId) throws EazyException{
         try{
             DBObject findObj = QueryBuilder.start(SettingdbKey.SCREEN_API.SCREEN).in(listScreenId).get();
             coll.remove(findObj);
        }catch( Exception ex ){
            Util.addErrorLog(ex);            
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
    }    
    
    public static List<String> listApi( String screenId) throws EazyException{
        List<String> list = new ArrayList<String>();
        try{
             BasicDBObject findObj = new BasicDBObject(SettingdbKey.SCREEN_API.SCREEN, screenId);
             DBCursor cur = coll.find(findObj);
             while(cur.hasNext()){
                 BasicDBObject obj = (BasicDBObject) cur.next();
                 String api = obj.getString(SettingdbKey.SCREEN_API.API);
                 list.add(api);
             }
        }catch( Exception ex ){
            Util.addErrorLog(ex);            
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return list;
    }    
    
    public static TreeMap<String, List<String>> getAll( ) throws EazyException{
        TreeMap<String, List<String>> result = new TreeMap<String, List<String>>();
         try{
             BasicDBObject sort = new BasicDBObject(SettingdbKey.SCREEN_API.API, 1);
             DBCursor cursor = coll.find().sort(sort);
             int count = 0;
             int size = cursor.count();
             String apiName = null;
             List<String> list = new ArrayList<String>();
             while(cursor.hasNext()){
                 BasicDBObject obj = (BasicDBObject) cursor.next();
                 String api = obj.getString(SettingdbKey.SCREEN_API.API);
                 String screen = obj.getString(SettingdbKey.SCREEN_API.SCREEN);
                 if(apiName == null || !api.equals(apiName)){
                    if(apiName != null)
                        result.put(apiName, list);
                    list = new ArrayList<String>();
                    apiName = api;
                    list.add(screen);
                }else{
                     list.add(screen);
                }
                count++;
                if(count == size)
                    result.put(apiName, list);
             }
        }catch( Exception ex ){
            Util.addErrorLog(ex);            
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }      

}
