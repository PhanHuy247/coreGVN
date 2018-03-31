/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.backend.server.respond.impl.backend.emoji;

import com.vn.ntsc.backend.dao.emoji.EmojiCategoryDAO;
import com.vn.ntsc.backend.dao.emoji.EmojiDAO;
import com.vn.ntsc.backend.server.respond.IApiAdapter;
import com.vn.ntsc.backend.server.respond.Respond;
import com.vn.ntsc.backend.server.respond.result.EntityRespond;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.ParamKey;
import eazycommon.exception.EazyException;
import eazycommon.util.Util;
import org.json.simple.JSONObject;

/**
 *
 * @author hoangnh
 */
public class DelEmojiCategoryApi implements IApiAdapter{

    @Override
    public Respond execute(JSONObject obj) {
        EntityRespond respond = new EntityRespond();
        try{
            String id = Util.getStringParam(obj, ParamKey.ID);
            
            EmojiCategoryDAO.deleteCategory(id);
            EmojiDAO.delEmojiCategory(id);
            respond = new EntityRespond(ErrorCode.SUCCESS, null);
        }catch(EazyException ex){
            Util.addErrorLog(ex);
        }catch(Exception ex){
            Util.addErrorLog(ex);
        }
        return respond;
    }
    
}
