                /*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.chatserver.socketserver.impl;

import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import eazycommon.util.Util;
import com.vn.ntsc.Config;
import com.vn.ntsc.Core;
import com.vn.ntsc.chatserver.authentication.impl.UnAuthenticatedConnection;
import com.vn.ntsc.chatserver.socketserver.IServer;

/**
 *
 * @author tuannxv00804000
 */
public class Server implements IServer{
    
    private static final int MaxSocketSize = 1000000; //~1MB
    private static final int MaxQueueSize = 100000; //max 100000 client's connection can wait in the queue.
    
    @Override
    public void startListen(){
        Thread t = new Thread( this );
        t.start();
    }
    
    @Override
    public void run() {
        try{
//            ServerSocket server = new ServerSocket( Config.ChatServerPort );
//            server.setReuseAddress( true );
            
            ServerSocket server = new ServerSocket();
            server.setReuseAddress( true );
            server.setReceiveBufferSize( MaxSocketSize );
            server.bind( new InetSocketAddress( Config.ChatServerPort ), MaxQueueSize );
            
            while( true ){
                try{
                    
                    Socket soc = server.accept();
                    if(soc != null){
                        Util.addInfoLog("Socket " + soc.getRemoteSocketAddress() + " connect to server");
//                    
                        UnAuthenticatedConnection con = new UnAuthenticatedConnection( soc );
                        con.soc = soc;
                        Core.getAuthPool().put( con , 0);
                    }
                } catch( Exception ex ) {
                   
                }
            }
        } catch( Exception ex ) {
           
        }
    }

    
}
