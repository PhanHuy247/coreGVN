/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.buzzserver.server.respond.impl;

import com.mongodb.DBObject;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import eazycommon.constant.API;
import eazycommon.constant.Constant;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.ParamKey;
import eazycommon.constant.mongokey.UserdbKey;
import eazycommon.exception.EazyException;
import eazycommon.inspection.version.InspectionVersionDAO;
import eazycommon.util.Util;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import com.vn.ntsc.buzzserver.blacklist.BlackListManager;
import com.vn.ntsc.buzzserver.dao.impl.BuzzCommentDAO;
import com.vn.ntsc.buzzserver.dao.impl.BuzzDetailDAO;
import com.vn.ntsc.buzzserver.dao.impl.BuzzTagDAO;
import com.vn.ntsc.buzzserver.dao.impl.BuzzViewDAO;
import com.vn.ntsc.buzzserver.dao.impl.CommentDAO;
import com.vn.ntsc.buzzserver.dao.impl.LikeDAO;
import com.vn.ntsc.buzzserver.dao.impl.PrioritizeUserBuzzDAO;
import com.vn.ntsc.buzzserver.dao.impl.SubCommentDAO;
import com.vn.ntsc.buzzserver.dao.impl.SubCommentListDAO;
import com.vn.ntsc.buzzserver.dao.impl.UserBuzzDAO;
import com.vn.ntsc.buzzserver.dao.impl.UserDAO;
import com.vn.ntsc.buzzserver.entity.impl.buzz.Buzz;
import com.vn.ntsc.buzzserver.entity.impl.buzz.Comment;
import com.vn.ntsc.buzzserver.entity.impl.buzz.PrioritizeUserBuzzSetting;
import com.vn.ntsc.buzzserver.entity.impl.buzz.SubComment;
import com.vn.ntsc.buzzserver.entity.impl.buzz.Tag;
import com.vn.ntsc.buzzserver.entity.impl.buzz.User;
import com.vn.ntsc.buzzserver.server.respond.IApiAdapter;
import com.vn.ntsc.buzzserver.server.respond.Respond;
import com.vn.ntsc.buzzserver.server.respond.common.Helper;
import com.vn.ntsc.buzzserver.server.respond.result.EntityRespond;
import com.vn.ntsc.buzzserver.server.respond.result.ListEntityRespond;
import java.util.Comparator;

/**
 *
 * @author RuAc0n
 */
public class GetBuzzApi implements IApiAdapter {

    private static final int USER_BUZZ = 0;
    private static final int LOCAL_BUZZ = 1;
    private static final int FRIEND_BUZZ = 2;
    private static final int FAVOURIST_BUZZ = 3;
    private static final int STREAM_BUZZ = 4;
    private static final int NOT_STREAM_BUZZ = 5;
    private Long buzzKind ;

