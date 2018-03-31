/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.com.ntsc.staticfileserver.server.respond.impl;

import eazycommon.constant.ErrorCode;
import eazycommon.constant.FilesAndFolders;
import java.util.Date;
import eazycommon.exception.EazyException;
import vn.com.ntsc.staticfileserver.dao.impl.FileDAO;
import vn.com.ntsc.staticfileserver.server.Request;
import vn.com.ntsc.staticfileserver.server.respond.IApiAdapter;
import vn.com.ntsc.staticfileserver.server.respond.Respond;
import vn.com.ntsc.staticfileserver.server.respond.common.Helper;
import vn.com.ntsc.staticfileserver.server.respond.result.ByteRespond;
import eazycommon.util.Util;

/**
 *
 * @author RuAc0n
 */
public class LoadFileApi implements IApiAdapter {

    @Override
    public Respond execute(Request request, Date time) {
        Respond respond = new Respond();
        String fileId = request.getFileId();
        if (fileId == null || fileId.isEmpty()) {
            respond.code = ErrorCode.WRONG_DATA_FORMAT;
            return respond;
        }
        try {
            String fileUrl = FileDAO.getFileUrl(fileId);
            String filePath = FilesAndFolders.FOLDERS.FILES_FOLDER + fileUrl;
            filePath = filePath.replace(".sh", ".mp4");
            ByteRespond result = new ByteRespond();
            result.data = Helper.getFile(filePath);
            respond = result;
        } catch (EazyException ex) {
            Util.addErrorLog(ex);
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
        return respond;
    }

}
