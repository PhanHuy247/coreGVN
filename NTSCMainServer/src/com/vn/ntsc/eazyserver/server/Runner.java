/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.eazyserver.server;

import java.util.LinkedList;
import com.vn.ntsc.dao.impl.AdjustCMCodeDAO;
import com.vn.ntsc.dao.impl.DeviceDAO;
import com.vn.ntsc.dao.impl.UserDAO;
import eazycommon.util.Util;
import com.vn.ntsc.eazyserver.server.Device;

/**
 *
 * @author hungdt
 */
public class Runner extends Thread {

    public static void startService() {
        Util.addDebugLog("Start worker Runner");
        Runner g = new Runner();
        g.start();
    }

    @Override
    public void run() {
        while (true) {
            try {
                checkTracker();
                sleep();

            } catch (Exception ex) {
                Util.addErrorLog(ex);

            }
        }
    }

    private static void sleep() {
        try {
            Thread.sleep(5 * 60 * 1000);
        } catch (Exception ex) {

        }
    }

    private boolean checkTracker() {
        boolean result = false;
        DeviceDAO dao = new DeviceDAO();
        AdjustCMCodeDAO adDao = new AdjustCMCodeDAO();
        try {
            LinkedList<Device> ll = dao.getDevices();
            if (ll != null) {
                for (Device d : ll) {
                    //Util.addDebugLog("deviceid: " + d.deviceid);
                    if (d != null && d.checkid != null && !d.checkid.isEmpty()) {
                        //Util.addDebugLog("checkid: " + d.checkid);
                        String tracker_id = adDao.getTrackerId(d.checkid);
                        //Util.addDebugLog("tracker_id: " + tracker_id);
                        String label = adDao.getLabel(d.checkid);
                        String os_name = adDao.os_name(d.checkid);
                        //Util.addDebugLog("os_name: " + os_name);
                        if (label != null) {
                            String oldCm_code = UserDAO.getCMCode(d.userid);
                            if (oldCm_code == null || oldCm_code.isEmpty()) {
                                boolean resultUpdate = UserDAO.updateCmCode(d.userid, label);
                                UserDAO.updateOSName(d.userid, os_name);
                                UserDAO.updateAdid(d.userid, d.checkid);
                                UserDAO.updateDeviceId(d.userid, d.deviceid);
                                //Util.addDebugLog("resultUpdate: " + resultUpdate);
                                if (resultUpdate) {
                                    //Util.addDebugLog("resultUpdate: flag update");
                                    boolean resultFlag = AdjustCMCodeDAO.updateFlag(d.checkid, 1);
                                    //Util.addDebugLog("resultUpdate: flag update " + resultFlag);
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }

        return result;
    }
}
