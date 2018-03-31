/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.backend.server.respond.impl.backend.emoji;

import com.vn.ntsc.backend.dao.emoji.EmojiCategoryDAO;
import com.vn.ntsc.backend.entity.impl.datarespond.InsertData;
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
public class AddEmojiCategoryApi implements IApiAdapter{

    @Override
    public Respond execute(JSONObject obj) {
        EntityRespond respond = new EntityRespond();
        try{
            String vnCatName = Util.getStringParam(obj, ParamKey.VIET_NAME);
            String vnDes = Util.getStringParam(obj, ParamKey.VIET_DESCRIPTION);
            String enCatName = Util.getStringParam(obj, ParamKey.ENGLISH_NAME);
            String enDes = Util.getStringParam(obj, ParamKey.ENGLISH_DESCRIPTION);
            String fileId = Util.getStringParam(obj, ParamKey.FILE_ID);
            
            int order = EmojiCategoryDAO.getMaxOrder() + 1;
            String id = EmojiCategoryDAO.addEmojiCategory(fileId, vnCatName, vnDes, enCatName, enDes, order);
            respond = new EntityRespond(ErrorCode.SUCCESS, new InsertData(id));
        }catch(EazyException ex){
            Util.addErrorLog(ex);
        }catch(Exception ex){
            Util.addErrorLog(ex);
        }
        return respond;
    }
    
}
