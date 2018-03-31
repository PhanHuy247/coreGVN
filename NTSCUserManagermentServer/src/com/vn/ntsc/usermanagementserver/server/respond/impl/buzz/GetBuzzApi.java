/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.usermanagementserver.server.respond.impl.buzz;

import com.vn.ntsc.usermanagementserver.server.respond.Respond;
import com.vn.ntsc.usermanagementserver.server.respond.IApiAdapter;
import com.vn.ntsc.usermanagementserver.dao.impl.UserActivityDAO;
import com.vn.ntsc.usermanagementserver.dao.impl.FavoristDAO;
import com.vn.ntsc.usermanagementserver.dao.impl.UserDAO;
import eazycommon.util.Util;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import eazycommon.constant.Constant;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.ListPosition;
import eazycommon.constant.ParamKey;
import eazycommon.constant.StatePosition;
import eazycommon.exception.EazyException;
import org.json.simple.JSONObject;
import com.vn.ntsc.usermanagementserver.entity.impl.datarespond.GetBuzzData;
import com.vn.ntsc.usermanagementserver.server.blacklist.BlockUserManager;
import com.vn.ntsc.usermanagementserver.server.respond.result.EntityRespond;
import com.vn.ntsc.usermanagementserver.server.userinformanager.UserInforManager;
import java.util.LinkedList;

/**
 *
 * @author RuAc0n
 */
public class GetBuzzApi implements IApiAdapter {

    private static final int USER_BUZZ = 0;
    private static final int LOCAL_BUZZ = 1;
    private static final int FRIEND_BUZZ = 2;
    private static final int FAVOURIST_BUZZ = 3;

    @Override
    public Respond execute(JSONObject obj, Date time) {
        EntityRespond result = new EntityRespond();
        try {
            String userId = Util.getStringParam(obj, ParamKey.USER_ID);
            Long buzzKind = Util.getLongParam(obj, "buzz_kind");
            List<String> respondList = new ArrayList<>();
            List<String> blockList = new LinkedList<>();
            List<String> friendList = new LinkedList<>();
            
            if (userId != null){
                if (buzzKind != null && buzzKind == LOCAL_BUZZ && userId == null){
                    respondList = UserInforManager.getAllUser();
                }
                blockList = BlockUserManager.getBlackList(userId);
                friendList = FavoristDAO.getFavouristList(userId);
            }else{
                respondList = UserInforManager.getAllUser();
            }
            
            GetBuzzData data = new GetBuzzData(respondList, blockList, friendList);
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
