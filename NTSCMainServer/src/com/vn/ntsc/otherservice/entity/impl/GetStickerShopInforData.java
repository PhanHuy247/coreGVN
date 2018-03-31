/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.otherservice.entity.impl;

import java.util.List;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import com.vn.ntsc.otherservice.entity.IEntity;

/**
 *
 * @author RuAc0n
 */
public class GetStickerShopInforData implements IEntity {

    private static final String haveNewCategory = "have_new";
    public Integer haveNew;

    private static final String listDownloadKey = "lst_download";
    public List<String> listDownload;

    public JSONObject toJsonObject() {
        JSONObject jo = new JSONObject();

        if (this.listDownload != null) {
            JSONArray arr = new JSONArray();
            for (String str : this.listDownload) {
                arr.add(str);
            }
            jo.put(listDownloadKey, arr);
        }
        if (this.haveNew != null) {
            jo.put(haveNewCategory, this.haveNew);
        }
        return jo;
    }

    public GetStickerShopInforData(List<String> listDownload, int haveNew) {
        this.haveNew = haveNew;
        this.listDownload = listDownload;
    }
}
