/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.com.ntsc.staticfileserver.server.respond.impl;

import vn.com.ntsc.staticfileserver.server.respond.IApiAdapter;
import vn.com.ntsc.staticfileserver.server.respond.Respond;
import vn.com.ntsc.staticfileserver.dao.impl.ThumbnailDAO;
import vn.com.ntsc.staticfileserver.dao.impl.ImageDAO;
import eazycommon.constant.Constant;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.FilesAndFolders;
import eazycommon.constant.ParamKey;
import eazycommon.util.Util;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.Part;
import org.json.simple.JSONObject;
import sun.misc.IOUtils;
import vn.com.ntsc.staticfileserver.entity.impl.UploadImageData;
import vn.com.ntsc.staticfileserver.server.Request;
import vn.com.ntsc.staticfileserver.server.respond.common.Helper;
import vn.com.ntsc.staticfileserver.server.respond.common.MD5Checksum;
import vn.com.ntsc.staticfileserver.server.respond.result.EntityRespond;

/**
 *
 * @author RuAc0n
 */
public class UploadImageVersion3Api implements IApiAdapter {

    @Override
    public Respond execute(Request request, Date time) {
        Respond respond = new Respond();
//        String imageCat = request.getImageCat();
//        if (imageCat == null || imageCat.isEmpty() || request.getSum() == null || request.getSum().isEmpty()) {
//            respond.code = ErrorCode.WRONG_DATA_FORMAT;
//            return respond;
//        }

        Collection<Part> parts = request.parts;
        byte[] arrayImageInput ;
        String token = "";
        for(Part part : parts){
            if(part.getName().equals("token")){
                try {
                    BufferedReader br = new BufferedReader(new InputStreamReader(part.getInputStream()));
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line = br.readLine()) != null) {
                        sb.append(line);
                    }
                    Util.addDebugLog("UploadImageVersion3Api ================== string "  + sb.toString());
                    token = sb.toString();
                } catch (IOException ex) {
                    Logger.getLogger(UploadImageVersion3Api.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        
        for(Part part : parts){
            Util.addDebugLog("UploadImageVersion3Api ================== getContentType " + part.getContentType());
            Util.addDebugLog("UploadImageVersion3Api ================== getName " + part.getName());
            Util.addDebugLog("UploadImageVersion3Api ================== getSubmittedFileName " + part.getSubmittedFileName());
            Util.addDebugLog("UploadImageVersion3Api ================== getHeaderNames " + part.getHeaderNames());
            Util.addDebugLog("UploadImageVersion3Api ================== getSize "  + part.getSize());
            
            for(String name : part.getHeaderNames()){
                Util.addDebugLog("UploadImageVersion3Api ================== getHeader " + name + " " + part.getHeader(name));
            }
            try {
                arrayImageInput = Helper.getInputArrayByte(part.getInputStream());
                Util.addDebugLog("arrayImageInput "  + arrayImageInput);
                if(part.getName().equals("files")){
                    Respond respond_item = new Respond();
                    respond_item = writePart(request, Constant.IMAGE_KIND_STRING.PUBLIC_IMAGE, arrayImageInput, time, token);
                    Util.addDebugLog("respond_item ================== " + respond_item.toJsonObject().toJSONString());
                }
            } catch (IOException ex) {
                Util.addErrorLog(ex);
            }
        }
        
//        if (arrayImageInput == null) {
//            respond.code = ErrorCode.WRONG_DATA_FORMAT;
//            return respond;
//        }
        
        Util.addDebugLog("respond : " + respond.toJsonObject().toJSONString());
        return respond;
    }
    
    public static Respond writePart(Request request,String imageCat,byte[] arrayImageInput, Date time, String token){
        Respond respond = new Respond();
        try {
//            String token = request.getToken();
//            int is_free = request.getIsFree();
            int is_free = 0;
            Util.addDebugLog("UploadImageVersion2Api " + is_free);
            // write original image
            if (imageCat.equals(Constant.IMAGE_KIND_STRING.THUMBNAIL_VIDEO)) {
                String fileId = request.getFileId();
                String imageUrl = Helper.createUrlPath(false, FilesAndFolders.EXTENSIONS.IMAGE_JPG_EXTENSION);
                try {
                    Helper.writeFile(imageUrl, arrayImageInput, FilesAndFolders.FOLDERS.ORIGINAL_IMAGE_FOLDER, time);

                    //insert image collection
                    ImageDAO.insertImage2(imageUrl, fileId, is_free);
                    Util.addDebugLog("UploadImageVersion2Api 2" + is_free);

                    //write thumbnail image
                    String thumbnailUrl = Helper.createUrlPath(true, FilesAndFolders.EXTENSIONS.IMAGE_JPG_EXTENSION);

                    Helper.createThumbnailImage(imageUrl, thumbnailUrl, time);
                    //insert thumbnail collection
                    ThumbnailDAO.insertThumbnail(fileId, thumbnailUrl);
                } catch (Exception ex) {
                    Util.addErrorLog(ex);
                    respond.code = ErrorCode.UPLOAD_IMAGE_ERROR;
                    return respond;
                }

                UploadImageData data = new UploadImageData(fileId);
                respond = new EntityRespond(ErrorCode.SUCCESS, data);
                return respond;
            }
            String imageId;
            try {
                String imageUrl = Helper.createUrlPath(false, FilesAndFolders.EXTENSIONS.IMAGE_JPG_EXTENSION);
                Util.addDebugLog("imageUrl " + imageUrl);
                String imagePath = FilesAndFolders.FOLDERS.ORIGINAL_IMAGE_FOLDER + imageUrl;
                Util.addDebugLog("imagePath " + imagePath);
                Helper.writeFile(imageUrl, arrayImageInput, FilesAndFolders.FOLDERS.ORIGINAL_IMAGE_FOLDER, time);
                String sum = MD5Checksum.getMD5Checksum(imagePath);
                Util.addDebugLog("UPLOADED SUM " + request.getSum() +" ======== REAL FILE SUM " + sum );
//                if (sum == null || !sum.equals(request.getSum())) {
//                    respond.code = ErrorCode.UPLOAD_FILE_ERROR;
//                    return respond;
//                }

                Util.addDebugLog("UploadImageVersion2Api 3 " + is_free);
                //insert image collection
                imageId = ImageDAO.insertImage2(imageUrl, is_free);
                Util.addDebugLog("imageId " + imageId);
                Util.addDebugLog("UploadImageVersion2Api 4 " + is_free);

                //write thumbnail image
                String thumbnailUrl = Helper.createUrlPath(true, FilesAndFolders.EXTENSIONS.IMAGE_JPG_EXTENSION);
                Helper.createThumbnailImage(imageUrl, thumbnailUrl, time);

                //insert thumbnail collection
                ThumbnailDAO.insertThumbnail(imageId, thumbnailUrl);
            } catch (Exception ex) {
                Util.addErrorLog(ex);
                respond.code = ErrorCode.UPLOAD_IMAGE_ERROR;
                return respond;
            }

            // save db in UMS
            JSONObject data = Util.toJSONObject(Helper.confirmUploadImage(token, imageId, imageCat, request.getIp(), time));
            Util.addDebugLog("data " + data);
            Long code = (Long) data.get(ParamKey.ERROR_CODE);
            if (code == ErrorCode.SUCCESS) {
                JSONObject umsData = (JSONObject) data.get(ParamKey.DATA);
                Long isApp = (Long) umsData.get(ParamKey.IS_APPROVED_IMAGE);

                if (imageCat.equals(Constant.IMAGE_KIND_STRING.CHAT_IMAGE)) {
                    isApp = null;
                }
                UploadImageData uid = new UploadImageData(imageId, isApp);
                Util.addDebugLog("uid " + uid);
                //create buzz
                if (imageCat.equals(Constant.IMAGE_KIND_STRING.AVATAR)) {
                    Util.addDebugLog("imageCat.equals ");
                    Helper.addBuzz(token, imageId, request.getIp(), isApp.intValue(), time);
                }
                respond = new EntityRespond(ErrorCode.SUCCESS, uid);
            } else {
                respond.code = code.intValue();
            }
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
        return respond;
    }

}
