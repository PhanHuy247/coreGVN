/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.backend.server.respond.impl.backend.automessage.loginbonus;

import eazycommon.constant.ErrorCode;
import eazycommon.constant.ParamKey;
import eazycommon.util.Util;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.json.simple.JSONObject;
import com.vn.ntsc.backend.dao.admin.LoginBonusMessageDAO;
import com.vn.ntsc.backend.dao.user.UserDAO;
import com.vn.ntsc.backend.entity.IEntity;
import com.vn.ntsc.backend.entity.impl.datarespond.SizedListData;
import com.vn.ntsc.backend.entity.impl.user.SimpleUser;
import com.vn.ntsc.backend.entity.impl.user.User;
import com.vn.ntsc.backend.server.respond.IApiAdapter;
import com.vn.ntsc.backend.server.respond.Respond;
import com.vn.ntsc.backend.server.respond.result.EntityRespond;

/**
 *
 * @author HuyDX
 */
public class GetReceiversLoginBonusMessageApi implements IApiAdapter{

    @Override
    public Respond execute(JSONObject obj) {
        Respond response = new EntityRespond();
        try {
            String id = Util.getStringParam(obj, ParamKey.ID);
            Long skip = Util.getLongParam(obj, ParamKey.SKIP);
            Long take = Util.getLongParam(obj, ParamKey.TAKE);
            if (skip == null || take == null) {
                return null;
            }
            List<String> receivers = LoginBonusMessageDAO.getReceivers(id);
            
            int total = 0;
            List<String> friendList = new ArrayList<>();
            if(receivers != null){
                total = receivers.size();
                int  count = 1;
                int limit = (int) (skip + take);
                for(String friend: receivers){
                    if(count > limit)
                        break;
                    if(count > skip){
                        friendList.add(friend);
                    }
                    count ++;
                }
            }
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
            SizedListData data = new SizedListData(total, listData);
            response = new EntityRespond(ErrorCode.SUCCESS, data);
        }catch (Exception ex) {
            Util.addErrorLog(ex);
        }
        return response;
    }
    
}
