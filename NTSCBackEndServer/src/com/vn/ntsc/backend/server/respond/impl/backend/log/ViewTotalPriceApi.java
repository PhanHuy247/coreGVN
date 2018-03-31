/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.backend.server.respond.impl.backend.log;

import eazycommon.constant.ErrorCode;
import eazycommon.constant.ParamKey;
import eazycommon.exception.EazyException;
import eazycommon.util.Util;
import org.json.simple.JSONObject;
import com.vn.ntsc.backend.dao.log.TransactionLogDAO;
import com.vn.ntsc.backend.entity.impl.datarespond.SizedListData;
import com.vn.ntsc.backend.server.respond.IApiAdapter;
import com.vn.ntsc.backend.server.respond.Respond;
import com.vn.ntsc.backend.server.respond.result.EntityRespond;

/**
 *
 * @author ThanhDD  03/10/2016 #4789
 */
public class ViewTotalPriceApi implements IApiAdapter{
    @Override
    public Respond execute(JSONObject obj) {
        EntityRespond respond = new EntityRespond();
        SizedListData data = null;
        try {
            String userId = Util.getStringParam(obj, ParamKey.ID);
            // LongLT Edit
           
            int productionType = obj.get(ParamKey.PRODUCTION_TYPE) !=null ? 
                    new Integer (obj.get(ParamKey.PRODUCTION_TYPE).toString()) : -1;
            
            if(productionType == -1){
                data = TransactionLogDAO.listLogTotalPrice(userId);
            } else {
                data = TransactionLogDAO.listLogTotalPrice(userId,productionType);
            }
            
            
//            List<String> partnerList = new ArrayList<>();
//            for (IEntity l : list) {
//                String partnerId = ((LogPoint) l).partnerId;
//                if (partnerId != null && (((LogPoint) l).type != 14)) {
//                    partnerList.add(partnerId);
//                } 
//            }
//
//            Map<String, String> partnerName = UserDAO.getUserName(partnerList);
//            for (IEntity l : list) {
//                String partnerId = ((LogPoint) l).partnerId;
//              
//                if (partnerId != null) {
//                    if (
//                            (((LogPoint) l).type == 14)
//                            
//                            ) {                        
//                        ((LogPoint) l).partnerName = partnerId;
//                    } else {
//                        String partName = partnerName.get(partnerId);
//                        ((LogPoint) l).partnerName = partName;
//                    }
//
//                }
//                // thanhdd #4789
//            }
           
            respond = new EntityRespond(ErrorCode.SUCCESS, data);
        } catch (EazyException ex) {
            Util.addErrorLog(ex);
            respond = new EntityRespond(ex.getErrorCode());
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
        return respond;
    }
}
