/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.otherservice.servicemanagement.respond;

import com.vn.ntsc.otherservice.servicemanagement.respond.impl.BuyStickerByMoneyAndroid;
import com.vn.ntsc.otherservice.servicemanagement.respond.impl.BuyStickerByMoneyIOSApi;
import com.vn.ntsc.otherservice.servicemanagement.respond.impl.BuyStickerByPointApi;
import com.vn.ntsc.otherservice.servicemanagement.respond.impl.CallLogApi;
import com.vn.ntsc.otherservice.servicemanagement.respond.impl.ClickPushNotificationApi;
import com.vn.ntsc.otherservice.servicemanagement.respond.impl.ConfirmPurchaseAmazon;
import com.vn.ntsc.otherservice.servicemanagement.respond.impl.ConfirmPurchaseAndroid;
import com.vn.ntsc.otherservice.servicemanagement.respond.impl.ConfirmPurchaseIOS;
import com.vn.ntsc.otherservice.servicemanagement.respond.impl.DownloadStickerCategoryApi;
import com.vn.ntsc.otherservice.servicemanagement.respond.impl.EndCallApi;
import com.vn.ntsc.otherservice.servicemanagement.respond.impl.GetAllGiftApi;
import com.vn.ntsc.otherservice.servicemanagement.respond.impl.GetBannedWordApi;
import com.vn.ntsc.otherservice.servicemanagement.respond.impl.GetExtraPageApi;
import com.vn.ntsc.otherservice.servicemanagement.respond.impl.GetListGiftApi;
import com.vn.ntsc.otherservice.servicemanagement.respond.impl.GetReplaceWordApi;
import com.vn.ntsc.otherservice.servicemanagement.respond.impl.GetStaticPageApi;
import com.vn.ntsc.otherservice.servicemanagement.respond.impl.GetStickerShopInforApi;
import com.vn.ntsc.otherservice.servicemanagement.respond.impl.InstallApplicationApi;
import com.vn.ntsc.otherservice.servicemanagement.respond.impl.ListActionPointPacketApi;
import com.vn.ntsc.otherservice.servicemanagement.respond.impl.ListActionPointPacketVersion2Api;
import com.vn.ntsc.otherservice.servicemanagement.respond.impl.ListAutoNewsNotifyApi;
import com.vn.ntsc.otherservice.servicemanagement.respond.impl.ListDefaultStickerCategoryApi;
import com.vn.ntsc.otherservice.servicemanagement.respond.impl.ListNewsApi;
import com.vn.ntsc.otherservice.servicemanagement.respond.impl.ListPointPacketApi;
import com.vn.ntsc.otherservice.servicemanagement.respond.impl.LogBeforePurchaseApi;
import com.vn.ntsc.otherservice.servicemanagement.respond.impl.LogBeforePurchaseVersion2Api;
import com.vn.ntsc.otherservice.servicemanagement.respond.impl.MakeCallApi;
import com.vn.ntsc.otherservice.servicemanagement.respond.impl.RestoreStickerByPointApi;
import com.vn.ntsc.otherservice.servicemanagement.respond.impl.SearchStickerCategoryApi;
import com.vn.ntsc.otherservice.servicemanagement.respond.impl.StartCallApi;
import com.vn.ntsc.otherservice.servicemanagement.respond.impl.StaticFileApi;
import com.vn.ntsc.otherservice.servicemanagement.respond.impl.StickerCategoryDetailApi;
import com.vn.ntsc.otherservice.servicemanagement.respond.impl.StickerCategoryListApi;
import com.vn.ntsc.otherservice.servicemanagement.respond.impl.TotalBadgeApi;
import com.vn.ntsc.otherservice.servicemanagement.respond.impl.TotalNotiSeen;
import com.vn.ntsc.otherservice.servicemanagement.respond.impl.UpdateServerTimeApi;
import com.vn.ntsc.otherservice.servicemanagement.respond.impl.UpdateStaticPageApi;
import java.util.TreeMap;
import eazycommon.constant.API;

