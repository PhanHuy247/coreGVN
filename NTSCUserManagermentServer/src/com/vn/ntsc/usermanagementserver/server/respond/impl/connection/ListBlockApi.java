/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.usermanagementserver.server.respond.impl.connection;

import com.vn.ntsc.usermanagementserver.server.respond.Respond;
import com.vn.ntsc.usermanagementserver.server.respond.IApiAdapter;
import com.vn.ntsc.usermanagementserver.dao.impl.BlockDAO;
import com.vn.ntsc.usermanagementserver.dao.impl.FavoristDAO;
import com.vn.ntsc.usermanagementserver.dao.impl.UserDAO;
import eazycommon.util.Util;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.ParamKey;
import eazycommon.exception.EazyException;
import org.json.simple.JSONObject;
import com.vn.ntsc.usermanagementserver.entity.impl.user.User;
import com.vn.ntsc.usermanagementserver.server.respond.result.ListEntityRespond;

/**
 *
 * @author RuAc0n
 */
public class ListBlockApi implements IApiAdapter {

    public Respond execute(JSONObject obj, Date time) {
        ListEntityRespond result = new ListEntityRespond();
        try {
            String userId = Util.getStringParam(obj, ParamKey.USER_ID);
            long skip = Util.getLongParam(obj, ParamKey.SKIP);
            long take = Util.getLongParam(obj, ParamKey.TAKE);
            
            //1. get full block list
            List<String> blockListId = BlockDAO.listBlockUsers(userId);
            
            //2. get full userinfo of block list
            List<User> blockUserList = UserDAO.getListUser(blockListId);
            
            //3. filter blockUserList based on skip and take
            List<User> pagingBlockUsers = getPagingBlockUsers(blockUserList, skip, take);
            if(pagingBlockUsers != null)
                for(int i = 0; i < pagingBlockUsers.size(); i++){
                    pagingBlockUsers.get(i).isFav = FavoristDAO.checkFavourist(userId, pagingBlockUsers.get(i).userId);
                }
            result = new ListEntityRespond(ErrorCode.SUCCESS, pagingBlockUsers);
        } catch (EazyException ex) {
            Util.addErrorLog(ex);
            result.code = ex.getErrorCode();
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
        return result;
    }
    
    public List<User> getPagingBlockUsers(List<User> blockUserList, long skip, long take){
        List<User> listUser = new ArrayList<User>();
        
        if (skip > blockUserList.size() || take < 0)
            return listUser;
        
        long fromIndex = skip;
        long toIndex = fromIndex + take;
        if (toIndex > blockUserList.size())
            toIndex = blockUserList.size();
        
        if (skip == 0 && take == 0)
            return blockUserList;
        else {
            listUser = blockUserList.subList((int)fromIndex, (int)toIndex);
            //for (long i = startIndex; i < endIndex; i++) {
            //    listUser.add(blockUserList.get((int)i));
            //}
            
        }
        return listUser;
    }        
}