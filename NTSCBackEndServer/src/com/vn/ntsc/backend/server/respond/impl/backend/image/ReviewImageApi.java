/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.backend.server.respond.impl.backend.image;

import java.lang.reflect.Method;
import eazycommon.constant.Constant;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.ParamKey;
import eazycommon.constant.mongokey.SettingdbKey;
import eazycommon.util.DateFormat;
import eazycommon.util.Util;
import java.util.Date;
import org.json.simple.JSONObject;
import com.vn.ntsc.backend.dao.admin.AdminDAO;
import com.vn.ntsc.backend.dao.user.ImageDAO;
import com.vn.ntsc.backend.entity.IEntity;
import com.vn.ntsc.backend.entity.impl.buzz.CommentDetail;
import com.vn.ntsc.backend.entity.impl.image.Image;
import com.vn.ntsc.backend.entity.impl.image.ReviewImage;
import com.vn.ntsc.backend.server.respond.IApiAdapter;
import com.vn.ntsc.backend.server.respond.Respond;
import com.vn.ntsc.backend.server.respond.impl.backend.image.imagereviewer.ChangeStatusActionManager;
import com.vn.ntsc.backend.server.respond.impl.backend.image.imagereviewer.ImageReviewer;
import com.vn.ntsc.backend.server.respond.impl.backend.image.imagereviewer.ImageStatusChangeType;
import com.vn.ntsc.backend.server.respond.impl.backend.image.imagereviewer.StatusChangeMangager;
import com.vn.ntsc.backend.server.respond.result.EntityRespond;

/**
 *
 * @author RuAc0n
 */
public class ReviewImageApi implements IApiAdapter {

    @Override
    public Respond execute(JSONObject obj) {
        EntityRespond respond = new EntityRespond();
        try {
            String imageId = Util.getStringParam(obj, ParamKey.IMAGE_ID);
            Long type = Util.getLongParam(obj, ParamKey.TYPE);
            //String userDeny = Util.getStringParam(obj, ParamKey.USER_ID);
            String userDeny = Util.getStringParam(obj, "admin_name");
            Long flag_func = Util.getLongParam(obj, "flag_func");
            
            if (imageId == null || type == null) {
                return null;
            }
            Image image = ImageDAO.getImageInfor(imageId);
            if (image == null) {
                return new EntityRespond(ErrorCode.UNKNOWN_ERROR);
            }
            
             IEntity userAdmin = null;
             //String userAdminName = "";
//             if (userDeny!=null){
//                 userAdmin = AdminDAO.getDetail(userDeny);
//             }
//             userAdminName = userAdmin.toJsonObject().get(SettingdbKey.ADMINISTRATOR.NAME).toString();
             Util.addDebugLog("==========userAdminName===="+userDeny);       
            ImageDAO.updateUserDeny(imageId, userDeny , Util.currentTime(), type);
            
            
            ReviewImage ri = new ReviewImage();
            ri.userId = image.userId;
            ri.imageId = imageId;
//            if (type != -1) {
//                userDeny = "";
//            }
            //ri.userDeny = userDeny;
            ri.userDeny = userDeny;
            
            if(image.flag == Constant.FLAG.ON){
                ImageStatusChangeType statusChange = StatusChangeMangager.getStatusChange(image.imageStatus, type.intValue());
                Integer reportFlag = Constant.REPORT_STATUS_FLAG.GOOD;
                if(image.reportFlag != null){
                    reportFlag = image.reportFlag;
                }
                String method = ChangeStatusActionManager.getMethod(reportFlag, statusChange);
                if(method != null){
                    if (type == null && type != -1) {
                        userDeny = "";
                    }
                    ImageReviewer ir = new ImageReviewer();
                    Method met = ir.getClass().getMethod(method, Image.class, ReviewImage.class);
                    met.invoke(ir, image, ri);
//                    if (type != null) {
//                        ImageDAO.updateUserDeny(imageId, userDeny, Util.currentTime(), type);
//                    }
                }
            }
            respond = new EntityRespond(ErrorCode.SUCCESS, ri);
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
        return respond;
    }
}
