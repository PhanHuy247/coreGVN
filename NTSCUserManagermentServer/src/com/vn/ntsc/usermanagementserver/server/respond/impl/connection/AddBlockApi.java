/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.usermanagementserver.server.respond.impl.connection;

import com.vn.ntsc.usermanagementserver.server.respond.Respond;
import com.vn.ntsc.usermanagementserver.server.respond.IApiAdapter;
import com.vn.ntsc.usermanagementserver.dao.impl.FavoristDAO;
import com.vn.ntsc.usermanagementserver.dao.impl.BlockDAO;
import com.vn.ntsc.usermanagementserver.dao.impl.UserDAO;
import eazycommon.util.Util;
import java.util.Date;
import eazycommon.constant.Constant;
import eazycommon.constant.ErrorCode;
import eazycommon.exception.EazyException;
import org.json.simple.JSONObject;
import eazycommon.constant.ParamKey;
import com.vn.ntsc.usermanagementserver.dao.impl.log.LogBlockDAO;
import com.vn.ntsc.usermanagementserver.entity.impl.datarespond.BlockData;
import com.vn.ntsc.usermanagementserver.entity.impl.user.User;
import com.vn.ntsc.usermanagementserver.server.blacklist.BlockUserManager;
import com.vn.ntsc.usermanagementserver.server.respond.common.Helper;
import com.vn.ntsc.usermanagementserver.server.respond.result.EntityRespond;

/**
 *
 * @author RuAc0n
 */
public class AddBlockApi implements IApiAdapter {

    @Override
    public Respond execute(JSONObject obj, Date time) {
        EntityRespond result = new EntityRespond();
        try {
            String friendId = Util.getStringParam(obj, ParamKey.REQUEST_USER_ID);
            String userId = Util.getStringParam(obj, ParamKey.USER_ID);
            boolean checker = UserDAO.checkUser(friendId);
            if (checker && !friendId.equals(userId)) {
                //1. check Friend
//                Long isBlock = BlockDAO.isBlock(userId, friendId) ? (long) Constant.YES;
                if (!BlockUserManager.isBlock(userId, friendId)) {
                    BlockDAO.addBlockList(userId, friendId);
                    BlockDAO.addBlockedList(friendId, userId);
                    BlockUserManager.add(userId, friendId);
                    BlockUserManager.add(friendId, userId);
                    String ip = Util.getStringParam(obj, ParamKey.IP);
                    LogBlockDAO.addLog(userId, friendId, time, Constant.FLAG.ON, ip);
                    if (FavoristDAO.checkFavourist(userId, friendId) == Constant.FLAG.ON) {
                        UserDAO.removeFavourist(userId);
                        UserDAO.removeFavourited(friendId);
                    }
                    if (FavoristDAO.checkFavourist(friendId, userId) == Constant.FLAG.ON) {
                        UserDAO.removeFavourist(friendId);
                        UserDAO.removeFavourited(userId);
                    }
                    User user = UserDAO.getUserInfor(userId);
                    BlockData data = new BlockData(user.favouristNumber, Helper.getMyFootPrintNumber(userId));
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

}
