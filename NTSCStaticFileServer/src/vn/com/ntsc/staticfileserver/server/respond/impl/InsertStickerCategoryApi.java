/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.com.ntsc.staticfileserver.server.respond.impl;

import eazycommon.constant.ErrorCode;
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import eazycommon.constant.FilesAndFolders;
import java.io.IOException;
import java.util.Date;
import java.util.UUID;
import vn.com.ntsc.staticfileserver.dao.impl.StickerCategoryDAO;
import vn.com.ntsc.staticfileserver.entity.impl.StampData;
import vn.com.ntsc.staticfileserver.server.Request;
import vn.com.ntsc.staticfileserver.server.respond.IApiAdapter;
import vn.com.ntsc.staticfileserver.server.respond.Respond;
import vn.com.ntsc.staticfileserver.server.respond.common.Helper;
import vn.com.ntsc.staticfileserver.server.respond.result.EntityRespond;
import eazycommon.util.Util;

/**
 *
 * @author RuAc0n
 */
public class InsertStickerCategoryApi implements IApiAdapter {

    @Override
    public Respond execute(Request request, Date time) {
        Respond respond = new Respond();
        byte[] arrayImageInput = Helper.getInputArrayByte(request);
        String imageString = new String(arrayImageInput);
        byte[] imagebytes = Base64.decode(imageString);
        try {
            UUID code = UUID.randomUUID();
            String avaUrl = code + FilesAndFolders.EXTENSIONS.STAMP_EXTENSION;
            Helper.writeFile(avaUrl, imagebytes, FilesAndFolders.FOLDERS.AVATAR_STICKER_CATEGORY_FOLDER, time);
            // check gif image
//            String avatarImagePath = avaUrl + Constant.STICKER_AVATAR_CATEGORY_FOLDER_PATH;
//            if(!Helper.isGifImage(avatarImagePath)){
//                return new EntityRespond(ErrorCode.WRONG_FILE_TYPE);
//            }
            //insert image collection
            String catId = StickerCategoryDAO.insertCategory(avaUrl);
            StampData data = new StampData(catId, null);
            respond = new EntityRespond(ErrorCode.SUCCESS, data);
        } catch (IOException ex) {
            Util.addErrorLog(ex);
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
        return respond;
    }

}
