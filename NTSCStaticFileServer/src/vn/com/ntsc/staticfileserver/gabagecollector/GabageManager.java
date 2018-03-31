/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.com.ntsc.staticfileserver.gabagecollector;

import java.util.concurrent.ConcurrentLinkedQueue;

/**
 *
 * @author tuannxv00804
 */
public class GabageManager {
    
    private static final ConcurrentLinkedQueue<GabageFile> queue = new ConcurrentLinkedQueue<>();
    
    public static void add( GabageFile file ){
        queue.add(file);
    }
    
    public static GabageFile poll(  ){
        return queue.poll();
    }
    
    
}
