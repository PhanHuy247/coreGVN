/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.usermanagementserver.server.respond.impl.backend;

import com.vn.ntsc.usermanagementserver.server.respond.Respond;
import com.vn.ntsc.usermanagementserver.server.respond.IApiAdapter;
import eazycommon.util.Util;
import java.util.ArrayList;
import java.util.Date;
import eazycommon.constant.ErrorCode;
import org.json.simple.JSONObject;
import eazycommon.constant.ParamKey;
import com.vn.ntsc.usermanagementserver.setting.Setting;

/**
 *
 * @author RuAc0n
 */
public class UpdateDistanceSettingApi implements IApiAdapter {

    @Override
    public Respond execute(JSONObject obj, Date time) {
        Respond result = new Respond();
        try {
            Double localBuzz = Util.getDoubleParam(obj, "local_buzz");
            Setting.LOCAL_DISTANCE = localBuzz;
            ArrayList<Double> distance = new ArrayList<>();
            Double near = Util.getDoubleParam(obj, "near");
            distance.add(near);
            Double city = Util.getDoubleParam(obj, "city");
            distance.add(city);
            Double country = Util.getDoubleParam(obj, "country");
            distance.add(country);
            Setting.DISTANCE = distance;
            result.code = ErrorCode.SUCCESS;
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
        return result;
    }

}
