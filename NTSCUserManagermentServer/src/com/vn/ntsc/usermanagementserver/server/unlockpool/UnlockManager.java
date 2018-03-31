/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.usermanagementserver.server.unlockpool;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author tuannxv00804
 */
public class UnlockManager {

    private static final Map<String, UnlockInfor> m = new ConcurrentHashMap<>();

//    static{
//        m.putAll(UnlockDAO.initUnlockPool());
//    }
    public static UnlockInfor getInfor(String id) {
        return m.get(id);
    }

    public static void put(UnlockInfor infor) {
        m.put(infor.getId(), infor);
    }
    
    public static void putAll(Map<String, UnlockInfor> infors) {
        m.putAll(infors);
    }

    public static void remove(String id) {
        m.remove(id);
    }

    public static void clear() {
        m.clear();
    }

    public static Collection<UnlockInfor> getAll() {
        return m.values();
    }

}
