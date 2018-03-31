/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.jpns.server.workers;

import org.json.simple.JSONObject;
import com.vn.ntsc.jpns.dao.pojos.Device;

/**
 *
 * @author tuannxv00804
 */
public class ReadyPackage {

    public JSONObject msg;
    public Device device;
    
    public ReadyPackage(){}
    
    public ReadyPackage( Device d, JSONObject msg ){
        this.msg = msg;
        this.device = d;
    }
}
