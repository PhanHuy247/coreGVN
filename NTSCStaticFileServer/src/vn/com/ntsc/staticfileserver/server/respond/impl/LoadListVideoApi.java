/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.com.ntsc.staticfileserver.server.respond.impl;

import eazycommon.constant.Constant;
import eazycommon.constant.ErrorCode;
import eazycommon.util.Util;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import vn.com.ntsc.staticfileserver.entity.impl.FileUrl;
import vn.com.ntsc.staticfileserver.server.Request;
import vn.com.ntsc.staticfileserver.server.respond.IApiAdapter;
import vn.com.ntsc.staticfileserver.server.respond.Respond;
import vn.com.ntsc.staticfileserver.server.respond.common.Helper;
import vn.com.ntsc.staticfileserver.server.respond.result.ListEntityRespond;

/**
 *
 * @author Phan Huy
 */
public class LoadListVideoApi implements IApiAdapter{

    @Override
    public Respond execute(Request request, Date time) {
        Respond respond = new Respond();
        List<String> listVideoId = request.getListVideoId();
        if (listVideoId == null || listVideoId.isEmpty()) {
            respond.code = ErrorCode.WRONG_DATA_FORMAT;
            return respond;
        }
        try {
          List<FileUrl> listVideoUrl = new LinkedList<>();
            Map<String, String> mapVideo = Helper.getListVideoUrl(listVideoId);
            Map<String, String> mapOrginal = Helper.getListFileUrl(listVideoId, Constant.IMAGE_KIND_VALUE.ORIGINAL_IMAGE);
            Map<String, String> mapThumnail = Helper.getListFileUrl(listVideoId, Constant.IMAGE_KIND_VALUE.THUMBNAIL);
            for (String id : listVideoId){
                String videoUrl = mapVideo.get(id);
                String originalUrl = mapOrginal.get(id);
                String thumbnailUrl = mapThumnail.get(id);
                listVideoUrl.add(new FileUrl(id,thumbnailUrl, originalUrl,videoUrl));
            }
            respond = new ListEntityRespond(ErrorCode.SUCCESS, listVideoUrl);
        
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
        return respond;
    }
    
}
