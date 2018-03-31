/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.com.ntsc.staticfileserver.server.respond.impl;

import vn.com.ntsc.staticfileserver.server.respond.IApiAdapter;
import vn.com.ntsc.staticfileserver.server.respond.Respond;
import eazycommon.constant.ErrorCode;
import java.util.Date;
import eazycommon.exception.EazyException;
import eazycommon.util.Util;
import vn.com.ntsc.staticfileserver.Config;
import vn.com.ntsc.staticfileserver.dao.impl.FileDAO;
import vn.com.ntsc.staticfileserver.entity.impl.GetVideoUrlData;
import vn.com.ntsc.staticfileserver.server.Request;
import vn.com.ntsc.staticfileserver.server.respond.result.EntityRespond;

/**
 *
 * @author RuAc0n
 */
public class GetVideoUrlApi implements IApiAdapter {

    public Respond execute(Request request, Date time) {
        Respond respond = new Respond();
        String fileId = request.getFileId();
        if (fileId == null || fileId.isEmpty()) {
            respond.code = ErrorCode.WRONG_DATA_FORMAT;
            return respond;
        }
        try {
            String fileUrl = FileDAO.getFileUrl(fileId);
            if (fileUrl!=null){
                fileUrl = fileUrl.replace(".sh", ".mp4");
                String streamingUrl = Config.STREAMING_HOST + fileUrl;
                GetVideoUrlData data = new GetVideoUrlData(streamingUrl);
                respond = new EntityRespond(ErrorCode.SUCCESS, data);
            }
            else {
                respond = new EntityRespond(ErrorCode.FILE_NOT_FOUND);
            }
        } catch (EazyException ex) {
            Util.addErrorLog(ex);
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
        System.out.println("respond : " + respond.toJsonObject());
        return respond;
    }

}
