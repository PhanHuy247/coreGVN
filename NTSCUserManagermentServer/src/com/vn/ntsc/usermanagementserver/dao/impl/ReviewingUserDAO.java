/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.usermanagementserver.dao.impl;

import eazycommon.util.DateFormat;
import eazycommon.util.Util;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import eazycommon.constant.Constant;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.mongokey.UserdbKey;
import eazycommon.exception.EazyException;
import com.vn.ntsc.usermanagementserver.dao.DatabaseLoader;
import com.vn.ntsc.usermanagementserver.entity.impl.user.ReviewingUser;

/**
 *
 * @author RuAc0n
 */
public class ReviewingUserDAO {

    private static DBCollection coll;
    
    static {
        try {
            coll = DatabaseLoader.getUserDB().getCollection(UserdbKey.REVIEWING_USER_COLLECTION);
        } catch (Exception ex) {
            //Future logging here
            Util.addErrorLog(ex);
           
        }
    }

    public static String addReviewUser(String userId, int gender, ReviewingUser user) throws EazyException {
        String result = null;
        try {
            BasicDBObject insertObj = new BasicDBObject();
            if(gender == Constant.GENDER.FEMALE){
                if(isValidate(user.typeOfMan) ){
                    insertObj.append(UserdbKey.REVIEWING_USER.TYPE_OF_MAN, user.typeOfMan);
                }
                if(isValidate(user.fetish) ){
                    insertObj.append(UserdbKey.REVIEWING_USER.FETISH, user.fetish);
                }
                if(isValidate(user.about) ){
                    insertObj.append(UserdbKey.REVIEWING_USER.ABOUT, user.about);
                }
//                if(isValidate(user.hobby) ){
//                    insertObj.append(UserdbKey.REVIEWING_USER.HOBBY, user.hobby);
//                }
            }else if(gender == Constant.GENDER.MALE){
//                if(isValidate(user.hobby) ){
//                    insertObj.append(UserdbKey.REVIEWING_USER.HOBBY, user.hobby);
//                }
                if(isValidate(user.about) ){
                    insertObj.append(UserdbKey.REVIEWING_USER.ABOUT, user.about);
                }
            }
            if(!insertObj.isEmpty()){
                insertObj.append(UserdbKey.REVIEWING_USER.USER_ID, userId);
                insertObj.append(UserdbKey.REVIEWING_USER.GENDER, gender);
                insertObj.append(UserdbKey.REVIEWING_USER.UPLOAD_TIME, Util.currentTime());
                coll.insert(insertObj);
                result = insertObj.get(UserdbKey.REVIEWING_USER.ID).toString();
            }
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }

    private static boolean isValidate(String inputString){
        return inputString != null && !inputString.isEmpty();
    }
    public static void remove(String userId) throws EazyException{
        try{
            BasicDBObject removeObj = new BasicDBObject(UserdbKey.REVIEWING_USER.USER_ID, userId);
            coll.remove(removeObj);
        }catch(Exception ex){
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
    }
    
    public static ReviewingUser get(String id) throws EazyException{
        ReviewingUser result = null;
        try{
            DBObject findObj = new BasicDBObject(UserdbKey.REVIEWING_USER.USER_ID, id);
            DBObject obj = coll.findOne(findObj);
            if(obj != null){
                result = new ReviewingUser();
                String typeOfMan = (String) obj.get(UserdbKey.REVIEWING_USER.TYPE_OF_MAN);
                result.typeOfMan = typeOfMan;
                String fetish = (String) obj.get(UserdbKey.REVIEWING_USER.FETISH);
                result.fetish = fetish;
                String about = (String) obj.get(UserdbKey.REVIEWING_USER.ABOUT);
                result.about = about;
                result.userId = id;
                String hobby = (String)obj.get(UserdbKey.REVIEWING_USER.HOBBY);
                result.hobby = hobby;
                Integer gender = (Integer)obj.get(UserdbKey.REVIEWING_USER.GENDER);
                result.gender = gender;
                Long time = (Long)obj.get(UserdbKey.REVIEWING_USER.UPLOAD_TIME);
                if(time != null)
                    result.time = DateFormat.format(time);
            }
        }catch(Exception ex){
            Util.addErrorLog(ex);            
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);   
        }
        return result;
    }      
}
