/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.chatserver.pojos.message.messagetype;

/**
 *
 * @author tuannxv00804
 */
public enum MessageType {
    PP, 
    PE, 
    ROOM,
    CMD, //command
    AUTH, //authentication
    PRC, //presence
    MDS, //message delivering state
    BCAST, //broad cast message
    RT, //The LastTime a user read other's message.
    WINK,
    FILE,
    AUDIO,
    VIDEO,
    IMAGE,
    GIFT,
    STK, //Sticker
    LCT,
    SVOICE, //start voice call
    EVOICE, //end voice call
    SVIDEO, //start video call
    EVIDEO,  //end video call
    CALLREQ,
    BUZZCMT,
    BUZZJOIN,
    BUZZLEAVE,
    BUZZSUBCMT,
    BUZZTAG,
    BUZZADDTAG,
    BUZZDELCMT,
    BUZZDELSUBCMT,
    PING,
    PONG,
    REMOVESOCKET,
    NOTIBUZZ,
    BUZZUPDATE,
    //Location
}
