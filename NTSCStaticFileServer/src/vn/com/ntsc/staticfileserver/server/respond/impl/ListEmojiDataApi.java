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
import vn.com.ntsc.staticfileserver.server.respond.result.ListEntityRespond;
import vn.com.ntsc.staticfileserver.server.respond.result.VersionRespond;

/**
 *
 * @author hoangnh
 */
public class ListEmojiDataApi implements IApiAdapter{

    @Override
    public Respond execute(Request request, Date time) {
        Util.addDebugLog("=========ListEmojiDataApi========");
        Respond respond = new Respond();
        try{
            Integer version = 0;
            List<EmojiCategoryData> listEmoji = EmojiCategoryDAO.getActiveCategory();
            for(EmojiCategoryData item: listEmoji){
                version = version + item.version;
            }
            listEmoji = EmojiDAO.getCategoryUrl(listEmoji);
            listEmoji = EmojiItemDAO.getAllCategory(listEmoji);

//            respond = new ListEntityRespond(ErrorCode.SUCCESS, listEmoji);
            respond = new VersionRespond(ErrorCode.SUCCESS, listEmoji, version);
        }catch(Exception ex){
            Util.addErrorLog(ex);
        }       
        return respond;
    }
    
}
