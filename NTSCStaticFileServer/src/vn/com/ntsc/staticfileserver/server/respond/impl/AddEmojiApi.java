/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.com.ntsc.staticfileserver.server.respond.impl;

import eazycommon.constant.ErrorCode;
import eazycommon.constant.ParamKey;
import eazycommon.util.Util;
import eu.medsea.mimeutil.MimeUtil;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.Part;
import org.json.simple.JSONObject;
import vn.com.ntsc.staticfileserver.entity.impl.FileData;
import vn.com.ntsc.staticfileserver.entity.impl.InsertData;
import vn.com.ntsc.staticfileserver.server.Request;
import vn.com.ntsc.staticfileserver.server.respond.IApiAdapter;
import vn.com.ntsc.staticfileserver.server.respond.Respond;
import vn.com.ntsc.staticfileserver.server.respond.common.Helper;
import vn.com.ntsc.staticfileserver.server.respond.result.EntityRespond;

/**
 *
 * @author hoangnh
 */
public class AddEmojiApi implements IApiAdapter{

    @Override
    public Respond execute(Request request, Date time) {
        EntityRespond result = new EntityRespond();
        Collection<Part> parts = request.parts;
        byte[] arrayImageInput ;
        
        JSONObject formData = getFormData(parts);
        
        String token = (String) formData.get(ParamKey.TOKEN_STRING);
        String catId = (String) formData.get(ParamKey.CATEGORY_ID);
        String code = (String) formData.get(ParamKey.EMOJI_CODE);
        String name = (String) formData.get(ParamKey.EMOJI_NAME);
        Boolean inInvalid = (Boolean) formData.get("invalid");
        FileData imageData = new FileData();
        
        if(!inInvalid){
            for(Part part: parts){
                if(part.getName().equals(ParamKey.FILES)){
                    try{
                        arrayImageInput = Helper.getInputArrayByte(part.getInputStream());
                        String fileType = Helper.getExtension(part.getSubmittedFileName());
                        String mimeType = part.getContentType();
                        Boolean isImage = mimeType.contains("image");
                        if(isImage){
                            imageData = Helper.generateEmoji(arrayImageInput, catId, name, fileType, time);
                        }
                    }catch(Exception ex){
                        Util.addErrorLog(ex);
                    }
                }
            }
            JSONObject info = Util.toJSONObject(Helper.addEmoji(token, catId, code, name, imageData.fileId, imageData.fileType));
            Long uploadCode = (Long) info.get(ParamKey.ERROR_CODE);
            if(uploadCode.intValue() == ErrorCode.SUCCESS){
                JSONObject obj = (JSONObject) info.get(ParamKey.DATA);
                String emojiId = (String) obj.get(ParamKey.ID);
                Long warningFlag = (Long) obj.get(ParamKey.WARNING_FLAG);
                InsertData data = new InsertData(emojiId, warningFlag.intValue());
                result = new EntityRespond(ErrorCode.SUCCESS, data);
            }else{
                result = new EntityRespond(uploadCode.intValue());
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
        try{
            for(Part part : parts){
                if(part.getName().equals(ParamKey.TOKEN_STRING)){
                    data.put(ParamKey.TOKEN_STRING, getString(part.getInputStream()));
                }
                if(part.getName().equals(ParamKey.CATEGORY_ID)){
                    data.put(ParamKey.CATEGORY_ID, getString(part.getInputStream()));
                }
                if(part.getName().equals(ParamKey.EMOJI_CODE)){
                    data.put(ParamKey.EMOJI_CODE, getString(part.getInputStream()));
                }
                if(part.getName().equals(ParamKey.EMOJI_NAME)){
                    data.put(ParamKey.EMOJI_NAME, getString(part.getInputStream()));
                }
                if(part.getName().equals(ParamKey.FILES)){
                    String mimeType = part.getContentType();
                    Boolean isImage = mimeType.contains("image");
                    if(isImage){
                        countImage++;
                    }
                }
            }
            if(countImage == 1){
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
