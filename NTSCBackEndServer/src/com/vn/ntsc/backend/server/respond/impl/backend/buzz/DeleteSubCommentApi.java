/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.backend.server.respond.impl.backend.buzz;

import eazycommon.constant.Constant;
import eazycommon.constant.ErrorCode;
import eazycommon.exception.EazyException;
import eazycommon.util.Util;
import org.json.simple.JSONObject;
import com.vn.ntsc.backend.dao.buzz.CommentDetailDAO;
import com.vn.ntsc.backend.dao.buzz.ReviewingSubCommentDAO;
import com.vn.ntsc.backend.dao.buzz.SubCommentDAO;
import com.vn.ntsc.backend.dao.buzz.SubCommentListDAO;
import com.vn.ntsc.backend.entity.impl.buzz.SubComment;
import com.vn.ntsc.backend.server.respond.IApiAdapter;
import com.vn.ntsc.backend.server.respond.Respond;

/**
 *
 * @author RuAc0n
 */
public class DeleteSubCommentApi implements IApiAdapter {

    @Override
    public Respond execute(JSONObject obj) {
        Respond result = new Respond();
        try {
            String subCommentId = Util.getStringParam(obj, "sub_comment_id");
            if (subCommentId == null  || subCommentId.isEmpty()) {
                result.code = ErrorCode.WRONG_DATA_FORMAT;
                return result;
            }
            
            SubComment subComment = SubCommentDAO.get(subCommentId);
            
            if (subComment.flag != Constant.AVAILABLE_FLAG_VALUE.DISABLE_FLAG) {
                SubCommentDAO.updateFlag(subCommentId, Constant.AVAILABLE_FLAG_VALUE.DISABLE_FLAG);
                ReviewingSubCommentDAO.remove(subCommentId);
                SubCommentListDAO.delSubComment(subComment.commentId, subCommentId);
                if(subComment.flag == Constant.USER_STATUS_FLAG.ACTIVE)
                    CommentDetailDAO.removeSubComment(subComment.commentId);
            }
            result.code = ErrorCode.SUCCESS;
        } catch (EazyException ex) {
            Util.addErrorLog(ex);
            result.code = ex.getErrorCode();
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
        return result;
    }

}
