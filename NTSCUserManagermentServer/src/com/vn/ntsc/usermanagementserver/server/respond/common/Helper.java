/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.usermanagementserver.server.respond.common;

import com.vn.ntsc.usermanagementserver.server.pointaction.ActionManager;
import com.vn.ntsc.usermanagementserver.server.pointaction.ActionType;
import com.vn.ntsc.usermanagementserver.server.pointaction.ConnectionPrice;
import com.vn.ntsc.usermanagementserver.server.pointaction.PriceGender;
import com.vn.ntsc.usermanagementserver.dao.impl.UserActivityDAO;
import com.vn.ntsc.usermanagementserver.dao.impl.LoginTrackingDAO;
import com.vn.ntsc.usermanagementserver.dao.impl.FavoritedDAO;
import com.vn.ntsc.usermanagementserver.dao.impl.LastTimeGetDAO;
import com.vn.ntsc.usermanagementserver.dao.impl.NotificationDAO;
import com.vn.ntsc.usermanagementserver.dao.impl.UserDAO;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;
import eazycommon.constant.Constant;
import eazycommon.exception.EazyException;
import eazycommon.util.Util;
import com.vn.ntsc.usermanagementserver.dao.impl.log.LogLoginDAO;
import com.vn.ntsc.usermanagementserver.entity.impl.datarespond.GetAttentionNumberData;
import com.vn.ntsc.usermanagementserver.entity.impl.datarespond.LoginData;
import com.vn.ntsc.usermanagementserver.entity.impl.datarespond.MyPageData;
import com.vn.ntsc.usermanagementserver.entity.impl.image.Image;
import com.vn.ntsc.usermanagementserver.entity.impl.notification.Notification;
import com.vn.ntsc.usermanagementserver.entity.impl.setting.UserSetting;
import com.vn.ntsc.usermanagementserver.entity.impl.user.User;
import com.vn.ntsc.usermanagementserver.server.blacklist.BlockUserManager;
import com.vn.ntsc.usermanagementserver.server.notificationpool.NotificationCleaner;
import com.vn.ntsc.usermanagementserver.server.tool.Tool;
import com.vn.ntsc.usermanagementserver.server.userinformanager.CheckoutManager;
import com.vn.ntsc.usermanagementserver.server.userinformanager.MyFootPrintManager;
import com.vn.ntsc.usermanagementserver.server.userinformanager.UserInforManager;
import com.vn.ntsc.usermanagementserver.setting.Setting;

/**
 *
 * @author RuAc0n
 */
public class Helper {

    public static void checkDailyBonus(String userId, Date loginDate, Date time, User user, LoginData data, Date lastLogin, String ip) throws EazyException {
        int price = 0;
        if (lastLogin == null) {
            Util.addDebugLog("Last login null: userId : " + userId + " date : " + loginDate.toString() + " now : " + time.toString());
            int curPoint = UserInforManager.getPoint(userId);
            if(data.isVerify){
                ActionManager.doAction(ActionType.daily_bonus, userId, null, time, null, null, ip);
                LoginTrackingDAO.increaseCount(userId);
            }
            price = UserInforManager.getPoint(userId) - curPoint;
            user.point = user.point + price;

            // add notification
        } else {
            if (loginDate.after(lastLogin) && (loginDate.getDate() != lastLogin.getDate() || loginDate.getMonth() != lastLogin.getMonth() || loginDate.getYear() != lastLogin.getYear())) {
                Util.addDebugLog("Last login not null: userId : " + userId + " loginDate : " + loginDate.toString() + " last login : " + lastLogin.toString() + " now :" + time.toString());
                int curPoint = UserInforManager.getPoint(userId);
                if(data.isVerify){
                    ActionManager.doAction(ActionType.daily_bonus, userId, null, time, null, null, ip);
                    LoginTrackingDAO.increaseCount(userId);
                }
                price = UserInforManager.getPoint(userId) - curPoint;
                user.point = user.point + price;
                // add notification
            }
        }
        if(price > 0){
//            if (NotificationSettingDAO.checkUserNotification(userId, Constant.DAILY_BONUS_NOTI)) {
            Notification noti = new Notification();
            noti.point = price;
            String notificationId = NotificationDAO.addNotification(userId, noti, time.getTime(), Constant.NOTIFICATION_TYPE_VALUE.DAILY_BONUS_NOTI);
            NotificationCleaner.put(notificationId, time.getTime());
            data.isNoti = new Long(Constant.FLAG.ON);
            data.addPoint = price;
//            }
        }
    }

