/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.backend.server.respond.impl.backend.log;

import eazycommon.constant.ErrorCode;
import com.vn.ntsc.backend.server.respond.IApiAdapter;
import java.util.List;
import org.json.simple.JSONObject;
import eazycommon.constant.ParamKey;
import eazycommon.exception.EazyException;
import com.vn.ntsc.backend.dao.log.LogLoginDAO;
import com.vn.ntsc.backend.dao.user.UserDAO;
import com.vn.ntsc.backend.entity.impl.datarespond.SizedListData;
import com.vn.ntsc.backend.entity.impl.log.LogLogin;
import com.vn.ntsc.backend.server.respond.Respond;
import com.vn.ntsc.backend.server.common.LogProcess;
import com.vn.ntsc.backend.server.respond.result.EntityRespond;
import eazycommon.util.Util;

/**
 *
 * @author RuAc0n
 */
public class SearchLogLoginApi implements IApiAdapter {

    @Override
    public Respond execute(JSONObject obj) {
        EntityRespond respond = new EntityRespond();
        try {
            Long userType = Util.getLongParam(obj, ParamKey.USER_TYPE);
            String email = Util.getStringParam(obj, ParamKey.EMAIL);
            if (userType == null) {
                if (email != null) {
                    return null;
                }
            }
            String uId = Util.getStringParam(obj, ParamKey.ID);
            String cmCode = Util.getStringParam(obj, ParamKey.CM_CODE);
            List<String> listId = UserDAO.getListUser(userType, uId, email, cmCode);
            String toTime = Util.getStringParam(obj, ParamKey.TO_TIME);
//            toTime = getToTime(toTime);
            String fromTime = Util.getStringParam(obj, ParamKey.FROM_TIME);
            Long sort = Util.getLongParam(obj, ParamKey.SORT);
            Long order = Util.getLongParam(obj, ParamKey.ORDER);
            Long skip = Util.getLongParam(obj, ParamKey.SKIP);
            Long take = Util.getLongParam(obj, ParamKey.TAKE);
            if (sort == null || order == null) {
                return null;
            }
            Long csv = Util.getLongParam(obj, ParamKey.CSV);
            boolean isCsv = false;
            if (csv != null) {
                isCsv = true;
            }
            SizedListData data = LogLoginDAO.listLog(listId, fromTime, toTime, sort, order, skip, take, isCsv);
            if (isCsv) {
                String api = Util.getStringParam(obj, ParamKey.API_NAME);
                return LogProcess.createCSV(api, csv, data.ll, new LogLogin(), null);
            }

            LogProcess.getOneObjectInfor(data.ll);
            respond = new EntityRespond(ErrorCode.SUCCESS, data);
        } catch (EazyException ex) {
            Util.addErrorLog(ex);
            respond = new EntityRespond(ex.getErrorCode());
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
        return respond;
    }

//    private static void getInfor(List<IEntity> ll) throws DaoException {
//        List<String> list = new ArrayList<String>();
//        for (IEntity l : ll) {
//            list.add(((LogLogin) l).userId);
//        }
//        Map<String, IEntity> mapName = UserDAO.getMapLogInfor(list);
//        for (IEntity l : ll) {
//            IEntity user = mapName.get(((LogLogin) l).userId);
//            ((LogLogin) l).email = ((User) user).email;
//            ((LogLogin) l).userName = ((User) user).username;
//            ((LogLogin) l).cmCode = ((User) user).cmCode;
//            ((LogLogin) l).userType = ((User) user).userType.intValue();
//        }
//    }
//
//    private static Respond createCSV(Long csv, List<IEntity> ll) throws Exception {
//        if (csv < -12 || csv > 12) {
//            return new EntityRespond(timezone_error);
//        }
//        getInfor(ll);
//        CSVCreator.createCSV(ll, LogLogin.getHeaders(null), LogLogin.getJsonType(null), Constant.CSV_PATH, csv.intValue());
//        byte[] bytes = Util.getFile(Constant.CSV_PATH);
//        return new CSVRespond(bytes);
//    }

}
