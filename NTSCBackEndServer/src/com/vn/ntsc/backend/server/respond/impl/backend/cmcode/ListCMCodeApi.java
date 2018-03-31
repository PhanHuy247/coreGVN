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

/**
 *
 * @author RuAc0n
 */
public class ListCMCodeApi implements IApiAdapter {

    @Override
    public Respond execute(JSONObject obj) {
        EntityRespond respond = new EntityRespond();
        try {
            String affId = Util.getStringParam(obj, ParamKey.AFFICIATE_ID);
            String mediaId = Util.getStringParam(obj, "media_id");
            String code = Util.getStringParam(obj, ParamKey.CM_CODE);
            Long flag = Util.getLongParam(obj, ParamKey.FLAG);
            Long type = Util.getLongParam(obj, ParamKey.TYPE);
            Long skip = Util.getLongParam(obj, ParamKey.SKIP);
            Long take = Util.getLongParam(obj, ParamKey.TAKE);
            if (skip == null && take == null) {
                return null;
            }
            SizedListData map = CMCodeDAO.search(affId, mediaId, code, flag, type, skip.intValue(), take.intValue());
            int total = map.total;
            List<IEntity> cmList = map.ll;
            Map<String, IEntity> mapAfficiate = AfficiateDAO.getMapName();
            Map<String, IEntity> mapMedia = MediaDAO.getMapName();
            for (IEntity cmList1 : cmList) {
                CMCode cmCode = (CMCode) cmList1;
                cmCode.mediaName = ((Media) mapMedia.get(cmCode.mediaId)).mediaName;
                cmCode.affName = ((Afficiate) mapAfficiate.get(cmCode.afficiateId)).shopName;
            }
            ListCMCodeData data = new ListCMCodeData(cmList, total);
            respond = new EntityRespond(ErrorCode.SUCCESS, data);
        } catch (EazyException ex) {
            Util.addErrorLog(ex);
            respond = new EntityRespond(ex.getErrorCode());
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
        return respond;
    }

}
