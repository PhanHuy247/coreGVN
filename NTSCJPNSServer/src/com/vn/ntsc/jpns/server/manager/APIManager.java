/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.jpns.server.manager;

import com.vn.ntsc.jpns.server.manager.impl.NotiCommentYourBuzzApi;
import eazycommon.constant.API;
import java.util.TreeMap;
import com.vn.ntsc.jpns.server.manager.impl.AutoMessageApi;
import com.vn.ntsc.jpns.server.manager.impl.NotiApproveBackstageApi;
import com.vn.ntsc.jpns.server.manager.impl.DeactiveApi;
import com.vn.ntsc.jpns.server.manager.impl.LogoutAPI;
import com.vn.ntsc.jpns.server.manager.impl.LoginAPI;
import com.vn.ntsc.jpns.server.manager.impl.NotiBackStageDeniedApi;
import com.vn.ntsc.jpns.server.manager.impl.NotiBuzzApprovedApi;
import com.vn.ntsc.jpns.server.manager.impl.NotiBuzzDeniedApi;
import com.vn.ntsc.jpns.server.manager.impl.NotiCallRequestApi;
import com.vn.ntsc.jpns.server.manager.impl.NotiDailyBonusApi;
import com.vn.ntsc.jpns.server.manager.impl.NotiHasTagInBuzzApi;
import com.vn.ntsc.jpns.server.manager.impl.NotiInviteFriend;
import com.vn.ntsc.jpns.server.manager.impl.NotiLikeYourBuzzApi;
import com.vn.ntsc.jpns.server.manager.impl.NotiMissCallApi;
import com.vn.ntsc.jpns.server.manager.impl.NotiNewBuzzFromFavoritedApi;
import com.vn.ntsc.jpns.server.manager.impl.NotiNewChatMessageApi;
import com.vn.ntsc.jpns.server.manager.impl.NotiNewLiveStreamFromFavoritedApi;
import com.vn.ntsc.jpns.server.manager.impl.NotiOnlineAlertApi;
import com.vn.ntsc.jpns.server.manager.impl.NotiPingApi;
import com.vn.ntsc.jpns.server.manager.impl.NotiRecordingFileApi;
import com.vn.ntsc.jpns.server.manager.impl.NotiReplyYourCommentApi;
import com.vn.ntsc.jpns.server.manager.impl.NotiShareMusic;
import com.vn.ntsc.jpns.server.manager.impl.PushNotificationApi;
import com.vn.ntsc.jpns.server.manager.impl.PushNotificationFromFreePageApi;
import com.vn.ntsc.jpns.server.manager.impl.ResetConfigApi;
import com.vn.ntsc.jpns.server.manager.impl.ResetPasswordApi;
import com.vn.ntsc.jpns.server.manager.impl.ReviewBuzzTextApi;
import com.vn.ntsc.jpns.server.manager.impl.ReviewUserApi;
import com.vn.ntsc.jpns.server.manager.impl.UnregisterNotiTokenApi;
import com.vn.ntsc.jpns.server.manager.impl.UpdateNotiTokenApi;
import com.vn.ntsc.jpns.server.manager.impl.UpdateUserInfoApi;



/**
 *
 * @author RuAc0n
 */
public class APIManager {

    public static final TreeMap<String, IApiAdapter> m = new TreeMap<>();

