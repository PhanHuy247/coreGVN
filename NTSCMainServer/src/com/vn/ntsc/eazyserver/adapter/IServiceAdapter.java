/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.eazyserver.adapter;

import com.vn.ntsc.eazyserver.server.request.Request;

/**
 *
 * @author tuannxv00804
 */
public interface IServiceAdapter {
    
    //public String callService( String requestStr );
    
    //public String callService( String requestStr , String userID );
    
    public String callService( Request request );
}
