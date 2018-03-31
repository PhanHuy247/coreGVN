/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.vn.ntsc.usermanagementserver.entity.impl.setting;

import eazycommon.util.Util;
import org.json.simple.JSONObject;
import com.vn.ntsc.usermanagementserver.entity.IEntity;

/**
 *
 * @author RuAc0n
 */
public class OtherSetting implements IEntity{

    private static final String unlockBackstageKey = "unlock_bckstg_time";
    public int unlockBackstageTime;
    private static final String unlockViewImageTimeKey = "unlock_view_image_time";
    public int unlockViewImageTime;
    private static final String unlockWatchVideoTimeKey = "unlock_watch_video_time";
    public int unlockWatchVideoTime;
    private static final String unlockListenAudioTimeKey = "unlock_listen_audio_time";
    public int unlockListenAudioTime;
    
    private static final String autoApprovedImageKey = "auto_approved_img";
    public int autoApproveImage;
    
    private static final String autoApprovedUserInfoKey = "auto_approved_user_info";
    public int autoApproveUserInfo;

    private static final String autoHideReportedImageKey = "auto_hide_reported_image";
    public int autoHideReportedImage;    
    private static final String autoHideReportedVideoKey = "auto_hide_reported_video";
    public int autoHideReportedVideo;
    private static final String autoHideReportedAudioKey = "auto_hide_reported_audio";
    public int autoHideReportedAudio;
    
    @Override
    public JSONObject toJsonObject(){
        JSONObject jo = new JSONObject();

        jo.put(unlockBackstageKey, this.unlockBackstageTime);
        jo.put(unlockViewImageTimeKey, this.unlockViewImageTime);
        jo.put(unlockWatchVideoTimeKey, this.unlockWatchVideoTime);
        jo.put(unlockListenAudioTimeKey, this.unlockListenAudioTime);
        jo.put(autoApprovedImageKey, this.autoApproveImage);
        jo.put(autoHideReportedImageKey, this.autoHideReportedImage);
        jo.put(autoApprovedUserInfoKey, this.autoApproveUserInfo);
        jo.put(autoHideReportedVideoKey, this.autoHideReportedVideo);
        jo.put(autoHideReportedAudioKey, this.autoHideReportedAudio);   
        return jo;
    }

    public String toJson(){
        JSONObject jo = this.toJsonObject();
        return jo.toJSONString();
    } 
  
    
    public static OtherSetting createOtherSetting (JSONObject obj){
        OtherSetting os = new OtherSetting();
        try{
            Long back = Util.getLongParam(obj, unlockBackstageKey);
            os.unlockBackstageTime = back.intValue();
            
            Long image = Util.getLongParam(obj, unlockViewImageTimeKey);
            os.unlockViewImageTime = image.intValue();
            
            Long video = Util.getLongParam(obj, unlockWatchVideoTimeKey);
            os.unlockWatchVideoTime = video.intValue();
            
            Long audio = Util.getLongParam(obj, unlockListenAudioTimeKey);
            os.unlockListenAudioTime = audio.intValue();
                        
            Long auto = Util.getLongParam(obj, autoApprovedImageKey);
            os.autoApproveImage = auto.intValue(); 
            
            Long userInfo = Util.getLongParam(obj, autoApprovedUserInfoKey);
            os.autoApproveUserInfo = userInfo.intValue(); 
            
            Long hide = Util.getLongParam(obj, autoHideReportedImageKey);
            os.autoHideReportedImage = hide.intValue();
            Long hideVideo = Util.getLongParam(obj, autoHideReportedVideoKey);
            os.autoHideReportedVideo = hideVideo.intValue();  
//            Long hideAudio = Util.getLongParam(obj, autoHideReportedAudioKey);
//            os.autoHideReportedAudio = hideAudio.intValue();  
        }catch(Exception ex){
            Util.addErrorLog(ex);
           
            return null;
        }
        return os;
    }
}
