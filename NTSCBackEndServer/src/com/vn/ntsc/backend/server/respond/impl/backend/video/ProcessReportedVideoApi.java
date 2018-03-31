/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.backend.server.respond.impl.backend.video;

import com.vn.ntsc.backend.dao.buzz.BuzzDetailDAO;
import com.vn.ntsc.backend.dao.buzz.UserBuzzDAO;
import com.vn.ntsc.backend.dao.user.AudioDAO;
import com.vn.ntsc.backend.dao.user.BuzzUserDAO;
import com.vn.ntsc.backend.dao.user.PbAudioDAO;
import com.vn.ntsc.backend.dao.user.PbVideoDAO;
import com.vn.ntsc.backend.dao.user.VideoDAO;
import com.vn.ntsc.backend.entity.impl.video.Video;
import com.vn.ntsc.backend.entity.impl.video.FileInfo;
import com.vn.ntsc.backend.server.respond.IApiAdapter;
import com.vn.ntsc.backend.server.respond.Respond;
import com.vn.ntsc.backend.server.respond.impl.backend.image.reportedimageprocessor.ReportFlagChangeType;
import com.vn.ntsc.backend.server.respond.impl.backend.reportedprocessor.ReportFlagChange;
import com.vn.ntsc.backend.server.respond.result.EntityRespond;
import eazycommon.constant.Constant;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.ParamKey;
import eazycommon.exception.EazyException;
import eazycommon.util.Util;
import org.json.simple.JSONObject;

/**
 *
 * @author hoangnh
 */
public class ProcessReportedVideoApi implements IApiAdapter{

    @Override
    public Respond execute(JSONObject obj) {
        EntityRespond respond = new EntityRespond();
        try {
            String fileType = Util.getStringParam(obj, "file_type");
            
            switch(fileType){
                case "audio":
                    respond = processAudio(obj);
                    break;
                default: 
                    respond = processVideo(obj);
                    break;
            }
           
        } catch (Exception ex) {
            Util.addErrorLog(ex);

        }
        return respond;
    }
    
    public EntityRespond processVideo(JSONObject obj) throws EazyException {
        EntityRespond respond = new EntityRespond();
        String videoId = Util.getStringParam(obj, ParamKey.VIDEO_ID);
        Long type = Util.getLongParam(obj, ParamKey.TYPE);
        if (videoId == null || type == null) {
            return null;
        }
        FileInfo video = VideoDAO.getVideoInfor(videoId);

        if (video == null) {
            return new EntityRespond(ErrorCode.UNKNOWN_ERROR);
        }

        Integer reportFlag = Constant.REPORT_STATUS_FLAG.GOOD;
        if (video.reportFlag != null) {
            reportFlag = video.reportFlag;
        }
        ReportFlagChangeType updateFlag = ReportFlagChange.getFlagChange(reportFlag, type.intValue());
        VideoDAO.updateReportFlag(videoId, type.intValue());

        if (updateFlag == ReportFlagChangeType.GOOD_TO_NG || updateFlag == ReportFlagChangeType.WAITING_TO_NG) {
                //UserDAO.removeBuzz(image.userId);
            //UserDAO.removePbImage(image.userId);
            String buzzId = BuzzDetailDAO.getReportedBuzz(videoId);
            UserBuzzDAO.updateFlagUserBuzz(video.userId, buzzId, Constant.FLAG.OFF);
            BuzzDetailDAO.updateFlagBuzz(videoId, Constant.FLAG.OFF);
            PbVideoDAO.updateFlag(video.userId, videoId, Constant.FLAG.OFF);
        }
        if (updateFlag == ReportFlagChangeType.NG_TO_GOOD || updateFlag == ReportFlagChangeType.NG_TO_WAITING) {
                //UserDAO.addPbImage(image.userId);
            //UserDAO.addBuzz(image.userId);
            String buzzId = BuzzDetailDAO.getReportedBuzz(videoId);
            UserBuzzDAO.updateFlagUserBuzz(video.userId, buzzId, Constant.FLAG.ON);
            BuzzDetailDAO.updateFlagBuzz(videoId, Constant.FLAG.ON);
            PbVideoDAO.updateFlag(video.userId, videoId, Constant.FLAG.ON);
        }
        respond = new EntityRespond(ErrorCode.SUCCESS);
        return respond;
    }
    
    public EntityRespond processAudio(JSONObject obj) throws EazyException {
        EntityRespond respond = new EntityRespond();
        String audioId = Util.getStringParam(obj, ParamKey.VIDEO_ID);
        Long type = Util.getLongParam(obj, ParamKey.TYPE);
        if (audioId == null || type == null) {
            return null;
        }
        FileInfo audio = AudioDAO.getAudioInfor(audioId);

        if (audio == null) {
            return new EntityRespond(ErrorCode.UNKNOWN_ERROR);
        }

        Integer reportFlag = Constant.REPORT_STATUS_FLAG.GOOD;
        if (audio.reportFlag != null) {
            reportFlag = audio.reportFlag;
        }
        ReportFlagChangeType updateFlag = ReportFlagChange.getFlagChange(reportFlag, type.intValue());
        AudioDAO.updateReportFlag(audioId, type.intValue());

        if (updateFlag == ReportFlagChangeType.GOOD_TO_NG || updateFlag == ReportFlagChangeType.WAITING_TO_NG) {
                //UserDAO.removeBuzz(image.userId);
            //UserDAO.removePbImage(image.userId);
            String buzzId = BuzzDetailDAO.getReportedBuzz(audioId);
            UserBuzzDAO.updateFlagUserBuzz(audio.userId, buzzId, Constant.FLAG.OFF);
            BuzzDetailDAO.updateFlagBuzz(audioId, Constant.FLAG.OFF);
            PbAudioDAO.updateFlag(audio.userId, audioId, Constant.FLAG.OFF);
        }
        if (updateFlag == ReportFlagChangeType.NG_TO_GOOD || updateFlag == ReportFlagChangeType.NG_TO_WAITING) {
                //UserDAO.addPbImage(image.userId);
            //UserDAO.addBuzz(image.userId);
            String buzzId = BuzzDetailDAO.getReportedBuzz(audioId);
            UserBuzzDAO.updateFlagUserBuzz(audio.userId, buzzId, Constant.FLAG.ON);
            BuzzDetailDAO.updateFlagBuzz(audioId, Constant.FLAG.ON);
            PbAudioDAO.updateFlag(audio.userId, audioId, Constant.FLAG.ON);
        }
        respond = new EntityRespond(ErrorCode.SUCCESS);
        return respond;
    }
}
