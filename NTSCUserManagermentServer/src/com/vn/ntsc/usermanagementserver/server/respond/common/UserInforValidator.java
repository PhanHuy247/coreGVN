/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.usermanagementserver.server.respond.common;

import eazycommon.util.Util;
import java.util.ArrayList;
import java.util.List;
import eazycommon.constant.Constant;
import com.vn.ntsc.usermanagementserver.entity.impl.user.User;
import com.vn.ntsc.usermanagementserver.entity.impl.user.extend.Female;
import com.vn.ntsc.usermanagementserver.entity.impl.user.extend.Male;

/**
 *
 * @author RuAc0n
 */
public class UserInforValidator {

    private static final Long ask_me_value = (long) -1;
    private static final List<Long> gender;
    private static final List<Long> region;
    private static final List<Long> cuteType;
    private static final List<Long> jobs;
    private static final List<Long> joinHours;
    private static final List<Long> cups;
//    private static final List<Long> incedents;
    public static class Measurements {

        public static final int minBust = 50;
        public static final int maxBust = 120;

        public static final int minWaist = 50;
        public static final int maxWaist = 120;

        public static final int minHips = 50;
        public static final int maxHips = 120;
    }

    static {
        gender = new ArrayList<>();
        cuteType = new ArrayList<>();
        region = new ArrayList<>();
        jobs = new ArrayList<>();
        joinHours = new ArrayList<>();
        cups = new ArrayList<>();
//        incedents = new ArrayList<Long>();
        
        initListData(0, 2, gender);
        initListData(0, 13, cuteType);
        initListData(1, 63, region);
        initListData(0, 40, jobs);
        initListData(0, 8, joinHours);
        initListData(0, 9, cups);
//        initListData(0, 6, incedents);
    }

    private static final int min_lengh = 1;

    private static final int max_user_name_lengh = 14;
    //private static final int max_about_lengh = 200;
    //HUNGDT edit
    //private static final int max_about_lengh = 3000;
    private static final int max_about_lengh = 20000;
    private static final int max_other_field_lengh = 100;
    
    private static final int min_age = 14;
    private static final int max_age = 120;
    
    

    public static void initListData(long minValue, long maxValue, List<Long> list) {
        for (long i = minValue; i <= maxValue; i++) {
            list.add(i);
        }
    }

    
    
    public static boolean validate(User user, int flag, User beforeUser) {
        if (!validateName(user, flag)) {
//            System.out.println("2");
            return false;
        }
//        if (user instanceof Female) {
//            if (!validateFemale(user, beforeUser)) {
////                System.out.println("3");
//                return false;
//            }
//        }else{
//            Male male = (Male) user;
//            validateText(male.hobby, min_lengh, max_other_field_lengh);
//        }
        if(!validateRegion(user, beforeUser.region)){
//            System.out.println("region");
            return false;
        }
        return validateText(user.about, min_lengh, max_about_lengh);
    }
    
    public static boolean validate(User user) {
        if (!validateName(user, Constant.FLAG.OFF)) {
            return false;
        }
        if (user instanceof Female) {
            if (!validateFemale(user)) {
//                System.out.println("3");
                return false;
            }
        }else{
            Male male = (Male) user;
            validateText(male.hobby, min_lengh, max_other_field_lengh);
        }
        
        return validateText(user.about, min_lengh, max_about_lengh);
    }

    public static boolean validateGender(User user) {
//        System.out.println("5");
        return user.gender != null && gender.contains(user.gender);
    }    
//    
//    public static boolean validateJob(User user, int flag, User beforeUser) {
//        if (user instanceof Female) {
//            if(user.job != null){
//                return jobs.contains(user.job);
//            }else{
//                return flag != Constant.YES;
//            }
//        }else{
//            if(beforeUser.job == null || beforeUser.job == -1){
//                if(user.job == null || user.job.intValue() == ask_me_value)
//                    return true;                
//            
//            }else{
//                System.out.println("vao day");
//                if(user.job == null || user.job.intValue() == ask_me_value){
//                    System.out.println("vao day 1");
//                    return false; 
//                }
//            }
//            return jobs.contains(user.job);            
//        }
//    }    
    public static boolean validateJob(User user, int flag, User beforeUser) {
//        if(beforeUser.job == null || beforeUser.job == -1){
        if(beforeUser.job == null){
            if(user.job == null || user.job.intValue() == ask_me_value)
                return true;                

        }else{
//            System.out.println("vao day");
            if(user.job == null || user.job.intValue() == ask_me_value){
//                System.out.println("vao day 1");
                return false; 
            }
        }
        return jobs.contains(user.job);            
        
    }    
    
    public static boolean validateFemale(User user) {
        Female female = (Female) user;
        if (!validateMeasurements(female, null ) /**|| !validateIndecent(female, before)**/) {
//            System.out.println("6");
            return false;
        }
        if(!validateValue(female.cup, cups, null) ){
//            System.out.println("7");
            return false;
        }
        if(!validateValue( female.cuteType, cuteType, null) ){
//            System.out.println("8");
            return false;
        }
        if(!validateValue( female.joinHours, joinHours, null) ){
//            System.out.println("9");
            return false;
        }
        
        if(!validateText(female.fetishs, min_lengh, max_other_field_lengh) ){
            return false;
        }        
        if(!validateText(female.typeOfMan, min_lengh, max_about_lengh) ){
            return false;
        }  
        return true;
    }
    
