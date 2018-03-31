/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.usermanagementserver.server.respond.impl.account;

import com.vn.ntsc.usermanagementserver.server.respond.Respond;
import com.vn.ntsc.usermanagementserver.server.respond.IApiAdapter;
import com.vn.ntsc.usermanagementserver.dao.impl.UserActivityDAO;
import com.vn.ntsc.usermanagementserver.dao.impl.DeactivateDAO;
import com.vn.ntsc.usermanagementserver.dao.impl.UserDAO;
import eazycommon.util.DateFormat;
import eazycommon.util.Util;
import java.util.Date;
import java.util.List;
import eazycommon.constant.Constant;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.ParamKey;
import eazycommon.exception.EazyException;
import org.json.simple.JSONObject;
import com.vn.ntsc.usermanagementserver.entity.impl.datarespond.LoginData;
import com.vn.ntsc.usermanagementserver.entity.impl.user.User;
import com.vn.ntsc.usermanagementserver.server.blacklist.BlockUserManager;
import com.vn.ntsc.usermanagementserver.server.respond.common.Helper;
import com.vn.ntsc.usermanagementserver.server.respond.result.EntityRespond;
import com.vn.ntsc.usermanagementserver.server.tool.Tool;

/**
 *
 * @author RuAc0n
 */
public class ChangePasswordForgotApi implements IApiAdapter {

    @Override
    public Respond execute(JSONObject obj, Date time) {
        EntityRespond result = new EntityRespond();
        try {
            String email = Util.getStringParam(obj, ParamKey.EMAIL);
            String code = Util.getStringParam(obj, "vrf_code");
            String newPass = Util.getStringParam(obj, ParamKey.NEW_PASSWORD);
            String originalPass = Util.getStringParam(obj, ParamKey.ORIGINAL_PASSWORD);
            boolean isCorrectCode = UserDAO.checkVerificationCode(email, code);
           // boolean isCorrectCode = true;
            if (isCorrectCode) {
                User user = UserDAO.changePasswordInCaseForgot(email, newPass, originalPass, time);
                LoginData data = new LoginData();
                data.user = user;
                List<String> blockList = BlockUserManager.getBlackList(user.userId);
//                Collection<String> deactiveUser = BlackListManager.toList();
//                blackList.addAll(blockList);
//                blackList.addAll(BlackListManager.toList());
                data.blackList = blockList;
                data.setting = Helper.setSetting(user.userId);

                String userId = user.userId;
                //Helper.setUnlockSetting(userId, user, blackList);
                Helper.getAttentionNumber(data);
                Helper.setNotificationNumber(userId, blockList, data);
                data.isVerify = Helper.isVerify(user);
                String loginTime = Util.getStringParam(obj, ParamKey.LOGIN_TIME);
                Date loginDate = DateFormat.parse(loginTime);

                if(data.isVerify){
                    Date lastLogin = UserActivityDAO.getLastOnline(userId);
                    UserDAO.updateLoginTime(userId, time);
                    String ip = Util.getStringParam(obj, ParamKey.IP);
                    loginDate = Helper.addLoginTime(loginDate, time,lastLogin, userId, ip);
                    Helper.checkDailyBonus(userId, loginDate, time, user, data, lastLogin, ip);
                }
                if(user.isActiveUser != null && user.isActiveUser == Constant.FLAG.ON){
                    UserDAO.updateFlag(userId, Constant.FLAG.ON);
                    DeactivateDAO.activate(userId);
                    Tool.active(userId, blockList);
                }

                result = new EntityRespond(ErrorCode.SUCCESS, data);
            }
        } catch (EazyException ex) {
            Util.addErrorLog(ex);
            return new Respond(ex.getErrorCode());
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
        return result;
    }

}
