/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.com.ntsc.staticfileserver.server.respond.impl;

import eazycommon.constant.ErrorCode;
import eazycommon.util.Util;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import vn.com.ntsc.staticfileserver.dao.impl.EmojiCategoryDAO;
import vn.com.ntsc.staticfileserver.dao.impl.EmojiDAO;
import vn.com.ntsc.staticfileserver.dao.impl.EmojiItemDAO;
import vn.com.ntsc.staticfileserver.entity.impl.EmojiCategoryData;
import vn.com.ntsc.staticfileserver.server.Request;
import vn.com.ntsc.staticfileserver.server.respond.IApiAdapter;
import vn.com.ntsc.staticfileserver.server.respond.Respond;
import vn.com.ntsc.staticfileserver.server.respond.result.ListEntityRespond;

/**
 *
 * @author hoangnh
 */
public class ListUpdatedEmojiCatApi implements IApiAdapter{

    @Override
    public Respond execute(Request request, Date time) {
        Util.addDebugLog("=========ListUpdatedEmojiCatApi========");
        Respond respond = new Respond();
        try{
            HashMap<String, Integer> listCategory = request.getListCategory();
            List<EmojiCategoryData> listUpdatedCategory = new ArrayList<>();
            
            List<EmojiCategoryData> listEmoji = EmojiCategoryDAO.getActiveCategory();
            for(EmojiCategoryData item : listEmoji){
                Integer version = listCategory.get(item.id);
                if(version == null || item.version > version){
                    listUpdatedCategory.add(item);
                }
            }
            listUpdatedCategory = EmojiDAO.getCategoryUrl(listUpdatedCategory);
            listUpdatedCategory = EmojiItemDAO.getAllCategory(listUpdatedCategory);
            respond = new ListEntityRespond(ErrorCode.SUCCESS, listUpdatedCategory);
        }catch(Exception ex){
            Util.addErrorLog(ex);
        }       
        return respond;
    }
    
}
