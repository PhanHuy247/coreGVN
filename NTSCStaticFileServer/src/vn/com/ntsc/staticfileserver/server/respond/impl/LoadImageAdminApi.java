/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package vn.com.ntsc.staticfileserver.server.respond.impl;

import eazycommon.constant.ErrorCode;
import java.util.Date;
import eazycommon.exception.EazyException;
import vn.com.ntsc.staticfileserver.server.Request;
import vn.com.ntsc.staticfileserver.server.respond.IApiAdapter;
import vn.com.ntsc.staticfileserver.server.respond.Respond;
import vn.com.ntsc.staticfileserver.server.respond.common.Helper;
import vn.com.ntsc.staticfileserver.server.respond.result.ByteRespond;
import eazycommon.util.Util;

/**
 *
 * @author RuAc0n
 */
public class LoadImageAdminApi implements IApiAdapter{

    @Override
    public Respond execute(Request request, Date time) {
        Respond respond = new Respond();
        String imageId = request.getImageId();
        String imageKind = request.getImageKind();
        if (imageId == null || imageId.isEmpty() || imageKind == null || imageKind.isEmpty()) {
            respond.code = ErrorCode.WRONG_DATA_FORMAT;
            return respond;
        }
        try {
            String imageUrl = Helper.getImageUrl(imageId, imageKind);
            if (imageUrl != null) {
                ByteRespond data = new ByteRespond();
                data.data = Helper.getFile(imageUrl);
                respond = data;
            } else {
                return null;
            }
        } catch (EazyException ex) {
            Util.addErrorLog(ex);
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
        return respond;
    }
    
}
