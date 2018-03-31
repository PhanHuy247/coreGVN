/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.backend.server.respond.impl.backend.video;

import com.vn.ntsc.backend.dao.user.AudioDAO;
import com.vn.ntsc.backend.dao.user.UserDAO;
import com.vn.ntsc.backend.dao.user.VideoDAO;
import com.vn.ntsc.backend.entity.IEntity;
import com.vn.ntsc.backend.entity.impl.datarespond.SizedListData;
import com.vn.ntsc.backend.entity.impl.log.LogFileReport;
import com.vn.ntsc.backend.entity.impl.video.Video;
import com.vn.ntsc.backend.server.respond.IApiAdapter;
import com.vn.ntsc.backend.server.respond.Respond;
import com.vn.ntsc.backend.server.respond.result.EntityRespond;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.ParamKey;
import eazycommon.exception.EazyException;
import eazycommon.util.Util;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.json.simple.JSONObject;

/**
 *
 * @author hoangnh
 */
public class ListReportedVideoApi implements IApiAdapter{

    @Override
    public Respond execute(JSONObject obj) {
        EntityRespond respond = new EntityRespond();
        try {
            Long type = Util.getLongParam(obj, ParamKey.TYPE);
            Long order = Util.getLongParam(obj, ParamKey.ORDER);
            Long sort = Util.getLongParam(obj, ParamKey.SORT);
            Long skip = Util.getLongParam(obj, ParamKey.SKIP);
            Long take = Util.getLongParam(obj, ParamKey.TAKE);
            SizedListData data = new SizedListData();
            SizedListData dataVideo = VideoDAO.searchReportedVideo(type, sort, order, skip.intValue(), take.intValue());
            SizedListData dataAudio = AudioDAO.searchReportedAudioWithVideo(type, sort, order, skip.intValue(), take.intValue());
            Util.addDebugLog("datavideo=================================="+dataVideo.ll.size());
            Util.addDebugLog("dataAudio=================================="+dataAudio.ll.size());
            data.ll = sortReportMergeFile(dataVideo.ll,dataAudio.ll,sort,order,skip,take);
            data.total = data.ll.size();
            List<IEntity> ll = data.ll;
            List<String> list = new ArrayList<>();
            for (IEntity l : ll) {
                list.add(((Video) l).userId);
            }
            Map<String, String> mapName = UserDAO.getUserName(list);
            for (IEntity l : ll) {
                String userName = mapName.get(((Video) l).userId);
                ((Video) l).username = userName;
            }
            data.ll = ll;
            respond = new EntityRespond(ErrorCode.SUCCESS, data);
            
        } catch (EazyException ex) {
            Util.addErrorLog(ex);
            respond = new EntityRespond(ex.getErrorCode());

        } catch (Exception ex) {
            Util.addErrorLog(ex);

        }
        return respond;
    }

    private List sortReportMergeFile(List llVideo, List llAudio,Long sort, Long order, Long skip, Long take) {
        List merge = new ArrayList();
        Long timeAudio = 0L;
        Long timeVideo = 0L;
       
        Integer takeVideo = 0;
        Integer takeAudio = 0;
        
        Long timeVideoReport = 0L;
        Long timeAudioReport = 0L;
        
        Integer numberAudioReport = 0;
        Integer numberVideoReport = 0;
        if (llVideo.size() == 0) {
            return llAudio;
        }
        if (llAudio.size() == 0) {
            return llVideo;
        }
        if(order == 1){
            
            for (Object audio : llAudio) {
              
                Video reportAudio = (Video) audio;
                if (!reportAudio.reportTimeStr.isEmpty()) {
                    timeAudio = Long.parseLong(reportAudio.reportTimeStr);
                }

                for (Object video : llVideo) {
                    Video reportVideo = (Video) video;
                    if (sort == 1) {
                        if (!reportVideo.reportTimeStr.isEmpty()) {
                            timeVideo = Long.parseLong(reportVideo.reportTimeStr);
                        }
                        if (timeAudio < timeVideo) {
                            merge.add(reportAudio);
                            timeAudioReport = timeAudio;
                            takeAudio++;
                            if (takeAudio == llAudio.size()) {
                                for (Object vid : llVideo) {
                                    Video reportVid = (Video) vid;
                                    if (!merge.contains(reportVid)) {
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
                        } else {
                            if (timeAudioReport < timeVideo || timeAudioReport == 0L ) {
                                merge.add(reportVideo);
                                timeVideoReport = timeVideo;
                                takeVideo++;
                                if (takeVideo == llVideo.size()) {
                                    for (Object audi : llAudio) {
                                        Video reportAudi = (Video) audi;
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
                    }else{
                        if(sort == 2){
                            if(reportAudio.reportNumber < reportVideo.reportNumber){
                                merge.add(reportAudio);
                                numberAudioReport = reportAudio.reportNumber;
                                takeAudio++;
                                if (takeAudio == llAudio.size()) {
                                    for (Object vid : llVideo) {
                                        Video reportVid = (Video) vid;
                                        if (!merge.contains(reportVid)) {
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
                            }else {
                                if (numberAudioReport < reportVideo.reportNumber || timeAudioReport == 0L) {
                                    merge.add(reportVideo);
                                    timeVideoReport = timeVideo;
                                    takeVideo++;
                                    if (takeVideo == llVideo.size()) {
                                        for (Object audi : llAudio) {
                                            Video reportAudi = (Video) audi;
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
            }
        }else{
            for (Object audio : llAudio) {
              
                Video reportAudio = (Video) audio;
                if (!reportAudio.reportTimeStr.isEmpty()) {
                    timeAudio = Long.parseLong(reportAudio.reportTimeStr);
                }

                for (Object video : llVideo) {
                    Video reportVideo = (Video) video;
                    if (sort == 1) {
                        if (!reportVideo.reportTimeStr.isEmpty()) {
                            timeVideo = Long.parseLong(reportVideo.reportTimeStr);
                        }
                        if (timeAudio > timeVideo) {
                            merge.add(reportAudio);
                            timeAudioReport = timeAudio;
                            takeAudio++;
                            if (takeAudio == llAudio.size()) {
                                for (Object vid : llVideo) {
                                    Video reportVid = (Video) vid;
                                    if (!merge.contains(reportVid)) {
                                        merge.add(reportVid);
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
                            }else{
                                break;
                            }
                        } else {
                            if (timeAudioReport > timeVideo || timeAudioReport == 0L) {
                                merge.add(reportVideo);
                                timeVideoReport = timeVideo;
                                takeVideo++;
                                if (takeVideo == llVideo.size()) {
                                    for (Object audi : llAudio) {
                                        Video reportAudi = (Video) audi;
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
                    }else{
                        if(sort == 2){
                            if(reportAudio.reportNumber > reportVideo.reportNumber){
                                merge.add(reportAudio);
                                numberAudioReport = reportAudio.reportNumber;
                                takeAudio++;
                                if (takeAudio == llAudio.size()) {
                                    for (Object vid : llVideo) {
                                        Video reportVid = (Video) vid;
                                        if (!merge.contains(reportVid)) {
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
                            }else {
                                if (numberAudioReport > reportVideo.reportNumber || numberAudioReport == 0) {
                                    merge.add(reportVideo);
                                    timeVideoReport = timeVideo;
                                    takeVideo++;
                                    if (takeVideo == llVideo.size()) {
                                        for (Object audi : llAudio) {
                                            Video reportAudi = (Video) audi;
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
