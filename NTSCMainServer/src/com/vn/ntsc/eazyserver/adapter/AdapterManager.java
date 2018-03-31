/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.eazyserver.adapter;

import com.vn.ntsc.eazyserver.adapter.impl.AutoMessageAdapter;
import com.vn.ntsc.eazyserver.adapter.impl.BlockUserAdapter;
import com.vn.ntsc.eazyserver.adapter.impl.BuzzAdapter;
import com.vn.ntsc.eazyserver.adapter.impl.ChatLogAdapter;
import com.vn.ntsc.eazyserver.adapter.impl.ChatRoomAdapter;
import com.vn.ntsc.eazyserver.adapter.impl.DeactivateAdapter;
import com.vn.ntsc.eazyserver.adapter.impl.GetInformationForApplicationAdapter;
import com.vn.ntsc.eazyserver.adapter.impl.GetUserApplicationVersionAdapter;
import com.vn.ntsc.eazyserver.adapter.impl.LogoutAdapter;
import com.vn.ntsc.eazyserver.adapter.impl.MeetPeople;
import com.vn.ntsc.eazyserver.adapter.impl.MixServiceAdapter;
import com.vn.ntsc.eazyserver.adapter.impl.PushFromFreePageAdapter;
import com.vn.ntsc.eazyserver.adapter.impl.R1;
import com.vn.ntsc.eazyserver.adapter.impl.RegisterAdapter;
import com.vn.ntsc.eazyserver.adapter.impl.ReportAdapter;
import com.vn.ntsc.eazyserver.adapter.impl.ResetConfigAdapter;
import com.vn.ntsc.eazyserver.adapter.impl.SettingAdapter;
import com.vn.ntsc.eazyserver.adapter.impl.SipAdapter;
import com.vn.ntsc.eazyserver.adapter.impl.StickerAdapter;
import com.vn.ntsc.eazyserver.adapter.impl.TokenAdapter;
import com.vn.ntsc.eazyserver.adapter.impl.UmsMis;
import com.vn.ntsc.eazyserver.adapter.impl.UpdateNotificationAdapter;
import com.vn.ntsc.eazyserver.adapter.impl.UpdateUserInfor;
import com.vn.ntsc.eazyserver.adapter.impl.UserManagermentServiceAdapter;
import eazycommon.constant.API;
import java.util.HashMap;
import com.vn.ntsc.eazyserver.adapter.impl.backend.impl.BackEndAdapter;
import com.vn.ntsc.eazyserver.adapter.impl.backend.impl.BackEndLogAdapter;
import com.vn.ntsc.eazyserver.adapter.impl.backend.impl.BackEndTool;
import com.vn.ntsc.eazyserver.adapter.impl.backend.impl.ReviewImageAdapter;
import com.vn.ntsc.eazyserver.adapter.impl.backend.impl.ReviewVideoAdapter;
import com.vn.ntsc.otherservice.servicemanagement.respond.IApiAdapter;
import com.vn.ntsc.otherservice.servicemanagement.respond.impl.StaticFileApi;

/**
 *
 * @author tuannxv00804
 */
public class AdapterManager {

    public static final HashMap<String, IServiceAdapter> m = new HashMap<>();

