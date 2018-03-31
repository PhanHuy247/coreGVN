/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.chatserver.messageio;

import com.vn.ntsc.Config;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.LinkedList;
import eazycommon.util.DateFormat;
import eazycommon.util.Util;
import com.vn.ntsc.chatclient.Client;
import com.vn.ntsc.chatserver.authentication.impl.UnAuthenticatedConnection;
import com.vn.ntsc.chatserver.pojos.message.Message;
import com.vn.ntsc.chatserver.pojos.message.messagetype.MessageType;
import com.vn.ntsc.chatserver.pojos.user.UserConnection;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 *
 * @author tuannxv00804
 */
public class MessageIO {
    
    public static final String CharSet = "UTF-8";
    
    public static final String HeartBeatStr = "&-";
    public static final byte[] HeartBeat = HeartBeatStr.getBytes();
    
    public static final String TerminatedSignalStr = "&term";
    public static final byte[] TerminatedSingal = TerminatedSignalStr.getBytes();
    
    public static final int ReadSocLatency = Config.SOCKET_TIMEOUT * 60 *1000; //ms
    private static final int SpoonSize = 10;
    
    
    /**
     * 
     * @param soc
     * @param msg
     * @return message sending status.
     *      0: OK.
     *      1: Error.
     */
    public static int sendMessage( Socket soc, String msg ){
        try{
            OutputStreamWriter out = new OutputStreamWriter( soc.getOutputStream(), CharSet );
            out.write( msg );
            out.flush();
            
            /*
             * After close outputstreamwriter, no one can use socket.
             */
            //out.close();
            
        } catch( IOException ex ) {
//            Util.addErrorLog(ex);
            return 1;
        }
        return 0;
    }
    
    public static int sendMessage( Socket soc, Message msg ){
        String msgStr = MessageParser.serialize( msg );
        return sendMessage( soc, msgStr );
    }
    
    public static int sendMessage( Client client, Message msg ){
        return sendMessage( client, MessageParser.serialize( msg ) );
    }
    
    public static int sendMessage( Client client, String str ){
        try{
            client.writer.write( str );
            client.writer.flush();
            
//            System.out.print( client.username + " has written to server." );
        } catch( Exception ex ) {
//            Util.addErrorLog(ex);
            return 1;
        }
        return 0;
    }
    
    public static int sendMessage( UnAuthenticatedConnection uac, String msg ){
        try{
            uac.writer.write( msg );
            uac.writer.flush();
            return 0;
        } catch( Exception ex ) {
//            Util.addErrorLog(ex);
            return 1;
        }
    }
    
    public static int sendMessage( UnAuthenticatedConnection uac, Message msg ){
        String str = MessageParser.serialize( msg );
        return sendMessage( uac, str );
    }
    
    public static int sendMessage( UserConnection UC, Message msg ){
        int result = 0;
        Exception ex1 = null;
        long beforeAlive = System.currentTimeMillis();
        try{
            UC.writer.write( MessageParser.serialize( msg ) );
            UC.writer.flush();
        } catch( Exception ex ) {
            result = 1;
            ex1 = ex;
        }
        long afterAlive = System.currentTimeMillis();
        if(afterAlive - beforeAlive > 10000){
            Util.addInfoLog("Alive socket :" + UC.soc.getRemoteSocketAddress().toString() +" spend : " + (afterAlive - beforeAlive));
            Util.addErrorLog(ex1);
        }
        return result;
    }
    
    public static int sendMessageWebSocket( UserConnection UC, Message msg ){
        int result = 0;
        Exception ex1 = null;
        long beforeAlive = System.currentTimeMillis();
        try{
           if(msg.msgType == MessageType.NOTIBUZZ){
               Util.addDebugLog("MSG SEND TO CLIENT=========================================== " + msg.jsonSendNotiWebSocket());
            
               UC.webSocKet.getAsyncRemote().sendText(msg.jsonSendNotiWebSocket());
           }else{
               UC.webSocKet.getAsyncRemote().sendText(MessageParser.serialize( msg ));
           }
        } catch( Exception ex ) {
            result = 1;
            ex1 = ex;
        }
        long afterAlive = System.currentTimeMillis();
        if(afterAlive - beforeAlive > 10000){
            Util.addInfoLog("Alive socket :" + UC.soc.getRemoteSocketAddress().toString() +" spend : " + (afterAlive - beforeAlive));
            Util.addErrorLog(ex1);
        }
        return result;
    }
    
    public static void sendTerminatedSignal( Socket soc ){
        try{
            soc.getOutputStream().write( TerminatedSingal );
        } catch( Exception ex ) {
//            Util.addErrorLog(ex);
        }
    }
    
