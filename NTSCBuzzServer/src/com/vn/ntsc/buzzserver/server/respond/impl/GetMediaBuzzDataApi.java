/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.buzzserver.server.respond.impl;

import com.vn.ntsc.buzzserver.dao.impl.BuzzDetailDAO;
import com.vn.ntsc.buzzserver.dao.impl.LikeDAO;
import com.vn.ntsc.buzzserver.entity.impl.buzz.Buzz;
import com.vn.ntsc.buzzserver.server.respond.IApiAdapter;
import com.vn.ntsc.buzzserver.server.respond.Respond;
import com.vn.ntsc.buzzserver.server.respond.result.EntityRespond;
import com.vn.ntsc.buzzserver.server.respond.result.ListEntityRespond;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.ParamKey;
import eazycommon.util.Util;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 *
 * @author hoangnh
 */
public class GetMediaBuzzDataApi implements IApiAdapter{

    @Override
    public Respond execute(JSONObject obj, long time) {
        Util.addDebugLog("===========GetMediaBuzzDataApi==============");
        ListEntityRespond result = new ListEntityRespond();
        try{
            String userId = Util.getStringParam(obj, ParamKey.USER_ID);
            List<String> fileList = Util.getListString(obj, ParamKey.LIST_ID);
//            JSONArray fileList = (JSONArray) obj.get(ParamKey.LIST_ID);
            if(fileList.isEmpty()){
                List<Buzz> lBuzz = new ArrayList<>();
                result = new ListEntityRespond(ErrorCode.SUCCESS, lBuzz);
            }else{
                Map<String, Buzz> mBuzz = BuzzDetailDAO.getMediaBuzzDetail(fileList);
                if(userId != null){
                    mBuzz = LikeDAO.getMapLikeBuzz(mBuzz, userId);
                }
                Collection<Buzz> cBuzz = mBuzz.values();
                Iterator<Buzz> it = cBuzz.iterator();
                List<Buzz> lBuzz = new ArrayList<>();
                while (it.hasNext()) {
                    Buzz buzz = it.next();
                    lBuzz.add(buzz);
                }
                result = new ListEntityRespond(ErrorCode.SUCCESS, lBuzz);
            }
            
        }catch(Exception ex){
            Util.addErrorLog(ex);
        }
        return result;
    }
    
}
