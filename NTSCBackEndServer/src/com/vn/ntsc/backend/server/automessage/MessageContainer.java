/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.backend.server.automessage;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import eazycommon.exception.EazyException;
import eazycommon.util.Util;
import com.vn.ntsc.backend.dao.admin.AutoMessageChatDAO;
import com.vn.ntsc.backend.dao.admin.AutoNewsNotifyDAO;
import com.vn.ntsc.backend.dao.admin.AutoNotifyDAO;
import com.vn.ntsc.backend.dao.admin.LoginBonusMessageDAO;
import com.vn.ntsc.backend.entity.impl.automessage.Message;


/**
 *
 * @author tuannxv00804
 */
public class MessageContainer {

    //public static Map<String, Session> SS = new HashMap<String, Session>( Config.MaxConcurrent );
    public static final Map<String, Message> m = new ConcurrentHashMap<>();

    static {
        try {
            // get all message has flag = true; not yet send
            m.putAll(AutoMessageChatDAO.getAll());
            m.putAll(AutoNotifyDAO.getAll());
            m.putAll(AutoNewsNotifyDAO.getAll());
	    m.putAll(LoginBonusMessageDAO.getAll());
        } catch (EazyException ex) {
            Util.addErrorLog(ex);
        }
    }

    public static Message getMessage(String id) {
        return m.get(id);
    }

    public static void put(Message msg) {
        m.put(msg.id, msg);
    }

    public static void remove(String id) {
        m.remove(id);
    }    
    
    public static void clear() {
        m.clear();
    }

    public static boolean isEmpty(){
        return m.isEmpty();
    }
   
    public static Collection<Message> getAll() {
        return m.values();
    }  
    
    public static void changeFlag(String id){
        m.get(id).flag = 0;
    }
}
