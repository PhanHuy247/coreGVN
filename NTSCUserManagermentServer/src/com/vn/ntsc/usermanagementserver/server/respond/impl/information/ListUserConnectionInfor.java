/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.usermanagementserver.server.respond.impl.information;

import com.vn.ntsc.usermanagementserver.server.respond.Respond;
import com.vn.ntsc.usermanagementserver.server.respond.IApiAdapter;
import com.vn.ntsc.usermanagementserver.dao.impl.FavoristDAO;
import com.vn.ntsc.usermanagementserver.dao.impl.ContactTrackingDAO;
import com.vn.ntsc.usermanagementserver.dao.impl.MemoDAO;
import com.vn.ntsc.usermanagementserver.dao.impl.OnlineAlertDAO;
import com.vn.ntsc.usermanagementserver.dao.impl.UserDAO;
import eazycommon.constant.Constant;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.ParamKey;
import eazycommon.exception.EazyException;
import eazycommon.util.Util;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.json.simple.JSONObject;
import com.vn.ntsc.usermanagementserver.entity.impl.user.ConnectionInfor;
import com.vn.ntsc.usermanagementserver.entity.impl.user.User;
import com.vn.ntsc.usermanagementserver.server.pointaction.ActionType;
import com.vn.ntsc.usermanagementserver.server.pointaction.ConnectionPrice;
import com.vn.ntsc.usermanagementserver.server.respond.result.ListEntityRespond;
import com.vn.ntsc.usermanagementserver.server.tool.Tool;
import com.vn.ntsc.usermanagementserver.setting.Setting;
import java.util.HashMap;
import java.util.LinkedList;

/**
 *
 * @author RuAc0n
 */
public class ListUserConnectionInfor implements IApiAdapter {

    @Override
    public Respond execute(JSONObject obj, Date time) {
        Respond result = new Respond();
        try {
            String userId = Util.getStringParam(obj, ParamKey.USER_ID);
            List<String> friends = Util.getListString(obj, ParamKey.LIST_ID);
            //1. get List Friend id
            List<String> favouristList = new LinkedList<>();
            List<String> contactedUsers = new LinkedList<>();
            Map<String, String> memos = new HashMap<>();
            if (userId != null && !userId.isEmpty()){
                favouristList = FavoristDAO.getFavouristList(userId);
                contactedUsers = ContactTrackingDAO.getContactedUsers(userId);
                Tool.removeBlackList(favouristList, userId);
                memos = MemoDAO.getMemos(userId); //khanhdd 06/01/2017
            }
            //2. get List Friend detail
            List<User> friendList = UserDAO.getListUser(friends);
//            List<String> favouristList = new ArrayList<>();
//            for(User user: friendList){
//                favouristList.add(user.userId);
//            }
            
            List<ConnectionInfor> respondList = new ArrayList<>();
            for(User user : friendList){
                ConnectionInfor infor = new ConnectionInfor();
                String id = user.userId;
                infor.requestId = id;
                infor.job = user.job;
                
                infor.isFavourist = favouristList.contains(id) ? (long)Constant.FLAG.ON : (long)Constant.FLAG.OFF;
                infor.isContacted = contactedUsers.contains(id);
                infor.memo = memos.get(id); //khanhdd 06/01/2017
                
                if (userId != null && userId.isEmpty()){
                    infor.subCommentPoint = 0 -  ConnectionPrice.getConnectionPrice(String.valueOf(ActionType.reply_comment), userId, id).senderPrice;
                    infor.commentBuzzPoint = 0 -  ConnectionPrice.getConnectionPrice(String.valueOf(ActionType.comment_buzz), userId, id).senderPrice;
                    infor.chatPoint = 0 - ConnectionPrice.getConnectionPrice(String.valueOf(ActionType.chat), userId, id).senderPrice;
                    infor.listenAudioPoint = 0 - ConnectionPrice.getConnectionPrice(String.valueOf(ActionType.listen_audio), userId, id).senderPrice;
                    infor.viewImagePoint = 0 - ConnectionPrice.getConnectionPrice(String.valueOf(ActionType.view_image), userId, id).senderPrice;
                    infor.watchVideoPoint = 0 - ConnectionPrice.getConnectionPrice(String.valueOf(ActionType.watch_video), userId, id).senderPrice;
                }
                
                infor.listenAudioTime = Setting.LISTEN_AUDIO_TIME;
                infor.viewImageTime = Setting.VIEW_IMAGE_TIME;
                infor.watchVideoTime = Setting.WATCH_VIDEO_TIME;
                respondList.add(infor);
            }
            for(ConnectionInfor user: respondList){
                String friendId = user.requestId;
                Long isAlert = (long) Constant.FLAG.OFF;
                boolean checkAlert = OnlineAlertDAO.checkAlert(friendId, userId);
                if (checkAlert) {
                    isAlert = new Long(Constant.FLAG.ON);
                }
                user.isAlert = isAlert.intValue();
            }
            
            result = new ListEntityRespond(ErrorCode.SUCCESS, respondList);
        } catch (EazyException ex) {
            Util.addErrorLog(ex);
            result.code = ex.getErrorCode();
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
        return result;
    }

}
