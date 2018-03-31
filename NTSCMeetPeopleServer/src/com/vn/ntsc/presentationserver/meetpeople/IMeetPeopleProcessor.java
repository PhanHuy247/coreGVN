/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.presentationserver.meetpeople;

import java.util.LinkedList;
import com.vn.ntsc.presentationserver.server.Request;
import com.vn.ntsc.presentationserver.meetpeople.pojos.entity.QuerySetting;
import com.vn.ntsc.presentationserver.meetpeople.pojos.entity.User;
import com.vn.ntsc.presentationserver.server.response.R_GetUserOnline;
import com.vn.ntsc.presentationserver.server.response.R_UpdateUser;

/**
 *
 * @author tuannxv00804
 */
public interface IMeetPeopleProcessor {
    
    public LinkedList<User> meetPeople( Request request );
    
    public LinkedList<User> getListUserProfile( Request request );
    
    public LinkedList<User> getUsersPresentation( Request request );
    
    /**
     * 
     * @param request
     * @return  0: OK
     *          1: Unknown error
     */
    
    public boolean initAllUsers();
    
    public boolean insert( Request request );
    
    public boolean updatePlace(Request request);
    
    public boolean removeUser( Request request );
    
    public boolean updateMeetPeopleSetting(Request request);
    
    public QuerySetting getMeetPeopleSetting(Request request);
    
    public R_UpdateUser updateUser(Request request);
    
    public boolean updateDistance(Request request);
                
    public R_GetUserOnline getNumberUserOnline(Request request);
    
    public LinkedList<String> autoMessage(Request request);
    
    public boolean login(Request request);
    
    public void logout (Request request);
       
    public void deletePresentation (Request request);
    
    public void updatePresentaion( Request request);
    
    public void autoLogout( Request request);
}
