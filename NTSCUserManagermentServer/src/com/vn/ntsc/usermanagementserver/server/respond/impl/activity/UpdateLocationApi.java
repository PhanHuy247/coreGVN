/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.usermanagementserver.server.respond.impl.activity;

import com.vn.ntsc.usermanagementserver.server.respond.Respond;
import com.vn.ntsc.usermanagementserver.server.respond.IApiAdapter;
import eazycommon.util.Util;
import java.util.Date;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.ListPosition;
import eazycommon.exception.EazyException;
import org.json.simple.JSONObject;
import eazycommon.constant.ParamKey;
import eazycommon.constant.StatePosition;
import com.vn.ntsc.usermanagementserver.dao.impl.UserActivityDAO;
import com.vn.ntsc.usermanagementserver.dao.impl.UserDAO;

/**
 *
 * @author RuAc0n
 */
public class UpdateLocationApi implements IApiAdapter {

    @Override
    public Respond execute(JSONObject obj, Date time) {
        Respond result = new Respond();
        try {
            String userId = Util.getStringParam(obj, ParamKey.USER_ID);
            double longitude = Util.getDoubleParam(obj, ParamKey.LONGITUDE);
            double latitude = Util.getDoubleParam(obj, ParamKey.LATITUDE);

            // LongLT 11 Oct2016 ///////////////////////////  #4783
            if (longitude == 0 && latitude == 0) {
                ListPosition lp = ListPosition.getInstance();
                StatePosition sp = null;
                sp = lp.getStatePosistion(UserDAO.getUserInfor(userId.toString()).region.intValue());
                if (sp != null) {
                    longitude = sp.longtitude;
                    latitude = sp.latitude;
                }
            }

            boolean isUpdate = UserActivityDAO.updatedLocation(longitude, latitude, userId);
            if (isUpdate) {
                result.code = ErrorCode.SUCCESS;
            }
        } catch (EazyException ex) {
            Util.addErrorLog(ex);
            result.code = ex.getErrorCode();
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
        return result;
    }

}
