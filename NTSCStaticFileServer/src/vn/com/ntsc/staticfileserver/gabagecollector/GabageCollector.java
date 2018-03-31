/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.com.ntsc.staticfileserver.gabagecollector;

import eazycommon.constant.Constant;
import eazycommon.util.Util;

/**
 *
 * @author duongle
 */
public class GabageCollector extends Thread {

    public static void startGabageCollector() {
        GabageCollector g = new GabageCollector();
        g.start();
    }

    @Override
    public void run() {
        while( true ) {
            try{
                GabageFile file = GabageManager.poll();
                long now = Util.currentTime();
                if(file != null){
                    if(file.deleteTime > now){
                        Util.deleteFile(file.fileName);
                    }
                }else{
                    Thread.sleep(Constant.A_MINUTE);
                }
            } catch( Exception ex ) {
                Util.addErrorLog(ex);                
            }
        }
    }
}