    @Override
    public Respond execute(JSONObject obj, long time) {
        Respond result = new ListEntityRespond();
        try {
            buzzKind = Util.getLongParam(obj, "buzz_kind");
            String userId = Util.getStringParam(obj, ParamKey.USER_ID);
            String req_userId = Util.getStringParam(obj, ParamKey.REQUEST_USER_ID);
            if (buzzKind == null) {
                result.code = ErrorCode.WRONG_DATA_FORMAT;
                return result;
            }
            
            if (buzzKind == LOCAL_BUZZ && userId != null) {
                result = getLocalBuzz(obj);
            }
            else if (buzzKind == USER_BUZZ && userId != null) {
                result = getUserBuzz(obj);
            }
            else if ((buzzKind == FRIEND_BUZZ || buzzKind == FAVOURIST_BUZZ) && userId != null){
                result = getFriendBuzz(obj);
            }
            else if (buzzKind == LOCAL_BUZZ && userId == null) {
                result = getAllBuzz(obj);
            }
            else if (buzzKind == USER_BUZZ && req_userId !=null ) {
                result = getUserBuzz(obj);
            }
            else if (buzzKind == STREAM_BUZZ) {
                result = getStreamBuzz(obj);
            }
            else if (buzzKind == NOT_STREAM_BUZZ){
                result = getImageVideoAudioBuzz(obj);
            }
            else {
                result = new EntityRespond(ErrorCode.INVALID_TOKEN);
            } 
            
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
        return result;
    }

    private ListEntityRespond getLocalBuzz(JSONObject obj) {
        ListEntityRespond result = new ListEntityRespond();
        try {
            String userId = Util.getStringParam(obj, ParamKey.USER_ID);
            
            Long skip = Util.getLongParam(obj, ParamKey.SKIP);
            Long take = Util.getLongParam(obj, ParamKey.TAKE);
            
            long buzzNumber = skip + take;
            Map<String, Buzz> mBuzz = new TreeMap<>();
            List<String> blockList = Util.getListString(obj, ParamKey.BLOCK_LIST);
            List<String> friendList = Util.getListString(obj, ParamKey.FRIEND_LIST);
            
            List<String> listUserPrioritize = getListPrioritizeUser();
            if(listUserPrioritize != null && !listUserPrioritize.isEmpty()){
                friendList.addAll(listUserPrioritize);
            }
            
            List<Buzz> lBuzzId = UserBuzzDAO.getListAllBuzz(friendList, userId, blockList);
//            List<Buzz> lBuzzId = UserBuzzDAO.getListLocalBuzz(listUser, userId);
            if (skip < lBuzzId.size()) {
                if (buzzNumber > lBuzzId.size()) {
                    buzzNumber = lBuzzId.size();
                }
//                Collections.sort(lBuzzId);
                lBuzzId = collectionBuzz(lBuzzId);
                lBuzzId = lBuzzId.subList(skip.intValue(), (int) buzzNumber);
                mBuzz = BuzzDetailDAO.getListBuzz(lBuzzId, userId);
                LikeDAO.getLikeBuzz(mBuzz, userId);
                mBuzz = mapData(mBuzz, lBuzzId, userId, blockList);
            }
            Collection<Buzz> cBuzz = mBuzz.values();
            Iterator<Buzz> it = cBuzz.iterator();
            List<Buzz> lBuzz = new ArrayList<>();
            while (it.hasNext()) {
                Buzz buzz = it.next();
                lBuzz.add(buzz);
            }
//            Collections.sort(lBuzz);

            lBuzz = collectionBuzz(lBuzz);
            
            result = new ListEntityRespond(ErrorCode.SUCCESS, lBuzz);
        } catch (EazyException ex) {
            Util.addErrorLog(ex);
            result.code = ex.getErrorCode();
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
        return result;
    }

    private ListEntityRespond getFriendBuzz(JSONObject obj) {
        ListEntityRespond result = new ListEntityRespond();
        try {
            String userId = Util.getStringParam(obj, ParamKey.USER_ID);
            
            Long skip = Util.getLongParam(obj, ParamKey.SKIP);
            Long take = Util.getLongParam(obj, ParamKey.TAKE);
            long buzzNumber = skip + take;
            Map<String, Buzz> mBuzz = new TreeMap<>();
//            List<String> listUser = Util.getListString(obj, ParamKey.LIST_USER);
            List<String> listUser = Util.getListString(obj, ParamKey.FRIEND_LIST);
            List<String> blockList = Util.getListString(obj, ParamKey.BLOCK_LIST);
//            List<String> deactiveList = Util.getListString(obj, ParamKey.DEACTIVE_LIST);
//            List<String> blackList = new ArrayList<>();
//            blackList.addAll(blockList);
//            blackList.addAll(deactiveList);
            Helper.removeBlockUserId(listUser, blockList);
            if (!listUser.isEmpty()) {
                List<Buzz> lBuzzId = UserBuzzDAO.getListBuzz(listUser);
                if (!lBuzzId.isEmpty()) {
                    if (skip < lBuzzId.size()) {
                        if (buzzNumber > lBuzzId.size()) {
                            buzzNumber = lBuzzId.size();
                        }
//                        Collections.sort(lBuzzId);
                       lBuzzId = collectionBuzz(lBuzzId);
                        lBuzzId = lBuzzId.subList(skip.intValue(), (int) buzzNumber);
                        mBuzz = BuzzDetailDAO.getListBuzz(lBuzzId, userId);
                        LikeDAO.getLikeBuzz(mBuzz, userId);
                        mBuzz = mapData(mBuzz, lBuzzId, userId, blockList);
                    }
                }
            }
            Collection<Buzz> cBuzz = mBuzz.values();
            Iterator<Buzz> it = cBuzz.iterator();
            List<Buzz> lBuzz = new ArrayList<>();
            while (it.hasNext()) {
                Buzz buzz = it.next();
                lBuzz.add(buzz);
            }
            
            lBuzz = collectionBuzz(lBuzz);
            result = new ListEntityRespond(ErrorCode.SUCCESS, lBuzz);
        } catch (EazyException ex) {
            Util.addErrorLog(ex);
            result.code = ex.getErrorCode();
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
        return result;
    }

    private ListEntityRespond getUserBuzz(JSONObject obj) {
        ListEntityRespond result = new ListEntityRespond();
        try {
            Util.addDebugLog("=======getUserBuzz==========");
            String userId = Util.getStringParam(obj, ParamKey.USER_ID);
            String friendId = Util.getStringParam(obj, ParamKey.REQUEST_USER_ID);
            
            Long skip = Util.getLongParam(obj, ParamKey.SKIP);
            Long take = Util.getLongParam(obj, ParamKey.TAKE);
            List<String> blockList = Util.getListString(obj, ParamKey.BLOCK_LIST);
            List<String> friendList = Util.getListString(obj, ParamKey.FRIEND_LIST);
//            List<String> deactiveList = Util.getListString(obj, ParamKey.DEACTIVE_LIST);
//            List<String> blackList = new ArrayList<>();
//            blackList.addAll(blockList);
//            blackList.addAll(deactiveList);
            long buzzNumber = skip + take;
            Map<String, Buzz> mBuzz = new TreeMap<>();
            List<Buzz> lBuzzId;
            List<String> lTaggedBuzzId;
            if (friendId != null && !friendId.equals(userId)) {
                if (!BlackListManager.isDeactivateUser(friendId)) {
                    if (!blockList.contains(friendId)) {
                        lBuzzId = UserBuzzDAO.getListBuzz(friendId, false, friendList);
                        lTaggedBuzzId = BuzzTagDAO.getListBuzzTaggedByUserId(friendId);
                    } else {
                        result.code = ErrorCode.BLOCK_USER;
                        return result;
                    }
                } else {
                    result.code = ErrorCode.USER_NOT_EXIST;
                    return result;
                }
            } else {
                lBuzzId = UserBuzzDAO.getListBuzz(userId, true, friendList);
                lTaggedBuzzId = BuzzTagDAO.getListBuzzTaggedByUserId(userId);
            }
            
            //get tagged buzz
            if (lTaggedBuzzId != null && !lTaggedBuzzId.isEmpty() && userId != null && !userId.isEmpty()){
//                Util.addDebugLog("------------- lst id size: "+lTaggedBuzzId);
                List<Buzz> lTaggedBuzz = BuzzDetailDAO.getBuzzByListId(lTaggedBuzzId, userId, friendList);
//                Util.addDebugLog("------------- lst tagged buzz: "+lTaggedBuzz);
                lBuzzId.addAll(lTaggedBuzz);
            }
            //end
            if (!lBuzzId.isEmpty()) {
                if (skip < lBuzzId.size()) {
                    if (buzzNumber > lBuzzId.size()) {
                        buzzNumber = lBuzzId.size();
                    }
//                    Collections.sort(lBuzzId);
                    lBuzzId = collectionBuzz(lBuzzId);
                    lBuzzId = lBuzzId.subList(skip.intValue(), (int) buzzNumber);
                    mBuzz = BuzzDetailDAO.getListBuzz(lBuzzId, friendId);
                    LikeDAO.getLikeBuzz(mBuzz, userId);
                    mBuzz = mapData(mBuzz, lBuzzId, userId, blockList);
                }
            }
            Collection<Buzz> cBuzz = mBuzz.values();
            Iterator<Buzz> it = cBuzz.iterator();
            List<Buzz> lBuzz = new ArrayList<>();
            while (it.hasNext()) {
                Buzz buzz = it.next();
                lBuzz.add(buzz);
            }
//            Collections.sort(lBuzz);
           
            lBuzz = collectionBuzz(lBuzz);
            
            result = new ListEntityRespond(ErrorCode.SUCCESS, lBuzz);
        } catch (EazyException ex) {
            Util.addErrorLog(ex);
            result.code = ex.getErrorCode();
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
        return result;

    }
        
    private ListEntityRespond getAllBuzz(JSONObject obj) {
        ListEntityRespond result = new ListEntityRespond();
        try {
            Long skip = Util.getLongParam(obj, ParamKey.SKIP);
            Long take = Util.getLongParam(obj, ParamKey.TAKE);
           
            int max = Constant.MAX_USER_LOCAL_BUZZ;
            boolean isExit = false;
            long buzzNumber = skip + take;
            Map<String, Buzz> mBuzz = new TreeMap<>();
            
            List<String> listUser = Util.getListString(obj, ParamKey.LIST_USER);
            List<String> blockList = Util.getListString(obj, ParamKey.BLOCK_LIST);
            
            List<String> listUserPrioritize = getListPrioritizeUser();
            if(listUserPrioritize != null && !listUserPrioritize.isEmpty()){
                listUser.addAll(listUserPrioritize);
            }
            
            List<Buzz> lBuzzId = UserBuzzDAO.getListLocalBuzz(listUser);
                
            if (skip < lBuzzId.size()) {
                if (buzzNumber > lBuzzId.size()) {
                    buzzNumber = lBuzzId.size();
                }
//                        Collections.sort(lBuzzId);
                lBuzzId = collectionBuzz(lBuzzId);
                lBuzzId = lBuzzId.subList(skip.intValue(), (int) buzzNumber);
                mBuzz = BuzzDetailDAO.getListBuzz(lBuzzId, null);
                Map<String, List<Comment>> mCommnet = BuzzCommentDAO.getListComment(lBuzzId, null);
                for (Map.Entry pairs : mCommnet.entrySet()) {
                    String buzzId = (String) pairs.getKey();
                    Buzz bu = mBuzz.get(buzzId);
                    if (bu != null) {
                        List<Comment> lCommentId = (List<Comment>) pairs.getValue();
                        //get buzz comment number
                        long commentNumber = 0;
                        for (Comment comment : lCommentId) {
                            String commenter = comment.userId;
                            if (!BlackListManager.isDeactivateUser(commenter) && !blockList.contains(commenter)) {
                                commentNumber++;
                            }
                        }
                        bu.cmtNum = commentNumber;
                        List<Comment> lCommentDetail = CommentDAO.get4FirstComment(lCommentId, blockList);
                        for (Comment comment : lCommentDetail) {
                            List<String> subCommentIds = SubCommentListDAO.getListSubComment(comment.cmtId, null);
                            List<SubComment> lSubCommentDetail = SubCommentDAO.getDetailByIds(subCommentIds, blockList);
                            comment.subCommentNumber = lSubCommentDetail.size();
                        }
                        bu.comment = lCommentDetail;
                        mBuzz.put(buzzId, bu);
                    }
                }
                Map<String, List<Buzz>> mChildBuzz = BuzzDetailDAO.getListChildBuzz(lBuzzId, null);
                for (Map.Entry pairs : mChildBuzz.entrySet()) {
                    String buzzId = (String) pairs.getKey();
                    Buzz bu = mBuzz.get(buzzId);
                    if (bu != null) {
                        List<Buzz> listBuzz = (List<Buzz>) pairs.getValue();
                        
                        if(!listBuzz.isEmpty()){
                            Map<String, Integer> mViewBuzz = BuzzViewDAO.getListView(listBuzz);
                            for (Buzz item: listBuzz){
                                Integer viewNum = mViewBuzz.get(item.buzzId);
                                if(item.viewNumber != null){
                                    item.viewNumber = item.viewNumber + viewNum;
                                }else{
                                    item.viewNumber = viewNum;
                                }
                            }
                        }
                        
                        bu.listChild = listBuzz;
                        bu.childNum = listBuzz.size();
                        mBuzz.put(buzzId, bu);
                    }
                }    
                Map<String, List<Tag>> mTagBuzz = BuzzTagDAO.getListTag(lBuzzId);

                List<String> tagUserList = new ArrayList<>();
                Iterator it = mTagBuzz.entrySet().iterator();
                while(it.hasNext()){
                    Map.Entry pair = (Map.Entry)it.next();
                    List<Tag> temp = (List<Tag>) pair.getValue();
                    for(Tag item : temp){
                        tagUserList.add(item.userId);
                    }
                }
                Map<String, String> mUserName = UserDAO.getListUserName(tagUserList);
                for (Map.Entry pairs : mTagBuzz.entrySet()) {
                    String buzzId = (String) pairs.getKey();
                    Buzz bu = mBuzz.get(buzzId);
                    if (bu != null) {
                        List<Tag> listTag = (List<Tag>) pairs.getValue();
                        for(Tag item: listTag){
                            item.userName = mUserName.get(item.userId);
                        }
                        bu.listTag = listTag;
                        bu.tagNum = listTag.size();
                        mBuzz.put(buzzId, bu);
                    }
                }

                Map<String, Integer> mSubCommentNum = SubCommentDAO.getListSubCommentNum(lBuzzId, blockList, mCommnet);
                for (Map.Entry pairs : mSubCommentNum.entrySet()) {
                    String buzzId = (String) pairs.getKey();
                    Buzz bu = mBuzz.get(buzzId);
                    if (bu != null) {
                        Integer temp = (Integer) pairs.getValue();
                        bu.subCmtNum = temp;
                        mBuzz.put(buzzId, bu);
                    }
                }
                
                for(Buzz item: lBuzzId){
                    Buzz bu = mBuzz.get(item.buzzId);
                    if(bu != null && bu.shareDetail != null){
                        String buzzId = bu.shareDetail.buzzId;

                        List<Comment> lCommentId = BuzzCommentDAO.getListShareComment(buzzId, null);
                        long commentNumber = 0;
                        for (Comment comment : lCommentId) {
                            String commenter = comment.userId;
                            if (!BlackListManager.isDeactivateUser(commenter) && !blockList.contains(commenter)) {
                                commentNumber++;
                            }
                        }
                        bu.shareDetail.cmtNum = commentNumber;

                        List<String> lcComment = BuzzCommentDAO.getListComment(buzzId, null);
                        Integer num = SubCommentDAO.getSubCommentNumber(buzzId, blockList, lcComment);
                        bu.shareDetail.subCmtNum = num;
                        
                        List<Tag> tagShareBuzz = BuzzTagDAO.getTag(buzzId);
                        List<String> tagShareUserList = new ArrayList<>();
                        for(Tag tagData : tagShareBuzz){
                            tagShareUserList.add(tagData.userId);
                        }
                        Map<String, String> mShareUserName = UserDAO.getListUserName(tagShareUserList);
                        for(Tag tagData: tagShareBuzz){
                            item.userName = mShareUserName.get(tagData.userId);
                        }
                        bu.shareDetail.listTag = tagShareBuzz;
                        bu.shareDetail.tagNum = tagShareBuzz.size();
                    }
                }
                
                Map<String, Integer> mViewBuzz = BuzzViewDAO.getListView(lBuzzId);
                for (Map.Entry pairs : mViewBuzz.entrySet()) {
                    String buzzId = (String) pairs.getKey();
                    Buzz bu = mBuzz.get(buzzId);
                    if (bu != null) {
                        Integer count = (Integer) pairs.getValue();
                        if(bu.viewNumber != null){
                            bu.viewNumber = bu.viewNumber + count;
                        }else{
                            bu.viewNumber = count;
                        }
                        mBuzz.put(buzzId, bu);
                    }
                }
            }
            
            Collection<Buzz> cBuzz = mBuzz.values();
            Iterator<Buzz> it = cBuzz.iterator();
            List<Buzz> lBuzz = new ArrayList<>();
            while (it.hasNext()) {
                Buzz buzz = it.next();
                lBuzz.add(buzz);
            }
//            Collections.sort(lBuzz);
           
            lBuzz = collectionBuzz(lBuzz);
            
            result = new ListEntityRespond(ErrorCode.SUCCESS, lBuzz);
        } catch (EazyException ex) {
            Util.addErrorLog(ex);
            result.code = ex.getErrorCode();
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
        return result;
    }
    
    private ListEntityRespond getStreamBuzz (JSONObject obj){
        ListEntityRespond result = new ListEntityRespond();
        try{
            String userId = Util.getStringParam(obj, ParamKey.USER_ID);
            Boolean isFavorist = (Boolean) obj.get(ParamKey.IS_FAV);
           
            Long skip = Util.getLongParam(obj, ParamKey.SKIP);
            Long take = Util.getLongParam(obj, ParamKey.TAKE);
            
            List<String> blockList = Util.getListString(obj, ParamKey.BLOCK_LIST);
            List<String> friendList = Util.getListString(obj, ParamKey.FRIEND_LIST);
            
            List<Buzz> lBuzzId = UserBuzzDAO.getListStreamBuzz(friendList, userId, isFavorist);
            
            Map<String, Buzz> mBuzz = new TreeMap<>();
            long buzzNumber = skip + take;
            if (skip < lBuzzId.size()) {
                if (buzzNumber > lBuzzId.size()) {
                    buzzNumber = lBuzzId.size();
                }
                lBuzzId = collectionBuzz(lBuzzId);
                lBuzzId = lBuzzId.subList(skip.intValue(), (int) buzzNumber);
                mBuzz = BuzzDetailDAO.getListStreamBuzz(lBuzzId, userId);
                LikeDAO.getLikeBuzz(mBuzz, userId);
                
                mBuzz = mapData(mBuzz, lBuzzId, userId, blockList);
            }
            Collection<Buzz> cBuzz = mBuzz.values();
            Iterator<Buzz> it = cBuzz.iterator();
            List<Buzz> lBuzz = new ArrayList<>();
            while (it.hasNext()) {
                Buzz buzz = it.next();
                lBuzz.add(buzz);
            }
           
            lBuzz = collectionBuzz(lBuzz);
            
            result = new ListEntityRespond(ErrorCode.SUCCESS, lBuzz);
        }catch (Exception ex){
            Util.addErrorLog(ex);
        }
        return result;
    }
    
    private ListEntityRespond getImageVideoAudioBuzz(JSONObject obj){
        ListEntityRespond result = new ListEntityRespond();
        try{
            String userId = Util.getStringParam(obj, ParamKey.USER_ID);
            String friendId = Util.getStringParam(obj, ParamKey.REQUEST_USER_ID);
           
            Long skip = Util.getLongParam(obj, ParamKey.SKIP);
            Long take = Util.getLongParam(obj, ParamKey.TAKE);
            
            List<String> blockList = Util.getListString(obj, ParamKey.BLOCK_LIST);
            List<String> friendList = Util.getListString(obj, ParamKey.FRIEND_LIST);
            
            List<Buzz> lBuzzId = new ArrayList<>();
            
            if(friendId != null){
                lBuzzId = UserBuzzDAO.getImageVideoAudioBuzz(friendList, friendId);
            }else{
                lBuzzId = UserBuzzDAO.getImageVideoAudioBuzz(friendList, userId);
            }
            
            Map<String, Buzz> mBuzz = new TreeMap<>();
            long buzzNumber = skip + take;
            if (skip < lBuzzId.size()) {
                if (buzzNumber > lBuzzId.size()) {
                    buzzNumber = lBuzzId.size();
                }
                lBuzzId = collectionBuzz(lBuzzId);
                lBuzzId = lBuzzId.subList(skip.intValue(), (int) buzzNumber);
                mBuzz = BuzzDetailDAO.getListBuzz(lBuzzId, userId);
                LikeDAO.getLikeBuzz(mBuzz, userId);
                
                mBuzz = mapData(mBuzz, lBuzzId, userId, blockList);
            }
            Collection<Buzz> cBuzz = mBuzz.values();
            Iterator<Buzz> it = cBuzz.iterator();
            List<Buzz> lBuzz = new ArrayList<>();
            while (it.hasNext()) {
                Buzz buzz = it.next();
                lBuzz.add(buzz);
            }
           
            lBuzz = collectionBuzz(lBuzz);
            
            result = new ListEntityRespond(ErrorCode.SUCCESS, lBuzz);
        }catch (Exception ex){
            Util.addErrorLog(ex);
        }
        return result;
    }
    
    private Map<String, Buzz> mapData(Map<String, Buzz> mBuzz, List<Buzz> lBuzzId, String userId, List<String> blockList) throws EazyException{
        Map<String, List<Comment>> mCommnet = BuzzCommentDAO.getListComment(lBuzzId, userId);
        for (Map.Entry pairs : mCommnet.entrySet()) {
            String buzzId = (String) pairs.getKey();
            Buzz bu = mBuzz.get(buzzId);
            if (bu != null) {
                List<Comment> lCommentId = (List<Comment>) pairs.getValue();
                //get buzz comment number
                long commentNumber = 0;
                for (Comment comment : lCommentId) {
                    String commenter = comment.userId;
                    if (!BlackListManager.isDeactivateUser(commenter) && !blockList.contains(commenter)) {
                        commentNumber++;
                    }
                }
                bu.cmtNum = commentNumber;
                List<Comment> lCommentDetail = CommentDAO.get4FirstComment(lCommentId, blockList);
                for (Comment comment : lCommentDetail) {
                    List<String> subCommentIds = SubCommentListDAO.getListSubComment(comment.cmtId, userId);
                    List<SubComment> lSubCommentDetail = SubCommentDAO.getDetailByIds(subCommentIds, blockList);
                    comment.subCommentNumber = lSubCommentDetail.size();
                    comment.isDel = comment.isDel == Constant.FLAG.ON
                            && (comment.userId.equals(userId) || bu.userId.equals(userId)) ? Constant.FLAG.ON : Constant.FLAG.OFF;
                }
                bu.comment = lCommentDetail;
                mBuzz.put(buzzId, bu);
            }
        }
        Map<String, List<Buzz>> mChildBuzz = BuzzDetailDAO.getListChildBuzz(lBuzzId, userId);
        for (Map.Entry pairs : mChildBuzz.entrySet()) {
            String buzzId = (String) pairs.getKey();
            Buzz bu = mBuzz.get(buzzId);
            if (bu != null) {
                List<Buzz> listBuzz = (List<Buzz>) pairs.getValue();
                Map<String, Integer> mLike = LikeDAO.getLikeValue(listBuzz, userId);
                Map<String, Integer> mViewBuzz = BuzzViewDAO.getListView(listBuzz);
                for(Buzz item: listBuzz){
                    Integer isLike = mLike.get(item.buzzId);
                    if(isLike != null){
                        item.isLike = isLike;
                    }else{
                        item.isLike = Constant.FLAG.OFF;
                    }
                    
                    Integer viewNum = mViewBuzz.get(item.buzzId);
                    if(item.viewNumber != null){
                        item.viewNumber = item.viewNumber + viewNum;
                    }else{
                        item.viewNumber = viewNum;
                    }
                }
                bu.listChild = listBuzz;
                bu.childNum = listBuzz.size();
                
                if(bu.childNum > 0){
                    for(Buzz item: bu.listChild){
                        List<String> lcComment = BuzzCommentDAO.getListComment(item.buzzId, userId);
                        Integer num = SubCommentDAO.getSubCommentNumber(item.buzzId, blockList, lcComment);
                        item.subCmtNum = num;
                    }
                }
                mBuzz.put(buzzId, bu);
            }
        }
        Map<String, List<Tag>> mTagBuzz = BuzzTagDAO.getListTag(lBuzzId);

        List<String> tagUserList = new ArrayList<>();
        Iterator it = mTagBuzz.entrySet().iterator();
        while(it.hasNext()){
            Map.Entry pair = (Map.Entry)it.next();
            List<Tag> temp = (List<Tag>) pair.getValue();
            for(Tag item : temp){
                tagUserList.add(item.userId);
            }
        }
        Map<String, String> mUserName = UserDAO.getListUserName(tagUserList);
        for (Map.Entry pairs : mTagBuzz.entrySet()) {
            String buzzId = (String) pairs.getKey();
            Buzz bu = mBuzz.get(buzzId);
            if (bu != null) {
                List<Tag> listTag = (List<Tag>) pairs.getValue();
                for(Tag item: listTag){
                    item.userName = mUserName.get(item.userId);
                }
                bu.listTag = listTag;
                bu.tagNum = listTag.size();
                mBuzz.put(buzzId, bu);
            }
        }

        Map<String, Integer> mSubCommentNum = SubCommentDAO.getListSubCommentNum(lBuzzId, blockList, mCommnet);
        for (Map.Entry pairs : mSubCommentNum.entrySet()) {
            String buzzId = (String) pairs.getKey();
            Buzz bu = mBuzz.get(buzzId);
            if (bu != null) {
                Integer temp = (Integer) pairs.getValue();
                bu.subCmtNum = temp;
                mBuzz.put(buzzId, bu);
            }
        }
        
        for(Buzz item: lBuzzId){
            Buzz bu = mBuzz.get(item.buzzId);
            if(bu != null && bu.shareDetail != null){
                String buzzId = bu.shareDetail.buzzId;
                
                List<Comment> lCommentId = BuzzCommentDAO.getListShareComment(buzzId, userId);
                long commentNumber = 0;
                for (Comment comment : lCommentId) {
                    String commenter = comment.userId;
                    if (!BlackListManager.isDeactivateUser(commenter) && !blockList.contains(commenter)) {
                        commentNumber++;
                    }
                }
                bu.shareDetail.cmtNum = commentNumber;
                
                List<String> lcComment = BuzzCommentDAO.getListComment(buzzId, userId);
                Integer num = SubCommentDAO.getSubCommentNumber(buzzId, blockList, lcComment);
                bu.shareDetail.subCmtNum = num;
                
                List<Tag> tagShareBuzz = BuzzTagDAO.getTag(buzzId);
                List<String> tagShareUserList = new ArrayList<>();
                for(Tag tagData : tagShareBuzz){
                    tagShareUserList.add(tagData.userId);
                }
                Map<String, String> mShareUserName = UserDAO.getListUserName(tagShareUserList);
                for(Tag tagData: tagShareBuzz){
                    tagData.userName = mShareUserName.get(tagData.userId);
                }
                bu.shareDetail.listTag = tagShareBuzz;
                bu.shareDetail.tagNum = tagShareBuzz.size();
            }
        }
        
        Map<String, Integer> mViewBuzz = BuzzViewDAO.getListView(lBuzzId);
        for (Map.Entry pairs : mViewBuzz.entrySet()) {
            String buzzId = (String) pairs.getKey();
            Buzz bu = mBuzz.get(buzzId);
            if (bu != null) {
                Integer count = (Integer) pairs.getValue();
                if(bu.viewNumber != null){
                    bu.viewNumber = bu.viewNumber + count;
                }else{
                    bu.viewNumber = count;
                }
                mBuzz.put(buzzId, bu);
            }
        }
        
        return mBuzz;
    }
    
    private boolean isSafeUser(String userId, String safaryVersion) {
        try {
            DBObject dbObject = UserDAO.getUserInforJSON(userId);

            if (dbObject == null) {
                Util.addDebugLog("GET BUZZ isSafeUser FALSE ");
                return false;
            }
            Util.addDebugLog("GET BUZZ isSafeUser " + dbObject.toString());
            String appVersion = (String) dbObject.get(UserdbKey.USER.APP_VERSION);
            Integer safetyUser = (Integer) dbObject.get(UserdbKey.USER.SAFE_USER);
            Util.addDebugLog("GET BUZZ isSafeUser safetyUser " + safetyUser);
            if (safetyUser != null) {
                if (safetyUser == 1) {
                    Util.addDebugLog("GET BUZZ isSafeUser TRUE " + safetyUser);
                    return true;
                }
            }
            if (appVersion.equals(safaryVersion)) {
                return true;
            }

        } catch (EazyException ex) {
            Logger.getLogger(GetBuzzApi.class.getName()).log(Level.SEVERE, null, ex);
        }
        Util.addDebugLog("GET BUZZ isSafeUser FALSE ");
        return false;
    }
    
    public List<Buzz> collectionBuzz(List<Buzz> listBuzz) throws EazyException{
        List<Buzz> listFinal = new ArrayList();

        Collections.sort(listBuzz,new Comparator<Buzz>(){
            public int compare(Buzz b1,Buzz b2){
                long a = b1.lastAct;
                long b = b2.lastAct;
                if(a > b ){
                    return -1;
                } else if (a < b){
                    return 1;
                }
                return 0;
        }});
        
        if(buzzKind == LOCAL_BUZZ){
            List<Buzz> listPrioritizeUser = new ArrayList();
            List<Buzz> listNomalBuzz = new ArrayList();

//                if(prioritizeUser.listUserId != null && !prioritizeUser.listUserId.isEmpty()){
//                    listPrioritizeUser = UserBuzzDAO.getListLocalBuzz(prioritizeUser.listUserId);
//                }else{
//                    return listBuzz;
//                }
            List<String> listPrioritize = getListPrioritizeBuzzId();
            if(listPrioritize == null || listPrioritize.isEmpty()){
                listPrioritize = getListPrioritizeUser();
            }
            for (Buzz item : listBuzz) {
                if (listPrioritize != null && !listPrioritize.isEmpty() && (!listPrioritize.contains(item.userId) && !listPrioritize.contains(item.buzzId))) {
                    listNomalBuzz.add(item);
                } else {
                    item.buzzSpecial = 1;
                    listPrioritizeUser.add(item);
                }
            }
            listFinal.addAll(listPrioritizeUser);
            listFinal.addAll(listNomalBuzz);
        }else{
            return listBuzz;
        }
        return listFinal;
    }
    
    public List<String> getListPrioritizeUser() throws EazyException{
        PrioritizeUserBuzzSetting prioritizeUser = PrioritizeUserBuzzDAO.getPrioritizeSetting();
        return prioritizeUser.listUserId;
    }
    
     public List<String> getListPrioritizeBuzzId() throws EazyException{
        PrioritizeUserBuzzSetting prioritizeUser = PrioritizeUserBuzzDAO.getPrioritizeSetting();
        return prioritizeUser.listBuzzId;
    }
}
