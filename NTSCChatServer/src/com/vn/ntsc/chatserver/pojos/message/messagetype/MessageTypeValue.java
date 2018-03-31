/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.chatserver.pojos.message.messagetype;

import com.vn.ntsc.chatserver.pojos.message.Message;
import eazycommon.util.Util;

/**
 *
 * @author tuannxv00804
 */
public class MessageTypeValue {
    
    public static final String Auth_AutheSuccess = "s"; //success
    public static final String Auth_AuthenFalse = "f"; //false
    public static final String Auth_AuthenNot = "n"; //not
    public static final String Auth_AlreadyInConnection = "aic"; //already in connection
    
    public static final String Presence_Online = "ol";
    public static final String Presence_Offline = "of";
    public static final String Presence_Visible = "v";
    public static final String Presence_Invisible = "iv";
    public static final String Presence_Away = "aw";
    public static final String Presence_Writing = "wt";
    public static final String Presence_StopWriting = "sw";
    public static final String Presence_Erasing = "es";
    
    public static final String BCast_All = "all";
    
    public static final String MsgStatus_Read = "rd"; //the message was read
    public static final String MsgStatus_Read_All = "rd_all"; //the message was read
    public static final String MsgStatus_Sent = "st";
    public static final String MsgStatus_Sent_Other = "ost";
    public static final String MsgStatus_Delivered = "dlv";
    public static final String MsgStatus_Unsent = "us"; // unsend    
    
    public static final String CMD_name_getFriendStatus = "fs";
    public static final String CMD_name_getFriendsListStatus = "fls";
    public static final String CMD_name_getAccquaintanceList = "al";
    public static final String CMD_name_PPFileTransfer = "ppf";
    public static final String CMD_PPFileStransfer_signal_invite = "i";
    public static final String CMD_PPFileStransfer_signal_accept = "a";
    public static final String CMD_PPFileStransfer_signal_decline = "d";
    public static final String CMD_PPFileStransfer_signal_servercreated = "sct";
    
    public static final String Separater = "&";
    
    public static String CMD_getValue_FriendStatus( String friendName ){
        return CMD_name_getFriendStatus + Separater + friendName;
    }
    
    public static String CMD_getCommandName( String value ){
        String result = CMD_name_getFriendStatus;
        try{
            String[] eles = value.split( Separater );
            result = eles[ 0 ];
        } catch( Exception ex ) {
            Util.addErrorLog(ex);            
        }
        return result;
    }
    
    public static String CMD_getFriendName( String value ){
        String result = "";
        try{
            String[] eles = value.split( Separater );
            result = eles[1];
        } catch( Exception ex ) {
            Util.addErrorLog(ex);            
        }
        return result;
    }
    
    
    public static String CMD_PPFileTransfer_getValue( String friendName, String hostname, int port, String signal ){
        return CMD_name_PPFileTransfer + 
                Separater + hostname + 
                Separater + port + 
                Separater + signal;
    }
    
    public static String CMD_PPFilesTransfer_getHostname( String value ){
        String result = "";
        try{
            String[] ele = value.split( Separater );
            result = ele[1];
        } catch( Exception ex ) {
            Util.addErrorLog(ex);
        }
        return result;
    }
    
    public static int CMD_PPFilesTransfer_getPort( String value ){
        int result = 0;
        try{
            String[] ele = value.split( Separater );
            result = Integer.parseInt( ele[2] );
        } catch( Exception ex ) {
            Util.addErrorLog(ex);
        }
        return result;
    }
    
    public static String CMD_PPFilesTransfer_getSignal( String value ){
        String result = "";
        try{
            String[] eles = value.split( Separater );
            result = eles[3];
        } catch( Exception ex ) {
            Util.addErrorLog(ex);
        }
        return result;
    }
    
    public static String Block = "block";
    public static String CMD_Block_value( String userID ){
        return Block + Separater + userID;
    }
    
    public static String CMD_Block_getCMDName( String value ){
        String s = null;
        try{
            s = value.split( Separater )[0];
        } catch( Exception ex ) {
            Util.addErrorLog(ex);
        }
        return s;
    }
    
    public static String CMD_Block_getUserID( String value ){
        String s = null;
        try{
            s = value.split( Separater )[1];
        } catch( Exception ex ) {
            Util.addErrorLog(ex);
        }
        return s;
    }
    
    public static String MsgStatus_getValue( Message msg, String msgDeliveringState ) {
        return msg.id + "&" + msgDeliveringState;
    }
    
    public static String MsgStatus_getValue( String msgID, String msgDeliveringState ){
        return msgID + "&" + msgDeliveringState;
    }
    
    public static String MsgStatus_getOriginID( Message msg ){
        if( msg.msgType == MessageType.MDS ){
            String[] eles = msg.value.split( "&" );
            try{
                String origin = eles[0] + "&" + eles[1] + "&" + eles[2];
                return origin;
            } catch( Exception ex ) {
                Util.addErrorLog(ex);
                return null; 
            }
        }
        return null;
    }
    
    public static String MsgStatus_getStatus( Message msg ){
        if( msg.msgType == MessageType.MDS ){
            String eles = msg.value.toString();
            try{
                return eles;
            } catch( Exception ex ) {
                Util.addErrorLog(ex);                
                return null;
            }
        }
        return null;
    }
    
    public static String MsgStatus_getReadMsg( Message msg ){
        if( msg.msgType == MessageType.MDS ){
            String eles = msg.id;
            try{
                return eles;
            } catch( Exception ex ) {
                Util.addErrorLog(ex);                
                return null;
            }
        }
        return null;
    }    
    
    public static String MsgCall_getStartMsg( Message msg ){
        if( msg.msgType == MessageType.EVIDEO || msg.msgType == MessageType.EVOICE ){
            String[] eles = msg.value.split( "\\|" );
            try{
                return eles[1];
            } catch( Exception ex ) {
                Util.addErrorLog(ex);                
                return null;
            }
        }
        return null;
    }    
}
