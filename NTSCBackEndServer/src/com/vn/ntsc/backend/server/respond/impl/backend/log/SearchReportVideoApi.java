/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.backend.server.respond.impl.backend.log;

import com.vn.ntsc.backend.dao.log.ReportAudioDAO;
import com.vn.ntsc.backend.dao.log.ReportImageDAO;
import com.vn.ntsc.backend.dao.log.ReportVideoDAO;
import com.vn.ntsc.backend.dao.user.UserDAO;
import com.vn.ntsc.backend.entity.impl.datarespond.SizedListData;
import com.vn.ntsc.backend.entity.impl.log.LogFileReport;
import com.vn.ntsc.backend.entity.impl.log.LogImageReport;
import com.vn.ntsc.backend.server.common.LogProcess;
import com.vn.ntsc.backend.server.respond.IApiAdapter;
import com.vn.ntsc.backend.server.respond.Respond;
import com.vn.ntsc.backend.server.respond.result.EntityRespond;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.ParamKey;
import eazycommon.exception.EazyException;
import eazycommon.util.Util;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import org.json.simple.JSONObject;

/**
 *
 * @author hoangnh
 */
public class SearchReportVideoApi implements IApiAdapter{

    @Override
    public Respond execute(JSONObject obj) {
        EntityRespond respond = new EntityRespond();
        try {
            Long requestUserType = Util.getLongParam(obj, ParamKey.REQUEST_USER_TYPE);
            String requestEmail = Util.getStringParam(obj, ParamKey.REQUEST_EMAIL);
            if (requestUserType == null) {
                if (requestEmail != null) {
                    return null;
                }
            }
            String requestUId = Util.getStringParam(obj, ParamKey.REQUEST_ID);
            String requestCmCode = Util.getStringParam(obj, ParamKey.REQUEST_CM_CODE);
            List<String> listRequestId = UserDAO.getListUser(requestUserType, requestUId, requestEmail, requestCmCode);

            Long partnerUserType = Util.getLongParam(obj, ParamKey.PARTNER_USER_TYPE);
            String partnerEmail = Util.getStringParam(obj, ParamKey.PARTNER_EMAIL);
            if (partnerUserType == null) {
                if (partnerEmail != null) {
                    return null;
                }
            }
            String partnerUId = Util.getStringParam(obj, ParamKey.PARTNER_ID);
            String patCmCode = Util.getStringParam(obj, ParamKey.PARTNER_CM_CODE);
            List<String> listPartnerId = UserDAO.getListUser(partnerUserType, partnerUId, partnerEmail, patCmCode);

            String toTime = Util.getStringParam(obj, ParamKey.TO_TIME);
            String fromTime = Util.getStringParam(obj, ParamKey.FROM_TIME);

            String videoId = Util.getStringParam(obj, ParamKey.FILE_ID);
            //Long imageType = Util.getLongParam(obj, ParamKey.IMAGE_TYPE);
            Long reportType = Util.getLongParam(obj, ParamKey.REPORT_TYPE);
            Long privacy = Util.getLongParam(obj, "privacy");

            Long sort = Util.getLongParam(obj, ParamKey.SORT);
            Long order = Util.getLongParam(obj, ParamKey.ORDER);
            Long skip = Util.getLongParam(obj, ParamKey.SKIP);
            Long take = Util.getLongParam(obj, ParamKey.TAKE);
            if (sort == null || order == null) {
                return null;
            }

            Long csv = Util.getLongParam(obj, ParamKey.CSV);
            boolean isCsv = false;
            if (csv != null) {
                isCsv = true;
            }
            SizedListData data = new SizedListData();
            SizedListData dataVideo = ReportVideoDAO.listLog(videoId, listRequestId, listPartnerId, fromTime, toTime, reportType, sort, order, skip, take, isCsv,privacy);
            SizedListData dataAudio = ReportAudioDAO.listLog(videoId, listRequestId, listPartnerId, fromTime, toTime, reportType, sort, order, skip, take, isCsv,privacy);
            Util.addDebugLog("Datavideo==================================="+dataVideo.ll.size());
            Util.addDebugLog("dataAudio==================================="+dataAudio.ll.size());
            data.ll = sortMergeListVideoListAudio(dataAudio.ll,dataVideo.ll,order,skip,take,sort);
            data.total = data.ll.size();
            if (isCsv) {
                String api = Util.getStringParam(obj, ParamKey.API_NAME);
                return LogProcess.createCSV(api, csv, data.ll, null, new LogFileReport());
            }
            LogProcess.getTwoObjectInfor(data.ll);
            respond = new EntityRespond(ErrorCode.SUCCESS, data);
        } catch (EazyException ex) {
            Util.addErrorLog(ex);
            respond = new EntityRespond(ex.getErrorCode());
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
        return respond;
    }

    private List sortMergeListVideoListAudio(List llAudio, List llVideo, Long order,Long skip, Long take, Long sort) {
        List merge = new ArrayList();
        Long timeAudio = 0L;
        Long timeVideo = 0L;
        Integer takeVideo = 0;
        Integer takeAudio = 0;
        
        Long timeVideoReport = 0L;
        Long timeAudioReport = 0L;
        if (llAudio.size() == 0) {
            return llVideo;
        }
        if (llVideo.size() == 0) {
            return llAudio;
        }
        if(order == 1){
            
            for(Object audio : llAudio){
                LogFileReport reportAudio = (LogFileReport)audio;
                
                if(!reportAudio.time.isEmpty()){
                    timeAudio = Long.parseLong(reportAudio.time);
                }
                for(Object video : llVideo){
                    LogFileReport reportVideo = (LogFileReport)video;
                    if(!reportVideo.time.isEmpty()){
                        timeVideo = Long.parseLong(reportVideo.time);
                    }
                   
                    if(timeAudio < timeVideo){
                        merge.add(reportAudio);
                        timeAudioReport = timeAudio;
                        takeAudio ++;
                        if(takeAudio == llAudio.size()){
                             for(Object vid : llVideo){
                                 LogFileReport reportVid = (LogFileReport)vid;
                                 if(!merge.contains(reportVid)){
                                     merge.add(reportVid);
                                 }
                             }   
                             if (take.intValue() + skip.intValue() > merge.size()) {
                                return merge.subList(skip.intValue(), merge.size());
                            }
                            if (skip.intValue() >= merge.size()) {
                                merge.clear();
                                return merge;
                            }
                            return merge.subList(skip.intValue(), take.intValue() + skip.intValue());
                        }else{
                            break;
                        }
                    }else{
                        if (timeAudioReport < timeVideo || timeAudioReport == 0L) {
                            merge.add(reportVideo);
                            timeVideoReport = timeVideo;
                            takeVideo++;
                            if (takeVideo == llVideo.size() ) {
                                for (Object audi : llAudio) {
                                    LogFileReport reportAudi = (LogFileReport) audi;
                                    if (!merge.contains(reportAudi)) {
                                        merge.add(reportAudi);
                                    }
                                }
                                if(take.intValue() + skip.intValue() > merge.size()){
                                    return merge.subList(skip.intValue(),merge.size());
                                }
                                if(skip.intValue() >= merge.size()){
                                    merge.clear();
                                    return  merge;
                                }
                                return merge.subList(skip.intValue(), take.intValue() + skip.intValue());
                            }
                        }
                    }
                    
                }
            }
        }else{
             for(Object audio : llAudio){
                LogFileReport reportAudio = (LogFileReport)audio;
                
                if(!reportAudio.time.isEmpty()){
                    timeAudio = Long.parseLong(reportAudio.time);
                }
                for(Object video : llVideo){
                    LogFileReport reportVideo = (LogFileReport)video;
                    if(!reportVideo.time.isEmpty()){
                        timeVideo = Long.parseLong(reportVideo.time);
                    }
                   
                    if(timeAudio > timeVideo){
                        merge.add(reportAudio);
                        timeAudioReport = timeAudio;
                        takeAudio ++;
                        if(takeAudio == llAudio.size()){
                             for(Object vid : llVideo){
                                 LogFileReport reportVid = (LogFileReport)vid;
                                 if(!merge.contains(reportVid)){
                                     merge.add(reportVid);
                                 }
                             }   
                             if (take.intValue() + skip.intValue() > merge.size()) {
                                return merge.subList(skip.intValue(), merge.size());
                            }
                            if (skip.intValue() >= merge.size()) {
                                merge.clear();
                                return merge;
                            }
                            return merge.subList(skip.intValue(), take.intValue() + skip.intValue());
                        }else{
                            break;
                        }
                    }else{
                        if (timeAudioReport > timeVideo || timeAudioReport == 0L) {
                            merge.add(reportVideo);
                            timeVideoReport = timeVideo;
                            takeVideo++;
                            if (takeVideo == llVideo.size() + 1) {
                                for (Object audi : llAudio) {
                                    LogFileReport reportAudi = (LogFileReport) audi;
                                    if (!merge.contains(reportAudi)) {
                                        merge.add(reportAudi);
                                    }
                                }
                                if (take.intValue() + skip.intValue() > merge.size()) {
                                    return merge.subList(skip.intValue(), merge.size());
                                }
                                if (skip.intValue() >= merge.size()) {
                                    merge.clear();
                                    return merge;
                                }
                                return merge.subList(skip.intValue(), take.intValue() + skip.intValue());
                            }
                        }
                    }
                    
                }
            }
        }
        if (take.intValue() + skip.intValue() > merge.size()) {
            return merge.subList(skip.intValue(), merge.size());
        }
        if (skip.intValue() >= merge.size()) {
            merge.clear();
            return merge;
        }
        return merge.subList(skip.intValue(), take.intValue() + skip.intValue());
    }
    
}
