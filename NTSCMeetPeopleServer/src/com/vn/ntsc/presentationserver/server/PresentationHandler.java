/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.presentationserver.server;

import com.vn.ntsc.presentationserver.server.response.R_GetMeetPeopleSetting;
import com.vn.ntsc.presentationserver.server.response.R_InsertPeople;
import com.vn.ntsc.presentationserver.server.response.R_UpdatePlace;
import com.vn.ntsc.presentationserver.server.response.R_MeetPeople;
import com.vn.ntsc.presentationserver.server.response.R_UpdateUser;
import com.vn.ntsc.presentationserver.server.response.R_UpdateDistance;
import com.vn.ntsc.presentationserver.server.response.R_RemoveUser;
import com.vn.ntsc.presentationserver.server.response.R_GetUsersPresentation;
import com.vn.ntsc.presentationserver.server.response.R_InitAllUsers;
import com.vn.ntsc.presentationserver.server.response.R_Login;
import com.vn.ntsc.presentationserver.server.response.R_UpdateMeetPeopleSetting;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.text.ParseException;
import java.util.LinkedList;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import eazycommon.util.Util;
import org.eclipse.jetty.server.handler.AbstractHandler;
import com.vn.ntsc.presentationserver.Core;

/**
 *
 * @author tuannxv00804
 */
public class PresentationHandler extends AbstractHandler{
    
    public Response handle( Request req ) throws ParseException{
        Response response = new Response( req.requestType );
        switch( req.requestType ){
            case Request.ReqType_MeetPeople:
                response.meetPeople = new R_MeetPeople();
                response.meetPeople.llUser = Core.meetPeopleProcessor.meetPeople( req );
                break;
            case Request.RepType_ListUserProfile:
                response.meetPeople = new R_MeetPeople();
                response.meetPeople.llUser = Core.meetPeopleProcessor.getListUserProfile(req );
                break;
            case Request.RepType_RemoveUser:
                response.removeUser = new R_RemoveUser();
                response.removeUser.isSuccess = Core.meetPeopleProcessor.removeUser( req );
                break;
            case Request.ReqType_GetUsersPresentation:
                response.usersPresentation = new R_GetUsersPresentation();
                response.usersPresentation.llUsers = Core.meetPeopleProcessor.getUsersPresentation( req );
                break;
            case Request.ReqType_InsertUser:
                response.insertPeople = new R_InsertPeople();
                response.insertPeople.insertResult = Core.meetPeopleProcessor.insert( req );
                break;
            case Request.RepType_UpdatePlace:
                response.updatePlace = new R_UpdatePlace();
                response.updatePlace.isSuccess = Core.meetPeopleProcessor.updatePlace(req);
                break;
            case Request.RepType_UpdateMeetPeopleSetting:
                response.updateMeetPeopleSetting = new R_UpdateMeetPeopleSetting();
                response.updateMeetPeopleSetting.isSuccess = Core.meetPeopleProcessor.updateMeetPeopleSetting(req);
                break;
            case Request.RepType_GetMeetPeopleSetting:
                response.getMeetPeopleSetting = new R_GetMeetPeopleSetting();
                response.getMeetPeopleSetting.setting = Core.meetPeopleProcessor.getMeetPeopleSetting(req);
                break;
            case Request.RepType_InitAllUsers:
                response.getInitAllUsers = new R_InitAllUsers();
                response.getInitAllUsers.isSuccess = Core.meetPeopleProcessor.initAllUsers();
                break;
            case Request.RepType_UpdateUser:
                response.updateUser = new R_UpdateUser();
                response.updateUser = Core.meetPeopleProcessor.updateUser(req);
                break;
            case Request.RepType_UpdateDistance:
                response.updateDistance = new R_UpdateDistance();
                response.updateDistance.isSuccess = Core.meetPeopleProcessor.updateDistance(req);
                break;
            case Request.RepType_GetUserOnline:
                response.numberOnline = Core.meetPeopleProcessor.getNumberUserOnline(req);
                break;
            case Request.RepType_AutoMessage:
                response.autoMessages = new LinkedList<>();
                response.autoMessages = Core.meetPeopleProcessor.autoMessage(req);
                break;
            case Request.RepType_Login:
                response.login = new R_Login();
                response.login.isChange = Core.meetPeopleProcessor.login(req);
                break;
            case Request.RepType_Logout:
                Core.meetPeopleProcessor.logout(req);
                break;
            case Request.RepType_DeletePresentation:
                Core.meetPeopleProcessor.deletePresentation(req);
                break;                  
            case Request.RepType_UpdatePresentation:
                Core.meetPeopleProcessor.updatePresentaion(req);
                break;                  
            case Request.RepType_AutoLogout:
                Core.meetPeopleProcessor.autoLogout(req);
                break;                  
        }
           
        return response;
    }

    @Override
    public void handle( String string, org.eclipse.jetty.server.Request rqst, HttpServletRequest hsr, HttpServletResponse response ) throws IOException, ServletException {
        response.setContentType("text/plain;charset=UTF-8");
        OutputStream out = response.getOutputStream();
        try{
            InputStreamReader isr = new InputStreamReader( rqst.getInputStream() );
            BufferedReader reader = new BufferedReader( isr );
            String requestStr = reader.readLine();
            Util.addDebugLog("request: " + requestStr);            
            Request request = Request.parseRequest( requestStr );
            
            Response res = handle( request );
            String str = res.toJson();
            if(str != null){
                Util.addDebugLog("respond: " + str);
                out.write( str.getBytes() );
                out.flush();
            }
        } catch( Exception ex ) {
            Util.addErrorLog(ex);            
           
        }
        out.close();
    }
    
}
