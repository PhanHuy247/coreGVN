/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.chatserver.logging;

import com.vn.ntsc.chatserver.pojos.message.Message;

/**
 *
 * @author tuannxv00804
 */
public interface ILogger {
 
    public void log( Message msg );
    
    public void startService();
}
