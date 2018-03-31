/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.backend.server.respond.impl.backend.buzz;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.json.simple.JSONObject;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.ParamKey;
import eazycommon.exception.EazyException;
import com.vn.ntsc.backend.dao.buzz.LikeDAO;
import com.vn.ntsc.backend.dao.user.UserDAO;
import com.vn.ntsc.backend.entity.IEntity;
import com.vn.ntsc.backend.entity.impl.buzz.Like;
import com.vn.ntsc.backend.entity.impl.user.User;
import com.vn.ntsc.backend.server.respond.IApiAdapter;
import com.vn.ntsc.backend.server.respond.Respond;
import com.vn.ntsc.backend.server.respond.result.ListEntityRespond;
import eazycommon.util.Util;

/**
 *
 * @author RuAc0n
 */
public class ListLikeApi implements IApiAdapter {

    @Override
    public Respond execute(JSONObject obj) {
        ListEntityRespond respond = new ListEntityRespond();
        try {
            String buzzId = Util.getStringParam(obj, ParamKey.BUZZ_ID);
            List<String> likeId = LikeDAO.getLikeList(buzzId);
            Map<String, IEntity> mLog = UserDAO.getMapLogInfor(likeId);
            List<Like> listLike = new ArrayList<Like>();
            for (String str : likeId) {
                IEntity user = mLog.get(str);
                String userId = ((User) user).userId;
                String username = ((User) user).username;
                String cmCode = ((User) user).cmCode;
                Long userType = ((User) user).userType;
                String email = ((User) user).email;
                listLike.add(new Like(userId, username, email, userType, cmCode));
            }
            respond = new ListEntityRespond(ErrorCode.SUCCESS, listLike);
        } catch (EazyException ex) {
            Util.addErrorLog(ex);
            respond = new ListEntityRespond(ex.getErrorCode());
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
        return respond;
    }
}
