/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.usermanagementserver.server.respond.impl.connection;

import com.vn.ntsc.usermanagementserver.server.respond.Respond;
import com.vn.ntsc.usermanagementserver.server.respond.IApiAdapter;
import com.vn.ntsc.usermanagementserver.dao.impl.MyFootPrintDAO;
import com.vn.ntsc.usermanagementserver.dao.impl.UserDAO;
import eazycommon.util.Util;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import eazycommon.constant.ErrorCode;
import eazycommon.exception.EazyException;
import org.json.simple.JSONObject;
import eazycommon.constant.ParamKey;
import com.vn.ntsc.usermanagementserver.entity.impl.user.FollowingUser;
import com.vn.ntsc.usermanagementserver.server.respond.result.ListEntityRespond;
import com.vn.ntsc.usermanagementserver.server.tool.Tool;

/**
 *
 * @author RuAc0n
 */
public class ListMyFootprintApi implements IApiAdapter {

    @Override
    public Respond execute(JSONObject obj, Date time) {
        ListEntityRespond result = new ListEntityRespond();
        try {
            String userId = Util.getStringParam(obj, ParamKey.USER_ID);
            long skip = Util.getLongParam(obj, ParamKey.SKIP);
            long take = Util.getLongParam(obj, ParamKey.TAKE);
            // boolean isUnlock = UnlockDAO.IsCheckOutUnlock(userId);
            // get map checkoutid, time
            // get list user checkout
            List<String> listCheckout = new ArrayList<>();
            Map<String, FollowingUser> mapCheckout = MyFootPrintDAO.getMyFootprintsList(userId, listCheckout);
//            List<User> listCheckout = UserDAO.getMapCheckOutUser(mapCheckoutId);
            // remove blockUser
            Tool.removeBlackList(listCheckout, userId);
            // get sublist
//            List<String> listFriendId = new ArrayList<String>();
//            if (skip < listCheckout.size()) {
//                long startIndex = skip;
//                long endIndex = startIndex + take;
//                if (endIndex > listCheckout.size()) {
//                    endIndex = listCheckout.size();
//                }
//                listFriendId = listCheckout.subList((int) startIndex, (int) endIndex);
////                UserActivityDAO.getListStatus(respondList);
//            }
            // get detailUser
            UserDAO.getListFollowingUser(listCheckout, mapCheckout);
            List<FollowingUser> listRespond = new ArrayList<>();
            for (String id : listCheckout) {
                listRespond.add(mapCheckout.get(id));
            }
            Collections.sort(listRespond, new CompareByCheckoutTime());
            if (skip < listRespond.size()) {
                long startIndex = skip;
                long endIndex = startIndex + take;
                if (endIndex > listRespond.size()) {
                    endIndex = listRespond.size();
                }
                listRespond = listRespond.subList((int) startIndex, (int) endIndex);
//                UserActivityDAO.getListStatus(respondList);
            }
            // update last time get list checkout
//            LastTimeGetDAO.updateLastGetCheckOut(userId, time.getTime(), null);
            result = new ListEntityRespond(ErrorCode.SUCCESS, listRespond);
//            }
        } catch (EazyException ex) {
            Util.addErrorLog(ex);
            result.code = ex.getErrorCode();
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
        return result;
    }

    public static class CompareByCheckoutTime implements Comparator<FollowingUser> {

        public int compare(FollowingUser o1, FollowingUser o2) {
            long io = o2.checkoutTime - o1.checkoutTime;
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
