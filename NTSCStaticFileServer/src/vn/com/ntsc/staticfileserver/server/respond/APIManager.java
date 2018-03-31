/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package vn.com.ntsc.staticfileserver.server.respond;

import vn.com.ntsc.staticfileserver.server.respond.impl.UploadFileApi;
import vn.com.ntsc.staticfileserver.server.respond.impl.LoadFileApi;
import vn.com.ntsc.staticfileserver.server.respond.impl.LoadImageApi;
import vn.com.ntsc.staticfileserver.server.respond.impl.GetVideoUrlApi;
import vn.com.ntsc.staticfileserver.server.respond.impl.InsertGiftApi;
import vn.com.ntsc.staticfileserver.server.respond.impl.BuyStickerByMoneyIOSApi;
import vn.com.ntsc.staticfileserver.server.respond.impl.UploadImageApi;
import vn.com.ntsc.staticfileserver.server.respond.impl.UpdateStickerImageApi;
import vn.com.ntsc.staticfileserver.server.respond.impl.UploadNewBannerImageApi;
import vn.com.ntsc.staticfileserver.server.respond.impl.DownloadStickerApi;
import vn.com.ntsc.staticfileserver.server.respond.impl.InsertStickerApi;
import vn.com.ntsc.staticfileserver.server.respond.impl.DownloadStickerCategoryApi;
import vn.com.ntsc.staticfileserver.server.respond.impl.ResetConfigApi;
import vn.com.ntsc.staticfileserver.server.respond.impl.UploadImageByChatApi;
import vn.com.ntsc.staticfileserver.server.respond.impl.UploadFileByChatApi;
import vn.com.ntsc.staticfileserver.server.respond.impl.InsertStickerCategoryApi;
import vn.com.ntsc.staticfileserver.server.respond.impl.UpdateGiftImageApi;
import vn.com.ntsc.staticfileserver.server.respond.impl.UploadImageByMocomApi;
import vn.com.ntsc.staticfileserver.server.respond.impl.UpdateStickerCategoryImageApi;
import vn.com.ntsc.staticfileserver.server.respond.impl.UploadFileVersion2Api;
import vn.com.ntsc.staticfileserver.server.respond.impl.UploadImageVersion2Api;
import vn.com.ntsc.staticfileserver.server.respond.impl.BuyStickerByMoneyAndroidApi;
import vn.com.ntsc.staticfileserver.server.respond.impl.LoadImageAdminApi;
import vn.com.ntsc.staticfileserver.server.respond.impl.UploadImageByFAMUApi;
import vn.com.ntsc.staticfileserver.server.respond.impl.LoadImageWithSizeApi;
import java.util.TreeMap;
import eazycommon.constant.API;
import vn.com.ntsc.staticfileserver.server.respond.impl.AddAlbumAndImage;
import vn.com.ntsc.staticfileserver.server.respond.impl.AddAlbumImage;
import vn.com.ntsc.staticfileserver.server.respond.impl.AddBuzzApi;
import vn.com.ntsc.staticfileserver.server.respond.impl.AddEmojiApi;
import vn.com.ntsc.staticfileserver.server.respond.impl.AddEmojiCatApi;
import vn.com.ntsc.staticfileserver.server.respond.impl.EditEmojiApi;
import vn.com.ntsc.staticfileserver.server.respond.impl.EditEmojiCatDataApi;
import vn.com.ntsc.staticfileserver.server.respond.impl.GetUploadSetting;
import vn.com.ntsc.staticfileserver.server.respond.impl.ListEmojiCatDataApi;
import vn.com.ntsc.staticfileserver.server.respond.impl.ListEmojiDataApi;
import vn.com.ntsc.staticfileserver.server.respond.impl.ListEmojiItemDataApi;
import vn.com.ntsc.staticfileserver.server.respond.impl.ListEmojiUrlApi;
import vn.com.ntsc.staticfileserver.server.respond.impl.ListStickerUrlApi;
import vn.com.ntsc.staticfileserver.server.respond.impl.ListUpdatedEmojiCatApi;
import vn.com.ntsc.staticfileserver.server.respond.impl.ListUpdatedStickerCatApi;
import vn.com.ntsc.staticfileserver.server.respond.impl.LoadFileDataApi;
import vn.com.ntsc.staticfileserver.server.respond.impl.LoadListAudioApi;
import vn.com.ntsc.staticfileserver.server.respond.impl.LoadListFileApi;
import vn.com.ntsc.staticfileserver.server.respond.impl.UploadImageVersion3Api;
import vn.com.ntsc.staticfileserver.server.respond.impl.LoadListImageApi;
import vn.com.ntsc.staticfileserver.server.respond.impl.LoadListVideoApi;
import vn.com.ntsc.staticfileserver.server.respond.impl.UpdateOtherSettingApi;
import vn.com.ntsc.staticfileserver.server.respond.impl.UpdateStreamURLApi;
import vn.com.ntsc.staticfileserver.server.respond.impl.UpdateStreamVideoApi;
import vn.com.ntsc.staticfileserver.server.respond.impl.UpdateUploadSetting;


