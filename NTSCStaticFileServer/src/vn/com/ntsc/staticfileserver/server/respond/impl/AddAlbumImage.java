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
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.Part;
import org.json.simple.JSONObject;
import vn.com.ntsc.staticfileserver.dao.impl.AlbumDAO;
import vn.com.ntsc.staticfileserver.dao.impl.ImageDAO;
import vn.com.ntsc.staticfileserver.dao.impl.ThumbnailDAO;
import vn.com.ntsc.staticfileserver.entity.impl.AlbumData;
import vn.com.ntsc.staticfileserver.entity.impl.FileData;
import vn.com.ntsc.staticfileserver.server.Request;
import vn.com.ntsc.staticfileserver.server.Setting;
import vn.com.ntsc.staticfileserver.server.respond.IApiAdapter;
import vn.com.ntsc.staticfileserver.server.respond.Respond;
import vn.com.ntsc.staticfileserver.server.respond.common.Helper;
import vn.com.ntsc.staticfileserver.server.respond.result.EntityRespond;

/**
 *
 * @author hoangnh
 */
public class AddAlbumImage implements IApiAdapter{

    @Override
    public Respond execute(Request request, Date time) {
        Util.addDebugLog("==========AddAlbumImage=======");
        EntityRespond respond = new EntityRespond();
        Collection<Part> parts = request.parts;
        byte[] arrayImageInput ;
        
        JSONObject formData = getFormData(parts);
        String token = (String) formData.get(ParamKey.TOKEN_STRING);
        String albumId = (String) formData.get(ParamKey.ALBUM_ID);
        
        Boolean isValidFileNumber = (Boolean) formData.get("invalid");
        Integer countImg = (Integer) formData.get("count_item");

        List<String> listImage = new ArrayList();
        
        if(isValidFileNumber){
            Boolean isAlbumOwned = false;
            AlbumData album = new AlbumData();
        
            JSONObject data = Util.toJSONObject(Helper.checkAlbumOwned(token, albumId));
            Long code = (Long) data.get(ParamKey.ERROR_CODE);
            if (code == ErrorCode.SUCCESS) {
                isAlbumOwned = true;
                
                JSONObject albumData = (JSONObject) data.get(ParamKey.DATA);
                
                album.albumId = (String) albumData.get(ParamKey.ALBUM_ID);
                album.userId = (String) albumData.get(ParamKey.USER_ID);
                album.albumDes = (String) albumData.get(ParamKey.ALBUM_DES);
                album.albumName = (String) albumData.get(ParamKey.ALBUM_NAME);
                album.privacy = ((Long) albumData.get(ParamKey.PRIVACY)).intValue();
                album.time = (Long) albumData.get(ParamKey.TIME);
                album.numberImage = ((Long) albumData.get("number_image")).intValue();
                
                if((album.numberImage + countImg) > Setting.max_file_per_album){
                    Util.addDebugLog("=================max item per album");
                    respond = new EntityRespond(ErrorCode.MAX_FILE_NUMBER);
                }else{
                    if(isAlbumOwned){
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
                                }catch(Exception ex){
                                    Util.addErrorLog(ex);
                                }
                            }
                        }
                        if(!listImage.isEmpty()){
                            JSONObject info = Util.toJSONObject(Helper.addAlbumImage(token, albumId, listImage));
                            Long uploadCode = (Long) info.get(ParamKey.ERROR_CODE);
                            if (uploadCode == ErrorCode.SUCCESS) {
                                AlbumDAO.updateNumberImage(albumId, listImage.size());

                                List<String> returnImage = new ArrayList<>();
                                returnImage.add(listImage.get(0));
                                Map<String, FileData> mapOrginal = ImageDAO.getMapImageData(returnImage);
                                Map<String, FileData> mapThumnail = ThumbnailDAO.getMapImageData(returnImage);
                                List<FileData> listImageData = new LinkedList<>();

                                for (String id : returnImage){
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
                                    album.imageList = temp;
                                }
                                album.numberImage = album.numberImage + listImage.size();

                                respond = new EntityRespond(ErrorCode.SUCCESS,album);
                            }
                        }
                    }else{
                        respond = new EntityRespond(ErrorCode.ACCESS_DENIED);
                    }
                }
            }else{
                respond = new EntityRespond(ErrorCode.ACCESS_DENIED);
            }
        }else{
            respond = new EntityRespond(ErrorCode.MAX_FILE_NUMBER);
        }
        
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
                if(part.getName().equals(ParamKey.ALBUM_ID)){
                    data.put(ParamKey.ALBUM_ID, getString(part.getInputStream()));
                }
                if(part.getName().equals(ParamKey.FILES)){
                    String mimeType = part.getContentType();
                    Boolean isImage = mimeType.contains("image");
                    if(isImage){
                        countImage++;
                    }
                }
            }
            data.put("count_item", countImage);
            if(countImage > Setting.max_image_number){
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
