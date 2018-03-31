/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.otherservice.servicemanagement.respond.impl;

import com.vn.ntsc.Config;
import com.vn.ntsc.dao.impl.StickerCategoryDAO;
import com.vn.ntsc.dao.impl.UserStickerDAO;
import com.vn.ntsc.eazyserver.adapter.impl.util.InterCommunicator;
import com.vn.ntsc.eazyserver.server.request.Request;
import com.vn.ntsc.otherservice.entity.impl.GetStickerShopInforData;
import com.vn.ntsc.otherservice.servicemanagement.respond.IApiAdapter;
import com.vn.ntsc.otherservice.servicemanagement.respond.Respond;
import com.vn.ntsc.otherservice.servicemanagement.respond.result.EntityRespond;
import eazycommon.constant.API;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.ParamKey;
import eazycommon.util.Util;
import java.util.List;
import org.json.simple.JSONObject;

/**
 *
 * @author Phan Huy
 */
public class TotalBadgeApi implements IApiAdapter{

    @Override
    public Respond execute(Request request) {
        EntityRespond result = new EntityRespond();
         try {
            JSONObject jsonObject = request.reqObj;
            String userId = Util.getStringParam(request.reqObj, ParamKey.USER_ID);
            jsonObject.put(ParamKey.API_NAME, API.GET_MY_PAGE_INFOR);
                    jsonObject.put(ParamKey.USER_ID, userId);
                    String umsResult = InterCommunicator.sendRequest(request.toString(), Config.UMSServerIP, Config.UMSPort);
                    Util.addDebugLog("========== umsResult: "+umsResult);
                    JSONObject unreadNotiObj = Util.toJSONObject(umsResult);
                    JSONObject unreadNoti = (JSONObject) unreadNotiObj.get("data");
                    long notiLikeNum = (Long) unreadNoti.get("noti_like_num");
                    long notiNum = (Long) unreadNoti.get("noti_num");
                    long notiQANum = (Long) unreadNoti.get("noti_qa_num");
                    long notiNewsNum = (Long) unreadNoti.get("noti_news_num");
    //                jsonObject.put(ParamKey.API_NAME, API.TOTAL_UNREAD);
    //                String chatResult = InterCommunicator.sendRequest(request.toString(), Config.ChatServerIP, Config.ChatServerPort);
    //                Util.addDebugLog("========== chatResult: "+chatResult);
    //                int unreadMessage = new Integer(chatResult);

                    long badge = notiLikeNum + notiNum + notiQANum + notiNewsNum ;
                    GetTotalBadgeApi data = new GetTotalBadgeApi(badge);
                    Util.addDebugLog("========== total badge: "+badge);
            result = new EntityRespond(ErrorCode.SUCCESS, data);
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
        return result;
    }
    
}
