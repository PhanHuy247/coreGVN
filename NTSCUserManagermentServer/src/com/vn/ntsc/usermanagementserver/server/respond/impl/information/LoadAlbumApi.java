/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.usermanagementserver.server.respond.impl.information;

import com.vn.ntsc.usermanagementserver.dao.impl.AlbumDAO;
import com.vn.ntsc.usermanagementserver.dao.impl.AlbumImageDAO;
import com.vn.ntsc.usermanagementserver.entity.impl.datarespond.AlbumData;
import com.vn.ntsc.usermanagementserver.server.respond.IApiAdapter;
import com.vn.ntsc.usermanagementserver.server.respond.Respond;
import com.vn.ntsc.usermanagementserver.server.respond.result.ListEntityRespond;
import eazycommon.constant.Constant;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.ParamKey;
import eazycommon.util.Util;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import org.json.simple.JSONObject;

/**
 *
 * @author hoangnh
 */
public class LoadAlbumApi implements IApiAdapter{

    @Override
    public Respond execute(JSONObject obj, Date time) {
        ListEntityRespond result = new ListEntityRespond();
        try {
            String userId = Util.getStringParam(obj, ParamKey.USER_ID);
            String reqUserId = Util.getStringParam(obj, ParamKey.REQUEST_USER_ID);
            Integer take = Util.getIntParam(obj, ParamKey.TAKE);
            Integer skip = Util.getIntParam(obj, ParamKey.SKIP);
            
            List<AlbumData> data = new ArrayList();
            
            if(reqUserId != null && !reqUserId.equals(userId)){
                data = AlbumDAO.loadAllAlbum(reqUserId, Constant.POST_MODE.EVERYONE,skip,take);
            }
            if(userId != null && userId.equals(reqUserId)){
                data = AlbumDAO.loadAllAlbum(reqUserId, Constant.POST_MODE.ONLY_ME,skip,take);
            }
            data = AlbumImageDAO.getLastestImage(data);
            collectionAlbum(data);
            result = new ListEntityRespond(ErrorCode.SUCCESS, data);
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
        return result;
    }
    
    public void collectionAlbum(List<AlbumData> data){
        Collections.sort(data,new Comparator<AlbumData>(){
                     public int compare(AlbumData s1,AlbumData s2){
                           // Write your logic here.
                         if(s1.time > s2.time)
                             return -1;
                         else if(s1.time < s2.time)
                             return 1;
                         return 0;
                     }});
    }
    
}
