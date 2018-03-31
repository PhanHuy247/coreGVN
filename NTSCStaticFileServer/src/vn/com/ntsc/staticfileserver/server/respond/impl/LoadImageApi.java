/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.com.ntsc.staticfileserver.server.respond.impl;

import eazycommon.constant.Constant;
import eazycommon.constant.ErrorCode;
import java.util.Date;
import eazycommon.exception.EazyException;
import vn.com.ntsc.staticfileserver.server.Request;
import vn.com.ntsc.staticfileserver.server.respond.IApiAdapter;
import vn.com.ntsc.staticfileserver.server.respond.Respond;
import vn.com.ntsc.staticfileserver.server.respond.common.Helper;
import vn.com.ntsc.staticfileserver.server.respond.result.ByteRespond;
import eazycommon.util.Util;
import vn.com.ntsc.staticfileserver.dao.impl.UserImageDAO;

/**
 *
 * @author RuAc0n
 */
public class LoadImageApi implements IApiAdapter {

    @Override
    public Respond execute(Request request, Date time) {
        Util.addDebugLog("====LoadImageApi");
        Respond result = new Respond();
        String imageId = request.getImageId();
        String imageKind = request.getImageKind();
        Util.addDebugLog("=======LoadImageApi imageId:"+imageId);
        Util.addDebugLog("=======LoadImageApi imageKind:"+imageKind);
        if (imageId == null || imageId.isEmpty() || imageKind == null || imageKind.isEmpty()) {
            result.code = ErrorCode.WRONG_DATA_FORMAT;
            return result;
        }
        try {
            if (imageKind.equals(Constant.IMAGE_KIND_VALUE.THUMBNAIL) || imageKind.equals(Constant.IMAGE_KIND_VALUE.ORIGINAL_IMAGE)) {
                if (!UserImageDAO.imageExist(imageId, request.getUserId())) {
                    return null;
                }
            }
            String imageUrl = Helper.getImageUrl(imageId, imageKind);
            ByteRespond respond = new ByteRespond();
            respond.data = Helper.getFile(imageUrl);
            result = respond;
        } catch (EazyException ex) {
            result.code = ex.getErrorCode();
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
        return result;
    }

}
