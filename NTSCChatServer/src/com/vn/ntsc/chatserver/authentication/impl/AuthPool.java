/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.chatserver.authentication.impl;

import java.util.concurrent.ConcurrentLinkedQueue;
import com.vn.ntsc.Config;
import com.vn.ntsc.chatserver.authentication.IAuthPool;
import com.vn.ntsc.chatserver.authentication.IAuthThread;
/**
 *
 * @author tuannxv00804
 */
public class AuthPool implements IAuthPool{
    
    private static final int AuthThreadNumber = Config.AuthThreadNumber;
    
    protected static final ConcurrentLinkedQueue<UnAuthenticatedConnection> SocketQueue = new ConcurrentLinkedQueue<>();

    @Override
    public synchronized void put( UnAuthenticatedConnection con, int flag ) {
//        
//        Util.addInforLog(new Date(Util.currentTime()), "UnAuthenticateContion number : " + SocketQueue.size() + " flag : " + flag);
        SocketQueue.add( con );
    }

    @Override
    public synchronized UnAuthenticatedConnection poll() {
        return SocketQueue.poll();
    }

    @Override
    public void startAuthService() {
        for( int i = 0; i < AuthThreadNumber; i++ ){
            IAuthThread au = new AuthThread();
            Thread t = new Thread( au );
            t.start();
        }
    }

    @Override
    public int getAuthenNumber() {
        return SocketQueue.size();
    }
    
}
