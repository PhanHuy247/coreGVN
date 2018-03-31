/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.com.ntsc.staticfileserver.server.respond.impl;

import eazycommon.constant.ErrorCode;
import eazycommon.constant.ParamKey;
import eazycommon.util.Util;
import static eu.medsea.mimeutil.MimeUtil.getExtension;
import static eu.medsea.mimeutil.MimeUtil.getMimeTypes;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.Part;
import org.json.simple.JSONObject;
import vn.com.ntsc.staticfileserver.dao.impl.AlbumDAO;
import vn.com.ntsc.staticfileserver.entity.impl.AlbumData;
import vn.com.ntsc.staticfileserver.entity.impl.FileData;
import vn.com.ntsc.staticfileserver.server.Request;
import vn.com.ntsc.staticfileserver.server.Setting;
import vn.com.ntsc.staticfileserver.server.respond.IApiAdapter;
import vn.com.ntsc.staticfileserver.server.respond.Respond;
import vn.com.ntsc.staticfileserver.server.respond.common.Helper;
import static vn.com.ntsc.staticfileserver.server.respond.impl.AddBuzzApi.getString;
import vn.com.ntsc.staticfileserver.server.respond.result.EntityRespond;

/**
 *
 * @author hoangnh
 */
public class AddAlbumAndImage implements IApiAdapter{
    
    @Override
    public Respond execute(Request request, Date time) {
        EntityRespond result = new EntityRespond();
        Collection<Part> parts = request.parts;
        byte[] arrayImageInput ;
        
        JSONObject formData = getFormData(parts);
        
        String token = (String) formData.get(ParamKey.TOKEN_STRING);
        String albumName = (String) formData.get(ParamKey.ALBUM_NAME);
        String albumDes = (String) formData.get(ParamKey.ALBUM_DES);
        Integer privacy = (Integer) formData.get(ParamKey.PRIVACY);
        Boolean isValidFileNumber = (Boolean) formData.get("invalid");
        Integer countImg = (Integer) formData.get("count_item");
        
        if(isValidFileNumber && countImg <= Setting.max_file_per_album){
            if(albumName != null){
                JSONObject album = Util.toJSONObject(Helper.addAlbum(token, albumName, albumDes, privacy));
                JSONObject albumData = (JSONObject) album.get(ParamKey.DATA);
                String albumId = (String) albumData.get(ParamKey.ALBUM_ID);

                AlbumData data = new AlbumData();
                data.albumId = albumId;
                
                List<String> listImage = new ArrayList();
                for(Part part : parts){
                    if(part.getName().equals(ParamKey.FILES)){
                        try{
                            arrayImageInput = Helper.getInputArrayByte(part.getInputStream());
                            String fileType = Helper.getExtension(part.getSubmittedFileName());
                            String mimeType = part.getContentType();
                            Boolean isImage = mimeType.contains("image");
                            if(isImage){
                                FileData imageData = Helper.generateImage(arrayImageInput, fileType, time);
                                listImage.add(imageData.fileId);
                            }
                            //respond = new EntityRespond(ErrorCode.SUCCESS, new ListFileData(listImage,listVideo,listAudio));
                        }catch(Exception ex){
                            Util.addErrorLog(ex);
                        }
                    }
                }
                if(listImage.size() != 0){
                    JSONObject info = Util.toJSONObject(Helper.addAlbumImage(token, albumId, listImage));
                    Long uploadCode = (Long) info.get(ParamKey.ERROR_CODE);
                    if (uploadCode == ErrorCode.SUCCESS) {
                        AlbumDAO.updateNumberImage(albumId, listImage.size());
                    }
                }
                result = new EntityRespond(ErrorCode.SUCCESS, data);
            }else{
                result = new EntityRespond(ErrorCode.WRONG_DATA_FORMAT, null);
            }
        }else{
            result = new EntityRespond(ErrorCode.MAX_FILE_NUMBER, null);
        }
        return result;
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
//                if(part.getName().equals(ParamKey.ALBUM_ID)){
//                    data.put(ParamKey.ALBUM_ID, getString(part.getInputStream()));
//                }
                if(part.getName().equals(ParamKey.FILES)){
                    String mimeType = part.getContentType();
                    Boolean isImage = mimeType.contains("image");
                    if(isImage){
                        countImage++;
                    }
                }
                if(part.getName().equals(ParamKey.ALBUM_NAME)){
                    data.put(ParamKey.ALBUM_NAME, getString(part.getInputStream()));
                }
                if(part.getName().equals(ParamKey.ALBUM_DES)){
                    data.put(ParamKey.ALBUM_DES, getString(part.getInputStream()));
                }
                if(part.getName().equals(ParamKey.PRIVACY)){
                    String postMode = getString(part.getInputStream());
                    if(postMode != null && !postMode.isEmpty()){
                        data.put(ParamKey.PRIVACY, Integer.valueOf(postMode));
                    }else{
                        data.put(ParamKey.PRIVACY, 0);
                    }
                }
            }
            data.put("count_item", countImage);
            if(countImage > Setting.max_image_number && countImage != 0){
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
