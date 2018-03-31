/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.backend.server.respond.impl.backend.cmcode;

import java.util.List;
import java.util.Map;
import org.json.simple.JSONObject;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.ParamKey;
import eazycommon.exception.EazyException;
import com.vn.ntsc.backend.dao.cmcode.AfficiateDAO;
import com.vn.ntsc.backend.dao.cmcode.CMCodeDAO;
import com.vn.ntsc.backend.dao.cmcode.MediaDAO;
import com.vn.ntsc.backend.entity.IEntity;
import com.vn.ntsc.backend.entity.impl.cmcode.Afficiate;
import com.vn.ntsc.backend.entity.impl.cmcode.CMCode;
import com.vn.ntsc.backend.entity.impl.cmcode.ListCMCodeData;
import com.vn.ntsc.backend.entity.impl.cmcode.Media;
import com.vn.ntsc.backend.entity.impl.datarespond.SizedListData;
import com.vn.ntsc.backend.server.respond.IApiAdapter;
import com.vn.ntsc.backend.server.respond.Respond;
import com.vn.ntsc.backend.server.respond.result.EntityRespond;
import eazycommon.util.Util;
import com.vn.ntsc.backend.server.respond.result.ListStringRespond;

/**
 *
 * @author RuAc0n
 */
public class GetAllCMCodeApi implements IApiAdapter {

    @Override
    public Respond execute(JSONObject obj) {
        ListStringRespond respond = new ListStringRespond();
        try {
            List<String> cmCodes = CMCodeDAO.getAll();
            respond.code = ErrorCode.SUCCESS;
            respond.data = cmCodes;
        } catch (EazyException ex) {
            Util.addErrorLog(ex);
            respond.code = ex.getErrorCode();
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
        return respond;
    }

}
