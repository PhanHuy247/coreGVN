/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.buzzserver.server.respond.impl;

import java.util.List;
import eazycommon.exception.EazyException;
import eazycommon.util.Util;
import org.json.simple.JSONObject;
import eazycommon.constant.Constant;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.ParamKey;
import com.vn.ntsc.buzzserver.blacklist.BlackListManager;
import com.vn.ntsc.buzzserver.dao.impl.BuzzDetailDAO;
import com.vn.ntsc.buzzserver.dao.impl.LikeDAO;
import com.vn.ntsc.buzzserver.dao.impl.UserBuzzDAO;
import com.vn.ntsc.buzzserver.dao.impl.UserInteractionDAO;
import com.vn.ntsc.buzzserver.entity.impl.buzz.Buzz;
import com.vn.ntsc.buzzserver.entity.impl.datarespond.LikeData;
import com.vn.ntsc.buzzserver.server.respond.IApiAdapter;
import com.vn.ntsc.buzzserver.server.respond.Respond;
import com.vn.ntsc.buzzserver.server.respond.result.EntityRespond;

/**
 *
 * @author RuAc0n
 */
public class LikeBuzzApi implements IApiAdapter {

    @Override
    public Respond execute(JSONObject obj, long time) {
        Respond respond = new Respond();
        Long likeType = Util.getLongParam(obj, "like_type");
        if (likeType == null) {
            respond.code = ErrorCode.WRONG_DATA_FORMAT;
            return respond;
        }
        if (likeType == Constant.FLAG.ON) {
            respond = likeBuzz(obj, time);
        } else {
            respond = unlikeBuzz(obj);
        }
        return respond;
    }

    private Respond likeBuzz(JSONObject obj, long time) {
        EntityRespond result = new EntityRespond();
        try {
            String liker = Util.getStringParam(obj, ParamKey.USER_ID);
            String buzzId = Util.getStringParam(obj, ParamKey.BUZZ_ID);
            List<String> blockList = Util.getListString(obj, ParamKey.BLOCK_LIST);
//            List<String> deactiveList = Util.getListString(obj, ParamKey.DEACTIVE_LIST);
            if (buzzId == null || buzzId.isEmpty()) {
                result.code = ErrorCode.WRONG_DATA_FORMAT;
                return result;
            }
            boolean check = BuzzDetailDAO.checkBuzzExist(buzzId);
            if (!check) {
                result.code = ErrorCode.BUZZ_NOT_FOUND;
                return result;
            }
            Buzz buzz = BuzzDetailDAO.getBuzzDetail(buzzId);
            String buzzOwner = buzz.userId;
            if (blockList.contains(buzzOwner)) {
                result.code = ErrorCode.BLOCK_USER;
                return result;
            }
            if (BlackListManager.isDeactivateUser(buzzOwner)) {
                result.code = ErrorCode.USER_NOT_EXIST;
                return result;
            }
            if (buzz.isApp == Constant.FLAG.OFF) {
                result.code = ErrorCode.WATTING_APPROVE;
                return result;
            }
            if(buzz.parentId != null && !buzz.parentId.isEmpty()){
                int approvedFlag = UserBuzzDAO.getApprovedFlag(buzz.userId, buzz.parentId);
                if (approvedFlag != Constant.REVIEW_STATUS_FLAG.APPROVED) {
                    result.code = ErrorCode.WATTING_APPROVE;
                    return result;
                }
            }else{
                int approvedFlag = UserBuzzDAO.getApprovedFlag(buzz.userId, buzzId);
                if (approvedFlag != Constant.REVIEW_STATUS_FLAG.APPROVED) {
                    result.code = ErrorCode.WATTING_APPROVE;
                    return result;
                }
            }
            int isNoti = Constant.FLAG.OFF;
            if (!LikeDAO.checkUserLikeExist(buzzId, liker)) {
                isNoti = Constant.FLAG.ON;
            }
            if (!LikeDAO.isLikeBuzz(buzzId, liker)) {
                LikeDAO.addLike(buzzId, liker);
                BuzzDetailDAO.addLike(buzzId);
            }
            UserBuzzDAO.updateBuzzActivity(buzzId, buzzOwner, time, null, null, null, null);
            LikeData data = new LikeData(buzzId, time, buzzOwner, isNoti);
            result = new EntityRespond(ErrorCode.SUCCESS, data);

            //add user interaction
            if (!buzzOwner.equalsIgnoreCase(liker))
                addInteraction(buzzOwner, liker);

        } catch (EazyException ex) {
            Util.addErrorLog(ex);
            result.code = ex.getErrorCode();
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
        return result;
    }

    private Respond unlikeBuzz(JSONObject obj) {
        Respond result = new Respond();
        try {
            String userId = Util.getStringParam(obj, ParamKey.USER_ID);
            String buzzId = Util.getStringParam(obj, ParamKey.BUZZ_ID);
            List<String> blockList = Util.getListString(obj, ParamKey.BLOCK_LIST);
//            List<String> deactiveList = Util.getListString(obj, ParamKey.DEACTIVE_LIST);
            if (buzzId == null || buzzId.isEmpty()) {
                result.code = ErrorCode.WRONG_DATA_FORMAT;
                return result;
            }
            Buzz buzz = BuzzDetailDAO.getBuzzDetail(buzzId);
            String buzzOwner = buzz.userId;
            if (blockList.contains(buzzOwner)) {
                result.code = ErrorCode.BLOCK_USER;
                return result;
            }
            if (BlackListManager.isDeactivateUser(buzzOwner)) {
                result.code = ErrorCode.USER_NOT_EXIST;
                return result;
            }
            if (buzz.isApp == Constant.FLAG.OFF) {
                result.code = ErrorCode.WATTING_APPROVE;
                return result;
            }
            if(buzz.parentId != null && !buzz.parentId.isEmpty()){
                int approvedFlag = UserBuzzDAO.getApprovedFlag(buzz.userId, buzz.parentId);
                if (approvedFlag != Constant.REVIEW_STATUS_FLAG.APPROVED) {
                    result.code = ErrorCode.WATTING_APPROVE;
                    return result;
                }
            }else{
                int approvedFlag = UserBuzzDAO.getApprovedFlag(buzz.userId, buzzId);
                if (approvedFlag != Constant.REVIEW_STATUS_FLAG.APPROVED) {
                    result.code = ErrorCode.WATTING_APPROVE;
                    return result;
                }
            }
            if (LikeDAO.isLikeBuzz(buzzId, userId)) {
                LikeDAO.addLike(buzzId, userId);
                BuzzDetailDAO.removeLike(buzzId);
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
    
    public static void addInteraction(String userId, String friendId) {
        
        boolean isExistUserInteractionList = UserInteractionDAO.checkExistInteractionList(userId);
        boolean isExistUserInteraction = UserInteractionDAO.checkExistInteraction(userId, friendId);

        boolean isExistFriendInteractionList = UserInteractionDAO.checkExistInteractionList(friendId);
        boolean isExistFriendInteraction = UserInteractionDAO.checkExistInteraction(friendId, userId);

        if (isExistUserInteractionList && !isExistUserInteraction) {
            UserInteractionDAO.updateInteraction(userId, friendId);
        } else if (!isExistUserInteractionList) {
            UserInteractionDAO.addInteraction(userId, friendId);
        }

        if (isExistFriendInteractionList && !isExistFriendInteraction) {
            UserInteractionDAO.updateInteraction(friendId, userId);
        } else if (!isExistFriendInteractionList) {
            UserInteractionDAO.addInteraction(friendId, userId);
        }
    }
}
