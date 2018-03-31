/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.backend.dao.news;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import java.util.ArrayList;
import java.util.List;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.mongokey.SettingdbKey;
import eazycommon.exception.EazyException;
import eazycommon.util.DateFormat;
import eazycommon.util.Util;
import org.bson.types.ObjectId;
import com.vn.ntsc.backend.dao.DBManager;
import com.vn.ntsc.backend.entity.impl.datarespond.SizedListData;
import com.vn.ntsc.backend.entity.impl.news.News;

/**
 *
 * @author HuyDX
 */
public class NewsDAO {

    private static DBCollection coll;

    static {
        try {
            coll = DBManager.getSettingDB().getCollection(SettingdbKey.NEWS_COLLECTION);
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
    }
    
    public static String insert(News news) throws EazyException {
        String result = null;
        try{
            DBObject insertObj = new BasicDBObject();
            insertObj.put(SettingdbKey.NEWS.title, news.getTitle());
            if (news.getBannerId()!=null)
                insertObj.put(SettingdbKey.NEWS.banner_id, news.getBannerId());
            insertObj.put(SettingdbKey.NEWS.content, news.getContent());
            insertObj.put(SettingdbKey.NEWS.update_date, news.getUpdateDate());
            insertObj.put(SettingdbKey.NEWS.from, news.getFrom());
            insertObj.put(SettingdbKey.NEWS.to, news.getTo());
            
            //Linh 6/10/2016 #4858
            insertObj.put(SettingdbKey.NEWS.registerFromStr, news.getRegisterFromStr());
            insertObj.put(SettingdbKey.NEWS.registerToStr, news.getRegisterToStr());
            insertObj.put(SettingdbKey.NEWS.registerType, news.getRegisterType());
            insertObj.put(SettingdbKey.NEWS.userType, news.getUserType());
            
            // LongLT 19Sep2016 /////////////////////////// START #4597
            insertObj.put(SettingdbKey.NEWS.IS_PURCHASED, news.getIsPurchased());
            insertObj.put(SettingdbKey.NEWS.haveEmail, news.getHaveEmail());
            
            if (news.getDevice_type()!=null)
                insertObj.put(SettingdbKey.NEWS.device_type, news.getDevice_type());
            insertObj.put(SettingdbKey.NEWS.is_shown, news.getIsShown());
            if (news.getTargetGender()!=null)
                insertObj.put(SettingdbKey.NEWS.target_gender, news.getTargetGender());
            coll.insert(insertObj);
            result = insertObj.get(SettingdbKey.NEWS.ID).toString();
            
        }
        catch (Exception ex){
            Util.addErrorLog(ex);            
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);            
        }
        return result;
    }

