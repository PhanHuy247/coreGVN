/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.test.testclient;

import com.test.testclient.handlers.AuthenMessageHandler;
import com.test.testclient.handlers.ChatMessageHandler;
import com.test.testclient.handlers.MessageStatusHandler;
import com.test.testclient.handlers.PresenceMessageHandler;
import com.vn.ntsc.chatclient.Client;
import com.vn.ntsc.chatclient.listeners.IAuthenListener;
import com.vn.ntsc.chatclient.listeners.IChatListener;
import com.vn.ntsc.chatclient.listeners.IGenericMessagesListener;
import java.net.Socket;
import java.util.Date;
import java.util.Scanner;
//import vn.com.ntqsolution.chatlog.client.ILogClient;
//import vn.com.ntqsolution.chatlog.client.impl.LogClient;
import com.vn.ntsc.Config;
import com.vn.ntsc.chatclient.listeners.ICallListener;
import com.vn.ntsc.chatclient.listeners.IFileListener;
import com.vn.ntsc.chatclient.listeners.IWinkMessageListener;
import com.vn.ntsc.chatserver.messageio.MessageIO;
import com.vn.ntsc.chatserver.pojos.message.Message;
import com.vn.ntsc.chatserver.pojos.message.messagetype.MessageType;
import com.vn.ntsc.chatserver.pojos.message.messagetype.MessageTypeValue;
import com.vn.ntsc.chatserver.pojos.message.messagetype.SendFileMessage;

/**
 *
 * @author tuannxv00804
 */
public class TestClient {
//    public static final String server = "192.168.6.226";
//    public static final String server = "202.32.202.162";
    public static final String server = "localhost";
    
//    public static String username = "hungbm3@gmail.com";
    public static String username = "duong";
    
    public static void main( String[] args ) {
        
        Client client = initClient();
        startChat( client );

        System.out.println( "Client:" );
        System.out.println( "Local port: " + client.soc.getLocalPort() );
        System.out.println( "Local Socket: " + client.soc.getLocalSocketAddress() );
        System.out.println( "Local InetAddress: " + client.soc.getLocalAddress() );

        System.out.println( "Remote address:" + client.soc.getRemoteSocketAddress() );
        
    }

    private static Client initClient( ){
        final int port = Config.ChatServerPort;
        
        Client client = new Client( server, port );
        
        client.addAuthenListener( new AuthenMessageHandler() );
        client.addChatListener( new ChatMessageHandler() );
        client.addPresenceListener( new PresenceMessageHandler() );
        client.addMessgeStatusListener( new MessageStatusHandler() );
        client.addWinkMessageListener( new IWinkMessageListener() {

            @Override
            public void handle( Message msg ) {
                System.out.println( "You've got a WINK from: " + msg.from );
                msg.printStruct();
            }
        });
 
        client.addFileListener( new IFileListener() {

            @Override
            public void handle( Message msg ) {
                System.out.println( "File sending from: " + msg.from + ", to: " + msg.to + ", value: " + msg.value );
            }
        });
        
        client.addGenericMessageListener( new IGenericMessagesListener() {

            @Override
            public void handle( Message msg ) {
                if( msg.msgType == MessageType.GIFT ){
                    System.out.println( "Gift from: " + msg.from );
                    System.out.println( "GiftID = " + msg.value );
                    msg.printStruct();
                }else{
//                    System.out.println( "Generic is NOT GIFT. From = " + msg.from + ", type = " + msg.msgType + ", value = " + msg.value );
                }
            }
        });
        
        client.addGenericMessageListener( new IGenericMessagesListener() {

            @Override
            public void handle( Message msg ) {
                if( msg.msgType == MessageType.CMD ){
                    System.out.println( "Msg from: " + msg.from + ". CMD. value = " + msg.value );
                }
            }
        });
        
        client.addCallListener( new ICallListener() {

            @Override
            public void handle( Message msg ){
                System.out.println( "A call have been made from: " + msg.from );
                System.out.println( "Call type: " + msg.msgType );
                System.out.println( "Value: " + msg.value );
            }
        });
        
        String password = "38137b36-c485-4243-a67b-92f9e4e3712a";
        client.sendAuthenMessage( username, password );
        
        return client;
    }
    
