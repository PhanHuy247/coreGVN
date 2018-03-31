/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.backend.server.apimanagement;

import com.vn.ntsc.backend.entity.impl.video.ReviewVideoApi;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import eazycommon.constant.API;
import com.vn.ntsc.backend.server.respond.IApiAdapter;
import com.vn.ntsc.backend.server.respond.impl.backend.automessage.loginbonus.DeleteLoginBonusMessageApi;
import com.vn.ntsc.backend.server.respond.impl.backend.automessage.loginbonus.GetReceiversLoginBonusMessageApi;
import com.vn.ntsc.backend.server.respond.impl.backend.automessage.loginbonus.InsertLoginBonusMessageApi;
import com.vn.ntsc.backend.server.respond.impl.backend.automessage.loginbonus.ListLoginBonusMessageApi;
import com.vn.ntsc.backend.server.respond.impl.backend.automessage.loginbonus.UpdateLoginBonusMessageApi;
import com.vn.ntsc.backend.server.respond.impl.backend.news.DeleteNewsApi;
import com.vn.ntsc.backend.server.respond.impl.backend.news.GetNewsDetailApi;
import com.vn.ntsc.backend.server.respond.impl.backend.news.InsertNewsApi;
import com.vn.ntsc.backend.server.respond.impl.backend.news.ListNewsApi;
import com.vn.ntsc.backend.server.respond.impl.backend.news.UpdateNewsApi;
import com.vn.ntsc.backend.server.respond.impl.backend.news.UpdateNewsToAppApi;
import com.vn.ntsc.backend.server.respond.impl.backend.replaceword.DeleteReplaceWordApi;
import com.vn.ntsc.backend.server.respond.impl.backend.replaceword.InsertReplaceWordApi;
import com.vn.ntsc.backend.server.respond.impl.backend.replaceword.ListReplaceWordApi;
import com.vn.ntsc.backend.server.respond.impl.backend.replaceword.UpdateReplaceWordApi;
import com.vn.ntsc.backend.server.respond.impl.backend.admin.*;
import com.vn.ntsc.backend.server.respond.impl.backend.audio.ListReportedAudioApi;
import com.vn.ntsc.backend.server.respond.impl.backend.audio.ProcessReportedAudioApi;
import com.vn.ntsc.backend.server.respond.impl.backend.automessage.chat.DeleteAutoMessageApi;
import com.vn.ntsc.backend.server.respond.impl.backend.automessage.chat.GetReceiverAutoMessageApi;
import com.vn.ntsc.backend.server.respond.impl.backend.automessage.chat.InsertAutoMessageApi;
import com.vn.ntsc.backend.server.respond.impl.backend.automessage.chat.ListAutoMessageApi;
import com.vn.ntsc.backend.server.respond.impl.backend.automessage.chat.UpdateAutoMessageApi;
import com.vn.ntsc.backend.server.respond.impl.backend.automessage.notify.DeleteAutoNewsNotifyApi;
import com.vn.ntsc.backend.server.respond.impl.backend.automessage.notify.DeleteAutoNotifyApi;
import com.vn.ntsc.backend.server.respond.impl.backend.automessage.notify.DeleteQANotifyApi;
import com.vn.ntsc.backend.server.respond.impl.backend.automessage.notify.GetAutoNewsNotifyDetailApi;
import com.vn.ntsc.backend.server.respond.impl.backend.automessage.notify.GetAutoNotifyDetailApi;
import com.vn.ntsc.backend.server.respond.impl.backend.automessage.notify.GetQANotifyDetailApi;
import com.vn.ntsc.backend.server.respond.impl.backend.automessage.notify.GetReceiverAutoNewsNotifyApi;
import com.vn.ntsc.backend.server.respond.impl.backend.automessage.notify.GetReceiverAutoNotifyApi;
import com.vn.ntsc.backend.server.respond.impl.backend.automessage.notify.GetReceiverQANotifyApi;
import com.vn.ntsc.backend.server.respond.impl.backend.automessage.notify.InsertAutoNewsNotifyApi;
import com.vn.ntsc.backend.server.respond.impl.backend.automessage.notify.InsertAutoNotifyApi;
import com.vn.ntsc.backend.server.respond.impl.backend.automessage.notify.InsertQANotifyApi;
import com.vn.ntsc.backend.server.respond.impl.backend.automessage.notify.ListAutoNewsNotifyApi;
import com.vn.ntsc.backend.server.respond.impl.backend.automessage.notify.ListAutoNotifyApi;
import com.vn.ntsc.backend.server.respond.impl.backend.automessage.notify.ListQANotifyApi;
import com.vn.ntsc.backend.server.respond.impl.backend.automessage.notify.UpdateAutoNewsNotifyApi;
import com.vn.ntsc.backend.server.respond.impl.backend.automessage.notify.UpdateAutoNotifyApi;
import com.vn.ntsc.backend.server.respond.impl.backend.automessage.notify.UpdateQANotifyApi;
import com.vn.ntsc.backend.server.respond.impl.backend.bannedword.DeleteBannedWordApi;
import com.vn.ntsc.backend.server.respond.impl.backend.bannedword.InsertBannedWordApi;
import com.vn.ntsc.backend.server.respond.impl.backend.bannedword.ListBannedWordApi;
import com.vn.ntsc.backend.server.respond.impl.backend.bannedword.UpdateBannedWordApi;
import com.vn.ntsc.backend.server.respond.impl.backend.buzz.BuzzDetailApi;
import com.vn.ntsc.backend.server.respond.impl.backend.buzz.DeleteBuzzApi;
import com.vn.ntsc.backend.server.respond.impl.backend.buzz.DeleteCommentApi;
import com.vn.ntsc.backend.server.respond.impl.backend.buzz.DeleteSubCommentApi;
import com.vn.ntsc.backend.server.respond.impl.backend.buzz.GetCommentApi;
import com.vn.ntsc.backend.server.respond.impl.backend.buzz.GetLogCommentApi;
import com.vn.ntsc.backend.server.respond.impl.backend.buzz.ListLikeApi;
import com.vn.ntsc.backend.server.respond.impl.backend.buzz.ListReviewingBuzzApi;
import com.vn.ntsc.backend.server.respond.impl.backend.buzz.ListReviewingCommentApi;
import com.vn.ntsc.backend.server.respond.impl.backend.buzz.ListReviewingSubCommentApi;
import com.vn.ntsc.backend.server.respond.impl.backend.buzz.ListSubCommentApi;
import com.vn.ntsc.backend.server.respond.impl.backend.buzz.ReviewBuzzApi;
import com.vn.ntsc.backend.server.respond.impl.backend.buzz.ReviewCommentApi;
import com.vn.ntsc.backend.server.respond.impl.backend.buzz.ReviewSubCommentApi;
import com.vn.ntsc.backend.server.respond.impl.backend.buzz.SearchLogBuzzApi;
import com.vn.ntsc.backend.server.respond.impl.backend.cmcode.CMCodeStatisticApi;
import com.vn.ntsc.backend.server.respond.impl.backend.cmcode.GetAllCMCodeApi;
import com.vn.ntsc.backend.server.respond.impl.backend.cmcode.GetCMCodeDetailApi;
import com.vn.ntsc.backend.server.respond.impl.backend.cmcode.InitRegisterCMCodeApi;
import com.vn.ntsc.backend.server.respond.impl.backend.cmcode.InsertAfficiateApi;
import com.vn.ntsc.backend.server.respond.impl.backend.cmcode.InsertCMCodeApi;
import com.vn.ntsc.backend.server.respond.impl.backend.cmcode.InsertMediaApi;
import com.vn.ntsc.backend.server.respond.impl.backend.cmcode.ListAfficiateApi;
import com.vn.ntsc.backend.server.respond.impl.backend.cmcode.ListCMCodeApi;
import com.vn.ntsc.backend.server.respond.impl.backend.cmcode.ListMediaApi;
import com.vn.ntsc.backend.server.respond.impl.backend.cmcode.UpdateAfficiateApi;
import com.vn.ntsc.backend.server.respond.impl.backend.cmcode.UpdateCMCodeApi;
import com.vn.ntsc.backend.server.respond.impl.backend.cmcode.UpdateMediaApi;
import com.vn.ntsc.backend.server.respond.impl.backend.emoji.AddEmojiApi;
import com.vn.ntsc.backend.server.respond.impl.backend.emoji.AddEmojiCategoryApi;
import com.vn.ntsc.backend.server.respond.impl.backend.emoji.DelEmojiApi;
import com.vn.ntsc.backend.server.respond.impl.backend.emoji.DelEmojiCategoryApi;
import com.vn.ntsc.backend.server.respond.impl.backend.emoji.EditEmojiApi;
import com.vn.ntsc.backend.server.respond.impl.backend.emoji.EditEmojiCategoryApi;
import com.vn.ntsc.backend.server.respond.impl.backend.emoji.OrderEmojiApi;
import com.vn.ntsc.backend.server.respond.impl.backend.extrapage.DeleteExtraPageApi;
import com.vn.ntsc.backend.server.respond.impl.backend.extrapage.InsertExtraPageApi;
import com.vn.ntsc.backend.server.respond.impl.backend.extrapage.ListExtraPageApi;
import com.vn.ntsc.backend.server.respond.impl.backend.extrapage.UpdateExtraPageApi;
import com.vn.ntsc.backend.server.respond.impl.backend.freepoint.InsertFreePointApi;
import com.vn.ntsc.backend.server.respond.impl.backend.freepoint.ListFreePointApi;
import com.vn.ntsc.backend.server.respond.impl.backend.freepoint.UpdateFreePointApi;
import com.vn.ntsc.backend.server.respond.impl.backend.gift.DeleteGiftApi;
import com.vn.ntsc.backend.server.respond.impl.backend.gift.InsertGiftApi;
import com.vn.ntsc.backend.server.respond.impl.backend.gift.ListGiftApi;
import com.vn.ntsc.backend.server.respond.impl.backend.gift.ListGiftCategoryApi;
import com.vn.ntsc.backend.server.respond.impl.backend.gift.OrderGiftApi;
import com.vn.ntsc.backend.server.respond.impl.backend.gift.UpdateGiftApi;
import com.vn.ntsc.backend.server.respond.impl.backend.gift.UpdateGiftCategoryApi;
import com.vn.ntsc.backend.server.respond.impl.backend.gift.UpdateGiftImageApi;
import com.vn.ntsc.backend.server.respond.impl.backend.image.DeleteImageApi;
import com.vn.ntsc.backend.server.respond.impl.backend.image.GetImageInforApi;
import com.vn.ntsc.backend.server.respond.impl.backend.image.ListImageApi;
import com.vn.ntsc.backend.server.respond.impl.backend.image.ListReportImageApi;
import com.vn.ntsc.backend.server.respond.impl.backend.image.ProcessReportedImageApi;
import com.vn.ntsc.backend.server.respond.impl.backend.image.ReviewImageApi;
import com.vn.ntsc.backend.server.respond.impl.backend.log.GetLogUserInfoApi;
import com.vn.ntsc.backend.server.respond.impl.backend.log.GetMoneyTradePointApi;
import com.vn.ntsc.backend.server.respond.impl.backend.log.ListDetailWinkBombApi;
import com.vn.ntsc.backend.server.respond.impl.backend.log.ListLogPointApi;
import com.vn.ntsc.backend.server.respond.impl.backend.log.ListLogPurchaseApi;
import com.vn.ntsc.backend.server.respond.impl.backend.log.SearchLogBlockApi;
import com.vn.ntsc.backend.server.respond.impl.backend.log.SearchLogCallApi;
import com.vn.ntsc.backend.server.respond.impl.backend.log.SearchLogChatApi;
import com.vn.ntsc.backend.server.respond.impl.backend.log.SearchLogCheckOutApi;
import com.vn.ntsc.backend.server.respond.impl.backend.log.SearchLogDeactivateApi;
import com.vn.ntsc.backend.server.respond.impl.backend.log.SearchLogFavouristApi;
import com.vn.ntsc.backend.server.respond.impl.backend.log.SearchLogLoginApi;
import com.vn.ntsc.backend.server.respond.impl.backend.log.SearchLogLookApi;
import com.vn.ntsc.backend.server.respond.impl.backend.log.SearchLogNotificationApi;
import com.vn.ntsc.backend.server.respond.impl.backend.log.SearchLogOnlineAlertApi;
import com.vn.ntsc.backend.server.respond.impl.backend.log.SearchLogPointApi;
import com.vn.ntsc.backend.server.respond.impl.backend.log.SearchLogPurchaseApi;
import com.vn.ntsc.backend.server.respond.impl.backend.log.SearchLogShakeChatApi;
import com.vn.ntsc.backend.server.respond.impl.backend.log.SearchLogWinkApi;
import com.vn.ntsc.backend.server.respond.impl.backend.log.SearchLogWinkBombApi;
import com.vn.ntsc.backend.server.respond.impl.backend.log.SearchReportAudioApi;
import com.vn.ntsc.backend.server.respond.impl.backend.log.SearchReportImageApi;
import com.vn.ntsc.backend.server.respond.impl.backend.log.SearchReportUserApi;
import com.vn.ntsc.backend.server.respond.impl.backend.log.SearchReportVideoApi;
import com.vn.ntsc.backend.server.respond.impl.backend.log.ViewTotalPointApi;
import com.vn.ntsc.backend.server.respond.impl.backend.log.ViewTotalPriceApi;
import com.vn.ntsc.backend.server.respond.impl.backend.pointpackage.DeleteActionPointpackageApi;
import com.vn.ntsc.backend.server.respond.impl.backend.pointpackage.DeletePointpackageApi;
import com.vn.ntsc.backend.server.respond.impl.backend.pointpackage.InsertActionPointPackageApi;
import com.vn.ntsc.backend.server.respond.impl.backend.pointpackage.InsertPointPackageApi;
import com.vn.ntsc.backend.server.respond.impl.backend.pointpackage.ListActionPointPackageApi;
import com.vn.ntsc.backend.server.respond.impl.backend.pointpackage.ListPointPackageApi;
import com.vn.ntsc.backend.server.respond.impl.backend.pointpackage.UpdateActionPointPackageApi;
import com.vn.ntsc.backend.server.respond.impl.backend.pointpackage.UpdatePointPackageApi;
import com.vn.ntsc.backend.server.respond.impl.backend.setting.GetGeneralSettingApi;
import com.vn.ntsc.backend.server.respond.impl.backend.setting.GetOtherSettingAppApi;
import com.vn.ntsc.backend.server.respond.impl.backend.setting.GetUploadSetting;
import com.vn.ntsc.backend.server.respond.impl.backend.setting.SetCommunicationSettingApi;
import com.vn.ntsc.backend.server.respond.impl.backend.setting.SetConnectionPointSettingApi;
import com.vn.ntsc.backend.server.respond.impl.backend.setting.SetDistanceSettingApi;
import com.vn.ntsc.backend.server.respond.impl.backend.setting.SetOtherSettingApi;
import com.vn.ntsc.backend.server.respond.impl.backend.setting.SetOtherSettingAppApi;
import com.vn.ntsc.backend.server.respond.impl.backend.setting.SetPointSettingApi;
import com.vn.ntsc.backend.server.respond.impl.backend.setting.SetPrioritizeUserBuzzSetting;
import com.vn.ntsc.backend.server.respond.impl.backend.setting.SetUploadSetting;
import com.vn.ntsc.backend.server.respond.impl.backend.setting.SetVersionSettingApi;
import com.vn.ntsc.backend.server.respond.impl.backend.statistic.InstallationStatisticApi;
import com.vn.ntsc.backend.server.respond.impl.backend.sticker.DeleteStickerApi;
import com.vn.ntsc.backend.server.respond.impl.backend.sticker.DeleteStickerCategoryApi;
import com.vn.ntsc.backend.server.respond.impl.backend.sticker.DeleteStickerVer2Api;
import com.vn.ntsc.backend.server.respond.impl.backend.sticker.InsertStickerApi;
import com.vn.ntsc.backend.server.respond.impl.backend.sticker.InsertStickerCategoryApi;
import com.vn.ntsc.backend.server.respond.impl.backend.sticker.InsertStickerCategoryVer2Api;
import com.vn.ntsc.backend.server.respond.impl.backend.sticker.InsertStickerVer2Api;
import com.vn.ntsc.backend.server.respond.impl.backend.sticker.ListStickerApi;
import com.vn.ntsc.backend.server.respond.impl.backend.sticker.ListStickerCategoryApi;
import com.vn.ntsc.backend.server.respond.impl.backend.sticker.OrderStickerApi;
import com.vn.ntsc.backend.server.respond.impl.backend.sticker.OrderStickerVer2Api;
import com.vn.ntsc.backend.server.respond.impl.backend.sticker.PublicStickerCategoryApi;
import com.vn.ntsc.backend.server.respond.impl.backend.sticker.UpdateStickerCategoryApi;
import com.vn.ntsc.backend.server.respond.impl.backend.sticker.UpdateStickerCategoryVer2Api;
import com.vn.ntsc.backend.server.respond.impl.backend.sticker.UpdateStickerImageApi;
import com.vn.ntsc.backend.server.respond.impl.backend.transaction.TransactionStatisticApi;
import com.vn.ntsc.backend.server.respond.impl.backend.user.AddPointApi;
import com.vn.ntsc.backend.server.respond.impl.backend.user.AddPointByListApi;
import com.vn.ntsc.backend.server.respond.impl.backend.user.AddPurchaseByAdmin;
import com.vn.ntsc.backend.server.respond.impl.backend.user.DetailUserApi;
import com.vn.ntsc.backend.server.respond.impl.backend.user.GetSystemAccountApi;
import com.vn.ntsc.backend.server.respond.impl.backend.user.GetUserOnlineApi;
import com.vn.ntsc.backend.server.respond.impl.backend.user.ListConnectionApi;
import com.vn.ntsc.backend.server.respond.impl.backend.user.ListReviewingUserApi;
import com.vn.ntsc.backend.server.respond.impl.backend.user.ResetPasswordApi;
import com.vn.ntsc.backend.server.respond.impl.backend.user.ReviewUserApi;
import com.vn.ntsc.backend.server.respond.impl.backend.user.SearchConnectionApi;
import com.vn.ntsc.backend.server.respond.impl.backend.user.SearchUserApi;
import com.vn.ntsc.backend.server.respond.impl.backend.user.UserStatisticApi;
import com.vn.ntsc.backend.server.respond.impl.backend.video.ListReportedVideoApi;
import com.vn.ntsc.backend.server.respond.impl.backend.video.ListVideoApi;
import com.vn.ntsc.backend.server.respond.impl.backend.video.ProcessReportedVideoApi;
import com.vn.ntsc.backend.server.respond.impl.tool.adminstring.*;
import com.vn.ntsc.backend.server.respond.impl.tool.api.*;
import com.vn.ntsc.backend.server.respond.impl.tool.init.*;
import com.vn.ntsc.backend.server.respond.impl.tool.screen.*;
import com.vn.ntsc.backend.server.respond.impl.tool.screengroup.*;

