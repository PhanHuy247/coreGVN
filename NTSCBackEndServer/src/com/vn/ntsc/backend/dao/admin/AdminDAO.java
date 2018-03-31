/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.vn.ntsc.backend.dao.admin;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;
import eazycommon.constant.Constant;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.mongokey.SettingdbKey;
import eazycommon.exception.EazyException;
import eazycommon.util.Util;
import org.bson.types.ObjectId;
import com.vn.ntsc.backend.dao.DBManager;
import com.vn.ntsc.backend.entity.IEntity;
import com.vn.ntsc.backend.entity.impl.admin.Admin;


/**
 *
 * @author DuongLTD
 */
public class AdminDAO {

    private static DBCollection coll;

    private static MessageDigest md;

    static{
         try{
             md  = MessageDigest.getInstance( "MD5" );
             coll = DBManager.getSettingDB().getCollection( SettingdbKey.ADMINISTRATOR_COLLECTION );
        } catch( Exception ex ) {
            Util.addErrorLog(ex);
        }
    }

    public static IEntity login(String email, String password) throws EazyException {
        Admin result = null;
        try{
            //command find
            BasicDBObject findObject = new BasicDBObject(SettingdbKey.ADMINISTRATOR.EMAIL, email.toLowerCase());
            DBObject obj = coll.findOne(findObject);
 
            if( obj == null ){ // not found email
                    throw new EazyException(ErrorCode.EMAIL_NOT_FOUND);
            }else{ //found email
                Integer flag = (Integer) obj.get(SettingdbKey.ADMINISTRATOR.FLAG);
                if(flag == Constant.FLAG.OFF)
                    throw new EazyException(ErrorCode.DISABLE_EMAIL); 
                String dbPass = (String)obj.get(SettingdbKey.ADMINISTRATOR.PASSWORD);
                byte[] b = md.digest(password.getBytes());
                String uPass = Util.byteToString( b );
                if(uPass.equals( dbPass)){
                    result = new Admin();
                    result.id = obj.get(SettingdbKey.ADMINISTRATOR.ID).toString();
                    result.roleId = (String)obj.get(SettingdbKey.ADMINISTRATOR.ROLE_ID);
                    result.name = (String)obj.get(SettingdbKey.ADMINISTRATOR.NAME);
                }else{
                    throw new EazyException(ErrorCode.INCORRECT_PASSWORD);
                }
            }
        }catch(EazyException ex){
            Util.addErrorLog(ex);            
            throw new EazyException(ex.getErrorCode());
        }catch(Exception ex){
            Util.addErrorLog(ex);            
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;

    }

    public static boolean update(String id, String email, String password, String originalPassword, String roleId, String name, int flag) throws EazyException {
        boolean result = false;
        try{
            //serch object
            ObjectId oId = new ObjectId(id);
            BasicDBObject findObj = new BasicDBObject(SettingdbKey.ADMINISTRATOR.EMAIL, email.toLowerCase());
            BasicDBObject neObj = new BasicDBObject("$ne", oId);
            findObj.append(SettingdbKey.ADMINISTRATOR.ID, neObj);
            DBObject object = coll.findOne(findObj);
            if(object != null)
                throw new EazyException(ErrorCode.EMAIL_REGISTED);
            findObj = new BasicDBObject(SettingdbKey.ADMINISTRATOR.ID, oId);
            BasicDBObject doc = new BasicDBObject(SettingdbKey.ADMINISTRATOR.EMAIL, email.toLowerCase());
            if(password != null && !password.isEmpty()){
                byte[] b = md.digest(password.getBytes());
                String uPass = Util.byteToString( b );
                doc.append(SettingdbKey.ADMINISTRATOR.PASSWORD, uPass);   
                doc.append(SettingdbKey.ADMINISTRATOR.ORIGINAL_PASSWORD,  originalPassword);
            }
            doc.append(SettingdbKey.ADMINISTRATOR.FLAG, flag);
            doc.append(SettingdbKey.ADMINISTRATOR.NAME,  name);
            doc.append(SettingdbKey.ADMINISTRATOR.ROLE_ID, roleId);
            DBObject setObj = new BasicDBObject("$set", doc);
            coll.update(findObj, setObj);
        } catch(EazyException ex){
            Util.addErrorLog(ex);            
            throw new EazyException(ex.getErrorCode());
        } catch (Exception ex) {
            Util.addErrorLog(ex);            
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }    

    public static String deleteRole(String roleId) throws EazyException {
        String result = null;
        try{
            //serch object
            BasicDBObject findObj = new BasicDBObject(SettingdbKey.ADMINISTRATOR.ROLE_ID, roleId);
            DBCursor cur = coll.find(findObj);
            while(cur.hasNext()){
                coll.remove(cur.next());
            } 
        }catch (Exception ex) {
            Util.addErrorLog(ex);            
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }      
    
    public static String insert(String email, String password, String originalPassword, String roleId, String name, int flag) throws EazyException {
        String result = null;
        try{
            //serch object
            BasicDBObject findObj = new BasicDBObject(SettingdbKey.ADMINISTRATOR.EMAIL, email.toLowerCase());
            DBObject obj = coll.findOne(findObj);
            if(obj == null){
                byte[] b = md.digest(password.getBytes());
                String uPass = Util.byteToString( b );
                BasicDBObject doc = new BasicDBObject(SettingdbKey.ADMINISTRATOR.PASSWORD, uPass);
                doc.append(SettingdbKey.ADMINISTRATOR.EMAIL, email);
                doc.append(SettingdbKey.ADMINISTRATOR.ROLE_ID, roleId);
                doc.append(SettingdbKey.ADMINISTRATOR.NAME, name);
                doc.append(SettingdbKey.ADMINISTRATOR.ORIGINAL_PASSWORD, originalPassword);
                doc.append(SettingdbKey.ADMINISTRATOR.FLAG, flag);
                coll.insert(doc);
                result = doc.getObjectId(SettingdbKey.ADMINISTRATOR.ID).toString();
            }else{
                throw new EazyException(ErrorCode.EMAIL_REGISTED);
            }
        } catch(EazyException ex){
            Util.addErrorLog(ex);            
            throw new EazyException(ex.getErrorCode());
        }  catch (Exception ex) {
            Util.addErrorLog(ex);            
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }    
    
    public static boolean collectionExists() throws EazyException {
        try{
            return DBManager.getSettingDB().collectionExists(SettingdbKey.ADMINISTRATOR_COLLECTION);
        }catch(Exception ex){
            Util.addErrorLog(ex);            
            return false;
        }

    }

    public static boolean isSupperAdmin(String id) throws EazyException{
        boolean result = false;
        try{
            ObjectId oId = new ObjectId(id);
            DBObject findObj = new BasicDBObject(SettingdbKey.ADMINISTRATOR.ID, oId);
            DBObject obj = coll.findOne(findObj);
            if(obj != null){
                Integer specialFlag = (Integer) obj.get(SettingdbKey.ADMINISTRATOR.SPECIAL_FLAG);
                if(specialFlag != null && specialFlag == 1)
                    return true;
            }
        }catch(Exception ex){
            Util.addErrorLog(ex);            
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        
        return result;
     }    
    
    public static boolean delete(String id) throws EazyException{
        boolean result = false;
        try{
            ObjectId oId = new ObjectId(id);
            DBObject findObj = new BasicDBObject(SettingdbKey.ADMINISTRATOR.ID, oId);
            coll.remove(findObj);
            result = true;
        }catch(Exception ex){
            Util.addErrorLog(ex);            
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
     }    
    
    public static IEntity getDetail(String id) throws EazyException{
        IEntity result = new Admin();
        Util.addDebugLog("====getDetail userDenyID:"+id);
        try{
            ObjectId oId = new ObjectId(id);
            DBObject findObj = new BasicDBObject(SettingdbKey.ADMINISTRATOR.ID, oId);
            DBObject obj = coll.findOne(findObj);
            if(obj != null){
                String email = (String) obj.get(SettingdbKey.ADMINISTRATOR.EMAIL);
                String name = (String) obj.get(SettingdbKey.ADMINISTRATOR.NAME);
                String groupId = (String) obj.get(SettingdbKey.ADMINISTRATOR.ROLE_ID);
                Integer flag = (Integer) obj.get(SettingdbKey.ADMINISTRATOR.FLAG);
                result = new Admin(id, email, groupId, name, flag);
            }
        }catch(Exception ex){
            Util.addErrorLog(ex);            
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
     }    
    
    public static List<IEntity> listAdmin(Integer flag) throws EazyException{
        List<IEntity> result = new ArrayList<IEntity>();
        try{
            DBCursor cursor = null;
            if (flag != null) {
                BasicDBObject neObj = new BasicDBObject("$ne", flag.intValue());
                BasicDBObject findObj = new BasicDBObject(SettingdbKey.ADMINISTRATOR.SPECIAL_FLAG, neObj);
                cursor = coll.find(findObj);
            } else {
                cursor = coll.find();
            }
            while(cursor.hasNext()){
                BasicDBObject obj = (BasicDBObject) cursor.next();
                String id = obj.getObjectId(SettingdbKey.ADMINISTRATOR.ID).toString();
                String email = obj.getString(SettingdbKey.ADMINISTRATOR.EMAIL);
                String name = obj.getString(SettingdbKey.ADMINISTRATOR.NAME);
                String groupId = obj.getString(SettingdbKey.ADMINISTRATOR.ROLE_ID);
                Integer flg = obj.getInt(SettingdbKey.ADMINISTRATOR.FLAG);
                result.add(new Admin(id, email, groupId, name, flg));
            }
        }catch(Exception ex){
            Util.addErrorLog(ex);            
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
     }    
    
    public static boolean changePassword(String id, String oldPassword, String newPassword, String originalPassword) throws EazyException{
        boolean result = false;
        try{
            // command search
            ObjectId oId = new ObjectId(id);
            BasicDBObject findObject = new BasicDBObject(SettingdbKey.ADMINISTRATOR.ID, oId);
            DBObject dboject = coll.findOne(findObject);
            
            if(dboject != null){
                String dbPass = (String)dboject.get( SettingdbKey.ADMINISTRATOR.PASSWORD );

                //encode to md5
                byte[] b = md.digest( oldPassword.getBytes() );
                String uPass = Util.byteToString( b );

                if(dbPass.equals(uPass)){ // correct password
                    if(newPassword == null || newPassword.length() < 6)
                        throw new EazyException(ErrorCode.INVALID_PASSWORD);
                    byte[] bt = md.digest( newPassword.getBytes() );
                    newPassword = Util.byteToString( bt );
                    BasicDBObject changePass = new BasicDBObject(SettingdbKey.ADMINISTRATOR.PASSWORD, newPassword);
                    changePass.append(SettingdbKey.ADMINISTRATOR.ORIGINAL_PASSWORD, originalPassword);
                    BasicDBObject updateObj = new BasicDBObject("$set", changePass);
                    coll.update(dboject, updateObj);
                    result =  true;
                }else{ // not correct password
                    throw new EazyException(ErrorCode.INCORRECT_PASSWORD);
                }
            }else{
                throw new EazyException(ErrorCode.EMAIL_NOT_FOUND);
            }
        }catch (EazyException ex){
            Util.addErrorLog(ex);            
            throw new EazyException(ex.getErrorCode());
        }catch(Exception ex){
            Util.addErrorLog(ex);            
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
     }

    public static boolean changePassword(String email, String password) throws EazyException{
        boolean result = false;
        try{
            // command search
            BasicDBObject findObject = new BasicDBObject(SettingdbKey.ADMINISTRATOR.EMAIL, email);
            DBObject dboject = coll.findOne(findObject);
            
            if(dboject != null){
                byte[] b = md.digest(password.getBytes());
                String uPass = Util.byteToString( b );
                BasicDBObject updateObj = new BasicDBObject(SettingdbKey.ADMINISTRATOR.PASSWORD, uPass);
                BasicDBObject setOBj = new BasicDBObject("$set", updateObj);
                coll.update(findObject, setOBj);
            }
        }catch(Exception ex){
            Util.addErrorLog(ex);            
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
     }    
  
    public static boolean addSpecialFlag(String email, Integer flag) throws EazyException{
        boolean result = false;
        try{
            // command search
            BasicDBObject findObject = new BasicDBObject(SettingdbKey.ADMINISTRATOR.EMAIL, email);
            DBObject dboject = coll.findOne(findObject);
            
            if(dboject != null){
                BasicDBObject updateObj = new BasicDBObject(SettingdbKey.ADMINISTRATOR.SPECIAL_FLAG, flag);
                BasicDBObject setOBj = new BasicDBObject("$set", updateObj);
                coll.update(findObject, setOBj);
            }
        }catch(Exception ex){
            Util.addErrorLog(ex);            
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
     }     
    
    public static String getRoleId(String id) throws EazyException {
        String result = null;
        try {
            ObjectId oId = new ObjectId(id);
            DBObject findObj = new BasicDBObject(SettingdbKey.ADMINISTRATOR.ID, oId);
            DBObject obj = coll.findOne(findObj);
            if(obj != null)
                result = (String) obj.get(SettingdbKey.ADMINISTRATOR.ROLE_ID);
        } catch (Exception ex) {
            Util.addErrorLog(ex);            
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }      
}