    private static void startChat( Client client ) {
        while( true ){
            try{
                System.out.println(  );
                Scanner scan = new Scanner( System.in );
                System.out.println( "To: " );
                String to = scan.nextLine();
                
                String winkCmd = "wink";
                String getlog = "getlog";
                String friendlistRequest = "friendlist";
                String markread = "markread";
                String getFriendStatus = "friendStatus";
                String lastRead = "lastread";
                String loop = "loop";
                String wink = "wink";
                String sendFile = "sendfile";
                String confirmFile = "confirmfile";
                String block = "block";
                String gift = "gift";
                
                if( to.equals( winkCmd ) ){
                    
                    System.out.println( "To user: "  );
                    String friendName = scan.nextLine();
                    Message winkMsg = new Message( client.username, friendName, MessageType.WINK, "" );
                    MessageIO.sendMessage( client, winkMsg );
                    System.out.println( "Wink message was sent to " + friendName );
                    
                }else if( to.equals( getlog ) ){
                    
                    //user type: getlog
                    //then: from sonto
                    
//                    ILogClient logClient = new LogClient( Config.ChatLogServer_IP, Config.ChatLogServer_Port );
                    System.out.println( "What do you wanna get from logger?" );
                    String str = scan.nextLine();
                    String cmd = str.split( " " )[0];
                    String toUsername = str.split( " " )[1];
                    
                    System.out.println( "Date: " );
                    String dateStr = scan.nextLine();
                    String[] eles = dateStr.split( " " );
                    int year = Integer.parseInt( eles[0] );
                    int month = Integer.parseInt( eles[1] );
                    int day = Integer.parseInt( eles[2] );
                    Date d = new Date( year, month, day );
                    
                    System.out.println( "Get log is NOT available now. Date = " + d );
                    if( cmd.equals( "from" ) ){
//                        LinkedList<Message> ll = logClient.getLogMsg_SentFrom( client.username, toUsername, d );
//                        for( int i = 0; i < ll.size(); i++ ){
//                            System.out.println( ll.get( i ) );
//                        }
                    }else if( cmd.equals( "to") ){
//                        LinkedList<Message> ll = logClient.getLogMsg_SentTo( client.username, toUsername, d );
//                        for( int i = 0; i < ll.size(); i++ ){
//                            System.out.println( ll.get( i ) );
//                        }
                    }
                        
                }else if( to.equals( friendlistRequest ) ){
                    client.requestFriendListStatus();
                  
                }else if( to.equals( markread ) ){
                    System.out.println( "Enter your messageid: " );
                    String messageid = scan.nextLine();
                    client.markMessageAsRead( messageid );
                    System.out.println( "Message: " + messageid + " is marked as read." );
                    
                }else if( to.equals( getFriendStatus ) ){
                    System.out.println( "Friend username: " );
                    String friendUserName = scan.nextLine();
                    client.requestFriendStatus( friendUserName );
                  
                }else if( to.equals( lastRead ) ){    
                    System.out.println( "Sending lastread message, to user: " );
                    String friendID = scan.nextLine();
                    client.sendLastReadTime( friendID, new Date() );
                    
                }else if( to.equals( loop ) ){
                    int maxLoop = 300;
                    String friendID = client.username.equals( "a" ) ? "b" : "a";
                    for( int i = 0; i < maxLoop; i++ ){
                        String str = "a" + i;
                        Message msg = new Message( client.username, friendID, MessageType.PP, str );
                        MessageIO.sendMessage( client, msg );
//                        sleep( 1000 );
                    }
                    
                }else if( to.equals( wink ) ){
                    System.out.println( "Wink to user: " );
                    String toUser = scan.nextLine();
                    Message msg = new Message( client.username, toUser, MessageType.WINK, " " );
                    MessageIO.sendMessage( client, msg );
                    
                }else if( to.equals( sendFile ) ){
                    System.out.println( "SendFile to: " );
                    String toUser = scan.nextLine();
                    String value = "p";
                    Message msg = new Message( client.username, toUser, MessageType.FILE, value );
                    MessageIO.sendMessage( client, msg );
                    System.out.println( "MSGID: " + msg.id );
                    
                    System.out.println( "ádfuiop" );
                    msg.printStruct();
                    
                }else if( to.equals( confirmFile ) ){
                    System.out.println( "Confirm to: " );
                    String toUser = scan.nextLine();
                    System.out.println( "FileID: " );
                    String fileID = scan.nextLine();
                    System.out.println( "FileName: " );
                    String fileName = scan.nextLine();
                    System.out.println( "MsgID: " );
                    String msgID = scan.nextLine();
                    
                    String value = SendFileMessage.createValue( msgID, fileID, fileName );
                    Message msg = new Message( client.username, toUser, MessageType.FILE, value );
                    MessageIO.sendMessage( client, msg );
                    
                    System.out.println( "ádfuiop" );
                    msg.printStruct();
                    
                }else if( to.equals( block ) ){
                    System.out.println( "UserID: " );
                    String toUser = scan.nextLine();
                    String value = MessageTypeValue.CMD_Block_value( toUser );
                    Message msg = new Message( client.username, toUser, MessageType.CMD, value );
                    MessageIO.sendMessage( client, msg );
                    msg.printStruct();
                    
                } else if ( to.equals( gift ) ){
                    System.out.println( "to UserID: " );
                    String toUserID = scan.nextLine();
                    Message msg = new Message( client.username, toUserID, MessageType.GIFT, "helloGift ID" );
                    MessageIO.sendMessage( client, msg );
                    msg.printStruct();
                    
                }else if( to.equals( "v" ) ){
                    
                    System.out.println( "msgtype (SVOICE, EVOICE, SVIDEO, EVIDEO): " );
                    String msgType = scan.nextLine();
                    System.out.println( "toUser: " );
                    String toUserID = scan.nextLine();
                    Message msg = new Message( client.username, toUserID, MessageType.valueOf( msgType ), "hello voice and video :)" );
                    MessageIO.sendMessage( client, msg );
                    
                    msg.printStruct();
                    
                }else{
                    
                    System.out.println( "Message: " );
                    String str = scan.nextLine();
                    String jp = " 日本語";
                    str += jp;
//                    System.out.println( "str = " + str );
                    
                    Message msg = new Message( client.username, to, MessageType.PP, str );
                    System.out.print( "Struct: " ); msg.printStruct();
                    
                    System.out.println( "MsgID = " + msg.id );
                    MessageIO.sendMessage( client, msg );
                    System.out.println( "Sending message: " + msg );
                
                }
                
            } catch( Exception ex ) {
               
            }
        }
    }

