/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.com.ntsc.staticfileserver.server.respond.impl;

import eazycommon.constant.Constant;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.FilesAndFolders;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.Date;
import javax.imageio.ImageIO;
import eazycommon.exception.EazyException;
import vn.com.ntsc.staticfileserver.server.Request;
import vn.com.ntsc.staticfileserver.server.respond.IApiAdapter;
import vn.com.ntsc.staticfileserver.server.respond.Respond;
import vn.com.ntsc.staticfileserver.server.respond.common.Helper;
import vn.com.ntsc.staticfileserver.server.respond.result.ByteRespond;
import eazycommon.util.Util;
import vn.com.ntsc.staticfileserver.dao.impl.UserImageDAO;

/**
 *
 * @author RuAc0n
 */
public class LoadImageWithSizeApi implements IApiAdapter {

    @Override
    public Respond execute(Request request, Date time) {
        Respond respond = new Respond();
        String imageId = request.getImageId();
        String withSizeStr = request.getWidthSize();
        if (imageId == null || imageId.isEmpty() || withSizeStr == null || withSizeStr.isEmpty()) {
            respond.code = ErrorCode.WRONG_DATA_FORMAT;
            return respond;
        }
        try {
            int withSize = Integer.parseInt(withSizeStr);
            String imageUrl = Helper.getImageUrl(imageId, Constant.IMAGE_KIND_VALUE.ORIGINAL_IMAGE);
            Util.addDebugLog("imageId  =========" + imageId);
            Util.addDebugLog("imageUrl  =========" + imageUrl);
            if (imageUrl == null) {
                Util.addDebugLog("check imageUrl  =========" + imageUrl);
                return null;
            }
//            if (!UserImageDAO.imageExist(imageId, request.getUserId())) {
//                Util.addDebugLog("check UserImageDAO.imageExist  =========" + imageId);
//                Util.addDebugLog("check UserImageDAO.imageExist request.getUserId() =========" + request.getUserId());
//                return null;
//            }
            BufferedImage img = ImageIO.read(new File(imageUrl));
            double scale = (double) img.getWidth(null) / (double) withSize;
            int height = new Double(img.getHeight(null) / scale).intValue();
            Image newImg = img.getScaledInstance(withSize, height, Image.SCALE_SMOOTH);
            BufferedImage bsi = new BufferedImage(newImg.getWidth(null), newImg.getHeight(null), BufferedImage.TYPE_INT_RGB);
            bsi.getGraphics().drawImage(newImg, 0, 0, null);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(bsi, FilesAndFolders.EXTENSIONS.IMAGE_JPG_EXTENSION, baos);
            ByteRespond result = new ByteRespond();
            result.data = baos.toByteArray();
            respond = result;
            Util.addDebugLog("result =====" + result.toString() + "imageUrl  =========" + imageUrl);
        } catch (EazyException ex) {
            Util.addErrorLog(ex);
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
        return respond;
    }
}
