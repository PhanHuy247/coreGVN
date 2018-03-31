/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.usermanagementserver.entity.impl.user;

import org.json.simple.JSONObject;
import com.vn.ntsc.usermanagementserver.entity.IEntity;

/**
 *
 * @author Administrator
 */
public class Memo implements Comparable<User>, IEntity {

    private static final String memoIdKey = "_id";
    public String memoId;

    private static final String friendIdKey = "friend_id";
    public String friendId;

    private static final String userIdKey = "user_id";
    public String userId;

    private static final String memoKey = "memo";
    public String memo;

    private static final String updateTimeKey = "update_time";
    public String updateTime;

    private static final String friendAvatarKey = "friend_avatar";
    public String friendAvatar;

    private static final String friendNameKey = "friend_name";
    public String friendName;

    private static final String friendBirthKey = "friend_birth";
    public String friendBirth;

    private static final String friendAgeKey = "friend_age";
    public int friendAge;

    @Override
    public int compareTo(User o) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public Memo(String memoId, String friendId, String memo, String updateTime, String friendAvatar, String friendName, String friendBirth, int age) {
        this.memoId = memoId;
        this.friendId = friendId;
        this.memo = memo;
        this.updateTime = updateTime;
        this.friendAvatar = friendAvatar;
        this.friendName = friendName;
        this.friendBirth = friendBirth;
        this.friendAge = age;
    }

    public Memo(String friendId, String userId, String memo) {
        this.friendId = friendId;
        this.userId = userId;
        this.memo = memo;
    }

    public Memo() {
    }

    @Override
    public JSONObject toJsonObject() {
        JSONObject jo = new JSONObject();

        if (this.userId != null) {
            jo.put(userIdKey, this.userId);
        }
        if (this.memoId != null) {
            jo.put(memoIdKey, this.memoId);
        }
        if (this.friendId != null) {
            jo.put(friendIdKey, this.friendId);
        }
        if (this.userId != null) {
            jo.put(userIdKey, this.userId);
        }
        if (this.memo != null) {
            jo.put(memoKey, this.memo);
        }
        if (this.updateTime != null) {
            jo.put(updateTimeKey, this.updateTime);
        }
        if (this.friendAvatar != null) {
            jo.put(friendAvatarKey, this.friendAvatar);
        }
        if (this.friendAvatar == null) {
            jo.put(friendAvatarKey, "");
        }
        if (this.friendName != null) {
            jo.put(friendNameKey, this.friendName);
        }
        if (this.friendBirth != null) {
            jo.put(friendBirthKey, this.friendBirth);
        }
        if (this.friendAge != 0) {
            jo.put(friendAgeKey, this.friendAge);
        }
        return jo;
    }

}
