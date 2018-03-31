/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.usermanagementserver.server.respond.impl.connection;

import com.vn.ntsc.usermanagementserver.server.respond.Respond;
import com.vn.ntsc.usermanagementserver.server.respond.IApiAdapter;
import com.vn.ntsc.usermanagementserver.dao.impl.UserInteractionDAO;
import com.vn.ntsc.usermanagementserver.dao.impl.FavoritedDAO;
import com.vn.ntsc.usermanagementserver.dao.impl.FavoristDAO;
import com.vn.ntsc.usermanagementserver.dao.impl.UserDAO;
import eazycommon.util.Util;
import java.util.Date;
import eazycommon.constant.Constant;
import eazycommon.constant.ErrorCode;
import eazycommon.exception.EazyException;
import org.json.simple.JSONObject;
import eazycommon.constant.ParamKey;
import com.vn.ntsc.usermanagementserver.dao.impl.log.LogFavouristDAO;
import com.vn.ntsc.usermanagementserver.entity.impl.datarespond.AddFavouristData;
import com.vn.ntsc.usermanagementserver.server.blacklist.BlockUserManager;
import com.vn.ntsc.usermanagementserver.server.respond.result.EntityRespond;

/**
 *
 * @author RuAc0n
 */
public class AddFavouristApi implements IApiAdapter {

    @Override
    public Respond execute(JSONObject obj, Date time) {
        EntityRespond result = new EntityRespond();
        try {
            String favourist = Util.getStringParam(obj, ParamKey.REQUEST_USER_ID);
            String userId = Util.getStringParam(obj, ParamKey.USER_ID);
            //1. check Friend
            boolean checker = UserDAO.checkUser(favourist);
            if (checker  && !favourist.equals(userId)) {
                if (!BlockUserManager.isBlock(userId, favourist)) {
                    //2. check favourist
                    Long isFavourist = FavoristDAO.checkFavourist(userId, favourist);
                    AddFavouristData data = null;
                    if (isFavourist == Constant.FLAG.OFF) {
                        //3. update collection user
                        UserDAO.addFavorist(userId);
                        //4.add favourist to user
                        FavoristDAO.addFavorist(userId, favourist);
                        //5. update document user of friend
                        UserDAO.addFavorited(favourist);
                        //6. add favourited to friend
                        FavoritedDAO.addFavourited(favourist, userId, time.getTime());

                        // add log favourist
                        String ip = Util.getStringParam(obj, ParamKey.IP);
                        LogFavouristDAO.addLog(userId, favourist, time, Constant.FLAG.ON, ip);
                        // add notification
//                        Long isUnlock = (long) Constant.YES;
//                        int price = PriceGender.getPrice(favourist, ActionType.who_favorited_me);
//                        if (price == 0 || UnlockDAO.IsFavoritedUnlock(favourist)) {
//                            isUnlock = new Long(Constant.YES);
//                        }

//                        if (NotificationSettingDAO.checkUserNotification(favourist, Constant.FAVORITED_UNLOCK_NOTI)) {
//                            Notification noti = new Notification();
//                            noti.notiUserId = userId;
//                            noti.notiUserName = UserDAO.getUserName(userId);
//                            NotificationDAO.addNotification(favourist, noti, time.getTime(), Constant.FAVORITED_UNLOCK_NOTI);
//                            data = new AddFavouristData(isUnlock, Constant.YES);
//                        } 
                        
                        //add interaction
                        if (!favourist.equalsIgnoreCase(userId)) {
                            addInteraction(favourist, userId);
                        }
                    }
                    //7. set data return
                    result = new EntityRespond(ErrorCode.SUCCESS, data);
                } else {
                    result.code = ErrorCode.BLOCK_USER;
                }
            } else {
                result.code = ErrorCode.USER_NOT_EXIST;
            }
        } catch (EazyException ex) {
            Util.addErrorLog(ex);
            result.code = ex.getErrorCode();
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
        return result;
    }
    
    public static void addInteraction(String userId, String friendId) {

        boolean isExistUserInteractionList = UserInteractionDAO.checkExistInteractionList(userId);
        boolean isExistUserInteraction = UserInteractionDAO.checkExistInteraction(userId, friendId);

        boolean isExistFriendInteractionList = UserInteractionDAO.checkExistInteractionList(friendId);
        boolean isExistFriendInteraction = UserInteractionDAO.checkExistInteraction(friendId, userId);

        if (isExistUserInteractionList && !isExistUserInteraction) {
            UserInteractionDAO.updateInteraction(userId, friendId);
        } else if (!isExistUserInteractionList) {
            UserInteractionDAO.addInteraction(userId, friendId);
        }

        if (isExistFriendInteractionList && !isExistFriendInteraction) {
            UserInteractionDAO.updateInteraction(friendId, userId);
        } else if (!isExistFriendInteractionList) {
            UserInteractionDAO.addInteraction(friendId, userId);
        }
    }

}
