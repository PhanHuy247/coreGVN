/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.vn.ntsc.usermanagementserver.server.respond.impl.information;

import com.vn.ntsc.usermanagementserver.server.respond.Respond;
import com.vn.ntsc.usermanagementserver.server.respond.IApiAdapter;
import eazycommon.util.Util;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.ParamKey;
import org.json.simple.JSONObject;
import com.vn.ntsc.usermanagementserver.entity.impl.datarespond.BlackListData;
import com.vn.ntsc.usermanagementserver.server.blacklist.BlockUserManager;
import com.vn.ntsc.usermanagementserver.server.blacklist.DeactivateUserManager;
import com.vn.ntsc.usermanagementserver.server.respond.result.EntityRespond;
import com.vn.ntsc.usermanagementserver.server.systemaccount.SystemAccountManager;
import com.vn.ntsc.usermanagementserver.server.userinformanager.UserInforManager;

/**
 *
 * @author RuAc0n
 */
public class GetBlackListApi implements IApiAdapter{

    @Override
    public Respond execute(JSONObject obj, Date time) {
        EntityRespond result = new EntityRespond();
        try {
            String userId = Util.getStringParam(obj, ParamKey.USER_ID);
            
            long getBlockListStart = System.currentTimeMillis();
            List<String> blockList = BlockUserManager.getBlackList(userId);
            long getBlockListEnd = System.currentTimeMillis();
            getBlockListEnd -= getBlockListStart;
            if(getBlockListEnd > 1500){
                Util.addInfoLog("GetBackList API : get block list slow");
            }
            
            long getSystemAccountListStart = System.currentTimeMillis();
            blockList.addAll(SystemAccountManager.toList());
            long getSystemAccountListEnd = System.currentTimeMillis();
            getSystemAccountListEnd -= getSystemAccountListStart;
            if(getSystemAccountListEnd > 1500){
                Util.addInfoLog("GetBackList API : get system account list slow");
            }
            
            long getDeactivateListStart = System.currentTimeMillis();
            Collection<String> deactiveList = DeactivateUserManager.toList();
            long getDeactivateListEnd = System.currentTimeMillis();
            getDeactivateListEnd -= getDeactivateListStart;
            if(getDeactivateListEnd > 1500){
                Util.addInfoLog("GetBackList API : get deactivate list slow");
            }
            
            BlackListData data = new BlackListData(blockList, deactiveList, UserInforManager.getGender(userId));
            result = new EntityRespond(ErrorCode.SUCCESS, data);
        } catch (Exception ex) {
            Util.addErrorLog(ex);            
        }
        return result;
    }
}
