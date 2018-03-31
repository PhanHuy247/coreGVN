/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.com.ntsc.staticfileserver.server.respond.impl;

import eazycommon.constant.Constant;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.mongokey.StaticFiledbKey;
import eazycommon.util.Util;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import vn.com.ntsc.staticfileserver.entity.impl.FileUrl;
import vn.com.ntsc.staticfileserver.entity.impl.ListFileUrl;
import vn.com.ntsc.staticfileserver.server.Request;
import vn.com.ntsc.staticfileserver.server.respond.IApiAdapter;
import vn.com.ntsc.staticfileserver.server.respond.Respond;
import vn.com.ntsc.staticfileserver.server.respond.common.Helper;
import vn.com.ntsc.staticfileserver.server.respond.result.EntityRespond;
import vn.com.ntsc.staticfileserver.server.respond.result.ListEntityRespond;

/**
 *
 * @author Administrator
 */
public class LoadListFileApi implements IApiAdapter {

    @Override
    public Respond execute(Request request, Date time) {
        Respond respond = new Respond();
        List<String> listImageId = request.getListImgId();
        List<String> listGiftId = request.getListGiftId();
        List<String> listVideoId = request.getListVideoId();
        List<String> listCoverId = request.getListCoverId();
        List<String> listAudioId = request.getListAudioId();
        List<String> listStreamId = request.getListStreamId();
        
        ListFileUrl data = new ListFileUrl();
        try {
            if (listImageId != null && !listImageId.isEmpty()){
                List<FileUrl> listImageUrl = new LinkedList<>();
                Map<String, String> mapOrginal = Helper.getListFileUrlWithInfo(listImageId, Constant.IMAGE_KIND_VALUE.ORIGINAL_IMAGE);
                Map<String, String> mapThumnail = Helper.getListFileUrlWithInfo(listImageId, Constant.IMAGE_KIND_VALUE.THUMBNAIL);
                for (String id : listImageId){
                    if(mapOrginal.get(id) != null){
                        String originalUrl = mapOrginal.get(id).split(" ")[0];
                        String thumbnailUrl = mapThumnail.get(id).split(" ")[0];
                        String infoOriginal = mapOrginal.get(id).substring(originalUrl.length()+1);
                        String infoThumbnail = mapThumnail.get(id).substring(thumbnailUrl.length()+1);
                        listImageUrl.add(new FileUrl(id, thumbnailUrl, originalUrl, infoThumbnail,infoOriginal));
                    }
                }
                data.listImg = listImageUrl;
            }
            if (listGiftId != null && !listGiftId.isEmpty()){
                List<FileUrl> listGiftUrl = new LinkedList<>();
                Map<String, String> mapGift = Helper.getListFileUrl(listGiftId, Constant.IMAGE_KIND_VALUE.GIFT);
                for (String id : listGiftId){
                    String giftUrl = mapGift.get(id);
                    listGiftUrl.add(new FileUrl(id, giftUrl));
                }
                data.listGift = listGiftUrl;
            }
            if (listVideoId != null && !listVideoId.isEmpty()){
                List<FileUrl> listVideoUrl = new LinkedList<>();
                Map<String, String> mapVideo = Helper.getListVideoUrlWithInfo(listVideoId);
                Map<String, String> mapThumnail = Helper.getListFileUrlWithInfo(listVideoId, Constant.IMAGE_KIND_VALUE.THUMBNAIL);
                Map<String, String> mapOrginal = Helper.getListFileUrlWithInfo(listVideoId, Constant.IMAGE_KIND_VALUE.ORIGINAL_IMAGE);
                for (String id : listVideoId){
                    String videoUrl = mapVideo.get(id).split(" ")[0];
                    String infoVideo = mapVideo.get(id).substring(videoUrl.length()+1);
                    
                    String originalUrl = "";
                    String infoOriginal = "";
                    String thumbnailUrl = "";
                    String infoThumbnail = "";
                    if(mapOrginal.get(id) != null){
                        originalUrl = mapOrginal.get(id).split(" ")[0];
                        infoOriginal = mapOrginal.get(id).substring(originalUrl.length()+1);
                    }
                    if(mapThumnail.get(id) != null){
                        thumbnailUrl = mapThumnail.get(id).split(" ")[0];
                        infoThumbnail = mapThumnail.get(id).substring(thumbnailUrl.length()+1);
                    }
                    
                    listVideoUrl.add(new FileUrl(id, videoUrl, infoVideo, thumbnailUrl, infoThumbnail, originalUrl, infoOriginal));
                }
                data.listVideo = listVideoUrl;
            }
            if (listStreamId != null && !listStreamId.isEmpty()){
                List<FileUrl> listStreamUrl = new LinkedList<>();
                Map<String, String> mapVideo = Helper.getListStreamUrlWithInfo(listStreamId);
                Map<String, String> mapThumnail = Helper.getListStreamUrlWithInfo(listStreamId, Constant.IMAGE_KIND_VALUE.THUMBNAIL);
                Map<String, String> mapOrginal = Helper.getListStreamUrlWithInfo(listStreamId, Constant.IMAGE_KIND_VALUE.ORIGINAL_IMAGE);
                for (String id : listStreamId){
                    String videoUrl = mapVideo.get(id).split(" ")[0];
                    String infoVideo = mapVideo.get(id).substring(videoUrl.length()+1);
                    
                    String originalUrl = "";
                    String infoOriginal = "";
                    String thumbnailUrl = "";
                    String infoThumbnail = "";
                    if(mapOrginal.get(id) != null){
                        originalUrl = mapOrginal.get(id).split(" ")[0];
                        infoOriginal = mapOrginal.get(id).substring(originalUrl.length()+1);
                    }
                    if(mapThumnail.get(id) != null){
                        thumbnailUrl = mapThumnail.get(id).split(" ")[0];
                        infoThumbnail = mapThumnail.get(id).substring(thumbnailUrl.length()+1);
                    }
                    
                    listStreamUrl.add(new FileUrl(id, videoUrl, infoVideo, thumbnailUrl, infoThumbnail, originalUrl, infoOriginal));
                }
                data.listStream = listStreamUrl;
            }
            if (listCoverId != null && !listCoverId.isEmpty()){
                List<FileUrl> listCoverUrl = new LinkedList<>();
                Map<String, String> mapOrginal = Helper.getListFileUrlWithInfo(listCoverId, Constant.IMAGE_KIND_VALUE.ORIGINAL_IMAGE);
                for (String id : listCoverId){
                    if(mapOrginal.get(id) != null){
                        String originalUrl = mapOrginal.get(id).split(" ")[0];
                        String infoOriginal = mapOrginal.get(id).substring(originalUrl.length()+1);
                        listCoverUrl.add(new FileUrl(id, "", originalUrl, "",infoOriginal));
                    }
                }
                data.listCover = listCoverUrl;
            }
            if (listAudioId != null && !listAudioId.isEmpty()){
                List<FileUrl> listAudioUrl = new LinkedList<>();
                Map<String, String> mapAudio = Helper.getListVideoUrlWithInfo(listAudioId);
                for (String id : listAudioId){
                    String audioUrl = mapAudio.get(id).split(" ")[0];
                    String infoAudio = mapAudio.get(id).substring(audioUrl.length()+1);
//                    listAudioUrl.add(new FileUrl(id, "", audioUrl,"",infoAudio));
                    listAudioUrl.add(new FileUrl(id, audioUrl, infoAudio, "", "", "", ""));
                }
                data.listAudio = listAudioUrl;
            }
            
            respond = new EntityRespond(ErrorCode.SUCCESS, data);
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
        return respond;
    }

}
