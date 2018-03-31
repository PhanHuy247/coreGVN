/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.backend.entity.impl.video;

import com.vn.ntsc.backend.dao.file.FileDAO;
import com.vn.ntsc.backend.dao.user.ImageDAO;
import com.vn.ntsc.backend.entity.IEntity;
import com.vn.ntsc.backend.entity.impl.image.Image;
import com.vn.ntsc.backend.entity.impl.image.ReviewImage;
import com.vn.ntsc.backend.server.respond.IApiAdapter;
import com.vn.ntsc.backend.server.respond.Respond;
import com.vn.ntsc.backend.server.respond.impl.backend.image.imagereviewer.ChangeStatusActionManager;
import com.vn.ntsc.backend.server.respond.impl.backend.image.imagereviewer.ImageReviewer;
import com.vn.ntsc.backend.server.respond.impl.backend.image.imagereviewer.ImageStatusChangeType;
import com.vn.ntsc.backend.server.respond.impl.backend.image.imagereviewer.StatusChangeMangager;
import com.vn.ntsc.backend.server.respond.result.EntityRespond;
import eazycommon.constant.Constant;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.ParamKey;
import eazycommon.util.Util;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.json.simple.JSONObject;

/**
 *
 * @author Phan Huy
 */
public class ReviewVideoApi implements IApiAdapter{

    @Override
    public Respond execute(JSONObject obj) {
         EntityRespond respond = new EntityRespond();
        try {
            String videoId = Util.getStringParam(obj, ParamKey.VIDEO_ID);
            Long type = Util.getLongParam(obj, ParamKey.VIDEO_STATUS);
            Date time = Util.getGMTTime();
            if (videoId == null || type == null) {
                return null;
            }
            FileDAO.UpdateTimeReviewVideo(videoId, time);
            Video video = FileDAO.getVideoInfo(videoId);
            if (video == null) {
                return new EntityRespond(ErrorCode.UNKNOWN_ERROR);
            }
            FileDAO.InsertReviewVideo(videoId, type);
            respond = new EntityRespond(ErrorCode.SUCCESS, video);
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
        return respond;
    }
}
