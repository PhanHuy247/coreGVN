/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.usermanagementserver.server.respond.impl.information;

import com.vn.ntsc.usermanagementserver.dao.impl.AlbumDAO;
import com.vn.ntsc.usermanagementserver.dao.impl.AlbumImageDAO;
import com.vn.ntsc.usermanagementserver.entity.impl.datarespond.AlbumData;
import com.vn.ntsc.usermanagementserver.server.respond.IApiAdapter;
import com.vn.ntsc.usermanagementserver.server.respond.Respond;
import com.vn.ntsc.usermanagementserver.server.respond.result.EntityRespond;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.ParamKey;
import eazycommon.util.Util;
import java.util.Date;
import org.json.simple.JSONObject;

/**
 *
 * @author hoangnh
 */
public class CheckAlbumOwnedApi implements IApiAdapter{

    @Override
    public Respond execute(JSONObject obj, Date time) {
        EntityRespond result = new EntityRespond();
        try {
            String userId = Util.getStringParam(obj, ParamKey.USER_ID);
            String albumId = Util.getStringParam(obj, ParamKey.ALBUM_ID);
            
            Boolean isOwned = AlbumDAO.checkAlbumOwned(userId, albumId);
            if(isOwned){
                AlbumData data = AlbumDAO.getAlbum(albumId);
                data.numberImage = AlbumImageDAO.getImageNumber(albumId);
                result = new EntityRespond(ErrorCode.SUCCESS, data);
            }else{
                result = new EntityRespond(ErrorCode.ACCESS_DENIED);
            }
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
        return result;
    }
    
}
