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
import vn.com.ntsc.staticfileserver.dao.impl.FileDAO;
import vn.com.ntsc.staticfileserver.dao.impl.ImageDAO;
import vn.com.ntsc.staticfileserver.dao.impl.ThumbnailDAO;
import vn.com.ntsc.staticfileserver.entity.impl.FileData;
import vn.com.ntsc.staticfileserver.entity.impl.FileUrl;
import vn.com.ntsc.staticfileserver.entity.impl.ListFileData;
import vn.com.ntsc.staticfileserver.server.Request;
import vn.com.ntsc.staticfileserver.server.respond.IApiAdapter;
import vn.com.ntsc.staticfileserver.server.respond.Respond;
import vn.com.ntsc.staticfileserver.server.respond.common.Helper;
import vn.com.ntsc.staticfileserver.server.respond.result.EntityRespond;

/**
 *
 * @author hoangnh
 */
public class LoadFileDataApi implements IApiAdapter{

    @Override
    public Respond execute(Request request, Date time) {
        Util.addDebugLog("==============LoadFileDataApi==============");
        Respond respond = new Respond();
        List<String> listImageId = request.getListImgId();
        List<String> listVideoId = request.getListVideoId();
        List<String> listCoverId = request.getListCoverId();
        List<String> listAudioId = request.getListAudioId();
        List<String> listGiftId = request.getListGiftId();
        List<String> listStreamId = request.getListStreamId();
        
        ListFileData data = new ListFileData();
        try {
            if (listImageId != null && !listImageId.isEmpty()){
                //Util.addDebugLog("==============LoadFileDataApi=============="+listImageId);
                Map<String, FileData> mapOrginal = ImageDAO.getMapImageData(listImageId);
                Map<String, FileData> mapThumnail = ThumbnailDAO.getMapImageData(listImageId);
                
                List<FileData> listImageData = new LinkedList<>();
                
                for (String id : listImageId){
                    FileData temp = new FileData();
                    temp.addFileInfo(id);
                    
                    if(mapOrginal.get(id) != null){
                        FileData originInfo = mapOrginal.get(id);
                        temp.addOriginalInfo(originInfo);
                    }
                    if(mapThumnail.get(id) != null){
                        FileData thumnailInfo = mapThumnail.get(id);
                        temp.addThumbnailInfo(thumnailInfo);
                    }
                    //Util.addDebugLog("FileData============================"+temp.toJsonObject().toString());
                    listImageData.add(temp);
                }
                data.listImg = listImageData;
            }
            if(listGiftId != null && !listGiftId.isEmpty()){
                Map<String, String> mapGift = Helper.getListFileUrl(listGiftId, Constant.IMAGE_KIND_VALUE.GIFT);
                List<FileData> listGiftData = new LinkedList<>();
                
                for (String id : listGiftId){
                    String giftUrl = mapGift.get(id);
                    listGiftData.add(new FileData(id, giftUrl));
                }
                data.listGift = listGiftData;
            }
            if (listVideoId != null && !listVideoId.isEmpty()){
                List<FileData> listVideoData = new LinkedList<>();
                Map<String, FileData> mapVideo = FileDAO.getMapFileData(listVideoId);
                Map<String, FileData> mapOrginal = ImageDAO.getMapImageData(listVideoId);
                Map<String, FileData> mapThumnail = ThumbnailDAO.getMapImageData(listVideoId);
                
                for (String id : listVideoId){
                    FileData temp = new FileData();
                    temp.addFileInfo(id);
                    
                    if(mapVideo.get(id) != null){
                        FileData fileInfo = mapVideo.get(id);
                        temp.addFileProperty(fileInfo);
                    }
                    if(mapOrginal.get(id) != null){
                        FileData originInfo = mapOrginal.get(id);
                        temp.addOriginalInfo(originInfo);
                    }
                    if(mapThumnail.get(id) != null){
                        FileData thumnailInfo = mapThumnail.get(id);
                        temp.addThumbnailInfo(thumnailInfo);
                    }
                    
                    listVideoData.add(temp);
                }
                data.listVideo = listVideoData;
            }
            if (listCoverId != null && !listCoverId.isEmpty()){
                List<FileData> listCoverData = new LinkedList<>();
                Map<String, FileData> mapOrginal = ImageDAO.getMapImageData(listCoverId);
                
                for (String id : listCoverId){
                    FileData temp = new FileData();
                    temp.addFileInfo(id);
                    
                    if(mapOrginal.get(id) != null){
                        FileData originInfo = mapOrginal.get(id);
                        temp.addOriginalInfo(originInfo);
                    }
                    
                    listCoverData.add(temp);
                }
                data.listCover = listCoverData;
            }
            if (listAudioId != null && !listAudioId.isEmpty()){
                List<FileData> listAudioData = new LinkedList<>();
                Map<String, FileData> mapAudio = FileDAO.getMapFileData(listAudioId);
                
                for (String id : listAudioId){
                    FileData temp = new FileData();
                    temp.addFileInfo(id);
                    
                    if(mapAudio.get(id) != null){
                        FileData fileInfo = mapAudio.get(id);
                        temp.addFileProperty(fileInfo);
                    }
                    
                    listAudioData.add(temp);
                }
                data.listAudio = listAudioData;
            }
            if (listStreamId != null && !listStreamId.isEmpty()){
                List<FileData> listStreamData = new LinkedList<>();
                Map<String, FileData> mapStream = FileDAO.getMapStreamData(listStreamId);
                Map<String, FileData> mapOrginal = ImageDAO.getMapStreamImageData(listStreamId);
                Map<String, FileData> mapThumnail = ThumbnailDAO.getMapStreamImageData(listStreamId);
                
                for (String id : listStreamId){
                    FileData temp = new FileData();
                    temp.addFileInfo(id);
                    
                    if(mapStream.get(id) != null){
                        FileData fileInfo = mapStream.get(id);
                        temp.addFileProperty(fileInfo);
                    }
                    if(mapOrginal.get(id) != null){
                        FileData originInfo = mapOrginal.get(id);
                        temp.addOriginalInfo(originInfo);
                    }
                    if(mapThumnail.get(id) != null){
                        FileData thumnailInfo = mapThumnail.get(id);
                        temp.addThumbnailInfo(thumnailInfo);
                    }
                    
                    listStreamData.add(temp);
                }
                data.listStream = listStreamData;
            }
            
            respond = new EntityRespond(ErrorCode.SUCCESS, data);
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
        return respond;
    }
    
}
