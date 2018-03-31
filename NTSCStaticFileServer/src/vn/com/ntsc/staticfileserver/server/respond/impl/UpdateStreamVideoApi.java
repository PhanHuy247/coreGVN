/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.com.ntsc.staticfileserver.server.respond.impl;

import eazycommon.constant.Constant;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.FilesAndFolders;
import eazycommon.constant.ParamKey;
import eazycommon.util.Util;
import static eu.medsea.mimeutil.MimeUtil.getExtension;
import static eu.medsea.mimeutil.MimeUtil.getMimeTypes;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.Part;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.Java2DFrameConverter;
import org.json.simple.JSONObject;
import vn.com.ntsc.staticfileserver.dao.impl.FileDAO;
import vn.com.ntsc.staticfileserver.dao.impl.ImageDAO;
import vn.com.ntsc.staticfileserver.dao.impl.ThumbnailDAO;
import vn.com.ntsc.staticfileserver.entity.file.FileInfo;
import vn.com.ntsc.staticfileserver.entity.impl.UploadFileData;
import vn.com.ntsc.staticfileserver.server.Request;
import vn.com.ntsc.staticfileserver.server.Setting;
import vn.com.ntsc.staticfileserver.server.respond.IApiAdapter;
import vn.com.ntsc.staticfileserver.server.respond.Respond;
import vn.com.ntsc.staticfileserver.server.respond.common.Helper;
import static vn.com.ntsc.staticfileserver.server.respond.impl.AddBuzzApi.getString;
import vn.com.ntsc.staticfileserver.server.respond.result.EntityRespond;
import vn.com.ntsc.staticfileserver.server.videocollector.VideoCollector;

/**
 *
 * @author hoangnh
 */
public class UpdateStreamVideoApi implements IApiAdapter{

    @Override
    public Respond execute(Request request, Date time) {
        Util.addDebugLog("================== UpdateStreamVideoApi ==================");
        Respond respond = new Respond();
        
        Collection<Part> parts = request.parts;
        byte[] arrayImageInput ;
        List<JSONObject> videoList = new ArrayList();
        
        JSONObject formDataJson = getFormData(parts);
        
        Boolean invalid = (Boolean) formDataJson.get("invalid");
        if(!invalid){
            formDataJson.put(ParamKey.API_NAME, "get_user_id");
            JSONObject formData = Util.toJSONObject(Helper.requestAndG(formDataJson.toJSONString()));
            
            String token = (String) formDataJson.get(ParamKey.TOKEN_STRING);
            String buzzId = (String) formDataJson.get(ParamKey.BUZZ_ID);
            Integer privacy = (Integer) formDataJson.get(ParamKey.PRIVACY);
            
            String email = (String) formData.get(ParamKey.EMAIL);
            String userName = (String) formData.get(ParamKey.USER_NAME);
            String userId = (String) formData.get(ParamKey.USER_ID);
            String videoStt = (String)formData.get(ParamKey.VIDEO_STATUS);
            Long videoStatus = 0L;
            if(videoStt != null){
                videoStatus = Long.parseLong(videoStt);
            }
            for(Part part : parts){
                if(part.getName().equals(ParamKey.FILES)){
                    try {
                        arrayImageInput = Helper.getInputArrayByte(part.getInputStream());
                        if(part.getName().equals("files")){
                            String fileType = Helper.getExtension(part.getSubmittedFileName());
                            String mimeType = part.getContentType();
                            Util.addDebugLog("mimeType=================="+mimeType);
                            Boolean isVideo = mimeType.toString().contains("video");
                            if(isVideo){
                                UploadFileData respond_item = writeVideo(request,Constant.VIDEO_KIND_STRING.PUBLIC_VIDEO, arrayImageInput, time,token, userId,userName,email,fileType,videoStatus);
                                if(respond_item != null){
                                    JSONObject temp = new JSONObject();
                                    temp.put("data", respond_item.file_id);
                                    temp.put("is_app", Constant.FLAG.ON);
                                    videoList.add(temp);
                                }
                            }
                        }
                    } catch (IOException ex) {
                        Util.addErrorLog(ex);
                    }
                }
            }
            JSONObject data = Util.toJSONObject(Helper.uploadStreamFile(token, buzzId, request.getIp(), time, videoList, privacy, 0, 0, 0, ""));
            Util.addDebugLog("data : " + data.toJSONString());
            Long code = (Long) data.get(ParamKey.ERROR_CODE);
            if (code == ErrorCode.SUCCESS) {
//                JSONObject obj = (JSONObject) data.get(ParamKey.DATA);
//
//                String id = (String) obj.get(ParamKey.BUZZ_ID);
//                String cmtId = (String) obj.get(ParamKey.COMMENT_ID);
//                Long affFlag = (Long) obj.get(ParamKey.IS_APPROVED_IMAGE);
//                Long approveCommentFlag = (Long) obj.get("comment_app");
//                List<String> img_id_list = (List<String>) obj.get(ParamKey.IMG_LIST);
//                AddBuzzData buzzData = new AddBuzzData(id, cmtId, null, affFlag.intValue(), approveCommentFlag.intValue(), img_id_list);
//                Util.addDebugLog("buzzData : " + buzzData.toJsonObject().toJSONString());
                respond = new EntityRespond(ErrorCode.SUCCESS);
                return respond;
            }else{
                respond = new Respond(code.intValue());
            }
        }else{
            respond = new Respond(ErrorCode.MAX_FILE_NUMBER);
        }
        Util.addDebugLog("respond : " + respond.toJsonObject().toJSONString());
        return respond;
    }
    