    static {
        IServiceAdapter reset = new ResetConfigAdapter();
        m.put(API.RESET_CONFIG, reset);

        
        IServiceAdapter ums = new UserManagermentServiceAdapter();
        m.put(API.FORGOT_PASSWORD, ums);

        m.put(API.LIST_BACKSTAGE, ums);
        m.put(API.LIST_PUBLIC_IMAGE, UserManagermentServiceAdapter.listPublicImage);
        m.put(API.LIST_PUBLIC_VIDEO, UserManagermentServiceAdapter.listPublicVideo);
        m.put(API.LIST_PUBLIC_FILE, UserManagermentServiceAdapter.listPublicFile);
        m.put(API.REMOVE_BACKSTAGE, ums);
        m.put(API.REMOVE_PB_IMAGE, ums);
        m.put(API.REMOVE_FAVOURIST, ums);
        m.put(API.LIST_SENT_IMAGE, ums);
        m.put(API.LIST_SENT_IMAGE_WITH_USER, ums);
        m.put(API.RATE_BACKSTAGE, ums);
        m.put(API.UNLOCK, UserManagermentServiceAdapter.unlock);
        m.put(API.UNLOCK_VERSION_2, UserManagermentServiceAdapter.unlock);
        m.put(API.UNLOCK_VERSION_3, UserManagermentServiceAdapter.unlock);
        m.put(API.SAVE_IMAGE, ums);
        m.put(API.SAVE_IMAGE_VERSION_2, ums);
        m.put(API.ADD_ONLINE_ALERT, ums);
        m.put(API.GET_ONLINE_ALERT, ums);
        m.put(API.CHECK_IMAGE, ums);
        
        m.put(API.UPDATE_MEMO, ums);
        m.put(API.GET_MEMO_LIST, ums);
        m.put(API.CALL_PAYMENT, ums);

        m.put(API.BLACK_LIST, ums);
        m.put(API.CHECK_CALL, UserManagermentServiceAdapter.checkCall);

        m.put(API.GET_POINT, ums);
        m.put(API.GET_NOTIFICATION_NUMBER, ums);

        m.put(API.GET_MY_PAGE_INFOR, UserManagermentServiceAdapter.getMyPageInfo);
        m.put(API.GET_CALL_WAITING, ums);
        m.put(API.GET_UPDATE_INFO_FLAGS, ums);
        m.put(API.GET_ATTENTION_NUMBER, UserManagermentServiceAdapter.getAttentionNumber);

        m.put(API.SETTING_CALL_WAITING, UserManagermentServiceAdapter.settingCallWaiting);
        m.put(API.SET_CREA_USER_INFO, UserManagermentServiceAdapter.setCreaUserInfo);

        IServiceAdapter deactive = new DeactivateAdapter();
        m.put(API.DEACTIVATE, deactive);

        IServiceAdapter updateInfor = new UpdateUserInfor();
        m.put(API.UPDATE_USER_INFOR, updateInfor);
        m.put(API.UPDATE_LOCATION, updateInfor);
        m.put(API.UPDATE_AVATAR, updateInfor);
        
        m.put(API.CREATE_ACCOUNT_FROM_FBID, UserManagermentServiceAdapter.createAccFromFbId);
        m.put(API.ADD_FAVOURIST, UserManagermentServiceAdapter.addFavourist);
        m.put(API.CONFIRM_UPLOAD_IMAGE, UserManagermentServiceAdapter.confirmUploadImage);
        m.put(API.CONFIRM_UPLOAD_VIDEO, UserManagermentServiceAdapter.confirmUploadVideo);
        m.put(API.CONFIRM_UPLOAD_AUDIO, UserManagermentServiceAdapter.confirmUploadAudio);
        m.put(API.CONFIRM_RECORDING_VIDEO, UserManagermentServiceAdapter.confirmRecordingVideo);
        m.put(API.CONFIRM_STREAMING_VIDEO, UserManagermentServiceAdapter.confirmStreamingVideo);
        m.put(API.GET_ALL_PEOPLE, ums);
        m.put(API.CHECK_UNLOCK, ums);
        m.put(API.CHECK_UNLOCK_VERSION_2, ums);
        m.put(API.CHECK_UNLOCK_VERSION_3, ums);
        m.put(API.POINT_TO_MONEY, ums);
        m.put(API.CHANGE_POINT, ums);
        m.put(API.GET_CONNECTION_NUMBER, ums);

        m.put(API.GET_NOTIFICATION_SETTING, ums);
        m.put(API.GET_LIKE_NOTIFICATION, R1.listNotification);
        m.put(API.GET_NEWS_NOTIFICATION, R1.listNotification);
        m.put(API.GET_QA_NOTIFICATION, R1.listNotification);
        m.put(API.CLICK_NEWS_NOTIFICATION, R1.clickNewsNotification);
        m.put(API.CLICK_LIKE_NOTIFICATION, R1.clickLikeNotification);
        m.put(API.CLICK_NOTI_NOTIFICATION, R1.clickNotiNotification);
        m.put(API.CLICK_QA_NOTIFICATION, R1.clickNotiNotification);
        m.put(API.DELETE_NOTIFICATION, R1.deleteNotification);
        m.put(API.DELETE_FOOTPRINT, R1.deleteFootprint);
        
        m.put(API.DELETE_CHECKOUT_FOOTPRINT, R1.deleteFootprint);
//        m.put(API.DELETE_NOTIFICATION, ums);
        m.put(API.DELETE_LIKE, ums);
//        m.put(API.DELETE_FOOTPRINT, ums);
//        m.put(API.GET_NOTIFICATION1_SETTING_VER_2, ums);        
        m.put(API.NOTIFICATION_SETTING, UserManagermentServiceAdapter.notiSetting);

        m.put(API.ADD_SALE, ums);
        m.put(API.UPDATE_PREMIUM_MEMBER, ums);
        m.put(API.GET_SALE_STATISTIC, ums);
        m.put(API.ADD_FREE_POINT, ums);
        m.put(API.GET_FREE_POINT_HISTORY, ums);
        m.put(API.UPDATE_USER_AGE_STATUS, UserManagermentServiceAdapter.updateUserAgeFlag);
        m.put(API.GET_BACKEND_SETTING, UserManagermentServiceAdapter.getBackendSetting);
        m.put(API.GET_USER_AGE_STATUS, ums);
        m.put(API.GET_PUSH_NOTIFICATION_SETTING, ums);

        m.put(API.GET_CONNECTION_POINT_ACTION, ums);

        m.put(API.GET_CM_CODE_BY_USER_ID, ums);
        m.put(API.GET_USER_BY_REGISTER_TIME, ums);

        IServiceAdapter block = new BlockUserAdapter();
        m.put(API.ADD_BLOCK, block);
        m.put(API.REMOVE_BLOCK, block);
        m.put(API.LIST_BLOCK, BlockUserAdapter.listBlock);
        m.put(API.LIST_ONLINE_ALERT, ums);
        m.put(API.LIST_USER_ONLINE_ALERT, UserManagermentServiceAdapter.listUserOnlineAlert);

        m.put(API.ADD_TEMPLATE, ums);
        m.put(API.UPDATE_TEMPLATE, ums);
        m.put(API.DELETE_TEMPLATE, ums);
        m.put(API.LIST_TEMPLATE, ums);

        m.put(API.GET_USER_STATUS_BY_EMAIL, ums);
        m.put(API.CHECK_ONLINE_STATUS_BY_DEVICE, ums);
        
        m.put(API.RATE_USER_VOICE, ums);
        m.put(API.ADD_ALBUM, ums);
        m.put(API.DEL_ALBUM, ums);
        m.put(API.UPDATE_ALBUM, UserManagermentServiceAdapter.updateAlbum);
        m.put(API.LOAD_ALBUM, UserManagermentServiceAdapter.loadAlbum);
        m.put(API.DEL_ALBUM_IMAGE, ums);
        m.put(API.CHECK_ALBUM_OWNED, ums);
        m.put(API.ADD_ALBUM_IMAGE, ums);
        m.put(API.LOAD_ALBUM_IMAGE, UserManagermentServiceAdapter.loadAlbumImage);

        m.put(API.REGISTER, RegisterAdapter.register);
        m.put(API.REGISTER_VERSION_2, RegisterAdapter.registerByWeb);

        m.put(API.CHANGE_PASS_CASE_FORGOT, UmsMis.changePassAdapter);
        m.put(API.CHANGE_PASSWORD, UmsMis.changePass);
        m.put(API.CHANGE_EMAIL, UmsMis.changePass);
        m.put(API.LOGIN, UmsMis.loginAdapter);
        m.put(API.LOG_OUT, new LogoutAdapter());

        m.put(API.LIST_CHECK_OUT, R1.listUserAdapter);
        m.put(API.LIST_MY_FOOTPRINT, R1.listUserAdapter);
        m.put(API.LIST_FAVOURITED, R1.listUserAdapter);
        m.put(API.LIST_FAVOURIST, R1.listUserAdapter);
        m.put(API.SEARCH_BY_NAME, R1.listUserAdapter);
        m.put(API.GET_USER_INFOR, R1.userInforAdapter);
        m.put(API.LIST_NOTIFICATION, R1.listNotification);
        m.put(API.GET_BASIC_INFOR, R1.basicInforAdapter);
        m.put(API.GET_CALL_LOG, R1.listCallLog);
        
        //add by Huy 201721Sep
        m.put(API.GET_FRIEND, R1.listUserAdapter);

        m.put(API.MEET_PEOPLE, MeetPeople.getMeetPeople);
        m.put(API.LIST_USER_PROFILE, MeetPeople.getMeetPeople);
//        m.put(API.GET_MEET_PEOPLE_SETTING, meetpeople);
//        m.put(API.UPDATE_MEET_PEOPLE_SETTING, meetpeople);

        IServiceAdapter mix = new MixServiceAdapter();
        m.put(API.DOWNLOAD_STICKER_CATEGORY, mix);
        /**
         * not use two api are commented. m.put(API.GET_ALL_GIFT_CATEGORY, mix);
         * m.put(API.GET_GIFT_CATEGORY, mix);
         */
        m.put(API.GET_ALL_GIFT, mix);
        m.put(API.CALL_LOG, mix);
        m.put(API.GET_EXTRA_PAGE, mix);
        m.put(API.LOG_BEFORE_PURCHASE, mix);
        m.put(API.LOG_BEFORE_PURCHASE_VERSION_2, mix);
        m.put(API.INSTALL_APPLICATION, mix);
        m.put(API.CLICK_PUSH_NOTIFICATION, mix);

        m.put(API.STATIC_PAGE, SettingAdapter.settingMis);
        m.put(API.GET_BANNED_WORD, SettingAdapter.settingMis);
        m.put(API.GET_REPLACE_WORD, SettingAdapter.settingMis);
        m.put(API.LIST_POINT_PACKET, SettingAdapter.settingMis);
        m.put(API.LIST_ACTION_POINT_PACKET, SettingAdapter.settingMis);
        m.put(API.LIST_ACTION_POINT_PACKET_VERSION_2, SettingAdapter.settingMis);
        m.put(API.CONFIRM_PURCHASE_IOS, SettingAdapter.confirmPurchase);
        m.put(API.CONFIRM_PURCHASE_ANDROID, SettingAdapter.confirmPurchase);
        m.put(API.CONFIRM_PURCHASE_AMAZON, SettingAdapter.confirmPurchase);
        m.put(API.LIST_NEWS_CLIENT, SettingAdapter.settingMis);

        //Linh add 2017/06/27 #9088
        ChatLogAdapter chat = new ChatLogAdapter();
        m.put(API.DEL_MESSAGE, chat);
        
        m.put(API.LIST_CONVERSATION, ChatLogAdapter.listConversation);
        m.put(API.GET_CHAT_HISTORY, ChatLogAdapter.getHistory);
        m.put(API.GET_NEW_CHAT_MESSAGE, ChatLogAdapter.getHistory);
        m.put(API.MARKREADS, ChatLogAdapter.markReads);
        m.put(API.DELCOVERSATION, ChatLogAdapter.delConversation);
        m.put(API.TOTAL_UNREAD, ChatLogAdapter.unRead);
        m.put(API.CHECK_STATE_WEBSOCKET, ChatLogAdapter.checkStateWebsocket);
        m.put(API.REMOVE_WEBSOCKET, ChatLogAdapter.removeWebsocket);
        m.put(API.GET_FILE_CHAT, ChatLogAdapter.getFileChat);
        
        IServiceAdapter buzz = new BuzzAdapter();
        m.put(API.ADD_NUMBER_OF_SHARE, buzz);
        m.put(API.ADD_NUMBER_OF_VIEW, buzz);
        m.put(API.ADD_BUZZ, BuzzAdapter.addBuzz);
        m.put(API.DELETE_BUZZ, BuzzAdapter.delBuzz);
        m.put(API.LIST_COMMENT, BuzzAdapter.listComment);
        m.put(API.LIST_SUB_COMMENT, BuzzAdapter.listSubComment);
        m.put(API.ADD_COMMENT, BuzzAdapter.addComment);
        m.put(API.ADD_SUB_COMMENT, BuzzAdapter.addSubComment);
        m.put(API.BUZZ_LEAVE_WEBSOCKET, BuzzAdapter.buzzLeaveSocket);
//        m.put(API.ADD_COMMENT_VERSION_2, BuzzAdapter.addComment);
        m.put(API.ADD_COMMENT_VERSION_2, BuzzAdapter.addCommentVersion2);
        m.put(API.DELETE_COMMENT, BuzzAdapter.delComment);
        m.put(API.DELETE_SUB_COMMENT, BuzzAdapter.delSubComment);
        m.put(API.LIKE_BUZZ, BuzzAdapter.like);
        m.put(API.GET_BUZZ, BuzzAdapter.getBuzz);
        m.put(API.GET_BUZZ_DETAIL, BuzzAdapter.getBuzzDetail);
        m.put(API.GET_LOCAL_PEOPLE, BuzzAdapter.locPeople);
        m.put(API.SEND_GIFT, BuzzAdapter.gift);
        
        m.put(API.CHECK_BUZZ_WEBSOCKET,BuzzAdapter.checkBuzz);
        m.put(API.INVITE_FRIEND,BuzzAdapter.inviteFriend);
        
        m.put(API.UPLOAD_STREAM_FILE, BuzzAdapter.uploadStreamFile);
        m.put(API.UPDATE_TAG, BuzzAdapter.updateTag);
        m.put(API.ADD_TAG, BuzzAdapter.addTag);
        m.put(API.START_STREAM_SERVER, buzz);
        
        StaticFileApi stfApi = new StaticFileApi();
        m.put(API.GET_USER_ID, stfApi);

        IServiceAdapter report = new ReportAdapter();
        m.put(API.REPORT, report);

        IServiceAdapter autoMessage = new AutoMessageAdapter();
        m.put(API.AUTO_MESSAGE, autoMessage);

        IServiceAdapter updateJpns = new UpdateNotificationAdapter();
        m.put(API.UPDATE_NOTI_TOKEN, updateJpns);
        m.put(API.UNREGISTER_NOTI_TOKEN, UpdateNotificationAdapter.unregister);

        IServiceAdapter chatRoom = new ChatRoomAdapter();
        m.put(API.GET_ROOM_NAME, chatRoom);
        m.put(API.CREATE_CHAT_ROOM, chatRoom);
        m.put(API.ADD_MEMBER, ChatRoomAdapter.chatRoomMis);
        m.put(API.OUT_OFF_ROOM, ChatRoomAdapter.chatRoomMis);

        IServiceAdapter tool = new BackEndTool();
        m.put(API.LOGIN_TOOL, BackEndTool.loginTool);
        m.put(API.INSERT_SCREEN_GROUP, tool);
        m.put(API.UPDATE_SCREEN_GROUP, tool);
        m.put(API.LIST_SCREEN_GROUP, tool);
        m.put(API.INSERT_SCREEN, tool);
        m.put(API.UPDATE_SCREEN, tool);
        m.put(API.LIST_SCREEN, tool);
        m.put(API.DELETE_SCREEN, tool);
        m.put(API.DELETE_SCREEN_GROUP, tool);
        m.put(API.LIST_API, tool);
        m.put(API.SET_SCREEN_API, tool);
        m.put(API.SET_STRING, tool);
        m.put(API.GET_STRING, tool);

        IServiceAdapter backend = new BackEndAdapter();
        m.put(API.INIT, backend);
        m.put(API.GET_ADMIN_SETTING, backend);
        m.put(API.SET_ADMIN_SETTING, backend);
        m.put(API.LOGIN_ADMINISTRATOR, BackEndAdapter.loginAdmin);
        m.put(API.CHAGE_PASSWORD_ADMIN, BackEndAdapter.changePass);
        m.put(API.SET_OTHER_SETTING_APP, BackEndAdapter.setOtherSettingApp);
        m.put(API.OTHER_SETTING_APP, backend);
        
        m.put(API.SET_UPLOAD_SETTING, BackEndAdapter.setUploadSetting);
        m.put(API.GET_UPLOAD_SETTING, StaticFileApi.getUploadSetting);
        m.put(API.LIST_STICKER_URL, StaticFileApi.getStickerUrl);
        m.put(API.LIST_UPDATED_STICKER_CAT, StaticFileApi.getStickerUrl);
        m.put(API.SETTING_PRIORITIZE_USER_BUZZ, BackEndAdapter.setPrioritizeUserBuzzSetting);

        m.put(API.REGISTER_ADMIN, backend);
        m.put(API.GET_ADMIN_DETAIL, backend);
        m.put(API.UPDATE_ADMIN, BackEndAdapter.updateAdmin);
        m.put(API.DELETE_ADMIN, BackEndAdapter.updateAdmin);
        m.put(API.LIST_ADMIN, backend);

        m.put(API.INSERT_ROLE, backend);
        m.put(API.GET_ROLE_DETAIL, backend);
        m.put(API.UPDATE_ROLE, BackEndAdapter.updateRole);
        m.put(API.LIST_ROLE, backend);
        m.put(API.DELETE_ROLE, BackEndAdapter.updateRole);

        m.put(API.INSERT_AFFICIATE, backend);
        m.put(API.UPDATE_AFFICIATE, backend);
        m.put(API.LIST_AFFICIATE, backend);

        m.put(API.INSERT_MEDIA, backend);
        m.put(API.UPDATE_MEDIA, backend);
        m.put(API.LIST_MEDIA, backend);

        m.put(API.INIT_REGISTER_CM_CODE, backend);
        m.put(API.INSERT_CM_CODE, backend);
        m.put(API.UPDATE_CM_CODE, backend);
        m.put(API.LIST_CM_CODE, backend);
        m.put(API.GET_ALL_CM_CODE, backend);
        m.put(API.GET_CM_CODE_DETAIL, backend);

        m.put(API.USER_STATISTIC, backend);
        m.put(API.CM_CODE_STATISTIC, backend);
        m.put(API.TRANSACTION_STATISTIC, backend);

        m.put(API.GET_SYSTEM_ACC, backend);
        m.put(API.INSERT_AUTO_MESSAGE, backend);
        m.put(API.UPDATE_AUTO_MESSAGE, backend);
        m.put(API.DELETE_AUTO_MESSAGE, backend);
        m.put(API.LIST_AUTO_MESSAGE, backend);
        m.put(API.GET_RECEIVERS_AUTO_MESSAGE, backend);
        m.put(API.INSERT_AUTO_PUSH, backend);
        m.put(API.UPDATE_AUTO_PUSH, backend);
        m.put(API.DELETE_AUTO_PUSH, backend);
        m.put(API.LIST_AUTO_PUSH, backend);
        m.put(API.GET_RECEIVERS_AUTO_NOTIFY, backend);
        m.put(API.GET_AUTO_NOTIFY_DETAIL, backend);
        m.put(API.INSERT_AUTO_NEWS_PUSH, backend);
        m.put(API.UPDATE_AUTO_NEWS_PUSH, backend);
        m.put(API.DELETE_AUTO_NEWS_PUSH, backend);
        m.put(API.LIST_AUTO_NEWS_PUSH, backend);
        m.put(API.GET_RECEIVERS_AUTO_NEWS_NOTIFY, backend);
        m.put(API.GET_AUTO_NEWS_NOTIFY_DETAIL, backend);
        
        //Linh add
        m.put(API.INSERT_QA_PUSH, backend);
        m.put(API.UPDATE_QA_PUSH, backend);
        m.put(API.LIST_QA_PUSH, backend);
        m.put(API.DELETE_QA_PUSH, backend);
        m.put(API.GET_RECEIVERS_QA_PUSH, backend);
        m.put(API.GET_QA_PUSH, backend);

        m.put(API.INSERT_BANNED_WORD, backend);
        m.put(API.UPDATE_BANNED_WORD, backend);
        m.put(API.DELETE_BANNED_WORD, backend);
        m.put(API.LIST_BANNED_WORD, backend);

        // Add LongLT 8/2016
        m.put(API.INSERT_REPLACE_WORD, backend);
        m.put(API.UPDATE_REPLACE_WORD, backend);
        m.put(API.DELETE_REPLACE_WORD, backend);
        m.put(API.LIST_REPLACE_WORD, backend);

        m.put(API.INSERT_LOGIN_BONUS_MESSAGE, backend);
        m.put(API.DELETE_LOGIN_BONUS_MESSAGE, backend);
        m.put(API.UPDATE_LOGIN_BONUS_MESSAGE, backend);
        m.put(API.LIST_LOGIN_BONUS_MESSAGE, backend);
        m.put(API.GET_RECEIVERS_LOGIN_BONUS_MESSAGE, backend);

        m.put(API.GET_USER_ONLINE, BackEndAdapter.getUserOnline);

        m.put(API.GET_GENERAL_SETTING, backend);
        m.put(API.SET_POINT_SETTING, BackEndAdapter.setPointSetting);
        m.put(API.SET_DISTANCE_SETTING, BackEndAdapter.setDistanceSetting);
        m.put(API.SET_OTHER_SETTING, BackEndAdapter.setOtherSetting);
        m.put(API.SET_VERSION_SETTING, BackEndAdapter.setVersionSetting);
        m.put(API.SET_COMMUNICATION_SETTING, BackEndAdapter.setCommunicationSetting);
        m.put(API.SET_CONNECTION_POINT_SETTING, BackEndAdapter.setConnectionPointSetting);

        m.put(API.UPDATE_STATIC_PAGE, BackEndAdapter.staticPage);
        m.put(API.GET_STATIC_PAGE, BackEndAdapter.staticPage);

        m.put(API.UPDATE_USER_INF_BY_ADMIN, BackEndAdapter.updateUserInfor);
        m.put(API.REGISTER_BY_ADMIN, BackEndAdapter.registerByAdmin);
        m.put(API.RESET_PASSWORD, backend);
        // ThanhDD 03/10/2016 #4789
        m.put(API.VIEW_TOTAL_PRICE, backend);
        //#4311
        m.put(API.VIEW_TOTAL_POINT, backend);
        m.put(API.GET_MONEY_TRADE_FROM_POINT, backend);

        IServiceAdapter backEndLog = new BackEndLogAdapter();
        m.put(API.SEARCH_USER, backEndLog);
        m.put(API.DETAIL_USER, backend);
        m.put(API.ADD_POINT, BackEndAdapter.addPoint);
        m.put(API.ADD_POINT_BY_LIST, BackEndAdapter.addPointByList);
        m.put(API.SEARCH_CONNECTION, backend);
        m.put(API.LIST_CONNECTION, backend);
        m.put(API.ADD_PURCHASE_BY_AMIN, BackEndAdapter.addPurchaseByAdmin);//LongLT 8/8/2016

        m.put(API.SEARCH_LOG_PURCHASE, backend);
        m.put(API.LIST_LOG_PURCHASE, backend);
        m.put(API.LIST_LOG_POINT, backend);
        m.put(API.SEARCH_LOG_POINT, backEndLog);
        m.put(API.SEARCH_LOG_LOGIN, backEndLog);
        m.put(API.SEARCH_LOG_DEACTIVATE, backEndLog);
        m.put(API.SEARCH_LOG_LOOK, backEndLog);
        m.put(API.SEARCH_LOG_ONLINE_ALERT, backEndLog);
        m.put(API.SEARCH_LOG_BLOCK, backEndLog);
        m.put(API.SEARCH_LOG_WINK_BOMB, backEndLog);
        m.put(API.DETAIL_WINK_BOMB, backend);
        m.put(API.SEARCH_LOG_CHAT, backEndLog);
        m.put(API.SEARCH_LOG_FAVOURIST, backEndLog);
        m.put(API.SEARCH_LOG_SHAKE_CHAT, backEndLog);
        m.put(API.SEARCH_LOG_NOTIFICATION, backEndLog);
        m.put(API.SEARCH_LOG_WINK, backEndLog);
        m.put(API.SEARCH_LOG_CHECK_OUT, backEndLog);
        m.put(API.SEARCH_LOG_CALL, backEndLog);
        m.put(API.SEARCH_REPORT_USER, backEndLog);
        m.put(API.SEARCH_REPORT_IMAGE, backEndLog);
        m.put(API.SEARCH_REPORT_VIDEO, backEndLog);
        m.put(API.SEARCH_REPORT_AUDIO, backEndLog);

        m.put(API.SEARCH_LOG_BUZZ, backEndLog);
        m.put(API.BUZZ_DETAIL, backend);
        m.put(API.GET_COMMENT, backend);
        m.put(API.LIST_LIKE, backend);
        m.put(API.DEL_BUZZ_ADMIN, BackEndAdapter.deleteBuzz);
        m.put(API.DEL_COMMENT_ADMIN, backend);
        m.put(API.GET_LIST_SUB_COMMENT_BY_ADMIN, backend);
        m.put(API.DELETE_SUB_COMMENT_BY_ADMIN, backend);

        IServiceAdapter reviewAdapter = new ReviewImageAdapter();
        m.put(API.LIST_IMAGE, backend);
        m.put(API.REVIEW_IMAGE, reviewAdapter);
        m.put(API.DELETE_IMAGE, ReviewImageAdapter.deleteImage);
        m.put(API.GET_IMAGE_INFOR, backend);
        
        // add by Huy 201710Oct
        IServiceAdapter reviewVideoAdapter = new ReviewVideoAdapter();
        m.put(API.LIST_VIDEO, backend);
        m.put(API.REVIEW_VIDEO, reviewVideoAdapter);

        m.put(API.LIST_REPORTED_IMAGE, backend);
        m.put(API.LIST_REPORTED_VIDEO, backend);
        m.put(API.LIST_REPORTED_AUDIO, backend);
        m.put(API.PROCESS_REPORTED_IMAGE, reviewAdapter);
        m.put(API.PROCESS_REPORTED_VIDEO, ReviewImageAdapter.processReportFile);
        m.put(API.PROCESS_REPORTED_AUDIO, ReviewImageAdapter.processReportFile);

        m.put(API.INSERT_GIFT_CATEGORY, backend);
        m.put(API.UPDATE_GIFT_CATEGORY, backend);
        m.put(API.DELETE_GIFT_CATEGORY, backend);
        m.put(API.LIST_GIFT_CATEGORY, backend);

        m.put(API.INSERT_GIFT, BackEndAdapter.insertStamp);
        m.put(API.UPDATE_GIFT_IMAGE, BackEndAdapter.updateStampImage);
        m.put(API.UPDATE_GIFT, backend);
        m.put(API.LIST_GIFT, backend);
        m.put(API.DELETE_GIFT, backend);
        m.put(API.ORDER_GIFT, backend);

        m.put(API.INSERT_STICKER_CATEGORY, BackEndAdapter.insertStamp);
        m.put(API.UPDATE_STICKER_CATEGORY, backend);
        m.put(API.UPDATE_STICKER_CATEGORY_IMAGE, BackEndAdapter.updateStampImage);
        m.put(API.DELETE_STICKER_CATEGORY, backend);
        m.put(API.LIST_STICKER_CATEGORY, backend);
        m.put(API.PUBLIC_STICKER_CATEGORY, backend);

        m.put(API.INSERT_STICKER, BackEndAdapter.insertStamp);
        m.put(API.UPDATE_STICKER_IMAGE, BackEndAdapter.updateStampImage);
        m.put(API.DELETE_STICKER, backend);
        m.put(API.LIST_STICKER, backend);
        m.put(API.ORDER_STICKER, backend);

//        m.put(API.insert_sticker_category_ver_2, BackEndAdapter.insertStamp);
        m.put(API.UPDATE_STICKER_CATEGORY, backend);
//        m.put(API.insert_sticker_ver_2, BackEndAdapter.insertStamp);
//        m.put(API.delete_sticker_ver_2, backend);
//        m.put(API.order_sticker_ver_2, backend);

        //HUNGDT 20425
        m.put(API.MAKE_CALL, SipAdapter.makeCallAdapter);
        m.put(API.START_CALL, SipAdapter.startCallAdapter);
        m.put(API.END_CALL, SipAdapter.endCallAdapter);
        m.put(API.CALL_PAYMENT, SipAdapter.callPaymentAdapter);

        m.put(API.INSERT_EXTRA_PAGE, backend);
        m.put(API.UPDATE_EXTRA_PAGE, backend);
        m.put(API.DELETE_EXTRA_PAGE, backend);
        m.put(API.LIST_EXTRA_PAGE, backend);

        m.put(API.INSERT_POINT_PACKAGE, backend);
        m.put(API.UPDATE_POINT_PACKAGE, backend);
        m.put(API.DELETE_POINT_PACKAGE, backend);
        m.put(API.LIST_POINT_PACKAGE, backend);
        m.put(API.INSERT_ACTION_POINT_PACKAGE, backend);
        m.put(API.UPDATE_ACTION_POINT_PACKAGE, backend);
        m.put(API.DELETE_ACTION_POINT_PACKAGE, backend);
        m.put(API.LIST_ACTION_POINT_PACKAGE, backend);

        m.put(API.INSERT_NEWS, backend);
        m.put(API.UPDATE_NEWS, backend);
        m.put(API.DELETE_NEWS, backend);
        m.put(API.GET_NEWS_DETAIL, backend);
        m.put(API.LIST_NEWS_BACKEND, backend);
        m.put(API.UPDATE_NEWS_TO_APP, backend);

        m.put(API.INSERT_FREE_POINT, backend);
        m.put(API.UPDATE_FREE_POINT, backend);
        m.put(API.LIST_FREE_POINT, backend);

        m.put(API.INSTALLATION_STATISTIC, backend);

        m.put(API.GET_REVIEWING_BUZZ, backend);
        m.put(API.REVIEW_BUZZ, BackEndAdapter.reviewBuzz);

        m.put(API.GET_REVIEWING_COMMENT, backend);
        m.put(API.REVIEW_COMMENT, BackEndAdapter.reviewComment);
        
        // Namhv #8001 20/04/2017
        m.put(API.GET_LOG_COMMENT, backend);
        // #8001
        m.put(API.GET_REVIEWING_SUB_COMMENT, backend);
        m.put(API.REVIEW_SUB_COMMENT, BackEndAdapter.reviewSubComment);

        m.put(API.GET_REVIEW_USER, backend);
        m.put(API.REVIEW_USER, BackEndAdapter.reviewUser);
        m.put(API.GET_LOG_USER_INFO, backend);
        
        m.put(API.ADD_EMOJI_CAT, backend);
        m.put(API.ADD_EMOJI, backend);
        m.put(API.EMOJI_URL, StaticFileApi.getEmojiUrl);
        m.put(API.EMOJI_DATA, StaticFileApi.getEmojiUrl);
        m.put(API.LIST_UPDATED_EMOJI_CAT, StaticFileApi.getEmojiUrl);
        m.put(API.LIST_EMOJI_CAT, StaticFileApi.getEmojiUrl);
        m.put(API.LIST_EMOJI, StaticFileApi.getEmojiUrl);
        m.put(API.EDIT_EMOJI_CAT, backend);
        m.put(API.DEL_EMOJI_CAT, backend);
        m.put(API.EDIT_EMOJI, backend);
        m.put(API.DEL_EMOJI, backend);
        m.put(API.ORDER_EMOJI, backend);

        IServiceAdapter stickerAdapter = new StickerAdapter();
        m.put(API.GET_STICKER_SHOP_INFOR, stickerAdapter);
        m.put(API.STICKER_CATEGORY_DETAIL, stickerAdapter);
        m.put(API.STICKER_CATEGORY_LIST, stickerAdapter);
        m.put(API.LIST_DEFAULT_STICKER_CATEGORY, stickerAdapter);
        m.put(API.SEARCH_STICKER_CATEGORY, stickerAdapter);
        m.put(API.BUY_STICKER_BY_POINT, StickerAdapter.buyStickerByPoint);
        m.put(API.RESTORE_STICKER_BY_MONEY, stickerAdapter);
        m.put(API.RESTORE_STICKER_BY_POINT, stickerAdapter);
        m.put(API.BUY_STICKER_BY_MONEY_ANDROID, stickerAdapter);
        m.put(API.BUY_STICKER_BY_MONEY_IOS, stickerAdapter);

        IServiceAdapter pushFromFreePage = new PushFromFreePageAdapter();
        m.put(API.PUSH_NOTIFICATION_FROM_FREE_PAGE, pushFromFreePage);
        m.put(API.PUSH_NOTIFICATION_FROM_BACK_END, PushFromFreePageAdapter.pushFromBackend);
        m.put(API.PUSH_NOTIFICATION, PushFromFreePageAdapter.pushFromBackendNews);

//        IServiceAdapter pushFromNews = new PushFromNewsAdapter();
//         m.put(API.PUSH_NOTIFICATION_FROM_NEWS, pushFromNews);
        IServiceAdapter getInforForApp = new GetInformationForApplicationAdapter();
        m.put(API.GET_INFOR_FOR_APPLICATION, getInforForApp);

        IServiceAdapter getUserAppVersion = new GetUserApplicationVersionAdapter();
        m.put(API.GET_USER_APPLICATION_VERSION, getUserAppVersion);

        IServiceAdapter tokenAdapter = new TokenAdapter();
        m.put(API.CHECK_TOKEN, tokenAdapter);

    }

    public static IServiceAdapter getAdapter(String apiName) {
        return m.get(apiName);
    }
}
