/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.backend.server.respond.impl.backend.image;

import java.util.List;
import eazycommon.constant.Constant;
import eazycommon.constant.ErrorCode;
import eazycommon.exception.EazyException;
import eazycommon.util.Util;
import org.json.simple.JSONObject;
import eazycommon.constant.ParamKey;
import com.vn.ntsc.backend.dao.buzz.BuzzCommentDAO;
import com.vn.ntsc.backend.dao.buzz.BuzzDetailDAO;
import com.vn.ntsc.backend.dao.buzz.CommentDetailDAO;
import com.vn.ntsc.backend.dao.buzz.ReviewingBuzzDAO;
import com.vn.ntsc.backend.dao.buzz.ReviewingCommentDAO;
import com.vn.ntsc.backend.dao.buzz.ReviewingSubCommentDAO;
import com.vn.ntsc.backend.dao.buzz.UserBuzzDAO;
import com.vn.ntsc.backend.dao.user.BackstageDAO;
import com.vn.ntsc.backend.dao.user.BuzzUserDAO;
import com.vn.ntsc.backend.dao.user.ImageDAO;
import com.vn.ntsc.backend.dao.user.PbImageDAO;
import com.vn.ntsc.backend.dao.user.UserDAO;
import com.vn.ntsc.backend.entity.impl.image.Image;
import com.vn.ntsc.backend.entity.impl.image.ReviewImage;
import com.vn.ntsc.backend.server.respond.IApiAdapter;
import com.vn.ntsc.backend.server.respond.Respond;
import com.vn.ntsc.backend.server.respond.result.EntityRespond;

/**
 *
 * @author RuAc0n
 */
public class DeleteImageApi implements IApiAdapter {

    @Override
    public Respond execute(JSONObject obj) {
        EntityRespond respond = new EntityRespond();
        try {
            String imageId = Util.getStringParam(obj, ParamKey.IMAGE_ID);
            if (imageId == null || imageId.isEmpty()) {
                return null;
            }
            ReviewImage ri = new ReviewImage();
            Image image = ImageDAO.getImageInfor(imageId);
            if (image == null) {
                return new EntityRespond(ErrorCode.UNKNOWN_ERROR);
            }
            ri.imageId = imageId;
            ri.userId = image.userId;
            if (image.flag == Constant.FLAG.ON) {
                if (image.imageType == Constant.IMAGE_TYPE_VALUE.IMAGE_BACKSTAGE) {
                    if (BackstageDAO.checkBackStageExist(image.userId, imageId)) {
                        UserDAO.removeBackstage(image.userId);
                        BackstageDAO.updateFlag(image.userId, imageId, Constant.AVAILABLE_FLAG_VALUE.NOT_AVAILABLE_FLAG);
                    }
                } else if (image.imageType == Constant.IMAGE_TYPE_VALUE.IMAGE_PUBLIC) {
                    if (PbImageDAO.checkPbImageExist(image.userId, imageId)) {
                        UserDAO.removePbImage(image.userId);
                        UserDAO.removeBuzz(image.userId);
                        PbImageDAO.updateFlag(image.userId, imageId, Constant.AVAILABLE_FLAG_VALUE.NOT_AVAILABLE_FLAG);
                        if (UserDAO.isAvatar(image.userId, imageId)) {
                            UserDAO.removeAvatar(image.userId, imageId);
                            ri.avatar = Constant.USER_STATUS_FLAG.DISABLE;
                        }
                        String buzzId = PbImageDAO.getBuzzId(image.userId, imageId);
                        BuzzDetailDAO.updateFlag(buzzId, Constant.AVAILABLE_FLAG_VALUE.DISABLE_FLAG);
                        ReviewingBuzzDAO.remove(buzzId);
                        ReviewingCommentDAO.removeByBuzzId(buzzId);
                        ReviewingSubCommentDAO.removeByBuzzId(buzzId);
                        // change flag comment
                        List<String> lCommnet = BuzzCommentDAO.getAllComment(buzzId);
                        for (int i = 0; i < lCommnet.size(); i++) {
                            String commentId = lCommnet.get(i);
                            CommentDetailDAO.updateFlag(commentId, Constant.AVAILABLE_FLAG_VALUE.DISABLE_FLAG);
                        }
                        UserBuzzDAO.delBuzz(image.userId, buzzId);
                        BuzzUserDAO.remove(buzzId);
                    }
                }
            }
            respond = new EntityRespond(ErrorCode.SUCCESS, ri);
        } catch (EazyException ex) {
            Util.addErrorLog(ex);
            respond = new EntityRespond(ex.getErrorCode());
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
        return respond;
    }

}
