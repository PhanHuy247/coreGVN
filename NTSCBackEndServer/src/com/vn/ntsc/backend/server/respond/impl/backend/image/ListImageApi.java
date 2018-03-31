/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.backend.server.respond.impl.backend.image;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.json.simple.JSONObject;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.ParamKey;
import eazycommon.constant.mongokey.SettingdbKey;
import eazycommon.constant.mongokey.UserdbKey;
import eazycommon.exception.EazyException;
import com.vn.ntsc.backend.dao.user.ImageDAO;
import com.vn.ntsc.backend.dao.user.UserDAO;
import com.vn.ntsc.backend.entity.IEntity;
import com.vn.ntsc.backend.entity.impl.datarespond.SizedListData;
import com.vn.ntsc.backend.entity.impl.image.Image;
import com.vn.ntsc.backend.server.respond.IApiAdapter;
import com.vn.ntsc.backend.server.respond.Respond;
import com.vn.ntsc.backend.server.respond.result.EntityRespond;
import eazycommon.util.Util;
import com.vn.ntsc.backend.dao.admin.AdminDAO;

/**
 *
 * @author RuAc0n
 */
public class ListImageApi implements IApiAdapter {

    @Override
    public Respond execute(JSONObject obj) {
        EntityRespond respond = new EntityRespond();
        try {
            Long type = Util.getLongParam(obj, ParamKey.TYPE);
            String userId = Util.getStringParam(obj, ParamKey.ID);
            String imageId = Util.getStringParam(obj, ParamKey.IMAGE_ID);
            Long imageType = Util.getLongParam(obj, ParamKey.IMAGE_TYPE);
            Long order = Util.getLongParam(obj, ParamKey.ORDER);
            Long sort = Util.getLongParam(obj, ParamKey.SORT);
            Long skip = Util.getLongParam(obj, ParamKey.SKIP);
            Long take = Util.getLongParam(obj, ParamKey.TAKE);
            // Linh 2016/12/15 #5677
            Long gender = null;
            if (obj.get(ParamKey.GENDER) != null){
                gender = Util.getLongParam(obj, ParamKey.GENDER);
            }
            SizedListData data = ImageDAO.searchImageVer2(type, imageType, userId, imageId, sort, order, skip.intValue(), take.intValue(), gender);
//            SizedListData data = ImageDAO.searchImage(type, imageType, userId, imageId, sort, order, skip.intValue(), take.intValue());
            List<IEntity> ll = data.ll;
            List<String> list = new ArrayList<String>();
            for (IEntity l : ll) {
                list.add(((Image) l).userId);
            }
            Map<String, String> mapName = UserDAO.getUserName(list);
            for (IEntity l : ll) {
                String userName = mapName.get(((Image) l).userId);
                ((Image) l).username = userName;
                
//                String userDenyID = (String) l.toJsonObject().get(UserdbKey.IMAGE.USER_DENY);
//                Util.addDebugLog("====ListImageApi userDenyID:"+userDenyID);
//                if (userDenyID != null && !"".equals(userDenyID)) {
//                    IEntity userAdmin = AdminDAO.getDetail(userDenyID);
//                    String userAdminName = userAdmin.toJsonObject().get(SettingdbKey.ADMINISTRATOR.NAME).toString();
//                    ((Image) l).userDenyName = userAdminName;
//                }
            }
            
            data.ll = ll;
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
