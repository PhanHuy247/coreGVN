/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.com.ntsc.staticfileserver.server.respond.impl;

import eazycommon.constant.ErrorCode;
import eazycommon.constant.FilesAndFolders;
import eazycommon.util.Util;
import java.util.Date;
import java.util.List;
import vn.com.ntsc.staticfileserver.dao.impl.StickerCategoryDAO;
import vn.com.ntsc.staticfileserver.dao.impl.StickerDAO;
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
public class ListStickerUrlApi implements IApiAdapter{

    @Override
    public Respond execute(Request request, Date time) {
        Util.addDebugLog("=========ListStickerUrlApi========");
        Respond respond = new Respond();
        try{
            Integer version = 0;
            List<StickerCategoryData> listSticker = StkCatDAO.getAllCategory();
            for(StickerCategoryData item: listSticker){
                version = version + item.version;
            }
            
            listSticker = StickerCategoryDAO.getCategoryUrl(listSticker);
            listSticker = StkDAO.getAllCategory(listSticker);
            
//            respond = new ListEntityRespond(ErrorCode.SUCCESS, listSticker);
            respond = new VersionRespond(ErrorCode.SUCCESS, listSticker, version);
        }catch(Exception ex){
            Util.addErrorLog(ex);
        }       
        return respond;
    }
    
}
