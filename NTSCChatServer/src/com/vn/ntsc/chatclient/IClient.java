/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.chatclient;

import java.util.Date;
import java.util.LinkedList;
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
import com.vn.ntsc.chatserver.pojos.message.Message;

/**
 *
 * @author tuannxv00804
 */
public interface IClient {

    public void sendAuthenMessage( String username, String password );

    //Tested method.
    public boolean sendMessage( Message message );

    public void markMessageAsRead( String messageid );

    //Tested method.
    public LinkedList<Message> getLogMsgInbox( Date fromDate, String fromUser );

    //Tested method.
    public LinkedList<Message> getLogMsgOutbox( Date fromDate, String toUser );

    //Tested method.
    public LinkedList<Message> getOfflineMessage();

    //Tested method.
    public void requestFriendStatus( String friendUsername );

    //Tested method.
    public void requestFriendListStatus();

    //Tested method.
    public void requestAccquaintanceListStatus();

    public void setStatus( String status );

    public void sendWritingSignal( String friendUsername );

    public void sendStopWritingSignal( String friendUsername );

    public void sendErasingSignal( String friendUsername );

    public void sendSticker( String friendID, String content );
    
    public void sendLocation( String friendID, String content );
    
    //Tested method.
    public void wink( String friendUsername );

    public void wink( long percentOfTotalUser );

    public void addGenericMessageListener( IGenericMessagesListener msgListener );

    public void addAuthenListener( IAuthenListener authenListener );

    public void addChatListener( IChatListener chatListener );

    public void addPresenceListener( IPresenceListener presenceListener );

    public void addMessgeStatusListener( IMessageStatusListener messageStatusListener );

    public void addWinkMessageListener( IWinkMessageListener winkMessageListener );
    
    public void addFileListener( IFileListener fileMessageListener );
    
    public void addStickerListener( IStickerListener stickerListener );
    
    public void addLocationListener( ILocationListener locationListener );
    
    public void addGiftListener( IGiftMessageListener giftListener );
    
    public void addCMDListener( ICMDListener cmdListener );
    
    public void addCallListener( ICallListener callListener );
    
    public void dispose();
}
