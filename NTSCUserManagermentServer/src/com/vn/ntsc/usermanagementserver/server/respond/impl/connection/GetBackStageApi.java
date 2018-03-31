/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.usermanagementserver.server.respond.impl.connection;

import com.vn.ntsc.usermanagementserver.server.respond.Respond;
import com.vn.ntsc.usermanagementserver.server.respond.IApiAdapter;
import com.vn.ntsc.usermanagementserver.dao.impl.UnlockDAO;
import com.vn.ntsc.usermanagementserver.dao.impl.BackstageDAO;
import com.vn.ntsc.usermanagementserver.dao.impl.UserDAO;
import eazycommon.util.Util;
import java.util.Date;
import java.util.List;
import eazycommon.constant.ErrorCode;
import eazycommon.exception.EazyException;
import org.json.simple.JSONObject;
import eazycommon.constant.ParamKey;
import com.vn.ntsc.usermanagementserver.server.blacklist.BlockUserManager;
import com.vn.ntsc.usermanagementserver.server.pointaction.ActionType;
import com.vn.ntsc.usermanagementserver.server.pointaction.ConnectionPrice;
import com.vn.ntsc.usermanagementserver.server.respond.result.ListStringRespond;

/**
 *
 * @author RuAc0n
 */
public class GetBackStageApi implements IApiAdapter {

    @Override
    public Respond execute(JSONObject obj, Date time) {
        ListStringRespond result = new ListStringRespond();
        try {
            String userId = Util.getStringParam(obj, ParamKey.USER_ID);
            long skip = Util.getLongParam(obj, ParamKey.SKIP);
            long take = Util.getLongParam(obj, ParamKey.TAKE);
            String friendId = Util.getStringParam(obj, ParamKey.REQUEST_USER_ID);
            List<String> listBackstage;
            if (friendId != null) {
                boolean checker = UserDAO.checkUser(friendId);
                if (checker) {
                    if (!BlockUserManager.isBlock(userId, friendId)) {
                        ConnectionPrice connectionPrice = ConnectionPrice.getConnectionPrice(String.valueOf(ActionType.unlock_backstage), userId, friendId);
                        int price = 0- connectionPrice.senderPrice;
                        boolean isUnlock = price == 0 || UnlockDAO.isBackStageUnlock(userId, friendId);
                        if (isUnlock) {
                            listBackstage = BackstageDAO.getBackStage(friendId, skip, take);
                            result = new ListStringRespond(ErrorCode.SUCCESS, listBackstage);
                        } else {
                            result.code = ErrorCode.LOCK_FEATURE;
                        }
                    } else {
                        result.code = ErrorCode.BLOCK_USER;
                    }
                } else {
                    result.code = ErrorCode.USER_NOT_EXIST;
                }
            } else {
                listBackstage = BackstageDAO.getBackStage(userId, skip, take);
                result = new ListStringRespond(ErrorCode.SUCCESS, listBackstage);
            }
        } catch (EazyException ex) {
            Util.addErrorLog(ex);
            result.code = ex.getErrorCode();
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
        return result;
    }

}
