/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.eazyserver.server.session;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import eazycommon.constant.API;
import eazycommon.constant.Constant;
import eazycommon.constant.ParamKey;
import eazycommon.util.Util;
import org.json.simple.JSONObject;
import com.vn.ntsc.Config;
import com.vn.ntsc.dao.impl.UserDAO;
import com.vn.ntsc.dao.impl.UserSessionDAO;
import com.vn.ntsc.eazyserver.adapter.impl.util.InterCommunicator;

/**
 *
 * @author tuannxv00804
 */
public class GabageCollector extends Thread {

    public static void startCleaningService() {
        GabageCollector g = new GabageCollector();
        g.start();
    }

    @Override
    public void run() {
        while( true ) {
            try {
                Collection<Session> ss = SessionManager.getAllSession();
                Iterator<Session> iter = ss.iterator();
                long now = Util.currentTime();
                LinkedList<String> autoLogoutUsers = new LinkedList<>();
                List<String> removeTokens = new ArrayList<>();
                while( iter.hasNext() ){
                    Session s = iter.next();
                    if( s.sessionExpire < now ){
                        List<Session> sessions = SessionManager.getSessionsByUserId(s.userID);
                        if(!sessions.isEmpty() && sessions.size() == 1){
                            autoLogoutUsers.add(s.userID);
                        }
                        SessionManager.removeSession( s.token );
                        if(s.token != null && !s.token.isEmpty()){
                            removeTokens.add(s.token);
                        }
//                        UserSessionDAO.remove(s);
                        //Linh #9880: remove device id when token expire
                        String deviceId = (String) UserDAO.getUserInfor(s.userID, ParamKey.DEVICE_ID);
                        if (deviceId != null && !deviceId.isEmpty()){
                            UserDAO.unsetThisDeviceId(deviceId);
                        }
                    }
                }
                UserSessionDAO.removeByTokens(removeTokens);
                if(!autoLogoutUsers.isEmpty()){
                    JSONObject request = new JSONObject();
                    request.put(ParamKey.API_NAME, API.AUTO_LOG_OUT);
                    request.put(ParamKey.LIST, autoLogoutUsers);
                    InterCommunicator.sendRequest( request.toJSONString(), Config.MeetPeopleServerIP, Config.MeetPeopleServerPort );
                }
                Thread.sleep( Constant.A_MINUTE );
            } catch( Exception ex ) {
                Util.addErrorLog(ex);                
            }
        }
    }
}
