/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.backend.server.respond.impl.backend.cmcode;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.json.simple.JSONObject;
import eazycommon.constant.ErrorCode;
import eazycommon.exception.EazyException;
import com.vn.ntsc.backend.dao.cmcode.AfficiateDAO;
import com.vn.ntsc.backend.dao.cmcode.MediaDAO;
import com.vn.ntsc.backend.entity.IEntity;
import com.vn.ntsc.backend.entity.impl.cmcode.Afficiate;
import com.vn.ntsc.backend.entity.impl.cmcode.Media;
import com.vn.ntsc.backend.server.respond.IApiAdapter;
import com.vn.ntsc.backend.server.respond.Respond;
import com.vn.ntsc.backend.server.respond.result.ListEntityRespond;
import eazycommon.util.Util;

/**
 *
 * @author RuAc0n
 */
public class InitRegisterCMCodeApi implements IApiAdapter {

    @Override
    public Respond execute(JSONObject obj) {
        ListEntityRespond respond = new ListEntityRespond();
        try {
            Map<String, IEntity> mapAff = AfficiateDAO.getMapName();
            List<IEntity> listMedia = MediaDAO.getListName();
            for (IEntity media : listMedia) {
                String affId = ((Media) media).afficiateId;
                Afficiate afficiate = (Afficiate) mapAff.get(affId);
                afficiate.mediaList.add((Media) media);
            }
            List<IEntity> list = new ArrayList<IEntity>();
            for (Map.Entry<String, IEntity> pairs : mapAff.entrySet()) {
                IEntity afficiate = pairs.getValue();
                list.add(afficiate);
            }
            respond = new ListEntityRespond(ErrorCode.SUCCESS, list);
        } catch (EazyException ex) {
            Util.addErrorLog(ex);
            respond = new ListEntityRespond(ex.getErrorCode());
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
        return respond;
    }

}
