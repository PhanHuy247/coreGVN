/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.backend.server.respond.impl.backend.buzz;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.ParamKey;
import eazycommon.exception.EazyException;
import eazycommon.util.Util;
import org.json.simple.JSONObject;
import com.vn.ntsc.backend.dao.buzz.BuzzCommentDAO;
import com.vn.ntsc.backend.dao.buzz.CommentDetailDAO;
import com.vn.ntsc.backend.dao.buzz.SubCommentDAO;
import com.vn.ntsc.backend.dao.buzz.SubCommentListDAO;
import com.vn.ntsc.backend.dao.user.UserDAO;
import com.vn.ntsc.backend.entity.impl.buzz.Comment;
import com.vn.ntsc.backend.entity.impl.buzz.SubComment;
import com.vn.ntsc.backend.entity.impl.datarespond.SizedListData;
import com.vn.ntsc.backend.server.respond.IApiAdapter;
import com.vn.ntsc.backend.server.respond.Respond;
import com.vn.ntsc.backend.server.respond.result.EntityRespond;

/**
 *
 * @author RuAc0n
 */
public class GetCommentApi implements IApiAdapter {

    @Override
    public Respond execute(JSONObject obj) {
        EntityRespond respond = new EntityRespond();
        try {
            String buzzId = Util.getStringParam(obj, ParamKey.BUZZ_ID);
            Long skip = Util.getLongParam(obj, ParamKey.SKIP);
            Long take = Util.getLongParam(obj, ParamKey.TAKE);
            if (buzzId == null || skip == null || take == null) {
                return null;
            }
            List<String> lCommnet = BuzzCommentDAO.getListComment(buzzId);
            List<Comment> lCommentDetail = new ArrayList<>();
            if (!lCommnet.isEmpty()) {
                lCommentDetail = CommentDetailDAO.getListComment(lCommnet);
            }
            int total = lCommentDetail.size();
            List<Comment> respondList = new ArrayList<>();
            if (skip < lCommentDetail.size()) {
                long startIndex = skip;
                long endIndex = startIndex + take;
                if (endIndex > lCommentDetail.size()) {
                    endIndex = lCommentDetail.size();
                }
                respondList = lCommentDetail.subList((int) startIndex, (int) endIndex);
                List<String> listCommentId = new ArrayList<>();
                for (Comment cmt : respondList) {
                    listCommentId.add(cmt.userId);
//                    List<SubComment> subComments = Arrays.asList(new SubComment(), new SubComment(), new SubComment(), new SubComment());
//                    System.out.println("commentId :" + cmt.cmtId);
                    List<String> subCommentIds = SubCommentListDAO.getListSubComment(cmt.cmtId);
//                    System.out.println("size : " + subCommentIds.size());
                    List<SubComment> subComments = SubCommentDAO.get4CommentByIds(subCommentIds);
                    cmt.subComment = subComments;
                    for(SubComment subComment : subComments){
                        listCommentId.add(subComment.userId);
                    }
                    cmt.subComment = subComments;
                    
                }
                Map<String, String> mCommentName = UserDAO.getUserName(listCommentId);
                for (Comment cmt : respondList) {
                    String name = mCommentName.get(cmt.userId);
                    cmt.userName = name;
                    if(cmt.subCommentNumber != null && cmt.subCommentNumber > 0){
                        for(SubComment subComment : cmt.subComment){
                            name = mCommentName.get(subComment.userId);
                            subComment.userName = name;
                        }
                    }
                }
            }
            SizedListData data = new SizedListData(total, respondList);
            respond = new EntityRespond(ErrorCode.SUCCESS, data);
        } catch (EazyException ex) {
            Util.addErrorLog(ex);
            respond = new EntityRespond(ex.getErrorCode());
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
        return respond;
    }

}
