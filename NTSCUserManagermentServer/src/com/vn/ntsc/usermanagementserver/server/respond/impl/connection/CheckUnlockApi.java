/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.usermanagementserver.server.respond.impl.connection;

import com.vn.ntsc.usermanagementserver.server.respond.Respond;
import com.vn.ntsc.usermanagementserver.server.respond.IApiAdapter;
import com.vn.ntsc.usermanagementserver.dao.impl.RateDAO;
import com.vn.ntsc.usermanagementserver.dao.impl.UnlockDAO;
import com.vn.ntsc.usermanagementserver.dao.impl.UserDAO;
import eazycommon.util.Util;
import java.util.Date;
import eazycommon.constant.Constant;
import eazycommon.constant.ErrorCode;
import eazycommon.exception.EazyException;
import org.json.simple.JSONObject;
import eazycommon.constant.ParamKey;
import com.vn.ntsc.usermanagementserver.entity.impl.datarespond.CheckUnlockBackstageData;
import com.vn.ntsc.usermanagementserver.entity.impl.user.User;
import com.vn.ntsc.usermanagementserver.server.blacklist.BlockUserManager;
import com.vn.ntsc.usermanagementserver.server.pointaction.ActionType;
import com.vn.ntsc.usermanagementserver.server.pointaction.ConnectionPrice;
import com.vn.ntsc.usermanagementserver.server.respond.result.EntityRespond;
import com.vn.ntsc.usermanagementserver.server.userinformanager.UserInforManager;

/**
 *
 * @author RuAc0n
 */
public class CheckUnlockApi implements IApiAdapter {

    @Override
    public Respond execute(JSONObject obj, Date time) {
        Respond result = new Respond();
        try {
            String userId = Util.getStringParam(obj, ParamKey.USER_ID);
            result = checkUnlockBackstage(obj, userId);
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
        return result;
    }

    private Respond checkUnlockBackstage(JSONObject obj, String userId) throws EazyException {
        Respond result;
        String friendId = Util.getStringParam(obj, ParamKey.REQUEST_USER_ID);
        EntityRespond backRes = new EntityRespond();
        if (friendId != null && !friendId.isEmpty()) {
            boolean checker = UserDAO.checkUser(friendId);
            if (checker) {
                if (!BlockUserManager.isBlock(userId, friendId)) {
                    CheckUnlockBackstageData data;
                    int price = 0 - ConnectionPrice.getBadConnectionPrice(String.valueOf(ActionType.unlock_backstage), userId).senderPrice;
                    boolean isUnlock = price == 0 || UnlockDAO.isBackStageUnlock(userId, friendId);
                    int ratePoint = RateDAO.getRatePoint(friendId, userId);
                    User user = UserDAO.getBackStageInfor(friendId);
                    int point = UserInforManager.getPoint(userId);
                    if (!isUnlock) {
                        data = new CheckUnlockBackstageData((long) Constant.FLAG.OFF, (long) point, user.backStageRate, user.backstageNumber, user.rateNumber, ratePoint);
                    } else {
                        data = new CheckUnlockBackstageData((long) Constant.FLAG.ON, (long) point, user.backStageRate, user.backstageNumber, user.rateNumber, ratePoint);
                    }
                    backRes = new EntityRespond(ErrorCode.SUCCESS, data);
                } else {
                    backRes.code = ErrorCode.BLOCK_USER;
                }
            } else {
                backRes.code = ErrorCode.USER_NOT_EXIST;
            }
        } else {
            User user = UserDAO.getBackStageInfor(userId);
            CheckUnlockBackstageData data = new CheckUnlockBackstageData((long) Constant.FLAG.ON, user.point, user.backStageRate, user.backstageNumber, user.rateNumber, null);
            backRes = new EntityRespond(ErrorCode.SUCCESS, data);
        }
        result = backRes;
        return result;
    }

}
