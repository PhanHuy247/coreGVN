/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.jpns.dao;

import java.util.LinkedList;
import com.vn.ntsc.jpns.dao.pojos.Device;
import java.util.List;

/**
 *
 * @author tuannxv00804
 */
public interface IDAO {
    
    public void add( Device user );
    
    public LinkedList<Device> getDevices( String userid );
    public Device getDevice( String deviceId );
    public List<String> getListDeviceId(String userId);
    
    public String getUsername( String userid );
    
    public void remove( Device user );
    
    public void removeByToken( String token );    

    public void removeDuplicatedOrEmtyTokenDevice(Device user);
    
    public void removeDuplicatedDeviceId(Device user);
    
    public void removeUser(String userId);
    
    public void updateUserName(String userId, String userName);
    
    public boolean isExistDevice(String userId, String deviceToken);
    
    public void updateDeviceToken(Device device, String newDeviceToken);
}