/**
 *
 * @author RuAc0n
 */
public class APIManager {

    public static final TreeMap<String, IApiAdapter> mTool = new TreeMap<>();
    public static final TreeMap<String, IApiAdapter> m = new TreeMap<>();

    static {
        mTool.put(API.RESET_CONFIG, new ResetConfigApi());

        mTool.put(API.INSERT_SCREEN_GROUP, new InsertScreenGroupApi());
        mTool.put(API.UPDATE_SCREEN_GROUP, new UpdateScreenGroupApi());
        mTool.put(API.LIST_SCREEN_GROUP, new ListScreenGroupApi());
        mTool.put(API.INSERT_SCREEN, new InsertScreenApi());
        mTool.put(API.UPDATE_SCREEN, new UpdateScreenApi());
        mTool.put(API.LIST_SCREEN, new ListScreenApi());
        mTool.put(API.DELETE_SCREEN, new DeleteScreenApi());
        mTool.put(API.DELETE_SCREEN_GROUP, new DeleteScreenGroupApi());
        mTool.put(API.LIST_API, new ListAPIApi());
        mTool.put(API.SET_SCREEN_API, new SetScreenAPIApi());
        mTool.put(API.LOGIN_ADMINISTRATOR, new LoginApi());
        mTool.put(API.CHAGE_PASSWORD_ADMIN, new ChangePasswordApi());
        mTool.put(API.INIT, new InitApi());
        mTool.put(API.GET_STRING, new GetStringApi());
        mTool.put(API.SET_STRING, new SetStringApi());
    }