/**
 *
 * @author RuAc0n
 */
public class APIManager {

    private static final TreeMap<String, IApiAdapter> m = new TreeMap<>();

    static {        
        m.put(API.RESET_CONFIG, new ResetConfigApi());

        m.put(API.LOAD_FILE, new LoadFileApi());
        m.put(API.LOAD_IMAGE, new LoadImageApi());
        m.put(API.LOAD_LIST_IMAGE, new LoadListImageApi());
        m.put(API.LOAD_LIST_VIDEO, new LoadListVideoApi());
        m.put(API.LOAD_LIST_AUDIO, new LoadListAudioApi());
        m.put(API.LOAD_LIST_FILE, new LoadListFileApi());
        m.put(API.LOAD_IMAGE_WITH_SIZE, new LoadImageWithSizeApi());
        m.put(API.UPLOAD_FILE, new UploadFileApi());
        m.put(API.UPLOAD_FILE_VERSION_2, new UploadFileVersion2Api());
        m.put(API.UPLOAD_FILE_BY_CHAT, new UploadFileByChatApi());
        m.put(API.UPLOAD_IMAGE, new UploadImageApi());
        m.put(API.UPLOAD_IMAGE_VERSION_2, new UploadImageVersion2Api());
        m.put(API.UPLOAD_IMAGE_VERSION_3, new UploadImageVersion3Api());
        m.put(API.UPLOAD_IMAGE_BY_CHAT, new UploadImageByChatApi());
        m.put(API.UPLOAD_IMAGE_BY_MOCOM, new UploadImageByMocomApi());
        m.put(API.UPLOAD_IMAGE_BY_FAMU, new UploadImageByFAMUApi());
        m.put(API.GET_VIDEO_URL, new GetVideoUrlApi());
        
        m.put(API.DOWNLOAD_STICKER_CATEGORY, new DownloadStickerCategoryApi());
        m.put(API.INSERT_GIFT, new InsertGiftApi());
        m.put(API.UPDATE_GIFT_IMAGE, new UpdateGiftImageApi());
        m.put(API.INSERT_STICKER, new InsertStickerApi());
        m.put(API.UPDATE_STICKER_IMAGE, new UpdateStickerImageApi());
        
        m.put(API.INSERT_STICKER_CATEGORY, new InsertStickerCategoryApi());
        m.put(API.UPDATE_STICKER_CATEGORY_IMAGE, new UpdateStickerCategoryImageApi());
        m.put(API.LOAD_IMAGE_ADMIN, new LoadImageAdminApi());
        m.put(API.RESTORE_STICKER_BY_POINT, new DownloadStickerApi());
        m.put(API.BUY_STICKER_BY_POINT, new DownloadStickerApi());
        m.put(API.RESTORE_STICKER_BY_MONEY, new DownloadStickerApi());
        
        m.put(API.BUY_STICKER_BY_MONEY_IOS, new BuyStickerByMoneyIOSApi());
        m.put(API.BUY_STICKER_BY_MONEY_ANDROID, new BuyStickerByMoneyAndroidApi());
        
        m.put(API.UPLOAD_NEWS_BANNER, new UploadNewBannerImageApi());
        
        
        m.put(API.ADD_BUZZ, new AddBuzzApi());
        m.put(API.UPLOAD_STREAM_FILE, new UpdateStreamVideoApi());
        m.put(API.UPDATE_STREAM_URL, new UpdateStreamURLApi());
        m.put(API.SET_UPLOAD_SETTING, new UpdateUploadSetting());
        m.put(API.GET_UPLOAD_SETTING, new GetUploadSetting());
        m.put(API.SET_OTHER_SETTING, new UpdateOtherSettingApi());
        
        m.put(API.ADD_ALBUM_IMAGE, new AddAlbumImage());
        m.put(API.LOAD_FILE_DATA, new LoadFileDataApi());
        m.put(API.ADD_ALBUM_AND_IMAGE, new AddAlbumAndImage());
        
        m.put(API.LIST_STICKER_URL, new ListStickerUrlApi());
        m.put(API.LIST_UPDATED_STICKER_CAT, new ListUpdatedStickerCatApi());
        
        m.put(API.ADD_EMOJI_CAT, new AddEmojiCatApi());
        m.put(API.ADD_EMOJI, new AddEmojiApi());
        m.put(API.EMOJI_DATA, new ListEmojiDataApi());
        m.put(API.LIST_EMOJI_CAT, new ListEmojiCatDataApi());
        m.put(API.LIST_EMOJI, new ListEmojiItemDataApi());
        m.put(API.EDIT_EMOJI_CAT, new EditEmojiCatDataApi());
        m.put(API.EDIT_EMOJI, new EditEmojiApi());
        m.put(API.EMOJI_URL, new ListEmojiUrlApi());
        m.put(API.LIST_UPDATED_EMOJI_CAT, new ListUpdatedEmojiCatApi());
    }

    public static IApiAdapter getApi(String apiName) {
        return m.get(apiName);
    }
}
