/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.backend.server.respond.impl.backend.news;

import eazycommon.constant.ErrorCode;
import eazycommon.exception.EazyException;
import eazycommon.util.Util;
import org.json.simple.JSONObject;
import com.vn.ntsc.backend.dao.news.NewsDAO;
import com.vn.ntsc.backend.entity.impl.news.News;
import com.vn.ntsc.backend.server.respond.IApiAdapter;
import com.vn.ntsc.backend.server.respond.Respond;
import com.vn.ntsc.backend.server.respond.result.EntityRespond;

/**
 *
 * @author HuyDX
 */
public class GetNewsDetailApi implements IApiAdapter{
    
    @Override
    public Respond execute(JSONObject obj) {
        
        Respond response = new EntityRespond();
        try {
            String id = Util.getStringParam(obj, "id");
            News news = NewsDAO.get(id);
            long from = news.getFrom();
            long to = news.getTo();
            long currentTime = Util.getGMTTime().getTime();
            if (from>currentTime)
                news.setStatus(1); //news's not yet show to the app user
            else if (currentTime < to)
                news.setStatus(0); // news's showing to the app user
            else 
                news.setStatus(-1); //news showed and no longer needed
            
            if (news!=null){
                response = new EntityRespond(ErrorCode.SUCCESS, news);
            }
        } catch (EazyException ex) {
            Util.addErrorLog(ex);
            response = new EntityRespond(ex.getErrorCode());
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
        return response;
    }
    
}
