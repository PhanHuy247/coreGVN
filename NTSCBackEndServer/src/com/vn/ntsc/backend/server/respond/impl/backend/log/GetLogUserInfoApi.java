/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.backend.server.respond.impl.backend.log;

import eazycommon.constant.ErrorCode;
import eazycommon.constant.ParamKey;
import eazycommon.util.Util;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.json.simple.JSONObject;
import com.vn.ntsc.backend.dao.log.LogUserInfoDAO;
import com.vn.ntsc.backend.dao.user.UserDAO;
import com.vn.ntsc.backend.entity.impl.datarespond.SizedListData;
import com.vn.ntsc.backend.entity.impl.log.LogUserInfo;
import com.vn.ntsc.backend.server.respond.IApiAdapter;
import com.vn.ntsc.backend.server.respond.Respond;
import com.vn.ntsc.backend.server.respond.result.EntityRespond;

/**
 *
 * @author Administrator
 */
public class GetLogUserInfoApi implements IApiAdapter{

    @Override
    public Respond execute(JSONObject obj) {
        Respond respond = new EntityRespond();
        try {
            String userId = (String) obj.get(ParamKey.ID);
//            Long userType = (Long) obj.get(ParamKey.USER_TYPE);
//            Long appId = (Long) obj.get(ParamKey.APPLICATION_ID);
            Long gender = (Long) obj.get(ParamKey.GENDER);
            String fromTime = (String) obj.get(ParamKey.FROM_TIME);
            String toTime = (String) obj.get(ParamKey.TO_TIME);
            Long skip = (Long) obj.get(ParamKey.SKIP);
            Long take = (Long) obj.get(ParamKey.TAKE);
            
            SizedListData result = LogUserInfoDAO.getLog(userId, gender, fromTime, toTime, skip, take);
            List<String> userIdList = new LinkedList<>();
            for (Object logObj : result.ll) {
                userIdList.add(((LogUserInfo) logObj).userId);
            }
            
            Map<String, String> mapUserName = UserDAO.getListName(userIdList);
            for (Object logObj : result.ll) {
                ((LogUserInfo) logObj).userName = mapUserName.get(((LogUserInfo) logObj).userId);
            }
        
            respond = new EntityRespond(ErrorCode.SUCCESS, result);
        }
        catch (Exception e){
            Util.addErrorLog(e);
        }
        return respond;
    }
      
}
