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
import vn.com.ntsc.staticfileserver.dao.impl.StickerDAO;
import vn.com.ntsc.staticfileserver.server.Request;
import vn.com.ntsc.staticfileserver.server.respond.IApiAdapter;
import vn.com.ntsc.staticfileserver.server.respond.Respond;
import vn.com.ntsc.staticfileserver.server.respond.common.Helper;
import eazycommon.util.Util;

/**
 *
 * @author RuAc0n
 */
public class UpdateStickerImageApi implements IApiAdapter {

    @Override
    public Respond execute(Request request, Date time) {
        Respond respond = new Respond();
        byte[] arrayImageInput = Helper.getInputArrayByte(request);
        String imageString = new String(arrayImageInput);
        byte[] imagebytes = Base64.decode(imageString);
        try {
            String stickerId = request.getStickerId();
            String stickerUrl = StickerDAO.getStickerURL(stickerId);
            if (stickerUrl != null) {
//                UUID code = UUID.randomUUID();
//                String newImagePath = code + Constant.STAMP_TYPE;
//                Helper.writeFile(newImagePath, imagebytes, Constant.STICKER_AVATAR_CATEGORY_FOLDER_PATH, time);
//                // check gif image
//                String imagePath = newImagePath + Constant.STICKER_AVATAR_CATEGORY_FOLDER_PATH;
//                if(!Helper.isGifImage(imagePath)){
//                    return new Respond(ErrorCode.WRONG_FILE_TYPE);
//                }
                
                Helper.writeFile(stickerUrl, imagebytes, FilesAndFolders.FOLDERS.STICKERS_FOLDER, time);
                //insert image collection
                respond.code = ErrorCode.SUCCESS;
            } else {
                respond.code = ErrorCode.WRONG_DATA_FORMAT;
            }
        } catch (IOException ex) {
            Util.addErrorLog(ex);
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
        return respond;
    }

}
