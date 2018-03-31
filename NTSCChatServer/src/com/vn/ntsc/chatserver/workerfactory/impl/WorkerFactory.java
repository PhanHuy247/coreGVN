/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.chatserver.workerfactory.impl;

import com.vn.ntsc.Config;
import com.vn.ntsc.chatserver.workerfactory.IFactory;
import com.vn.ntsc.chatserver.workerfactory.IWorker;

/**
 *
 * @author tuannxv00804
 */
public class WorkerFactory implements IFactory{

    @Override
    public void startFactory() {
        for( int i = 0; i < Config.WorkerThreadNumber; i++ ){
            IWorker w = new Worker();
            Thread t = new Thread( w );
            t.start();
        }
    }
    
}
