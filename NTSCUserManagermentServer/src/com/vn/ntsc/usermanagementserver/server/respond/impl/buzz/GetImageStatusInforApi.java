/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.usermanagementserver.server.respond.impl.buzz;

import com.vn.ntsc.usermanagementserver.dao.impl.ImageDAO;
import com.vn.ntsc.usermanagementserver.entity.impl.image.GetImageData;
import com.vn.ntsc.usermanagementserver.server.respond.IApiAdapter;
import com.vn.ntsc.usermanagementserver.server.respond.Respond;
import com.vn.ntsc.usermanagementserver.server.respond.result.EntityRespond;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.ParamKey;
import eazycommon.exception.EazyException;
import eazycommon.util.Util;
import java.util.Date;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 *
 * @author hoangnh
 */
public class GetImageStatusInforApi implements IApiAdapter{

    @Override
    public Respond execute(JSONObject obj, Date time) {
        EntityRespond result = new EntityRespond();
        try {
            JSONArray img_list = (JSONArray) obj.get("img_list");
            Util.addDebugLog("===========img_list========"+img_list);
            JSONObject temp = (JSONObject) img_list.get(0);
//            String text = (String) temp.get("text");
            String imgId = (String) temp.get("data"); 
//            Long is_app = (Long) temp.get("is_app");
            
            
//            String imageId = Util.getStringParam(obj, ParamKey.BUZZ_VALUE);
            
            
            int isApp = ImageDAO.getStattus(imgId);
            result = new EntityRespond(ErrorCode.SUCCESS, new GetImageData((long) isApp));
        } catch (EazyException ex) {
            Util.addErrorLog(ex);
            result.code = ex.getErrorCode();
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
        return result;
    }
    
}
