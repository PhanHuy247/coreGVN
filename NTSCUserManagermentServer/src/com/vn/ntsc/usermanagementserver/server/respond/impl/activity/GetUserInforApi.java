/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.usermanagementserver.server.respond.impl.activity;

import com.vn.ntsc.usermanagementserver.server.respond.Respond;
import com.vn.ntsc.usermanagementserver.server.respond.IApiAdapter;
import com.vn.ntsc.usermanagementserver.dao.impl.ReviewingUserDAO;
import com.vn.ntsc.usermanagementserver.dao.impl.MyFootPrintDAO;
import com.vn.ntsc.usermanagementserver.dao.impl.ImageDAO;
import com.vn.ntsc.usermanagementserver.dao.impl.CheckOutDAO;
import com.vn.ntsc.usermanagementserver.dao.impl.UnlockDAO;
import com.vn.ntsc.usermanagementserver.dao.impl.MemoDAO;
import com.vn.ntsc.usermanagementserver.dao.impl.OnlineAlertDAO;
import com.vn.ntsc.usermanagementserver.dao.impl.FavoristDAO;
import com.vn.ntsc.usermanagementserver.dao.impl.RateVoiceDAO;
import com.vn.ntsc.usermanagementserver.dao.impl.UserDAO;
import eazycommon.util.Util;
import java.util.Date;
import java.util.List;
import eazycommon.constant.Constant;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.ParamKey;
import eazycommon.exception.EazyException;
import eazycommon.inspection.version.InspectionVersionDAO;
import org.json.simple.JSONObject;
import com.vn.ntsc.usermanagementserver.dao.impl.log.LogCheckOutDAO;
import com.vn.ntsc.usermanagementserver.entity.impl.datarespond.GetUserInfoData;
import com.vn.ntsc.usermanagementserver.entity.impl.user.ReviewingUser;
import com.vn.ntsc.usermanagementserver.entity.impl.user.User;
import com.vn.ntsc.usermanagementserver.entity.impl.user.extend.Female;
import com.vn.ntsc.usermanagementserver.entity.impl.user.extend.Male;
import com.vn.ntsc.usermanagementserver.server.blacklist.BlockUserManager;
import com.vn.ntsc.usermanagementserver.server.respond.common.Helper;
import com.vn.ntsc.usermanagementserver.server.respond.result.EntityRespond;
import com.vn.ntsc.usermanagementserver.server.userinformanager.CheckoutManager;
import com.vn.ntsc.usermanagementserver.server.userinformanager.MyFootPrintManager;

/**
 *
 * @author RuAc0n
 */
public class GetUserInforApi implements IApiAdapter {

    private static final long TIME_CHECK_OUT = 30 * 60 * 1000;

