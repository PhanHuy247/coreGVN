/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.usermanagementserver.server.respond.impl.activity;

import com.vn.ntsc.usermanagementserver.dao.impl.BuzzDetailDAO;
import com.vn.ntsc.usermanagementserver.dao.impl.BuzzTag;
import com.vn.ntsc.usermanagementserver.dao.impl.FavoritedDAO;
import com.vn.ntsc.usermanagementserver.dao.impl.UserDAO;
import com.vn.ntsc.usermanagementserver.entity.impl.video.ConfirmUploadVideoData;
import com.vn.ntsc.usermanagementserver.server.respond.IApiAdapter;
import com.vn.ntsc.usermanagementserver.server.respond.Respond;
import com.vn.ntsc.usermanagementserver.server.respond.result.EntityRespond;
import com.vn.ntsc.usermanagementserver.server.tool.Tool;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.ParamKey;
import eazycommon.util.Util;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.json.simple.JSONObject;

/**
 *
 * @author hoangnh
 */
public class GetNotificationListApi implements IApiAdapter{

    @Override
    public Respond execute(JSONObject obj, Date time) {
        EntityRespond result = new EntityRespond();
        try {
            Util.addDebugLog("==========GetNotificationListApi==========");
            String userId = Util.getStringParam(obj, ParamKey.USER_ID);

            //buzzID 
            String buzzId = Util.getStringParam(obj, ParamKey.BUZZ_ID);
            List<String> listFavourited = new ArrayList<>();
            listFavourited = FavoritedDAO.getFavoristIdList(userId);
            Tool.removeBlackList(listFavourited, userId);
            
            String userName = UserDAO.getUserName(userId);
            
            //get streamId from buzzId
            String streamId  = BuzzDetailDAO.getStreamId(buzzId);
            List<String> listTag = BuzzTag.getListUserIdTag(buzzId);

            ConfirmUploadVideoData data = new ConfirmUploadVideoData();
            data.lstFav = listFavourited;
            data.userName = userName;
            if(streamId != null){
                data.streamId = streamId;
            }
            if(listTag != null){
                data.lstTag = listTag;
            }
            result = new EntityRespond(ErrorCode.SUCCESS, data);
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
        return result;
    }
    
}