/**
 *
 * @author RuAc0n
 */
public class APIManager {

    private static final TreeMap<String, IApiAdapter> m = new TreeMap<>();

    static {
        //m.put(API.GET_ALL_GIFT_CATEGORY, new GetAllGiftCategoryApi());
        // m.put(API.GET_GIFT_CATEGORY, new GetGiftCategoryApi());
        m.put(API.GET_ALL_GIFT, new GetAllGiftApi());
        m.put(API.GET_LIST_GIFT, new GetListGiftApi());
        m.put(API.DOWNLOAD_STICKER_CATEGORY, new DownloadStickerCategoryApi());
        m.put(API.RESTORE_STICKER_BY_MONEY, new DownloadStickerCategoryApi());

        m.put(API.CALL_LOG, new CallLogApi());
        m.put(API.GET_EXTRA_PAGE, new GetExtraPageApi());
        m.put(API.GET_STATIC_PAGE, new GetStaticPageApi());
        m.put(API.STATIC_PAGE, new GetStaticPageApi());

        m.put(API.UPDATE_STATIC_PAGE, new UpdateStaticPageApi());
        m.put(API.LIST_POINT_PACKET, new ListPointPacketApi());
        m.put(API.LIST_ACTION_POINT_PACKET, new ListActionPointPacketApi());
        m.put(API.LIST_ACTION_POINT_PACKET_VERSION_2, new ListActionPointPacketVersion2Api());
        m.put(API.CONFIRM_PURCHASE_IOS, new ConfirmPurchaseIOS());
        m.put(API.CONFIRM_PURCHASE_ANDROID, new ConfirmPurchaseAndroid());
        m.put(API.CONFIRM_PURCHASE_AMAZON, new ConfirmPurchaseAmazon());
        m.put(API.LIST_NEWS_CLIENT, new ListNewsApi());
        m.put(API.LIST_AUTO_NEWS, new ListAutoNewsNotifyApi());

        m.put(API.UPDATE_SERVER_LAST_TIME, new UpdateServerTimeApi());
        m.put(API.GET_BANNED_WORD, new GetBannedWordApi());

        // Add LongLT 2016
        m.put(API.GET_REPLACE_WORD, new GetReplaceWordApi());

        m.put(API.STICKER_CATEGORY_LIST, new StickerCategoryListApi());
        m.put(API.LIST_DEFAULT_STICKER_CATEGORY, new ListDefaultStickerCategoryApi());
        m.put(API.SEARCH_STICKER_CATEGORY, new SearchStickerCategoryApi());
        m.put(API.STICKER_CATEGORY_DETAIL, new StickerCategoryDetailApi());

        m.put(API.GET_STICKER_SHOP_INFOR, new GetStickerShopInforApi());
        m.put(API.RESTORE_STICKER_BY_POINT, new RestoreStickerByPointApi());
        m.put(API.BUY_STICKER_BY_POINT, new BuyStickerByPointApi());
        m.put(API.BUY_STICKER_BY_MONEY_ANDROID, new BuyStickerByMoneyAndroid());
        m.put(API.BUY_STICKER_BY_MONEY_IOS, new BuyStickerByMoneyIOSApi());

        m.put(API.LOG_BEFORE_PURCHASE, new LogBeforePurchaseApi());
        m.put(API.LOG_BEFORE_PURCHASE_VERSION_2, new LogBeforePurchaseVersion2Api());
        m.put(API.INSTALL_APPLICATION, new InstallApplicationApi());

        m.put(API.CLICK_PUSH_NOTIFICATION, new ClickPushNotificationApi());

        //HUNGDT add 20425
        m.put(API.MAKE_CALL, new MakeCallApi());
        m.put(API.START_CALL, new StartCallApi());
        m.put(API.END_CALL, new EndCallApi());
        
        m.put(API.TOTAL_BADGE, new TotalBadgeApi());
        m.put(API.TOTAL_NOTI_SEEN, new TotalNotiSeen());
    }

    public static IApiAdapter getApi(String apiName) {
        return m.get(apiName);
    }
}
