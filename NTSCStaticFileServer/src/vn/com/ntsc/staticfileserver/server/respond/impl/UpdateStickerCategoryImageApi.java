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
import vn.com.ntsc.staticfileserver.server.Request;
import vn.com.ntsc.staticfileserver.server.respond.IApiAdapter;
import vn.com.ntsc.staticfileserver.server.respond.Respond;
import vn.com.ntsc.staticfileserver.server.respond.common.Helper;
import eazycommon.util.Util;

/**
 *
 * @author RuAc0n
 */
public class UpdateStickerCategoryImageApi implements IApiAdapter {

    @Override
    public Respond execute(Request request, Date time) {
        Respond respond = new Respond();
        byte[] arrayImageInput = Helper.getInputArrayByte(request);
        String imageString = new String(arrayImageInput);
        byte[] imagebytes = Base64.decode(imageString);
        try {
            String catId = request.getStickerCatId();
            String avaUrl = StickerCategoryDAO.getCategoryAvaPath(catId);
            if (avaUrl != null) {
//                UUID code = UUID.randomUUID();
//                String newImagePath = code + Constant.STAMP_TYPE;
//                Helper.writeFile(newImagePath, imagebytes, Constant.STICKER_AVATAR_CATEGORY_FOLDER_PATH, time);
//                // check gif image
//                String imagePath = newImagePath + Constant.STICKER_AVATAR_CATEGORY_FOLDER_PATH;
//                if(!Helper.isGifImage(imagePath)){
//                    return new EntityRespond(ErrorCode.WRONG_FILE_TYPE);
//                }
                Helper.writeFile(avaUrl, imagebytes, FilesAndFolders.FOLDERS.AVATAR_STICKER_CATEGORY_FOLDER, time);
            } else {
                UUID code = UUID.randomUUID();
                avaUrl = code + FilesAndFolders.EXTENSIONS.STAMP_EXTENSION;
                Helper.writeFile(avaUrl, imagebytes, FilesAndFolders.FOLDERS.AVATAR_STICKER_CATEGORY_FOLDER, time);
                // check gif image
//                String imagePath = avaUrl + Constant.STICKER_AVATAR_CATEGORY_FOLDER_PATH;
//                if(!Helper.isGifImage(imagePath)){
//                    return new EntityRespond(ErrorCode.WRONG_FILE_TYPE);
//                }
                //insert image collection
                StickerCategoryDAO.insertCategory(catId, avaUrl);
            }
            respond.code = ErrorCode.SUCCESS;
        } catch (IOException ex) {
            Util.addErrorLog(ex);
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
        return respond;
    }

}
