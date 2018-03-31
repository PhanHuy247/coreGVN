/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.chatserver.authentication.impl;

import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import com.vn.ntsc.chatserver.messageio.MessageIO;
import eazycommon.util.Util;

/**
 *
 * @author tuannxv00804
 */
public class UnAuthenticatedConnection {
    public Socket soc;
    public int TTL = 0;
    
    public OutputStreamWriter writer;
    public InputStreamReader reader;
    
    private static final int ReadSocTimeOut = MessageIO.ReadSocLatency;
    private static final String CharSet = "UTF-8";
    
    public UnAuthenticatedConnection( Socket soc ){
        this.soc = soc;
        try{
            writer = new OutputStreamWriter( soc.getOutputStream(), CharSet );
            reader = new InputStreamReader( soc.getInputStream(), CharSet );
            this.soc.setSoTimeout( ReadSocTimeOut );
        } catch( Exception ex ) {
            Util.addErrorLog(ex);
        }
    }
}
