/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.usermanagementserver.server.respond.impl.information;

import com.vn.ntsc.usermanagementserver.dao.impl.AlbumDAO;
import com.vn.ntsc.usermanagementserver.dao.impl.AlbumImageDAO;
import com.vn.ntsc.usermanagementserver.entity.impl.datarespond.AlbumData;
import com.vn.ntsc.usermanagementserver.entity.impl.datarespond.AlbumImageData;
import com.vn.ntsc.usermanagementserver.server.respond.IApiAdapter;
import com.vn.ntsc.usermanagementserver.server.respond.Respond;
import com.vn.ntsc.usermanagementserver.server.respond.result.EntityRespond;
import com.vn.ntsc.usermanagementserver.server.respond.result.ListEntityRespond;
import eazycommon.constant.Constant;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.ParamKey;
import eazycommon.util.Util;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.json.simple.JSONObject;

/**
 *
 * @author hoangnh
 */
public class LoadAlbumImageApi implements IApiAdapter{

    @Override
    public Respond execute(JSONObject obj, Date time) {
        ListEntityRespond result = new ListEntityRespond();
        try {
            String userId = Util.getStringParam(obj, ParamKey.USER_ID);
            String albumId = Util.getStringParam(obj, ParamKey.ALBUM_ID);
            Integer take = Util.getIntParam(obj, ParamKey.TAKE);
            Integer skip = Util.getIntParam(obj, ParamKey.SKIP);
            
            List<AlbumImageData> listImage = new ArrayList();
            AlbumData album = AlbumDAO.getAlbum(albumId);
            if(album.albumId != null){
                if(album.userId.equals(userId) || album.privacy == Constant.POST_MODE.EVERYONE){
                    listImage = AlbumImageDAO.getListImage(albumId);
                    Integer imgNumber = skip + take;
                    if(imgNumber > listImage.size()){
                        imgNumber = listImage.size();
                    }
                    if(skip > imgNumber){
                        listImage = new ArrayList<>();
                        result = new ListEntityRespond(ErrorCode.SUCCESS, listImage);
                        return result;
                    }
                    listImage = listImage.subList(skip, imgNumber);
                    for(AlbumImageData listImg : listImage){
                        listImg.albumDes = album.albumDes;
                        listImg.albumName = album.albumName;
                        listImg.privacy = album.privacy;
                    }
                    if(listImage == null || listImage.isEmpty()){
                        AlbumImageData listImg = new AlbumImageData();
                        listImg.albumDes = album.albumDes;
                        listImg.albumName = album.albumName;
                        listImg.privacy = album.privacy;
                        listImage.add(listImg);
                    }
                }else{
                    result = new ListEntityRespond(ErrorCode.ACCESS_DENIED, null);
                }
            }else{
                result = new ListEntityRespond(ErrorCode.ACCESS_DENIED, null);
            }
            
            result = new ListEntityRespond(ErrorCode.SUCCESS, listImage);
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
        return result;
    }
    
}
