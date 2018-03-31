/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.usermanagementserver.server.respond.impl.connection;

import com.vn.ntsc.usermanagementserver.server.respond.Respond;
import com.vn.ntsc.usermanagementserver.server.respond.IApiAdapter;
import com.vn.ntsc.usermanagementserver.dao.impl.BlockDAO;
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
public class RemoveBlockApi implements IApiAdapter {

    @Override
    public Respond execute(JSONObject obj, Date time) {
        EntityRespond result = new EntityRespond();
        try {
            String friendId = Util.getStringParam(obj, "blk_user_id");
            String userId = Util.getStringParam(obj, ParamKey.USER_ID);
            boolean checker = UserDAO.checkUser(friendId);

            if (checker) {
                //1. check Friend
//                Long isBlock = BlockDAO.isBlock(userId, friendId);
                if (BlockUserManager.isBlock(userId, friendId)) {
                    BlockDAO.removeBlock(userId, friendId);
                    BlockDAO.removeBlocked(friendId, userId);
                    BlockUserManager.remove(userId, friendId);
                    BlockUserManager.remove(friendId, userId);
                    String ip = Util.getStringParam(obj, ParamKey.IP);
                    LogBlockDAO.addLog(userId, friendId, time, Constant.FLAG.OFF, ip);
                    if (FavoristDAO.checkFavourist(userId, friendId) == Constant.FLAG.ON) {
                        UserDAO.addFavorist(userId);
                        UserDAO.addFavorited(friendId);
                    }
                    if (FavoritedDAO.checkFavourited(userId, friendId) == Constant.FLAG.ON) {
                        UserDAO.addFavorist(friendId);
                        UserDAO.addFavorited(userId);
                    }
                }
                User user = UserDAO.getUserInfor(userId);
                result.code = ErrorCode.SUCCESS;
                result.data = new BlockData(user.favouristNumber,  Helper.getMyFootPrintNumber(userId));
            } else {
                result.code = ErrorCode.USER_NOT_EXIST;
                return result;
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
