/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.usermanagementserver.server.respond.impl.information;

import com.vn.ntsc.usermanagementserver.dao.impl.AlbumDAO;
import com.vn.ntsc.usermanagementserver.dao.impl.UserDAO;
import com.vn.ntsc.usermanagementserver.dao.impl.UserRateDAO;
import com.vn.ntsc.usermanagementserver.entity.impl.datarespond.AlbumData;
import com.vn.ntsc.usermanagementserver.server.respond.IApiAdapter;
import com.vn.ntsc.usermanagementserver.server.respond.Respond;
import com.vn.ntsc.usermanagementserver.server.respond.result.EntityRespond;
import eazycommon.constant.Constant;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.ParamKey;
import eazycommon.exception.EazyException;
import eazycommon.util.Util;
import java.util.Date;
import java.util.List;
import org.json.simple.JSONObject;

/**
 *
 * @author hoangnh
 */
public class AddAlbumApi implements IApiAdapter{

    @Override
    public Respond execute(JSONObject obj, Date time) {
        EntityRespond result = new EntityRespond();
        try {
            String userId = Util.getStringParam(obj, ParamKey.USER_ID);
            String albumName = Util.getStringParam(obj, ParamKey.ALBUM_NAME);
            String albumDes = Util.getStringParam(obj, ParamKey.ALBUM_DES);
            Integer privacy = Util.getIntParam(obj, ParamKey.PRIVACY);
            
            String albumId = AlbumDAO.addAlbum(userId, albumName, albumDes, privacy, time);
            AlbumData albumData = new AlbumData();
            albumData.albumId = albumId;
            albumData.userId = userId;
            albumData.albumName = albumName;
            albumData.albumDes = albumDes;
            albumData.privacy = privacy;
            albumData.time = time.getTime();
            if(albumId != null){
                result = new EntityRespond(ErrorCode.SUCCESS,albumData);
            }
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
        return result;
    }
    
}
