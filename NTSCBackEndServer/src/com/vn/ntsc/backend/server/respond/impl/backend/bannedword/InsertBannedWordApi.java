/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.vn.ntsc.backend.server.respond.impl.backend.bannedword;

import org.json.simple.JSONObject;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.ParamKey;
import eazycommon.exception.EazyException;
import com.vn.ntsc.backend.dao.admin.BannedWordDAO;
import com.vn.ntsc.backend.server.respond.IApiAdapter;
import com.vn.ntsc.backend.server.respond.Respond;
import com.vn.ntsc.backend.server.respond.result.EntityRespond;
import eazycommon.util.Util;
import java.util.Date;

/**
 *
 * @author RuAc0n
 */
public class InsertBannedWordApi implements IApiAdapter{

    private static final int flag_error = 5;
    
    @Override
    public Respond execute(JSONObject obj) {
        EntityRespond respond = new EntityRespond();
        try{
            String baseWords = Util.getStringParam(obj, "word");
            Long flag = Util.getLongParam(obj, ParamKey.FLAG);
            String isSaved="";
            if(flag == null){
                return new EntityRespond(flag_error);
            }
            String [] words = baseWords.split("[,、，､]");
            Date now = new Date();
            for(String word : words){
                if(!word.isEmpty())
                  isSaved=  BannedWordDAO.insert(word.trim(), flag.intValue(), now);
            }
            BannedWordDAO.changeVersion(1);
            Util.addDebugLog("=========isSaved==="+isSaved);
            if (isSaved.equals("exist")){
                respond = new EntityRespond(ErrorCode.EXISTS_DATA);
            } else {
                respond = new EntityRespond(ErrorCode.SUCCESS);
            }
            
        }catch(EazyException ex){
            Util.addErrorLog(ex);            
            respond = new EntityRespond(ex.getErrorCode());
        }catch(Exception ex){
            Util.addErrorLog(ex);            
        }
        return respond;
    }    
}
