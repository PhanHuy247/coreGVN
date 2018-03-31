/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.vn.ntsc.usermanagementserver.server.respond.impl.account;

import com.vn.ntsc.usermanagementserver.server.respond.Respond;
import com.vn.ntsc.usermanagementserver.server.respond.IApiAdapter;
import com.vn.ntsc.usermanagementserver.dao.impl.DeactivateDAO;
import com.vn.ntsc.usermanagementserver.dao.impl.UserDAO;
import eazycommon.util.Util;
import java.util.Date;
import java.util.List;
import eazycommon.constant.Constant;
import eazycommon.constant.ErrorCode;
import eazycommon.exception.EazyException;
import org.json.simple.JSONObject;
import eazycommon.constant.ParamKey;
import com.vn.ntsc.usermanagementserver.dao.impl.log.LogDeactivateDAO;
import com.vn.ntsc.usermanagementserver.server.blacklist.BlockUserManager;
import com.vn.ntsc.usermanagementserver.server.blacklist.DeactivateUserManager;
import com.vn.ntsc.usermanagementserver.server.tool.Tool;

/**
 *
 * @author RuAc0n
 */
public class DeactiveApi implements IApiAdapter{

    @Override
    public Respond execute(JSONObject obj, Date time) {
        Respond result = new Respond();
        try {
            String userId = Util.getStringParam(obj, ParamKey.USER_ID);
            String comment = Util.getStringParam(obj, "cmt");
            String ip = Util.getStringParam(obj, ParamKey.IP);
            if (!DeactivateUserManager.isDeactivateUser(userId)) {
                List<String> blackList = BlockUserManager.getBlackList(userId);
                DeactivateDAO.addDeactiveUser(userId, comment, time.getTime());
                LogDeactivateDAO.addLog(userId, comment, time, ip);
                UserDAO.updateFlag(userId, Constant.FLAG.OFF);
                Tool.deactive(userId, blackList);
            }
            result.code = ErrorCode.SUCCESS;
        } catch (EazyException ex) {
            Util.addErrorLog(ex);            
            result.code = ex.getErrorCode();
        } catch (Exception ex) {
            Util.addErrorLog(ex);            
        }
        return result;
    }
    
}
