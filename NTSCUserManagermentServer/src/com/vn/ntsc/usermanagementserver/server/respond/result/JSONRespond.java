/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.usermanagementserver.server.respond.result;

import com.vn.ntsc.usermanagementserver.server.respond.Respond;
import org.json.simple.JSONObject;

/**
 *
 * @author phanhuy
 */
public class JSONRespond extends Respond {
    
    public JSONObject jsonData = new JSONObject();

    public JSONRespond() {
        super();
    }
    
    @Override
    public JSONObject toJsonObject() {
        return jsonData;
    }
    
}
