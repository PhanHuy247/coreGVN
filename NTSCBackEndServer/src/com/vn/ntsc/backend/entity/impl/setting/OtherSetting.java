/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.vn.ntsc.backend.entity.impl.setting;

import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import eazycommon.constant.ErrorCode;
import eazycommon.util.Util;
import org.json.simple.JSONObject;
import com.vn.ntsc.backend.entity.IEntity;

/**
 *
 * @author RuAc0n
 */
public class OtherSetting implements IEntity{
    
    private static final String unlockBackstageKey = "unlock_bckstg_time";
    public int unlockBackstageTime;
    
    public static final String unlockViewImageKey = "unlock_view_image_time";
    public Integer unlockViewImageTime;
    
    public static final String unlockWatchVideoKey = "unlock_watch_video_time";
    public Integer unlockWatchVideoTime;
    
    public static final String unlockListenAudioKey = "unlock_listen_audio_time";  
    public Integer unlockListenAudioTime;
    
    private static final String autoApprovedImageKey = "auto_approved_img";
    public int autoApproveImage;
    
    private static final String autoApprovedBuzzKey = "auto_approved_buzz";
    public Integer autoApproveBuzz;
    private static final String autoApprovedCommentKey = "auto_approved_comment";
    public Integer autoApproveComment;
    private static final String autoApprovedUserInfoKey = "auto_approved_user_info";
    public Integer autoApproveUserInfo;
    
    private static final String autoHideReportedImageKey = "auto_hide_reported_image";
    public int autoHideReportedImage;
    private static final String autoHideReportedVideoKey = "auto_hide_reported_video";
    public int autoHideReportedVideo;
    private static final String autoHideReportedAudioKey = "auto_hide_reported_audio";
    public int autoHideReportedAudio;
    
//    private static final String enterpriseTurnOffSafaryKey = "enterprise_turn_off_safary";
//    public Boolean enterpriseTurnOffSafary;
    
//    private static final String androidUsableVersionKey = "android_usable_version";
//    public String androidUsableVersion;
    
    
    private static final String turnOffSafaryKey = "turn_off_safary";
    public Boolean turnOffSafary;
    
    private static final String turnOffSafaryVersionKey = "turn_off_safary_version";
    public String turnOffSafaryVersion; 
    
    private static final String turnOffloginByMocomKey = "turn_off_login_by_mocom";
    public Boolean loginByMocom; 
    
    private static final String turnOffExtendedUserInfoKey = "turn_off_extended_user_info";
    public Boolean turnOffExtendedUserInfo; 
    
    private static final String turnOffShowNewsKey = "turn_off_show_news";
    public Boolean turnOffShowNews; 
    
    private static final String turnOffgetFreePointKey = "turn_off_get_free_point";
    public Boolean getFreePoint;
    
    private static final String turnOffBrowserAndroidKey = "turn_off_browser_android";
    public Boolean turnOffBrowser;
    
    private static final String turnOffBrowserAndroidVersionKey = "turn_off_browser_android_version";
    public String turnOffBrowserAndroidVersion; 
    
    private static final String turnOffloginByMocomAndroidKey = "turn_off_login_by_mocom_android";
    public Boolean loginByMocomAndroid; 
    
    private static final String turnOffExtendedUserInfoAndroidKey = "turn_off_extended_user_info_android";
    public Boolean turnOffExtendedUserInfoAndroid; 
    
    private static final String turnOffShowNewsAndroidKey = "turn_off_show_news_android";
    public Boolean turnOffShowNewsAndroid; 
    
    private static final String turnOffgetFreePointAndroidKey = "turn_off_get_free_point_android";
    public Boolean getFreePointAndroid;
    
    //Linh add 5729
    private static final String autoApprovedVideoKey = "auto_approved_video";
    public int autoApproveVideo;
    
//    private static final String maxLengthBuzzKey = "max_length_buzz";
//    public Integer maxLengthBuzz;
    
//    private static final String enterpriseTurnOffSafaryVersionKey = "enterprise_turn_off_safary_version";
//    public String enterpriseTurnOffSafaryVersion; 
//    
//    private static final String enterpriseTurnOffLoginByMocomKey = "enterprise_turn_off_login_by_mocom";
//    public Boolean enterpriseLoginByMocom;  
    
//    private static final String iosEnterpriseVersionKey = "ios_enterprise_usable_version";
//    public String iosEnterpriseVersion; 
//    
//    private static final String iosNonEnterpriseVersionKey = "ios_non_enterprise_usable_version";
//    public String iosNonEnterpriseVersion;  
    
