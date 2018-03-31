/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.com.ntsc.staticfileserver.server.respond.impl;

import vn.com.ntsc.staticfileserver.server.respond.IApiAdapter;
import vn.com.ntsc.staticfileserver.server.respond.Respond;
import eazycommon.constant.ErrorCode;
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import eazycommon.constant.FilesAndFolders;
import java.io.IOException;
import java.util.Date;
import vn.com.ntsc.staticfileserver.dao.impl.GiftDAO;
import vn.com.ntsc.staticfileserver.server.Request;
import vn.com.ntsc.staticfileserver.server.respond.common.Helper;
import eazycommon.util.Util;

/**
 *
 * @author RuAc0n
 */
public class UpdateGiftImageApi implements IApiAdapter {

    @Override
    public Respond execute(Request request, Date time) {
        Respond respond = new Respond();
        byte[] arrayImageInput = Helper.getInputArrayByte(request);
        String imageString = new String(arrayImageInput);
        byte[] imageBytes = Base64.decode(imageString);
        try {
            String giftId = request.getGiftId();
            String giftUrl = GiftDAO.getGiftURL(giftId);
            if (giftUrl != null) {
                Helper.writeFile(giftUrl, imageBytes, FilesAndFolders.FOLDERS.GIFTS_FOLDER, time);
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
