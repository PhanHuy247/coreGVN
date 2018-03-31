/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.buzzserver.server.respond.result;

import eazycommon.constant.ParamKey;
import org.json.simple.JSONObject;
import com.vn.ntsc.buzzserver.entity.IEntity;
import com.vn.ntsc.buzzserver.server.respond.Respond;

/**
 *
 * @author DuongLTD
 */
public class EntityRespond extends Respond {

    public IEntity data;

    public EntityRespond() {
        super();
        this.data = null;
    }

    public EntityRespond(int code) {
        super(code);
        this.data = null;
    }

    public EntityRespond(int xErrorCode, IEntity xData) {
        super(xErrorCode);
        this.data = xData;
    }

    @Override
    public JSONObject toJsonObject() {
        JSONObject jo = super.toJsonObject();
        if (this.data != null) {
            jo.put(ParamKey.DATA, this.data.toJsonObject());
        }
        return jo;
    }

}
