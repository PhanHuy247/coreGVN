/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.vn.ntsc.jpns.server.manager;

import java.util.Date;
import org.json.simple.JSONObject;

/**
 *
 * @author RuAc0n
 */
public interface IApiAdapter {
    
    public void execute(JSONObject obj, Date time);    
}
