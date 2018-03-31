/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.vn.ntsc.buzzserver.entity.impl.buzz;

import java.util.ArrayList;
import java.util.List;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import com.vn.ntsc.buzzserver.entity.IEntity;

/**
 *
 * @author DuongLTD
 */
public class Buzz implements Comparable<Buzz>, IEntity {
    private static final String userIdKey = "user_id";
    public String userId;

    private static final String userNameKey = "user_name";
    public String userName;

    private static final String avaIdKey = "ava_id";
    public String avaId;

    private static final String genderKey = "gender";
    public Long gender;

    private static final String logitudeKey = "long";
    public Double longitude;

    private static final String latitudeKey = "lat";
    public Double latitude;

    private static final String isLikeKey = "is_like";
    public Integer isLike;

    private static final String buzzTimeKey = "buzz_time";
    public String buzzTime;

//    private static final String seenNumKey = "seen_num";
//    public Long seenNum;

    private static final String likeNumKey = "like_num";
    public Long likeNum;

    private static final String cmtNumKey = "cmt_num";
    public Long cmtNum;

    private static final String buzzIdKey = "buzz_id";
    public String buzzId;

    private static final String buzzValKey = "buzz_val";
    public String buzzVal;

    private static final String buzzTypeKey = "buzz_type";
    public Long buzzType;

    private static final String commentKey = "comment";
    public List<Comment> comment = new ArrayList<>();

    private static final String lastActKey = "lst_act";
    public Long lastAct;

    private static final String isAppKey = "is_app";
    public Integer isApp;
    
    private static final String flagKey = "flag";
    public Integer flag;
    
    private static final String listChildKey = "list_child";
    public List<Buzz> listChild = new ArrayList<>();
    
    private static final String childNumKey = "child_num";
    public Integer childNum;
    
    private static final String fileIdKey = "file_id";
    public String fileId;
    
    private static final String listTagKey = "tag_list";
    public List<Tag> listTag = new ArrayList<>();
    
    private static final String tagNumKey = "tag_num";
    public Integer tagNum;

    private static final String parentIdKey = "parent_id";
    public String parentId;

    private static final String coverIdKey = "cover_id";
    public String coverId;
    
    private static final String privacyKey = "privacy";
    public Integer privacy;
    
    private static final String buzzRegionKey = "buzz_region";
    public Integer buzzRegion;
    
    private static final String streamIdKey = "stream_id";
    public String streamId;
    
    private static final String subCmtNumKey = "sub_cmt_num";
    public Integer subCmtNum;
    
    private static final String viewNumberKey = "view_number";
    public Integer viewNumber;
    
    private static final String curentViewKey = "current_view";
    public Integer curentView;
    
    private static final String durationKey = "duration";
    public Integer duration;
    
    private static final String streamEndTimeKey = "stream_end_time";
    public Long streamEndTime;
    
    private static final String streamStartTimeKey = "stream_start_time";
    public Long streamStartTime;
    
    private static final String streamStatusKey = "stream_status";
    public String streamStatus;
    
    public static final String shareNumberKey = "share_number";
    public Integer shareNumber;
    
    public static final String buzzSpecialKey = "buzz_special";
    public Integer buzzSpecial;
    
    public static final String shareIdKey = "share_id";
    public String shareId;
    
    public static final String shareDetailKey = "share_detail";
    public Buzz shareDetail;
    
    @Override
    public JSONObject toJsonObject(){
        JSONObject jo = new JSONObject();

        if(this.userId != null)
            jo.put(userIdKey, this.userId);
        if(this.userName != null)
            jo.put(userNameKey, this.userName);
        if(this.avaId != null)
            jo.put(avaIdKey, this.avaId);
        if(this.gender != null)
            jo.put(genderKey, this.gender);
        if(this.longitude != null)
            jo.put(logitudeKey, this.longitude);
        if(this.latitude != null)
            jo.put(latitudeKey, this.latitude);
        if(this.isLike != null)
            jo.put(isLikeKey, this.isLike);
        if(this.buzzTime != null)
            jo.put(buzzTimeKey, this.buzzTime);
//        if(this.seenNum != null)
//            jo.put(seenNumKey, this.seenNum);
        if(this.likeNum != null)
            jo.put(likeNumKey, this.likeNum);
        if(cmtNum != null)
            jo.put(cmtNumKey, this.cmtNum);
        if(this.buzzId != null)
            jo.put(buzzIdKey, this.buzzId);
        if(this.buzzVal != null)
            jo.put(buzzValKey, this.buzzVal);
        if(this.buzzType != null)
            jo.put(buzzTypeKey, this.buzzType);
        if(this.lastAct != null)
            jo.put(lastActKey, this.lastAct);

        if(this.comment != null){
            JSONArray arr = new JSONArray();
            for(int i = 0; i < this.comment.size(); i++)
                arr.add(this.comment.get(i).toJsonObject());
            jo.put(commentKey, arr);
        }

        if(this.isApp != null)
            jo.put(isAppKey, this.isApp);
        if(this.listChild != null){
            JSONArray list = new JSONArray();
            for(int i = 0; i < this.listChild.size(); i++){
                list.add(this.listChild.get(i).toJsonObject());
            }
            jo.put(listChildKey, list);
        }
        if(this.childNum != null)
            jo.put(childNumKey, this.childNum);
        if(this.fileId != null)
            jo.put(fileIdKey, this.fileId);
        if(this.listTag != null)
            jo.put(listTagKey, listTag);
        if(this.tagNum != null)
            jo.put(tagNumKey, tagNum);
        if(this.listTag != null){
            JSONArray list = new JSONArray();
            for(int i = 0; i < this.listTag.size(); i++){
                list.add(this.listTag.get(i).toJsonObject());
            }
            jo.put(listTagKey, list);
        } 
        if(this.coverId != null)
            jo.put(coverIdKey, this.coverId);
        if(this.privacy != null)
            jo.put(privacyKey, this.privacy);
        if(this.buzzRegion != null)
            jo.put(buzzRegionKey, this.buzzRegion);
        if(this.streamId != null)
            jo.put(streamIdKey, this.streamId);
        if(this.subCmtNum != null)
            jo.put(subCmtNumKey, this.subCmtNum);
        if(this.viewNumber != null)
            jo.put(viewNumberKey, this.viewNumber);
        if(this.curentView != null)
            jo.put(curentViewKey, this.curentView);
        if(this.duration != null)
            jo.put(durationKey, this.duration);
        if(this.streamEndTime != null)
            jo.put(streamEndTimeKey, this.streamEndTime);
        if(this.streamStartTime != null)
            jo.put(streamStartTimeKey, this.streamStartTime);
        if(this.streamStatus != null)
            jo.put(streamStatusKey, this.streamStatus);
        if(this.shareNumber != null)
            jo.put(shareNumberKey, this.shareNumber);
        if(this.buzzSpecial != null)
            jo.put(buzzSpecialKey, this.buzzSpecial);
        if(this.shareId != null)
            jo.put(shareIdKey, this.shareId);
        if(this.shareDetail != null)
            jo.put(shareDetailKey, this.shareDetail.toJsonObject());
        if(this.flag != null)
            jo.put(flagKey, this.flag);
        return jo;
    }
    
    @Override
    public int compareTo(Buzz obj) {
        if(this.lastAct > obj.lastAct)
            return -1;
        else if(this.lastAct < obj.lastAct)
            return 1;
        else
            return 0;
    }
    
}
