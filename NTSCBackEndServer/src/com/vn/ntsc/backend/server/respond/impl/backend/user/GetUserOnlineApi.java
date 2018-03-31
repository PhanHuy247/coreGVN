/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.vn.ntsc.backend.server.respond.impl.backend.user;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.json.simple.JSONObject;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.ParamKey;
import eazycommon.exception.EazyException;
import com.vn.ntsc.backend.dao.user.UserDAO;
import com.vn.ntsc.backend.entity.IEntity;
import com.vn.ntsc.backend.entity.impl.user.SimpleUser;
import com.vn.ntsc.backend.entity.impl.user.User;
import com.vn.ntsc.backend.server.respond.IApiAdapter;
import com.vn.ntsc.backend.server.respond.Respond;
import com.vn.ntsc.backend.server.respond.result.EntityRespond;
import eazycommon.util.Util;
import com.vn.ntsc.backend.entity.impl.datarespond.UserOnlineData;

/**
 *
 * @author RuAc0n
 */
public class GetUserOnlineApi implements  IApiAdapter{

    @Override
    public Respond execute(JSONObject obj) {
        EntityRespond respond = new EntityRespond();
        try{
//            System.out.println("obj : " + obj.toJSONString());
            Long total = Util.getLongParam(obj, ParamKey.TOTAL);
            Long male = Util.getLongParam(obj, "male");
            Long female = Util.getLongParam(obj, "female");
            Long videoCall = Util.getLongParam(obj, "video_call");
            Long voiceCall = Util.getLongParam(obj, "voice_call");
            List<String> friendList = Util.getListString(obj, ParamKey.FRIEND_LIST);
            Map<String, IEntity> map = UserDAO.getMapLogInfor(friendList);
            List<SimpleUser> listData = new ArrayList<>();
            for (Map.Entry<String, IEntity> pair : map.entrySet()) {
                IEntity user = pair.getValue();
                User u = (User) user;
                String userId = u.userId;
                String username = u.username;
                String cmCode = u.cmCode;
                Long userType = u.userType;
                String email = u.email;
                listData.add(new SimpleUser(userId, username, email, userType, cmCode, u.videoCall, u.voiceCall));
            }            
            UserOnlineData data = new UserOnlineData(total.intValue(),female.intValue(), male.intValue(), voiceCall.intValue(), videoCall.intValue(), listData);
            respond = new EntityRespond(ErrorCode.SUCCESS, data);
        }catch(EazyException ex){
            Util.addErrorLog(ex);            
            respond = new EntityRespond(ex.getErrorCode());
        }catch(Exception ex){
            Util.addErrorLog(ex);            
        }
        return respond;
    }     
}
