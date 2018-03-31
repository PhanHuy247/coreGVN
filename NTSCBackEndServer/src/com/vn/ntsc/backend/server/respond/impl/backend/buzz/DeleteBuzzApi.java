/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.vn.ntsc.backend.server.respond.impl.backend.buzz;

import java.util.List;
import org.json.simple.JSONObject;
import eazycommon.constant.Constant;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.ParamKey;
import eazycommon.exception.EazyException;
import com.vn.ntsc.backend.dao.buzz.BuzzCommentDAO;
import com.vn.ntsc.backend.dao.buzz.BuzzDetailDAO;
import com.vn.ntsc.backend.dao.buzz.CommentDetailDAO;
import com.vn.ntsc.backend.dao.buzz.StatusDAO;
import com.vn.ntsc.backend.dao.buzz.UserBuzzDAO;
import com.vn.ntsc.backend.dao.user.ImageDAO;
import com.vn.ntsc.backend.dao.user.PbImageDAO;
import com.vn.ntsc.backend.dao.user.UserActivityDAO;
import com.vn.ntsc.backend.dao.user.UserDAO;
import com.vn.ntsc.backend.dao.user.UserGiftDAO;
import com.vn.ntsc.backend.entity.impl.buzz.Buzz;
import com.vn.ntsc.backend.entity.impl.image.ReviewImage;
import com.vn.ntsc.backend.server.respond.IApiAdapter;
import com.vn.ntsc.backend.server.respond.Respond;
import com.vn.ntsc.backend.server.respond.result.EntityRespond;
import eazycommon.util.Util;
import com.vn.ntsc.backend.dao.buzz.ReviewingBuzzDAO;
import com.vn.ntsc.backend.dao.buzz.ReviewingCommentDAO;
import com.vn.ntsc.backend.dao.buzz.ReviewingSubCommentDAO;
import com.vn.ntsc.backend.dao.user.BuzzUserDAO;
import com.vn.ntsc.backend.entity.impl.buzz.ReviewingComment;

/**
 *
 * @author RuAc0n
 */
public class DeleteBuzzApi implements IApiAdapter{

    @Override
    public Respond execute(JSONObject obj) {
        EntityRespond respond = new EntityRespond();
        try{
            String buzzId = Util.getStringParam(obj, ParamKey.BUZZ_ID);
            if (buzzId == null || buzzId.isEmpty()) {
                return null;
            }
            Buzz buzz = BuzzDetailDAO.getBuzzDetail(buzzId);
            String userId = BuzzDetailDAO.getUserId(buzzId);
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
            //del buzzCom
//            bCommentDao.deleteBuzz(buzzId);
            UserBuzzDAO.delBuzz(userId, buzzId);
            Long buzzType = null;
            int type = buzz.buzzType;
            String buzzVal = buzz.buzzVal;
            int isStatus = StatusDAO.checkStatus(buzzId, userId);
//            int isAva = Constant.NO;
            if (type == Constant.BUZZ_TYPE_VALUE.STATUS_BUZZ) {
                if (isStatus == Constant.FLAG.ON) {
                    buzzType = new Long(type);
                }
            } else {
                buzzType = new Long(type);
            }
            ReviewImage ri = new ReviewImage();
            ri.userId = userId;
            if (buzzType != null) {
                if (buzzType == Constant.BUZZ_TYPE_VALUE.STATUS_BUZZ) {
                    UserDAO.removeBuzz(userId);
                    UserActivityDAO.removeStatus(userId);
                }
                if (buzzType == Constant.BUZZ_TYPE_VALUE.IMAGE_BUZZ) {
                    if (PbImageDAO.checkPbImageExist(userId, buzzVal)) {
                        UserDAO.removePbImage(userId);
                    }
                    PbImageDAO.removePublicImage(userId, buzzId);
                    if (buzzVal != null) {
                        ImageDAO.removeImage(userId, buzzVal);
                    }
                    if(UserDAO.isAvatar(userId, buzzVal)){
//                        isAva = Constant.YES;
                        UserDAO.removeAvatar(userId, buzzVal);
                        ri.avatar = Constant.USER_STATUS_FLAG.DISABLE;
                    }
                }
                if(buzzType == Constant.BUZZ_TYPE_VALUE.GIFT_BUZZ){
                    UserGiftDAO.removeGift(userId, buzzVal);
                }
                BuzzUserDAO.remove(buzzId);
            }            
            respond = new EntityRespond(ErrorCode.SUCCESS, ri);
        }catch(EazyException ex){
            Util.addErrorLog(ex);            
            respond = new EntityRespond(ex.getErrorCode());
        }catch(Exception ex){
            Util.addErrorLog(ex);            
        }
        return respond;
    }
    
}
