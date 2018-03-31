/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.backend.server.respond.impl.backend.buzz;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.ParamKey;
import eazycommon.exception.EazyException;
import eazycommon.util.Util;
import org.json.simple.JSONObject;
import com.vn.ntsc.backend.dao.buzz.SubCommentDAO;
import com.vn.ntsc.backend.dao.buzz.SubCommentListDAO;
import com.vn.ntsc.backend.entity.impl.buzz.SubComment;
import com.vn.ntsc.backend.server.respond.IApiAdapter;
import com.vn.ntsc.backend.server.respond.Respond;
import com.vn.ntsc.backend.server.respond.result.ListEntityRespond;
/**
 *
 * @author RuAc0n
 */
public class ListSubCommentApi implements IApiAdapter {

    @Override
    public Respond execute(JSONObject obj) {
        ListEntityRespond result = new ListEntityRespond();
        try {
            String commentId = Util.getStringParam(obj, ParamKey.COMMENT_ID);
            Long take = Util.getLongParam(obj, ParamKey.TAKE);
            Long skip = Util.getLongParam(obj, ParamKey.SKIP);
            if (commentId == null  || commentId.isEmpty()) {
                result.code = ErrorCode.WRONG_DATA_FORMAT;
                return result;
            }
//            List<SubComment> respondList = Arrays.asList(new SubComment(), new SubComment(), new SubComment(), new SubComment());
            System.out.println("comment id : " + commentId);
            List<String> lSubCommnet = SubCommentListDAO.getListSubComment(commentId);
            System.out.println("lComment size : " + lSubCommnet.size());
            List<SubComment> lSubCommentDetail = new ArrayList<>();
            if (!lSubCommnet.isEmpty()) {
                lSubCommentDetail = SubCommentDAO.getDetailByIds(lSubCommnet);
            }
            List<SubComment> respondList = new ArrayList<>();
            if (skip < lSubCommentDetail.size()) {
                long startIndex = skip;
                long endIndex = startIndex + take;
                if (endIndex > lSubCommentDetail.size()) {
                    endIndex = lSubCommentDetail.size();
                }
                respondList = lSubCommentDetail.subList((int) startIndex, (int) endIndex);
            }
            result = new ListEntityRespond(ErrorCode.SUCCESS, respondList);
        } catch (EazyException ex) {
            Util.addErrorLog(ex);
            result.code = ex.getErrorCode();
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
        return result;

    }

}
