/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.vn.ntsc.usermanagementserver.server.respond.impl.buzz;

import com.vn.ntsc.usermanagementserver.server.respond.Respond;
import com.vn.ntsc.usermanagementserver.server.respond.IApiAdapter;
import com.vn.ntsc.usermanagementserver.dao.impl.UserBuzzDAO;
import com.vn.ntsc.usermanagementserver.dao.impl.CommentDAO;
import eazycommon.util.Util;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.ParamKey;
import org.json.simple.JSONObject;
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
public class AddSubCommentGetInforApi implements IApiAdapter{

    @Override
    public Respond execute(JSONObject obj, Date time) {
        EntityRespond result = new EntityRespond();
        try {
            String userId = Util.getStringParam(obj, ParamKey.USER_ID);
            String buzzId = Util.getStringParam(obj, ParamKey.BUZZ_ID);
            String commentId = Util.getStringParam(obj, ParamKey.COMMENT_ID);
            List<String> blockList = BlockUserManager.getBlackList(userId);
            blockList.addAll(SystemAccountManager.toList());
            Collection<String> deactiveList = DeactivateUserManager.toList();
//            deactiveList.addAll(BlackListManager.toList());
//            deactiveList.addAll();
            int point = UserInforManager.getPoint(userId);
            String ownerId = UserBuzzDAO.getUserId(buzzId);
            String commenter = CommentDAO.getUserId(commentId);
            ConnectionPrice price = ConnectionPrice.getConnectionPrice(String.valueOf(ActionType.reply_comment), userId, commenter);
            int senderPrice = 0 - price.senderPrice;
            //if(!userId.equals(ownerId) && !userId.equals(commenter) && point < senderPrice){
            if(!userId.equals(ownerId) && point < senderPrice){
                return new EntityRespond(ErrorCode.NOT_ENOUGHT_POINT, new AddCommentInforData(point, null, senderPrice));
            }
            AddCommentInforData data = new AddCommentInforData(senderPrice, blockList, deactiveList, point);
            result = new EntityRespond(ErrorCode.SUCCESS, data);
        } catch (Exception ex) {
            Util.addErrorLog(ex);            
        }
        return result;
    }
}
