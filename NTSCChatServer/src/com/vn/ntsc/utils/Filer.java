/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.utils;

import java.util.HashMap;
import eazycommon.util.Util;
import com.vn.ntsc.Config;

/**
 *
 * @author tuannxv00804
 */
public class Filer {
    
    private static final HashMap<String, Long> BlockUserLastReadTimeMap = new HashMap<>();
    public static long getLastReadTimeBlockUser( String userid, String friendID ){
        if( userid == null || friendID == null ) return Util.currentTime() - Config.OneYear;
        String fileName = userid + "&" + friendID;
        Long lastReadtime = BlockUserLastReadTimeMap.get( fileName );
        long readTime = lastReadtime == null ? Util.currentTime() - Config.OneYear : lastReadtime;
        return readTime;
    }
    
    public static void putLastReadTimeBlockUser( String userid, String friendID, long readTime ){
        if( userid == null || friendID == null ) return;
        String fileName = userid + "&" + friendID;
        BlockUserLastReadTimeMap.put( fileName, readTime );
    }
    

}
