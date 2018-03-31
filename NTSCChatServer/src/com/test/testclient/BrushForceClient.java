/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.test.testclient;

import com.test.testclient.handlers.AuthenMessageHandler;
import com.test.testclient.handlers.ChatMessageHandler;
import com.test.testclient.handlers.PresenceMessageHandler;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Scanner;
import com.vn.ntsc.Config;
import com.vn.ntsc.chatclient.Client;
import com.vn.ntsc.chatclient.listeners.IFileListener;
import com.vn.ntsc.chatclient.listeners.IGenericMessagesListener;
import com.vn.ntsc.chatclient.listeners.IMessageStatusListener;
import com.vn.ntsc.chatclient.listeners.IWinkMessageListener;
import com.vn.ntsc.chatserver.messageio.MessageIO;
import com.vn.ntsc.chatserver.pojos.message.Message;
import com.vn.ntsc.chatserver.pojos.message.messagetype.MessageType;
import com.vn.ntsc.chatserver.pojos.message.messagetype.MessageTypeValue;

/**
 *
 * @author tuannxv00804
 */
public class BrushForceClient {

    public static final String userid = "li@gmail.com";
    public static final String toUserid = "he@gmail.com";
    public static final int delay = 50;
    
    public static void main( String[] args ) {
        Client client = initClient();

        int max = 1000;
        for( int i = 0; i < max; i++ ) {
            Message msg = new Message( userid, toUserid, MessageType.PP, "" + i );
            MsgPool.put( msg.id, msg );
            MessageIO.sendMessage( client, msg );
            System.out.println( " + " + i );
            sleep();
        }
        
        Scanner scan = new Scanner( System.in );
        String s = scan.nextLine();
        if( s.equals( "count" ) ){
            System.out.println( "TotalMsg = " + MsgPool.size() );
            System.out.println( "Total Sent = " + llSent.size() );
            System.out.println( "Total Dlv = " + llDlv.size() );
            
            for( int i = 0; i < max; i++ ){
                if( !llSent.contains( String.valueOf( i ) ) ){
                    System.out.println( "Sent Lost: " + i );
                }
            }
            for( int i = 0; i < max; i++ ){
                if( !llDlv.contains( String.valueOf( i ) ) ){
                    System.out.println( "Dlv lost: " + i );
                }
            }
        }
    }
    
    private static void sleep() {
        try {
            Thread.sleep( delay );
        } catch( Exception ex ) {
           
        }
    }

    private static Client initClient() {

        Client client = new Client( TestClient.server, Config.ChatServerPort );

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
        } );

        client.addFileListener( new IFileListener() {

            @Override
            public void handle( Message msg ) {
                System.out.println( "File sending from: " + msg.from + ", to: " + msg.to + ", value: " + msg.value );
            }
        } );

        client.addGenericMessageListener( new IGenericMessagesListener() {

            @Override
            public void handle( Message msg ) {
                if( msg.msgType == MessageType.GIFT ) {
                    System.out.println( "Gift from: " + msg.from );
                    System.out.println( "GiftID = " + msg.value );
                    msg.printStruct();
                } else {
//                    System.out.println( "Generic is NOT GIFT. From = " + msg.from + ", type = " + msg.msgType + ", value = " + msg.value );
                }
            }
        } );

        String password = "38137b36-c485-4243-a67b-92f9e4e3712a";
        client.sendAuthenMessage( userid, password );

        return client;
    }

    private static HashMap<String, Message> MsgPool = new HashMap<String, Message>();
    private static LinkedList<String> llSent = new LinkedList<String>();
    private static LinkedList<String> llDlv = new LinkedList<String>();
    
    static class MessageStatusHandler implements IMessageStatusListener {

        @Override
        public void handle( Message msg ) {

            String confirmValue = readConfirmString( msg );
            String originID = readID( msg );
            if( confirmValue.equals( MessageTypeValue.MsgStatus_Sent ) ) {
                //System.out.println( "Message: " + MsgPool.get( originID ).value + " is sent to server." );
                llSent.add( MsgPool.get( originID ).value );
            } else if( confirmValue.equals( MessageTypeValue.MsgStatus_Delivered ) ) {
                //System.out.println( "Message: " + MsgPool.get( originID ).value + " is delivered." );
                llDlv.add( MsgPool.get( originID ).value );
            } else {
                System.out.println( "Unknown MsgDlvStatus - MessageStatusHandler: msg = " + msg );
            }
        }

        private String readConfirmString( Message msg ) {
            String str = msg.value;
            String[] eles = str.split( "&" );
            try {
                return eles[3];
            } catch( Exception ex ) {
                return null;
            }
        }

        private String readID( Message msg ) {
            String str = msg.value;
            String[] eles = str.split( "&" );
            try {
                String result = eles[0]
                        + "&" + eles[1]
                        + "&" + eles[2];
                return result;
            } catch( Exception ex ) {
                return null;
            }
        }
    }
}
