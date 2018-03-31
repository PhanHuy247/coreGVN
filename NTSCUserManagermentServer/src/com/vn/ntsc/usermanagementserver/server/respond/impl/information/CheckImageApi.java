/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.vn.ntsc.usermanagementserver.server.respond.impl.information;

import com.vn.ntsc.usermanagementserver.server.respond.Respond;
import com.vn.ntsc.usermanagementserver.server.respond.IApiAdapter;
import eazycommon.util.Util;
import java.util.Date;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.ParamKey;
import org.json.simple.JSONObject;
import com.vn.ntsc.usermanagementserver.dao.impl.ImageDAO;

/**
 *
 * @author RuAc0n
 */
public class CheckImageApi implements IApiAdapter{

    @Override
    public Respond execute(JSONObject obj, Date time) {
        Respond result = new Respond();
        try {
            String imageId = Util.getStringParam(obj, ParamKey.IMAGE_ID);
            String userId = Util.getStringParam(obj, ParamKey.USER_ID);
            boolean check = ImageDAO.imageExist(imageId, userId);
            if (check) {
                result.code = ErrorCode.SUCCESS;
            }
        } catch (Exception ex) {
            Util.addErrorLog(ex);            
        }
        return result;
    }
    
}
