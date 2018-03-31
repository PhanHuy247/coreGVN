/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.presentationserver.server;

import com.vn.ntsc.presentationserver.server.request.DeletePresentation;
import com.vn.ntsc.presentationserver.server.request.UpdateDistance;
import com.vn.ntsc.presentationserver.server.request.GetUserOnline;
import com.vn.ntsc.presentationserver.server.request.UpdatePlace;
import com.vn.ntsc.presentationserver.server.request.AutoLogout;
import com.vn.ntsc.presentationserver.server.request.UpdateUser;
import com.vn.ntsc.presentationserver.server.request.GetMeetPeopleSetting;
import com.vn.ntsc.presentationserver.server.request.InsertUser;
import com.vn.ntsc.presentationserver.server.request.RemoveUser;
import com.vn.ntsc.presentationserver.server.request.Login;
import com.vn.ntsc.presentationserver.server.request.Logout;
import com.vn.ntsc.presentationserver.server.request.AutoMessage;
import com.vn.ntsc.presentationserver.server.request.GetUsersPresentation;
import com.vn.ntsc.presentationserver.server.request.UpdatePresentation;
import com.vn.ntsc.presentationserver.server.request.MeetPeople;
import com.vn.ntsc.presentationserver.server.request.UpdateMeetPeopleSetting;
import eazycommon.constant.API;
import eazycommon.constant.ParamKey;
import eazycommon.util.Util;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import com.vn.ntsc.presentationserver.Config;

/**
 *
 * @author tuannxv00804
 */
public class Request {

    public static final int ReqType_MeetPeople = 0;
    public static final int ReqType_GetUsersPresentation = 2;
    public static final int ReqType_InsertUser = 3;
    public static final int RepType_RemoveUser = 5;
    public static final int RepType_UpdatePlace = 6;
    public static final int RepType_UpdateMeetPeopleSetting = 7;
    public static final int RepType_GetMeetPeopleSetting = 8;
    public static final int RepType_InitAllUsers = 9;
    public static final int RepType_UpdateUser = 10;
    public static final int RepType_UpdateDistance = 11;
    public static final int RepType_GetUserOnline = 15;
    public static final int RepType_AutoMessage = 16;
    public static final int RepType_Login = 18;
    public static final int RepType_Logout = 19;
    public static final int RepType_DeletePresentation = 21;    
    public static final int RepType_UpdatePresentation = 22;    
    public static final int RepType_ListUserProfile = 23;    
    public static final int RepType_AutoLogout = 24;    
    /**
     * 0: MeetPeople
     * 1: GetUser by manhattanDistance.
     * 2: Get user's presentation. (return {is_online, lon, lat})
     * 3: Insert people
     * 4: Update presentation 
     * 5: Remove user
     * 6: Update place
     * 7: Update meet people setting
     * 8: Get meet people setting
     * 9: Init all user
     */
    public int requestType;
    public MeetPeople meetPeople;
    public GetUsersPresentation getUsersPresentation;
    public InsertUser insertUser;
    public UpdatePresentation updatePresentation;
    public RemoveUser removeUser;
    public UpdatePlace updatePlace;
    public UpdateMeetPeopleSetting updateMeetPeopleSetting;
    public GetMeetPeopleSetting getMeetPeopleSetting;
    public UpdateUser updateUser;
    public UpdateDistance updateDistance;
    public GetUserOnline getUserOnline;
    public AutoMessage autoMessage;
    public Login login;
    public Logout logout;
    public AutoLogout autoLogout;
    public DeletePresentation deletePresentation;      
            
