/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.usermanagementserver.server.respond.impl.connection;

import com.vn.ntsc.usermanagementserver.dao.impl.FavoristDAO;
import com.vn.ntsc.usermanagementserver.dao.impl.FavoritedDAO;
import com.vn.ntsc.usermanagementserver.dao.impl.UserDAO;
import com.vn.ntsc.usermanagementserver.entity.impl.user.User;
import com.vn.ntsc.usermanagementserver.server.respond.IApiAdapter;
import com.vn.ntsc.usermanagementserver.server.respond.Respond;
import com.vn.ntsc.usermanagementserver.server.respond.result.ListEntityRespond;
import com.vn.ntsc.usermanagementserver.server.tool.Tool;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.ParamKey;
import eazycommon.exception.EazyException;
import eazycommon.util.Util;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.simple.JSONObject;

/**
 *
 * @author Phan Huy
 */
public class ListFriendApi implements IApiAdapter {

    @Override
    public Respond execute(JSONObject obj, Date time) {
        ListEntityRespond result = new ListEntityRespond();
        try {
            
            String userId = Util.getStringParam(obj, ParamKey.USER_ID);
            Long skip = Util.getLongParam(obj, ParamKey.SKIP);
            Long take = Util.getLongParam(obj, ParamKey.TAKE);
            
            List<String> listFriendId = new ArrayList<>();
            
            List<String> listFavouristId = FavoristDAO.getFavouristList(userId);
            List<String> listFavouritedId = FavoritedDAO.getFavoristIdList(userId);
            listFriendId.addAll(listFavouristId);
            listFriendId.addAll(listFavouritedId);
            
            listFriendId.remove(userId);
            Tool.removeBlackList(listFriendId, userId);
            
            List<User> friendList = UserDAO.getListUser(listFriendId);
            
             List<User> respondList = new ArrayList<>();
            if (skip != null && skip < friendList.size()) {
                long startIndex = skip;
                long endIndex = startIndex + take;
                if (endIndex > friendList.size()) {
                    endIndex = friendList.size();
                }
                respondList = friendList.subList((int) startIndex, (int) endIndex);
            }
            else if (skip == null && take == null) {
                respondList = friendList;
            }
            result = new ListEntityRespond(ErrorCode.SUCCESS, respondList);
        } catch (EazyException ex) {
            Util.addErrorLog(ex);
            result.code = ex.getErrorCode();
        }
        return result;
    }

}
