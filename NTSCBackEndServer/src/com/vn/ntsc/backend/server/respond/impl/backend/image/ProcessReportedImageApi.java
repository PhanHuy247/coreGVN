/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.backend.server.respond.impl.backend.image;

import com.vn.ntsc.backend.dao.album.AlbumImageDAO;
import java.lang.reflect.Method;
import eazycommon.constant.Constant;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.ParamKey;
import eazycommon.util.Util;
import org.json.simple.JSONObject;
import com.vn.ntsc.backend.dao.user.ImageDAO;
import com.vn.ntsc.backend.entity.impl.image.Image;
import com.vn.ntsc.backend.entity.impl.image.ReviewImage;
import com.vn.ntsc.backend.server.Setting;
import com.vn.ntsc.backend.server.respond.IApiAdapter;
import com.vn.ntsc.backend.server.respond.Respond;
import com.vn.ntsc.backend.server.respond.impl.backend.image.reportedimageprocessor.ChangeReportFlagManager;
import com.vn.ntsc.backend.server.respond.impl.backend.image.reportedimageprocessor.ReportFlagChangeManager;
import com.vn.ntsc.backend.server.respond.impl.backend.image.reportedimageprocessor.ReportFlagChangeType;
import com.vn.ntsc.backend.server.respond.impl.backend.image.reportedimageprocessor.ReportedImageProcessor;
import com.vn.ntsc.backend.server.respond.result.EntityRespond;

/**
 *
 * @author RuAc0n
 */
public class ProcessReportedImageApi implements IApiAdapter {

    @Override
    public Respond execute(JSONObject obj) {
        EntityRespond respond = new EntityRespond();
        try {
            String imageId = Util.getStringParam(obj, ParamKey.IMAGE_ID);
//            String imageId = null;
            Long type = Util.getLongParam(obj, ParamKey.TYPE);
//            Long type = (long) 1;
            if (imageId == null || type == null) {
                return null;
            }
            Image image = ImageDAO.getImageInfor(imageId);
            
            if (image == null) {
                return new EntityRespond(ErrorCode.UNKNOWN_ERROR);
            }
            ReviewImage ri = new ReviewImage();
            
            Boolean isAlbumImage = AlbumImageDAO.isAlbumImage(imageId);
            if(isAlbumImage){
                Integer reportFlag = Constant.REPORT_STATUS_FLAG.GOOD;
                if(image.reportFlag != null){
                    reportFlag = image.reportFlag;
                }
                ReportFlagChangeType flagChange = ReportFlagChangeManager.getFlagChange(reportFlag, type.intValue());
                ImageDAO.updateReport(imageId, type.intValue());
                if (flagChange == ReportFlagChangeType.GOOD_TO_NG || flagChange == ReportFlagChangeType.WAITING_TO_NG) {
                    Util.addDebugLog("flagChange===========1");
                    AlbumImageDAO.updateFlag(imageId, Constant.FLAG.OFF);
                }
                if (flagChange == ReportFlagChangeType.NG_TO_GOOD || flagChange == ReportFlagChangeType.WAITING_TO_GOOD) {
                    Util.addDebugLog("flagChange===========2");
                    AlbumImageDAO.updateFlag(imageId, Constant.FLAG.ON);
                }
                if (flagChange == ReportFlagChangeType.GOOD_TO_WAITING || flagChange == ReportFlagChangeType.NG_TO_WAITING){
                    Util.addDebugLog("flagChange===========3");
                    Util.addDebugLog("Setting.AUTO_HIDE_REPORTED_IMAGE==========="+Setting.AUTO_HIDE_REPORTED_IMAGE);
                    if(Setting.AUTO_HIDE_REPORTED_IMAGE == Constant.FLAG.ON){
                        AlbumImageDAO.updateFlag(imageId, Constant.FLAG.OFF);
                    }else{
                        AlbumImageDAO.updateFlag(imageId, Constant.FLAG.ON);
                    }
                }
            }else{
                ri.userId = image.userId;
                ri.imageId = imageId;
                if(image.flag == Constant.FLAG.ON){
                    Integer reportFlag = Constant.REPORT_STATUS_FLAG.GOOD;
                    if(image.reportFlag != null){
                        reportFlag = image.reportFlag;
                    }
                    ReportFlagChangeType flagChange = ReportFlagChangeManager.getFlagChange(reportFlag, type.intValue());
                    String method = ChangeReportFlagManager.getMethod(image.imageStatus, flagChange);
                    if(method != null){
                        ReportedImageProcessor rip = new ReportedImageProcessor();
                        Method met = rip.getClass().getMethod(method, Image.class, ReviewImage.class);
                        met.invoke(rip, image, ri);
                    }
                }
            }
            
            
            
            respond = new EntityRespond(ErrorCode.SUCCESS, ri);
        } catch (Exception ex) {
            Util.addErrorLog(ex);

        }
        return respond;
    }
}