    private static void sleep( int delay ){
        try{
            Thread.sleep( delay );
        } catch( Exception ex ) {
           
        }
    }

    //<editor-fold defaultstate="collapsed" desc="Old version of code">
    static class MsgHandler implements IGenericMessagesListener{

        @Override
        public void handle( Message msg ) {
            System.out.println( "Incoming message: " + msg );
        }
        
    }
    
    static class AuthenHandler implements IAuthenListener{

        @Override
        public void handle( Message msg ) {
            System.out.println( "Authen message from server: " + msg );
        }
        
    }
    
    static class ChatHandler implements IChatListener{
        
        @Override
        public void handle( Message msg ){
            System.out.println( "Chat message: " + msg );
        }
    }
    
    private static void authen( Socket soc ) {
        try{
            while( true ){
                Scanner scan = new Scanner( System.in );

                System.out.print( "Username: " );
                String username = scan.nextLine();
                System.out.println( );

                System.out.print( "Password: " );
                String password = scan.nextLine();

                Message authenMsg = new Message( username, "server", MessageType.AUTH, password );
                System.out.println( "Sending message: " + authenMsg );

                MessageIO.sendMessage( soc, authenMsg );
            }
        } catch( Exception ex ) {
           
        }
    }
    //</editor-fold>

}