    @Override
    public Respond execute(JSONObject obj, Date time) {
        EntityRespond result = new EntityRespond();
        try {
            Util.addDebugLog("GetUserInforApi--------------------------- ");
            String userId = Util.getStringParam(obj, ParamKey.USER_ID);
            String friendId = Util.getStringParam(obj, ParamKey.REQUEST_USER_ID);
            Util.addDebugLog("userId " + userId);
            Util.addDebugLog("friendId " + friendId);
            if (friendId != null && !friendId.equals(userId)) {
          
                if (UserDAO.checkUser(friendId) && UserDAO.getRegisterFlag(friendId) == Constant.FLAG.ON) {
                    if(userId == null){
                        User user = UserDAO.getUserInfor(friendId);
                        user.rateValue = RateVoiceDAO.getTopRate(friendId, 2);
                        user.hasRated = RateVoiceDAO.getRatedValue(userId, friendId);
//                        GetUserInfoData data = new GetUserInfoData();
                        user.email = null;
                        user.finishRegisterFlag = null;
//                        user.updateEmailFlag = null;
                        if (user instanceof Female) {
                            ((Female) user).verificationFlag = null;
                        }
//                        data.user = user;
                        Util.addDebugLog("user--------------------------- "+user.toJsonObject());
                        result = new EntityRespond(ErrorCode.SUCCESS, user);
                    }else{
                        if (!BlockUserManager.isBlock(userId, friendId)) {
                            User user = UserDAO.getUserInfor(friendId);
                            user.rateValue = RateVoiceDAO.getTopRate(friendId, 2);
                            user.hasRated = RateVoiceDAO.getRatedValue(userId, friendId);
                            User user1 = UserDAO.getUserInfor(userId);
                            Util.addDebugLog("deviceType1 " + user1.deviceType);
                            Util.addDebugLog("user1.job1 " + user1.job);
                            Util.addDebugLog("user1.appVersion1 " + user1.appVersion);
    //                        if (user1.deviceType == 1 && user1.gender == 1) {
    //                            if (user1.appVersion != null && user1.appVersion.equals("1.1")) {
    //                                if (user.job != null) {
    //                                    user.job = user.job + 17;
    //                                }
    //                                Util.addDebugLog("user.job1 +17 " + user.job);
    //                            }
    //
    //                        }

                            GetUserInfoData data = new GetUserInfoData();
                            // dont return email of others
                            user.email = null;
                            user.finishRegisterFlag = null;
//                            user.updateEmailFlag = null;
                            if (user instanceof Female) {
                                ((Female) user).verificationFlag = null;
                            }
                            data.user = user;

    //                        if (user1.deviceType == 0) {
    //                            String safaryVersion = InspectionVersionDAO.getIOSTurnOffSafaryVersion();
    //                            if (user1.appVersion.equals(safaryVersion)) {
    //                                //IOSの審査官
    //                                if (!user1.appVersion.equals(user.appVersion)) {
    //                                    data.user.backstageNumber = 0L;
    //                                }
    //                                //IOS審査官、Android not android
    //                                if (user.deviceType == 1) {
    //                                    data.user.backstageNumber = 0L;
    //                                }
    //                            }
    //                        }

                            data.isFavourist = FavoristDAO.checkFavourist(userId, friendId);
    //                        String status = UserActivityDAO.getStatus(friendId);
    //                        data.user.status = status;
    //                        List<String> listGift = GiftUserDAO.getGiftList(friendId, 0, 0);
    //                        data.listGift = listGift;
                            Long isAlert = (long) Constant.FLAG.OFF;
                            boolean checkAlert = OnlineAlertDAO.checkAlert(friendId, userId);
                            if (checkAlert) {
                                isAlert = new Long(Constant.FLAG.ON);
                            }
                            Integer isUnlockBackStage = Constant.FLAG.OFF;
                            if (UnlockDAO.isBackStageUnlock(userId, friendId)) {
                                isUnlockBackStage = Constant.FLAG.ON;
                            }
                            data.unlockBackstage = isUnlockBackStage;
                            data.isAlert = isAlert;
                            data.myFootprintNumber = Helper.getMyFootPrintNumber(userId);

                            Date lastCheckOut = CheckOutDAO.getCheckOutTime(friendId, userId);
                            if (lastCheckOut == null || (time.getTime() - lastCheckOut.getTime()) >= TIME_CHECK_OUT) {
                                String ip = Util.getStringParam(obj, ParamKey.IP);

                                LogCheckOutDAO.addLog(userId, friendId, time, ip);

                                CheckOutDAO.addCheckOut(friendId, userId, time);
                                CheckoutManager.update(friendId, userId, time.getTime());

                                MyFootPrintDAO.add(userId, friendId, time);
                                MyFootPrintManager.update(userId, friendId, time.getTime());

                                // add checkOut notification
    //                            if (NotificationSettingDAO.checkUserNotification(friendId, Constant.CHECK_OUT_UNLOCK_NOTI)) {
    //                                Notification noti = new Notification();
    //                                noti.notiUserId = userId;
    //                                noti.notiUserName = UserDAO.getUserName(userId);
    //                                NotificationDAO.addNotification(friendId, noti, time.getTime(), Constant.CHECK_OUT_UNLOCK_NOTI);
    //                                data.isNoti = new Long(Constant.YES);
    //                            }
                            }

                            //Linh add 2017/02/20 #6687
                            String memo = MemoDAO.getMemoByFriendID(userId, friendId);
                            data.memo = memo;

                            result = new EntityRespond(ErrorCode.SUCCESS, data);
                        } else {
                            result.code = ErrorCode.BLOCK_USER;
                        }
                    }
                    
                } else {
                    result.code = ErrorCode.USER_NOT_EXIST;
                }
            } else {
                long starUserInf = System.currentTimeMillis();
                User user = UserDAO.getUserInfor(userId);
                user.rateValue = RateVoiceDAO.getTopRate(userId, 2);
                user.hasRated = RateVoiceDAO.getRatedValue(userId, userId);
                Util.addDebugLog("deviceType2 " + user.deviceType);
                Util.addDebugLog("user.job2 " + user.job);
                Util.addDebugLog("user.appVersion " + user.appVersion);
                if (user.deviceType == 1 && user.gender == 0) {
                    if (user.appVersion != null && user.appVersion.equals("1.1")) {
                        user.job = user.job + 17;
                        Util.addDebugLog("user.job2 +17 " + user.job);
                    }
                }

                getReviewingInfo(userId, user);
                GetUserInfoData data = new GetUserInfoData();
                data.user = user;
                long endUserInf = System.currentTimeMillis();
                endUserInf -= starUserInf;
                if (endUserInf > 1500) {
                    Util.addInfoLog(" Get user Inf :  get User infor slow");
                }

//                long startStatus = System.currentTimeMillis();
//                String status = UserActivityDAO.getStatus(userId);
//                data.user.status = status;
//                long endStatus = System.currentTimeMillis();
//                endStatus -= startStatus;
//                if(endStatus > 1500){
//                    Util.addInfoLog(" Get user Inf :  get status slow" );
//                }
//                long startGift = System.currentTimeMillis();
//                List<String> listGift = GiftUserDAO.getGiftList(userId, 0, 0);
//                data.listGift = listGift;
//                long endGift = System.currentTimeMillis();
//                endGift -= startGift;
//                if(endGift > 1500){
//                    Util.addInfoLog(" Get user Inf :  get gift slow" );
//                }
                data.notiNum = Helper.getNotificationSeenNumber(userId);
                long startCheckout = System.currentTimeMillis();
                data.checkoutNum = Helper.getCheckOutNumber(userId);
                long endCheckout = System.currentTimeMillis();
                endCheckout -= startCheckout;
                if (endCheckout > 1500) {
                    Util.addInfoLog(" Get user Inf :  get checkout slow");
                }

                long startAvatar = System.currentTimeMillis();
                data.reviewingAvatar = ImageDAO.getReviewingAvatar(userId);
                long endAvatar = System.currentTimeMillis();
                endAvatar -= startAvatar;
                if (endAvatar > 1500) {
                    Util.addInfoLog(" Get user Inf :  get reviewing avatar slow");
                }

                long startFavorited = System.currentTimeMillis();
                data.user.favouritedNumber = (long) Helper.getFavoristedNumber(userId);
                long endFavorited = System.currentTimeMillis();
                endFavorited -= startFavorited;
                if (endFavorited > 1500) {
                    Util.addInfoLog(" Get user Inf :  get favorited slow");
                }

                result = new EntityRespond(ErrorCode.SUCCESS, data);
            }
        } catch (EazyException ex) {
            Util.addErrorLog(ex);
            result.code = ex.getErrorCode();
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
        return result;
    }

    private void getReviewingInfo(String userId, User user) throws EazyException {
        ReviewingUser reviewingUser = ReviewingUserDAO.get(userId);
        if (reviewingUser != null) {
            if (reviewingUser.about != null) {
                user.about = reviewingUser.about;
            }
            if (user.gender == Constant.GENDER.FEMALE) {
                Female female = (Female) user;
                if (reviewingUser.fetish != null) {
                    female.fetishs = reviewingUser.fetish;
                }
                if (reviewingUser.typeOfMan != null) {
                    female.typeOfMan = reviewingUser.typeOfMan;
                }
                if (reviewingUser.hobby != null) {
                    female.hobby = reviewingUser.hobby;
                }
            } else {
                Male male = (Male) user;
                if (reviewingUser.hobby != null) {
                    male.hobby = reviewingUser.hobby;
                }
            }
        }
    }

}
