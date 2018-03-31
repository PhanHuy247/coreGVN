/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.chatserver.authentication.impl;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import eazycommon.util.Util;
import com.vn.ntsc.Config;
import com.vn.ntsc.Core;
import com.vn.ntsc.chatserver.authentication.IAuthThread;
import com.vn.ntsc.chatserver.messageio.MessageIO;
import com.vn.ntsc.chatserver.pojos.message.Message;
import com.vn.ntsc.chatserver.pojos.message.messagetype.MessageType;
import com.vn.ntsc.chatserver.pojos.message.messagetype.MessageTypeValue;
import com.vn.ntsc.chatserver.pojos.user.User;
import com.vn.ntsc.chatserver.pojos.user.UserConnection;
import com.vn.ntsc.utils.Validator;

/**
 *
 * @author tuannxv00804
 */
public class AuthThread implements IAuthThread {

    private static final int IdleLatency = Config.IdleThreadLatency;

    @Override
    public void run() {
        while( true ) {

            UnAuthenticatedConnection con = Core.getAuthPool().poll();
            if( con != null ) {

                if( con.soc != null ) {

                    boolean authenResult = false;
                    LinkedList<Message> ll = MessageIO.readMessageFromUAC( con );

                    for( int i = 0; ll != null && i < ll.size(); i++ ) {
                        Message msg = ll.get( i );
                        if( msg.msgType == MessageType.AUTH ) {
                            String username = msg.from;
                            String password = msg.value;
                            String authenString = authenFromToken(username, password);
                            if(username != null && !username.trim().isEmpty()){
                                authenResult = getAuthenResult(username, authenString);
                            }
                            if( authenResult ) {
                                Message authenSuccess = new Message( Config.ServerName, username, MessageType.AUTH, MessageTypeValue.Auth_AutheSuccess, Util.currentTime() ,null);
                                MessageIO.sendMessage( con, authenSuccess );
                                
                                Util.addInfoLog("Socket : " + con.soc.getRemoteSocketAddress().toString() + " with User: " + msg.from + ", authen success.");

                                User u = new User( username );
                                UserConnection uc = new UserConnection( con.soc, u );
//                                Util.addInfoLog("authen String : " + authenString );
//                                Util.addInfoLog("isSendReadMessage : " + isSendReadMessage(authenString) );
                                
                                uc.isSendReadMessage = isSendReadMessage(authenString);
                                /**
                                 * Sau khi authenticate thành công cho thằng user,
                                 * ta khởi tạo các dữ liệu cơ bản cho nó bao gồm:
                                 *  1. Init FriendList
                                 *  2. Init AccquaintainList
                                 *  3. Send online message cho bạn bè và accquaintance của thằng đó.
                                 *  4. Send các offline message xuống cho thằng đó.
                                 */
//                                initProceduce( uc );

                                
//                                
                                Core.getStoreEngine().add( uc );
                                break;

                            } else {
                                Util.addInfoLog("Socket : " + con.soc.getRemoteSocketAddress().toString() + " with User: " + msg.from + ", authen failure.");                                
                                Message authenFalse = new Message( Config.ServerName, username, MessageType.AUTH, MessageTypeValue.Auth_AuthenFalse, Util.currentTime(),null);
                                MessageIO.sendMessage( con, authenFalse );
                                
                            }
                        }
                    }

                    //Checking TimeToLive
                    if( !authenResult && con.TTL < Config.MaxTimeToLive ) {
//                        
                        con.TTL++;
                        Core.getAuthPool().put( con, 1 );
                    } else {
                        try {
                            if( authenResult ) {
                                
                            } else {
                                
                                con.soc.close();
                                Util.addInfoLog("Socket " + con.soc.getRemoteSocketAddress() + " close timeout");
                            }
                        } catch( Exception ex ) {
                            Util.addErrorLog(ex);
                        }
                    }

                } else {
                    //Bury the dead connection
                    try {
                        con.soc.close();
                        Util.addDebugLog("Socket " + con.soc.getRemoteSocketAddress() + " close die");
                    } catch( Exception ex ) {
                        Util.addErrorLog(ex);
                    }
                }
            } else {
                try {
                    //
                    Thread.sleep( IdleLatency );
                } catch( Exception ex ) {
                    Util.addErrorLog(ex);
                }
            }
        }
    }

