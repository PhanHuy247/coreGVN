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
public class UpdateAlbumApi implements IApiAdapter{

    @Override
    public Respond execute(JSONObject obj, Date time) {
        EntityRespond result = new EntityRespond();
        try {
            String userId = Util.getStringParam(obj, ParamKey.USER_ID);
            String albumId = Util.getStringParam(obj, ParamKey.ALBUM_ID);
            String albumName = Util.getStringParam(obj, ParamKey.ALBUM_NAME);
            String albumDes = Util.getStringParam(obj, ParamKey.ALBUM_DES);
            Integer privacy = Util.getIntParam(obj, ParamKey.PRIVACY);
            
            Boolean isAlbumOwned = AlbumDAO.checkAlbumOwned(userId, albumId);
            if(isAlbumOwned){
                Boolean isSuccess = AlbumDAO.updateAlbum(userId, albumId, albumName, albumDes, privacy);
                AlbumData albumData = AlbumDAO.getAlbum(albumId);
                albumData = AlbumImageDAO.getLastestImageAlbum(albumData);
                if(isSuccess){
                    result = new EntityRespond(ErrorCode.SUCCESS,albumData);
                }
            }else{
                result = new EntityRespond(ErrorCode.ACCESS_DENIED);
            }
            
            
            
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
        return result;
    }
    
}
