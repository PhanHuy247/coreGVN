/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.usermanagementserver.server.respond.impl.activity;

import com.vn.ntsc.usermanagementserver.dao.impl.AlbumDAO;
import com.vn.ntsc.usermanagementserver.dao.impl.AlbumImageDAO;
import com.vn.ntsc.usermanagementserver.dao.impl.AudioDAO;
import com.vn.ntsc.usermanagementserver.server.respond.Respond;
import com.vn.ntsc.usermanagementserver.server.respond.IApiAdapter;
import eazycommon.util.Util;
import java.util.Date;
import eazycommon.constant.Constant;
import eazycommon.constant.ErrorCode;
import eazycommon.exception.EazyException;
import org.json.simple.JSONObject;
import eazycommon.constant.ParamKey;
import com.vn.ntsc.usermanagementserver.dao.impl.BackstageDAO;
import com.vn.ntsc.usermanagementserver.dao.impl.BuzzDetailDAO;
import com.vn.ntsc.usermanagementserver.dao.impl.BuzzUserDAO;
import com.vn.ntsc.usermanagementserver.dao.impl.ImageDAO;
import com.vn.ntsc.usermanagementserver.dao.impl.PbAudioDAO;
import com.vn.ntsc.usermanagementserver.dao.impl.PbImageDAO;
import com.vn.ntsc.usermanagementserver.dao.impl.PbVideoDAO;
import com.vn.ntsc.usermanagementserver.dao.impl.UserBuzzDAO;
import com.vn.ntsc.usermanagementserver.dao.impl.UserDAO;
import com.vn.ntsc.usermanagementserver.dao.impl.VideoDAO;
import com.vn.ntsc.usermanagementserver.dao.impl.log.ReportAudioDAO;
import com.vn.ntsc.usermanagementserver.dao.impl.log.ReportImageDAO;
import com.vn.ntsc.usermanagementserver.dao.impl.log.ReportUserDAO;
import com.vn.ntsc.usermanagementserver.dao.impl.log.ReportVideoDAO;
import com.vn.ntsc.usermanagementserver.entity.impl.audio.Audio;
import com.vn.ntsc.usermanagementserver.entity.impl.image.Image;
import com.vn.ntsc.usermanagementserver.entity.impl.image.ReportImageData;
import com.vn.ntsc.usermanagementserver.entity.impl.video.Video;
import com.vn.ntsc.usermanagementserver.server.respond.common.Helper;
import com.vn.ntsc.usermanagementserver.server.respond.result.EntityRespond;
import com.vn.ntsc.usermanagementserver.setting.Setting;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author RuAc0n
 */
public class ReportApi implements IApiAdapter {

