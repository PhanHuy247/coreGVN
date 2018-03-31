/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.usermanagementserver.server.respond.impl.connection;

import com.vn.ntsc.usermanagementserver.Config;
import com.vn.ntsc.usermanagementserver.dao.impl.FavoristDAO;
import com.vn.ntsc.usermanagementserver.dao.impl.ImageStfDAO;
import com.vn.ntsc.usermanagementserver.server.respond.Respond;
import com.vn.ntsc.usermanagementserver.server.respond.IApiAdapter;
import eazycommon.util.Util;
import java.util.Date;
import java.util.List;
import eazycommon.constant.ErrorCode;
import eazycommon.exception.EazyException;
import org.json.simple.JSONObject;
import eazycommon.constant.ParamKey;
import com.vn.ntsc.usermanagementserver.dao.impl.OnlineAlertDAO;
import com.vn.ntsc.usermanagementserver.dao.impl.UserDAO;
import com.vn.ntsc.usermanagementserver.entity.impl.user.User;
import com.vn.ntsc.usermanagementserver.server.InterCommunicator;
import com.vn.ntsc.usermanagementserver.server.respond.result.ListEntityRespond;
import com.vn.ntsc.usermanagementserver.server.tool.Tool;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author RuAc0n
 */
public class ListOnlineAlertApi implements IApiAdapter {

    @Override
    public Respond execute(JSONObject obj, Date time) {
        ListEntityRespond result = new ListEntityRespond();
        try {
            String userId = Util.getStringParam(obj, ParamKey.USER_ID);

            //1. get List Friend id
            List<String> listFriendId = OnlineAlertDAO.getAlertList(userId);
            //2. get List Friend detail

            List<User> friendList = UserDAO.getListUser(listFriendId);
            
            Tool.removeBlackListUser(friendList, userId);  
            if(friendList != null)
                for(int i = 0; i < friendList.size(); i++){
                    friendList.get(i).isFav = FavoristDAO.checkFavourist(userId, friendList.get(i).userId);
                }
            result = new ListEntityRespond(ErrorCode.SUCCESS, friendList);
        } catch (EazyException ex) {
            Util.addErrorLog(ex);
            result.code = ex.getErrorCode();
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
        return result;
    }

}
