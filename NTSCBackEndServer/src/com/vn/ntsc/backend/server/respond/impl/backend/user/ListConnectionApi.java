/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.backend.server.respond.impl.backend.user;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.json.simple.JSONObject;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.ParamKey;
import eazycommon.exception.EazyException;
import com.vn.ntsc.backend.dao.user.FavoristDAO;
import com.vn.ntsc.backend.dao.user.FavoritedDAO;
import com.vn.ntsc.backend.dao.user.BlockDAO;
import com.vn.ntsc.backend.dao.user.UserDAO;
import com.vn.ntsc.backend.entity.IEntity;
import com.vn.ntsc.backend.entity.impl.user.ListSimpleUserData;
import com.vn.ntsc.backend.server.respond.IApiAdapter;
import com.vn.ntsc.backend.server.respond.Respond;
import com.vn.ntsc.backend.server.respond.result.EntityRespond;
import eazycommon.util.Util;

/**
 *
 * @author RuAc0n
 */
public class ListConnectionApi implements IApiAdapter {

    @Override
    public Respond execute(JSONObject obj) {
        EntityRespond respond = new EntityRespond();
        try {
            String userId = Util.getStringParam(obj, ParamKey.ID);
            Long type = Util.getLongParam(obj, ParamKey.TYPE);
            List<String> listFriendId = new ArrayList<String>();
            if (type == 2) {
                listFriendId = FavoristDAO.getFavouristList(userId);
            } else if (type == 3) {
                listFriendId = FavoritedDAO.getFavouritedList(userId);
            }
            List<String> blackList = BlockDAO.getBlackList(userId);
            blackList.addAll(UserDAO.getBlackList());
            removeBlockUser(listFriendId, blackList);
            Map<String, IEntity> m = UserDAO.getMapLogInfor(listFriendId);
            ListSimpleUserData data = new ListSimpleUserData(m);
            respond = new EntityRespond(ErrorCode.SUCCESS, data);
        } catch (EazyException ex) {
            Util.addErrorLog(ex);
            respond = new EntityRespond(ex.getErrorCode());
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
        return respond;
    }

    public void removeBlockUser(List<String> list, List<String> blackList) {
        for (int i = 0; i < blackList.size(); i++) {
            String id = blackList.get(i);
            if (list.contains(id)) {
                list.remove(id);
            }
        }
    }
}
