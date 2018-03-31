/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.dao;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import com.vn.ntsc.chatserver.pojos.message.Message;
import com.vn.ntsc.chatserver.pojos.user.UserConnection;

/**
 *
 * @author tuannxv00804
 */
public interface IDAO {

    public void save(Message msg);

    public void save(Message[] msgs);

    public void save(List<Message> msgs);

    public List<Message> getHistory(String user1, String user2, Long timeStamp, int take);

    public List<Message> getNewMessage(String user1, String user2, Long timeStamp);

    public int getUnreadNumber(UserConnection uc, String friendid);

    public void del(String userid, List<String> llFriends);
    public void delFileChat(String userid, List<String> llFriends);

    public boolean updateFileMessage(Message msg);

    public boolean updateReadMessage(String from, String to, String userId);

    public boolean updateReadMessage(String userId, List<String> msgIds);

    public boolean updateReadMessage(String userId, String msgIds);

    public boolean updateCallMessage(String msgId, Message msg);

    public boolean isExistFileMessage(Message msg);

    public boolean isUser(String friendid);

    public List<String> getListUserIds(Collection<String> ids);

    public Message getMessages(String userId, String messageId);

    public boolean removeMessage(String userId, String messageId);

    public List<String> getDeactivate();

    //HUNGDT add 20425
    public boolean updateCallMessage(String from, String to, String callId, String value, String msgId);

    public void delMsg(String userId, List<String> messageIdList);

    public Message getLastMessage(String userId, String friendId);

    public List<String> getFileValueById(String userId, List<String> listMsgId);

    public void removeButKeepFavoristFriends(String userid, List<String> llFav);
    public void removeAllFileChat(String userid, List<String> llFav);

    public String getFileChat(String from, String to, Long take, Long skip, Long type,Long sort);

}
