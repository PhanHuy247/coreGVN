/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.jpns.server.manager.impl;

import eazycommon.constant.ParamKey;
import java.util.Date;
import org.json.simple.JSONObject;
import com.vn.ntsc.jpns.Core;
import com.vn.ntsc.jpns.server.manager.IApiAdapter;

/**
 *
 * @author Administrator
 */
public class UpdateUserInfoApi implements IApiAdapter{

    @Override
    public void execute(JSONObject obj, Date time) {
        String userid = (String) obj.get(ParamKey.USER_ID);
        String newName = (String) obj.get(ParamKey.USER_NAME);
        if (newName != null) {
            String oldName = Core.dao.getUsername(userid);
            if (!newName.equals(oldName)) {
                Core.dao.updateUserName(userid, newName);
            }
        }
    }
    
}
