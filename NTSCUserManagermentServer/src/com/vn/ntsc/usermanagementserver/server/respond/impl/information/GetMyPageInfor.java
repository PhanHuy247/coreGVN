/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.usermanagementserver.server.respond.impl.information;

import eazycommon.util.Util;
import java.util.Date;
import java.util.List;
import eazycommon.constant.Constant;
import eazycommon.constant.ErrorCode;
import eazycommon.exception.EazyException;
import org.json.simple.JSONObject;
import eazycommon.constant.ParamKey;
import com.vn.ntsc.usermanagementserver.dao.impl.NotificationDAO;
import com.vn.ntsc.usermanagementserver.dao.impl.UserDAO;
import com.vn.ntsc.usermanagementserver.entity.impl.datarespond.MyPageData;
import com.vn.ntsc.usermanagementserver.server.blacklist.BlockUserManager;
import com.vn.ntsc.usermanagementserver.server.respond.IApiAdapter;
import com.vn.ntsc.usermanagementserver.server.respond.Respond;
import com.vn.ntsc.usermanagementserver.server.respond.common.Helper;
import com.vn.ntsc.usermanagementserver.server.respond.result.EntityRespond;

/**
 *
 * @author Admin
 */
public class GetMyPageInfor implements IApiAdapter {

    @Override
    public Respond execute(JSONObject obj, Date time) {
        EntityRespond respond = new EntityRespond();

        try {
            String userId = Util.getStringParam(obj, ParamKey.USER_ID);
//            Long activityTime = Util.getLongParam(obj, ParamKey.TIME);
//            UserActivityDAO.updateLastActivity(userId, activityTime);
//            if (UserDAO.isExists(userId)) {
            MyPageData data = new MyPageData();
            data.backstageNumber = new Long(UserDAO.getBackStageNumber(userId));
            data.buzzNumber = new Long(UserDAO.getBuzzNumber(userId));
//            List<String> giftList = GiftDAO.getAllGift();
//            data.giftData = UserGiftDAO.getGiftList(userId, giftList);
            Helper.getAttentionNumber(userId, data);
//            data.favouritedNumber = new Long(UserDAO.getFavoristedNumber(userId));
            List<String> blockUsers = BlockUserManager.getBlackList(userId);
//            Collection<String> deactiveUsers = BlackListManager.toList();
            Long readTime = UserDAO.getLikeNotificationReadTime(userId);
            Util.addDebugLog("------------------------------------");
            Util.addDebugLog("Read Like Notify Time" + readTime);
            data.notiLikeNumber = new Long(NotificationDAO.getNotificationNumberByType(userId, Constant.NOTIFICATION_TYPE_VALUE.LIKE_MY_BUZZ_NOTI, blockUsers, readTime));
            Long readNewsTime = UserDAO.getNewsNotificationReadTime(userId);
            Long readQATime = UserDAO.getQANotificationReadTime(userId);
            Util.addDebugLog("Read News Notify Time" + readNewsTime);
            data.notiNewsNumber = new Long(NotificationDAO.getNotificationNumberByType(userId, Constant.NOTIFICATION_TYPE_VALUE.AUTO_NEWS, blockUsers, readNewsTime));
//            Util.addDebugLog("GetMyPageInfo call DATA " + data.toString());
            data.notiQANumber = new Long(NotificationDAO.getNotificationNumberByType(userId, Constant.NOTIFICATION_TYPE_VALUE.QA_NOTI, blockUsers, readQATime));
            Util.addDebugLog("------------------------------------");
            
            data.notiNumber =  Helper.getNotificationNumber(userId) - data.notiQANumber.intValue();
            
            respond = new EntityRespond(ErrorCode.SUCCESS, data);
//            }
        } catch (EazyException ex) {
            Util.addErrorLog(ex);
        }

        return respond;
    }

}