    static {
        
        m.put(API.REGISTER_ADMIN, new RegisterAdminApi());
        m.put(API.GET_ADMIN_DETAIL, new GetAdminDetailApi());
        m.put(API.UPDATE_ADMIN, new UpdateAdminApi());
        m.put(API.DELETE_ADMIN, new DeleteAdminApi());
        m.put(API.LIST_ADMIN, new ListAdminApi());
        m.put(API.INSERT_ROLE, new InsertRoleApi());
        m.put(API.GET_ROLE_DETAIL, new GetRoleDetailApi());
        m.put(API.UPDATE_ROLE, new UpdateRoleApi());
        m.put(API.DELETE_ROLE, new DeleteRoleApi());
        m.put(API.LIST_ROLE, new ListRoleApi());
        m.put(API.LIST_AFFICIATE, new ListAfficiateApi());
        m.put(API.UPDATE_AFFICIATE, new UpdateAfficiateApi());
        m.put(API.INSERT_AFFICIATE, new InsertAfficiateApi());
        m.put(API.LIST_MEDIA, new ListMediaApi());
        m.put(API.INSERT_MEDIA, new InsertMediaApi());
        m.put(API.UPDATE_MEDIA, new UpdateMediaApi());
        m.put(API.LIST_MEDIA, new ListMediaApi());
        m.put(API.INIT_REGISTER_CM_CODE, new InitRegisterCMCodeApi());
        m.put(API.INSERT_CM_CODE, new InsertCMCodeApi());
        m.put(API.GET_CM_CODE_DETAIL, new GetCMCodeDetailApi());
        m.put(API.UPDATE_CM_CODE, new UpdateCMCodeApi());
        m.put(API.LIST_CM_CODE, new ListCMCodeApi());
        m.put(API.GET_ALL_CM_CODE, new GetAllCMCodeApi());
        m.put(API.CM_CODE_STATISTIC, new CMCodeStatisticApi());
        m.put(API.USER_STATISTIC, new UserStatisticApi());
        m.put(API.TRANSACTION_STATISTIC, new TransactionStatisticApi());
        m.put(API.GET_SYSTEM_ACC, new GetSystemAccountApi());
        m.put(API.INSERT_AUTO_MESSAGE, new InsertAutoMessageApi());
        m.put(API.UPDATE_AUTO_MESSAGE, new UpdateAutoMessageApi());
        m.put(API.LIST_AUTO_MESSAGE, new ListAutoMessageApi());
        m.put(API.DELETE_AUTO_MESSAGE, new DeleteAutoMessageApi());
        m.put(API.GET_RECEIVERS_AUTO_MESSAGE, new GetReceiverAutoMessageApi());
        m.put(API.GET_RECEIVERS_AUTO_NOTIFY, new GetReceiverAutoNotifyApi());
        m.put(API.GET_AUTO_NOTIFY_DETAIL, new GetAutoNotifyDetailApi());
        m.put(API.INSERT_AUTO_PUSH, new InsertAutoNotifyApi());
        m.put(API.UPDATE_AUTO_PUSH, new UpdateAutoNotifyApi());
        m.put(API.LIST_AUTO_PUSH, new ListAutoNotifyApi());
        m.put(API.DELETE_AUTO_PUSH, new DeleteAutoNotifyApi());
        m.put(API.GET_RECEIVERS_AUTO_NEWS_NOTIFY, new GetReceiverAutoNewsNotifyApi());
        m.put(API.GET_AUTO_NEWS_NOTIFY_DETAIL, new GetAutoNewsNotifyDetailApi());
        m.put(API.INSERT_AUTO_NEWS_PUSH, new InsertAutoNewsNotifyApi());
        m.put(API.UPDATE_AUTO_NEWS_PUSH, new UpdateAutoNewsNotifyApi());
        //Linh add
        m.put(API.INSERT_QA_PUSH, new InsertQANotifyApi());
        m.put(API.UPDATE_QA_PUSH, new UpdateQANotifyApi());
        m.put(API.DELETE_QA_PUSH, new DeleteQANotifyApi());
        m.put(API.LIST_QA_PUSH, new ListQANotifyApi());
        m.put(API.GET_RECEIVERS_QA_PUSH, new GetReceiverQANotifyApi());
        m.put(API.GET_QA_PUSH, new GetQANotifyDetailApi());
        
        m.put(API.LIST_AUTO_NEWS_PUSH, new ListAutoNewsNotifyApi());
        m.put(API.DELETE_AUTO_NEWS_PUSH, new DeleteAutoNewsNotifyApi());
        m.put(API.INSERT_BANNED_WORD, new InsertBannedWordApi());
        m.put(API.UPDATE_BANNED_WORD, new UpdateBannedWordApi());
        m.put(API.DELETE_BANNED_WORD, new DeleteBannedWordApi());
        m.put(API.LIST_BANNED_WORD, new ListBannedWordApi());
        
        // LongLT 8/2016
        m.put(API.INSERT_REPLACE_WORD, new InsertReplaceWordApi());
        m.put(API.UPDATE_REPLACE_WORD, new UpdateReplaceWordApi());
        m.put(API.DELETE_REPLACE_WORD, new DeleteReplaceWordApi());
        m.put(API.LIST_REPLACE_WORD, new ListReplaceWordApi());
        
        m.put(API.GET_USER_ONLINE, new GetUserOnlineApi());
        m.put(API.GET_GENERAL_SETTING, new GetGeneralSettingApi());
        m.put(API.SET_POINT_SETTING, new SetPointSettingApi());
        m.put(API.SET_DISTANCE_SETTING, new SetDistanceSettingApi());
        m.put(API.SET_OTHER_SETTING, new SetOtherSettingApi());
        m.put(API.SET_VERSION_SETTING, new SetVersionSettingApi());
        m.put(API.SET_COMMUNICATION_SETTING, new SetCommunicationSettingApi());
        m.put(API.SET_CONNECTION_POINT_SETTING, new SetConnectionPointSettingApi());
        m.put(API.GET_STATIC_PAGE, null);
        m.put(API.UPDATE_STATIC_PAGE, null);
        m.put(API.UPDATE_USER_INF_BY_ADMIN, null);
        m.put(API.REGISTER_BY_ADMIN, null);
        m.put(API.RESET_PASSWORD, new ResetPasswordApi());
        m.put(API.SEARCH_USER, new SearchUserApi());
        m.put(API.DETAIL_USER, new DetailUserApi());
        m.put(API.ADD_POINT, new AddPointApi());
        m.put(API.ADD_POINT_BY_LIST, new AddPointByListApi());
        m.put(API.SEARCH_CONNECTION, new SearchConnectionApi());
        m.put(API.LIST_CONNECTION, new ListConnectionApi());
        m.put(API.LIST_LOG_POINT, new ListLogPointApi());
        m.put(API.SEARCH_LOG_BLOCK, new SearchLogBlockApi());
        m.put(API.SEARCH_LOG_LOGIN, new SearchLogLoginApi());
        m.put(API.SEARCH_LOG_DEACTIVATE, new SearchLogDeactivateApi());
        m.put(API.SEARCH_LOG_LOOK, new SearchLogLookApi());
        m.put(API.SEARCH_LOG_ONLINE_ALERT, new SearchLogOnlineAlertApi());
        m.put(API.SEARCH_LOG_POINT, new SearchLogPointApi());
        m.put(API.SEARCH_LOG_WINK_BOMB, new SearchLogWinkBombApi());
        m.put(API.DETAIL_WINK_BOMB, new ListDetailWinkBombApi());
        m.put(API.SEARCH_REPORT_USER, new SearchReportUserApi());
        m.put(API.SEARCH_REPORT_IMAGE, new SearchReportImageApi());
        m.put(API.SEARCH_REPORT_VIDEO, new SearchReportVideoApi());
        m.put(API.SEARCH_REPORT_AUDIO, new SearchReportAudioApi());
        m.put(API.SEARCH_LOG_CHAT, new SearchLogChatApi());
        m.put(API.SEARCH_LOG_CHECK_OUT, new SearchLogCheckOutApi());
        m.put(API.SEARCH_LOG_WINK, new SearchLogWinkApi());
        m.put(API.SEARCH_LOG_SHAKE_CHAT, new SearchLogShakeChatApi());
        m.put(API.SEARCH_LOG_FAVOURIST, new SearchLogFavouristApi());
        m.put(API.SEARCH_LOG_NOTIFICATION, new SearchLogNotificationApi());
        m.put(API.SEARCH_LOG_CALL, new SearchLogCallApi());
        m.put(API.SEARCH_LOG_BUZZ, new SearchLogBuzzApi());
        m.put(API.BUZZ_DETAIL, new BuzzDetailApi());
        m.put(API.GET_COMMENT, new GetCommentApi());
        m.put(API.LIST_LIKE, new ListLikeApi());
        m.put(API.DEL_BUZZ_ADMIN, new DeleteBuzzApi());
        m.put(API.DEL_COMMENT_ADMIN, new DeleteCommentApi());
        m.put(API.GET_LIST_SUB_COMMENT_BY_ADMIN, new ListSubCommentApi());
        m.put(API.DELETE_SUB_COMMENT_BY_ADMIN, new DeleteSubCommentApi());
        // m.put(API.insert_gift_category, new InsertGiftCategoryApi());
        m.put(API.UPDATE_GIFT_CATEGORY, new UpdateGiftCategoryApi());
        //  m.put(API.delete_gift_category, new DeleteGiftCategoryApi());
        m.put(API.LIST_GIFT_CATEGORY, new ListGiftCategoryApi());
        m.put(API.INSERT_GIFT, new InsertGiftApi());
        m.put(API.UPDATE_GIFT, new UpdateGiftApi());
        m.put(API.UPDATE_GIFT_IMAGE, new UpdateGiftImageApi());
        m.put(API.DELETE_GIFT, new DeleteGiftApi());
        m.put(API.LIST_GIFT, new ListGiftApi());
        m.put(API.ORDER_GIFT, new OrderGiftApi());
        m.put(API.INSERT_STICKER_CATEGORY, new InsertStickerCategoryApi());
        m.put(API.UPDATE_STICKER_CATEGORY_IMAGE, new UpdateStickerImageApi());
        m.put(API.UPDATE_STICKER_CATEGORY, new UpdateStickerCategoryApi());
        m.put(API.DELETE_STICKER_CATEGORY, new DeleteStickerCategoryApi());
        m.put(API.LIST_STICKER_CATEGORY, new ListStickerCategoryApi());
        m.put(API.PUBLIC_STICKER_CATEGORY, new PublicStickerCategoryApi());
        m.put(API.INSERT_STICKER, new InsertStickerApi());
        m.put(API.UPDATE_STICKER_IMAGE, new UpdateStickerImageApi());
        m.put(API.DELETE_STICKER, new DeleteStickerApi());
        m.put(API.LIST_STICKER, new ListStickerApi());
        m.put(API.ORDER_STICKER, new OrderStickerApi());
        m.put(API.INSERT_STICKER_CATEGORY_VER_2, new InsertStickerCategoryVer2Api());
        m.put(API.UPDATE_STICKER_CATEGORY_VER_2, new UpdateStickerCategoryVer2Api());
        m.put(API.INSERT_STICKER_VER_2, new InsertStickerVer2Api());
        m.put(API.DELETE_STICKER_VER_2, new DeleteStickerVer2Api());
        m.put(API.ORDER_STICKER_VER_2, new OrderStickerVer2Api());
        m.put(API.LIST_IMAGE, new ListImageApi());
        m.put(API.REVIEW_IMAGE, new ReviewImageApi());
        m.put(API.DELETE_IMAGE, new DeleteImageApi());
        m.put(API.GET_IMAGE_INFOR, new GetImageInforApi());
        m.put(API.INSERT_EXTRA_PAGE, new InsertExtraPageApi());
        m.put(API.UPDATE_EXTRA_PAGE, new UpdateExtraPageApi());
        m.put(API.DELETE_EXTRA_PAGE, new DeleteExtraPageApi());
        m.put(API.LIST_EXTRA_PAGE, new ListExtraPageApi());
        m.put(API.SEARCH_LOG_PURCHASE, new SearchLogPurchaseApi());
        m.put(API.LIST_LOG_PURCHASE, new ListLogPurchaseApi());
        m.put(API.LIST_REPORTED_IMAGE, new ListReportImageApi());
        m.put(API.LIST_REPORTED_VIDEO, new ListReportedVideoApi());
        m.put(API.LIST_REPORTED_AUDIO, new ListReportedAudioApi());
        m.put(API.PROCESS_REPORTED_IMAGE, new ProcessReportedImageApi());
        m.put(API.PROCESS_REPORTED_VIDEO, new ProcessReportedVideoApi());
        m.put(API.PROCESS_REPORTED_AUDIO, new ProcessReportedAudioApi());
        m.put(API.INSERT_FREE_POINT, new InsertFreePointApi());
        m.put(API.UPDATE_FREE_POINT, new UpdateFreePointApi());
        m.put(API.LIST_FREE_POINT, new ListFreePointApi());
        
        //add by Huy 201710Oct
        m.put(API.LIST_VIDEO, new ListVideoApi());
        m.put(API.REVIEW_VIDEO, new ReviewVideoApi());
        
        m.put(API.INSERT_POINT_PACKAGE, new InsertPointPackageApi());
        m.put(API.UPDATE_POINT_PACKAGE, new UpdatePointPackageApi());
        m.put(API.DELETE_POINT_PACKAGE, new DeletePointpackageApi());
        m.put(API.LIST_POINT_PACKAGE, new ListPointPackageApi());

        m.put(API.INSERT_ACTION_POINT_PACKAGE, new InsertActionPointPackageApi());
        m.put(API.UPDATE_ACTION_POINT_PACKAGE, new UpdateActionPointPackageApi());
        m.put(API.DELETE_ACTION_POINT_PACKAGE, new DeleteActionPointpackageApi());
        m.put(API.LIST_ACTION_POINT_PACKAGE, new ListActionPointPackageApi());

        m.put(API.INSERT_NEWS, new InsertNewsApi());
        m.put(API.UPDATE_NEWS, new UpdateNewsApi());
        m.put(API.DELETE_NEWS, new DeleteNewsApi());
        m.put(API.GET_NEWS_DETAIL, new GetNewsDetailApi());
        m.put(API.LIST_NEWS_BACKEND, new ListNewsApi());
        m.put(API.UPDATE_NEWS_TO_APP, new UpdateNewsToAppApi());

        m.put(API.INSERT_LOGIN_BONUS_MESSAGE, new InsertLoginBonusMessageApi());
        m.put(API.UPDATE_LOGIN_BONUS_MESSAGE, new UpdateLoginBonusMessageApi());
        m.put(API.DELETE_LOGIN_BONUS_MESSAGE, new DeleteLoginBonusMessageApi());
        m.put(API.LIST_LOGIN_BONUS_MESSAGE, new ListLoginBonusMessageApi());
        m.put(API.GET_RECEIVERS_LOGIN_BONUS_MESSAGE, new GetReceiversLoginBonusMessageApi());

        m.put(API.INSTALLATION_STATISTIC, new InstallationStatisticApi());
        m.put(API.GET_REVIEWING_BUZZ, new ListReviewingBuzzApi());
        m.put(API.REVIEW_BUZZ, new ReviewBuzzApi());
        m.put(API.GET_REVIEWING_COMMENT, new ListReviewingCommentApi());
        m.put(API.REVIEW_COMMENT, new ReviewCommentApi());
        m.put(API.GET_REVIEWING_SUB_COMMENT, new ListReviewingSubCommentApi());
        m.put(API.REVIEW_SUB_COMMENT, new ReviewSubCommentApi());
        m.put(API.GET_REVIEW_USER, new ListReviewingUserApi());
        m.put(API.REVIEW_USER, new ReviewUserApi());
        // ThanhDD 03/10/2016 #4789,
        m.put(API.VIEW_TOTAL_PRICE, new ViewTotalPriceApi());
        //#4311
        m.put(API.VIEW_TOTAL_POINT, new ViewTotalPointApi());
        m.put(API.GET_MONEY_TRADE_FROM_POINT, new GetMoneyTradePointApi());
        // NamHv 20/04/2017 #8001
        m.put(API.GET_LOG_COMMENT, new GetLogCommentApi());
        // #8001
        
        //HoangNH #emoji
        m.put(API.ADD_EMOJI_CAT, new AddEmojiCategoryApi());
        m.put(API.ADD_EMOJI, new AddEmojiApi());
        m.put(API.EDIT_EMOJI_CAT, new EditEmojiCategoryApi());
        m.put(API.DEL_EMOJI_CAT, new DelEmojiCategoryApi());
        m.put(API.EDIT_EMOJI, new EditEmojiApi());
        m.put(API.DEL_EMOJI, new DelEmojiApi());
        m.put(API.ORDER_EMOJI, new OrderEmojiApi());

//        m.put(API.GET_ADMIN_SETTING, new GetAdminSettingApi());  
//        
//        m.put(API.SET_ADMIN_SETTING, new SetAdminSettingApi());        
        m.put(API.ADD_PURCHASE_BY_AMIN, new AddPurchaseByAdmin());// LongLT 8/8/2016
        m.put(API.GET_LOG_USER_INFO, new GetLogUserInfoApi());// Linh add 2017/04/17
        m.put(API.SET_OTHER_SETTING_APP, new SetOtherSettingAppApi()); //Thanhdd add 20/07/2017
        m.put(API.OTHER_SETTING_APP, new GetOtherSettingAppApi());
        
        m.put(API.SET_UPLOAD_SETTING, new SetUploadSetting());
        m.put(API.GET_UPLOAD_SETTING, new GetUploadSetting());
        
        m.put(API.SETTING_PRIORITIZE_USER_BUZZ, new SetPrioritizeUserBuzzSetting());
    }

    public static IApiAdapter getToolApi(String apiName) {
        return mTool.get(apiName);
    }

    public static IApiAdapter getApi(String apiName) {
        return m.get(apiName);
    }

    public static List<String> getAllApiName() {
        List<String> result = new ArrayList<>();
        for (Map.Entry<String, IApiAdapter> pair : m.entrySet()) {
            String api = pair.getKey();
            result.add(api);
        }
        return result;
    }
}
