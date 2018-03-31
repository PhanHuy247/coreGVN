/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.usermanagementserver.server.respond.impl.connection;

import com.vn.ntsc.usermanagementserver.server.respond.Respond;
import com.vn.ntsc.usermanagementserver.server.respond.IApiAdapter;
import com.vn.ntsc.usermanagementserver.dao.impl.PbImageDAO;
import com.vn.ntsc.usermanagementserver.dao.impl.UserDAO;
import eazycommon.util.Util;
import java.util.Date;
import java.util.List;
import eazycommon.constant.ErrorCode;
import eazycommon.exception.EazyException;
import org.json.simple.JSONObject;
import eazycommon.constant.ParamKey;
import com.vn.ntsc.usermanagementserver.entity.impl.image.PublicImage;
import com.vn.ntsc.usermanagementserver.server.blacklist.BlockUserManager;
import com.vn.ntsc.usermanagementserver.server.respond.result.ListEntityRespond;

/**
 *
 * @author RuAc0n
 */
public class ListPublicImageApi implements IApiAdapter {

    @Override
    public Respond execute(JSONObject obj, Date time) {
        ListEntityRespond result = new ListEntityRespond();
        try {
            String userId = Util.getStringParam(obj, ParamKey.USER_ID);
            long skip = Util.getLongParam(obj, ParamKey.SKIP);
            long take = Util.getLongParam(obj, ParamKey.TAKE);
            String friendId = Util.getStringParam(obj, ParamKey.REQUEST_USER_ID);
            List<PublicImage> listPbImage;
            if (friendId != null) {
                boolean checker = UserDAO.checkUser(friendId);
                if (checker) {
                    if(friendId.isEmpty() || userId==null){
                        listPbImage = PbImageDAO.getPublicImage(friendId, skip, take);
                        result = new ListEntityRespond( ErrorCode.SUCCESS, listPbImage);
                    }else if (!BlockUserManager.isBlock(userId, friendId)) {
                        listPbImage = PbImageDAO.getPublicImage(friendId, skip, take);
                        result = new ListEntityRespond( ErrorCode.SUCCESS, listPbImage);
                    } else {
                        result.code = ErrorCode.BLOCK_USER;
                    }
                } else {
                    result.code = ErrorCode.USER_NOT_EXIST;
                }
            } else {
                listPbImage = PbImageDAO.getPublicImage(userId, skip, take);
                result = new ListEntityRespond( ErrorCode.SUCCESS, listPbImage);
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
