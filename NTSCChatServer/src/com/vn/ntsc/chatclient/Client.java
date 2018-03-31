/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.chatclient;

import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Date;
import java.util.LinkedList;
import eazycommon.util.DateFormat;
import com.vn.ntsc.Config;
import com.vn.ntsc.chatclient.listeners.IAuthenListener;
import com.vn.ntsc.chatclient.listeners.ICMDListener;
import com.vn.ntsc.chatclient.listeners.ICallListener;
import com.vn.ntsc.chatclient.listeners.IChatListener;
import com.vn.ntsc.chatclient.listeners.IFileListener;
import com.vn.ntsc.chatclient.listeners.IGenericMessagesListener;
import com.vn.ntsc.chatclient.listeners.IGiftMessageListener;
import com.vn.ntsc.chatclient.listeners.ILocationListener;
import com.vn.ntsc.chatclient.listeners.IMessageStatusListener;
import com.vn.ntsc.chatclient.listeners.IPresenceListener;
import com.vn.ntsc.chatclient.listeners.IStickerListener;
import com.vn.ntsc.chatclient.listeners.IWinkMessageListener;
import com.vn.ntsc.chatclient.listeners.impl.MessageConfirmer;
import com.vn.ntsc.chatserver.messageio.MessageIO;
import com.vn.ntsc.chatserver.pojos.message.Message;
import com.vn.ntsc.chatserver.pojos.message.messagetype.MessageType;
import com.vn.ntsc.chatserver.pojos.message.messagetype.MessageTypeValue;

/**
 *
 * @author tuannxv00804
 */
public class Client implements IClient {

    String server;
    int port;
    public String username;
    public String password;
    public String status;
    public Socket soc;
    public OutputStreamWriter writer;
    public InputStreamReader reader;
    private static final int IdleThreadLatency = 100; //ms
    private static final String CharSet = "UTF-8";
    public StringBuilder buffer = new StringBuilder();
    SocketListener socListener;
    LinkedList<IGenericMessagesListener> Listeners = new LinkedList<IGenericMessagesListener>();
    MessageConfirmer confirmer;

    public Client( String serveraddress, int port ) {
        this.server = serveraddress;
        this.port = port;
        try {
            soc = new Socket( server, port );
            soc.setSoTimeout( MessageIO.ReadSocLatency );
            reader = new InputStreamReader( soc.getInputStream(), CharSet );
            writer = new OutputStreamWriter( soc.getOutputStream(), CharSet );

        } catch( Exception ex ) {
           
        }
        confirmer = new MessageConfirmer( this );
        Listeners.add( confirmer );

        socListener = new SocketListener( this );
        socListener.start();
    }

    /**
     * 
     * @param username
     * @param password
     * @return authen code:
     *      0: OK
     *      1: wrong username, password
     *      2: Unknown server address
     *      3: Connection error.
     */
    @Override
    public void sendAuthenMessage( String username, String password ) {
        this.username = username;
        this.password = password;

        Message authenMsg = new Message( this.username, "server", MessageType.AUTH, this.password );
        MessageIO.sendMessage( this, authenMsg );

//        System.out.println( "Authen message sent: " + authenMsg );
//        authenMsg.printStruct();
    }

