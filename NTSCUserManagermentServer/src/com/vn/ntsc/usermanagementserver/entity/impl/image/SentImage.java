/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.usermanagementserver.entity.impl.image;

import java.util.Objects;// LongLT
import org.json.simple.JSONObject;
import com.vn.ntsc.usermanagementserver.entity.IEntity;

/**
 *
 * @author RuAc0n
 */
public class SentImage implements IEntity, Comparable<SentImage> {

    private static final String imageIdKey = "img_id";
    public String imageId;

    private static final String isOwnKey = "is_own";
    public Boolean isOwn;

    // HUNGDT add
    private static final String isUnlockKey = "is_unlock";
    private Boolean isUnlock;

    @Override
    public JSONObject toJsonObject() {
        JSONObject jo = new JSONObject();

        if (this.imageId != null) {
            jo.put(imageIdKey, this.imageId);
        }
        if (this.isOwn != null) {
            jo.put(isOwnKey, this.isOwn);
        }
        // HUNGDT add
        if (this.isUnlock != null) {
            jo.put(isUnlockKey, this.isUnlock);
        }

        return jo;
    }

    public String toJson() {
        JSONObject jo = this.toJsonObject();
        return jo.toJSONString();
    }

    public SentImage(String imageId, Boolean isOwn) {
        this.imageId = imageId;
        this.isOwn = isOwn;
    }

    // HUNGDT add
    public void setIsUnlock(Boolean isUnlock) {
        this.isUnlock = isUnlock;
    }

    @Override
    public int compareTo(SentImage o) {
        return this.imageId.compareTo(o.imageId);
    }

    // HUNGDT add
    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        SentImage image = (SentImage) o;
        return image.imageId.equals(this.imageId);
    }

    // HUNGDT add
    @Override
    public int hashCode() {
        int hash = 3;
        hash = 97 * hash + Objects.hashCode(this.imageId);
        return hash;
    }

}
