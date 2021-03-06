/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.backend.server.respond.impl.backend.buzz;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.ParamKey;
import eazycommon.exception.EazyException;
import eazycommon.util.Util;
import org.json.simple.JSONObject;
import com.vn.ntsc.backend.dao.buzz.ReviewingSubCommentDAO;
import com.vn.ntsc.backend.dao.user.UserDAO;
import com.vn.ntsc.backend.entity.IEntity;
import com.vn.ntsc.backend.entity.impl.buzz.ReviewingSubComment;
import com.vn.ntsc.backend.entity.impl.datarespond.SizedListData;
import com.vn.ntsc.backend.server.respond.IApiAdapter;
import com.vn.ntsc.backend.server.respond.Respond;
import com.vn.ntsc.backend.server.respond.result.EntityRespond;

/**
 *
 * @author RuAc0n
 */
public class ListReviewingSubCommentApi implements IApiAdapter {

//    @Override
//    public Respond execute(JSONObject obj) {
//        EntityRespond respond = new EntityRespond();
//        try {
//            String userId = Util.getStringParam(obj, ParamKey.ID);
//            Long order = Util.getLongParam(obj, ParamKey.ORDER);
//            Long sort = Util.getLongParam(obj, ParamKey.SORT);
//            Long skip = Util.getLongParam(obj, ParamKey.SKIP);
//            Long take = Util.getLongParam(obj, ParamKey.TAKE);
////            ReviewingComment reviewingComment = ReviewingCommentDAO.get(commentId);
////            List<String> list = new ArrayList<>();
////            list.add(reviewingComment.userId);
////            Map<String, String> mapName = UserDAO.getUserName(list);
////            String commentor = mapName.get(reviewingComment.userId);
////            reviewingComment.userName = commentor;
//            List<ReviewingSubComment> data = ReviewingSubCommentDAO.getReviewingSubComment(userId, sort, order, skip.intValue(), take.intValue());
//            List<String> list = new ArrayList<>();
//            for (ReviewingSubComment l : data) {
//                list.add(l.userId);
//            }
//            
//            Map<String, String> mapName = UserDAO.getUserName(list);
//            for (ReviewingSubComment l : data) {
//                String userName = mapName.get(l.userId);
//                l.userName = userName;
//            }
////            ReviewingComment reviewingComment = new ReviewingComment();
////            List<ReviewingSubComment> data = new ArrayList<>();
////            for(int i = 0; i < 5; i++){
////                data.add(new ReviewingSubComment());
////            }
//            reviewingComment.subComments = data;
//            respond = new EntityRespond(ErrorCode.SUCCESS, reviewingComment);
//            
//        } catch (EazyException ex) {
//            Util.addErrorLog(ex);
//            respond = new EntityRespond(ex.getErrorCode());
//        } catch (Exception ex) {
//            Util.addErrorLog(ex);
//        }
//        return respond;
//    }
    
    @Override
    public Respond execute(JSONObject obj) {
        EntityRespond respond = new EntityRespond();
        try {
            String userId = Util.getStringParam(obj, ParamKey.ID);
            Long order = Util.getLongParam(obj, ParamKey.ORDER);
            Long sort = Util.getLongParam(obj, ParamKey.SORT);
            Long skip = Util.getLongParam(obj, ParamKey.SKIP);
            Long take = Util.getLongParam(obj, ParamKey.TAKE);
            SizedListData data = ReviewingSubCommentDAO.getReviewingSubComment(userId, sort, order, skip.intValue(), take.intValue());
            List<IEntity> ll = data.ll;
            List<String> list = new ArrayList<>();
            for (IEntity l : ll) {
                list.add(((ReviewingSubComment) l).userId);
            }
            Map<String, String> mapName = UserDAO.getUserName(list);
            for (IEntity l : ll) {
                String userName = mapName.get(((ReviewingSubComment) l).userId);
                ((ReviewingSubComment) l).userName = userName;
            }
            
//            SizedListData data = new SizedListData();
//            int size = 100;
//            data.total = size;
//            List<IEntity> ll = new ArrayList<>();
//            for(int i = 0; i < 50; i++){
//                ll.add(new ReviewingComment());
//            }
            data.ll = ll;
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