    public static UserSetting setSetting(String userId) {
        UserSetting setting = new UserSetting();
//        setting.saveImagePoint = PriceGender.getPrice(userId, ActionType.save_image);
        setting.saveImagePoint = 0 - ConnectionPrice.getBadConnectionPrice(String.valueOf(ActionType.save_image), userId).senderPrice;
//        setting.commentBuzzPoint = PriceGender.getPrice(userId, ActionType.comment_buzz);
        setting.commentBuzzPoint = 0 - ConnectionPrice.getBadConnectionPrice(String.valueOf(ActionType.comment_buzz), userId).senderPrice;
//        setting.chatPoint = PriceGender.getPrice(userId, ActionType.chat);
//        setting.chatPoint = ConnectionPrice.getBadConnectionPrice(String.valueOf(ActionType.chat), userId).senderPrice;
        setting.onlineAlertPoint = PriceGender.getPrice(userId, ActionType.online_alert);
        setting.daylyBonus = PriceGender.getPrice(userId, ActionType.daily_bonus);
        setting.regPoint = PriceGender.getPrice(userId, ActionType.register);
//        setting.winkPoint = PriceGender.getPrice(userId, ActionType.wink);
        setting.backstageTime = Setting.BACKSTAGE_TIME;
//        setting.bckstg_pri = PriceGender.getPrice(userId, ActionType.unlock_backstage);
        setting.bckstg_pri = 0 - ConnectionPrice.getBadConnectionPrice(String.valueOf(ActionType.unlock_backstage), userId).senderPrice;
//        setting.backstageBonus = PriceGender.getUnlockBackStageBonus(userId);
        setting.backstageBonus = ConnectionPrice.getBacstageBonusPrice(userId).receiverPrice;
        setting.near = Setting.DISTANCE.get(0);
        setting.city = Setting.DISTANCE.get(1);
        setting.country = Setting.DISTANCE.get(2);

        return setting;
    }

    public static void setNotificationNumber(String userId, List<String> blockUsers, LoginData data) throws EazyException {
        Long readTime = UserDAO.getNotificationReadTime(userId);
        int notiNum = NotificationDAO.getNotificationNumber(userId, readTime, blockUsers);
        data.notiNum = notiNum;
    }

    public static boolean isVerify(User respondUser) throws EazyException {
        if (respondUser.finishRegisterFlag == Constant.FLAG.OFF) {
            return false;
        }
        return true;
//        if (respondUser instanceof Female) {
//            if (((Female) respondUser).verificationFlag != Constant.APPROVED) {
//                data.isVerify = false;
//            } else {
//                data.isVerify = true;
//            }
//        } else {
//            data.isVerify = true;
//        }

    }

    public static int getNotificationNumber(String userId) throws EazyException {
        List<String> blockUser = BlockUserManager.getBlackList(userId);
//        Collection<String> deactiveUsers = BlackListManager.toList();
        Long readTime = UserDAO.getNotificationReadTime(userId);
        return NotificationDAO.getNotificationNumber(userId, readTime, blockUser);
    }
    
    public static int getNotificationSeenNumber(String userId) throws EazyException {
        List<String> blockUser = BlockUserManager.getBlackList(userId);
//        Collection<String> deactiveUsers = BlackListManager.toList();
        Long readTime = UserDAO.getNotificationReadTime(userId);
        return NotificationDAO.getNotificationSeenNumber(userId, readTime, blockUser);
    }

    public static int getFavoristedNumber(String userId) throws EazyException {
        List<String> listFvt = FavoritedDAO.getFavoristIdList(userId);
        listFvt.remove(userId);
        Tool.removeBlackList(listFvt, userId);
        return listFvt.size();
    }

    public static int getCheckOutNumber(String userId) throws EazyException {
        Set<String> listChO = CheckoutManager.getCollectionCheckOut(userId);
        listChO.remove(userId);
        Tool.removeBlackList(listChO, userId);
        return listChO.size();
    }
    
    public static int getMyFootPrintNumber(String userId) throws EazyException {
        if (userId == null || userId.isEmpty()){
            return 0;
        }
        Set<String> listChO = MyFootPrintManager.getMyFootprintCollections(userId);
        listChO.remove(userId);
        Tool.removeBlackList(listChO, userId);
        return listChO.size();
    }