    static {
        m.put(API.RESET_CONFIG, new ResetConfigApi());
        
        m.put(API.RESET_PASSWORD, new ResetPasswordApi());
        m.put(API.UPDATE_USER_INFOR, new UpdateUserInfoApi());
        m.put(API.LOGIN, new LoginAPI());
        m.put(API.LOG_OUT, new LogoutAPI());
        m.put(API.DEACTIVATE, new DeactiveApi());
        m.put(API.UPDATE_NOTI_TOKEN, new UpdateNotiTokenApi());
        m.put(API.UNREGISTER_NOTI_TOKEN, new UnregisterNotiTokenApi());
        m.put(API.REVIEW_USER, new ReviewUserApi());
        m.put(API.NOTI_BACKSTAGE_APPROVE, new NotiApproveBackstageApi());
        m.put(API.NOTI_BACKSTAGE_DENIED, new NotiBackStageDeniedApi());
        
        m.put(API.NOTI_LIKE_YOUR_BUZZ, new NotiLikeYourBuzzApi());
        m.put(API.NOTI_BUZZ_DENIED, new NotiBuzzDeniedApi());
        m.put(API.NOTI_BUZZ_APPROVED, new NotiBuzzApprovedApi());
        m.put(API.NOTI_COMMENT_YOUR_BUZZ, new NotiCommentYourBuzzApi());
        m.put(API.NOTI_TEXT_BUZZ_APPROVED, new ReviewBuzzTextApi());
        m.put(API.NOTI_TEXT_BUZZ_DENIED, new ReviewBuzzTextApi());
        m.put(API.NOTI_COMMENT_APPROVED, new ReviewBuzzTextApi());
        m.put(API.NOTI_COMMENT_DENIED, new ReviewBuzzTextApi());
        m.put(API.NOTI_SUB_COMMENT_APPROVED, new ReviewBuzzTextApi());
        m.put(API.NOTI_SUB_COMMENT_DENIED, new ReviewBuzzTextApi());
        
        m.put(API.NOTI_NEW_CHAT_MSG_AUDIO, new NotiNewChatMessageApi());
        m.put(API.NOTI_NEW_CHAT_MSG_LOCATION, new NotiNewChatMessageApi());
        m.put(API.NOTI_NEW_CHAT_MSG_PHOTO, new NotiNewChatMessageApi());
        m.put(API.NOTI_NEW_CHAT_MSG_STICKER, new NotiNewChatMessageApi());
        m.put(API.NOTI_NEW_CHAT_MSG_TEXT, new NotiNewChatMessageApi());
        m.put(API.NOTI_NEW_CHAT_MSG_VIDEO, new NotiNewChatMessageApi());
        m.put(API.NOTI_NEW_CHAT_MSG_WINK, new NotiNewChatMessageApi());
        m.put(API.NOTI_GAVE_GIFT, new NotiNewChatMessageApi());
        m.put(API.NOTI_ONLINE_ALERT, new NotiOnlineAlertApi());
        m.put(API.NOTI_DAILY_BONUS, new NotiDailyBonusApi());
        m.put(API.NOTI_PING, new NotiPingApi());
        m.put(API.NOTI_NEW_BUZZ_FROM_FAVORIST, new NotiNewBuzzFromFavoritedApi());
        m.put(API.NOTI_LIVESTREAM_FROM_FAVOURIST, new NotiNewLiveStreamFromFavoritedApi());
        m.put(API.NOTI_HAS_TAG_IN_BUZZ, new NotiHasTagInBuzzApi());
        m.put(API.NOTI_TAG_LIVESTREAM_FROM_FAVOURIST, new NotiHasTagInBuzzApi());
        m.put(API.NOTI_SHARE_MUSIC, new NotiShareMusic());
        m.put(API.NOTI_REPLY_YOUR_COMMENT, new NotiReplyYourCommentApi());
        m.put(API.NOTI_CALL_REQUEST, new NotiCallRequestApi());
        m.put(API.AUTO_MESSAGE, new AutoMessageApi());
        m.put(API.PUSH_NOTIFICATION, new PushNotificationApi());
        m.put(API.PUSH_NOTIFICATION_FROM_FREE_PAGE, new PushNotificationFromFreePageApi());
        
        m.put(API.NOTI_MISS_CALL, new NotiMissCallApi());
        m.put(API.NOTI_INVITE_FRIEND, new NotiInviteFriend());
        m.put(API.NOTI_RECORDING_FILE, new NotiRecordingFileApi());
    }

    public static IApiAdapter getApi(String apiName) {
        return m.get(apiName);
    }
}