    @Override
    public Respond execute(JSONObject obj, Date time) {
        Respond result = new Respond();
        try {
            String userId = Util.getStringParam(obj, ParamKey.USER_ID);
            //why is report?
            Long reportType = Util.getLongParam(obj, ParamKey.REPORT_TYPE);
            // what is report?
            Long subjectType = Util.getLongParam(obj, "subject_type");
            //image,  user, buzz which is reported
            String subjectId = Util.getStringParam(obj, "subject_id");

            String ip = Util.getStringParam(obj, ParamKey.IP);
            if (reportType == null || subjectType == null || subjectId == null || subjectId.isEmpty()) {
                result.code = ErrorCode.WRONG_DATA_FORMAT;
                return result;
            }
            if (subjectType == Constant.REPORT_TYPE_VALUE.USER_REPORT) {

                if (UserDAO.isExists(subjectId)) {
                    boolean check = ReportUserDAO.addReportUser(subjectId, userId, reportType.intValue(), ip, time);
                    if (check) {
                        UserDAO.addReport(subjectId);
                    }
                } else {
//                    System.out.println(time.toString() + " ActivityWorker.report() --> UserID not exists : " + subjectId);
                    Util.addInfoLog(time + "ActivityWorker.report() --> UserID not exists : " + subjectId);
                }
            } else if (subjectType == Constant.REPORT_TYPE_VALUE.BUZZ_REPORT) {
                String buzzId = subjectId;
                subjectId = UserBuzzDAO.getUserId(buzzId);
                if (UserDAO.isExists(subjectId)) {
                    boolean check = ReportUserDAO.addReportUser(subjectId, userId, reportType.intValue(), ip, time);
                    if (check) {
                        UserDAO.addReport(subjectId);
                    }
                } else {
//                    System.out.println(time.toString() + " ActivityWorker.report() --> UserID not exists : " + subjectId);
                    Util.addInfoLog(time + "ActivityWorker.report() --> UserID not exists : " + subjectId);
                    result.code = ErrorCode.ACCESS_DENIED;
                    return result;
                }
            } else if(subjectType == Constant.REPORT_TYPE_VALUE.VIDEO_REPORT){
                String videoId = subjectId;
                Video video = VideoDAO.getVideoInfo(videoId);
                String owner = video.userId;
                if(video != null && video.flag == Constant.FLAG.ON){
                    Integer reportStatus = Constant.REPORT_STATUS_FLAG.WAITING;
                    Util.addDebugLog("Setting.AUTO_HIDE_REPORTED_VIDEO ======"+Setting.AUTO_HIDE_REPORTED_VIDEO);
                    if(Setting.AUTO_HIDE_REPORTED_VIDEO == Constant.FLAG.ON){
                        BuzzDetailDAO.updateFlagBuzz(videoId, Constant.BUZZ_TYPE_VALUE.VIDEO_STATUS, Constant.FLAG.OFF);
                        String buzzId = BuzzDetailDAO.getReportedBuzz(videoId);
                        BuzzUserDAO.updateFlagUserBuzz(owner, buzzId, Constant.FLAG.OFF);
                        PbVideoDAO.updateFlag(owner, videoId, Constant.FLAG.OFF);
                        reportStatus = Constant.REPORT_STATUS_FLAG.NOT_GOOD;
                    }
                    boolean isIncreaseReportNumber = ReportVideoDAO.addReportVideo(videoId, userId, reportType.intValue(), ip, owner, time,video.videoType);
                    VideoDAO.addReport(videoId, time, isIncreaseReportNumber, reportStatus);
                    result = new EntityRespond(ErrorCode.SUCCESS);
                }else if(video == null){
                    Util.addDebugLog("ErrorCode.ACCESS_DENIED --- 1");
                    result.code = ErrorCode.ACCESS_DENIED;
                    return result;
                }
            } else if(subjectType == Constant.REPORT_TYPE_VALUE.AUDIO_REPORT){
                String audioId = subjectId;
                Audio audio = AudioDAO.getAudioInfo(audioId);
                String owner = audio.userId;
                if(audio != null && audio.flag == Constant.FLAG.ON){
                    Integer reportStatus = Constant.REPORT_STATUS_FLAG.WAITING;
                    Util.addDebugLog("Setting.AUTO_HIDE_REPORTED_AUDIO ======"+Setting.AUTO_HIDE_REPORTED_VIDEO);
                    if(Setting.AUTO_HIDE_REPORTED_VIDEO == Constant.FLAG.ON){
                        BuzzDetailDAO.updateFlagBuzz(audioId, Constant.BUZZ_TYPE_VALUE.AUDIO_STATUS, Constant.FLAG.OFF);
                        String buzzId = BuzzDetailDAO.getReportedBuzz(audioId);
                        BuzzUserDAO.updateFlagUserBuzz(owner, buzzId, Constant.FLAG.OFF);
                        PbAudioDAO.updateFlag(owner, audioId, Constant.FLAG.OFF);
                        reportStatus = Constant.REPORT_STATUS_FLAG.NOT_GOOD;
                    }
                    boolean isIncreaseReportNumber = ReportAudioDAO.addReportAudio(audioId, userId, reportType.intValue(), ip, owner, time,audio.audioType);
                    AudioDAO.addReport(audioId, time, isIncreaseReportNumber, reportStatus);
                    result = new EntityRespond(ErrorCode.SUCCESS);
                }else if(audio == null){
                    Util.addDebugLog("ErrorCode.ACCESS_DENIED --- 2");
                    result.code = ErrorCode.ACCESS_DENIED;
                    return result;
                }
            } else if(subjectType == Constant.REPORT_TYPE_VALUE.ALBUM_IMG_REPORT){
                String imgId = subjectId;
                String albumId = AlbumImageDAO.getAlbumId(imgId);
                if(albumId != null && !albumId.equals("")){
                    String owner = AlbumDAO.getUserId(albumId);
                    Util.addDebugLog("Setting.AUTO_HIDE_REPORTED_IMAGE ======"+Setting.AUTO_HIDE_REPORTED_IMAGE);
                    if(Setting.AUTO_HIDE_REPORTED_IMAGE == Constant.FLAG.ON){
                        AlbumImageDAO.updateFlag(imgId, Constant.FLAG.OFF);
                    }
                    boolean isIncreaseReportNumber = ReportImageDAO.addReportImage(imgId, userId, reportType.intValue(), 1, ip, owner, time);
                    ImageDAO.addAlbumImgReport(subjectId, owner, time, isIncreaseReportNumber);
                    result = new EntityRespond(ErrorCode.SUCCESS);
                }else{
                    Util.addDebugLog("ErrorCode.ACCESS_DENIED --- 7");
                    result.code = ErrorCode.ACCESS_DENIED;
                    return result;
                }
            } else{
                Image image = ImageDAO.getImageInfor(subjectId);
                if (image != null && image.flag == Constant.FLAG.ON) {
                    if(image.flag == Constant.FLAG.ON){
                        int type = image.imageType;
                        String owner = image.userId;
                        int isAppear = Constant.FLAG.ON;
                        String buzzId = null;
                        int isAva = Constant.FLAG.OFF;
                        if(Setting.AUTO_HIDE_REPORTED_IMAGE == Constant.FLAG.ON){
                            if(Helper.isAvalableImage(image)){
                                if(type == Constant.BUZZ_TYPE_VALUE.IMAGE_BUZZ){
                                    UserDAO.removePbImage(owner);
                                    UserDAO.removeBuzz(owner);
                                    buzzId = PbImageDAO.getBuzzId(owner, image.imageId);
                                    PbImageDAO.updateFlag(owner, image.imageId, Constant.FLAG.OFF);
                                    boolean checkAvatar = UserDAO.checkAvatar(owner, image.imageId);
                                    if(checkAvatar){
                                        isAva = Constant.FLAG.ON;
                                        UserDAO.removeAvatar(owner);
                                    }
                                }else if(type == Constant.IMAGE_TYPE_VALUE.IMAGE_BACKSTAGE){
                                    UserDAO.removeBackstage(owner);
                                    BackstageDAO.updateFlag(owner, image.imageId, Constant.FLAG.OFF);
                                }
                            }else{
                                Util.addDebugLog("ErrorCode.ACCESS_DENIED --- 3");
                                result.code = ErrorCode.ACCESS_DENIED;
                                return result;
                            }
                            isAppear = Constant.FLAG.OFF;
                        }else{
                            if(!Helper.isAvalableImage(image)){
                                Util.addDebugLog("ErrorCode.ACCESS_DENIED --- 4");
                                result.code = ErrorCode.ACCESS_DENIED;
                                return result;
                            }
                        }
                        boolean isIncreaseReportNumber = ReportImageDAO.addReportImage(subjectId, userId, reportType.intValue(), type, ip, owner, time);
                        ImageDAO.addReport(subjectId, isAppear, time, isIncreaseReportNumber);
                        result = new EntityRespond(ErrorCode.SUCCESS, new ReportImageData(owner, isAppear, buzzId, isAva));
                        
                    }else{
                        Util.addDebugLog("ErrorCode.ACCESS_DENIED --- 5");
                        result.code = ErrorCode.ACCESS_DENIED;
                        return result;
                    }
                } else {
                    Util.addDebugLog("ErrorCode.ACCESS_DENIED --- 6");
                    result.code = ErrorCode.ACCESS_DENIED;
                    return result;
                }
            }
            result.code = ErrorCode.SUCCESS;
        } catch (EazyException ex) {
            Util.addErrorLog(ex);
            result.code = ex.getErrorCode();
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
        return result;
    }

}
