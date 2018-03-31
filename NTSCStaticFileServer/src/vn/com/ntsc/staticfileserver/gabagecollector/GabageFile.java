/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package vn.com.ntsc.staticfileserver.gabagecollector;

import eazycommon.util.Util;

/**
 *
 * @author Rua
 */
public class GabageFile {
    
    private static final long BUFFER_TIME = 10L * 60 * 1000; 
    
    public String fileName;
    public long deleteTime;
    
    public GabageFile(String fileName){
        this.fileName = fileName;
        this.deleteTime = Util.currentTime() + BUFFER_TIME;
    }
    
}
