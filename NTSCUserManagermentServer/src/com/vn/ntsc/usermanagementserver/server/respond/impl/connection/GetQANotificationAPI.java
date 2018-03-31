/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.usermanagementserver.server.respond.impl.connection;

import eazycommon.constant.Constant;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.ParamKey;
import eazycommon.util.DateFormat;
import eazycommon.util.Util;
import java.util.Date;
import java.util.List;
import org.json.simple.JSONObject;
import com.vn.ntsc.usermanagementserver.dao.impl.NotificationDAO;
import com.vn.ntsc.usermanagementserver.dao.impl.UserDAO;
import com.vn.ntsc.usermanagementserver.entity.IEntity;
import com.vn.ntsc.usermanagementserver.server.blacklist.BlockUserManager;
import com.vn.ntsc.usermanagementserver.server.respond.IApiAdapter;
import com.vn.ntsc.usermanagementserver.server.respond.Respond;
import com.vn.ntsc.usermanagementserver.server.respond.result.ListEntityRespond;

/**
 *
 * @author Administrator
 */
public class GetQANotificationAPI implements IApiAdapter{
    
    @Override
    public Respond execute(JSONObject obj, Date time) {
        ListEntityRespond respond = new ListEntityRespond();
        List<IEntity> result;
        Util.addDebugLog("========== get qa notification");
        try {
            String userId = Util.getStringParam(obj, ParamKey.USER_ID);
            int take = (Util.getLongParam(obj, ParamKey.TAKE)).intValue();
            String timeStamp = Util.getStringParam(obj, ParamKey.TIME_STAMP);

            Date date = time;

            // if timeStamp == null, date = current time of server
            if (timeStamp != null) {
                date = DateFormat.parse(timeStamp);
            }
            List<String> blockList = BlockUserManager.getBlackList(userId);
//            Collection<String> deactiveList = BlackListManager.toList();

            result = NotificationDAO.getNotificationListByType(userId, date.getTime(), take, blockList, Constant.NOTIFICATION_TYPE_VALUE.QA_NOTI);
            UserDAO.updateQANotificationReadTime(userId,time.getTime());// edit LongLT 25Aug2016
            respond = new ListEntityRespond(ErrorCode.SUCCESS, result);
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
        return respond;
    }
}
