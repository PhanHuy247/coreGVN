/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.backend.dao.user;

import eazycommon.util.DateFormat;
import eazycommon.util.Util;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import java.util.ArrayList;
import java.util.List;
import eazycommon.constant.Constant;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.mongokey.UserdbKey;
import eazycommon.exception.EazyException;
import org.bson.types.ObjectId;
import com.vn.ntsc.backend.dao.DBManager;
import com.vn.ntsc.backend.entity.IEntity;
import com.vn.ntsc.backend.entity.impl.datarespond.SizedListData;
import com.vn.ntsc.backend.entity.impl.user.ReviewingUser;

/**
 *
 * @author RuAc0n
 */
public class ReviewingUserDAO {

    private static DBCollection coll;
    
    static {
        try {
            coll = DBManager.getUserDB().getCollection(UserdbKey.REVIEWING_USER_COLLECTION);
        } catch (Exception ex) {
            //Future logging here
            Util.addErrorLog(ex);
           
        }
    }

    public static SizedListData getReviewingUser( String userId, Long sort, Long order, int skip, int take) throws EazyException{
        SizedListData result = new SizedListData();
        try {
            DBObject findObject = new BasicDBObject();
            if (userId != null && !userId.isEmpty()) {
                ArrayList<BasicDBObject> listFindObject = new ArrayList<>();
                String listUserId[] = userId.split("[,、，､]");
                for (String i : listUserId) {
                    if (i != null && !i.trim().isEmpty()) {
                        i = i.trim();
                        for (String str : Constant.SPECIAL_CHARACTER) {
                            if (i.contains(str)) {
                                String string = "\\" + str;
                                i = i.replace(str, string);
                            }
                        }
                        BasicDBObject regex = new BasicDBObject("$regex", i.toLowerCase());
                        BasicDBObject findObjectUserId = new BasicDBObject(UserdbKey.REVIEWING_USER.USER_ID, regex);
                        listFindObject.add(findObjectUserId);
                    }
                }
                findObject.put("$or", listFindObject);
            }
            if (order == null) {
                return null;
            }
            int or = -1;
            if (order == Constant.FLAG.ON) {
                or = 1;
            }
            if (sort == null) {
                return null;
            }
            BasicDBObject sortObj = new BasicDBObject();
            if (sort == 1) {
                sortObj.append(UserdbKey.REVIEWING_USER.UPLOAD_TIME, or);
            } 
            DBCursor cursor = coll.find(findObject).sort(sortObj);
            //set to list
            Integer number = cursor.size();
            cursor = cursor.skip(skip).limit(take);
            List<IEntity> list = new ArrayList<>();
            while (cursor.hasNext()) {
                DBObject dbObject = cursor.next();
                ReviewingUser reviewingUser = new ReviewingUser();
                String typeOfMan = (String) dbObject.get(UserdbKey.REVIEWING_USER.TYPE_OF_MAN);
                reviewingUser.typeOfMan = typeOfMan;
                String fetish = (String) dbObject.get(UserdbKey.REVIEWING_USER.FETISH);
                reviewingUser.fetish = fetish;
                String about = (String) dbObject.get(UserdbKey.REVIEWING_USER.ABOUT);
                //HUNGDT 3678
                reviewingUser.about = Util.replaceBannedWordBackend(about);
                String id = (String) dbObject.get(UserdbKey.REVIEWING_USER.USER_ID);
                reviewingUser.userId = id;
                String hobby = (String)dbObject.get(UserdbKey.REVIEWING_USER.HOBBY);
                reviewingUser.hobby = hobby;
                Integer gender = (Integer)dbObject.get(UserdbKey.REVIEWING_USER.GENDER);
                reviewingUser.gender = gender;
                Long time = (Long)dbObject.get(UserdbKey.REVIEWING_USER.UPLOAD_TIME);
                if(time != null)
                    reviewingUser.time = DateFormat.format(time);
                list.add(reviewingUser);
            }
            result = new SizedListData(number, list);
        }catch (Exception ex) {
            Util.addErrorLog(ex);            
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }    
    
    public static String removeReviewField(String id, String field) throws EazyException{
        String result = null;
        try{
            DBObject findObj = new BasicDBObject(UserdbKey.REVIEWING_USER.USER_ID, id);
            BasicDBObject addition = new BasicDBObject(field, Constant.FLAG.ON);
            BasicDBObject command = new BasicDBObject("$unset", addition);
            coll.update(findObj, command);
        }catch(Exception ex){
            Util.addErrorLog(ex);            
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);   
        }
        return result;
    }  
    
    public static String getReviewField(String id, String field) throws EazyException{
        String result = null;
        try{
            DBObject findObj = new BasicDBObject(UserdbKey.REVIEWING_USER.USER_ID, id);
            DBObject obj = coll.findOne(findObj);
            if(obj != null){
                result = obj.get(field).toString();
            }
        }catch(Exception ex){
            Util.addErrorLog(ex);            
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);   
        }
        return result;
    }    
    
    public static ReviewingUser get(String id) throws EazyException{
        ReviewingUser result = null;
        try{
            DBObject findObj = new BasicDBObject(UserdbKey.REVIEWING_USER.USER_ID, id);
            DBObject obj = coll.findOne(findObj);
            if(obj != null){
                result = new ReviewingUser();
                ObjectId objId = (ObjectId) obj.get(UserdbKey.REVIEWING_USER.ID);
                result.id = objId;
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
                    result.time = time.toString();
            }
        }catch(Exception ex){
            Util.addErrorLog(ex);            
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);   
        }
        return result;
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
}
