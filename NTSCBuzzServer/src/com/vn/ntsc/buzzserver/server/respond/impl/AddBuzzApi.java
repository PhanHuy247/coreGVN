/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.buzzserver.server.respond.impl;

import eazycommon.exception.EazyException;
import eazycommon.util.Util;
import org.json.simple.JSONObject;
import eazycommon.constant.Constant;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.ParamKey;
import com.vn.ntsc.buzzserver.Setting;
import com.vn.ntsc.buzzserver.dao.impl.BuzzCommentDAO;
import com.vn.ntsc.buzzserver.dao.impl.BuzzDetailDAO;
import com.vn.ntsc.buzzserver.dao.impl.BuzzTagDAO;
import com.vn.ntsc.buzzserver.dao.impl.CommentDAO;
import com.vn.ntsc.buzzserver.dao.impl.ReviewingBuzzDAO;
import com.vn.ntsc.buzzserver.dao.impl.ReviewingCommentDAO;
import com.vn.ntsc.buzzserver.dao.impl.StatusDAO;
import com.vn.ntsc.buzzserver.dao.impl.UserBuzzDAO;
import com.vn.ntsc.buzzserver.entity.impl.datarespond.AddBuzzData;
import com.vn.ntsc.buzzserver.server.respond.IApiAdapter;
import com.vn.ntsc.buzzserver.server.respond.Respond;
import com.vn.ntsc.buzzserver.server.respond.result.EntityRespond;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 *
 * @author RuAc0n
 */
public class AddBuzzApi implements IApiAdapter {

