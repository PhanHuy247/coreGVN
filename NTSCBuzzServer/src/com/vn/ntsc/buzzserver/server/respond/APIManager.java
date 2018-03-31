/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.buzzserver.server.respond;

import com.vn.ntsc.buzzserver.server.respond.impl.ActiveApi;
import com.vn.ntsc.buzzserver.server.respond.impl.AddBuzzApi;
import com.vn.ntsc.buzzserver.server.respond.impl.AddCommentApi;
import com.vn.ntsc.buzzserver.server.respond.impl.AddNumberOfShareApi;
import com.vn.ntsc.buzzserver.server.respond.impl.AddNumberOfViewApi;
import com.vn.ntsc.buzzserver.server.respond.impl.AddSubCommentApi;
import com.vn.ntsc.buzzserver.server.respond.impl.AddTagApi;
import com.vn.ntsc.buzzserver.server.respond.impl.CheckBuzzSocket;
import com.vn.ntsc.buzzserver.server.respond.impl.DeactiveApi;
import com.vn.ntsc.buzzserver.server.respond.impl.DeleteBuzzApi;
import com.vn.ntsc.buzzserver.server.respond.impl.DeleteCommentApi;
import com.vn.ntsc.buzzserver.server.respond.impl.DeleteSubCommentApi;
import com.vn.ntsc.buzzserver.server.respond.impl.GetBuzzApi;
import com.vn.ntsc.buzzserver.server.respond.impl.GetBuzzDetailApi;
import com.vn.ntsc.buzzserver.server.respond.impl.GetBuzzNumberApi;
import com.vn.ntsc.buzzserver.server.respond.impl.GetMediaBuzzDataApi;
import com.vn.ntsc.buzzserver.server.respond.impl.LikeBuzzApi;
import com.vn.ntsc.buzzserver.server.respond.impl.ListCommentApi;
import com.vn.ntsc.buzzserver.server.respond.impl.ListSubCommentApi;
import com.vn.ntsc.buzzserver.server.respond.impl.ReportBuzzApi;
import com.vn.ntsc.buzzserver.server.respond.impl.ResetConfigApi;
import com.vn.ntsc.buzzserver.server.respond.impl.ReviewFileApi;
import com.vn.ntsc.buzzserver.server.respond.impl.SendGiftApi;
import com.vn.ntsc.buzzserver.server.respond.impl.UpdateOtherSettingApi;
import com.vn.ntsc.buzzserver.server.respond.impl.UpdateStreamStatusApi;
import com.vn.ntsc.buzzserver.server.respond.impl.UpdateStreamVideoApi;
import com.vn.ntsc.buzzserver.server.respond.impl.UpdateTag;
import com.vn.ntsc.buzzserver.server.respond.impl.UpdateUploadSetting;
import java.util.TreeMap;
import eazycommon.constant.API;


/**
 *
 * @author RuAc0n
 */
public class APIManager {

    private static final TreeMap<String, IApiAdapter> m = new TreeMap<>();

    static {
        m.put(API.RESET_CONFIG, new ResetConfigApi());

        m.put(API.GET_BUZZ, new GetBuzzApi());
        m.put(API.GET_BUZZ_DETAIL, new GetBuzzDetailApi());
        m.put(API.LIKE_BUZZ, new LikeBuzzApi());
        m.put(API.ADD_BUZZ, new AddBuzzApi());
        m.put(API.DELETE_BUZZ, new DeleteBuzzApi());
        
        m.put(API.ADD_COMMENT, new AddCommentApi());
        m.put(API.DELETE_COMMENT, new DeleteCommentApi());
        m.put(API.SEND_GIFT, new SendGiftApi());
        m.put(API.LIST_COMMENT, new ListCommentApi());
        m.put(API.ADD_SUB_COMMENT, new AddSubCommentApi());
        m.put(API.LIST_SUB_COMMENT, new ListSubCommentApi());
        m.put(API.DELETE_SUB_COMMENT, new DeleteSubCommentApi());
        
        m.put(API.REPORT, new ReportBuzzApi());
        m.put(API.DEACTIVATE, new DeactiveApi());
        m.put(API.ACTIVATE, new ActiveApi());
        
        m.put(API.SET_OTHER_SETTING, new UpdateOtherSettingApi());
        m.put(API.SET_UPLOAD_SETTING, new UpdateUploadSetting());
        m.put(API.GET_BUZZ_NUMBER, new GetBuzzNumberApi());
        m.put(API.UPLOAD_STREAM_FILE, new UpdateStreamVideoApi());
        m.put(API.UPDATE_TAG, new UpdateTag());
        m.put(API.ADD_TAG, new AddTagApi());
        m.put(API.START_STREAM_SERVER, new UpdateStreamStatusApi());
        
        m.put(API.GET_MEDIA_BUZZ_DATA, new GetMediaBuzzDataApi());
        m.put(API.ADD_NUMBER_OF_SHARE, new AddNumberOfShareApi());
        m.put(API.ADD_NUMBER_OF_VIEW, new AddNumberOfViewApi());
        m.put(API.CHECK_BUZZ_WEBSOCKET, new CheckBuzzSocket());
        
        m.put(API.REVIEW_VIDEO, new ReviewFileApi());
    }

   public static IApiAdapter getApi(String apiName) {
        return m.get(apiName);
    }
}
