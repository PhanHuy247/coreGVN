/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.vn.ntsc.usermanagementserver.server.respond.impl.information;

import java.util.Date;
import eazycommon.constant.Constant;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.ParamKey;
import eazycommon.util.Util;
import org.json.simple.JSONObject;
import com.vn.ntsc.usermanagementserver.dao.impl.UserDAO;
import com.vn.ntsc.usermanagementserver.entity.impl.datarespond.UpdateUserData;
import com.vn.ntsc.usermanagementserver.entity.impl.user.User;
import com.vn.ntsc.usermanagementserver.entity.impl.user.extend.Female;
import com.vn.ntsc.usermanagementserver.server.respond.IApiAdapter;
import com.vn.ntsc.usermanagementserver.server.respond.Respond;
import com.vn.ntsc.usermanagementserver.server.respond.result.EntityRespond;

/**
 *
 * @author Rua
 */
public class UpdateUserAgeStatusApi implements IApiAdapter{

    @Override
    public Respond execute(JSONObject obj, Date time) {
        Respond res = new EntityRespond();
        try{
            String userId = Util.getStringParam(obj, ParamKey.USERID);
            Long status = Util.getLongParam(obj, "age_status");
            if(status == null || userId == null){
                return res;
            }
            if(status < -2 || status > 1){
                return res;
            }
            Female beforeUser = (Female)UserDAO.getUserInfor(userId);
            User user = UserDAO.updateVerificationFlag(userId, status.intValue());
            if(user.flag == Constant.USER_STATUS_FLAG.ACTIVE){
                user.isActiveUser = Constant.FLAG.ON;
            }else{
                user.isActiveUser = Constant.FLAG.OFF;
            }
            boolean isVerify = true;
//            if(status != Constant.YES){
//                isVerify = false;
//            }
//            if(user.gender == Constant.FEMALE){
//                if(user.flag == Constant.ACTIVE && beforeUser.verificationFlag != Constant.APPROVED && status == Constant.APPROVED){
//                    List<String> blockList = BlockDAO.getBlackList(userId);
//                    Tool.active(userId, blockList);
//                }else if(user.flag == Constant.ACTIVE && beforeUser.verificationFlag == Constant.APPROVED && status != Constant.APPROVED){
//                    List<String> blockList = BlockDAO.getBlackList(userId);
//                    Tool.deactive(userId, blockList);
//                }
//            }
            // not care to status
            boolean isRemoveSession = true;
//            boolean isRemoveSession = false;
//            if(beforeUser.verificationFlag == Constant.APPROVED && status != Constant.APPROVED)
//                isRemoveSession = true;
//            else if(beforeUser.verificationFlag != Constant.APPROVED && status == Constant.APPROVED){
//                isRemoveSession = true;
//            }
            res = new EntityRespond(ErrorCode.SUCCESS, new UpdateUserData(user.finishRegisterFlag.intValue(), isVerify, isRemoveSession, user));
        }catch(Exception ex){
            Util.addErrorLog(ex);
        }
        return res;
    }
    
}
