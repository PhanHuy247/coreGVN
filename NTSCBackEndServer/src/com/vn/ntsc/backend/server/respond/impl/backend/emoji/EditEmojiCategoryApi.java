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
public class EditEmojiCategoryApi implements IApiAdapter{

    @Override
    public Respond execute(JSONObject obj) {
        EntityRespond respond = new EntityRespond();
        try{
            String id = Util.getStringParam(obj, ParamKey.ID);
            String vnCatName = Util.getStringParam(obj, ParamKey.VIET_NAME);
            String vnDes = Util.getStringParam(obj, ParamKey.VIET_DESCRIPTION);
            String enCatName = Util.getStringParam(obj, ParamKey.ENGLISH_NAME);
            String enDes = Util.getStringParam(obj, ParamKey.ENGLISH_DESCRIPTION);
            String fileId = Util.getStringParam(obj, ParamKey.FILE_ID);
            Integer flag = Util.getIntParam(obj, ParamKey.FLAG);
            
            EmojiCategoryDAO.updateEmojiCategory(id, fileId, vnCatName, vnDes, enCatName, enDes, flag);
            respond = new EntityRespond(ErrorCode.SUCCESS, null);
        }catch(EazyException ex){
            Util.addErrorLog(ex);
        }catch(Exception ex){
            Util.addErrorLog(ex);
        }
        return respond;
    }
    
}
