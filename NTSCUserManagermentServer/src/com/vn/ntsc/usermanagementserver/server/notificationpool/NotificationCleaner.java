/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.usermanagementserver.server.notificationpool;

import eazycommon.util.Util;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import eazycommon.constant.Constant;
import com.vn.ntsc.usermanagementserver.dao.impl.NotificationDAO;

/**
 *
 * @author tuannxv00804
 */
public class NotificationCleaner extends Thread {
    
    private static final Map<String, NotificationInfor> m = new ConcurrentHashMap<>();    

    public static void startNotificationCleaner() {
        Map<String, NotificationInfor> map = NotificationDAO.getAll();
        m.putAll(map);
        NotificationCleaner nc = new NotificationCleaner();
        nc.start();
    }

//    @Override
//    public void run() {
//        while (true) {
//            try {
//                NotificationDAO.remove();
//                Thread.sleep(Constant.TIME_PER_DAY);
//            } catch (Exception ex) {
//                Util.addErrorLog(ex);
//            }
//        }
//    }
//    
    @Override
    public void run(){
        try {
            while (true){
                removeExpiredNotification();
                Thread.sleep(Constant.A_DAY);                
            }
        } catch (InterruptedException ex) {
            Util.addErrorLog(ex);
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
    }

    private void removeExpiredNotification() throws Exception {
        List<String> removeIds = new ArrayList<>();
        for (Map.Entry<String,NotificationInfor> entry: m.entrySet()){
            NotificationInfor infor = entry.getValue();
            if (isExpiredNotification(infor)){
                m.remove(entry.getKey());
                removeIds.add(entry.getKey());
            }
        }
        NotificationDAO.removeByIds(removeIds);
    }
    
    public static void put(String id, long time) {
        m.put(id, new NotificationInfor(id, time));
    }  
    
    private static final int SIX_WEEKS = 42; 
    private boolean isExpiredNotification(NotificationInfor info) {    
        long time = Util.currentTime();
        return ((time - info.time) / Constant.A_DAY) > SIX_WEEKS;
    }    
}