    @Override
    public boolean sendMessage( Message message ) {
        int result = MessageIO.sendMessage( this, message );
        if( result == 0 ) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void markMessageAsRead( String messageid ) {
        String value = MessageTypeValue.MsgStatus_getValue( messageid, MessageTypeValue.MsgStatus_Read );
        Message msg = new Message( this.username, "", MessageType.MDS, value );
        MessageIO.sendMessage( this, msg );
    }

    @Override
    public void addGenericMessageListener( IGenericMessagesListener msgListener ) {
        Listeners.add( msgListener );
    }

    @Override
    public void addAuthenListener( IAuthenListener authenLitener ) {
        Listeners.add( authenLitener );
    }

    @Override
    public void addChatListener( IChatListener chatListener ) {
        Listeners.add( chatListener );
    }

    @Override
    public void addPresenceListener( IPresenceListener presenceListener ) {
        Listeners.add( presenceListener );
    }

    @Override
    public void addMessgeStatusListener( IMessageStatusListener messageStatusListener ) {
        Listeners.add( messageStatusListener );
    }

    @Override
    public void addWinkMessageListener( IWinkMessageListener winkMessageListener ) {
        Listeners.add( winkMessageListener );
    }

    @Override
    public void addFileListener( IFileListener fileMessageListener ){
        Listeners.add( fileMessageListener );
    }
    
    @Override
    public void addStickerListener( IStickerListener stickerListener ){
        Listeners.add( stickerListener );
    }
    
    @Override
    public void addGiftListener( IGiftMessageListener giftListener ){
        Listeners.add( giftListener );
    }
    
    @Override
    public void addLocationListener( ILocationListener locationListener ){
        Listeners.add( locationListener );
    }
    
    @Override
    public void addCMDListener( ICMDListener cmdListener ){
        Listeners.add( cmdListener );
    }
    
    @Override
    public void addCallListener( ICallListener callListener ){
        Listeners.add( callListener );
    }
    
    @Override
    public LinkedList<Message> getLogMsgInbox( Date fromDate, String fromUser ) {
//        ILogClient logClient = new LogClient( Config.ChatLogServer_IP, Config.ChatLogServer_Port );
//        return logClient.getLogMsg_SentFrom( this.username, fromUser, fromDate );
        return null;
    }

    @Override
    public LinkedList<Message> getLogMsgOutbox( Date fromDate, String toUser ) {
//        ILogClient logClient = new LogClient( Config.ChatLogServer_IP, Config.ChatLogServer_Port );
//        return logClient.getLogMsg_SentTo( this.username, toUser, fromDate );
        return null;
    }

    @Override
    public LinkedList<Message> getOfflineMessage() {
//        ILogClient logclient = new LogClient( Config.ChatLogServer_IP, Config.ChatLogServer_Port );
//        return logclient.getOfflineMsg( this.username, MessageTypeValue.MsgStatus_Sent );
        return null;
    }

    public void sendLastReadTime( String friendID, Date d ) {
        Message msg = new Message( username, friendID, MessageType.RT, DateFormat.format( d ) );
        MessageIO.sendMessage( this, msg );
        msg.printStruct();
    }

    @Override
    public void requestFriendStatus( String friendUsername ) {
        String msgValue = MessageTypeValue.CMD_getValue_FriendStatus( friendUsername );
        Message request = new Message( this.username, Config.ServerName, MessageType.CMD, msgValue );
        MessageIO.sendMessage( this, request );
    }

    @Override
    public void requestFriendListStatus() {
        String value = MessageTypeValue.CMD_name_getFriendsListStatus;
        Message requestMsg = new Message( this.username, Config.ServerName, MessageType.CMD, value );
        MessageIO.sendMessage( this, requestMsg );
    }

    @Override
    public void requestAccquaintanceListStatus() {
        String value = MessageTypeValue.CMD_name_getAccquaintanceList;
        Message request = new Message( this.username, Config.ServerName, MessageType.CMD, value );
        MessageIO.sendMessage( this, request );
    }

    @Override
    public void setStatus( String status ) {
        this.status = status;
        Message sttMsg = new Message( this.username, Config.ServerName, MessageType.PRC, status );
        MessageIO.sendMessage( this, sttMsg );
    }
    Message WritingMsg = null;

    @Override
    public void sendWritingSignal( String friendUsername ) {
        Message WritingMsg = new Message( this.username, friendUsername, MessageType.CMD, MessageTypeValue.Presence_Writing );
        MessageIO.sendMessage( this, WritingMsg );
    }
    Message StopWritingMsg = null;

    @Override
    public void sendStopWritingSignal( String friendUsername ) {
        Message StopWritingMsg = new Message( this.username, friendUsername, MessageType.CMD, MessageTypeValue.Presence_StopWriting );
        MessageIO.sendMessage( this, StopWritingMsg );
    }

    @Override
    public void sendSticker( String friendID, String content ){
        Message msg = new Message( this.username, friendID, MessageType.STK, content );
        MessageIO.sendMessage( this, msg );
    }
    
    @Override
    public void sendLocation( String friendID, String content ){
        Message msg = new Message( this.username, friendID, MessageType.LCT, content );
        MessageIO.sendMessage( this, msg );
    }
    
    @Override
    public void sendErasingSignal( String friendUsername ) {
        Message ErasingMsg = new Message( this.username, friendUsername, MessageType.CMD, MessageTypeValue.Presence_Erasing );
        MessageIO.sendMessage( this, ErasingMsg );
    }

    @Override
    public void wink( String friendUsername ) {
        Message wink = new Message( this.username, friendUsername, MessageType.WINK, "1" );
        MessageIO.sendMessage( this, wink );
    }

    /**
     * 
     * @param percentOfTotalUser : is written in long format. Ex: 10.1, 15.9
     */
    @Override
    public void wink( long percentOfTotalUser ) {
        Message wink = new Message( this.username, Config.ServerName, MessageType.WINK, String.valueOf( percentOfTotalUser ) );
        MessageIO.sendMessage( this, wink );
    }

    private boolean dispose = false;
    @Override
    public void dispose(){
        try{
            dispose = true;
            this.soc.close();
            this.Listeners.clear();
//            System.out.println( "Listeners size = " + this.Listeners.size() );
        } catch( Exception ex ) {
           
        }
    }
    
    class SocketListener extends Thread {

        Client client;

        public SocketListener( Client client ) {
            this.client = client;
        }

        @Override
        public void run() {
            while( true ) {
//                System.out.println("Chat client dang chay......");
                LinkedList<Message> ll = MessageIO.readMessage( client );

                if( ll.isEmpty() ) {
                    try {
                        Thread.sleep( IdleThreadLatency );
                    } catch( Exception ex ) {
                       
                    }
                }
                for( int i = 0; i < ll.size(); i++ ) {
                    Message msg = ll.get( i );

                    for( int ii = 0; ii < Listeners.size(); ii++ ) {
                        IGenericMessagesListener ltn = Listeners.get( ii );

                        if( ltn instanceof IChatListener && msg.msgType == MessageType.PP ) {
                            deliveMsg( ltn, msg );
                        }

                        if( ltn instanceof IMessageStatusListener && msg.msgType == MessageType.MDS ) {
                            deliveMsg( ltn, msg );
                        }

                        if( ltn instanceof IPresenceListener && msg.msgType == MessageType.PRC ) {
                            deliveMsg( ltn, msg );
                        }

                        if( ltn instanceof IAuthenListener && msg.msgType == MessageType.AUTH ) {
                            deliveMsg( ltn, msg );
                        }

                        if( ltn instanceof IWinkMessageListener && msg.msgType == MessageType.WINK ) {
                            deliveMsg( ltn, msg );
                        }

                        if( ltn instanceof IFileListener && msg.msgType == MessageType.FILE ) {
                            deliveMsg( ltn, msg );
                        }
                        
                        if( ltn instanceof ILocationListener && msg.msgType == MessageType.LCT ){
                            deliveMsg( ltn, msg );
                        }
                        
                        if( ltn instanceof IGiftMessageListener && msg.msgType == MessageType.GIFT ){
                            deliveMsg( ltn, msg );
                        }
                        
                        if( ltn instanceof IStickerListener && msg.msgType == MessageType.STK ){
                            deliveMsg( ltn, msg );
                        }
                        
                        if( ltn instanceof ICMDListener && msg.msgType == MessageType.CMD ){
                            deliveMsg( ltn, msg );
                        }
                        
                        if( ltn instanceof ICallListener &&
                                ( msg.msgType == MessageType.SVIDEO 
                                || msg.msgType == MessageType.SVOICE
                                || msg.msgType == MessageType.EVIDEO 
                                || msg.msgType == MessageType.EVOICE ) ){
                            deliveMsg( ltn, msg );
                        }
                        
//                        if( !( ltn instanceof IChatListener
//                                || ltn instanceof IMessageStatusListener
//                                || ltn instanceof IPresenceListener
//                                || ltn instanceof IAuthenListener
//                                || ltn instanceof IWinkMessageListener
//                                || ltn instanceof IFileListener 
//                                || ltn instanceof IGiftMessageListener
//                                || ltn instanceof ILocationListener
//                                || ltn instanceof IStickerListener
//                                || ltn instanceof ICMDListener) ) {
//                            deliveMsg( ltn, msg );
//                        }

                    }
                }
                if(dispose) return;
            }
        }

        public void deliveMsg( IGenericMessagesListener ltn, Message msg ) {
            Deliver d = new Deliver( msg, ltn );
            d.start();
        }

        class Deliver extends Thread {

            Message msg;
            IGenericMessagesListener listener;

            public Deliver( Message msg, IGenericMessagesListener listener ) {
                this.msg = msg;
                this.listener = listener;
            }

            @Override
            public void run() {
                this.listener.handle( msg );
            }
        }
    }
}
