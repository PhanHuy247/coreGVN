/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.com.ntsc.staticfileserver.server.respond.impl;

import eazycommon.constant.ErrorCode;
import eazycommon.util.Util;
import java.util.Date;
import java.util.List;
import vn.com.ntsc.staticfileserver.dao.impl.EmojiCategoryDAO;
import vn.com.ntsc.staticfileserver.dao.impl.EmojiDAO;
import vn.com.ntsc.staticfileserver.dao.impl.EmojiItemDAO;
import vn.com.ntsc.staticfileserver.entity.impl.EmojiCategoryData;
import vn.com.ntsc.staticfileserver.server.Request;
import vn.com.ntsc.staticfileserver.server.respond.IApiAdapter;
import vn.com.ntsc.staticfileserver.server.respond.Respond;
import vn.com.ntsc.staticfileserver.server.respond.result.EntityRespond;
import vn.com.ntsc.staticfileserver.server.respond.result.ListEntityRespond;

/**
 *
 * @author hoangnh
 */
public class ListEmojiItemDataApi implements IApiAdapter{

    @Override
    public Respond execute(Request request, Date time) {
        Util.addDebugLog("=========ListEmojiCatDataApi========");
        Respond respond = new Respond();
        try{
            String catId = request.getCatId();
            
            EmojiCategoryData emojiCategory = EmojiCategoryDAO.getCategory(catId);
            //listEmoji = EmojiDAO.getCategoryUrl(listEmoji);
            if(emojiCategory != null){
                emojiCategory.listEmoji = EmojiItemDAO.getListEmoji(catId);
            }
            
            respond = new EntityRespond(ErrorCode.SUCCESS, emojiCategory);
        }catch(Exception ex){
            Util.addErrorLog(ex);
        }       
        return respond;
    }
    
}
