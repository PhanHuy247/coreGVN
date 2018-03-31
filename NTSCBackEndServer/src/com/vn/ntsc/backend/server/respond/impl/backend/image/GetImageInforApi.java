/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.backend.server.respond.impl.backend.image;

import org.json.simple.JSONObject;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.ParamKey;
import com.vn.ntsc.backend.dao.user.ImageDAO;
import com.vn.ntsc.backend.entity.impl.image.Image;
import com.vn.ntsc.backend.server.respond.IApiAdapter;
import com.vn.ntsc.backend.server.respond.Respond;
import com.vn.ntsc.backend.server.respond.result.EntityRespond;
import eazycommon.util.Util;

/**
 *
 * @author RuAc0n
 */
public class GetImageInforApi implements IApiAdapter {

    @Override
    public Respond execute(JSONObject obj) {
        EntityRespond respond = new EntityRespond();
        try {
            String imageId = Util.getStringParam(obj, ParamKey.IMAGE_ID);
            Image image = ImageDAO.getImageInfor(imageId);
            respond = new EntityRespond(ErrorCode.SUCCESS, image);
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
        return respond;
    }

}
