/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.otherservice.servicemanagement.respond.impl;

import java.util.List;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.ParamKey;
import eazycommon.exception.EazyException;
import eazycommon.util.DateFormat;
import eazycommon.util.Util;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import com.vn.ntsc.Config;
import com.vn.ntsc.dao.impl.NewsDAO;
import com.vn.ntsc.dao.impl.UserDAO;
import com.vn.ntsc.eazyserver.adapter.impl.util.InterCommunicator;
import com.vn.ntsc.eazyserver.server.request.Request;
import com.vn.ntsc.eazyserver.server.session.Session;
import com.vn.ntsc.eazyserver.server.session.SessionManager;
import com.vn.ntsc.otherservice.entity.impl.News;
import com.vn.ntsc.otherservice.entity.impl.User;
import com.vn.ntsc.otherservice.servicemanagement.respond.IApiAdapter;
import com.vn.ntsc.otherservice.servicemanagement.respond.Respond;
import com.vn.ntsc.otherservice.servicemanagement.respond.result.EntityRespond;
import com.vn.ntsc.otherservice.servicemanagement.respond.result.ListEntityRespond;

/**
 *
 * @author HuyDX
 */
public class ListNewsApi implements IApiAdapter {

    @Override
    public Respond execute(Request request) {
        Respond response = new Respond();
        try {
            long device_type = Util.getLongParam(request.reqObj, "device_type");
            long gender = Util.getLongParam(request.reqObj, "gender");
            String token = Util.getStringParam(request.reqObj, "token");
            
//            //Linh 10/11/2016 #4883
            Session session = SessionManager.getSession(token);
            String userId = session.userID;
            User user = UserDAO.getUserInfor(userId);     
            
            Boolean isPurchase = checkUserIsPurchase(user);
            Boolean haveEmail = checkUserEmail(user);
            //Linh 10/17/2016 #4959
            Long userType = user.userType;
            String regDateStr = user.registerDate.substring(0, 8);
            Long regDate = Long.valueOf(regDateStr);
            
            //TRuyen ispurchaseO va haveEmailO vao la xong
            List<News> newsList = NewsDAO.list(device_type, gender, isPurchase, haveEmail, userType, regDate);
//            List<News> newsList = NewsDAO.list(device_type, gender);
            response = new ListEntityRespond(ErrorCode.SUCCESS, newsList);
        } catch (EazyException ex) {
            Util.addErrorLog(ex);
            response = new EntityRespond(ex.getErrorCode());
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
        
        return response;
    }
    
    public Boolean checkUserIsPurchase(User user){
        Boolean check = false;
        Boolean havePurchase = user.havePurchased;
        Boolean isPurchase = user.isPurchased;
        String firstTimePurchase = user.firstPurchaseTime;
        String lastTimePurchase = user.lastPurchaseTime;
        
        if (havePurchase != null || isPurchase != null || (firstTimePurchase != null && lastTimePurchase != null)){
            if (havePurchase != null){
                check = havePurchase;
            }
            else if (isPurchase != null){
                check = isPurchase;
            }
            else if ( firstTimePurchase != null && lastTimePurchase != null ){
                check = true;
            }
        }
        return check;
    }
    
    public Boolean checkUserEmail(User user){
        Boolean check = false;
        String email = user.email;
        
        if (email.contains("@")){
            check = true;
        }
                
        return check;
    }
    
}
