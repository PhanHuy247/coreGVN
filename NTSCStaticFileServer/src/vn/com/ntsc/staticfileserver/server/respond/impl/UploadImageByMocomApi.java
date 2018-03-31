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
import eazycommon.constant.ParamKey;
import eazycommon.util.Util;
import eazycommon.constant.Constant;
import eazycommon.constant.ErrorCode;
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import eazycommon.constant.FilesAndFolders;
import java.io.IOException;
import java.util.Date;
import org.json.simple.JSONObject;
import vn.com.ntsc.staticfileserver.entity.impl.UploadImageData;
import vn.com.ntsc.staticfileserver.server.Request;
import vn.com.ntsc.staticfileserver.server.respond.common.Helper;
import vn.com.ntsc.staticfileserver.server.respond.result.EntityRespond;

/**
 *
 * @author RuAc0n
 */
public class UploadImageByMocomApi implements IApiAdapter{

    @Override
    public Respond execute(Request request, Date time) {
        Respond respond = new Respond();
        String imageCat = request.getImageCat();
        if (imageCat == null || imageCat.isEmpty()) {
            respond.code = ErrorCode.WRONG_DATA_FORMAT;
            return respond;
        }
        byte[] arrayImageInput = Helper.getInputArrayByte(request);
        String imageString = new String(arrayImageInput);
        byte[] imageBytes = Base64.decode(imageString);
        if (imageBytes == null) {
            respond.code = ErrorCode.WRONG_DATA_FORMAT;
            return respond;
        }
        try {
            String token = request.getToken();
            String imageUrl = Helper.createUrlPath(false, FilesAndFolders.EXTENSIONS.IMAGE_JPG_EXTENSION);
            Helper.writeFile(imageUrl, imageBytes, FilesAndFolders.FOLDERS.ORIGINAL_IMAGE_FOLDER, time);

            //insert image collection
            String imageId = ImageDAO.insertImage(imageUrl);

            //write thumbnail image
            String thumbnailUrl = Helper.createUrlPath(true, FilesAndFolders.EXTENSIONS.IMAGE_JPG_EXTENSION);
            Helper.createThumbnailImage(imageUrl, thumbnailUrl, time);

            //insert thumbnail collection
            ThumbnailDAO.insertThumbnail(imageId, thumbnailUrl);

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
        } catch (IOException ex) {
            Util.addErrorLog(ex);
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
        Util.addDebugLog("respond : " + respond.toJsonObject().toJSONString());        
        return respond;
    }
    
}
