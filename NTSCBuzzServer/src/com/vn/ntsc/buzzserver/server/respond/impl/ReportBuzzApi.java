/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.buzzserver.server.respond.impl;

import com.vn.ntsc.buzzserver.dao.impl.ReviewingCommentDAO;
import com.vn.ntsc.buzzserver.dao.impl.UserBuzzDAO;
import com.vn.ntsc.buzzserver.server.respond.IApiAdapter;
import com.vn.ntsc.buzzserver.server.respond.Respond;
import eazycommon.exception.EazyException;
import eazycommon.util.Util;
import org.json.simple.JSONObject;
import eazycommon.constant.Constant;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.ParamKey;
import com.vn.ntsc.buzzserver.server.respond.result.EntityRespond;

/**
 *
 * @author RuAc0n
 */
public class ReportBuzzApi implements IApiAdapter {

    @Override
    public Respond execute(JSONObject obj, long time) {
        EntityRespond result = new EntityRespond();
        try {
            String userId = Util.getStringParam(obj, ParamKey.USER_ID);
            String buzzId = Util.getStringParam(obj, ParamKey.BUZZ_ID);
            if (buzzId == null || buzzId.isEmpty()) {
                result.code = ErrorCode.WRONG_DATA_FORMAT;
                return result;
            }
//            BuzzDetailDAO.updateApprovedFlag(buzzId, Constant.NO);
            UserBuzzDAO.updateApproveFlag(userId, buzzId, Constant.REVIEW_STATUS_FLAG.DENIED);
            ReviewingCommentDAO.updateAppearFlagByBuzzId(buzzId, Constant.FLAG.OFF);
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
