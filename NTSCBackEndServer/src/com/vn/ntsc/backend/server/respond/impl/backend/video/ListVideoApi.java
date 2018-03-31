/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.backend.server.respond.impl.backend.video;

import com.vn.ntsc.backend.dao.file.FileDAO;
import com.vn.ntsc.backend.dao.user.ImageDAO;
import com.vn.ntsc.backend.dao.user.UserDAO;
import com.vn.ntsc.backend.entity.IEntity;
import com.vn.ntsc.backend.entity.impl.datarespond.SizedListData;
import com.vn.ntsc.backend.entity.impl.image.Image;
import com.vn.ntsc.backend.entity.impl.video.Video;
import com.vn.ntsc.backend.server.respond.IApiAdapter;
import com.vn.ntsc.backend.server.respond.Respond;
import com.vn.ntsc.backend.server.respond.result.EntityRespond;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.ParamKey;
import eazycommon.exception.EazyException;
import eazycommon.util.Util;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.json.simple.JSONObject;

/**
 *
 * @author Phan Huy
 */
public class ListVideoApi implements IApiAdapter {

  

    @Override
    public Respond execute(JSONObject obj) {
        EntityRespond respond = new EntityRespond();
        try {
            Long type = Util.getLongParam(obj, ParamKey.VIDEO_STATUS);
            String userId = Util.getStringParam(obj, ParamKey.ID);
            String videoId = Util.getStringParam(obj, ParamKey.VIDEO_ID);
            Long order = Util.getLongParam(obj, ParamKey.ORDER);
            Long sort = Util.getLongParam(obj, ParamKey.SORT);
            Long skip = Util.getLongParam(obj, ParamKey.SKIP);
            Long take = Util.getLongParam(obj, ParamKey.TAKE);
            String userName = Util.getStringParam(obj, ParamKey.USER_NAME);
            String email = Util.getStringParam(obj, ParamKey.EMAIL);
           

            SizedListData data = FileDAO.searchVideo(type, userId, videoId, sort, order, skip.intValue(), take.intValue(),userName,email);
//            List<IEntity> ll = data.ll;
//            List<String> list = new ArrayList<String>();
//            for (IEntity l : ll) {
//                list.add(((Video) l).userId);
//            }
//            Map<String, String> mapName = UserDAO.getUserName(list);
//            for (IEntity l : ll) {
//                String userName = mapName.get(((Video) l).userId);
//                ((Video) l).username = userName;
//
//            }
//            data.ll = ll;
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
