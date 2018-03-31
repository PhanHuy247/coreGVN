/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.usermanagementserver.server.respond;

import com.vn.ntsc.usermanagementserver.server.respond.impl.buzz.AddCommentApi;
import com.vn.ntsc.usermanagementserver.server.respond.impl.buzz.LikeBuzzApi;
import com.vn.ntsc.usermanagementserver.server.respond.impl.buzz.ReviewBuzzApi;
import com.vn.ntsc.usermanagementserver.server.respond.impl.buzz.SendGiftApi;
import com.vn.ntsc.usermanagementserver.server.respond.impl.buzz.AddBuzzApi;
import com.vn.ntsc.usermanagementserver.server.respond.impl.buzz.AddCommentVersion2Api;
import com.vn.ntsc.usermanagementserver.server.respond.impl.buzz.GetBuzzApi;
import com.vn.ntsc.usermanagementserver.server.respond.impl.buzz.DeleteBuzzApi;
import com.vn.ntsc.usermanagementserver.server.respond.impl.buzz.ReviewCommentApi;
import com.vn.ntsc.usermanagementserver.server.respond.impl.buzz.AddCommentGetInforVersion2Api;
import com.vn.ntsc.usermanagementserver.server.respond.impl.buzz.GetImageInforApi;
import com.vn.ntsc.usermanagementserver.server.respond.impl.buzz.ReviewSubCommentApi;
import com.vn.ntsc.usermanagementserver.server.respond.impl.buzz.AddSubCommentApi;
import com.vn.ntsc.usermanagementserver.server.respond.impl.buzz.AddSubCommentGetInforApi;
import com.vn.ntsc.usermanagementserver.server.respond.impl.buzz.AddCommentGetInforApi;
import com.vn.ntsc.usermanagementserver.server.respond.impl.activity.ListTemplateApi;
import com.vn.ntsc.usermanagementserver.server.respond.impl.activity.DeleteTemplateApi;
import com.vn.ntsc.usermanagementserver.server.respond.impl.activity.SetNotificationSettingApi;
import com.vn.ntsc.usermanagementserver.server.respond.impl.activity.TradePointToMoney;
import com.vn.ntsc.usermanagementserver.server.respond.impl.activity.ReturnFailedUploadPointApi;
import com.vn.ntsc.usermanagementserver.server.respond.impl.activity.AddPointByListApi;
import com.vn.ntsc.usermanagementserver.server.respond.impl.activity.GetUserInforApi;
import com.vn.ntsc.usermanagementserver.server.respond.impl.activity.SaveImageApi;
import com.vn.ntsc.usermanagementserver.server.respond.impl.activity.GetPointApi;
import com.vn.ntsc.usermanagementserver.server.respond.impl.activity.SaveImageVersion2Api;
import com.vn.ntsc.usermanagementserver.server.respond.impl.activity.UpdateLocationApi;
import com.vn.ntsc.usermanagementserver.server.respond.impl.activity.SetCreaUserInfoApi;
import com.vn.ntsc.usermanagementserver.server.respond.impl.activity.AddTemplateApi;
import com.vn.ntsc.usermanagementserver.server.respond.impl.activity.GetNotificationNumberApi;
import com.vn.ntsc.usermanagementserver.server.respond.impl.activity.PayChatApi;
import com.vn.ntsc.usermanagementserver.server.respond.impl.activity.ReportApi;
import com.vn.ntsc.usermanagementserver.server.respond.impl.activity.AddPointApi;
import com.vn.ntsc.usermanagementserver.server.respond.impl.activity.UpdateAvatarApi;
import com.vn.ntsc.usermanagementserver.server.respond.impl.activity.ConfirmUploadImageApi;
import com.vn.ntsc.usermanagementserver.server.respond.impl.activity.BuyStickerApi;
import com.vn.ntsc.usermanagementserver.server.respond.impl.activity.GetNotificationSettingApi;
import com.vn.ntsc.usermanagementserver.server.respond.impl.activity.ChangPointApi;
import com.vn.ntsc.usermanagementserver.server.respond.impl.activity.RateBackstageApi;
import com.vn.ntsc.usermanagementserver.server.respond.impl.activity.UpdateTemplateApi;
import com.vn.ntsc.usermanagementserver.server.respond.impl.activity.RemoveSuccessUploadPointDocumentApi;
import com.vn.ntsc.usermanagementserver.server.respond.impl.activity.AddPurchaseByAdminApi;
import com.vn.ntsc.usermanagementserver.server.respond.impl.activity.UpdateUserApi;
import com.vn.ntsc.usermanagementserver.server.respond.impl.activity.RemoveBackstageApi;
import com.vn.ntsc.usermanagementserver.server.respond.impl.activity.PayCallApi;
import com.vn.ntsc.usermanagementserver.server.respond.impl.connection.DeleteFootprintApi;
import com.vn.ntsc.usermanagementserver.server.respond.impl.connection.ClickQANotificationApi;
import com.vn.ntsc.usermanagementserver.server.respond.impl.connection.GetConnectionNumberApi;
import com.vn.ntsc.usermanagementserver.server.respond.impl.connection.GetBackStageApi;
import com.vn.ntsc.usermanagementserver.server.respond.impl.connection.SearchByNameApi;
import com.vn.ntsc.usermanagementserver.server.respond.impl.connection.ListSentImageApi;
import com.vn.ntsc.usermanagementserver.server.respond.impl.connection.GetOnlineAlertApi;
import com.vn.ntsc.usermanagementserver.server.respond.impl.connection.AddBlockApi;
import com.vn.ntsc.usermanagementserver.server.respond.impl.connection.ListOnlineAlertApi;
import com.vn.ntsc.usermanagementserver.server.respond.impl.connection.RemoveBlockApi;
import com.vn.ntsc.usermanagementserver.server.respond.impl.connection.UnlockVersion3Api;
import com.vn.ntsc.usermanagementserver.server.respond.impl.connection.CheckUnlockVersion3Api;
import com.vn.ntsc.usermanagementserver.server.respond.impl.connection.DeleteCheckOutFootprintApi;
import com.vn.ntsc.usermanagementserver.server.respond.impl.connection.ClickLikeNotificationApi;
import com.vn.ntsc.usermanagementserver.server.respond.impl.connection.ListFavouristApi;
import com.vn.ntsc.usermanagementserver.server.respond.impl.connection.ClickNotiNotificationApi;
import com.vn.ntsc.usermanagementserver.server.respond.impl.connection.ListBlockApi;
import com.vn.ntsc.usermanagementserver.server.respond.impl.connection.AddFavouristApi;
import com.vn.ntsc.usermanagementserver.server.respond.impl.connection.RemoveFavouristApi;
import com.vn.ntsc.usermanagementserver.server.respond.impl.connection.NotificationLoginApi;
import com.vn.ntsc.usermanagementserver.server.respond.impl.connection.ClickNewsNotification;
import com.vn.ntsc.usermanagementserver.server.respond.impl.connection.CheckUnlockVersion2Api;
import com.vn.ntsc.usermanagementserver.server.respond.impl.connection.UnlockApi;
import com.vn.ntsc.usermanagementserver.server.respond.impl.connection.SettingCallWaiting;
import com.vn.ntsc.usermanagementserver.server.respond.impl.connection.ListMyFootprintApi;
import com.vn.ntsc.usermanagementserver.server.respond.impl.connection.DeleteNotificationApi;
import com.vn.ntsc.usermanagementserver.server.respond.impl.connection.GetNewsNotification;
import com.vn.ntsc.usermanagementserver.server.respond.impl.connection.GetCallWaiting;
import com.vn.ntsc.usermanagementserver.server.respond.impl.connection.UnlockVersion2Api;
import com.vn.ntsc.usermanagementserver.server.respond.impl.connection.ListNotificationApi;
import com.vn.ntsc.usermanagementserver.server.respond.impl.connection.GetQANotificationAPI;
import com.vn.ntsc.usermanagementserver.server.respond.impl.connection.ListCheckOutApi;
import com.vn.ntsc.usermanagementserver.server.respond.impl.connection.AddOnlineAlertApi;
import com.vn.ntsc.usermanagementserver.server.respond.impl.connection.ListSentImageWithUserApi;
import com.vn.ntsc.usermanagementserver.server.respond.impl.connection.CheckUnlockApi;
import com.vn.ntsc.usermanagementserver.server.respond.impl.connection.ListFavouritedApi;
import com.vn.ntsc.usermanagementserver.server.respond.impl.connection.ListPublicImageApi;
import com.vn.ntsc.usermanagementserver.server.respond.impl.backend.UpdateConnectionPointSettingApi;
import com.vn.ntsc.usermanagementserver.server.respond.impl.backend.UpdateOtherSettingApi;
import com.vn.ntsc.usermanagementserver.server.respond.impl.backend.UpdatePointSettingApi;
import com.vn.ntsc.usermanagementserver.server.respond.impl.backend.ResetConfigApi;
import com.vn.ntsc.usermanagementserver.server.respond.impl.backend.UpdateDistanceSettingApi;
import com.vn.ntsc.usermanagementserver.server.respond.impl.backend.UpdateCommunicationSettingApi;
import com.vn.ntsc.usermanagementserver.server.respond.impl.information.GiftApi;
import com.vn.ntsc.usermanagementserver.server.respond.impl.information.GetConnectionPointActionApi;
import com.vn.ntsc.usermanagementserver.server.respond.impl.information.GetMemoList;
import com.vn.ntsc.usermanagementserver.server.respond.impl.information.GetBackendSettingApi;
import com.vn.ntsc.usermanagementserver.server.respond.impl.information.AddFreePointApi;
import com.vn.ntsc.usermanagementserver.server.respond.impl.information.GetUserAgeStatusApi;
import com.vn.ntsc.usermanagementserver.server.respond.impl.information.GetAttentionNumber;
import com.vn.ntsc.usermanagementserver.server.respond.impl.information.DeniedImageApi;
import com.vn.ntsc.usermanagementserver.server.respond.impl.information.GetCmCodeByUserIdAPI;
import com.vn.ntsc.usermanagementserver.server.respond.impl.information.ApproveImageApi;
import com.vn.ntsc.usermanagementserver.server.respond.impl.information.ConfirmPurchaseApi;
import com.vn.ntsc.usermanagementserver.server.respond.impl.information.DeletePreviousImageApi;
import com.vn.ntsc.usermanagementserver.server.respond.impl.information.GetUserStatusByEmailApi;
import com.vn.ntsc.usermanagementserver.server.respond.impl.information.PushFromFreePageApi;
import com.vn.ntsc.usermanagementserver.server.respond.impl.information.ListStatusApi;
import com.vn.ntsc.usermanagementserver.server.respond.impl.information.CheckImageApi;
import com.vn.ntsc.usermanagementserver.server.respond.impl.information.GetBlockListApi;
import com.vn.ntsc.usermanagementserver.server.respond.impl.information.GetFreePointHistoryApi;
import com.vn.ntsc.usermanagementserver.server.respond.impl.information.SendImageByChatApi;
import com.vn.ntsc.usermanagementserver.server.respond.impl.information.UpdateMemoApi;
import com.vn.ntsc.usermanagementserver.server.respond.impl.information.PushFromBackendApi;
import com.vn.ntsc.usermanagementserver.server.respond.impl.information.GetUserByRegisterDateAPI;
import com.vn.ntsc.usermanagementserver.server.respond.impl.information.GetMyPageInfor;
import com.vn.ntsc.usermanagementserver.server.respond.impl.information.ListUserConnectionInfor;
import com.vn.ntsc.usermanagementserver.server.respond.impl.information.UpdateUserAgeStatusApi;
import com.vn.ntsc.usermanagementserver.server.respond.impl.information.CheckCallApi;
import com.vn.ntsc.usermanagementserver.server.respond.impl.information.DeleteConverstationApi;
import com.vn.ntsc.usermanagementserver.server.respond.impl.information.UpdatePremiumMemberApi;
import com.vn.ntsc.usermanagementserver.server.respond.impl.information.GetSaleStatisticApi;
import com.vn.ntsc.usermanagementserver.server.respond.impl.information.GetCallLog;
import com.vn.ntsc.usermanagementserver.server.respond.impl.information.GetUpdateInfoFlagsApi;
import com.vn.ntsc.usermanagementserver.server.respond.impl.information.PushFromNewsApi;
import com.vn.ntsc.usermanagementserver.server.respond.impl.information.GetBlackListApi;
import com.vn.ntsc.usermanagementserver.server.respond.impl.information.GetPushNotificationSettingApi;
import com.vn.ntsc.usermanagementserver.server.respond.impl.information.AddSaleApi;
import com.vn.ntsc.usermanagementserver.server.respond.impl.account.ChangeEmailApi;
import com.vn.ntsc.usermanagementserver.server.respond.impl.account.UpdateCMCodeApi;
import com.vn.ntsc.usermanagementserver.server.respond.impl.account.ReviewUserApi;
import com.vn.ntsc.usermanagementserver.server.respond.impl.account.ChangeFlagApi;
import com.vn.ntsc.usermanagementserver.server.respond.impl.account.ChangePasswordForgotApi;
import com.vn.ntsc.usermanagementserver.server.respond.impl.account.ForgotPasswordApi;
import com.vn.ntsc.usermanagementserver.server.respond.impl.account.RegisterByAdminApi;
import com.vn.ntsc.usermanagementserver.server.respond.impl.account.RegisterApi;
import com.vn.ntsc.usermanagementserver.server.respond.impl.account.DeactiveApi;
import com.vn.ntsc.usermanagementserver.server.respond.impl.account.ChangePasswordApi;
import com.vn.ntsc.usermanagementserver.server.respond.impl.account.CheckOnlineStatusByDeviceApi;
import com.vn.ntsc.usermanagementserver.server.respond.impl.account.CreateAccountFromFbId;
import com.vn.ntsc.usermanagementserver.server.respond.impl.account.LoginApi;
import com.vn.ntsc.usermanagementserver.server.respond.impl.account.RegisterVersion2Api;
import com.vn.ntsc.usermanagementserver.server.respond.impl.activity.ConfirmUploadAudioApi;
import com.vn.ntsc.usermanagementserver.server.respond.impl.activity.ConfirmUploadVideoApi;
import com.vn.ntsc.usermanagementserver.server.respond.impl.activity.GetNotificationListApi;
import com.vn.ntsc.usermanagementserver.server.respond.impl.buzz.AddTag;
import com.vn.ntsc.usermanagementserver.server.respond.impl.buzz.GetImageStatusInforApi;
import com.vn.ntsc.usermanagementserver.server.respond.impl.buzz.RateUserVoiceApi;
import com.vn.ntsc.usermanagementserver.server.respond.impl.connection.GetLikeNotification;
import com.vn.ntsc.usermanagementserver.server.respond.impl.connection.ListFriendApi;
import com.vn.ntsc.usermanagementserver.server.respond.impl.connection.ListPublicFileApi;
import com.vn.ntsc.usermanagementserver.server.respond.impl.connection.ListPublicVideoAudioApi;
import com.vn.ntsc.usermanagementserver.server.respond.impl.connection.TotalNotiSeen;
import com.vn.ntsc.usermanagementserver.server.respond.impl.information.AddAlbumApi;
import com.vn.ntsc.usermanagementserver.server.respond.impl.information.AddAlbumImageApi;
import com.vn.ntsc.usermanagementserver.server.respond.impl.information.ApproveFileApi;
import com.vn.ntsc.usermanagementserver.server.respond.impl.information.CheckAlbumOwnedApi;
import com.vn.ntsc.usermanagementserver.server.respond.impl.information.DelAlbumApi;
import com.vn.ntsc.usermanagementserver.server.respond.impl.information.DelAlbumImageApi;
import com.vn.ntsc.usermanagementserver.server.respond.impl.information.DeniedFileApi;
import com.vn.ntsc.usermanagementserver.server.respond.impl.information.LoadAlbumApi;
import com.vn.ntsc.usermanagementserver.server.respond.impl.information.LoadAlbumImageApi;
import com.vn.ntsc.usermanagementserver.server.respond.impl.information.UpdateAlbumApi;
import java.util.TreeMap;
import eazycommon.constant.API;
import eazycommon.util.Util;

