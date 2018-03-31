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
import java.util.List;
import vn.com.ntsc.staticfileserver.dao.impl.StickerCategoryDAO;
import vn.com.ntsc.staticfileserver.dao.impl.StkCatDAO;
import vn.com.ntsc.staticfileserver.dao.impl.StkDAO;
import vn.com.ntsc.staticfileserver.entity.impl.StickerCategoryData;
import vn.com.ntsc.staticfileserver.server.Request;
import vn.com.ntsc.staticfileserver.server.respond.IApiAdapter;
import vn.com.ntsc.staticfileserver.server.respond.Respond;
import vn.com.ntsc.staticfileserver.server.respond.result.ListEntityRespond;
import vn.com.ntsc.staticfileserver.server.respond.result.VersionRespond;

/**
 *
 * @author hoangnh
 */
public class ListUpdatedStickerCatApi implements IApiAdapter{

    @Override
    public Respond execute(Request request, Date time) {
        Respond respond = new Respond();
        try{
            HashMap<String, Integer> listCategory = request.getListCategory();
            List<StickerCategoryData> listUpdatedCategory = new ArrayList<>();
            
            List<StickerCategoryData> listSticker = StkCatDAO.getAllCategory();
            for(StickerCategoryData item: listSticker){
                Integer version = listCategory.get(item.id);
                if(version == null || item.version > version){
                    listUpdatedCategory.add(item);
                }
            }
            
            listSticker = StickerCategoryDAO.getCategoryUrl(listUpdatedCategory);
            listSticker = StkDAO.getAllCategory(listUpdatedCategory);
            
            respond = new ListEntityRespond(ErrorCode.SUCCESS, listUpdatedCategory);
        }catch(Exception ex){
            Util.addErrorLog(ex);
        }
        return respond;
    }
    
}
