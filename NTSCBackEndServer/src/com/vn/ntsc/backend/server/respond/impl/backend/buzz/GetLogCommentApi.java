/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.vn.ntsc.backend.server.respond.impl.backend.buzz;

import eazycommon.constant.ErrorCode;
import eazycommon.constant.ParamKey;
import eazycommon.constant.mongokey.SettingdbKey;
import eazycommon.exception.EazyException;
import eazycommon.util.Util;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.json.simple.JSONObject;
import com.vn.ntsc.backend.dao.admin.AdminDAO;
import com.vn.ntsc.backend.dao.buzz.CommentDetailDAO;
import com.vn.ntsc.backend.dao.buzz.UserBuzzDAO;
import com.vn.ntsc.backend.dao.user.UserDAO;
import com.vn.ntsc.backend.entity.IEntity;
import com.vn.ntsc.backend.entity.impl.buzz.CommentDetail;
import com.vn.ntsc.backend.entity.impl.datarespond.SizedListData;
import com.vn.ntsc.backend.server.respond.IApiAdapter;
import com.vn.ntsc.backend.server.respond.Respond;
import com.vn.ntsc.backend.server.respond.result.EntityRespond;

/**
 * 
 * @author Namhv
 */
public class GetLogCommentApi implements IApiAdapter{

    @Override
    public Respond execute(JSONObject obj) {
        EntityRespond respond = new EntityRespond();
        try {
            String userId = Util.getStringParam(obj, ParamKey.ID);
            String buzzId = Util.getStringParam(obj, ParamKey.BUZZ_ID);
            Long order = Util.getLongParam(obj, ParamKey.ORDER);
            Long sort = Util.getLongParam(obj, ParamKey.SORT);
            Long skip = Util.getLongParam(obj, ParamKey.SKIP);
            Long take = Util.getLongParam(obj, ParamKey.TAKE);
            String fromTime = Util.getStringParam(obj, ParamKey.FROM_TIME);
            String toTime = Util.getStringParam(obj, ParamKey.TO_TIME);
//            boolean isAppearBuzz = false;
//            if(buzzId != null) {
//                isAppearBuzz = UserBuzzDAO.isAppearBuzz(userId, buzzId);;
//            }
            SizedListData data = CommentDetailDAO.getCommentDetail(userId, buzzId, sort, order, skip.intValue(), take.intValue(),fromTime,toTime);
            List<IEntity> ll = data.ll;
            List<String> list = new ArrayList<>();
            for (IEntity l : ll) {
                list.add(((CommentDetail) l).userId);
            }
            Map<String, String> mapName = UserDAO.getUserName(list);
            for (IEntity l : ll) {
                String userName = mapName.get(((CommentDetail) l).userId);
                ((CommentDetail) l).userName = userName;
            }
//            IEntity userAdmin = AdminDAO.getDetail(buzzId);
            //List<String> listNameDeny = new ArrayList<>();
            for (IEntity l : ll) {
                if (((CommentDetail) l).isUserDeny != null) {
                    //IEntity userAdmin = AdminDAO.getDetail(((CommentDetail) l).isUserDeny);
                    String userAdminName = ((CommentDetail) l).isUserDeny;
                    ((CommentDetail) l).isUserDenyName = userAdminName;
                }
            }
//            for(int i = 0; i < ll.size(); i++) {
//                boolean isAppearBuzz = false;
//                if (((CommentDetail) ll.get(i)).buzzId != null) {
//                    isAppearBuzz = UserBuzzDAO.isAppearBuzz(((CommentDetail) ll.get(i)).userId, ((CommentDetail) ll.get(i)).buzzId);;
//                }
//                if(!isAppearBuzz) {
//                    Util.addDebugLog("GetLogCommentApi===remove"+((CommentDetail) ll.get(i)).commentValue);
//                    ll.remove(i);
//                }
//            }
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
