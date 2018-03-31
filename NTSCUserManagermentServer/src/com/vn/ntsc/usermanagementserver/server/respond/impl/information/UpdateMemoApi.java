/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.usermanagementserver.server.respond.impl.information;

import eazycommon.constant.ErrorCode;
import eazycommon.constant.ParamKey;
import eazycommon.util.Util;
import java.util.Date;
import org.json.simple.JSONObject;
import com.vn.ntsc.usermanagementserver.dao.impl.MemoDAO;
import com.vn.ntsc.usermanagementserver.server.respond.IApiAdapter;
import com.vn.ntsc.usermanagementserver.server.respond.Respond;

/**
 *
 * @author Administrator
 */
public class UpdateMemoApi implements IApiAdapter{
    
    private static final String memoKey = "memo";
    
    @Override
    public Respond execute(JSONObject obj, Date time) {
        Respond respond = null;
        try {
            String userId = (String) obj.get(ParamKey.USER_ID);
            String friendId = (String) obj.get(ParamKey.FRDID);
            String memo = (String) obj.get(memoKey);
            String[] friendIds = new String[2];
            if (friendId.contains(",")) {
                friendIds = friendId.split(",");
                for (String fr : friendIds) {
                    if (isValidate(userId, fr) && memo != null) {
                        MemoDAO.updateMemo(userId, fr, memo, time);
                        respond = new Respond(ErrorCode.SUCCESS);
                    } else {
                        respond = new Respond(ErrorCode.WRONG_DATA_FORMAT);
                    }
                }
            }else {
                if (isValidate(userId, friendId) && memo != null) {
                        MemoDAO.updateMemo(userId, friendId, memo, time);
                        respond = new Respond(ErrorCode.SUCCESS);
                    }
            }
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            respond = new Respond(ErrorCode.UNKNOWN_ERROR);
        }

        return respond;
    }
    
    public boolean isValidate(String userId, String friendId) {
        return isValidate(userId) && isValidate(friendId);
    }

    public boolean isValidate(String var) {
        return (var != null && !var.isEmpty());
    }
}
