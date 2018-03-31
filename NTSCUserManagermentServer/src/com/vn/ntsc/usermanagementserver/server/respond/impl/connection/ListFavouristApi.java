/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.usermanagementserver.server.respond.impl.connection;

import com.vn.ntsc.usermanagementserver.server.respond.Respond;
import com.vn.ntsc.usermanagementserver.server.respond.IApiAdapter;
import com.vn.ntsc.usermanagementserver.dao.impl.UserDAO;
import com.vn.ntsc.usermanagementserver.dao.impl.FavoristDAO;
import com.vn.ntsc.usermanagementserver.dao.impl.OnlineAlertDAO;
import eazycommon.util.Util;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import eazycommon.constant.ErrorCode;
import eazycommon.exception.EazyException;
import org.json.simple.JSONObject;
import eazycommon.constant.ParamKey;
import java.util.Collections;
import java.util.Comparator;
import com.vn.ntsc.usermanagementserver.entity.impl.user.User;
import com.vn.ntsc.usermanagementserver.server.respond.result.ListEntityRespond;
import com.vn.ntsc.usermanagementserver.server.tool.Tool;
import eazycommon.constant.Constant;

/**
 *
 * @author RuAc0n
 */
public class ListFavouristApi implements IApiAdapter {

    @Override
    public Respond execute(JSONObject obj, Date time) {
        ListEntityRespond result = new ListEntityRespond();
        try {
            String userId = Util.getStringParam(obj, ParamKey.USER_ID);
            Long skip = Util.getLongParam(obj, ParamKey.SKIP);
            Long take = Util.getLongParam(obj, ParamKey.TAKE);
            //1. get List Friend id
//            List<String> blackList = Tool.getBlackList(userId);
            List<String> listFriendId = FavoristDAO.getFavouristList(userId);
            listFriendId.remove(userId);
            Tool.removeBlackList(listFriendId, userId);
            //2. get List Friend detail
            List<User> friendList = UserDAO.getListUser(listFriendId);
            
            // LongLT 21 Oct2016 ///////////////////////////  #4889 START
            Collections.sort(friendList, new Comparator<User>() {
                @Override
                public int compare(User o1, User o2) {
                    if(o1 == null || o2 == null){
                        return 0;
                    }
                    long o2LastLoginTime = parseLongValue(o2.lastLoginTime, 0);
                    long o1LastLoginTime = parseLongValue(o1.lastLoginTime, 0);
                    
                    long io = o2LastLoginTime - o1LastLoginTime;
                    if (io == 0) {
                        return -1;
                    } else if (io > 0) {
                        return 1;
                    } else {
                        return -1;
                    }
                }
            });
            // LongLT /////////////////////////// #4889 END 
            
            List<User> respondList = new ArrayList<>();
            if ((skip == null && take == null) || (skip == 0 && take == 0)) {
                respondList = friendList;
            }
            else if (skip != null && skip < friendList.size()) {
                long startIndex = skip;
                long endIndex = startIndex + take;
                if (endIndex > friendList.size()) {
                    endIndex = friendList.size();
                }
                respondList = friendList.subList((int) startIndex, (int) endIndex);
//                UserActivityDAO.getListStatus(respondList);
            }
            for(User user: respondList){
                String friendId = user.userId;
                Long isAlert = (long) Constant.FLAG.OFF;
                boolean checkAlert = OnlineAlertDAO.checkAlert(friendId, userId);
                if (checkAlert) {
                    isAlert = new Long(Constant.FLAG.ON);
                }
                user.isAlert = isAlert.intValue();
            }
            result = new ListEntityRespond(ErrorCode.SUCCESS, respondList);
        } catch (EazyException ex) {
            Util.addErrorLog(ex);
            result.code = ex.getErrorCode();
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
        return result;
    }
    // LongLT 21 Oct2016 ///////////////////////////  #4889 
    private long parseLongValue(String value, long defaultValue){
        long returnValue = defaultValue;
        try {
            returnValue = Long.parseLong(value);
        } catch (Exception e) {
            Util.addErrorLog(e);
        }
        return returnValue;
    }

}
