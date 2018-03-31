/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.vn.ntsc.backend.dao.admin;


import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
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
public class RoleGroupDAO {

    private static DBCollection coll;

    static{
         try{
             coll = DBManager.getSettingDB().getCollection( SettingdbKey.ROLE_GROUP_COLLECTION );
        } catch( Exception ex ) {
            Util.addErrorLog(ex);            
        }
    }       
    
    public static void update( String group, String role) throws EazyException{
         try{
//             BasicDBObject findObj = new BasicDBObject(SettingdbKey.ROLE_GROUP.ROLE, role);
             BasicDBObject updateObj = new BasicDBObject(SettingdbKey.ROLE_GROUP.GROUP, group);
             updateObj.append(SettingdbKey.ROLE_GROUP.ROLE, role);
             coll.update(updateObj, updateObj, true, false);
        }catch( Exception ex ){
            Util.addErrorLog(ex);            
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
    }

    public static void remove( String group, String role) throws EazyException{
         try{
             BasicDBObject findObj = new BasicDBObject(SettingdbKey.ROLE_GROUP.ROLE, role);
             findObj.append(SettingdbKey.ROLE_GROUP.GROUP, group);
             coll.remove(findObj);
        }catch( Exception ex ){
            Util.addErrorLog(ex);            
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
    }      
 
    public static void removeByRole( String role) throws EazyException{
         try{
             BasicDBObject findObj = new BasicDBObject(SettingdbKey.ROLE_GROUP.ROLE, role);
             coll.remove(findObj);
        }catch( Exception ex ){
            Util.addErrorLog(ex);            
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
    }     
 
    public static void removeByGroup( String group) throws EazyException{
         try{
             BasicDBObject findObj = new BasicDBObject(SettingdbKey.ROLE_GROUP.GROUP, group);
             coll.remove(findObj);
        }catch( Exception ex ){
            Util.addErrorLog(ex);            
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
    }    
    
    public static List<String> listRole( String group) throws EazyException{
        List<String> list = new ArrayList<String>();
        try{
             BasicDBObject findObj = new BasicDBObject(SettingdbKey.ROLE_GROUP.GROUP, group);
             DBCursor cur = coll.find(findObj);
             while(cur.hasNext()){
                 BasicDBObject obj = (BasicDBObject) cur.next();
                 String role = obj.getString(SettingdbKey.ROLE_GROUP.ROLE);
                 list.add(role);
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
             BasicDBObject sort = new BasicDBObject(SettingdbKey.ROLE_GROUP.ROLE, 1);
             DBCursor cursor = coll.find().sort(sort);
             int count = 0;
             int size = cursor.count();
             String role = null;
             List<String> list = new ArrayList<String>();
             while(cursor.hasNext()){
                 BasicDBObject obj = (BasicDBObject) cursor.next();
                 String group = obj.getString(SettingdbKey.ROLE_GROUP.GROUP);
                 String rl = obj.getString(SettingdbKey.ROLE_GROUP.ROLE);
                 if(role == null || !role.equals(rl)){
                    if(role != null)
                        result.put(role, list);
                    list = new ArrayList<String>();
                    role = rl;
                    list.add(group);
                }else{
                     list.add(group);
                }
                count++;
                if(count == size)
                    result.put(role, list);
             }
        }catch( Exception ex ){
            Util.addErrorLog(ex);            
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }
    
}
