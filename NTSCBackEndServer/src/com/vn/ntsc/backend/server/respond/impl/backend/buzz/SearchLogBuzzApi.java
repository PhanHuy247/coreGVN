/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.backend.server.respond.impl.backend.buzz;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import eazycommon.util.DateFormat;
import eazycommon.util.Util;
import org.json.simple.JSONObject;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.ParamKey;
import eazycommon.constant.mongokey.BuzzdbKey;
import eazycommon.constant.mongokey.SettingdbKey;
import eazycommon.exception.EazyException;
import com.vn.ntsc.backend.dao.admin.AdminDAO;
import com.vn.ntsc.backend.dao.buzz.BuzzCommentDAO;
import com.vn.ntsc.backend.dao.buzz.BuzzDetailDAO;
import com.vn.ntsc.backend.dao.buzz.CommentDetailDAO;
import com.vn.ntsc.backend.dao.buzz.SubCommentDAO;
import com.vn.ntsc.backend.dao.buzz.SubCommentListDAO;
import com.vn.ntsc.backend.dao.buzz.UserBuzzDAO;
import com.vn.ntsc.backend.dao.user.UserDAO;
import com.vn.ntsc.backend.entity.IEntity;
import com.vn.ntsc.backend.entity.impl.buzz.Buzz;
import com.vn.ntsc.backend.entity.impl.buzz.Comment;
import com.vn.ntsc.backend.entity.impl.buzz.SubComment;
import com.vn.ntsc.backend.entity.impl.datarespond.SizedListData;
import com.vn.ntsc.backend.server.respond.IApiAdapter;
import com.vn.ntsc.backend.server.respond.Respond;
import com.vn.ntsc.backend.server.common.LogProcess;
import com.vn.ntsc.backend.server.respond.result.EntityRespond;

/**
 *
 * @author RuAc0n
 */
public class SearchLogBuzzApi implements IApiAdapter {
   
    @Override
    public Respond execute(JSONObject obj) {
        EntityRespond respond = new EntityRespond();
        try {
            Long userType = Util.getLongParam(obj, ParamKey.USER_TYPE);
            String email = Util.getStringParam(obj, ParamKey.EMAIL);
            if (userType == null) {
                if (email != null) {
                    return null;
                }
            }
            String uId = Util.getStringParam(obj, ParamKey.ID);
            String cmCode = Util.getStringParam(obj, ParamKey.CM_CODE);
            List<String> listId = UserDAO.getListUser(userType, uId, email, cmCode);
            String toTimeStr = Util.getStringParam(obj, ParamKey.TO_TIME);
//            toTime = getToTime(toTime);
            String fromTimeStr = Util.getStringParam(obj, ParamKey.FROM_TIME);
            Long buzzType = Util.getLongParam(obj, ParamKey.BUZZ_TYPE);
            Long toTime = null;
            Long fromTime = null;           
            if(toTimeStr != null)
                toTime = DateFormat.parse(toTimeStr).getTime();
            if(fromTimeStr != null)
                fromTime = DateFormat.parse(fromTimeStr).getTime();            
//            Long sort = Util.getLongParam(obj, ParamKey.SORT);
//            Long order = Util.getLongParam(obj, ParamKey.order);
            Long skip = Util.getLongParam(obj, ParamKey.SKIP);
            Long take = Util.getLongParam(obj, ParamKey.TAKE);
            Long buzzStatus = Util.getLongParam(obj, ParamKey.BUZZ_STATUS);
            Long isDeleted = Util.getLongParam(obj, ParamKey.IS_DELETED);
            Util.addDebugLog("======buzzStatus====="+buzzStatus);
            Long csv = Util.getLongParam(obj, ParamKey.CSV);
            boolean isCsv = false;
            if (csv != null) {
                isCsv = true;
            }
            List<Buzz> listBuzz = UserBuzzDAO.getListBuzz(listId, fromTime, toTime, buzzType, buzzStatus);
            int total = listBuzz.size();
            Collections.sort(listBuzz);            
            if(!isCsv){
                Long buzzNumber = skip + take;            
                if(skip > listBuzz.size()){
                    SizedListData data = new SizedListData(listBuzz.size(), new ArrayList<Buzz>());
                    return new EntityRespond(ErrorCode.SUCCESS, data);
                }
                if (buzzNumber > total) {
                    buzzNumber = new Long(total);
                }
                listBuzz = listBuzz.subList(skip.intValue(), buzzNumber.intValue());
            }
            BuzzDetailDAO.getListBuzz(listBuzz);
            for(Buzz buzz : listBuzz){
                List<String> lCommnet = BuzzCommentDAO.getListComment(buzz.buzzId);
                List<Comment> lCommentDetail = new ArrayList<>();
                if (!lCommnet.isEmpty()) {
                    lCommentDetail = CommentDetailDAO.getListComment(lCommnet);
                }
                buzz.cmtNum  = lCommentDetail.size();
                
                String userDenyID = (String) buzz.toJsonObject().get(BuzzdbKey.BUZZ_DETAIL.USER_DENY);
                Util.addDebugLog("===========SearchLogBuzzApi:"+userDenyID);
//                IEntity userAdmin = null;
//                String userAdminName ="";
                if (userDenyID != null) {
                    buzz.isDenyUserName = userDenyID;
                }
            }
            Collections.sort(listBuzz);
            if(isCsv){
                String api = Util.getStringParam(obj, ParamKey.API_NAME);
                return LogProcess.createCSV(api, csv, listBuzz, new Buzz(), null);
            }
            LogProcess.getOneObjectInfor(listBuzz);
            Map<String, List<Comment>> mCommnet = BuzzCommentDAO.getListComment(listBuzz);
            for (Buzz buzz : listBuzz) {
                List<Comment> lComment = mCommnet.get(buzz.buzzId);
                if(lComment != null && !lComment.isEmpty()){
                    List<Comment> list2First = CommentDetailDAO.get2FirstComment(lComment);
                    List<String> listCommentId = new ArrayList<>();
                    for (Comment cmt : list2First) {
                        listCommentId.add(cmt.userId);
                        if(cmt.subCommentNumber != null && cmt.subCommentNumber > 0){
                            List<String> subCommentIds = SubCommentListDAO.getListSubComment(cmt.cmtId);
                            List<SubComment> subComments = SubCommentDAO.get4CommentByIds(subCommentIds);
                            cmt.subComment = subComments;
                            for(SubComment subComment : subComments){
                                listCommentId.add(subComment.userId);
                            }
                        }
                    }
                    Map<String, String> mCommentName = UserDAO.getUserName(listCommentId);
                    for (Comment cmt : list2First) {
                        String name = mCommentName.get(cmt.userId);
                        cmt.userName = name;
                        if(cmt.subCommentNumber != null && cmt.subCommentNumber > 0){
                            for(SubComment subComment : cmt.subComment){
                                    name = mCommentName.get(subComment.userId);
                                subComment.userName = name;
                            }
                        }
                    }
                    buzz.comment = list2First;
                }
            }
            SizedListData data = new SizedListData(total, listBuzz);
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
