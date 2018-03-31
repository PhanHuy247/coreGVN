/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.backend.entity.impl.user.extend;

import java.util.List;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import com.vn.ntsc.backend.entity.impl.user.User;

/**
 *
 * @author Admin
 */
public class Female extends User {

    //    HashMap<String, Long> measurements;
//    public static final String bustKey = "bust";
//    public static final String waistKey = "waist";
//    public static final String hipsKey = "hips";
    // so do 3 vong
    public Female() {
    }

    public static final String measurementsKey = "measurements";
    public List<Long> measurements;

    public static final String bustKey = "bust";
    public Long bust;

    public static final String waistKey = "waist";
    public Long waist;

    public static final String hipsKey = "hips";
    public Long hips;
    // loai nguc
    public static final String cupKey = "cup";
    public Long cup;
    // muc do sex - min
//    public static final String indecentKey = "indecent";
//    public Long indecent;

    public static final String typeOfMansKey = "type_of_man";
    public String typeOfMan;

    public static final String fetishKey = "fetish";
    public String fetishs;

    private static final String cuteTypeKey = "cute_type";
    public Long cuteType;

    public static final String joinHoursKey = "join_hours";
    public Long joinHours;

    private static final String hobbyKey = "hobby";
    public String hobby;

    @Override
    public JSONObject toJsonObject() {
        JSONObject jo = super.toJsonObject();

        if (this.measurements != null) {
            jo.put(Female.measurementsKey, listToJSON(measurements));
        }

//        if (this.indecent != null) {
//            jo.put(Female.indecentKey, this.indecent);
//        }
        if (this.fetishs != null) {
            jo.put(Female.fetishKey, fetishs);
        }

        if (this.typeOfMan != null) {
            jo.put(Female.typeOfMansKey, this.typeOfMan);
        }

        if (cup != null) {
            jo.put(cupKey, cup);
        }

        if (cuteType != null) {
            jo.put(cuteTypeKey, cuteType);
        }

        if (joinHours != null) {
            jo.put(joinHoursKey, joinHours);
        }
        if (hobby != null) {
            jo.put(hobbyKey, hobby);
        }
        
        return jo;
    }

    public static JSONArray listToJSON(List<Long> list) {
        JSONArray arr = new JSONArray();
        for (Long l : list) {
            arr.add(l);
        }
        return arr;
    }
}
