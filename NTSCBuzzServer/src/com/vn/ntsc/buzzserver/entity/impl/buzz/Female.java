/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.buzzserver.entity.impl.buzz;

import eazycommon.util.Util;
import java.util.List;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;


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

//    public static final String bustKey = "bust";
//    public Long bust;
//
//    public static final String waistKey = "waist";
//    public Long waist;
//
//    public static final String hipsKey = "hips";
//    public Long hips;
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

    public static final String cuteTypeKey = "cute_type";
    public Long cuteType;

    public static final String joinHoursKey = "join_hours";
    public Long joinHours;

    private static final String verificationFlagKey = "verification_flag";
    public Long verificationFlag;

    public static final String hobbyKey = "hobby";
    public String hobby;
    
    @Override
    public JSONObject toJsonObject() {
        JSONObject jo = super.toJsonObject();

        if (this.measurements != null) {
            jo.put(Female.measurementsKey, listToJSON(measurements));
//            jo.put(bustKey, bust);
//            jo.put(waistKey, waist);
//            jo.put(hipsKey, hips);
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
        if (this.verificationFlag != null) {
            jo.put(verificationFlagKey, this.verificationFlag);
        }
        if (hobbyKey != null) {
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

    @Override
    public void getUpdateUser(JSONObject obj) {
        super.getUpdateUser(obj);
//        Long indecent = Util.getLongParam(obj, indecentKey);
//        this.indecent = indecent;
        Long cup = Util.getLongParam(obj, cupKey);
        this.cup = cup;
        Long cute = Util.getLongParam(obj, cuteTypeKey);
        this.cuteType = cute;
        /**
         * thứ tự các vòng vòng 1 vòng 2 vòng 3
         */
        List<Long> measurements = Util.getListLong(obj, measurementsKey);
        if(measurements != null && !measurements.isEmpty())
            this.measurements = measurements;

        String typeOfMalse = Util.getStringParam(obj, typeOfMansKey);
        if (typeOfMalse != null) {
            this.typeOfMan = typeOfMalse;
        }
        String fetishs = Util.getStringParam(obj, fetishKey);
        if (fetishs != null) {
            this.fetishs = fetishs;
        }

        Long actionTime = (Long) obj.get(joinHoursKey);
        if (actionTime != null) {
            this.joinHours = actionTime;
        }
        
        Long verification = (Long) obj.get(verificationFlagKey);
        if (verification != null) {
            this.verificationFlag = verification;
        }
        
        String str = Util.getStringParam(obj, hobbyKey);
        if (str != null) {
            hobby = str;
        }
    }

//    @Override
//    public boolean isCompleteUser(){
//        if(!super.isCompleteUser())
//            return false;
//        else{
//            if(this.measurements == null || this.cup == null || this.indecent == null )
//                return false;
//            if(this.cuteType == null || this.joinHours == null || this.job == null){
//                return false;
//            }
//            if(this.fetishs == null || this.typeOfMan == null){
//                return false;
//            }
//            
//        }
//        return true;
//    }
}
