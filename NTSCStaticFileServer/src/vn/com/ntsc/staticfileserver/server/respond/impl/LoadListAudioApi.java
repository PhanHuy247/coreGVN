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
public class LoadListAudioApi implements IApiAdapter{

    @Override
    public Respond execute(Request request, Date time) {
         Respond respond = new Respond();
        List<String> listAudioId = request.getListAudioId();
        List<String> listCoverId = request.getListCoverId();
        if (listAudioId == null || listAudioId.isEmpty()) {
            respond.code = ErrorCode.WRONG_DATA_FORMAT;
            return respond;
        }
        int numberCover = 0;
        try {
         List<FileUrl> listAudioUrl = new LinkedList<>();
         
         Map<String, String> mapAudio = Helper.getListVideoUrl(listAudioId);
         Map<String, String> mapOrginal = Helper.getListFileUrl(listCoverId, Constant.IMAGE_KIND_VALUE.ORIGINAL_IMAGE);
         for (String id : listAudioId){
            String audioUrl = mapAudio.get(id);
            String originalUrl = mapOrginal.get(listCoverId.get(numberCover));
            numberCover++;
            listAudioUrl.add(new FileUrl(id, audioUrl,originalUrl));
         }
            respond = new ListEntityRespond(ErrorCode.SUCCESS, listAudioUrl);
        
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
        return respond;
    }
    
}
