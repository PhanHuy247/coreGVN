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
public class InsertStickerApi implements IApiAdapter {

    @Override
    public Respond execute(Request request, Date time) {
        Respond respond = new Respond();
        byte[] arrayImageInput = Helper.getInputArrayByte(request);
        String imageString = new String(arrayImageInput);
        byte[] imagebytes = Base64.decode(imageString);
        try {
            long stickerCode = StickerDAO.getMaxCode() + 1;
            String stickerUrl = stickerCode + FilesAndFolders.EXTENSIONS.STAMP_EXTENSION;
            Helper.writeFile(stickerUrl, imagebytes, FilesAndFolders.FOLDERS.STICKERS_FOLDER, time);
            // check gif image
//            String imagePath = stickerUrl + Constant.STICKER_AVATAR_CATEGORY_FOLDER_PATH;
//            if(!Helper.isGifImage(imagePath)){
//                return new EntityRespond(ErrorCode.WRONG_FILE_TYPE);
//            }
            
            //insert image collection
            String stickerId = StickerDAO.insertSticker(stickerUrl, stickerCode);
            StampData data = new StampData(stickerId, stickerCode);
            respond = new EntityRespond(ErrorCode.SUCCESS, data);
        } catch (IOException ex) {
            Util.addErrorLog(ex);
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
        return respond;
    }

}
