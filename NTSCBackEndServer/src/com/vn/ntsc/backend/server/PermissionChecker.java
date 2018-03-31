/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.vn.ntsc.backend.server;

import java.util.List;
import eazycommon.constant.ParamKey;
import eazycommon.util.Util;
import org.json.simple.JSONObject;
import com.vn.ntsc.backend.dao.admin.AdminDAO;
import com.vn.ntsc.backend.dao.screen.ScreenDAO;
import com.vn.ntsc.backend.server.rolegroupmanagement.RoleGroupManager;
import com.vn.ntsc.backend.server.rolegroupmanagement.ScreenAPIManager;

/**
 *
 * @author RuAc0n
 */
public class PermissionChecker {
    
    public JSONObject inputJSON;

    public PermissionChecker(JSONObject inputJSON) {
        this.inputJSON = inputJSON;
    }
    
    public boolean validate(){
        boolean result = false;
        try{
            String api = Util.getStringParam(inputJSON, ParamKey.API_NAME);
            List<String> listScreenId = ScreenAPIManager.getScreen(api);
            if(listScreenId != null){
                for(String screenId : listScreenId){
                    String groupId = ScreenDAO.getGroupId(screenId);
                    String userId = Util.getStringParam(inputJSON, ParamKey.USER_ID);
                    String roleId = AdminDAO.getRoleId(userId);
                    result = RoleGroupManager.checkRole(groupId, roleId);
                    if(result)
                        break;
                }
            }
        }catch(Exception ex){
            Util.addErrorLog(ex);            
           
        }
        return result;
//        return true;
    }
    
}
