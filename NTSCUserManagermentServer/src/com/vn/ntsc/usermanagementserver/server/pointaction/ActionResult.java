/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.vn.ntsc.usermanagementserver.server.pointaction;

/**
 *
 * @author RuAc0n
 */
public class ActionResult {
    
    public int code;
    public int point;

    public ActionResult(int code, int point) {
        this.code = code;
        this.point = point;
    }

}
