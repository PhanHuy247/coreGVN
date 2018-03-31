/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.usermanagementserver.server.unlockpool;

import eazycommon.util.Util;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import eazycommon.constant.Constant;
import com.vn.ntsc.usermanagementserver.dao.impl.UnlockDAO;

/**
 *
 * @author tuannxv00804
 */
public class UnlockCleaner extends Thread {

    private static final int UNLOCK_CLEANER_DELAY = 5 * Constant.A_MINUTE;
    
    public static void startUnlockCleaner() {
        Map<String, UnlockInfor> map = UnlockDAO.initUnlockPool();
        UnlockManager.putAll(map);
        UnlockCleaner cl = new UnlockCleaner();
        cl.start();
    }

    @Override
    public void run() {
        while (true) {
            try {
                List<String> removeIds = new ArrayList<>();
                Collection<UnlockInfor> unlockList = UnlockManager.getAll();
                Iterator<UnlockInfor> iter = unlockList.iterator();
                while (iter.hasNext()) {
                    UnlockInfor u = iter.next();
                    long time = Util.getGMTTime().getTime();
                    if (u.getEndTime() < time) {
                        removeIds.add(u.getId());
//                        UnlockDAO.removeUnlock(u.getId());
                        UnlockManager.remove(u.getId());
                    }
                }
                UnlockDAO.removeByIds(removeIds);
                Thread.sleep(UNLOCK_CLEANER_DELAY);
            } catch (Exception ex) {
               Util.addErrorLog(ex);
            }
        }
    }
}
