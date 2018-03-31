/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.buzzserver.server.respond.impl;

import com.vn.ntsc.buzzserver.blacklist.BlackListManager;
import com.vn.ntsc.buzzserver.dao.impl.BuzzDetailDAO;
import com.vn.ntsc.buzzserver.entity.impl.buzz.Buzz;
import com.vn.ntsc.buzzserver.server.respond.IApiAdapter;
import com.vn.ntsc.buzzserver.server.respond.Respond;
import com.vn.ntsc.buzzserver.server.respond.result.EntityRespond;
import eazycommon.constant.Constant;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.ParamKey;
import eazycommon.exception.EazyException;
import eazycommon.util.Util;
import java.util.List;
import org.json.simple.JSONObject;

/**
 *
 * @author Phan Huy
 */
public class CheckBuzzSocket implements IApiAdapter{

    @Override
    public Respond execute(JSONObject obj, long time) {
        EntityRespond result = new EntityRespond();
        try{
            List<String> blockList = Util.getListString(obj, ParamKey.BLOCK_LIST);
            List<String> friendList = Util.getListString(obj, ParamKey.FRIEND_LIST);
            String userId = Util.getStringParam(obj, ParamKey.USER_ID);
            String buzzId = Util.getStringParam(obj, ParamKey.BUZZ_ID);

             if (buzzId == null || buzzId.isEmpty()) {
                    result.code = ErrorCode.WRONG_DATA_FORMAT;
                    return result;
                }
                Buzz buzz = BuzzDetailDAO.getBuzzDetail(buzzId);

                Boolean isFriend = friendList.contains(userId);
                Boolean isCurrentUser = (buzz.userId.equals(userId));
                if (blockList.contains(buzz.userId)) {
    //                System.out.println("vao block");
                    result.code = ErrorCode.BLOCK_USER;
                    return result;
                }
                if (BlackListManager.isDeactivateUser(buzz.userId)) {
    //                System.out.println("vao deactive");
                    result.code = ErrorCode.USER_NOT_EXIST;
                    return result;
                }
                if (buzz.isApp == Constant.FLAG.OFF) {
                    result.code = ErrorCode.WATTING_APPROVE;
                    return result;
                }
                if ( (buzz.privacy == Constant.POST_MODE.FRIEND && !isFriend && !isCurrentUser) || (buzz.privacy == Constant.POST_MODE.ONLY_ME && !isCurrentUser) ){
                    result.code = ErrorCode.BLOCK_USER;
                    return result;
                }
                result = new EntityRespond(ErrorCode.SUCCESS);
        } catch (EazyException ex) {
            Util.addErrorLog(ex);
            result.code = ex.getErrorCode();
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
        return result;
    }
    
}
