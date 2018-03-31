/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.chatserver.pojos.user;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import eazycommon.constant.mongokey.UserdbKey;
import eazycommon.dao.CommonDAO;
import eazycommon.util.Util;
import org.bson.types.ObjectId;
import com.vn.ntsc.Core;
import com.vn.ntsc.chatserver.logging.impl.mongo.LastChatUpdater;
import com.vn.ntsc.chatserver.pojos.message.Message;
import com.vn.ntsc.dao.impl.LastChatDAO;

/**
 *
 * @author tuannxv00804
 */
public class User {
    public String username;
    public ConcurrentLinkedQueue<Message> inbox;
    public LinkedList<Message> outbox;
    
//    public HashMap<String, Message> lastChats;
    public long lastChatTime;
    public HashMap<String, Long> readTimes;
    
    public List<String> friendList = new ArrayList<>();
    public List<String> favouristList = new ArrayList<>();
    
    public int sendChatNotificationSetting = 0;
    
    public String token = " ";
    
    //applicationType = 4 , device session authen by Web
    public Integer applicationType ;
    
    public List<String> buzzList = new ArrayList<>();
    
    public String deviceId;
    
    public User( String username ,String token){
        this.username = username;
        this.token = token;
        initResource();
    }
    
    public User( String username ,String token,Integer applicationType){
        this.username = username;
        this.token = token;
        this.applicationType = applicationType;
        initResource();
    }
    
    public User( String username ,String token,Integer applicationType, String deviceId){
        this.username = username;
        this.token = token;
        this.applicationType = applicationType;
        this.deviceId = deviceId;
        initResource();
    }

    public User(String username) {
        this.username = username;
        initResource();
    }

    private User() {
    }
    
    public static User getInstance(String userName){
        User user = new User();
        user.username = userName;
        user.inbox = new ConcurrentLinkedQueue<>();
        user.outbox = new LinkedList<>();
//        user.lastChats = new HashMap<>();
        user.readTimes = new HashMap<>();
        return user;
    }
    
    private void initResource(){
        this.inbox = new ConcurrentLinkedQueue<>();
        this.outbox = new LinkedList<>();
        UserConnection uc = Core.getStoreEngine().get(username);
//        this.lastChats = new HashMap<>();
        if(uc != null && uc.user != null){
//            this.lastChats.putAll(uc.user.lastChats);
            this.favouristList.addAll(uc.user.favouristList);
            this.sendChatNotificationSetting = uc.user.sendChatNotificationSetting;
        }else{
//            this.lastChats = LastChatDAO.getLastChatList(username);
            favouristList = FFUMS.getFavouristList( username );
            this.sendChatNotificationSetting = FFUMS.getChatNotificationSetting(username);
        }
//        if(uc == null || uc.user == null){
//            HashMap<String, Message> hm = Filer.getLastChats( username );
//            this.lastChats = hm != null ? hm : new HashMap<String, Message>();
//        }else{
//            this.lastChats = new HashMap<String, Message>();
//            this.lastChats.putAll(uc.user.lastChats);
//        }
//        LastChat.put(username, lastChats);
        
        
//        HashMap<String, Long> rt = Filer.getReadTimes( username );
//        this.readTimes = rt != null ? rt : new HashMap<String, Long>();
        this.readTimes = new HashMap<>();
        
//        ArrayList<String> fl = Filer.getFriendList( username );
//        fl = fl == null ? new ArrayList<String>() : fl;
//        ArrayList<String> fv = Filer.getFavouristList( username );
//        fv = fv == null ? new ArrayList<String>() : fv;
        
//        friendList = FFUMS.getFriendList( username );
        lastChatTime = 0;
    }
    
    public void putLastChat(String userId, Message message){
//        Message lastMessage = this.lastChats.get(userId);
//        if(lastMessage != null && lastMessage.serverTime > message.serverTime)
//            return;
//        this.lastChats.put(userId, message);
        LastMessageManager.update(username, userId, message);
        this.lastChatTime = Util.currentTime();
        LastChatUpdater.add(username, userId, message);
//        LastChatDAO.putLastChatList(username, userId, message);
    }    
    
    public HashMap<String, Message> getLastChat(){
        return LastChatDAO.getLastChatList(username);
    }
    
    public HashMap<String, Message> getLastChat2(){
        return LastChatDAO.getLastChatList2();
    }
    
//    public void removeLastChat(String friendId){
//        LastChatDAO.removeLastChatList(username, friendId);
//    }
    
    public void removeUnreadMessage(String friendId){
        UnreadMessageManager.remove(username, friendId);
    }
    
