/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.backend.server.respond.result;

import org.json.simple.JSONObject;
import com.vn.ntsc.backend.server.respond.Respond;

/**
 *
 * @author RuAc0n
 */
public class CSVRespond extends Respond {

    private byte [] data;

    public CSVRespond(byte[] bytes) {
        this.data = bytes;
    }


    @Override
    public JSONObject toJsonObject() {
        return null;
    }

    @Override
    public byte [] toByte() {
        return this.data;
    }

}
