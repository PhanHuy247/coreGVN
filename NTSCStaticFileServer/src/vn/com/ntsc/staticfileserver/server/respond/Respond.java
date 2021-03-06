/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package vn.com.ntsc.staticfileserver.server.respond;

import eazycommon.constant.ParamKey;
import eazycommon.constant.ErrorCode;
import org.json.simple.JSONObject;

/**
 *
 * @author RuAc0n
 */
public class Respond {
    
    public int code;

    public Respond(int code) {
        this.code = code;
    }

    public Respond() {
        this.code = ErrorCode.UNKNOWN_ERROR;
    }
    
    public JSONObject toJsonObject(){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(ParamKey.ERROR_CODE, code);
        return jsonObject;
    }

    public byte[] toByte(){
        return toJsonObject().toJSONString().getBytes();
    }
}