/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.jpns.server.manager.impl;

import java.util.Date;
import org.json.simple.JSONObject;
import com.vn.ntsc.jpns.Config;
import com.vn.ntsc.jpns.server.manager.IApiAdapter;

/**
 *
 * @author Administrator
 */
public class ResetConfigApi implements IApiAdapter{

    @Override
    public void execute(JSONObject obj, Date time) {
        Config.initConfig();
    }
    
}
