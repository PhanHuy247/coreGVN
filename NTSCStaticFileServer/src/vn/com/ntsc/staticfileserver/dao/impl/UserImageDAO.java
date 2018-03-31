/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.com.ntsc.staticfileserver.dao.impl;

import eazycommon.util.Util;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import eazycommon.constant.Constant;
import eazycommon.constant.mongokey.UserdbKey;
import eazycommon.exception.EazyException;
import eazycommon.inspection.version.InspectionVersionDAO;
import org.bson.types.ObjectId;
import vn.com.ntsc.staticfileserver.dao.DAO;

/**
 *
 * @author DuongLTD
 */
public class UserImageDAO {

    private static DBCollection coll;
    private static DBCollection collUser;
    
    static {
        try {
            coll = DAO.getUserDB().getCollection(UserdbKey.IMAGE_COLLECTION);
            collUser = DAO.getUserDB().getCollection(UserdbKey.USERS_COLLECTION);
        } catch (Exception ex) {
            //Future logging here
            Util.addErrorLog(ex);            
           
        }
    }

    public static boolean imageExist(String imageId, String userId) {
        boolean result = false;
        BasicDBObject findObject = new BasicDBObject(UserdbKey.IMAGE.IMAGE_ID, imageId);
        DBObject respondObj = coll.findOne(findObject);
        if (respondObj != null) {
            Integer flag = (Integer) respondObj.get(UserdbKey.IMAGE.FLAG);
            if (flag != null && flag == Constant.FLAG.ON) {
                String usId = (String) respondObj.get(UserdbKey.IMAGE.USER_ID);
                Integer status = (Integer) respondObj.get(UserdbKey.IMAGE.STATUS);
                if(status == Constant.REVIEW_STATUS_FLAG.APPROVED || (userId.equals(usId) && status != Constant.REVIEW_STATUS_FLAG.DENIED) )
                    if(!safeImage(userId,usId)){
                        return false;
                    }

                    return true;
            }
        }
        return result;
    }
    
    //userIdアプリユーザ
    //
    public static boolean safeImage(String userId,String imgOwner) {
        try{
            
            if (userId == null) {
                return true;
            }
            if (imgOwner == null) {
                return true;
            }

            //iosテストバージョン取得
            String testVaersion = InspectionVersionDAO.getIOSTurnOffSafaryVersion();
            
            ObjectId id = new ObjectId(userId);
            BasicDBObject obj = new BasicDBObject(UserdbKey.USER.ID, id);
            DBObject dbOject = collUser.findOne(obj);
            String appVersion = (String) dbOject.get(UserdbKey.USER.APP_VERSION);
            Integer deviceType = (Integer) dbOject.get(UserdbKey.USER.DEVICE_TYPE);
            String application = "1";
            
            if(dbOject.get(UserdbKey.USER.APPLICATION_ID) != null) {
                application = (String) dbOject.get(UserdbKey.USER.APPLICATION_ID) ;
            }
            if(!"2".equals(application)) {
                return true;
            }
            Integer sex = (Integer) dbOject.get(UserdbKey.USER.GENDER);
            //女性ならばOK
            if (sex == null) {
                return true;
            }
            if (sex.compareTo(new Integer(1)) == 0) {
                return true;
            }
            
            //対象デバイスはiOS
            if (deviceType == null) {
                return true;
            }
            if (deviceType.compareTo(new Integer(0)) != 0) {
                return true;
            }
            //バージョンがないということは古いユザー
            if(appVersion == null){
                return true;
            }
            //テストバージョンじゃないのでOK
            if(!appVersion.equals(testVaersion)){
                return true;
            }

            id = new ObjectId(imgOwner);
            obj = new BasicDBObject(UserdbKey.USER.ID, id);
            dbOject = collUser.findOne(obj);
            appVersion = (String) dbOject.get(UserdbKey.USER.APP_VERSION);
            Integer safetyUser = (Integer) dbOject.get(UserdbKey.USER.SAFE_USER);
            
            //バージョンがないということは古いユザー
            if(appVersion == null){
                return true;
            }
            //相手の人もテストバージョンなので表示
            if(appVersion.equals(testVaersion)){
                return true;
            }
            //この人安全な人なのでOK
            if(safetyUser != null){
                if(safetyUser.compareTo(new Integer(1)) == 0){
                    return true;
                }
            }
        }catch(EazyException ex){
            Util.addErrorLog(ex);
        }
        //それ以外は非表示
        return false;
    }
    
}
