/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.jpns.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import eazycommon.constant.ParamKey;
import eazycommon.util.Util;
import java.util.Date;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import com.vn.ntsc.jpns.server.manager.APIManager;
import com.vn.ntsc.jpns.server.manager.IApiAdapter;

/**
 *
 * @author tuannxv00804
 */
public class Handler extends AbstractHandler {

    @Override
    public void handle(String string, Request request, HttpServletRequest hsr, HttpServletResponse response) throws IOException, ServletException {
        request.setHandled(true);

        InputStreamReader isr = new InputStreamReader(request.getInputStream());
        BufferedReader reader = new BufferedReader(isr);
        string = reader.readLine();

        JSONParser parser = new JSONParser();
        JSONObject jo = null;
        try {
            jo = (JSONObject) parser.parse(string);
        } catch (Exception ex) {
            Util.addErrorLog(ex);

            jo = null;
        }
        if (jo == null) {
            return;
        }
        String api = null;
        try {
            api = (String) jo.get(ParamKey.API_NAME);
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            api = null;
        }
        if (api != null) {
            IApiAdapter adapter = APIManager.getApi(api);
            if (adapter != null){
                adapter.execute(jo, new Date());
            }
        }
        Util.addDebugLog("Handle." + api + " -> jo = " + jo.toJSONString());
    }

    
    
//    private void checkedProfile(JSONObject jo) {
//        try {
//            String api = (String) jo.get(ParamKey.API_NAME);
//            String fromUserid = (String) jo.get(ParamKey.FROM_USER_ID);
//            String fromUsername = Core.dao.getUsername(fromUserid);
//            String toUserid = (String) jo.get(ParamKey.TOUSERID);
//            String ip = (String) jo.get(ParamKey.IP);
//
//            if (fromUserid != null && fromUsername != null && toUserid != null) {
//                JSONObject msg = MsgUtil.iosPayload(api, fromUsername, fromUserid, toUserid);
//                send(msg, toUserid);
//                LogNotificationDAO.addLog(toUserid, Params.getNotiType(api), fromUserid, Util.getGMTTime(), ip);
//            }
//
//        } catch (Exception ex) {
//            Util.addErrorLog(ex);
//
//        }
//
//    }
//
//
//
//    private void favorited(JSONObject jo) {
//        try {
//            String api = (String) jo.get(ParamKey.API_NAME);
//            String fromUserid = (String) jo.get(ParamKey.FROM_USER_ID);
//            String toUserid = (String) jo.get(ParamKey.TOUSERID);
//            String fromUsername = Core.dao.getUsername(fromUserid);
//            String ip = (String) jo.get(ParamKey.IP);
//
//            if (fromUserid != null && fromUsername != null && toUserid != null) {
//                JSONObject msg = MsgUtil.iosPayload(api, fromUsername, fromUserid, toUserid);
//                send(msg, toUserid);
//                LogNotificationDAO.addLog(toUserid, Params.getNotiType(api), fromUserid, Util.getGMTTime(), ip);
//            }
//
//        } catch (Exception ex) {
//            Util.addErrorLog(ex);
//
//        }
//
//    }
//
////    private void likeYourBuzzOldVS(JSONObject jo) {
////        try {
////            String api = (String) jo.get(ParamKey.API_NAME);
////            String fromUserid = (String) jo.get(ParamKey.FROM_USER_ID);
////            String fromUsername = Core.dao.getUsername(fromUserid);
////            String toUser = (String) jo.get(ParamKey.TOUSERID);
////            String buzzid = (String) jo.get(ParamKey.NOTI_BUZZ_ID);
////            String ip = (String) jo.get(ParamKey.IP);
////
////            if (fromUserid != null && fromUsername != null && toUser != null && buzzid != null) {
////                JSONObject msg = null;
////                //Check user off ko?
////                //neu off -> sua key cua msg
////                //neu on thi giu nguyen
////                NotificationSetting notification = NotificationSettingDAO.getNotificationSetting(toUser);
////               
////                if (notification.notiLike == 0) { //OFF push noti
////                    msg = MsgUtil.iosPayload(api, fromUsername, buzzid, null, toUser, true);
////                } else if (notification.notiLike == 1) {//ON push noti
////                    msg = MsgUtil.iosPayload(api, fromUsername, buzzid, null, toUser, false);
////                }
////
////                send(msg, toUser);
////                LogNotificationDAO.addLog(toUser, Params.getNotiType(api), fromUserid, Util.getGMTTime(), ip);
////            }
////
////        } catch (Exception ex) {
////            Util.addErrorLog(ex);
////        }
////    }
////    
//    private void likeYourBuzz(JSONObject jo) {
//        try {
//            String api = (String) jo.get(ParamKey.API_NAME);
//            String fromUserid = (String) jo.get(ParamKey.FROM_USER_ID);
//            String fromUsername = Core.dao.getUsername(fromUserid);
//            String toUser = (String) jo.get(ParamKey.TOUSERID);
//            String buzzid = (String) jo.get(ParamKey.NOTI_BUZZ_ID);
//            String ip = (String) jo.get(ParamKey.IP);
//            if (fromUserid != null && fromUsername != null && toUser != null && buzzid != null) {
//                JSONObject msg = null;
//                //Check user off ko?
//                //neu off -> sua key cua msg
//                //neu on thi giu nguyen
//                NotificationSetting notification = NotificationSettingDAO.getNotificationSetting(toUser);
//                if (notification.notiLike == 0) { //OFF push noti
//                    msg = MsgUtil.iosPayload(api, fromUsername, buzzid, null, toUser, true);
//                } else if (notification.notiLike == 1) {//ON push noti
//                    msg = MsgUtil.iosPayload(api, fromUsername, buzzid, null, toUser, false);
//                }
//                send(msg, toUser);
//                LogNotificationDAO.addLog(toUser, Params.getNotiType(api), fromUserid, Util.getGMTTime(), ip);
//            }
//
//        } catch (Exception ex) {
//            Util.addErrorLog(ex);
//        }
//    }
//
//
//    private void alsoLikeBuzz(JSONObject jo) {
//        try {
//            String api = (String) jo.get(ParamKey.API_NAME);
//            String fromUserid = (String) jo.get(ParamKey.FROM_USER_ID);
//            String fromUsername = Core.dao.getUsername(fromUserid);
//            String buzzid = (String) jo.get(ParamKey.NOTI_BUZZ_ID);
//            String ownerName = (String) jo.get(ParamKey.NOTI_BUZZ_OWNER_NAME);
//            String ip = (String) jo.get(ParamKey.IP);
//            if (fromUserid != null && fromUsername != null && buzzid != null && ownerName != null) {
//                JSONArray toUserList = (JSONArray) jo.get(ParamKey.TO_LIST_USER_ID);
//                if (toUserList != null) {
//                    for (Object toUserList1 : toUserList) {
//                        String toUserid = (String) toUserList1;
//                        JSONObject msg = MsgUtil.iosPayload(api, fromUsername, buzzid, ownerName, toUserid);
//                        send(msg, toUserid);
//                        LogNotificationDAO.addLog(toUserid, Params.getNotiType(api), fromUserid, Util.getGMTTime(), ip);
//                    }
//                }
//            }
//        } catch (Exception ex) {
//            Util.addErrorLog(ex);
//
//        }
//    }
//
//
//    private void alsoResponsedBuzzz(JSONObject jo) {
//        try {
//            String api = (String) jo.get(ParamKey.API_NAME);
//            String fromUserid = (String) jo.get(ParamKey.FROM_USER_ID);
//            String fromUsername = Core.dao.getUsername(fromUserid);
//            String buzzid = (String) jo.get(ParamKey.NOTI_BUZZ_ID);
//            String ownerName = (String) jo.get(ParamKey.NOTI_BUZZ_OWNER_NAME);
//            JSONArray toList = (JSONArray) jo.get(ParamKey.TO_LIST_USER_ID);
//            String ip = (String) jo.get(ParamKey.IP);
//
//            if (fromUserid != null && fromUsername != null && buzzid != null && ownerName != null && toList != null) {
//                for (Object toList1 : toList) {
//                    String toUser = (String) toList1;
//                    JSONObject msg = MsgUtil.iosPayload(api, fromUsername, buzzid, ownerName, toUser);
//                    send(msg, toUser);
//                    LogNotificationDAO.addLog(toUser, Params.getNotiType(api), fromUserid, Util.getGMTTime(), ip);
//                }
//
//            }
//
//        } catch (Exception ex) {
//            Util.addErrorLog(ex);
//
//        }
//    }

}
