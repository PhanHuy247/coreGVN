/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.com.ntsc.staticfileserver.server.respond.impl;

import vn.com.ntsc.staticfileserver.server.respond.IApiAdapter;
import vn.com.ntsc.staticfileserver.server.respond.Respond;
import eazycommon.constant.ErrorCode;
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import eazycommon.constant.FilesAndFolders;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.UUID;
import eazycommon.util.DateFormat;
import vn.com.ntsc.staticfileserver.server.Request;
import vn.com.ntsc.staticfileserver.server.respond.common.Helper;
import eazycommon.util.Util;
import org.json.simple.JSONObject;
import vn.com.ntsc.staticfileserver.dao.impl.NewsBannerDAO;
import vn.com.ntsc.staticfileserver.entity.impl.StampData;
import vn.com.ntsc.staticfileserver.server.respond.result.EntityRespond;

/**
 *
 * @author RuAc0n
 */
public class UploadNewBannerImageApi implements IApiAdapter {

    @Override
    public Respond execute(Request request, Date time) {
        Respond respond = new Respond();
        try {
            InputStreamReader isr = new InputStreamReader(request.getInputStream());
            BufferedReader reader = new BufferedReader(isr);
            String inputString = reader.readLine();
            JSONObject req = Util.toJSONObject(inputString);
            String imageString = (String) req.get("data");
            byte[] imageBytes = Base64.decode(imageString);
            UUID code = UUID.randomUUID();
            String fileName = code + FilesAndFolders.EXTENSIONS.STAMP_EXTENSION;
            String dateString = DateFormat.format(Util.getGMTTime());
            StringBuilder monthYear = new StringBuilder();
            monthYear.append(dateString.substring(0, 4)).append(dateString.substring(4, 6));
            String urlPath = monthYear.toString() + File.separator + dateString.subSequence(6, 8) + File.separator + fileName;
            Helper.writeFile(urlPath, imageBytes, FilesAndFolders.FOLDERS.NEWS_BANNER_FOLDER, time);
            //insert image collection
            String id = NewsBannerDAO.insert(urlPath);
            StampData data = new StampData(id, null);
            respond = new EntityRespond(ErrorCode.SUCCESS, data);
            Util.addDebugLog(respond.toJsonObject().toJSONString());
        } catch (IOException ex) {
            Util.addErrorLog(ex);
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
        return respond;
    }

}