    public static boolean update(News news) throws EazyException {
        boolean result = false;
        try {
            DBObject findObj = new BasicDBObject(SettingdbKey.NEWS.ID, new ObjectId(news.getId()));
            DBObject updateObj = new BasicDBObject();
            updateObj.put(SettingdbKey.NEWS.title, news.getTitle());
            if (news.getBannerId()!=null)
                updateObj.put(SettingdbKey.NEWS.banner_id, news.getBannerId());
            updateObj.put(SettingdbKey.NEWS.content, news.getContent());
            updateObj.put(SettingdbKey.NEWS.update_date, Util.getGMTTime().getTime());
            updateObj.put(SettingdbKey.NEWS.from, news.getFrom());
            updateObj.put(SettingdbKey.NEWS.to, news.getTo());
            updateObj.put(SettingdbKey.NEWS.device_type, news.getDevice_type());
            updateObj.put(SettingdbKey.NEWS.target_gender, news.getTargetGender());
            
            updateObj.put(SettingdbKey.NEWS.IS_PURCHASED, news.getIsPurchased());
            updateObj.put(SettingdbKey.NEWS.haveEmail, news.getHaveEmail());
            
            //Linh 6/10/2016 #4858
            updateObj.put(SettingdbKey.NEWS.registerFromStr, news.getRegisterFromStr());
            updateObj.put(SettingdbKey.NEWS.registerToStr, news.getRegisterToStr());
            updateObj.put(SettingdbKey.NEWS.registerType, news.getRegisterType());
            updateObj.put(SettingdbKey.NEWS.userType, news.getUserType());            
            
            DBObject setObj = new BasicDBObject("$set", updateObj);
            coll.update(findObj, setObj);
            DBObject unsetObj;
            
            if (news.getDevice_type()==null){
                unsetObj = new BasicDBObject(SettingdbKey.NEWS.device_type, "");
                coll.update(findObj, new BasicDBObject("$unset", unsetObj));
            }
            if (news.getTargetGender()==null){
                unsetObj = new BasicDBObject(SettingdbKey.NEWS.target_gender, "");
                coll.update(findObj, new BasicDBObject("$unset", unsetObj));
            }
            result = true;
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }
    
    public static boolean delete(String id) throws EazyException{
        boolean result = false;
         try{
             DBObject findObj = new BasicDBObject(SettingdbKey.NEWS.ID, new ObjectId(id));
             coll.remove(findObj);
             result = true;
        }catch( Exception ex ){
            Util.addErrorLog(ex);            
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return result;
    }    
    
    public static News get(String id) throws EazyException {
        //Hello
        News news = null;
        try{
            DBObject findObj = new BasicDBObject(SettingdbKey.NEWS.ID, new ObjectId(id));
            DBObject result = coll.findOne(findObj);
            if (result!=null){
                news = new News();
                news.setContent(result.get(SettingdbKey.NEWS.content).toString());
                news.setUpdateDate((long) result.get(SettingdbKey.NEWS.update_date));
                news.setUpdateDateStr(DateFormat.format(news.getUpdateDate()));
                news.setFrom((long) result.get(SettingdbKey.NEWS.from));
                news.setFromStr(DateFormat.format(news.getFrom()));
                news.setTo((long) result.get(SettingdbKey.NEWS.to));
                news.setToStr(DateFormat.format(news.getTo()));
                news.setId(id);
                news.setIsShown((Boolean) result.get(SettingdbKey.NEWS.is_shown));
                if (result.get(SettingdbKey.NEWS.device_type)!=null)
                    news.setDevice_type((Integer) result.get(SettingdbKey.NEWS.device_type));
                if (result.get(SettingdbKey.NEWS.target_gender)!=null)
                    news.setTargetGender((Integer) result.get(SettingdbKey.NEWS.target_gender));
                news.setTitle(result.get(SettingdbKey.NEWS.title).toString());
                if (result.get(SettingdbKey.NEWS.banner_id)!=null){
                    news.setBannerId(result.get(SettingdbKey.NEWS.banner_id).toString()); 
                }
                
                //Linh 6/10/2016 #4858
                if (result.get(SettingdbKey.NEWS.registerFromStr) != null){
//                    news.setRegisterFrom((long) result.get(SettingdbKey.NEWS.registerFromStr));
//                    news.setRegisterFromStr(DateFormat.format(news.getRegisterFrom()));
                    news.setRegisterFromStr(result.get(SettingdbKey.NEWS.registerFromStr).toString());
                }
                if (result.get(SettingdbKey.NEWS.registerToStr) != null){
//                    news.setRegisterTo((long) result.get(SettingdbKey.NEWS.registerToStr));
//                    news.setRegisterToStr(DateFormat.format(news.getRegisterTo()));
                    news.setRegisterToStr(result.get(SettingdbKey.NEWS.registerToStr).toString());
                }
                if (result.get(SettingdbKey.NEWS.registerType) != null){
                    news.setRegisterType((long) result.get(SettingdbKey.NEWS.registerType));
                }
                if (result.get(SettingdbKey.NEWS.userType) != null){
                    news.setUserType((long) result.get(SettingdbKey.NEWS.userType));
                }
                
                // LongLT 19Sep2016 ///////////////////////////  #4597
                if (result.get(SettingdbKey.NEWS.IS_PURCHASED)!=null){
                    news.setIsPurchased((Boolean)result.get(SettingdbKey.NEWS.IS_PURCHASED)); 
                }
                if (result.get(SettingdbKey.NEWS.haveEmail)!=null){
                    news.setHaveEmail((Boolean)result.get(SettingdbKey.NEWS.haveEmail)); 
                }
            }
        } catch (Exception ex){
            Util.addErrorLog(ex);            
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);            
        }
        return news;
    }    
    
    public static SizedListData list(int skip, int take) throws EazyException{
        final int DES = -1;
        SizedListData result = null;
        try {
            List<News> newsList = new ArrayList<>();
            DBCursor cursor = coll.find();
            int size = cursor.size();
            cursor = cursor.skip(skip).limit(take);
            DBObject orderBy = new BasicDBObject(SettingdbKey.NEWS.update_date, DES);
            cursor.sort(orderBy);
            if (cursor.size()!=0){
                while (cursor.hasNext()){
                    News news = new News();
                    BasicDBObject currentObj = (BasicDBObject) cursor.next();
                    long from = (long) currentObj.get(SettingdbKey.NEWS.from);
                    long to = (long)currentObj.get(SettingdbKey.NEWS.to);
                    long currentTime = Util.getGMTTime().getTime();
                    if (from>currentTime)
                        news.setStatus(1); //news's not yet show to the app user
                    else if (currentTime < to)
                        news.setStatus(0); // news's showing to the app user
                    else 
                        news.setStatus(-1); //news showed and no longer needed
                    Boolean isShowed = currentObj.getBoolean(SettingdbKey.NEWS.is_shown);
                    if (!isShowed)
                        news.setStatus(1);
                    else {
                        if (from>currentTime)
                            news.setStatus(1); //news's not yet show to the app user
                        else if (currentTime < to)
                            news.setStatus(0); // news's showing to the app user
                        else 
                            news.setStatus(-1); //news showed and no longer needed
                    }
                    news.setUpdateDateStr(DateFormat.format_yyyyMMddHHmm(((long) currentObj.get(SettingdbKey.NEWS.update_date))));
                    news.setFromStr(DateFormat.format_yyyyMMddHHmm(from));
                    news.setToStr(DateFormat.format_yyyyMMddHHmm(to));
                    news.setContent(currentObj.get(SettingdbKey.NEWS.content).toString());
                    news.setUpdateDate((long) currentObj.get(SettingdbKey.NEWS.update_date));
                    news.setFrom(from);
                    news.setTo(to);
                    news.setId(currentObj.get(SettingdbKey.NEWS.ID).toString());
                    if (currentObj.get(SettingdbKey.NEWS.device_type)!=null)
                        news.setDevice_type((Integer) currentObj.get(SettingdbKey.NEWS.device_type));
                    if (currentObj.get(SettingdbKey.NEWS.target_gender)!=null)
                        news.setTargetGender((Integer) currentObj.get(SettingdbKey.NEWS.target_gender));
                    news.setTitle(currentObj.get(SettingdbKey.NEWS.title).toString());
                    news.setIsShown(currentObj.getBoolean(SettingdbKey.NEWS.is_shown));
                    if (currentObj.get(SettingdbKey.NEWS.banner_id)!=null)
                        news.setBannerId(currentObj.get(SettingdbKey.NEWS.banner_id).toString());      
                    // LongLT 19Sep2016 /////////////////////////// START #4597
                    if (currentObj.get(SettingdbKey.NEWS.IS_PURCHASED)!=null)
                        news.setIsPurchased((Boolean) currentObj.get(SettingdbKey.NEWS.IS_PURCHASED));      
                    newsList.add(news);
                }
                result = new SizedListData(size, newsList);
            }
        } catch (Exception ex){
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);                        
        }
        return result;
    }

    public static boolean updateShown() throws EazyException{
        boolean result = false;
        try {
            DBObject findObj = new BasicDBObject(SettingdbKey.NEWS.is_shown, false);
            DBObject updateObj = new BasicDBObject("$set", new BasicDBObject(SettingdbKey.NEWS.is_shown, true));
            coll.update(findObj, updateObj, false, true);
            result = true;
        } catch (Exception ex){
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);                        
        }
        return result;
    }
}
