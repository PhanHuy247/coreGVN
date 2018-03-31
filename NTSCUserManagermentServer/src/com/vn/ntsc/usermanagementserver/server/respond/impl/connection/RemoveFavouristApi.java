/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.usermanagementserver.server.respond.impl.connection;

import com.vn.ntsc.usermanagementserver.server.respond.Respond;
import com.vn.ntsc.usermanagementserver.server.respond.IApiAdapter;
import com.vn.ntsc.usermanagementserver.dao.impl.log.LogFavouristDAO;
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
import com.vn.ntsc.usermanagementserver.server.blacklist.BlockUserManager;

/**
 *
 * @author RuAc0n
 */
public class RemoveFavouristApi implements IApiAdapter {

    @Override
    public Respond execute(JSONObject obj, Date time) {
        Respond result = new Respond();
        try {
            String favourist = Util.getStringParam(obj, "fav_id");
            String userId = Util.getStringParam(obj, ParamKey.USER_ID);
            boolean checker = UserDAO.checkUser(favourist);
            if (checker) {
                if (!BlockUserManager.isBlock(userId, favourist)) {
                    Long isFavourist = FavoristDAO.checkFavourist(userId, favourist);
                    if (isFavourist == Constant.FLAG.ON) {
                        //1. remove in collection user
                        UserDAO.removeFavourist(userId);
                        //3.add favourist to user
                        FavoristDAO.removeFavorist(userId, favourist);
                        //4. remove in document user of friend
                        UserDAO.removeFavourited(favourist);
                        //5. add favourited to friend
                        FavoritedDAO.removeFavourited(favourist, userId);

                        String ip = Util.getStringParam(obj, ParamKey.IP);
                        LogFavouristDAO.addLog(userId, favourist, time, Constant.FLAG.OFF, ip);
                    }
                    //6. set data return
                    result.code = ErrorCode.SUCCESS;
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
