/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.com.ntsc.staticfileserver.server.respond.impl;

import eazycommon.constant.Constant;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.ParamKey;
import eazycommon.util.Util;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.simple.JSONObject;
import vn.com.ntsc.staticfileserver.dao.impl.BuzzDAO;
import vn.com.ntsc.staticfileserver.dao.impl.FileDAO;
import vn.com.ntsc.staticfileserver.dao.impl.ImageDAO;
import vn.com.ntsc.staticfileserver.dao.impl.ThumbnailDAO;
import vn.com.ntsc.staticfileserver.server.Request;
import vn.com.ntsc.staticfileserver.server.respond.IApiAdapter;
import vn.com.ntsc.staticfileserver.server.respond.Respond;
import vn.com.ntsc.staticfileserver.server.respond.common.Helper;
import vn.com.ntsc.staticfileserver.server.respond.result.EntityRespond;

/**
 *
 * @author hoangnh
 */
public class UpdateStreamURLApi implements IApiAdapter{

    @Override
    public Respond execute(Request request, Date time) {
        Respond respond = new Respond();
        String token = request.getToken();
        String buzzId = request.getBuzzId();
        String fileUrl = request.getFileUrl();
        String imageUrl = request.getImageUrl();
        String thumbnailUrl = request.getThumbnailUrl();
        Double imageWidth = request.getImageWidth();
        Double imageHeight = request.getImageHeight();
        Double thumbnailWidth = request.getThumbnailWidth();
        Double thumbnailHeight = request.getThumbnailHeight();
        Integer viewNumber = request.getViewNumber();
        Integer currentView = request.getCurrentView();
        Integer duration = request.getDuration();
        String status = request.getStatus();
        
        JSONObject formDataJson = new JSONObject();
        formDataJson.put(ParamKey.API_NAME, "get_user_id");
        formDataJson.put(ParamKey.TOKEN_STRING, token);
        JSONObject formData = Util.toJSONObject(Helper.requestAndG(formDataJson.toJSONString()));
        String email = (String) formData.get(ParamKey.EMAIL);
        String userName = (String) formData.get(ParamKey.USER_NAME);
        String userId = (String) formData.get(ParamKey.USER_ID);
        String videoStt = (String)formData.get(ParamKey.VIDEO_STATUS);
        Long videoStatus = 0L;
        if(videoStt != null){
            videoStatus = Long.parseLong(videoStt);
        }
        
        Util.addDebugLog("=======UpdateStreamURLApi=======");
        Util.addDebugLog("buzzId======="+buzzId);
        Util.addDebugLog("fileUrl======="+fileUrl);
        Util.addDebugLog("imageUrl======="+imageUrl);
        Util.addDebugLog("thumbnailUrl======="+thumbnailUrl);
        Util.addDebugLog("viewNumber======="+viewNumber);
        Util.addDebugLog("currentView======="+currentView);
        Util.addDebugLog("duration======="+duration);
        Util.addDebugLog("status======="+status);
        
        try {
            String fileId = "";
            String streamStatus = "";
            
            if(status == null){
                streamStatus = Constant.STREAM_STATUS.UPDATE;
            }else if(status.equals(Constant.STREAM_STATUS.ON)){
                streamStatus = Constant.STREAM_STATUS.ON;
                BuzzDAO.updateStreamStatus(buzzId, streamStatus);
                fileId = FileDAO.insertFileWithWidthHeight(fileUrl, userId, imageWidth, imageHeight,time,userName,email,videoStatus, null);
                ImageDAO.insertVideoImage(fileId, imageUrl, imageWidth, imageHeight);
                ThumbnailDAO.insertThumbnailWithWidthHeight(fileId, thumbnailUrl,thumbnailWidth,thumbnailHeight);
                JSONObject data = Util.toJSONObject(Helper.confirmStreamingVideo(token, buzzId, fileId, Constant.VIDEO_KIND_STRING.PUBLIC_VIDEO, request.getIp(), time));
            }else if(status.equals(Constant.STREAM_STATUS.PENDING)){
                streamStatus = Constant.STREAM_STATUS.PENDING;
            }else if(status.equals(Constant.STREAM_STATUS.RECORDING)){
                streamStatus = Constant.STREAM_STATUS.RECORDING;
                JSONObject data = Util.toJSONObject(Helper.confirmRecordingVideo(token, buzzId, fileId, Constant.VIDEO_KIND_STRING.PUBLIC_VIDEO, request.getIp(), time));
            }else{
                streamStatus = Constant.STREAM_STATUS.OFF;
                BuzzDAO.updateStreamStatus(buzzId, streamStatus);
                fileId = BuzzDAO.getBuzzFileId(buzzId);
                JSONObject data = Util.toJSONObject(Helper.confirmUploadVideo(token, buzzId, fileId, Constant.VIDEO_KIND_STRING.PUBLIC_VIDEO, request.getIp(), time));
            }
            
            List<JSONObject> videoList = new ArrayList();
            JSONObject temp = new JSONObject();
            if(!fileId.equals("")){
                temp.put("data", fileId);
                temp.put("is_app", Constant.FLAG.ON);
                videoList.add(temp);
            }
            
            Util.addDebugLog("streamStatus======="+streamStatus);
            JSONObject updateData = Util.toJSONObject(Helper.uploadStreamFile(token, buzzId, request.getIp(), time, videoList, null, viewNumber, currentView, duration, streamStatus));
            Util.addDebugLog("data : " + updateData.toJSONString());
            Long code = (Long) updateData.get(ParamKey.ERROR_CODE);
            if (code == ErrorCode.SUCCESS) {
                respond = new EntityRespond(ErrorCode.SUCCESS);
                return respond;
            }else{
                respond = new Respond(code.intValue());
            }
        } catch (Exception ex) {
            Logger.getLogger(AddBuzzApi.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        
        return respond;
    }
    
}
