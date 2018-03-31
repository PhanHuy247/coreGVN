/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.backend.server.respond.impl.backend.news;

import java.util.Map;
import java.util.TreeMap;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.mongokey.SettingdbKey;
import eazycommon.exception.EazyException;
import eazycommon.util.DateFormat;
import eazycommon.util.Util;
import org.json.simple.JSONObject;
import com.vn.ntsc.backend.dao.news.NewsDAO;
import com.vn.ntsc.backend.entity.impl.datarespond.InsertData;
import com.vn.ntsc.backend.entity.impl.news.News;
import com.vn.ntsc.backend.server.common.Validator;
import com.vn.ntsc.backend.server.respond.IApiAdapter;
import com.vn.ntsc.backend.server.respond.Respond;
import com.vn.ntsc.backend.server.respond.result.EntityRespond;

/**
 *
 * @author HuyDX
 */
public class InsertNewsApi implements IApiAdapter{

    private static final Map<String, Integer> keys = new TreeMap<>();

    static {
        keys.put("title", 4);
        keys.put("content", 5);
        keys.put("from", 6);
        keys.put("to", 7);
    }
    
    @Override
    public Respond execute(JSONObject obj) {
        EntityRespond response = new EntityRespond();
        try {    
            int error = Validator.validate(obj, keys);
            if (error != ErrorCode.SUCCESS) {
                response = new EntityRespond(error);
                return response;
            }
            String title = Util.getStringParam(obj, "title");
            String banner_id = Util.getStringParam(obj, "banner_id");
            String content = Util.getStringParam(obj, "content");                    
            Long created_date = Util.currentTime();
            String fromStr = Util.getStringParam(obj, "from");
            Long from = null;
            if (fromStr!=null){
                from = DateFormat.parse_yyyyMMddHHmm(fromStr).getTime();
            }
            String toStr = Util.getStringParam(obj, "to");
            Long to = null;
            if (toStr!=null){
                to = DateFormat.parse_yyyyMMddHHmm(toStr).getTime();
            }            
            if (from==null || (from>=to)){
                response = new EntityRespond(ErrorCode.UNUSABLE_VERSION_APPLICATION);
                return response;
            }
            
            if (to==null || (to<created_date)){
                response = new EntityRespond(ErrorCode.CHANGE_SETTING_TOKEN);
                return response;
            }
            Boolean isPurchase = (Boolean) obj.get(SettingdbKey.NEWS.IS_PURCHASED);
            Boolean haveEmail = (Boolean) obj.get(SettingdbKey.NEWS.haveEmail); 
            
            //Linh 10/5/2016 
            String regFrom = Util.getStringParam(obj, "register_from");
//            Long registerFrom = null;
//            if (regFrom != null){
//                registerFrom = DateFormat.parse_yyyyMMddHHmm(regFrom).getTime();
//            }
            
            String regTo = Util.getStringParam(obj, "register_to");
//            Long registerTo = null;
//            if (regTo != null){
//                registerTo = DateFormat.parse_yyyyMMddHHmm(regTo).getTime();
//            }
            Long registerType = Util.getLongParam(obj, "register_type");
            if (registerType != null) {
                if (registerType == 2 && Long.valueOf(regTo) <= Long.valueOf(regFrom)) {
                    response = new EntityRespond(ErrorCode.UNUSABLE_VERSION_APPLICATION);
                    return response;
                }
            }        
            Long userType = Util.getLongParam(obj, "user_type");
            
            Boolean is_shown = false;
            Long device_type = Util.getLongParam(obj, "device_type");   
            Long targetGender = Util.getLongParam(obj, "target_gender");   
            News news = new News();
            news.setBannerId(banner_id);
            news.setContent(content);
            news.setUpdateDate(created_date);
            
            news.setIsPurchased(isPurchase);
            news.setHaveEmail(haveEmail);
            
            if(registerType != null) news.setRegisterType(registerType);
            
            news.setFrom(from);
            news.setIsShown(is_shown);
//            if(regFrom != null)  news.setRegisterFrom(registerFrom);
//            if(regTo != null)  news.setRegisterTo(registerTo);
            if(regFrom != null)  news.setRegisterFromStr(regFrom);
            if(regTo != null)  news.setRegisterToStr(regTo);
            if (device_type != null)  news.setDevice_type(device_type.intValue());
            if (userType != null)  news.setUserType(userType);
            
            news.setTitle(title);
            news.setTo(to);
            if (targetGender!=null)
                news.setTargetGender(targetGender.intValue());
            String id = NewsDAO.insert(news);
            response = new EntityRespond(ErrorCode.SUCCESS, new InsertData(id));
        } catch (EazyException ex) {
            Util.addErrorLog(ex);
            response = new EntityRespond(ex.getErrorCode());
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
        return response;
    }
}