/**
 *
 * @author RuAc0n
 */
public class APIManager {

    public static final TreeMap<String, IApiAdapter> m = new TreeMap<>();

    static {        
        m.put(API.RESET_CONFIG, new ResetConfigApi());

        // account
        m.put(API.REGISTER, new RegisterApi());
        m.put(API.REGISTER_BY_ADMIN, new RegisterByAdminApi());
        m.put(API.LOGIN, new LoginApi());
        m.put(API.REGISTER_VERSION_2, new RegisterVersion2Api());
        m.put(API.CHANGE_PASSWORD, new ChangePasswordApi());
        m.put(API.CHANGE_PASS_CASE_FORGOT, new ChangePasswordForgotApi());
        m.put(API.FORGOT_PASSWORD, new ForgotPasswordApi());
        m.put(API.DEACTIVATE, new DeactiveApi());
        m.put(API.CHANGEFLAG, new ChangeFlagApi());
        m.put(API.CM_CODE, new UpdateCMCodeApi());

        //information
        m.put(API.CHECK_IMAGE, new CheckImageApi());
        m.put(API.CONFIRM_PURCHASE_ANDROID, new ConfirmPurchaseApi());
        m.put(API.CONFIRM_PURCHASE_IOS, new ConfirmPurchaseApi());
        m.put(API.CONFIRM_PURCHASE_AMAZON, new ConfirmPurchaseApi());
        m.put(API.APPROVED_IMAGE, new ApproveImageApi());
        m.put(API.DENIED_IMAGE, new DeniedImageApi());
        m.put(API.APPROVED_FILE, new ApproveFileApi());
        m.put(API.DENIED_FILE, new DeniedFileApi());
        m.put(API.LIST_STATUS, new ListStatusApi());
        m.put(API.GIFT, new GiftApi());
        m.put(API.GET_BLACK_LIST, new GetBlackListApi());
        m.put(API.GET_BLOCK_LIST, new GetBlockListApi());
        m.put(API.DELETE_PREVIOUS_IMAGE, new DeletePreviousImageApi());
        m.put(API.GET_MY_PAGE_INFOR, new GetMyPageInfor());
        m.put(API.GET_UPDATE_INFO_FLAGS, new GetUpdateInfoFlagsApi());
        m.put(API.GET_ATTENTION_NUMBER, new GetAttentionNumber());
        m.put(API.UPDATE_MEMO, new UpdateMemoApi());
        m.put(API.GET_MEMO_LIST, new GetMemoList());
        //backend
        m.put(API.SET_OTHER_SETTING, new UpdateOtherSettingApi());
        m.put(API.SET_COMMUNICATION_SETTING, new UpdateCommunicationSettingApi());
        m.put(API.SET_CONNECTION_POINT_SETTING, new UpdateConnectionPointSettingApi());
        m.put(API.SET_POINT_SETTING, new UpdatePointSettingApi());
        m.put(API.UPDATE_DISTANCE, new UpdateDistanceSettingApi());

        //buzz
        m.put(API.GET_BUZZ, new GetBuzzApi());
        m.put(API.GET_LOCAL_PEOPLE, new GetBuzzApi());
        m.put(API.ADD_BUZZ, new AddBuzzApi());
        m.put(API.DELETE_BUZZ, new DeleteBuzzApi());
        m.put(API.LIKE_BUZZ, new LikeBuzzApi());
        m.put(API.ADD_COMMENT, new AddCommentApi());
        m.put(API.ADD_COMMENT_VERSION_2, new AddCommentVersion2Api());
        m.put(API.SEND_GIFT, new SendGiftApi());
        m.put(API.GET_IMAGE_INFOR, new GetImageInforApi());
        m.put(API.ADD_COMMENT_GET_INFOR, new AddCommentGetInforApi());
        m.put(API.ADD_COMMENT_GET_INFOR_VERSION_2, new AddCommentGetInforVersion2Api());
        m.put(API.ADD_SUB_COMMENT, new AddSubCommentApi());
        m.put(API.ADD_SUB_COMMENT_GET_INFOR, new AddSubCommentGetInforApi());
        m.put(API.ADD_TAG,new AddTag());

        //activity
        m.put(API.UPDATE_USER_INFOR, new UpdateUserApi());
        m.put(API.UPDATE_USER_INF_BY_ADMIN, new UpdateUserApi());
        m.put(API.UPDATE_LOCATION, new UpdateLocationApi());
        m.put(API.GET_USER_INFOR, new GetUserInforApi());
        m.put(API.UPDATE_AVATAR, new UpdateAvatarApi());

        m.put(API.SET_CREA_USER_INFO, new SetCreaUserInfoApi());
        m.put(API.CONFIRM_UPLOAD_IMAGE, new ConfirmUploadImageApi());
        m.put(API.CONFIRM_UPLOAD_VIDEO, new ConfirmUploadVideoApi());
        m.put(API.CONFIRM_UPLOAD_AUDIO, new ConfirmUploadAudioApi());
        m.put(API.REMOVE_BACKSTAGE, new RemoveBackstageApi());
        m.put(API.REPORT, new ReportApi());
        m.put(API.SAVE_IMAGE, new SaveImageApi());
        m.put(API.SAVE_IMAGE_VERSION_2, new SaveImageVersion2Api());
        m.put(API.RATE_BACKSTAGE, new RateBackstageApi());
        m.put(API.GET_NOTIFICATION_LIST, new GetNotificationListApi());

        m.put(API.ADD_POINT, new AddPointApi());
        m.put(API.ADD_POINT_BY_LIST, new AddPointByListApi());
        m.put(API.ADD_PURCHASE_BY_AMIN, new AddPurchaseByAdminApi());// LongLT 8/8/2016
        m.put(API.BUY_STICKER_BY_POINT, new BuyStickerApi());

        m.put(API.CALL_PAYMENT, new PayCallApi());
        m.put(API.PAY_CHAT, new PayChatApi());
        m.put(API.RETURN_FAILED_UPLOAD_POINT, new ReturnFailedUploadPointApi());
        m.put(API.REMOVE_SUCCESS_UPLOAD_POINT_DOCUMENT, new RemoveSuccessUploadPointDocumentApi());

        m.put(API.GET_POINT, new GetPointApi());
        m.put(API.GET_NOTIFICATION_SETTING, new GetNotificationSettingApi());
        m.put(API.NOTIFICATION_SETTING, new SetNotificationSettingApi());
        m.put(API.GET_LIKE_NOTIFICATION, new GetLikeNotification());
        m.put(API.GET_NEWS_NOTIFICATION, new GetNewsNotification());
        m.put(API.GET_QA_NOTIFICATION, new GetQANotificationAPI());
        m.put(API.CLICK_NEWS_NOTIFICATION, new ClickNewsNotification());
        m.put(API.CLICK_LIKE_NOTIFICATION, new ClickLikeNotificationApi());
        m.put(API.CLICK_NOTI_NOTIFICATION, new ClickNotiNotificationApi());
        m.put(API.CLICK_QA_NOTIFICATION, new ClickQANotificationApi());
        //khanhdd
        m.put(API.DELETE_NOTIFICATION, new DeleteNotificationApi());
        m.put(API.DELETE_FOOTPRINT, new DeleteFootprintApi());
        m.put(API.DELETE_CHECKOUT_FOOTPRINT, new DeleteCheckOutFootprintApi());//thanhdd add 23/01/2017
        //connection
        m.put(API.REMOVE_FAVOURIST, new RemoveFavouristApi());
        m.put(API.ADD_FAVOURIST, new AddFavouristApi());

        m.put(API.LIST_PUBLIC_IMAGE, new ListPublicImageApi());
        m.put(API.LIST_PUBLIC_VIDEO,new ListPublicVideoAudioApi());
        m.put(API.LIST_PUBLIC_FILE,new ListPublicFileApi());
        m.put(API.LIST_BACKSTAGE, new GetBackStageApi());
        m.put(API.SEARCH_BY_NAME, new SearchByNameApi());
        m.put(API.LIST_FAVOURITED, new ListFavouritedApi());
        m.put(API.LIST_FAVOURIST, new ListFavouristApi());
        m.put(API.GET_FRIEND, new ListFriendApi());

        m.put(API.ADD_BLOCK, new AddBlockApi());
        m.put(API.LIST_CHECK_OUT, new ListCheckOutApi());
        m.put(API.LIST_MY_FOOTPRINT, new ListMyFootprintApi());
        m.put(API.REMOVE_BLOCK, new RemoveBlockApi());
        m.put(API.LIST_BLOCK, new ListBlockApi());
        m.put(API.LIST_ONLINE_ALERT, new ListOnlineAlertApi());
        m.put(API.ADD_ONLINE_ALERT, new AddOnlineAlertApi());
        m.put(API.GET_ONLINE_ALERT, new GetOnlineAlertApi());

        m.put(API.NOTI_LOGIN, new NotificationLoginApi());
        m.put(API.CHECK_UNLOCK, new CheckUnlockApi());
        m.put(API.CHECK_UNLOCK_VERSION_2, new CheckUnlockVersion2Api());
        m.put(API.CHECK_UNLOCK_VERSION_3, new CheckUnlockVersion3Api());
        m.put(API.UNLOCK, new UnlockApi());
        m.put(API.UNLOCK_VERSION_2, new UnlockVersion2Api());
        m.put(API.UNLOCK_VERSION_3, new UnlockVersion3Api());
        m.put(API.LIST_SENT_IMAGE, new ListSentImageApi());
        m.put(API.LIST_SENT_IMAGE_WITH_USER, new ListSentImageWithUserApi());
        m.put(API.LIST_NOTIFICATION, new ListNotificationApi());
        m.put(API.TOTAL_NOTI_SEEN, new TotalNotiSeen());
        m.put(API.GET_NOTIFICATION_NUMBER, new GetNotificationNumberApi());
        m.put(API.CHANGE_EMAIL, new ChangeEmailApi());
        m.put(API.POINT_TO_MONEY, new TradePointToMoney());
        m.put(API.CHANGE_POINT, new ChangPointApi());

        m.put(API.SETTING_CALL_WAITING, new SettingCallWaiting());
        m.put(API.GET_CALL_WAITING, new GetCallWaiting());
        m.put(API.GET_CALL_LOG, new GetCallLog());
        m.put(API.GET_CONNECTION_INFOR, new ListUserConnectionInfor());
//        m.put(API.UPDATE_USER_AGE_STATUS, new UpdateUserAgeStatusApi());

        m.put(API.ADD_SALE, new AddSaleApi());
        m.put(API.GET_SALE_STATISTIC, new GetSaleStatisticApi());
        m.put(API.ADD_FREE_POINT, new AddFreePointApi());
        m.put(API.GET_FREE_POINT_HISTORY, new GetFreePointHistoryApi());
        m.put(API.UPDATE_USER_AGE_STATUS, new UpdateUserAgeStatusApi());
        m.put(API.GET_USER_AGE_STATUS, new GetUserAgeStatusApi());
        m.put(API.GET_PUSH_NOTIFICATION_SETTING, new GetPushNotificationSettingApi());
        m.put(API.PUSH_NOTIFICATION_FROM_BACK_END, new PushFromBackendApi());
        m.put(API.PUSH_NOTIFICATION_FROM_FREE_PAGE, new PushFromFreePageApi());
        m.put(API.PUSH_NOTIFICATION, new PushFromNewsApi());
        m.put(API.CHECK_CALL, new CheckCallApi());

        m.put(API.SEND_IMAGE_BY_CHAT, new SendImageByChatApi());
        m.put(API.GET_CONNECTION_POINT_ACTION, new GetConnectionPointActionApi());
        m.put(API.DELCOVERSATION, new DeleteConverstationApi());

        m.put(API.GET_CM_CODE_BY_USER_ID, new GetCmCodeByUserIdAPI());
        m.put(API.GET_USER_BY_REGISTER_TIME, new GetUserByRegisterDateAPI());

        m.put(API.GET_CONNECTION_NUMBER, new GetConnectionNumberApi());
        m.put(API.UPDATE_PREMIUM_MEMBER, new UpdatePremiumMemberApi());

        m.put(API.ADD_TEMPLATE, new AddTemplateApi());
        m.put(API.UPDATE_TEMPLATE, new UpdateTemplateApi());
        m.put(API.DELETE_TEMPLATE, new DeleteTemplateApi());
        m.put(API.LIST_TEMPLATE, new ListTemplateApi());

        m.put(API.GET_USER_STATUS_BY_EMAIL, new GetUserStatusByEmailApi());

        m.put(API.REVIEW_BUZZ, new ReviewBuzzApi());
        m.put(API.REVIEW_COMMENT, new ReviewCommentApi());
        m.put(API.REVIEW_SUB_COMMENT, new ReviewSubCommentApi());
        m.put(API.REVIEW_USER, new ReviewUserApi());

        m.put(API.GET_BACKEND_SETTING, new GetBackendSettingApi());
        
        m.put(API.CHECK_ONLINE_STATUS_BY_DEVICE, new CheckOnlineStatusByDeviceApi());
        
        m.put(API.GET_IMAGE_STATUS_INFOR, new GetImageStatusInforApi());
        
        m.put(API.RATE_USER_VOICE, new RateUserVoiceApi());
        m.put(API.ADD_ALBUM, new AddAlbumApi());
        m.put(API.DEL_ALBUM, new DelAlbumApi());
        m.put(API.UPDATE_ALBUM, new UpdateAlbumApi());
        m.put(API.LOAD_ALBUM, new LoadAlbumApi());
        m.put(API.CHECK_ALBUM_OWNED, new CheckAlbumOwnedApi());
        m.put(API.ADD_ALBUM_IMAGE, new AddAlbumImageApi());
        m.put(API.DEL_ALBUM_IMAGE, new DelAlbumImageApi());
        m.put(API.LOAD_ALBUM_IMAGE, new LoadAlbumImageApi());
        
        m.put(API.CREATE_ACCOUNT_FROM_FBID, new CreateAccountFromFbId());
    }

    public static IApiAdapter getApi(String apiName) {
        return m.get(apiName);
    }
}
