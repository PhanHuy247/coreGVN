/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package vn.com.ntsc.staticfileserver.server.respond.result;

import vn.com.ntsc.staticfileserver.server.respond.Respond;

/**
 *
 * @author RuAc0n
 */
public class ByteRespond extends Respond{
    
    public byte[] data;
    
    public ByteRespond() {
        super();
        this.data = null;
    }

    public ByteRespond(int code) {
        super(code);
        this.data = null;
    }

    public ByteRespond(byte[] xData) {
        this.data = xData;
    }    
    
    @Override
    public byte[] toByte(){
        if(this.data != null)
            return this.data;
        else
            return super.toJsonObject().toJSONString().getBytes();
    }     
    
}
