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
import eazycommon.exception.EazyException;
import eazycommon.util.Util;
import static eu.medsea.mimeutil.MimeUtil.getExtension;
import static eu.medsea.mimeutil.MimeUtil.getMimeTypes;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.servlet.http.Part;
import org.bson.types.ObjectId;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.FFmpegFrameRecorder;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.FrameGrabber;
import org.bytedeco.javacv.Java2DFrameConverter;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.images.Artwork;
import org.jcodec.api.FrameGrab;
import org.jcodec.api.MediaInfo;
import org.jcodec.common.model.Picture;
import org.jcodec.scale.AWTUtil;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import vn.com.ntsc.staticfileserver.dao.impl.BuzzDAO;
import vn.com.ntsc.staticfileserver.dao.impl.FileDAO;
import vn.com.ntsc.staticfileserver.dao.impl.ImageDAO;
import vn.com.ntsc.staticfileserver.dao.impl.ThumbnailDAO;
import vn.com.ntsc.staticfileserver.entity.file.FileInfo;
import vn.com.ntsc.staticfileserver.entity.impl.AddBuzzData;
import vn.com.ntsc.staticfileserver.entity.impl.UploadFileData;
import vn.com.ntsc.staticfileserver.entity.impl.UploadImageData;
import vn.com.ntsc.staticfileserver.server.Request;
import vn.com.ntsc.staticfileserver.server.Setting;
import vn.com.ntsc.staticfileserver.server.respond.IApiAdapter;
import vn.com.ntsc.staticfileserver.server.respond.Respond;
import vn.com.ntsc.staticfileserver.server.respond.common.Helper;
import static vn.com.ntsc.staticfileserver.server.respond.common.Helper.convertOrientation;
import static vn.com.ntsc.staticfileserver.server.respond.common.Helper.getAtt;
import static vn.com.ntsc.staticfileserver.server.respond.common.Helper.getOrientation;
import static vn.com.ntsc.staticfileserver.server.respond.common.Helper.rotateImage;
import vn.com.ntsc.staticfileserver.server.respond.common.MD5Checksum;
import static vn.com.ntsc.staticfileserver.server.respond.impl.UploadImageVersion3Api.writePart;
import vn.com.ntsc.staticfileserver.server.respond.result.EntityRespond;
import vn.com.ntsc.staticfileserver.server.videocollector.VideoCollector;

/**
 *
 * @author hoangnh
 */
public class AddBuzzApi implements IApiAdapter{

