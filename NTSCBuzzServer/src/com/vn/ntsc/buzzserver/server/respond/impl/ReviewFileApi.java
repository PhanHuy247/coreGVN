/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.buzzserver.server.respond.impl;

import com.vn.ntsc.buzzserver.dao.impl.BuzzDetailDAO;
import com.vn.ntsc.buzzserver.dao.impl.PbAudioDAO;
import com.vn.ntsc.buzzserver.dao.impl.PbVideoDAO;
import com.vn.ntsc.buzzserver.dao.impl.UserBuzzDAO;
import com.vn.ntsc.buzzserver.entity.impl.buzz.Buzz;
import com.vn.ntsc.buzzserver.entity.impl.datarespond.DeleteBuzzData;
import com.vn.ntsc.buzzserver.entity.impl.datarespond.LikeData;
import com.vn.ntsc.buzzserver.server.respond.IApiAdapter;
import com.vn.ntsc.buzzserver.server.respond.Respond;
import com.vn.ntsc.buzzserver.server.respond.result.EntityRespond;
import com.vn.ntsc.buzzserver.server.respond.result.ListEntityRespond;
import eazycommon.constant.Constant;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.ParamKey;
import eazycommon.exception.EazyException;
import eazycommon.util.Util;
import java.util.ArrayList;
import java.util.List;
import org.json.simple.JSONObject;

/**
 *
 * @author hoangnh
 */
public class ReviewFileApi implements IApiAdapter{

    @Override
    public Respond execute(JSONObject obj, long time) {
        Respond result = new ListEntityRespond();
        List<DeleteBuzzData> listData = new ArrayList<>();
        try {
            String videoId = Util.getStringParam(obj, ParamKey.VIDEO_ID);
            Long type = Util.getLongParam(obj, ParamKey.VIDEO_STATUS);
            Integer isNoti = 0;
            Buzz buzz = BuzzDetailDAO.getBuzzByFileId(videoId);
            if(buzz.buzzId != null){
                if(buzz.isApp != type.intValue()){
                    isNoti = 1;
                }
                BuzzDetailDAO.updateApprovedFlag(buzz.buzzId, type.intValue());
                UserBuzzDAO.updateApproveFlag(buzz.userId, buzz.buzzId, type.intValue());
                
                Integer buzz_type = BuzzDetailDAO.getBuzzType(buzz.buzzId);
                if(buzz_type == Constant.BUZZ_TYPE_VALUE.VIDEO_STATUS){
                    PbVideoDAO.updateFlag(buzz.userId, videoId, type.intValue());
                }else if(buzz_type == Constant.BUZZ_TYPE_VALUE.AUDIO_STATUS){
                    PbAudioDAO.updateFlag(buzz.userId, videoId, type.intValue());
                }
                
                LikeData data = new LikeData();
                data.isNoti = isNoti;
                data.buzz_id = buzz.buzzId;
                data.buzz_owner_id = buzz.userId;
                result = new EntityRespond(ErrorCode.SUCCESS, data);
            }else{
                result = new EntityRespond(ErrorCode.SUCCESS);
            }
        } catch (EazyException ex) {
            Util.addErrorLog(ex);
            result.code = ex.getErrorCode();
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
        return result;
    }
    
}
