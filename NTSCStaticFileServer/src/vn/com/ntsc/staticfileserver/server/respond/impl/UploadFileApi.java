/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.com.ntsc.staticfileserver.server.respond.impl;

import vn.com.ntsc.staticfileserver.server.respond.IApiAdapter;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.ParamKey;
import java.io.IOException;
import java.util.Date;
import vn.com.ntsc.staticfileserver.server.Request;
import vn.com.ntsc.staticfileserver.server.respond.Respond;
import vn.com.ntsc.staticfileserver.server.respond.common.Helper;
import vn.com.ntsc.staticfileserver.server.respond.result.EntityRespond;
import eazycommon.util.Util;
import static eu.medsea.mimeutil.MimeUtil.getExtension;
import static eu.medsea.mimeutil.MimeUtil.getMimeTypes;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.Part;
import org.json.simple.JSONObject;
import vn.com.ntsc.staticfileserver.entity.impl.FileData;
import vn.com.ntsc.staticfileserver.entity.impl.ListFileData;
import vn.com.ntsc.staticfileserver.server.Setting;
import vn.com.ntsc.staticfileserver.server.respond.result.ListEntityRespond;

/**
 *
 * @author RuAc0n
 */
public class UploadFileApi implements IApiAdapter {

    @Override
    public Respond execute(Request request, Date time) {
        Respond respond = new Respond();
        Collection<Part> parts = request.parts;
        byte[] arrayImageInput ;
        
        JSONObject formData = getFormData(parts);
        Boolean isValidFileNumber = (Boolean) formData.get("invalid");
        
        List<FileData> listImage = new ArrayList();
        List<FileData> listVideo = new ArrayList();
        List<FileData> listAudio = new ArrayList();
        List<FileData> listFile = new ArrayList();

        if(isValidFileNumber){
            for(Part part : parts){
                if(part.getName().equals(ParamKey.FILES)){
                    try{
                        arrayImageInput = Helper.getInputArrayByte(part.getInputStream());
//                        String fileType = getExtension(part.getSubmittedFileName());
                        String fileType = Helper.getExtension(part.getSubmittedFileName());
                        Util.addDebugLog("fileType======="+fileType);
                        String mimeType = part.getContentType();
                        Util.addDebugLog("mimeType======="+mimeType);
                        Boolean isImage = mimeType.contains("image");
                        if(isImage){
                            FileData imageData = Helper.generateImage(arrayImageInput, fileType, time);
                            imageData.fileType = "image";
                            //listImage.add(imageData);
                            listFile.add(imageData);
                        }
                        Boolean isVideo = mimeType.contains("video");
                        if(isVideo){
                            FileData videoData = Helper.generateVideo(arrayImageInput, fileType, time);
                            videoData.fileType = "video";
                            //listVideo.add(videoData);
                            listFile.add(videoData);
                        }
                        Boolean isAudio = mimeType.contains("audio");
                        if(isAudio){
                            FileData audioData = Helper.generateAudio(arrayImageInput, fileType, time);
                            audioData.fileType = "audio";
                            //listAudio.add(audioData);
                            listFile.add(audioData);
                        }
                        respond = new ListEntityRespond(ErrorCode.SUCCESS, listFile);
                    }catch(Exception ex){
                        Util.addErrorLog(ex);
                    }
                }
            }
        }else{
            respond = new Respond(ErrorCode.MAX_FILE_NUMBER);
        }         
        return respond;
    }
    
    public static String getString (InputStream data){
        String respond = "";
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(data));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            respond = sb.toString();
        } catch (IOException ex) {
            Logger.getLogger(UploadImageVersion3Api.class.getName()).log(Level.SEVERE, null, ex);
        }
        return respond;
    }
    
    public static JSONObject getFormData(Collection<Part> parts){
        JSONObject data = new JSONObject();
        Integer countImage = 0;
        Integer countVideo = 0;
        Integer countAudio = 0;
        Integer totalFile = 0;
        try{
            for(Part part : parts){
                if(part.getName().equals(ParamKey.TOKEN_STRING)){
                    data.put(ParamKey.TOKEN_STRING, getString(part.getInputStream()));
                }
                if(part.getName().equals(ParamKey.FILES)){
                    String mimeType = part.getContentType();
                    //Collection mimeType = getMimeTypes(part.getInputStream());
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
            }
            if(countImage > Setting.max_image_number || countVideo > Setting.max_video_number || countAudio > Setting.max_audio_number || totalFile > Setting.total_file_upload){
                data.put("invalid", false);
            }else{
                data.put("invalid", true);
            }
        }catch(IOException ex){
            Logger.getLogger(AddBuzzApi.class.getName()).log(Level.SEVERE, null, ex);
        }
        return data;
    }

}
