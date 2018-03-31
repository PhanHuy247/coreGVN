/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.backend.server.respond.impl.backend.emoji;

import com.vn.ntsc.backend.dao.emoji.EmojiCategoryDAO;
import com.vn.ntsc.backend.dao.emoji.EmojiDAO;
import com.vn.ntsc.backend.entity.impl.datarespond.InsertData;
import com.vn.ntsc.backend.server.respond.IApiAdapter;
import com.vn.ntsc.backend.server.respond.Respond;
import com.vn.ntsc.backend.server.respond.result.EntityRespond;
import eazycommon.constant.Constant;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.ParamKey;
import eazycommon.exception.EazyException;
import eazycommon.util.Util;
import java.util.Set;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 *
 * @author hoangnh
 */
public class AddEmojiApi implements IApiAdapter{

    @Override
    public Respond execute(JSONObject obj) {
        EntityRespond respond = new EntityRespond();
        try{
            String name = Util.getStringParam(obj, ParamKey.EMOJI_NAME);
            String code = Util.getStringParam(obj, ParamKey.EMOJI_CODE);
            String catId = Util.getStringParam(obj, ParamKey.CATEGORY_ID);
            String fileId = Util.getStringParam(obj, ParamKey.FILE_ID);
            String fileType = Util.getStringParam(obj, ParamKey.FILE_TYPE);
            
            JSONArray query = (JSONArray) new JSONParser().parse(code);
            Boolean inCatDuplicate = false;
            Set<String> setCode = EmojiDAO.getSetCode(null, catId, true);
            for (int i = 0; i < query.size(); i++) {
                String item = query.get(i).toString();
                if(setCode.contains(item)){
                    inCatDuplicate = true;
                    break;
                }
            }
            
            Integer duplicate = Constant.FLAG.OFF;
            Set<String> listCode = EmojiDAO.getSetCode(null, catId, false);
            for (int i = 0; i < query.size(); i++) {
                String item = query.get(i).toString();
                if(listCode.contains(item)){
                    duplicate = Constant.FLAG.ON;
                    break;
                }
            }
            
            Boolean isNameDuplicate = EmojiDAO.isEmojiNameExist(catId, name, null);
            
            if(inCatDuplicate || isNameDuplicate){
                respond = new EntityRespond(ErrorCode.EXISTS_DATA);
            }else{
                int order = EmojiDAO.getMaxOrder() + 1;
                String id = EmojiDAO.addEmoji(fileId, catId, code, name, order, fileType, duplicate);
                EmojiCategoryDAO.updateNumber(catId, 1);
                EmojiCategoryDAO.updateVersion(catId);
                respond = new EntityRespond(ErrorCode.SUCCESS, new InsertData(id, duplicate));
            }
        }catch(EazyException ex){
            Util.addErrorLog(ex);
        }catch(Exception ex){
            Util.addErrorLog(ex);
        }
        return respond;
    }
    
}
