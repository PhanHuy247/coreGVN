/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.buzzserver.server.respond.impl;

import eazycommon.constant.Constant;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.ParamKey;
import eazycommon.exception.EazyException;
import eazycommon.util.Util;
import org.json.simple.JSONObject;
import com.vn.ntsc.buzzserver.dao.impl.BuzzDetailDAO;
import com.vn.ntsc.buzzserver.dao.impl.UserBuzzDAO;
import com.vn.ntsc.buzzserver.entity.impl.datarespond.SendGiftData;
import com.vn.ntsc.buzzserver.server.respond.IApiAdapter;
import com.vn.ntsc.buzzserver.server.respond.Respond;
import com.vn.ntsc.buzzserver.server.respond.result.EntityRespond;

/**
 *
 * @author RuAc0n
 */
public class SendGiftApi implements IApiAdapter {

    @Override
    public Respond execute(JSONObject obj, long time) {
        EntityRespond result = new EntityRespond();
        try {
            String userId = Util.getStringParam(obj, ParamKey.RECEIVER_ID);
            String buzzVal = Util.getStringParam(obj, ParamKey.GIFT_ID);
            String ip = Util.getStringParam(obj, ParamKey.IP);
            int buzzType = Constant.BUZZ_TYPE_VALUE.GIFT_BUZZ;
            if (buzzVal == null || buzzVal.isEmpty() || userId == null || userId.isEmpty()) {
                result.code = ErrorCode.WRONG_DATA_FORMAT;
                return result;
            }
            String id = BuzzDetailDAO.addBuzz(userId, buzzVal, buzzType, time, Constant.FLAG.ON, ip, "", 0, "", "", "", 0);
            UserBuzzDAO.updateBuzzActivity(id, userId, time, Constant.FLAG.ON, Constant.BUZZ_TYPE_VALUE.GIFT_BUZZ, null, null);
            SendGiftData data = new SendGiftData(id, time, userId);
            result = new EntityRespond(ErrorCode.SUCCESS, data);
        } catch (EazyException ex) {
            Util.addErrorLog(ex);
            result.code = ex.getErrorCode();
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
        return result;
    }

}