    public static int getNewFavoritedNumber(String userId) throws EazyException {
        Long time = LastTimeGetDAO.getLastTimeFvt(userId);
        time = time == null  ? 0L : time;
        List<String> listFvt = FavoritedDAO.getNewFavoritedNumber(userId, time);
        listFvt.remove(userId);
        Tool.removeBlackList(listFvt, userId);
//        LastTimeGetDAO.updateLastGetFavoristed(userId, updateTime, listFvt);
        return listFvt.size();
    }

    public static int getNewCheckoutNumber(String userId) throws EazyException {
        Long time = LastTimeGetDAO.getLastTimeCheckout(userId);
        time = time == null  ? 0L : time;
//        List<String> listCheckout = CheckOutDAO.getNewCheckOutNumber(userId, time);
        Set<String> listCheckout = CheckoutManager.getCollectionNumberByTime(userId, time);
        listCheckout.remove(userId);
        Tool.removeBlackList(listCheckout, userId);
//        LastTimeGetDAO.updateLastGetCheckOut(userId, updateTime, listCheckout);
        return listCheckout.size();
    }

    public static void getAttentionNumber(LoginData data) throws EazyException {
        String userId = data.user.userId;
        data.checkoutNum = Helper.getNewCheckoutNumber(userId);
        data.user.favouritedNumber = (long) getNewFavoritedNumber(userId);
        data.myFootprintNumber = getMyFootPrintNumber(userId);
    }

    public static void getAttentionNumber(String userId, MyPageData data) throws EazyException {
        data.checkoutNum = getNewCheckoutNumber(userId);
        data.myFootprintNumber = getMyFootPrintNumber(userId);
        data.favouritedNumber = (long) getNewFavoritedNumber(userId);
    }

    public static void getAttentionNumber(String userId, GetAttentionNumberData data) throws EazyException {
        long startCheckout = System.currentTimeMillis();
        data.newCheckoutNum = getNewCheckoutNumber(userId);
        long endCheckout = System.currentTimeMillis();
        endCheckout -= startCheckout;
        if(endCheckout > 1500){
            Util.addInfoLog("Get Attention Number: new checkout number slow");
        }
        
        long startNewFavorited = System.currentTimeMillis();
        data.newFavoritedNumber = getNewFavoritedNumber(userId);
        long endNewFavorited = System.currentTimeMillis();
        endNewFavorited -= startNewFavorited;
        if(endNewFavorited > 1500){
            Util.addInfoLog("Get Attention Number: new favorited number slow");
        }
        
        long startCheckOut = System.currentTimeMillis();
        data.checkoutNum = getCheckOutNumber(userId);
        data.myFootprintNumber = getMyFootPrintNumber(userId);
        long endCheckOut = System.currentTimeMillis();
        endCheckOut -= startCheckOut;
        if(endCheckOut > 1500){
            Util.addInfoLog("Get Attention Number: checkout number slow");
        }
        
        long startFavorited = System.currentTimeMillis();
        data.favouritedNumber = getFavoristedNumber(userId);
        long endFavorited = System.currentTimeMillis();
        endFavorited -= startFavorited;
        if(endFavorited > 1500){
            Util.addInfoLog("Get Attention Number: favorited number slow");
        }
    }

    public static Date addLoginTime(Date loginDate, Date time, Date lastLogin, String userId, String ip) throws EazyException {
        Util.addDebugLog("Update last online: userID: " + userId + " loginDate before : " + loginDate.toString() + " now: " + time.toString());
        Calendar cal = Calendar.getInstance();
//        cal.setTime(time);
//        if(lastLogin == null || lastLogin.before(loginDate)){
//            int loginHour = loginDate.getHours();
//            int nowHour = time.getHours();
//            
//            if (nowHour > loginHour) {
//                if ((nowHour - loginHour >= 12)) {
//                    cal.add(Calendar.DATE, 1);
//                }
//            } else {
//                if ((loginHour - nowHour > 12)) {
//                    cal.add(Calendar.DATE, -1);
//                }
//            }
//        }
        loginDate = cal.getTime();
        LogLoginDAO.addLog(userId, time, ip);
        Util.addDebugLog("Update last online: userID: " + userId + " loginDate : " + loginDate.toString() + " now: " + time.toString());
        UserActivityDAO.updateLastOnline(userId, loginDate.getTime(), time);
        return loginDate;
    }

    public static boolean isAvalableImage(Image image){
        return image.imageStatus == Constant.REVIEW_STATUS_FLAG.APPROVED  && (image.reportFlag == null || image.reportFlag == Constant.REPORT_STATUS_FLAG.GOOD || (image.reportFlag == Constant.REPORT_STATUS_FLAG.WAITING && image.appearFlag == Constant.FLAG.ON));
    }     
}
