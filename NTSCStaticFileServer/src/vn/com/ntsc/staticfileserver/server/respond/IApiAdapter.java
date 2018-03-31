/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package vn.com.ntsc.staticfileserver.server.respond;

import java.util.Date;
import vn.com.ntsc.staticfileserver.server.Request;

/**
 *
 * @author RuAc0n
 */
public interface IApiAdapter {
    
    public Respond execute(Request request, Date time);    
}