    @Override
    public Respond execute(Request request, Date time) {
        Util.addDebugLog("================== AddBuzzApi");
        Respond respond = new Respond();

        Collection<Part> parts = request.parts;
        byte[] arrayImageInput ;
        
        List<JSONObject> imgList = new ArrayList();
        List<JSONObject> videoList = new ArrayList();
        List<JSONObject> audioList = new ArrayList();
        
        
        JSONObject formDataJson = getFormData(parts);
        
        Boolean isInvalid = (Boolean) formDataJson.get("invalid");
        Boolean maxlength = (Boolean) formDataJson.get("maxlength");
        if(!isInvalid){
            formDataJson.put(ParamKey.API_NAME, "get_user_id");
            JSONObject formData = Util.toJSONObject(Helper.requestAndG(formDataJson.toJSONString()));
            String token = (String) formData.get(ParamKey.TOKEN_STRING);
            String buzzValue = (String) formData.get(ParamKey.BUZZ_VALUE);
            Long buzzTypeLong = (Long) formData.get(ParamKey.BUZZ_TYPE);
            String videoStt = (String)formData.get(ParamKey.VIDEO_STATUS);
            String streamUrl = (String)formData.get(ParamKey.STREAM_ID);
            String shareId = (String) formData.get(ParamKey.SHARE_ID);
            
            Long videoStatus = 0L;
            if(videoStt != null){
                videoStatus = Long.parseLong(videoStt);
            }
            Integer buzzType = null;
            Integer buzzTypeOtherBuzzShare = null;
            if(buzzTypeLong != null){
                buzzType = Integer.parseInt(String.valueOf(buzzTypeLong));
            }
            String buzzTag = (String) formData.get(ParamKey.TAG_LIST);
            Integer privacy = Integer.parseInt(String.valueOf(formData.get(ParamKey.PRIVACY)));
            String email = (String) formData.get(ParamKey.EMAIL);
            String userName = (String) formData.get(ParamKey.USER_NAME);
            String userId = (String) formData.get(ParamKey.USER_ID);
            
            Integer region = 0;
            if( formData.get(ParamKey.BUZZ_REGION) != null){
                region = Integer.parseInt(String.valueOf(formData.get(ParamKey.BUZZ_REGION)));
            }
            
//            Util.addDebugLog("token ================== "+token);
//            Util.addDebugLog("buzzValue ================== "+buzzValue);
//            Util.addDebugLog("buzzType ================== "+buzzType);
//            Util.addDebugLog("buzzTag ================== "+buzzTag);
//            Util.addDebugLog("request.getIp() ================== "+request.getIp());

            Integer countImage = 0;
            Integer countVideo = 0;
            Integer countAudio = 0;
            
            if(streamUrl == null && shareId == null ){
                for(Part part : parts){
                    if(part.getName().equals(ParamKey.FILES)){
                        try {
                            arrayImageInput = Helper.getInputArrayByte(part.getInputStream());
                            if(part.getName().equals("files")){
                                String fileType = Helper.getExtension(part.getSubmittedFileName());
                                String mimeType = part.getContentType();
                                Util.addDebugLog("mimeType=================="+mimeType);
                                Boolean isImage = mimeType.toString().contains("image");
                                if(isImage){ 
                                    UploadImageData respond_item = writePart(request, Constant.IMAGE_KIND_STRING.PUBLIC_IMAGE, arrayImageInput, time, token, fileType);
                                    if(respond_item != null){

                                        JSONObject temp = new JSONObject();
                                        temp.put("data", respond_item.image_id);
                                        temp.put("is_app", respond_item.is_app);
                                        imgList.add(temp);
                                    }
                                    countImage++;
                                }
                                Boolean isVideo = mimeType.toString().contains("video");
                                if(isVideo){
                                    UploadFileData respond_item = writeVideo(request,Constant.VIDEO_KIND_STRING.PUBLIC_VIDEO, arrayImageInput, time,token, userId,userName,email,fileType,videoStatus);
                                    if(respond_item != null){
                                        JSONObject temp = new JSONObject();
                                        temp.put("data", respond_item.file_id);
                                        temp.put("is_app", respond_item.is_app);
                                        videoList.add(temp);
                                    }
                                    countVideo++;
                                }
                                Boolean isAudio = mimeType.toString().contains("audio");
                                if(isAudio){

                                    UploadFileData respond_item = writeAudio(request,Constant.AUDIO_KIND_STRING.PUBLIC_AUDIO, arrayImageInput, time,token, userId,userName,email,fileType,videoStatus);
                                    if(respond_item != null){
                                        JSONObject temp = new JSONObject();
                                        temp.put("data", respond_item.file_id);
                                        temp.put("cover", respond_item.image_id);
                                        temp.put("is_app", respond_item.is_app);
                                        audioList.add(temp);
                                    }
                                    countAudio++;
                                }
                            }
                        } catch (IOException ex) {
                            Util.addErrorLog(ex);
                        }
                    }
                }
            }
            
//            Util.addDebugLog("imgList ================== "+imgList);
//            Util.addDebugLog("videoList ================== "+videoList);
//            Util.addDebugLog("audioList ================== "+audioList);
//            Util.addDebugLog("buzzValue==== "+buzzValue);
            if(streamUrl != null){
                buzzType = Constant.BUZZ_TYPE_VALUE.STREAM_STATUS;
            }else if(shareId != null){
                buzzType = Constant.BUZZ_TYPE_VALUE.SHARE_STATUS;
            }else if(countImage == 0 && countVideo == 0 && countAudio == 0){
                buzzType = Constant.BUZZ_TYPE_VALUE.TEXT_STATUS;
//            }else if(countImage == 1 && countVideo == 0 && countAudio == 0 && (buzzValue == null || buzzValue.equals(""))){
//                buzzType = Constant.BUZZ_TYPE_VALUE.IMAGE_STATUS;
//            }else if(countImage == 0 && countVideo == 1 && countAudio == 0 && (buzzValue == null || buzzValue.equals(""))){
//                buzzType = Constant.BUZZ_TYPE_VALUE.VIDEO_STATUS;
//            }else if(countImage == 0 && countVideo == 0 && countAudio == 1 && (buzzValue == null || buzzValue.equals(""))){
//                buzzType = Constant.BUZZ_TYPE_VALUE.AUDIO_STATUS;
            }else if((countImage + countVideo + countAudio) >= 1){
                buzzType = Constant.BUZZ_TYPE_VALUE.MULTI_STATUS;
            }
            
//            Util.addDebugLog("buzzType==== "+buzzType);
           
            if(buzzType == Constant.BUZZ_TYPE_VALUE.SHARE_STATUS && !ObjectId.isValid(shareId)){
                respond = new Respond(ErrorCode.WRONG_DATA_FORMAT);
            }else{
                try {
                    if(buzzType == Constant.BUZZ_TYPE_VALUE.SHARE_STATUS)
                        buzzTypeOtherBuzzShare = BuzzDAO.getBuzzType(shareId);
                    Util.addDebugLog("buzzTypeOtherBuzzShare===================================="+buzzTypeOtherBuzzShare);
                } catch (EazyException ex) {
                    Util.addDebugLog("buzzTypeOtherBuzzShare=======================");
                }
                JSONObject data = Util.toJSONObject(Helper.addStatus(token, buzzValue, buzzType, request.getIp(), time, imgList, videoList, audioList, buzzTag, privacy, streamUrl, shareId, buzzTypeOtherBuzzShare, region));
                Util.addDebugLog("data : " + data.toJSONString());

                Long code = (Long) data.get(ParamKey.ERROR_CODE);
                if (code == ErrorCode.SUCCESS) {
                    JSONObject obj = (JSONObject) data.get(ParamKey.DATA);

                    String id = (String) obj.get(ParamKey.BUZZ_ID);
                    String cmtId = (String) obj.get(ParamKey.COMMENT_ID);
                    Long affFlag = (Long) obj.get(ParamKey.IS_APPROVED_IMAGE);
                    Long approveCommentFlag = (Long) obj.get("comment_app");
                    List<String> img_id_list = (List<String>) obj.get(ParamKey.IMG_LIST);
                    AddBuzzData buzzData = new AddBuzzData(id, cmtId, null, affFlag.intValue(), approveCommentFlag.intValue(), img_id_list);
                    Util.addDebugLog("buzzData : " + buzzData.toJsonObject().toJSONString());
                    respond = new EntityRespond(ErrorCode.SUCCESS, buzzData);
                }else{
                    respond = new Respond(code.intValue());
                }
            }
        }else{
            if(maxlength){
                respond = new Respond(ErrorCode.MAX_LENGTH_BUZZ);
            }else{
                respond = new Respond(ErrorCode.MAX_FILE_NUMBER);
            }
        }
        
        
        Util.addDebugLog("respond : " + respond.toJsonObject().toJSONString());
        return respond;
    }
    
