/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.vn.ntsc.backend.entity.impl.datarespond;

import org.json.simple.JSONObject;
import com.vn.ntsc.backend.entity.IEntity;

/**
 *
 * @author Rua
 */
public class CSVData implements IEntity{
    private static final String csvNameKey = "csv_name";
    public String csvName;
 
    private static final String csvURLKey = "csv_url";
    public String csvUrl;

    public CSVData(String csvName, String csvUrl) {
        this.csvName = csvName;
        this.csvUrl = csvUrl;
    }

    @Override
    public JSONObject toJsonObject() {
        JSONObject jo = new JSONObject();

        if (csvName != null) {
            jo.put(csvNameKey, this.csvName);
        }
        if (csvUrl != null) {
            jo.put(csvURLKey, this.csvUrl);
        }

        return jo;
    }
}
