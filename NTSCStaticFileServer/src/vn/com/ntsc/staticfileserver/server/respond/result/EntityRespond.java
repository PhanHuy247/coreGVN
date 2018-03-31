/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package vn.com.ntsc.staticfileserver.server.respond.result;

import eazycommon.constant.ParamKey;
import org.json.simple.JSONObject;
import vn.com.ntsc.staticfileserver.entity.IEntity;
import vn.com.ntsc.staticfileserver.server.respond.Respond;


/**
 *
 * @author DuongLTD
 */
public class EntityRespond extends Respond {

    public IEntity data;
    public String responseString;
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
    
    public EntityRespond(int xErrorCode, String xData,String noImportant) {
        super(xErrorCode);
        this.responseString = xData;
    }

    @Override
    public JSONObject toJsonObject() {
        JSONObject jo = super.toJsonObject();
        if (this.data != null) {
            jo.put(ParamKey.DATA, this.data.toJsonObject());
        }
        if (this.responseString != null) {
            jo.put("new_token", this.responseString);
        }
        return jo;
    }
   
}
