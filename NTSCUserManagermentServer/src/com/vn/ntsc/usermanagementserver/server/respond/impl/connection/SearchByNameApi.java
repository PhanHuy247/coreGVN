/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.usermanagementserver.server.respond.impl.connection;

import com.vn.ntsc.usermanagementserver.dao.impl.OnlineAlertDAO;
import com.vn.ntsc.usermanagementserver.server.respond.Respond;
import com.vn.ntsc.usermanagementserver.server.respond.IApiAdapter;
import com.vn.ntsc.usermanagementserver.dao.impl.UserDAO;
import eazycommon.util.Util;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import eazycommon.constant.ErrorCode;
import eazycommon.exception.EazyException;
import org.json.simple.JSONObject;
import eazycommon.constant.ParamKey;
import com.vn.ntsc.usermanagementserver.entity.impl.user.User;
import com.vn.ntsc.usermanagementserver.server.respond.result.ListEntityRespond;
import com.vn.ntsc.usermanagementserver.server.tool.Tool;
import com.vn.ntsc.usermanagementserver.server.userinformanager.UserInforManager;
import eazycommon.constant.Constant;

/**
 *
 * @author RuAc0n
 */
public class SearchByNameApi implements IApiAdapter {

    @Override
    public Respond execute(JSONObject obj, Date time) {
        ListEntityRespond result = new ListEntityRespond();
        try {
            String userId = Util.getStringParam(obj, ParamKey.USER_ID);
            long skip = Util.getLongParam(obj, ParamKey.SKIP);
            long take = Util.getLongParam(obj, ParamKey.TAKE);
            String name = Util.getStringParam(obj, ParamKey.USER_NAME);

            List<User> friendList = UserDAO.searchByName(name.toLowerCase());
            if (userId != null && !userId.isEmpty()){
                Tool.removeUser(friendList, userId);
            }
            List<User> respondList = new ArrayList<>();
            if (skip < friendList.size()) {
                long startIndex = skip;
                long endIndex = startIndex + take;
                if (endIndex > friendList.size()) {
                    endIndex = friendList.size();
                }
                if (skip == 0 && take == 0) {
                    respondList = friendList;
                } else {
                    respondList = friendList.subList((int) startIndex, (int) endIndex);
                }
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

}
