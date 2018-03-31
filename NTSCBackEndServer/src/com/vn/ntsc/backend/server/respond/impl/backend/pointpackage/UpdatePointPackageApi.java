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
import com.vn.ntsc.backend.entity.impl.pointpackage.PointPacket;
import com.vn.ntsc.backend.server.common.Validator;
import com.vn.ntsc.backend.server.respond.IApiAdapter;
import com.vn.ntsc.backend.server.respond.Respond;
import com.vn.ntsc.backend.server.respond.result.EntityRespond;

/**
 *
 * @author RuAc0n
 */
public class UpdatePointPackageApi implements IApiAdapter {
    private static final Map<String, Integer> keys = new TreeMap<String, Integer>();

    static {
        keys.put(ParamKey.PRODUCTION_ID, 4);
        keys.put(ParamKey.PRICE, 5);
        keys.put(ParamKey.POINT, 6);
        keys.put(ParamKey.DESCRIPTION, 7);
    }
    @Override
    public Respond execute(JSONObject obj) {
        Respond respond = new Respond();
        try {
            String id = Util.getStringParam(obj, ParamKey.ID);
            if (id == null || id.isEmpty()) {
                return null;
            }
            String des = Util.getStringParam(obj, ParamKey.DESCRIPTION);
            String productionId = Util.getStringParam(obj, ParamKey.PRODUCTION_ID);
            Long point = Util.getLongParam(obj, ParamKey.POINT);
            Double price = Util.getDoubleParam(obj, ParamKey.PRICE);
            
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
                if(packets.size() > 2){
                    respond = new EntityRespond(4);
                    return respond; 
                }else{
                    PointPacket packet = packets.get(0);
                    if(!packet.id.equals(id)){
                        respond = new EntityRespond(4);
                        return respond; 
                    }
                }
            }
            
            PointPacketDAO.update(id, price, point.intValue(), des, productionId);
            respond = new Respond(ErrorCode.SUCCESS);
        } catch (EazyException ex) {
            Util.addErrorLog(ex);
            respond = new Respond(ex.getErrorCode());
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
        return respond;
    }

}