    public void removeUnreadMessageExceptList(List<String> friendIdList){
        UnreadMessageManager.keep(username, friendIdList);
    }
    
    public void increaseUnreadMessage(String friendId){
        UnreadMessageManager.increase(username, friendId);
    }
    public void updateUnreadMessage(String friendId, int amount){
        UnreadMessageManager.update(username, friendId, amount);
    }
    
    @Override
    public String toString() {
        return "User{" + "username=" + username + ", inbox=" + inbox + ", outbox=" + outbox + /*", received=" + received + ", sent=" + sent + */ '}';
    }

    class SentMessageComparator implements Comparator<Message>{

        @Override
        public int compare( Message o1, Message o2 ) {
            if( o1.to.equals( o2.to ) ){
                return o2.originTime.compareTo( o1.originTime );
            }
            return o2.to.compareTo( o1.to );
        }
        
    }
    
    class ReceivedMessageComparator implements Comparator<Message>{

        @Override
        public int compare( Message o1, Message o2 ) {
            if( o1.from.equals( o2.from ) ){
                return o2.originTime.compareTo( o1.originTime );
            }
            return o2.from.compareTo( o1.from );
        }
        
    }
    
    /**
     * This class is tend to create FriendList and FavouristList that is taken from UMS's MongoDB
     */
    public static class FFUMS {
        
        private static DB db;
        private static DBCollection FavouristCollection;
        private static DBCollection BlockCollection;
        private static DBCollection NotificationSettingCollection;
        static{
            try{
                db = CommonDAO.mongo.getDB( UserdbKey.DB_NAME );
                FavouristCollection = db.getCollection( UserdbKey.FAVORIST_COLLECTION ); 
                BlockCollection = db.getCollection(UserdbKey.BLOCK_COLLECTION);
                NotificationSettingCollection = db.getCollection(UserdbKey.NOTIFICATION_SETTING_COLLECTION);
            } catch( Exception ex ) {
                Util.addErrorLog(ex);
            }
        }
        
        public static int getChatNotificationSetting( String userid){
            int result = 0;
            ObjectId id = new ObjectId(userid);
            BasicDBObject findObj = new BasicDBObject(UserdbKey.NOTIFICATION_SETTING.ID, id);
            DBObject obj = NotificationSettingCollection.findOne(findObj);
            if (obj != null) {
                Integer notiChat = (Integer) obj.get(UserdbKey.NOTIFICATION_SETTING.NOTI_CHAT);
                Util.addDebugLog("================getChatNotificationSetting " + notiChat);
                if(notiChat != null){
                     result = notiChat;
                }
            }
            return result;
        }
        
        public static HashMap<String, ConcurrentLinkedQueue<String>> getAllBlockUser() {
            HashMap<String, ConcurrentLinkedQueue<String>> result = new HashMap<>();
            try {
                DBCursor cursor = BlockCollection.find();
                while(cursor.hasNext()){
                    DBObject obj = cursor.next();
                    String userId = obj.get(UserdbKey.BLOCK.ID).toString();
                    ConcurrentLinkedQueue<String> blackList = new ConcurrentLinkedQueue<>();
                    BasicDBList blockList = (BasicDBList) obj.get(UserdbKey.BLOCK.BLOCK_LIST);
                    if (blockList != null && !blockList.isEmpty()) {
                        for (Object blockList1 : blockList) {
                            blackList.add(blockList1.toString());
                        }
                    }
                    BasicDBList blockedList = (BasicDBList) obj.get(UserdbKey.BLOCK.BE_BLOCKED_LIST);
                    if (blockedList != null && !blockedList.isEmpty()) {
                        for (Object blockedList1 : blockedList) {
                            blackList.add(blockedList1.toString());
                        }
                    }
                    result.put(userId, blackList);
                }
            } catch (Exception ex) {
                Util.addErrorLog(ex);
            }
            return result;
        }

        public static List<String> getFavouristList( String userid ){
            ArrayList<String> ret = new ArrayList<>();
            try{
                BasicDBObject query = new BasicDBObject();
                query.put( UserdbKey.FAVORIST.ID, new ObjectId( userid ) );
                BasicDBObject obj = (BasicDBObject) FavouristCollection.findOne( query );
                if( obj == null ) return ret;
                BasicDBList favouristList = (BasicDBList) obj.get( UserdbKey.FAVORIST.FAVOURIS_LIST );

                if( favouristList != null ){
                    for (Object favouristList1 : favouristList) {
                        ret.add((String) favouristList1);
                    }
                }
            } catch( Exception ex ) {
                Util.addErrorLog(ex);
            }
            return ret;
        }
        
    }
}
