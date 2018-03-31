/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.usermanagementserver.server.respond.impl.connection;

import com.vn.ntsc.usermanagementserver.server.respond.Respond;
import com.vn.ntsc.usermanagementserver.server.respond.IApiAdapter;
import com.vn.ntsc.usermanagementserver.dao.impl.LastTimeGetDAO;
import com.vn.ntsc.usermanagementserver.dao.impl.UserDAO;
import com.vn.ntsc.usermanagementserver.dao.impl.FavoritedDAO;
import com.vn.ntsc.usermanagementserver.dao.impl.OnlineAlertDAO;
import eazycommon.util.Util;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.ParamKey;
import eazycommon.exception.EazyException;
import org.json.simple.JSONObject;
import com.vn.ntsc.usermanagementserver.entity.impl.user.FollowingUser;
import com.vn.ntsc.usermanagementserver.entity.impl.user.User;
import com.vn.ntsc.usermanagementserver.server.respond.result.ListEntityRespond;
import com.vn.ntsc.usermanagementserver.server.tool.Tool;
import eazycommon.constant.Constant;

/**
 *
 * @author RuAc0n
 */
public class ListFavouritedApi implements IApiAdapter {

    @Override
    public Respond execute(JSONObject obj, Date time) {
        ListEntityRespond result = new ListEntityRespond();
        try {
            String userId = Util.getStringParam(obj, ParamKey.USER_ID);
            Long skip = Util.getLongParam(obj, ParamKey.SKIP);
            Long take = Util.getLongParam(obj, ParamKey.TAKE);
            //boolean isUnlock = UnlockDAO.IsFavoritedUnlock(userId);
            List<String> listFavoristed = new ArrayList<>();
            Map<String, FollowingUser> mapFavoristed = FavoritedDAO.getFavouritedMap(userId, listFavoristed);
            // remove block list
            listFavoristed.remove(userId);
            Tool.removeBlackList(listFavoristed, userId);
            // get sublist
//            List<String> listFriendId = new ArrayList<String>();

            int startIndex = skip.intValue();

//            if(startIndex < listFavoristed.size() ){
//                int endIndex = startIndex + take.intValue();
//                if (endIndex > listFavoristed.size()) {
//                    endIndex = listFavoristed.size();
//                }
//                listFriendId = listFavoristed.subList(startIndex, endIndex);
//            }
            // get friendDetail
            UserDAO.getListFollowingUser(listFavoristed, mapFavoristed);
            List<FollowingUser> friendList = new ArrayList<>();
            for (String id : listFavoristed) {
                friendList.add(mapFavoristed.get(id));
            }
            Collections.sort(friendList, new CompareByFvtTime());
//            Collections.sort(friendList, new FollowingUser().);
            List<FollowingUser> listFriend = new ArrayList<>();
            if(startIndex == 0 && take.intValue() == 0){
                listFriend = friendList;
            } else if (startIndex < friendList.size()) {
                int endIndex = startIndex + take.intValue();
                if (endIndex > friendList.size()) {
                    endIndex = friendList.size();
                }
                listFriend = friendList.subList(startIndex, endIndex);
            }
            for(FollowingUser user: listFriend){
                String friendId = user.userId;
                Long isAlert = (long) Constant.FLAG.OFF;
                boolean checkAlert = OnlineAlertDAO.checkAlert(friendId, userId);
                if (checkAlert) {
                    isAlert = new Long(Constant.FLAG.ON);
                }
                user.isAlert = isAlert.intValue();
            }
            LastTimeGetDAO.updateLastGetFavoristed(userId, time.getTime(), null);
            result = new ListEntityRespond(ErrorCode.SUCCESS, listFriend);

        } catch (EazyException ex) {
            Util.addErrorLog(ex);
            result.code = ex.getErrorCode();
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
        return result;
    }

    public static class CompareByFvtTime implements Comparator<FollowingUser> {

        @Override
        public int compare(FollowingUser o1, FollowingUser o2) {
            long io = o2.fvtTime - o1.fvtTime;
            if (io == 0) {
                return -1;
            } else if (io > 0) {
                return 1;
            } else {
                return -1;
            }
        }
    }

}
