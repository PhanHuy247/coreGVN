/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.usermanagementserver.server.respond.impl.information;

import eazycommon.util.Util;
import java.util.Date;
import java.util.List;
import org.json.simple.JSONObject;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.ParamKey;
import eazycommon.exception.EazyException;
import com.vn.ntsc.usermanagementserver.dao.impl.UserDAO;
import com.vn.ntsc.usermanagementserver.dao.impl.log.LogCallDAO;
import com.vn.ntsc.usermanagementserver.entity.impl.datarespond.LogCallData;
import com.vn.ntsc.usermanagementserver.server.respond.IApiAdapter;
import com.vn.ntsc.usermanagementserver.server.respond.Respond;
import com.vn.ntsc.usermanagementserver.server.respond.result.ListEntityRespond;
import com.vn.ntsc.usermanagementserver.server.blacklist.BlockUserManager;

/**
 *
 * @author Admin
 */
public class GetCallLog implements IApiAdapter {

    @Override
    public Respond execute(JSONObject obj, Date time) {
        ListEntityRespond respond = new ListEntityRespond();
        try {
            String userId = Util.getStringParam(obj, ParamKey.USER_ID);
            Long skip = Util.getLongParam(obj, ParamKey.SKIP);
            Long take = Util.getLongParam(obj, ParamKey.TAKE);
            Long type = Util.getLongParam(obj, ParamKey.TYPE);
            if (UserDAO.isExists(userId)) {
                List<String> blockUsers = BlockUserManager.getBlackList(userId);
//                Collection<String> deactiveUsers = BlackListManager.toList();
//                blackList.addAll(BlockDAO.getBlackList(userId));
                List<LogCallData> list = LogCallDAO.getCallLog(userId, type.intValue(), blockUsers, skip.intValue(), take.intValue());
                respond = new ListEntityRespond(ErrorCode.SUCCESS, list);
            }
        } catch (EazyException ex) {
            Util.addErrorLog(ex);
        }

        return respond;
    }

}
