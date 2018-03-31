/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.backend.server.respond.impl.backend.log;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.json.simple.JSONObject;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.ParamKey;
import eazycommon.exception.EazyException;
import com.vn.ntsc.backend.dao.log.LogPointDAO;
import com.vn.ntsc.backend.dao.user.UserDAO;
import com.vn.ntsc.backend.entity.IEntity;
import com.vn.ntsc.backend.entity.impl.datarespond.SizedListData;
import com.vn.ntsc.backend.entity.impl.log.LogPoint;
import com.vn.ntsc.backend.server.respond.IApiAdapter;
import com.vn.ntsc.backend.server.respond.Respond;
import com.vn.ntsc.backend.server.respond.result.EntityRespond;
import eazycommon.util.Util;

/**
 *
 * @author RuAc0n
 */
public class ListLogPointApi implements IApiAdapter {

    @Override
    public Respond execute(JSONObject obj) {
        EntityRespond respond = new EntityRespond();
        try {
            String userId = Util.getStringParam(obj, ParamKey.ID);
            Integer skip = Util.getIntParam(obj, ParamKey.SKIP);
            Integer take = Util.getIntParam(obj, ParamKey.TAKE);
            
            if(skip == (new Integer(-1))){
                skip = 0;
            }
            
            if(take == (new Integer(-1))){
                take = 20;
            }
            Util.addDebugLog("ListLogPointApi ======================== SKIP " + skip);
            Util.addDebugLog("ListLogPointApi ======================== TAKE " + take);
            //2. get List Friend detail SizedListData data = new SizedListData(null, list);
//            List<IEntity> list = LogPointDAO.listLog(userId,skip,take);
            SizedListData data = LogPointDAO.listLog(userId,skip,take);
            List<IEntity> list = data.ll;
            List<String> partnerList = new ArrayList<>();
            for (IEntity l : list) {
                String partnerId = ((LogPoint) l).partnerId;
                if (partnerId != null && (((LogPoint) l).type != 14)&& (((LogPoint) l).type != 49)) {
                    partnerList.add(partnerId);
                } 
            }

            Map<String, String> partnerName = UserDAO.getUserName(partnerList);
            for (IEntity l : list) {
                String partnerId = ((LogPoint) l).partnerId;
                // LongLT 19Sep2016 /////////////////////////// START #4295
//                if (partnerId != null) {
//                    String partName = partnerName.get(partnerId);
//                    ((LogPoint) l).partnerName = partName;
//                }
                if (partnerId != null) {
                    if (
                            (((LogPoint) l).type == 14)
                            ||
                            (((LogPoint) l).type == 49)
                            ) {                        
                        ((LogPoint) l).partnerName = partnerId;
                    } else {
                        String partName = partnerName.get(partnerId);
                        ((LogPoint) l).partnerName = partName;
                    }
                }
                // LongLT 19Sep2016 /////////////////////////// END #4295
            }
//            SizedListData data = new SizedListData(null, list);
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
