/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.usermanagementserver.server.respond.impl.connection;

import eazycommon.constant.ErrorCode;
import eazycommon.util.Util;
import java.util.Date;
import org.bson.types.ObjectId;
import org.json.simple.JSONObject;
import com.vn.ntsc.usermanagementserver.dao.impl.CheckOutDAO;
import com.vn.ntsc.usermanagementserver.dao.impl.MyFootPrintDAO;
import com.vn.ntsc.usermanagementserver.server.respond.IApiAdapter;
import com.vn.ntsc.usermanagementserver.server.respond.Respond;
import com.vn.ntsc.usermanagementserver.server.respond.result.EntityRespond;

/**
 *
 * @author Administrator
 */
public class DeleteCheckOutFootprintApi implements IApiAdapter{

    @Override
    public Respond execute(JSONObject obj, Date time) {
        Util.addDebugLog("Test obj from DeleteCheckOutFootprintApi "+obj.toJSONString());
        EntityRespond respond = new EntityRespond();
        try {
            String chk_id = (String) obj.get("chk_id");
            String userId = (String) obj.get("user_id");
            Util.addDebugLog("====user_id:"+userId +"====chk_id:"+chk_id);
            if(chk_id == null && userId == null){
                respond = new EntityRespond(ErrorCode.NO_CONTENT);
            }
            else{
                CheckOutDAO.removeCheckOutFootprint(userId, chk_id);
                respond = new EntityRespond(ErrorCode.SUCCESS);
            }
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            respond = new EntityRespond(ErrorCode.UNKNOWN_ERROR);
        }
        return respond;
    }
    
}
