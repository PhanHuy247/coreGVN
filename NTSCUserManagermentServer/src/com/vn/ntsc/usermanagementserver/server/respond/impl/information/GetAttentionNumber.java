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
import org.json.simple.JSONObject;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.ParamKey;
import com.vn.ntsc.usermanagementserver.entity.impl.datarespond.GetAttentionNumberData;
import com.vn.ntsc.usermanagementserver.server.respond.IApiAdapter;
import com.vn.ntsc.usermanagementserver.server.respond.Respond;
import com.vn.ntsc.usermanagementserver.server.respond.common.Helper;
import com.vn.ntsc.usermanagementserver.server.respond.result.EntityRespond;
import com.vn.ntsc.usermanagementserver.dao.impl.NotificationDAO;
import com.vn.ntsc.usermanagementserver.dao.impl.UserDAO;
import com.vn.ntsc.usermanagementserver.server.blacklist.BlockUserManager;

/**
 *
 * @author duyetpt
 */
public class GetAttentionNumber implements IApiAdapter {

    @Override
    public Respond execute(JSONObject obj, Date time) {
        EntityRespond respond = new EntityRespond();
        try {
            String userId = Util.getStringParam(obj, ParamKey.USER_ID);
            GetAttentionNumberData data = new GetAttentionNumberData();
            Helper.getAttentionNumber(userId, data);
            List<String> blockUsers = BlockUserManager.getBlackList(userId);
            Long readTime = UserDAO.getNotificationReadTime(userId);
            data.notiLikeNumber = NotificationDAO.getNotificationNumberByType(userId, Constant.NOTIFICATION_TYPE_VALUE.LIKE_MY_BUZZ_NOTI, blockUsers,readTime);
            data.notiNumber = Helper.getNotificationNumber(userId);
            respond.code = ErrorCode.SUCCESS;
            respond.data = data;
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
        return respond;
    }

}