    public static boolean validateFemale(User user, User beforeUser) {
        Female female = (Female) user;
        Female before = (Female) beforeUser;
        if (!validateMeasurements(female, before ) /**|| !validateIndecent(female, before)**/) {
//            System.out.println("6");
            return false;
        }
        if(!validateValue(female.cup, cups, before.cup) ){
//            System.out.println("7");
            return false;
        }
        if(!validateValue( female.cuteType, cuteType, before.cuteType) ){
//            System.out.println("8");
            return false;
        }
        if(!validateValue( female.joinHours, joinHours, before.joinHours) ){
//            System.out.println("9");
            return false;
        }
//        if(!validateText(female.fetishs, flag) ){
//            System.out.println("10");
//            return false;
//        }        
//        if(!validateText(female.typeOfMan, flag) ){
//            System.out.println("11");
//            return false;
//        }  
        if(!validateText(female.fetishs, min_lengh, max_other_field_lengh) ){
//            System.out.println("10");
            return false;
        }        
        if(!validateText(female.typeOfMan, min_lengh, max_about_lengh) ){
//            System.out.println("11");
            return false;
        }  
        return true;
    }

    public static boolean validateMeasurements(Female female, Female before) {

        if (female.measurements != null && !female.measurements.isEmpty()) {
            if (female.measurements.size() != 3) {
//                 System.out.println("12");
                return false;
            }
            Long bust = female.measurements.get(0);
            if (bust < Measurements.minBust || bust > Measurements.maxBust) {
//                 System.out.println("13");
                return false;
            }

            Long waist = female.measurements.get(1);
            if (waist < Measurements.minWaist || waist > Measurements.maxWaist) {
//                 System.out.println("14");
                return false;
            }
            Long hips = female.measurements.get(2);
            if (hips < Measurements.minHips || hips > Measurements.maxHips) {
//                 System.out.println("15");
                return false;
            }
        }else{
//            System.out.println("mesurent : " + before.measurements);
            return before == null || before.measurements == null || before.measurements.isEmpty();
        }
        return true;
    }

//    public static boolean validateIndecent(Female female, Female before) {
//        Long indecent = female.indecent;
//        System.out.println("female. indicent: " + indecent);
//        System.out.println("standard: " + incedents);
//        if (indecent == null) {
//            System.out.println("indicent");
//            return before.indecent == null;
//        }else {
//            
//            return incedents.contains(indecent);
//        }
//    }

    public static boolean validateName(User user, int flag) {
        if(user.username != null){
            if (user.username.trim().isEmpty() || user.username.length() < min_lengh || user.username.length() > max_user_name_lengh) {
                    return false;
            }        
        }else{
            return flag != Constant.FLAG.ON;
        }
        return true;
    }

    public static boolean validateAbout(User user, int flag) {
//        if(user instanceof Female){
//        System.out.println("4");
//            if(user.about == null || user.about.trim().isEmpty()){
//                return flag != Constant.YES;
//            }else{
//                if (user.about.trim().length() < min_lengh || user.about.trim().length() > max_about_lengh) {
//                    return false;
//                }              
//            }
//        }
        return true;
    }

    public static boolean validateText(String value, int flag) {
        if(value == null || value.trim().isEmpty()){
            return flag != Constant.FLAG.ON;
        }else{
            if (value.trim().length() < min_lengh || value.trim().length() > max_other_field_lengh) {
                return false;
            } 
        }
        return true;
    }    
    public static boolean validateText(String value, int min, int max) {
        if(value == null || value.trim().isEmpty()){
            return true;
        }else{
            if (value.trim().length() < min || value.trim().length() > max) {
                return false;
            } 
        }
        return true;
    }    
    
    public static boolean validateAutoRegion(User user) {
        if (user.autoRegion == null || user.autoRegion < 0 || user.autoRegion > 1) {
            return false;
        }
        return true;
    }

    public static boolean validateRegion(User user, Long beforeRegion) {
        return validateValue(user.region, region, beforeRegion);
    }

    private static boolean validateValue(Long value, List<Long> standard, Long beforeValue) {
        if(value != null)
            return standard.contains(value);
        else{
            return beforeValue == null;
        }
    }    
    
    public static boolean validateBirthday(User user, int flag) {
        if(flag == Constant.FLAG.ON){
            if (user.birthday == null) {
                return false;
            }
        }else{
            if (user.birthday == null) {
                return true;
            }
        }
        if(user.birthday.length() != 8)
            return false;
        int age = Util.convertBirthdayToAge(user.birthday);
        return age >= min_age && age <= max_age;
    }

    public static boolean validatePassword(String pwd, String cfmPwd) {
        if (pwd == null){
            return false;
        }
        if (!pwd.equals(cfmPwd)){
            return false;
        }
        return true;
    }
    
}