    public static Request parseRequest( String requestStr ) {
        Request request = new Request();
        JSONParser jParser = new JSONParser();
        try {
            JSONObject jObject = (JSONObject) jParser.parse( requestStr );
            String api = (String) jObject.get( ParamKey.API_NAME );
            if( api.equals( API.MEET_PEOPLE ) ) {
                request.requestType = Request.ReqType_MeetPeople;
                request.meetPeople = new MeetPeople(requestStr);
                
            } else if( api.equals( API.LIST_USER_PROFILE ) ) {
                request.requestType = Request.RepType_ListUserProfile;
                request.meetPeople = new MeetPeople(requestStr);
            } else if( api.equals( API.REGISTER ) || api.equals( API.REGISTER_VERSION_2) ) {
                request.requestType = Request.ReqType_InsertUser;
                request.insertUser = new InsertUser(requestStr);
            } else if( api.equals( API.UPDATE_MEET_PEOPLE_SETTING ) ) {
                request.requestType = Request.RepType_UpdateMeetPeopleSetting;
                request.updateMeetPeopleSetting = new UpdateMeetPeopleSetting(requestStr);
            
            } else if( api.equals( API.GET_MEET_PEOPLE_SETTING ) ) {
                request.requestType = Request.RepType_GetMeetPeopleSetting;
                request.getMeetPeopleSetting = new GetMeetPeopleSetting(requestStr, requestStr);
            
            } else if( api.equals( API.GET_USER_PRESENTATION ) ){
                request.requestType = Request.ReqType_GetUsersPresentation;
                request.getUsersPresentation = new GetUsersPresentation(requestStr);
            } 
            else if(api.equals(API.UPDATE_USER_INFOR) || api.equals(API.UPDATE_AVATAR) || api.equals(API.UPDATE_LOCATION)){
                request.requestType = Request.RepType_UpdateUser;
                request.updateUser = new UpdateUser(requestStr);
            }
            else if(api.equals(API.UPDATE_DISTANCE)){
                request.requestType = Request.RepType_UpdateDistance;
                request.updateDistance = new UpdateDistance(requestStr);
            }
            else if(api.equals(API.DEACTIVATE)){
                request.requestType = Request.RepType_RemoveUser;
                request.removeUser = new RemoveUser(requestStr, 0);
            }
            else if(api.equals(API.GET_USER_ONLINE)){
                request.requestType = Request.RepType_GetUserOnline;
                request.getUserOnline = new GetUserOnline(requestStr);
            }
            else if(api.equals(API.AUTO_MESSAGE)){
                request.requestType = Request.RepType_AutoMessage;
                request.autoMessage = new AutoMessage(requestStr);
            }
            else if(api.equals(API.LOGIN)){
                request.requestType = Request.RepType_Login;
                request.login = new Login(requestStr);
            }            
            else if(api.equals(API.LOG_OUT)){
                request.requestType = Request.RepType_Logout;
                request.logout = new Logout(requestStr);
            }
            else if(api.equals(API.DELETE_PRESENTATION)){
                request.requestType = Request.RepType_DeletePresentation;
                request.deletePresentation = new DeletePresentation(requestStr);
            }            
            else if(api.equals(API.UPDATE_PRESENTATION)){
                request.requestType = Request.RepType_UpdatePresentation;
                request.updatePresentation = new UpdatePresentation(requestStr);
            }            
            else if(api.equals(API.AUTO_LOG_OUT)){
                request.requestType = Request.RepType_AutoLogout;
                request.autoLogout = new AutoLogout(requestStr);
            }                
            else if( api.equals( API.RESET_CONFIG ) ) {
                Config.initConfig();                
            }
        } catch( Exception ex ) {
            Util.addErrorLog(ex);            
           
        }

        /*      
        try {
        Gson g = new Gson();
        request = g.fromJson( requestStr, Request.class );
        } catch( Exception ex ) {
       
        }
         * 
         */
        return request;
    }

//    public String toJson() {
//        String res = "";
//        try {
//            Gson g = new Gson();
//            res = g.toJson( this );
//            return res;
//        } catch( Exception ex ) {
//           
//            return res;
//        }
//    }

    public String sendRequest( String serverIP, int serverPort ) {
//        try {
//            String req = "";
//            if( meetPeople != null ) {
//                req = JsonUtil.toJson( meetPeople );
//            } else if( getUserByManhattanDistance != null ) {
//                req = JsonUtil.toJson( getUserByManhattanDistance );
//            } else if( getUsersPresentation != null ) {
//                req = JsonUtil.toJson( getUsersPresentation );
//            } else if( insertUser != null ) {
//                req = JsonUtil.toJson( insertUser );
//            } else if( updatePresentation != null ) {
//                req = JsonUtil.toJson( updatePresentation );
//            } else if( removeUser != null ) {
//                req = JsonUtil.toJson( removeUser );
//            } else if( updatePlace != null ) {
//                req = JsonUtil.toJson( updatePlace );
//            } else if( updateMeetPeopleSetting != null ) {
//                req = JsonUtil.toJson( updateMeetPeopleSetting );
//            } else if( getMeetPeopleSetting != null ) {
//                req = JsonUtil.toJson( getMeetPeopleSetting );
//            } else if(updateUser != null){
//                req = JsonUtil.toJson( updateUser);
//            } else if(updateDistance != null){
//                req = JsonUtil.toJson( updateDistance);
//            } else if(look != null){
//                req = JsonUtil.toJson( look);
//            } else if(winkBoom != null){
//                req = JsonUtil.toJson( winkBoom);
//            } else if(shakeChat != null){
//                req = JsonUtil.toJson( shakeChat);
//            } else if(getUserOnline != null){
//                req = JsonUtil.toJson( getUserOnline);
//            } else if(autoMessage != null){
//                req = JsonUtil.toJson(autoMessage);
//            }
//          
//            StringBuilder builder = new StringBuilder( "http://" ).append( serverIP ).append( ":" ).append( serverPort ).append( "/" );
//
//            URL url = new URL( builder.toString() );
//
//            HttpURLConnection con = (HttpURLConnection) url.openConnection();
//            con.setDoOutput(true);
//            con.setRequestMethod( "POST" );
//            StringBuilder postData = new StringBuilder();
//            postData.append(req);
//            String encodedData = postData.toString();
//            // send data by byte
//            con.setRequestProperty("Content-Language", "en-US");
//            con.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
//            con.setRequestProperty("Content-Length",(new Integer(encodedData.length())).toString());
//            byte[] postDataByte = postData.toString().getBytes("UTF-8");
//            try{
//                OutputStream out = con.getOutputStream();
//                out.write(postDataByte);
//                out.close();
//            }catch(IOException ex){
//                Util.addErrorLog(ex);
//               
//            }
//            
//            InputStreamReader isr = new InputStreamReader( con.getInputStream() );
//            BufferedReader reader = new BufferedReader( isr );
//
//            String line = reader.readLine();
//            return line;
//        } catch( Exception ex ) {
//            Util.addErrorLog(ex);            
           
            return null;
//        }
    }
}
