/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.backend.server.respond.impl.backend.cmcode;

import org.json.simple.JSONObject;
import eazycommon.constant.ErrorCode;
import eazycommon.exception.EazyException;
import com.vn.ntsc.backend.dao.cmcode.AfficiateDAO;
import com.vn.ntsc.backend.dao.cmcode.CMCodeDAO;
import com.vn.ntsc.backend.dao.cmcode.MediaDAO;
import com.vn.ntsc.backend.entity.IEntity;
import com.vn.ntsc.backend.entity.impl.cmcode.CMCode;
import com.vn.ntsc.backend.server.respond.IApiAdapter;
import com.vn.ntsc.backend.server.respond.Respond;
import com.vn.ntsc.backend.server.respond.result.EntityRespond;
import eazycommon.util.Util;

/**
 *
 * @author RuAc0n
 */
public class GetCMCodeDetailApi implements IApiAdapter {

    @Override
    public Respond execute(JSONObject obj) {
        EntityRespond respond = new EntityRespond();
        try {
            String cmCodeId = Util.getStringParam(obj, "cm_code_id");
            if (cmCodeId == null || cmCodeId.isEmpty()) {
                return null;
            }
            IEntity entity = CMCodeDAO.getDetail(cmCodeId);
            String affName = AfficiateDAO.getName(((CMCode) entity).afficiateId);
            ((CMCode) entity).affName = affName;
            String mediaName = MediaDAO.getName(((CMCode) entity).mediaId);
            ((CMCode) entity).mediaName = mediaName;
            respond = new EntityRespond(ErrorCode.SUCCESS, entity);
        } catch (EazyException ex) {
            Util.addErrorLog(ex);
            respond = new EntityRespond(ex.getErrorCode());
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
        return respond;
    }

}
