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
import java.util.Date;
import org.json.simple.JSONObject;
import vn.com.ntsc.staticfileserver.entity.impl.UploadImageData;
import vn.com.ntsc.staticfileserver.server.Request;
import vn.com.ntsc.staticfileserver.server.respond.common.Helper;
import vn.com.ntsc.staticfileserver.server.respond.common.MD5Checksum;
import vn.com.ntsc.staticfileserver.server.respond.result.EntityRespond;

/**
 *
 * @author RuAc0n
 */
public class UploadImageVersion2Api implements IApiAdapter {

    @Override
    public Respond execute(Request request, Date time) {
        Respond respond = new Respond();
        String imageCat = request.getImageCat();
        if (imageCat == null || imageCat.isEmpty() || request.getSum() == null || request.getSum().isEmpty()) {
            respond.code = ErrorCode.WRONG_DATA_FORMAT;
            return respond;
        }
        byte[] arrayImageInput = Helper.getInputArrayByte(request);
        if (arrayImageInput == null) {
            respond.code = ErrorCode.WRONG_DATA_FORMAT;
            return respond;
        }
        try {
            String token = request.getToken();
            int is_free = request.getIsFree();
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
                String imagePath = FilesAndFolders.FOLDERS.ORIGINAL_IMAGE_FOLDER + imageUrl;
                Helper.writeFile(imageUrl, arrayImageInput, FilesAndFolders.FOLDERS.ORIGINAL_IMAGE_FOLDER, time);
                String sum = MD5Checksum.getMD5Checksum(imagePath);
                if (sum == null || !sum.equals(request.getSum())) {
                    respond.code = ErrorCode.UPLOAD_FILE_ERROR;
                    return respond;
                }

                Util.addDebugLog("UploadImageVersion2Api 3 " + is_free);
                //insert image collection
                imageId = ImageDAO.insertImage2(imageUrl, is_free);
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
            Long code = (Long) data.get(ParamKey.ERROR_CODE);
            if (code == ErrorCode.SUCCESS) {
                JSONObject umsData = (JSONObject) data.get(ParamKey.DATA);
                Long isApp = (Long) umsData.get(ParamKey.IS_APPROVED_IMAGE);

                if (imageCat.equals(Constant.IMAGE_KIND_STRING.CHAT_IMAGE)) {
                    isApp = null;
                }
                UploadImageData uid = new UploadImageData(imageId, isApp);
                //create buzz
                if (imageCat.equals(Constant.IMAGE_KIND_STRING.AVATAR)) {
                    Helper.addBuzz(token, imageId, request.getIp(), isApp.intValue(), time);
                }
                respond = new EntityRespond(ErrorCode.SUCCESS, uid);
            } else {
                respond.code = code.intValue();
            }
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
        Util.addDebugLog("respond : " + respond.toJsonObject().toJSONString());
        return respond;
    }

}