    public static boolean isClientAlive( Socket soc ){
        if(soc == null) return false;
        boolean result;
        Exception ex1 = null;
        long beforeAlive = System.currentTimeMillis();
        try{
            OutputStream out = soc.getOutputStream();
            out.write( HeartBeat );
            out.flush();
            result = true;
        }catch( Exception ex ){
            ex1 = ex;
            result = false;
        }
        long afterAlive = System.currentTimeMillis();
        if(afterAlive - beforeAlive > 10000){
            Util.addInfoLog("Alive socket :" + soc.getRemoteSocketAddress().toString() +" spend : " + (afterAlive - beforeAlive));
            Util.addErrorLog(ex1);
        }
        return result;
    }
    
    public static boolean isClientAlive( UserConnection uc ){
        if(uc == null  || uc.webSocKet == null) {
            return false;
        }
        boolean result = false;
        Exception ex1 = null;
        long beforeAlive = System.currentTimeMillis();
        String address = null;
        try{
//            if (uc.soc != null){
//                uc.writer.write( HeartBeatStr );
//                uc.writer.flush();
//                result = true;
//                address = uc.soc.getRemoteSocketAddress().toString();
//            }
            if(uc.webSocKet.isOpen())
                result = true;
            
          
        }catch( Exception ex ){
            Util.addDebugLog("CHECK IS_ALIVE EXCEPTION " + ex.getMessage());
            ex1 = ex;
            result = false;
        }
        long afterAlive = System.currentTimeMillis();
        if(afterAlive - beforeAlive > 10000){
            Util.addInfoLog("Alive socket :" + address +" spend : " + (afterAlive - beforeAlive));
            Util.addErrorLog(ex1);
        }
        return result;
    }
    
    
    /**
     * This method is NOT buffer safety!
     * This may cause data loosing.
     * @param soc
     * @return 
     */
    public static LinkedList<Message> readMessage( Socket soc ){
        StringBuilder builder = new StringBuilder();
        try{
            soc.setSoTimeout( ReadSocLatency );
            char[] spoon = new char[SpoonSize];
            InputStreamReader reader = new InputStreamReader( soc.getInputStream(), CharSet );
            int n = 0;
            while( true ){
                n = reader.read( spoon );
                builder.append( spoon, 0, n );
            }
        }catch( Exception ex ) {
//            Util.addErrorLog(ex);            
        }
        return MessageParser.parse( builder.toString() );
    }
    
    public static LinkedList<Message> readMessage( UserConnection uc ){
        StringBuilder builder = new StringBuilder();
        try{
            char[] spoon = new char[SpoonSize];
            int n = 0;
            while( true ){
                n = uc.reader.read( spoon );
                builder.append( spoon, 0, n );
            }
        }catch( Exception ex ) {
//            Util.addErrorLog(ex);            
        }
        return MessageParser.parse( builder.toString() );
    }
    
    public static LinkedList<Message> readMessage( Client client ){
        try{
            char[] spoon = new char[ SpoonSize ];
            int n = 0;
            while( true ){
                n = client.reader.read( spoon );
                client.buffer.append( spoon, 0, n );
            }
        } catch( Exception ex ) {
//            Util.addErrorLog(ex);            
        }
        return MessageParser.parse( client.buffer );
    }
    
    public static LinkedList<Message> readMessageFromUAC( UnAuthenticatedConnection UAC ){
        StringBuilder builder = new StringBuilder();
        try{
            char[] spoon = new char[SpoonSize];
            int n = 0;
            while( true ){
                n = UAC.reader.read( spoon );
                builder.append( spoon, 0, n );
            }
        }catch( Exception ex ) {
//            Util.addErrorLog(ex);            
        }
        return MessageParser.parse( builder.toString() );
    }
    
    public static void readMessageToOutbox( UserConnection UC ){
        try{
            char[] spoon = new char[SpoonSize];
            int n = 0;
            while( ( n = UC.reader.read( spoon ) ) > -1 ) {
                UC.inboxBuffer.append( spoon, 0, n );
            }
        }catch( Exception ex ) {
//            Util.addErrorLog(ex);            
        }
        LinkedList<Message> ll = MessageParser.parse( UC.inboxBuffer );
        String ip = null;
        try{
            String [] eles = UC.soc.getRemoteSocketAddress().toString().substring(1).split(":");
            ip = eles[0];
        }catch(Exception ex){
        }
        for (Message msg : ll) {
            msg.ip = ip;
            UC.user.outbox.add( msg );
        }
    }
    
}