    private static final Map<String, Integer> keys = new TreeMap<>();
    static{
        keys.put(unlockBackstageKey, 8);
        keys.put(autoApprovedImageKey, 9);
        keys.put(autoHideReportedImageKey, 10);
//        keys.put(androidUsableVersionKey, ErrorCode.WRONG_DATA_FORMAT);
        keys.put(turnOffSafaryVersionKey, ErrorCode.WRONG_DATA_FORMAT);
        keys.put(turnOffBrowserAndroidVersionKey, ErrorCode.WRONG_DATA_FORMAT);
//        keys.put(iosEnterpriseVersionKey, ErrorCode.WRONG_DATA_FORMAT);
//        keys.put(iosNonEnterpriseVersionKey, ErrorCode.WRONG_DATA_FORMAT);
        keys.put(unlockViewImageKey, 11);
        keys.put(unlockWatchVideoKey, 12);
        keys.put(unlockListenAudioKey, 13);
        keys.put(autoApprovedBuzzKey, 14);
        keys.put(autoApprovedCommentKey, 15);
        keys.put(autoApprovedUserInfoKey, 16);
        keys.put(autoApprovedUserInfoKey, 17);
        
    }    
    
    @Override
    public JSONObject toJsonObject(){
        JSONObject jo = new JSONObject();
       
        jo.put(unlockBackstageKey, this.unlockBackstageTime);
        jo.put(unlockViewImageKey, this.unlockViewImageTime);
        jo.put(unlockWatchVideoKey, this.unlockWatchVideoTime);
        jo.put(unlockListenAudioKey, this.unlockListenAudioTime);
        jo.put(autoApprovedImageKey, this.autoApproveImage);
        jo.put(autoApprovedVideoKey, this.autoApproveVideo);
        if(autoApproveBuzz != null)
            jo.put(autoApprovedBuzzKey, this.autoApproveBuzz);
        if(autoApproveComment != null)
            jo.put(autoApprovedCommentKey, this.autoApproveComment);
        if(autoApproveUserInfo != null)
            jo.put(autoApprovedUserInfoKey, this.autoApproveUserInfo);
        jo.put(autoHideReportedImageKey, this.autoHideReportedImage);
        jo.put(autoHideReportedVideoKey, this.autoHideReportedVideo);
        jo.put(autoHideReportedAudioKey, this.autoHideReportedAudio);
        jo.put(turnOffSafaryKey, this.turnOffSafary);
        jo.put(turnOffSafaryVersionKey, this.turnOffSafaryVersion);
        jo.put(turnOffloginByMocomKey, this.loginByMocom);
        jo.put(turnOffExtendedUserInfoKey, this.turnOffExtendedUserInfo);
        jo.put(turnOffShowNewsKey, this.turnOffShowNews);
        jo.put(turnOffgetFreePointKey, this.getFreePoint);
        jo.put(turnOffBrowserAndroidKey, this.turnOffBrowser);
        jo.put(turnOffBrowserAndroidVersionKey, this.turnOffBrowserAndroidVersion);
        jo.put(turnOffgetFreePointAndroidKey, this.getFreePointAndroid);
        jo.put(turnOffloginByMocomAndroidKey, this.loginByMocomAndroid);
        jo.put(turnOffExtendedUserInfoAndroidKey, this.turnOffExtendedUserInfoAndroid);
        jo.put(turnOffShowNewsAndroidKey, this.turnOffShowNewsAndroid);
//        if(maxLengthBuzz != null){
//            jo.put(maxLengthBuzzKey, this.maxLengthBuzz);
//        }
//        jo.put(androidUsableVersionKey, this.androidUsableVersion);

//        jo.put(iosEnterpriseVersionKey, this.iosEnterpriseVersion);
//        jo.put(iosNonEnterpriseVersionKey, this.iosNonEnterpriseVersion);

        
        
        return jo;
    }
    
    private static final String REGEX = "^(([0-9a-z.])+)$";
    private static final Pattern pattern = Pattern.compile(REGEX);
    public static int validate(JSONObject obj){
        for(Map.Entry<String, Integer> pair : keys.entrySet()){
            try{
                String key = pair.getKey();

                if(key.equals(unlockBackstageKey)
                        || key.equals(unlockViewImageKey)
                        || key.equals(unlockWatchVideoKey)
                        || key.equals(unlockListenAudioKey)){
                    Long d = Util.getLongParam(obj, key);
                    if(d == null || d <= 0){
                        return pair.getValue();
                    }
                }else if( key.equals(turnOffSafaryVersionKey) || key.equals(turnOffBrowserAndroidVersionKey)){
                    String str = Util.getStringParam(obj, key);
                    if(str != null && !str.isEmpty() && !validate(str))
                        return pair.getValue();
                }else{
                    Long d = Util.getLongParam(obj, key);
                    if( d<0 || d > 1)
                        return pair.getValue();
                }
            }catch(Exception ex){
                return pair.getValue();
            }
        }
        return 0;
    }    
    
    private static boolean validate(String string) {
        Matcher matcher = pattern.matcher(string);
        return matcher.matches();
    }    
    
