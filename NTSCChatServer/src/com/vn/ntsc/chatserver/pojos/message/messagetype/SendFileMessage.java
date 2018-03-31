/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.chatserver.pojos.message.messagetype;

import eazycommon.util.Util;


/**
 *
 * @author tuannxv00804
 */
public class SendFileMessage {

//    public static final String ValuePattern = "msgID|fileType|fileID|fileName";
    
    public static final String Separator = "|";
    public static final String BackSlash = "\\";
    
    public static String createValue( String msgID, String fileID, String fileName ) {
        return ( fileID == null 
                || fileID.isEmpty() 
                || fileName == null
                || fileName.isEmpty() ) ? "" : ( msgID + Separator + fileID + Separator + fileName );
    }
    
    public static String getMsgID( String value ){
        try{
            if( value != null 
                    && !value.isEmpty() ){
                String msgID = value.split( BackSlash + Separator )[0];
                return msgID;
            }
            
        } catch( Exception ex ) {
            Util.addErrorLog(ex);
        }
        return "";
    }
    
    public static String getFileID( String value ){
        try{
            if( value != null 
                    && !value.isEmpty()
                    && !value.contains( " " ) ){
                return value.split( BackSlash + Separator )[2];
            }
        } catch( Exception ex ) {
            Util.addErrorLog(ex);
        }
        return "";
    }

    public static String getFileName( String value ){
        try{
            if( value != null 
                    && !value.isEmpty()
                    && !value.contains( " " ) ){
                return value.split( BackSlash + Separator )[3];
            }
        } catch( Exception ex ) {
            Util.addErrorLog(ex);
        }
        return "";
    }
    
    public static String getFileType( String value ){
        try{
            if( value != null 
                    && !value.isEmpty()
                    && !value.contains( " " ) ){
                return value.split( BackSlash + Separator )[1];
            }
        } catch( Exception ex ) {
            Util.addErrorLog(ex);
        }
        return "";
    }    
}