    private String authenFromToken( String username, String password ) {
        if( Config.IsDebug ) {
            return String.valueOf(true);
        }
//        return true;
        String token = password;
        String urlStr = "http://" + Config.MainServer_IP + ":" +Config.MainServer_Port + "/checktoken=" + token;
        try {
            URL url = new URL( urlStr );
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod( "GET" );

            InputStreamReader isr = new InputStreamReader( con.getInputStream() );
            BufferedReader reader = new BufferedReader( isr );
            String line = reader.readLine();
            return line;
        } catch( Exception ex ) {
            Util.addErrorLog(ex);
        }
        return String.valueOf(false);
    }
    
    // maybe change condition of authen, dont need to call main service, success when it is user belong on system.
    @Override
    public boolean authen( String username, String password ) {
        return Validator.isUser(username);
    }

    private boolean getAuthenResult(String username, String authenString){
        if( authenString != null && authenString.contains( "true" ) ) {
            return true;
        }else{
            return false;
//            return Validator.isUser(username);
        }
    }
    
    private static final String SendReadMessageString = "send_read_message=";
    private static final Pattern SendReadMessagePattern = Pattern.compile( SendReadMessageString );
    
    private boolean isSendReadMessage(String authenString){
        Matcher matcher = SendReadMessagePattern.matcher( authenString );
        if( matcher.find() ){
            int end = matcher.end();
            String result = authenString.substring( end );
            if( result != null && result.contains( "true" ) ) {
                return true;
            }
        }
        return false;
    }
    
//    public static void main(String[] args) {
////        String authenString = "result=true&user_id=abc&send_read_message=false";
//        String authenString = "result=fase";
//        System.out.println(isSendReadMessage(authenString));
//    }
    
    private void initProceduce( UserConnection uc ) {
//        initLastChat( uc );
        /**
         * query friendslist and accquaintancelist
         * send online message to friends on friendslist and accquaintance list
         */
//        initFriendsList( uc );
//        initAccquaintanceList( uc );
        
//        notifyFriendsAndAccquaintance( uc );
//        sendUndeliveredMessage( uc );

    }

    /**
     *query from another service to init FriendList of a specified user.
     */
//    private void initFriendsList( UserConnection uc ) {
//        String userid = uc.user.username;
//        LinkedList<String> ll = Filer.getLLFriendList( userid );
//        if( ll != null ){
//            uc.user.FriendList = ll;
//        }
//    }

    /**
     * query from another service to init AccquaintainceList of a specified user.
     * @param uc 
     */
//    private void initAccquaintanceList( UserConnection uc ) {
//    }

    /**
     * Add presence message to friend of the specified user 's inbox.
     * Add to this user's inbox, presence status of friends and accquaintances.
     * @param uc 
     */
//    private void notifyFriendsAndAccquaintance( UserConnection uc ) {
//        if( uc == null 
//                || uc.user.lastChats == null ) return;
//        
//        Set<String> friends = uc.user.lastChats.keySet();
//        Iterator<String> iter = friends.iterator();
//        while( iter.hasNext() ){
//            String friendid = iter.next();
//            List<UserConnection> l = Core.getStoreEngine().gets( friendid );
//            
//            if( l == null || l.isEmpty() ) return;
//            for( int i = 0; i < l.size(); i++ ){
//                UserConnection f = l.get( i );
//                if( f != null 
//                        && MessageIO.isClientAlive( f.soc ) ){
//                    MessageIO.sendMessage( f, onlineMsg( uc.user.username, friendid ) );
//                }
//                
//            }
//        }
//    }

    private Message onlineMsg( String from, String to ){
        return new Message( from, to, MessageType.PRC, MessageTypeValue.Presence_Online );
    }
//    private void sendUndeliveredMessage( UserConnection uc ) {
//    }

//    private void initLastChat( UserConnection uc ) {
//        LinkedList<CaiThia> l = Filer.getLLCaiThia( uc.user.username );
//        if( l != null ){
//            uc.user.lastChats = l;
//        }
//    }
}
