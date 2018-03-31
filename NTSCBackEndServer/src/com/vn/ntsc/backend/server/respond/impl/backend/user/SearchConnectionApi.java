/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.backend.server.respond.impl.backend.user;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.json.simple.JSONObject;
import eazycommon.constant.ErrorCode;
import eazycommon.exception.EazyException;
import com.vn.ntsc.backend.dao.user.UserDAO;
import com.vn.ntsc.backend.entity.IEntity;
import com.vn.ntsc.backend.entity.impl.datarespond.SizedListData;
import com.vn.ntsc.backend.server.respond.IApiAdapter;
import com.vn.ntsc.backend.server.respond.Respond;
import com.vn.ntsc.backend.server.respond.result.EntityRespond;
import eazycommon.util.Util;

/**
 *
 * @author RuAc0n
 */
public class SearchConnectionApi implements IApiAdapter {

    @Override
    public Respond execute(JSONObject obj) {
        EntityRespond respond = new EntityRespond();
        try {
            Map<Integer, List<IEntity>> m = UserDAO.searchConnectionUser(obj);
            if (m == null) {
                return null;
            }
            if (m.isEmpty()) {
                SizedListData data = new SizedListData(0, new ArrayList<IEntity>());
                respond = new EntityRespond(ErrorCode.SUCCESS, data);
            }
            for (Map.Entry pairs : m.entrySet()) {
                Integer number = (Integer) pairs.getKey();
                List<IEntity> llUser = (List<IEntity>) pairs.getValue();
                SizedListData data = new SizedListData(number, llUser);
                respond = new EntityRespond(ErrorCode.SUCCESS, data);
                break;
            }
        } catch (EazyException ex) {
            Util.addErrorLog(ex);
            respond = new EntityRespond(ex.getErrorCode());
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
        return respond;
    }

}