    public static String getString (InputStream data){
        String respond = "";
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(data));
            StringBuilder sb = new StringBuilder();
            String line;
            Integer count = 0;
            while ((line = br.readLine()) != null) {
                if(count > 0){
                    sb.append(System.getProperty("line.separator"));
                }
                sb.append(line);
                count++;
            }
            respond = sb.toString();
        } catch (IOException ex) {
            Logger.getLogger(UploadImageVersion3Api.class.getName()).log(Level.SEVERE, null, ex);
        }
        return respond;
    }
    
    public static UploadImageData writePart(Request request,String imageCat,byte[] arrayImageInput, Date time, String token, String fileType){
        UploadImageData respond = null;
        try {
            
            String imageId;
            String imageUrl;
            try {
                imageUrl = Helper.createUrlPath(false, fileType);
                Util.addDebugLog("imageUrl ================== "+imageUrl);
//                String imagePath = FilesAndFolders.FOLDERS.ORIGINAL_IMAGE_FOLDER + imageUrl;
//                Helper.writeFile(imageUrl, arrayImageInput, FilesAndFolders.FOLDERS.ORIGINAL_IMAGE_FOLDER,time);
                
                 //add by Huy 201705Oct
                BufferedImage img = ImageIO.read(new ByteArrayInputStream(arrayImageInput));
                
                Integer orientation = getOrientation(new ByteArrayInputStream(arrayImageInput));
                Util.addDebugLog("orientation========"+orientation);
                if(orientation != 0){
                    img = rotateImage(orientation, img);
                }
                
                Helper.writeFile(imageUrl, img, FilesAndFolders.FOLDERS.ORIGINAL_IMAGE_FOLDER,time,fileType);
                
                Double imgWidth = new Double(img.getWidth());
                Double imgHeight = new Double(img.getHeight());
                
                //insert image collection
                imageId = ImageDAO.insertImagewithLengthWidth(imageUrl, imgWidth,imgHeight);
//                Util.addDebugLog("imageId ================== "+imageId);

                //add by Huy 201709Oct
                Double widthThumnail = (double) imgWidth / (double) Constant.THUMBNAIL_WIDTH_SIZE;
                Double heightThumnail = (double) imgHeight / (double) Constant.THUMBNAIL_WIDTH_SIZE;
                //write thumbnail image
                String thumbnailUrl = Helper.createUrlPath(true, FilesAndFolders.EXTENSIONS.IMAGE_JPG_EXTENSION);
//                Helper.createThumbnailImage(imageUrl, thumbnailUrl, time);
                Helper.createThumbnailImage(img, thumbnailUrl, time);
                //insert thumbnail collection
                ThumbnailDAO.insertThumbnailWithWidthHeight(imageId, thumbnailUrl, widthThumnail, heightThumnail);
            } catch (Exception ex) {
                Util.addErrorLog(ex);
                return respond;
            }
            
            // save db in UMS
            JSONObject data = Util.toJSONObject(Helper.confirmUploadImage(token, imageId, imageCat, request.getIp(), time));
//            Util.addDebugLog("data ================== "+data.toJSONString());
            Long code = (Long) data.get(ParamKey.ERROR_CODE);
            if (code == ErrorCode.SUCCESS) {
                JSONObject umsData = (JSONObject) data.get(ParamKey.DATA);
                Long isApp = (Long) umsData.get(ParamKey.IS_APPROVED_IMAGE);

                if (imageCat.equals(Constant.IMAGE_KIND_STRING.CHAT_IMAGE)) {
                    isApp = null;
                }
                UploadImageData uid = new UploadImageData(imageId, isApp, imageUrl);
                //create buzz
                if (imageCat.equals(Constant.IMAGE_KIND_STRING.AVATAR)) {
                    Helper.addBuzz(token, imageId, request.getIp(), isApp.intValue(), time);
                }
//                Util.addDebugLog("uid ================== "+uid.toJsonObject().toString());
                respond = uid;
            }
        } catch (Exception ex) {
            Logger.getLogger(AddBuzzApi.class.getName()).log(Level.SEVERE, null, ex);
        }
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
            //get rotate
            Integer orientation = convertOrientation(grabber.getVideoMetadata("rotate"));
            if(orientation != 0){
                img = rotateImage(orientation, img);
            }
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
            
//            UploadFileData uid = new UploadFileData(fileId);
//            respond = uid;
            
            JSONObject data = Util.toJSONObject(Helper.confirmUploadVideo(token, null, fileId, videoCat, request.getIp(), time));
            Long code = (Long) data.get(ParamKey.ERROR_CODE);
            if (code == ErrorCode.SUCCESS) {
                JSONObject uploadData = (JSONObject) data.get(ParamKey.DATA);
                Long isApp = (Long) uploadData.get(ParamKey.IS_APPROVED_IMAGE);
                
                UploadFileData uid = new UploadFileData(fileId, isApp);
                respond = uid;
            }
        } catch (EazyException | IOException ex) {
            Logger.getLogger(AddBuzzApi.class.getName()).log(Level.SEVERE, null, ex);
        }
        return respond;
    }
    
    public static UploadFileData writeAudio(Request request,String audioCat, byte[] arrayFileInput, Date time,String token, String userId,String userName,String email,String fileType,Long videoStatus){
        UploadFileData respond = null;
        try{
            String fileUrl = Helper.createUrlPath(false, fileType);
            String filePath = FilesAndFolders.FOLDERS.FILES_FOLDER + fileUrl;

            Helper.writeFile(fileUrl, arrayFileInput, FilesAndFolders.FOLDERS.FILES_FOLDER,time);
            String fileId = "";
            String imageId = "";
            AudioFile f = AudioFileIO.read(new File(filePath));
            Integer fileDuration = f.getAudioHeader().getTrackLength();
            Tag tag = f.getTag();
            Artwork artwork = null;
            try{
                artwork = tag.getFirstArtwork();
            }catch(Exception ex){
                Util.addDebugLog("can't get artwork");
            }
            
            if(artwork != null){
                byte[] imageRawData = artwork.getBinaryData();
                BufferedImage img = ImageIO.read(new ByteArrayInputStream(imageRawData));
                Double imgWidth = new Double(img.getWidth(null));
                Double imgHeight = new Double(img.getHeight(null));
                String imageUrl = Helper.createUrlPath(false, FilesAndFolders.EXTENSIONS.IMAGE_JPG_EXTENSION);
                Helper.writeFile(imageUrl, imageRawData, FilesAndFolders.FOLDERS.ORIGINAL_IMAGE_FOLDER,time);
                imageId = ImageDAO.insertImagewithLengthWidth(imageUrl, imgWidth,imgHeight);
            }else{
                imageId = Setting.default_audio_img;
            }
            
            fileId = FileDAO.insertFileWithWidthHeight(fileUrl, userId,0.0,0.0,time,userName,email,videoStatus,fileDuration);
//            UploadFileData uid = new UploadFileData(fileId, imageId);
//            respond = uid;

            JSONObject data = Util.toJSONObject(Helper.confirmUploadAudio(token, fileId, audioCat, request.getIp(), time,imageId));
            Util.addDebugLog("data ================== "+data.toJSONString());
            Long code = (Long) data.get(ParamKey.ERROR_CODE);
            if (code == ErrorCode.SUCCESS) {
                JSONObject uploadData = (JSONObject) data.get(ParamKey.DATA);
                Long isApp = (Long) uploadData.get(ParamKey.IS_APPROVED_IMAGE);
                
                UploadFileData uid = new UploadFileData(fileId, imageId, isApp);
                respond = uid;
            }
        }catch (Exception ex){
            Logger.getLogger(AddBuzzApi.class.getName()).log(Level.SEVERE, null, ex);
        }
        return respond;
    }

    
    public static JSONObject getFormData(Collection<Part> parts){
        JSONObject data = new JSONObject();
        Integer countImage = 0;
        Integer countVideo = 0;
        Integer countAudio = 0;
        Integer totalFile = 0;
        String buzzVal = "";
        try{
            for(Part part : parts){
                if(part.getName().equals(ParamKey.TOKEN_STRING)){
                    data.put(ParamKey.TOKEN_STRING, getString(part.getInputStream()));
                }
                if(part.getName().equals(ParamKey.BUZZ_VALUE)){
                    buzzVal = getString(part.getInputStream());
                    Util.addDebugLog("buzzVal==========="+buzzVal);
                    data.put(ParamKey.BUZZ_VALUE, buzzVal);
                }
                if(part.getName().equals(ParamKey.TAG_LIST)){
                    data.put(ParamKey.TAG_LIST, getString(part.getInputStream()));
                }
                if(part.getName().equals(ParamKey.POST_MODE)){
                    String postMode = getString(part.getInputStream());
                    data.put(ParamKey.POST_MODE, Integer.valueOf(postMode));
                }
                if(part.getName().equals(ParamKey.BUZZ_TYPE)){
                    String buzzType = getString(part.getInputStream());
                    data.put(ParamKey.BUZZ_TYPE, Integer.valueOf(buzzType));
                }
                if(part.getName().equals(ParamKey.PRIVACY)){
                    String postMode = getString(part.getInputStream());
                    if(postMode != null && !postMode.isEmpty()){
                        data.put(ParamKey.PRIVACY, Integer.valueOf(postMode));
                    }else{
                        data.put(ParamKey.PRIVACY, 0);
                    }
                }
                if(part.getName().equals(ParamKey.SHARE_ID)){
                    data.put(ParamKey.SHARE_ID, getString(part.getInputStream()));
                }
                if(part.getName().equals(ParamKey.FILES)){
                    String mimeType = part.getContentType();
                    //Collection mimeType = getMimeTypes(part.getInputStream());
                    Util.addDebugLog("mimeType.toString()==========="+mimeType);
                    Boolean isImage = mimeType.contains("image");
                    if(isImage){
                        countImage++;
                    }
                    Boolean isVideo = mimeType.contains("video");
                    if(isVideo){
                        countVideo++;
                    }
                    Boolean isAudio = mimeType.contains("audio");
                    if(isAudio){
                        countAudio++;
                    }
                    totalFile++;
                }
                if(part.getName().equals(ParamKey.STREAM_ID)){
                    String url = getString(part.getInputStream());
                    if(url != null && !url.isEmpty()){
                        data.put(ParamKey.STREAM_ID, url);
                    }else{
                        data.put(ParamKey.STREAM_ID, null);
                    }
                }
                if(part.getName().equals(ParamKey.BUZZ_REGION)){
                    String region = getString(part.getInputStream());
                    if(region != null && !region.isEmpty()){
                        data.put(ParamKey.BUZZ_REGION, Integer.valueOf(region));
                    }else{
                        data.put(ParamKey.BUZZ_REGION, 0);
                    }
                }
            }
            data.put("invalid", false);
            data.put("maxlength", false);
            if(buzzVal.length() > Setting.max_length_buzz){
                data.put("invalid", true);
                data.put("maxlength", true);
            }
            if(countImage > Setting.max_image_number || countVideo > Setting.max_video_number || countAudio > Setting.max_audio_number || totalFile > Setting.total_file_upload){
                data.put("invalid", true);
            }
        }catch(IOException ex){
            Logger.getLogger(AddBuzzApi.class.getName()).log(Level.SEVERE, null, ex);
        }
        return data;
    }
}
