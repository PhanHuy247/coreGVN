/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.usermanagementserver.server.respond.impl.information;

import com.vn.ntsc.usermanagementserver.dao.impl.AlbumDAO;
import com.vn.ntsc.usermanagementserver.dao.impl.AlbumImageDAO;
import com.vn.ntsc.usermanagementserver.server.respond.IApiAdapter;
import com.vn.ntsc.usermanagementserver.server.respond.Respond;
import com.vn.ntsc.usermanagementserver.server.respond.result.EntityRespond;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.ParamKey;
import eazycommon.util.Util;
import java.util.Date;
import java.util.List;
import org.json.simple.JSONObject;

/**
 *
 * @author hoangnh
 */
public class AddAlbumImageApi implements IApiAdapter{

    @Override
    public Respond execute(JSONObject obj, Date time) {
        EntityRespond result = new EntityRespond();
        try {
            String userId = Util.getStringParam(obj, ParamKey.USER_ID);
            String albumId = Util.getStringParam(obj, ParamKey.ALBUM_ID);
            List<String> listImage = Util.getListString(obj, ParamKey.LIST_IMAGE);
            
            AlbumImageDAO.addAlbumImage(albumId, listImage, time.getTime());
            
            result = new EntityRespond(ErrorCode.SUCCESS);
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
        return result;
    }
    
}
