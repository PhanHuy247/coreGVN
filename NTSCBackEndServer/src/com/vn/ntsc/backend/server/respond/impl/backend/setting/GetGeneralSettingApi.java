/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.backend.server.respond.impl.backend.setting;

import org.json.simple.JSONObject;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.ParamKey;
import eazycommon.exception.EazyException;
import com.vn.ntsc.backend.dao.setting.CommunicationSettingDAO;
import com.vn.ntsc.backend.dao.setting.DistanceSettingDAO;
import com.vn.ntsc.backend.dao.setting.OtherSettingDAO;
import com.vn.ntsc.backend.dao.setting.PointSettingDAO;
import com.vn.ntsc.backend.entity.IEntity;
import com.vn.ntsc.backend.entity.impl.datarespond.PointSettingData;
import com.vn.ntsc.backend.server.respond.IApiAdapter;
import com.vn.ntsc.backend.server.respond.Respond;
import com.vn.ntsc.backend.server.respond.result.EntityRespond;
import eazycommon.util.Util;
import com.vn.ntsc.backend.dao.setting.ConnectionPointSettingDAO;

/**
 *
 * @author RuAc0n
 */
public class GetGeneralSettingApi implements IApiAdapter {

    @Override
    public Respond execute(JSONObject obj) {
        EntityRespond respond = new EntityRespond();
        try {
            Long type = Util.getLongParam(obj, ParamKey.TYPE);
            IEntity entity = null;
            if (type == 1) {
                JSONObject json = PointSettingDAO.get();
                entity = new PointSettingData(json);
            } else if (type == 2) {
                entity = DistanceSettingDAO.get();
            } else if (type == 4) {
                entity = OtherSettingDAO.getOtherSeting();
            } else if (type == 5) {
                JSONObject json = CommunicationSettingDAO.get();
                entity = new PointSettingData(json);
            } else if (type == 6) {
                JSONObject json = ConnectionPointSettingDAO.get();
                entity = new PointSettingData(json);
            } else if (type == 7) {
                entity = OtherSettingDAO.getVersionSeting();
            }
            respond = new EntityRespond(ErrorCode.SUCCESS, entity);
        } catch (EazyException ex) {
            Util.addErrorLog(ex);
            respond = new EntityRespond(ex.getErrorCode());
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
        return respond;
    }

}