    @Override
    public Respond execute(JSONObject obj, long time) {
        EntityRespond result = new EntityRespond();
        try {
            String userId = Util.getStringParam(obj, ParamKey.USER_ID);
            String buzzVal = Util.getStringParam(obj, ParamKey.BUZZ_VALUE);
            Long buzzType = Util.getLongParam(obj, ParamKey.BUZZ_TYPE);
            Long isApp = Util.getLongParam(obj, ParamKey.IS_APPROVED_IMAGE);
            String ip = Util.getStringParam(obj, ParamKey.IP);
            Long privacy = Util.getLongParam(obj, ParamKey.PRIVACY);
            Long region = Util.getLongParam(obj, ParamKey.BUZZ_REGION);
            String streamId = "";
            String shareId = "";
            Util.addDebugLog("===========streamId========"+streamId);
            Util.addDebugLog("===========buzzType========"+buzzType);
            if(Util.getStringParam(obj, ParamKey.STREAM_ID) != null){
                streamId = Util.getStringParam(obj, ParamKey.STREAM_ID);
            }
            if(Util.getStringParam(obj, ParamKey.SHARE_ID) != null){
                shareId = Util.getStringParam(obj, ParamKey.SHARE_ID);
            }
            
            String buzzTag = Util.getStringParam(obj, ParamKey.TAG_LIST);
            
            JSONArray img_list = (JSONArray) obj.get(ParamKey.IMG_LIST);
//            Util.addDebugLog("===========img_list========"+img_list);
            JSONArray video_list = (JSONArray) obj.get(ParamKey.VID_LIST);
//            Util.addDebugLog("===========video_list========"+video_list);
            JSONArray audio_list = (JSONArray) obj.get(ParamKey.AUDIO_LIST);
            
            Util.addDebugLog("===========buzzTag========"+buzzTag);
            JSONParser parser = new JSONParser();
            JSONArray arrayTag = new JSONArray();
            if(buzzTag != null){
                try {
                    arrayTag = (JSONArray)parser.parse(buzzTag);
                } catch (ParseException ex) {
                    Logger.getLogger(AddBuzzApi.class.getName()).log(Level.SEVERE, null, ex);
                }
            }


//            Boolean isBuzzNull = (buzzType != Constant.BUZZ_TYPE_VALUE.MULTI_STATUS && (buzzVal == null || buzzVal.isEmpty()) );
//            
//            if (isBuzzNull || buzzType == null) {
//                result.code = ErrorCode.WRONG_DATA_FORMAT;
//                return result;
//            }
            int affFlag = Constant.FLAG.ON;
            String file = "";
            String img = "";
            List<String> img_id_list = new ArrayList();
            List<String> video_id_list = new ArrayList();
            List<String> audio_id_list = new ArrayList();
            
            if (buzzType == Constant.BUZZ_TYPE_VALUE.IMAGE_STATUS) {
//                affFlag = isApp.intValue();
                JSONObject temp = (JSONObject) img_list.get(0);
                file = (String) temp.get("data"); 
                affFlag = ((Long) temp.get("is_app")).intValue();
            }else if(buzzType == Constant.BUZZ_TYPE_VALUE.VIDEO_STATUS){
                JSONObject temp = (JSONObject) video_list.get(0);
                file = (String) temp.get("data"); 
                affFlag = ((Long) temp.get("is_app")).intValue();
            }else if(buzzType == Constant.BUZZ_TYPE_VALUE.TEXT_STATUS || buzzType == Constant.BUZZ_TYPE_VALUE.MULTI_STATUS){
                affFlag = Setting.auto_approve_buzz;
            }else if(buzzType == Constant.BUZZ_TYPE_VALUE.STREAM_STATUS || buzzType == Constant.BUZZ_TYPE_VALUE.SHARE_STATUS){
                affFlag = Constant.FLAG.ON;
            }else if(buzzType == Constant.BUZZ_TYPE_VALUE.AUDIO_STATUS){
                JSONObject temp = (JSONObject) audio_list.get(0);
                file = (String) temp.get("data"); 
                img = (String) temp.get("cover"); 
                affFlag = ((Long) temp.get("is_app")).intValue();
            }
            String id = BuzzDetailDAO.addBuzz(userId, buzzVal, buzzType.intValue(), time, affFlag, ip, file, privacy, img, streamId, shareId, region);

            BuzzTagDAO.addTagList(id, arrayTag);
            
            if (buzzType == Constant.BUZZ_TYPE_VALUE.IMAGE_STATUS){
                img_id_list.add(file);
                Util.addDebugLog("===========list child img buzz========"+img_id_list);
            }
            if(buzzType == Constant.BUZZ_TYPE_VALUE.VIDEO_STATUS){
                video_id_list.add(file);
            }
            if(buzzType == Constant.BUZZ_TYPE_VALUE.AUDIO_STATUS){
                audio_id_list.add(file);
            }
            
            if(buzzType == Constant.BUZZ_TYPE_VALUE.MULTI_STATUS){
                if(img_list != null){
                    Util.addDebugLog("===========add child img buzz========");
                    for (int i = 0; i < img_list.size(); i++) {
                        JSONObject temp = (JSONObject) img_list.get(i);
                        String text = (String) temp.get("text");
                        String url = (String) temp.get("data"); 
                        Long is_app = (Long) temp.get("is_app");
                        String item_id = BuzzDetailDAO.addChildBuzz(userId, text, Constant.BUZZ_TYPE_VALUE.IMAGE_STATUS, time, is_app.intValue(), ip, id, url, "", region);
                        img_id_list.add(url);
                    }
                    Util.addDebugLog("===========list child img buzz========"+img_id_list);
                }
                if(video_list != null){
                    Util.addDebugLog("===========add child video buzz========");
                    for (int i = 0; i < video_list.size(); i++) {
                        JSONObject temp = (JSONObject) video_list.get(i);
                        String text = (String) temp.get("text");
                        String url = (String) temp.get("data");
                        Long is_app = (Long) temp.get("is_app");
                        String item_id = BuzzDetailDAO.addChildBuzz(userId, text, Constant.BUZZ_TYPE_VALUE.VIDEO_STATUS, time, is_app.intValue(), ip, id, url, "", region);
                        video_id_list.add(url);
                    }
                    Util.addDebugLog("===========list child video buzz========"+video_id_list);
                }
                if(audio_list != null){
                    Util.addDebugLog("===========add child audio buzz========");
                    for (int i = 0; i < audio_list.size(); i++) {
                        JSONObject temp = (JSONObject) audio_list.get(i);
                        String text = (String) temp.get("text");
                        String url = (String) temp.get("data");
                        String cover = (String) temp.get("cover");
                        Long is_app = (Long) temp.get("is_app");
                        String item_id = BuzzDetailDAO.addChildBuzz(userId, text, Constant.BUZZ_TYPE_VALUE.AUDIO_STATUS, time, is_app.intValue(), ip, id, url, cover, region);
                        video_id_list.add(url);
                    }
                    Util.addDebugLog("===========list child audio buzz========"+video_id_list);
                }
            }

            if((buzzType == Constant.BUZZ_TYPE_VALUE.TEXT_STATUS || buzzType == Constant.BUZZ_TYPE_VALUE.MULTI_STATUS) && affFlag == Constant.FLAG.OFF){
                ReviewingBuzzDAO.addBuzz(id, userId, buzzVal, buzzType.intValue(), time, ip);
            }
            UserBuzzDAO.updateBuzzActivity(id, userId, time, affFlag, buzzType.intValue(), privacy, null);
            String cmtVal = Util.getStringParam(obj, "cmt_val");
            String cmtId = null;
            int approveCommentFlag = Constant.FLAG.OFF;
            if (cmtVal != null && !cmtVal.isEmpty()) {
                approveCommentFlag = Setting.auto_approve_comment;
                cmtId = CommentDAO.addComment(userId, id, cmtVal, approveCommentFlag,  Constant.FLAG.OFF, time);
                BuzzCommentDAO.addComment(id, cmtId, userId, approveCommentFlag);
                BuzzDetailDAO.addComment(id);
                if(approveCommentFlag == Constant.FLAG.OFF)
                    ReviewingCommentDAO.addComment(id, userId, cmtId, cmtVal, affFlag, time, ip);
            }
            if (buzzType == Constant.BUZZ_TYPE_VALUE.TEXT_STATUS) {
                StatusDAO.addStatus(id, userId);
            }
            Util.addDebugLog("===========list child video buzz========"+video_id_list);
//            AddBuzzData data = new AddBuzzData(id, cmtId, time, affFlag, approveCommentFlag);
            AddBuzzData data = new AddBuzzData(id, cmtId, time, affFlag, approveCommentFlag, img_id_list, video_id_list);
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
