/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.usermanagementserver.server.respond.impl.activity;

import com.vn.ntsc.usermanagementserver.server.respond.Respond;
import com.vn.ntsc.usermanagementserver.server.respond.IApiAdapter;
import com.vn.ntsc.usermanagementserver.dao.impl.UnlockDAO;
import com.vn.ntsc.usermanagementserver.dao.impl.RateDAO;
import com.vn.ntsc.usermanagementserver.dao.impl.UserDAO;
import eazycommon.util.Util;
import java.util.Date;
import java.util.List;
import eazycommon.constant.ErrorCode;
import org.json.simple.JSONObject;
import eazycommon.constant.ParamKey;
import com.vn.ntsc.usermanagementserver.entity.impl.image.RateBackstageData;
import com.vn.ntsc.usermanagementserver.server.blacklist.BlockUserManager;
import com.vn.ntsc.usermanagementserver.server.respond.result.EntityRespond;

/**
 *
 * @author RuAc0n
 */
public class RateBackstageApi implements IApiAdapter {

    @Override
    public Respond execute(JSONObject obj, Date time) {
        EntityRespond result = new EntityRespond();
        try {
            String userId = Util.getStringParam(obj, ParamKey.USER_ID);
            String friendId = Util.getStringParam(obj, ParamKey.REQUEST_USER_ID);
            Long ratePoint = Util.getLongParam(obj, "rate_point");
            boolean checker = UserDAO.checkUser(friendId);
            if (checker) {
                if (!BlockUserManager.isBlock(userId, friendId)) {
                    boolean isUnlock = UnlockDAO.isBackStageUnlock(userId, friendId);
                    if (isUnlock) {
                        RateDAO.addRate(friendId, userId, ratePoint.intValue());
                        List<Integer> listRate = RateDAO.getRateList(friendId);
                        double rate = 0;
                        for (Integer listRate1 : listRate) {
                            rate += listRate1;
                        }
                        rate = rate / (double) listRate.size();
                        UserDAO.addRate(friendId, rate, listRate.size());
                        result = new EntityRespond(ErrorCode.SUCCESS, new RateBackstageData(rate));
                    } else {
                        result.code = ErrorCode.LOCK_FEATURE;
                    }
                } else {
                    result.code = ErrorCode.BLOCK_USER;
                }
            } else {
                result.code = ErrorCode.USER_NOT_EXIST;
            }
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
        return result;
    }

}
