/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.dao.impl;

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
import com.vn.ntsc.dao.DBLoader;
import com.vn.ntsc.otherservice.entity.impl.News;

/**
 *
 * @author HuyDX
 */
public class NewsDAO {

    private static DBCollection coll;

    static {
        try {
            coll = DBLoader.getSettingDB().getCollection(SettingdbKey.NEWS_COLLECTION);
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
    }

    public static List<News> list(long device_type, long gender, Boolean is_purchase, Boolean have_email, Long user_type, Long reg_date) throws EazyException {
        final int DES = -1;
        List<News> newsList = null;
        try {
            int size = 4;
            if ((is_purchase != null)) {
                size++;
            }
            if (have_email != null) {
                size++;
            }

            long currentTime = Util.getGMTTime().getTime();
            BasicDBObject[] ands = new BasicDBObject[2];
//            BasicDBObject[] findObjs = new BasicDBObject[4];
            BasicDBObject[] findObjs = new BasicDBObject[size];
            BasicDBObject gte = new BasicDBObject("$gte", currentTime);
            BasicDBObject lte = new BasicDBObject("$lte", currentTime);
            ands[0] = new BasicDBObject(SettingdbKey.NEWS.to, gte);
            ands[1] = new BasicDBObject(SettingdbKey.NEWS.from, lte);
            findObjs[0] = new BasicDBObject("$and", ands);

            BasicDBObject[] ors1 = new BasicDBObject[2];
            ors1[0] = new BasicDBObject(SettingdbKey.NEWS.device_type, device_type);
            ors1[1] = new BasicDBObject(SettingdbKey.NEWS.device_type, new BasicDBObject("$exists", false));
            findObjs[1] = new BasicDBObject("$or", ors1);

            BasicDBObject[] ors2 = new BasicDBObject[2];
            ors2[0] = new BasicDBObject(SettingdbKey.NEWS.target_gender, gender);
            ors2[1] = new BasicDBObject(SettingdbKey.NEWS.target_gender, new BasicDBObject("$exists", false));
            findObjs[2] = new BasicDBObject("$or", ors2);

            //Linh 10/11/2016 #4883
            if (is_purchase != null) {
                BasicDBObject[] ors3 = new BasicDBObject[2];
                ors3[0] = new BasicDBObject(SettingdbKey.NEWS.IS_PURCHASED, is_purchase);
                ors3[1] = new BasicDBObject(SettingdbKey.NEWS.IS_PURCHASED, new BasicDBObject("$exists", true));
                findObjs[3] = new BasicDBObject("$or", ors3);
            }

            if (have_email != null) {
                BasicDBObject[] ors4 = new BasicDBObject[2];
                ors4[0] = new BasicDBObject(SettingdbKey.NEWS.haveEmail, have_email);
                ors4[1] = new BasicDBObject(SettingdbKey.NEWS.haveEmail, new BasicDBObject("$exists", true));
                findObjs[4] = new BasicDBObject("$or", ors4);
            }

            findObjs[size - 1] = new BasicDBObject(SettingdbKey.NEWS.is_shown, true);
            for (int i = 0; i < size; i++) {
                Util.addDebugLog("findObjs " + findObjs[i].toString());
            }

            BasicDBObject findObj = new BasicDBObject("$and", findObjs);
            DBObject orderBy = new BasicDBObject(SettingdbKey.NEWS.update_date, DES);
            DBCursor cursor = coll.find(findObj);
            Util.addDebugLog("findObj " + findObj.toString());

            cursor.sort(orderBy);
            if (cursor.size() != 0) {
                newsList = new ArrayList<>();
                while (cursor.hasNext()) {
                    Boolean isAdd = false;
                    
                    BasicDBObject currentObj = (BasicDBObject) cursor.next();
                    News news = new News();
                    news.setId(currentObj.get(SettingdbKey.NEWS.ID).toString());
                    news.setContent(currentObj.get(SettingdbKey.NEWS.content).toString());
                    news.setTitle(currentObj.get(SettingdbKey.NEWS.title).toString());
                    news.setFrom(DateFormat.format_yyyyMMddHHmm((long) currentObj.get(SettingdbKey.NEWS.from)));
                    news.setTo(DateFormat.format_yyyyMMddHHmm((long) currentObj.get(SettingdbKey.NEWS.to)));
                    //Linh 10/17/2016 #4959
                    if (currentObj.get(SettingdbKey.NEWS.registerType) != null)
                        news.setRegisterType((Long) currentObj.get(SettingdbKey.NEWS.registerType));
                    if (currentObj.get(SettingdbKey.NEWS.registerFromStr) != null)
                        news.setRegisterFrom(currentObj.get(SettingdbKey.NEWS.registerFromStr).toString());
                    if (currentObj.get(SettingdbKey.NEWS.registerToStr) != null)
                        news.setRegisterTo(currentObj.get(SettingdbKey.NEWS.registerToStr).toString());
                    if (currentObj.get(SettingdbKey.NEWS.userType) != null)
                        news.setUserType((Long) currentObj.get(SettingdbKey.NEWS.userType));
                    
                    if (currentObj.get(SettingdbKey.NEWS.banner_id) != null) {
                        news.setBannerId(currentObj.get(SettingdbKey.NEWS.banner_id).toString());
                    }
                    news.setIsPurchased((Boolean) currentObj.get(SettingdbKey.NEWS.IS_PURCHASED));
                    news.setHaveEmail((Boolean) currentObj.get(SettingdbKey.NEWS.haveEmail));
                    
                    if(news.getRegisterType() != null){
                        Long regType = news.getRegisterType();
                        Object regFrom = news.getRegisterFrom();
                        Object regTo = news.getRegisterTo();
                        isAdd = checkRegTime(reg_date, regType, regFrom, regTo);
                    }
                    else{
                        isAdd = true;
                    }
                    
                    if (isAdd){
                        if ((news.getUserType() == null)){
                            isAdd = true;
                        }
                        else if ( user_type.equals(news.getUserType()) ){
                            isAdd = true;
                        }
                        else {
                            isAdd = false;
                        }
                    }
                    
                    if (isAdd && (news.getIsPurchased() == null || is_purchase.equals(news.getIsPurchased())) && (news.getHaveEmail() == null || have_email.equals(news.getHaveEmail()) )) {
                        newsList.add(news);
                        
                    }
                }
            }
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new EazyException(ErrorCode.UNKNOWN_ERROR);
        }
        return newsList;
    }
    
    public static Boolean checkRegTime(Long user_reg_date, Long news_reg_type, Object news_reg_from, Object news_reg_to){
        boolean check = false;
        
        if (news_reg_type == 0){ // before
            Long newsRegDate = Long.valueOf((String) news_reg_from);
            if (user_reg_date < newsRegDate){
                check = true;
            }
        }
        
        if (news_reg_type == 1){ // after
            Long newsRegDate = Long.valueOf((String) news_reg_from);
            if (user_reg_date > newsRegDate){
                check = true;
            }
        }
        
        if (news_reg_type == 2){ // from-to
            Long newsRegDateFrom = Long.valueOf((String) news_reg_from);
            Long newsRegDateTo = Long.valueOf((String) news_reg_to);
            if ((newsRegDateFrom < user_reg_date) && (newsRegDateTo > user_reg_date)){
                check = true;
            }
        }
        
        return check;
    }
    
}
