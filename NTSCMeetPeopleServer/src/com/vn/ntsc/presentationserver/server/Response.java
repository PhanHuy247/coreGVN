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
import com.vn.ntsc.presentationserver.server.response.R_GetUserOnline;
import com.vn.ntsc.presentationserver.server.response.R_InitAllUsers;
import com.vn.ntsc.presentationserver.server.response.R_Login;
import com.vn.ntsc.presentationserver.server.response.R_UpdateMeetPeopleSetting;
import java.util.Iterator;
import java.util.LinkedList;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 * 
 * @author tuannxv00804
 */
public class Response {
    public int requestType;
    public R_GetUsersPresentation usersPresentation;
    public R_InsertPeople insertPeople;
    public R_MeetPeople meetPeople;
    public R_RemoveUser removeUser;
    public R_UpdatePlace updatePlace;
    public R_UpdateMeetPeopleSetting updateMeetPeopleSetting;
    public R_GetMeetPeopleSetting getMeetPeopleSetting;
    public R_InitAllUsers getInitAllUsers;
    public R_UpdateUser updateUser;
    public R_UpdateDistance updateDistance;
    public R_GetUserOnline numberOnline;
    public LinkedList<String> autoMessages;
    public R_Login login;    
    
    public Response(){}
    
    public Response( int requestType ){
        this.requestType = requestType;
    }

    public String toString() {
        return "Response{" + "requestType=" + requestType + ", usersPresentation=" + usersPresentation + ", insertPeople=" + insertPeople + ", meetPeople=" + meetPeople + ", removeUser=" + removeUser + '}';
    }
    
//    public static Response fromJson( String str ){
//        try{
//            Gson g = new Gson();
//            Response res = g.fromJson( str, Response.class );
//            return res;
//        } catch( Exception ex ) {
//            Util.addErrorLog(ex);            
//           
//            return null;
//        }
//    }
    
    public String toJson(){
        switch(requestType ){
            case Request.ReqType_MeetPeople:
                return meetPeople.toJson();
            case Request.RepType_ListUserProfile:
                return meetPeople.toJson();
            case Request.RepType_UpdateMeetPeopleSetting:
                return updateMeetPeopleSetting.toJson();
            case Request.RepType_GetMeetPeopleSetting:
                return getMeetPeopleSetting.toJson();
            case Request.RepType_UpdateUser:
                return updateUser.toJson();
            case Request.RepType_GetUserOnline:
                return numberOnline.toJson();
            case Request.ReqType_InsertUser:{
                JSONObject jo = new JSONObject();
                jo.put("insertPeople", insertPeople.toJsonObject());
                return jo.toJSONString();
            }
            case Request.ReqType_GetUsersPresentation:{
                JSONObject jo = new JSONObject();
                jo.put("usersPresentation", usersPresentation.toJsonObject());
                return jo.toJSONString();
            }
            case Request.RepType_RemoveUser:{
                JSONObject jo = new JSONObject();
                jo.put("removeUser", removeUser.toJsonObject());
                return jo.toJSONString();
            }
            case Request.RepType_AutoMessage:{
                JSONArray jarr = new JSONArray();
                Iterator<String> it = autoMessages.iterator();
                while(it.hasNext()){
                    jarr.add(it.next());
                }
                return jarr.toJSONString();                 
            }
            case Request.RepType_Login:
                return login.toJson();              
        }
        return null;
//        try{
//            Gson g = new Gson();
//            String str = g.toJson( this );
//            return str;
//        } catch( Exception ex ) {
//            Util.addErrorLog(ex);            
//           
//            return null;
//        }
    }
}
