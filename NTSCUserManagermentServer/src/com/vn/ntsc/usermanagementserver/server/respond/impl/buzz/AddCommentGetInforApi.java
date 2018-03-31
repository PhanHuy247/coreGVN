/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.vn.ntsc.usermanagementserver.server.respond.impl.buzz;

import com.vn.ntsc.usermanagementserver.server.respond.Respond;
import com.vn.ntsc.usermanagementserver.server.respond.IApiAdapter;
import com.vn.ntsc.usermanagementserver.dao.impl.UserBuzzDAO;
import eazycommon.util.Util;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import eazycommon.constant.ErrorCode;
import org.json.simple.JSONObject;
import eazycommon.constant.ParamKey;
import com.vn.ntsc.usermanagementserver.entity.impl.datarespond.AddCommentInforData;
import com.vn.ntsc.usermanagementserver.server.blacklist.BlockUserManager;
import com.vn.ntsc.usermanagementserver.server.blacklist.DeactivateUserManager;
import com.vn.ntsc.usermanagementserver.server.pointaction.ActionType;
import com.vn.ntsc.usermanagementserver.server.pointaction.ConnectionPrice;
import com.vn.ntsc.usermanagementserver.server.respond.result.EntityRespond;
import com.vn.ntsc.usermanagementserver.server.systemaccount.SystemAccountManager;
import com.vn.ntsc.usermanagementserver.server.userinformanager.UserInforManager;

/**
 *
 * @author RuAc0n
 */
public class AddCommentGetInforApi implements IApiAdapter{

    @Override
    public Respond execute(JSONObject obj, Date time) {
        EntityRespond result = new EntityRespond();
        try {
            String userId = Util.getStringParam(obj, ParamKey.USER_ID);
            String buzzId = Util.getStringParam(obj, ParamKey.BUZZ_ID);
            List<String> blockList = BlockUserManager.getBlackList(userId);
            blockList.addAll(SystemAccountManager.toList());
            Collection<String> deactiveList = null;
//            deactiveList = DeactivateUserManager.toList(); 
//            deactiveList.addAll();
//            deactiveList.addAll(SystemAccountManager.toList());
//            int point = UserInforManager.getPoint(userId);
//            int price = 0 - ConnectionPrice.getBadConnectionPrice(String.valueOf(ActionType.comment_buzz), userId).senderPrice;
//            String ownerId = UserBuzzDAO.getUserId(buzzId);
//            if(!ownerId.equals(userId) && point < price){
//                return new EntityRespond(ErrorCode.NOT_ENOUGHT_POINT, new AddCommentInforData(point));
//            }
            AddCommentInforData data = new AddCommentInforData(blockList, deactiveList, 0);
            result = new EntityRespond(ErrorCode.SUCCESS, data);
        } catch (Exception ex) {
            Util.addErrorLog(ex);            
        }
        return result;
    }
}
