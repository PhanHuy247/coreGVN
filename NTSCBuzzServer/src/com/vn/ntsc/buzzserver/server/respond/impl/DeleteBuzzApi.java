/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.buzzserver.server.respond.impl;

import com.vn.ntsc.buzzserver.dao.impl.BuzzCommentDAO;
import com.vn.ntsc.buzzserver.dao.impl.BuzzDetailDAO;
import com.vn.ntsc.buzzserver.dao.impl.CommentDAO;
import com.vn.ntsc.buzzserver.dao.impl.PbAudioDAO;
import com.vn.ntsc.buzzserver.dao.impl.PbImageDAO;
import com.vn.ntsc.buzzserver.dao.impl.PbVideoDAO;
import com.vn.ntsc.buzzserver.dao.impl.ReviewingCommentDAO;
import com.vn.ntsc.buzzserver.dao.impl.ReviewingSubCommentDAO;
import com.vn.ntsc.buzzserver.dao.impl.StatusDAO;
import com.vn.ntsc.buzzserver.dao.impl.UserBuzzDAO;
import eazycommon.exception.EazyException;
import eazycommon.util.Util;
import org.json.simple.JSONObject;
import eazycommon.constant.Constant;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.ParamKey;
import java.util.List;
import com.vn.ntsc.buzzserver.entity.impl.buzz.Buzz;
import com.vn.ntsc.buzzserver.entity.impl.datarespond.DeleteBuzzData;
import com.vn.ntsc.buzzserver.server.respond.IApiAdapter;
import com.vn.ntsc.buzzserver.server.respond.Respond;
import com.vn.ntsc.buzzserver.server.respond.result.EntityRespond;
import com.vn.ntsc.buzzserver.server.respond.result.ListEntityRespond;
import java.util.ArrayList;

/**
 *
 * @author RuAc0n
 */
public class DeleteBuzzApi implements IApiAdapter {

    @Override
    public Respond execute(JSONObject obj, long time) {
        Respond result = new ListEntityRespond();
        List<DeleteBuzzData> listData = new ArrayList<>();
        try {
            String userId = Util.getStringParam(obj, ParamKey.USER_ID);
            String buzzId = Util.getStringParam(obj, ParamKey.BUZZ_ID);
            if (buzzId == null || buzzId.isEmpty()) {
                result.code = ErrorCode.WRONG_DATA_FORMAT;
                return result;
            }
            Buzz buzz = BuzzDetailDAO.getBuzzDetail(buzzId);
            if(buzz.buzzId == null){
                result.code = ErrorCode.ACCESS_DENIED;
                return result;
            }
            String buzzOwner = buzz.userId;
//            int approvedFlag = Constant.REVIEW_STATUS_FLAG.APPROVED;
//            if(buzz.parentId != null && !buzz.parentId.isEmpty()){
//                approvedFlag = UserBuzzDAO.getApprovedFlag(userId, buzz.parentId);
//            }else{
//                approvedFlag = UserBuzzDAO.getApprovedFlag(userId, buzzId);
//            }
//            
//            
//            if (approvedFlag != Constant.REVIEW_STATUS_FLAG.APPROVED) {
//                result.code = ErrorCode.ACCESS_DENIED;
//                return result;
//            }
            if (!buzzOwner.equals(userId)) {
                result.code = ErrorCode.ACCESS_DENIED;
                return result;
            }
            if(buzz.buzzType == Constant.BUZZ_TYPE_VALUE.MULTI_STATUS){
                List<Buzz> listBuzz = BuzzDetailDAO.getListBuzzMulti(buzzId);
                delBuzz(buzzId, userId, buzz);
                for(Buzz buzzChild : listBuzz){
                    DeleteBuzzData deleteBuzzData = (DeleteBuzzData)delBuzz(buzzChild.buzzId, userId, buzzChild);
                    listData.add(deleteBuzzData);
                }
                result = new ListEntityRespond(ErrorCode.SUCCESS, listData);
            }else{
                DeleteBuzzData data = delBuzz(buzzId, userId, buzz);
                listData.add(data);
                result = new ListEntityRespond(ErrorCode.SUCCESS, listData);
            }
            //del like
//            LikeDAO.deleteBuzz(buzzId);
            // change flag comment
//            List<String> lCommnet = BuzzCommentDAO.getAllComment(buzzId);
//            for (String commentId : lCommnet) {
//                CommentDAO.updateFlag(commentId, Constant.NOT_AVAILABLE_FLAG);
//            }
            //del buzzCom
//            bCommentDao.deleteBuzz(buzzId);
            
            
            
        } catch (EazyException ex) {
            Util.addErrorLog(ex);
            result.code = ex.getErrorCode();
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
        return result;
    }
    private DeleteBuzzData delBuzz(String buzzId,String userId, Buzz buzz) throws EazyException{
        BuzzDetailDAO.updateFlag(buzzId, Constant.AVAILABLE_FLAG_VALUE.NOT_AVAILABLE_FLAG);
        ReviewingCommentDAO.removeByBuzzId(buzzId);
        ReviewingSubCommentDAO.removeByBuzzId(buzzId);
        List<String> lCommnet = BuzzCommentDAO.getAllComment(buzzId);
            for (int i = 0; i < lCommnet.size(); i++) {
                String commentId = lCommnet.get(i);
                CommentDAO.updateFlag(commentId, Constant.AVAILABLE_FLAG_VALUE.DISABLE_FLAG);
            }
            
            UserBuzzDAO.delBuzz(userId, buzzId);
            Long buzzType = (long) buzz.buzzType.intValue();
            String buzzVal = buzz.buzzVal;
            //add by Huy201709Oct
            String fileId = buzz.fileId;
            Util.addDebugLog("fileId-------------------------"+fileId);
            if(buzzType == Constant.BUZZ_TYPE_VALUE.VIDEO_STATUS)
                PbVideoDAO.updateFlag(userId, fileId, Constant.AVAILABLE_FLAG_VALUE.NOT_AVAILABLE_FLAG);
            if(buzzType == Constant.BUZZ_TYPE_VALUE.AUDIO_STATUS)
                PbAudioDAO.updateFlag(userId, fileId, Constant.AVAILABLE_FLAG_VALUE.NOT_AVAILABLE_FLAG);
            if(buzzType == Constant.BUZZ_TYPE_VALUE.IMAGE_STATUS)
                PbImageDAO.updateFlag(userId, fileId, Constant.AVAILABLE_FLAG_VALUE.NOT_AVAILABLE_FLAG);
            int isStatus;
            isStatus = StatusDAO.checkStatus(buzzId, userId);
            DeleteBuzzData data = new DeleteBuzzData(buzzType, buzzVal, (long) isStatus,fileId,buzzId,userId);
            return data;
    }
}
