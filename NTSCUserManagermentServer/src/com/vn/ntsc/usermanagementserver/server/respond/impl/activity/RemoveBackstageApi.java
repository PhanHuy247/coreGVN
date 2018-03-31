/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.usermanagementserver.server.respond.impl.activity;

import com.vn.ntsc.usermanagementserver.server.respond.Respond;
import com.vn.ntsc.usermanagementserver.server.respond.IApiAdapter;
import com.vn.ntsc.usermanagementserver.dao.impl.BackstageDAO;
import com.vn.ntsc.usermanagementserver.dao.impl.UserDAO;
import com.vn.ntsc.usermanagementserver.dao.impl.ImageDAO;
import eazycommon.util.Util;
import java.util.Date;
import eazycommon.constant.Constant;
import eazycommon.constant.ErrorCode;
import eazycommon.exception.EazyException;
import org.json.simple.JSONObject;
import eazycommon.constant.ParamKey;

/**
 *
 * @author RuAc0n
 */
public class RemoveBackstageApi implements IApiAdapter {

    @Override
    public Respond execute(JSONObject obj, Date time) {
        Respond result = new Respond();
        try {
            String userId = Util.getStringParam(obj, ParamKey.USER_ID);
            String imageId = Util.getStringParam(obj, ParamKey.IMAGE_ID);
            if (BackstageDAO.checkBackStageExist(userId, imageId)) {
                //1. remove to backstage image collection
                BackstageDAO.removeBackStage(userId, imageId);
                //2.update in user collection
                UserDAO.removeBackstage(userId);
                //3. remove in image collection
                ImageDAO.updateFlag(imageId, Constant.FLAG.OFF);
            }
            //4. set result
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