    public static OtherSetting createOtherSetting (JSONObject obj){
        OtherSetting os = new OtherSetting();

        Long back = Util.getLongParam(obj, unlockBackstageKey);
        os.unlockBackstageTime = back.intValue();
        
        Long image = Util.getLongParam(obj, unlockViewImageKey);
        os.unlockViewImageTime = image.intValue();
        
        Long video = Util.getLongParam(obj, unlockWatchVideoKey);
        os.unlockWatchVideoTime = video.intValue();
        
        Long audio = Util.getLongParam(obj, unlockListenAudioKey);
        os.unlockListenAudioTime = audio.intValue();

        Long auto = Util.getLongParam(obj, autoApprovedImageKey);
        os.autoApproveImage = auto.intValue();  
        
        Long autoApprovedVideo = Util.getLongParam(obj, autoApprovedVideoKey);
        os.autoApproveVideo = autoApprovedVideo.intValue();
        
        Long buzz = Util.getLongParam(obj, autoApprovedBuzzKey);
        os.autoApproveBuzz = buzz.intValue();  
        Long comment = Util.getLongParam(obj, autoApprovedCommentKey);
        os.autoApproveComment = comment.intValue();  
        Long userInfo = Util.getLongParam(obj, autoApprovedUserInfoKey);
        os.autoApproveUserInfo = userInfo.intValue();  

        Long hide = Util.getLongParam(obj, autoHideReportedImageKey);
        os.autoHideReportedImage = hide.intValue();  
        Long hideVideo = Util.getLongParam(obj, autoHideReportedVideoKey);
        os.autoHideReportedVideo = hideVideo.intValue();  
//        Long hideAudio = Util.getLongParam(obj, autoHideReportedAudioKey);
//        os.autoHideReportedAudio = hideAudio.intValue();  
        
        String turnOffSafaryVersion = Util.getStringParam(obj, turnOffSafaryVersionKey);
        os.turnOffSafaryVersion = turnOffSafaryVersion;  
        
        Boolean turnOffSafary = (Boolean) obj.get(turnOffSafaryKey);
        os.turnOffSafary = turnOffSafary; 
        
        Boolean loginByMocom = (Boolean) obj.get(turnOffloginByMocomKey);
        os.loginByMocom = loginByMocom; 
        
        Boolean extendedUserInfo = (Boolean) obj.get(turnOffExtendedUserInfoKey);
        os.turnOffExtendedUserInfo = extendedUserInfo; 
        
        Boolean showNews = (Boolean) obj.get(turnOffShowNewsKey);
        os.turnOffShowNews = showNews; 
        
        Boolean getFreePoint = (Boolean) obj.get(turnOffgetFreePointKey);
        os.getFreePoint = getFreePoint; 
        
        String turnOffBrowserVersion = Util.getStringParam(obj, turnOffBrowserAndroidVersionKey);
        os.turnOffBrowserAndroidVersion = turnOffBrowserVersion;  
        
        Boolean turnOffBrowserAndroid = (Boolean) obj.get(turnOffBrowserAndroidKey);
        os.turnOffBrowser = turnOffBrowserAndroid; 
        
        Boolean loginByMocomAndroid = (Boolean) obj.get(turnOffloginByMocomAndroidKey);
        os.loginByMocomAndroid = loginByMocomAndroid; 
        
        Boolean extendedUserInfoAndroid = (Boolean) obj.get(turnOffExtendedUserInfoAndroidKey);
        os.turnOffExtendedUserInfoAndroid = extendedUserInfoAndroid; 
        
        Boolean showNewsAndroid = (Boolean) obj.get(turnOffShowNewsAndroidKey);
        os.turnOffShowNewsAndroid = showNewsAndroid; 
        
        Boolean getFreePointAndroid = (Boolean) obj.get(turnOffgetFreePointAndroidKey);
        os.getFreePointAndroid = getFreePointAndroid; 
        
//        Long maxLengthBuzz = (Long) obj.get(maxLengthBuzzKey);
//        os.maxLengthBuzz = maxLengthBuzz.intValue();
        
//        String androidUsableVersion = Util.getStringParam(obj, androidUsableVersionKey);
//        os.androidUsableVersion = androidUsableVersion;  
//        System.out.println(os.androidUsableVersion);
//        
//        String iosEnterpriseUsableVersion = (String) obj.get(iosEnterpriseVersionKey);
//        os.iosEnterpriseVersion = iosEnterpriseUsableVersion; 
//        System.out.println(os.iosEnterpriseVersion);
//        
//        String iosNonEnterpriseUsableVersion = (String) obj.get(iosNonEnterpriseVersionKey);
//        os.iosNonEnterpriseVersion = iosNonEnterpriseUsableVersion; 
//        System.out.println(os.iosNonEnterpriseVersion);
        

        return os;
    }
}
