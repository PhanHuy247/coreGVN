/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.backend.server.respond.impl.backend.pointpackage;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.ParamKey;
import eazycommon.exception.EazyException;
import eazycommon.util.Util;
import org.json.simple.JSONObject;
import com.vn.ntsc.backend.dao.pointpackage.PointPacketDAO;
import com.vn.ntsc.backend.entity.impl.datarespond.InsertData;
import com.vn.ntsc.backend.entity.impl.pointpackage.PointPacket;
import com.vn.ntsc.backend.server.common.Validator;
import com.vn.ntsc.backend.server.respond.IApiAdapter;
import com.vn.ntsc.backend.server.respond.Respond;
import com.vn.ntsc.backend.server.respond.result.EntityRespond;

/**
 *
 * @author RuAc0n
 */
public class InsertPointPackageApi implements IApiAdapter {

    private static final Map<String, Integer> keys = new TreeMap<>();

    static {
        keys.put(ParamKey.PRODUCTION_ID, 4);
        keys.put(ParamKey.PRICE, 5);
        keys.put(ParamKey.POINT, 6);
        keys.put(ParamKey.DESCRIPTION, 7);
    }

    @Override
    public Respond execute(JSONObject obj) {
        EntityRespond respond = new EntityRespond();
        try {
            String des = Util.getStringParam(obj, ParamKey.DESCRIPTION);
            String productionId = Util.getStringParam(obj, ParamKey.PRODUCTION_ID);
            Long point = Util.getLongParam(obj, ParamKey.POINT);
            Double price = Util.getDoubleParam(obj, ParamKey.PRICE);
            Long type = Util.getLongParam(obj, ParamKey.TYPE);
            String applicationId = Util.getLongParam(obj, "application").toString();
            Util.addDebugLog("applicationId " + applicationId);
            // String applicationId = ApplicationDAO.getIdByUniqueName(applicationName);
            if (applicationId == null) {
                return null;
            }
            int error = Validator.validate(obj, keys);
            if (error != ErrorCode.SUCCESS) {
                respond = new EntityRespond(error);
                return respond;
            }
            
            if(point <= 0){
                respond = new EntityRespond(keys.get(ParamKey.POINT));
                return respond;
            }
            
            if(price <= 0){
                respond = new EntityRespond(keys.get(ParamKey.PRICE));
                return respond;
            }
            
            List<PointPacket> packets = PointPacketDAO.getPointPacketByProductId(productionId);
            if(packets != null && packets.size() > 0){
                respond = new EntityRespond(4);
                return respond;
            }
            
            String id = PointPacketDAO.insertMultiapp(applicationId, price, point.intValue(), des, type.intValue(), productionId);
            respond = new EntityRespond(ErrorCode.SUCCESS, new InsertData(id));
        } catch (EazyException ex) {
            Util.addErrorLog(ex);
            respond = new EntityRespond(ex.getErrorCode());
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
        return respond;
    }

}