    public static UploadFileData writeVideo(Request request,String videoCat, byte[] arrayFileInput, Date time,String token, String userId,String userName, String email, String fileType,Long videoStatus){
        UploadFileData respond = null;
        try{
            //String fileUrl = Helper.createUrlPath(false, fileType);
            String fileUrl = Helper.createUrlPath(false, FilesAndFolders.EXTENSIONS.VIDEO_MP4_EXTENSION);
            String filePath = FilesAndFolders.FOLDERS.FILES_FOLDER + fileUrl;
            
            Helper.writeFile(fileUrl, arrayFileInput, FilesAndFolders.FOLDERS.FILES_FOLDER,time);
            String fileId;
            String dbPath = fileUrl;
            
            
            FFmpegFrameGrabber grabber = new FFmpegFrameGrabber(new File(filePath));
            grabber.start();
            Long fileDuration = grabber.getLengthInTime();
            Frame frame = grabber.grabImage();
            Java2DFrameConverter converter = new Java2DFrameConverter();
            BufferedImage img = converter.getBufferedImage(frame);
            grabber.stop();
            
            Double width = new Double(img.getWidth());
            Double height = new Double(img.getHeight());
            double widthThumnail = (double) width / (double) Constant.THUMBNAIL_WIDTH_SIZE;
            Double heightThumnail = (double) height / (double) Constant.THUMBNAIL_WIDTH_SIZE;
            Integer fileLength = fileDuration.intValue() / 1000000;
            
            fileId = FileDAO.insertFileWithWidthHeight(dbPath, userId,width,height,time,userName,email,videoStatus,fileLength);         
            VideoCollector.put(fileId, new FileInfo(dbPath, System.currentTimeMillis()));  
            
            //write origin image
            String originUrl = Helper.createUrlPath(true, FilesAndFolders.EXTENSIONS.IMAGE_JPG_EXTENSION);
            //Helper.writeFile(originUrl, arrayImageInput, FilesAndFolders.FOLDERS.ORIGINAL_IMAGE_FOLDER,time);
            
            //File destFile = new File(FilesAndFolders.FOLDERS.ORIGINAL_IMAGE_FOLDER + originUrl);
            //destFile.getParentFile().mkdirs();
            //AWTUtil.savePicture(picture, FilesAndFolders.EXTENSIONS.IMAGE_JPG_EXTENSION, destFile);
            Helper.createImage(originUrl, img, time);
            
            ImageDAO.insertVideoImage(fileId, originUrl, width, height);

            //write thumbnail image
            String thumbnailUrl = Helper.createUrlPath(true, FilesAndFolders.EXTENSIONS.IMAGE_JPG_EXTENSION);
            
            Helper.createThumbnailVideo(img, thumbnailUrl, time);
            
            //insert thumbnail collection
            ThumbnailDAO.insertThumbnailWithWidthHeight(fileId, thumbnailUrl,widthThumnail,heightThumnail);
            UploadFileData uid = new UploadFileData(fileId);
            respond = uid;
            
            
            JSONObject data = Util.toJSONObject(Helper.confirmUploadVideo(token, null, fileId, videoCat, request.getIp(), time));
            Long code = (Long) data.get(ParamKey.ERROR_CODE);
            
        } catch (Exception ex) {
            Logger.getLogger(AddBuzzApi.class.getName()).log(Level.SEVERE, null, ex);
        }
        return respond;
    }
    
    public static JSONObject getFormData(Collection<Part> parts){
        JSONObject data = new JSONObject();
        Integer countVideo = 0;
        try{
            for(Part part : parts){
                if(part.getName().equals(ParamKey.TOKEN_STRING)){
                    data.put(ParamKey.TOKEN_STRING, getString(part.getInputStream()));
                }
                if(part.getName().equals(ParamKey.BUZZ_ID)){
                    data.put(ParamKey.BUZZ_ID, getString(part.getInputStream()));
                }
                if(part.getName().equals(ParamKey.PRIVACY)){
                    String postMode = getString(part.getInputStream());
                    if(postMode != null && !postMode.isEmpty()){
                        data.put(ParamKey.PRIVACY, Integer.valueOf(postMode));
                    }else{
                        data.put(ParamKey.PRIVACY, 0);
                    }
                }
                if(part.getName().equals(ParamKey.FILES)){
                    String mimeType = part.getContentType();
                    //Collection mimeType = getMimeTypes(part.getInputStream());
                    Boolean isVideo = mimeType.contains("video");
                    if(isVideo){
                        countVideo++;
                    }
                }
            }
            if(countVideo == 0 || countVideo > 1){
                data.put("invalid", true);
            }else{
                data.put("invalid", false);
            }
        }catch(IOException ex){
            Logger.getLogger(AddBuzzApi.class.getName()).log(Level.SEVERE, null, ex);
        }
        return data;
    }
}
