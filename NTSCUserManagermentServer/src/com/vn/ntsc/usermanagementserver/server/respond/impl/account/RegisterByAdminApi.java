/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.usermanagementserver.server.respond.impl.account;

import com.vn.ntsc.usermanagementserver.server.pointaction.ActionManager;
import com.vn.ntsc.usermanagementserver.server.pointaction.ActionType;
import com.vn.ntsc.usermanagementserver.dao.impl.LoginTrackingDAO;
import com.vn.ntsc.usermanagementserver.dao.impl.UserDAO;
import eazycommon.util.DateFormat;
import eazycommon.util.Util;
import java.util.Date;
import eazycommon.constant.Constant;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.ParamKey;
import eazycommon.exception.EazyException;
import org.json.simple.JSONObject;
import com.vn.ntsc.usermanagementserver.entity.impl.datarespond.LoginData;
import com.vn.ntsc.usermanagementserver.entity.impl.user.User;
import com.vn.ntsc.usermanagementserver.server.respond.IApiAdapter;
import com.vn.ntsc.usermanagementserver.server.respond.Respond;
import com.vn.ntsc.usermanagementserver.server.respond.common.UserInforValidator;
import com.vn.ntsc.usermanagementserver.server.respond.result.EntityRespond;
import com.vn.ntsc.usermanagementserver.server.tool.Tool;
import com.vn.ntsc.usermanagementserver.server.userinformanager.UserInforManager;

/**
 *
 * @author RuAc0n
 */
public class RegisterByAdminApi implements IApiAdapter {

    @Override
    public Respond execute(JSONObject obj, Date time) {
        EntityRespond result = new EntityRespond();
        try {
            
            User inputUser = User.getRegisterUserByAdmin(obj);
            
            if (!UserInforValidator.validateGender(inputUser)) {
                return new EntityRespond(ErrorCode.WRONG_DATA_FORMAT);
            }
            if (!UserInforValidator.validateBirthday(inputUser, Constant.FLAG.ON)) {
                return new EntityRespond(ErrorCode.INVALID_BIRTHDAY);
            }
            if ( inputUser.username == null || inputUser.username.trim().isEmpty() || !Tool.checkUserName(null, inputUser.username)) {
                result.code = ErrorCode.INVALID_USER_NAME;
                return result;
            }
            if(inputUser.region == null  || !UserInforValidator.validateRegion(inputUser, null)){
                return new EntityRespond(ErrorCode.WRONG_DATA_FORMAT);
            }
            
            if(inputUser.email != null){
                if (!Util.validateEmail(inputUser.email)) {
                    result.code = ErrorCode.INVALID_EMAIL;
                    return result;
                }else{
                    if (UserDAO.checkEmail(inputUser.email, null)) {
                        result.code = ErrorCode.EMAIL_REGISTED;
                        return result;
                    }
                }
            }
            else if(inputUser.fbId != null){
                if (UserDAO.checkFacebookId(inputUser.fbId, null)) {
                    result.code = ErrorCode.EMAIL_REGISTED;
                    return result;
                }
            }
            
            if(!UserInforValidator.validate(inputUser)){
                return new EntityRespond(ErrorCode.WRONG_DATA_FORMAT);
            }
            
            inputUser.lastUpdate = DateFormat.format(time);
            inputUser.registerDate = DateFormat.format(time);
//            inputUser.point = new Long(regPoint);

            User respondData = UserDAO.insertUser(inputUser);
            LoginData data = new LoginData();
            data.user = respondData;
            //add user point and user infor
            UserInforManager.add(respondData.userId, inputUser.gender.intValue());
//            UserInforManager.add(respondData);
            String ip = Util.getStringParam(obj, ParamKey.IP);
            ActionManager.doAction(ActionType.register, respondData.userId, null, Util.getGMTTime(), null, null, ip);
            LoginTrackingDAO.initialize(respondData.userId, respondData.gender.intValue());
            
            result = new EntityRespond(ErrorCode.SUCCESS, data);
        } catch (EazyException ex) {
            Util.addErrorLog(ex);
            result.code = ex.getErrorCode();
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
        return result;
    }

}
