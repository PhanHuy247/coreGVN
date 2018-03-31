/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.vn.ntsc.backend.server.respond.impl.backend.buzz;

import org.json.simple.JSONObject;
import eazycommon.constant.Constant;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.ParamKey;
import eazycommon.exception.EazyException;
import com.vn.ntsc.backend.dao.buzz.BuzzCommentDAO;
import com.vn.ntsc.backend.dao.buzz.BuzzDetailDAO;
import com.vn.ntsc.backend.dao.buzz.CommentDetailDAO;
import com.vn.ntsc.backend.server.respond.IApiAdapter;
import com.vn.ntsc.backend.server.respond.Respond;
import eazycommon.util.Util;
import com.vn.ntsc.backend.dao.buzz.ReviewingCommentDAO;
import com.vn.ntsc.backend.dao.buzz.ReviewingSubCommentDAO;

/**
 *
 * @author RuAc0n
 */
public class DeleteCommentApi implements IApiAdapter{

    @Override
    public Respond execute(JSONObject obj) {
        Respond respond = new Respond();
        try{
            String commentId = Util.getStringParam(obj, ParamKey.COMMENT_ID);
            String buzzId = Util.getStringParam(obj, ParamKey.BUZZ_ID);
            int flag = CommentDetailDAO.getFlag(commentId);
            if ( flag != Constant.AVAILABLE_FLAG_VALUE.DISABLE_FLAG) {
                CommentDetailDAO.updateFlag(commentId, Constant.AVAILABLE_FLAG_VALUE.DISABLE_FLAG);
                ReviewingCommentDAO.remove(commentId);
                ReviewingSubCommentDAO.removeByCommentId(commentId);
                BuzzCommentDAO.delComment(buzzId, commentId);
                if(flag == Constant.USER_STATUS_FLAG.ACTIVE)
                    BuzzDetailDAO.removeComment(buzzId);
            }
            respond = new Respond(ErrorCode.SUCCESS);
        }catch(EazyException ex){
            Util.addErrorLog(ex);            
            respond = new Respond(ex.getErrorCode());
        }catch(Exception ex){
            Util.addErrorLog(ex);            
        }
        return respond;
    }
    
}
