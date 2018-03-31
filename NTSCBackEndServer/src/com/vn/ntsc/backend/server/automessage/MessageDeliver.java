/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.backend.server.automessage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import eazycommon.constant.API;
import eazycommon.constant.Constant;
import eazycommon.constant.ParamKey;
import eazycommon.exception.EazyException;
import eazycommon.util.DateFormat;
import eazycommon.util.Util;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import com.vn.ntsc.backend.Config;
import com.vn.ntsc.backend.dao.admin.AutoMessageChatDAO;
import com.vn.ntsc.backend.dao.admin.AutoNewsNotifyDAO;
import com.vn.ntsc.backend.dao.admin.AutoNotifyDAO;
import com.vn.ntsc.backend.dao.admin.AutoQANotifyDAO;
import com.vn.ntsc.backend.dao.admin.LoginBonusMessageDAO;
import com.vn.ntsc.backend.dao.admin.LoginTrackingDAO;
import com.vn.ntsc.backend.dao.user.UserDAO;
import com.vn.ntsc.backend.entity.impl.automessage.AutoMessagePackage;
import com.vn.ntsc.backend.entity.impl.automessage.Message;
import com.vn.ntsc.backend.entity.impl.automessage.extend.AutoMessage;
import com.vn.ntsc.backend.entity.impl.automessage.extend.AutoNewsNotify;
import com.vn.ntsc.backend.entity.impl.automessage.extend.AutoNotify;
import com.vn.ntsc.backend.entity.impl.automessage.extend.AutoQANotify;
import com.vn.ntsc.backend.entity.impl.automessage.extend.LoginBonusMessage;
import com.vn.ntsc.backend.entity.impl.user.User;
import com.vn.ntsc.backend.server.common.Utility;

/**
 *
 * @author DUONGLTD
 */
public class MessageDeliver extends Thread {

    public static void startThread() {
        MessageDeliver md = new MessageDeliver();
        md.start();
    }

    @Override
    public void run() {
        while (true) {
            try {
                Collection<Message> msgList = MessageContainer.getAll();
                Iterator<Message> iter = msgList.iterator();
                while (iter.hasNext()) {
                    Util.addDebugLog("sendMessage " + msgList.toString());
                    Message msg = iter.next();
                    long time = Util.getGMTTime().getTime();
                    if (DateFormat.parse_yyyyMMddHHmm(msg.time).getTime() < time) {
                        List<String> receivers = sendMessage(msg);
                        updateFlag(msg, receivers);
                        if (!(msg instanceof LoginBonusMessage)) {
                            MessageContainer.remove(msg.id);
                        }
                    }
                }
                Thread.sleep(Constant.A_MINUTE);
            } catch (Exception ex) {
                Util.addErrorLog(ex);

            }
        }
    }

    public List<String> sendMessage(Message message) {
        List<String> listFriend = new ArrayList<>();
        try {
            Util.addDebugLog("sendMessage " + message.toString());
            String serverIp;
            int serverPort;
            if (message instanceof LoginBonusMessage) {
                LoginBonusMessage loginMessage = (LoginBonusMessage) message;
                listFriend = LoginTrackingDAO.getUserId(loginMessage.getLogin_bonus_times(), loginMessage.id, loginMessage.getGender());
                if (!listFriend.isEmpty()) {
                    JSONObject request = new JSONObject();
                    request.put(ParamKey.API_NAME, API.AUTO_MESSAGE_VAR);
                    request.put(ParamKey.USER_ID, loginMessage.getMessage_type());
                    request.put(ParamKey.IP, message.ip);
                    // request to chat server to sent message for user
                    serverIp = Config.ChatServerIP;
                    serverPort = Config.ChatServerPort;
                    String content = loginMessage.getContent();
                    JSONArray autoMessagePackageArray = new JSONArray();
                    for (String friendId : listFriend) {
                        AutoMessagePackage autoMessagePackage = new AutoMessagePackage(friendId, content);
                        autoMessagePackageArray.add(autoMessagePackage.toJsonObject());
                        if (autoMessagePackageArray.size() == 200) {
                            request.put(ParamKey.FRIEND_LIST, autoMessagePackageArray);
                            Util.sendRequest(request.toJSONString(), serverIp, serverPort);
                            autoMessagePackageArray.clear();
                        }
                    }
                    if (!autoMessagePackageArray.isEmpty()) {
                        request.put(ParamKey.FRIEND_LIST, autoMessagePackageArray);
                        Util.sendRequest(request.toJSONString(), serverIp, serverPort);
                    }
                }
            } else {
                List<String> purchaseList = new ArrayList<>();
                boolean getUserPurchaseByOtherSystem = Utility.getPurchaseList(message.query, purchaseList);
                if (getUserPurchaseByOtherSystem) {
                    purchaseList = UserDAO.getPurchaseUsers();
                }

                List<User> listUserFriend = UserDAO.getListUserAutoMessage(message.query, purchaseList);
                for (User user : listUserFriend) {
                    listFriend.add(user.userId);
                }

//            System.out.println("lisst send Friend : " + listFriend);
                JSONObject request = new JSONObject();
                boolean isSent = !listUserFriend.isEmpty();

                if (message instanceof AutoMessage) {
                    if (isSent) {
                        JSONArray autoMessagePackageArray = new JSONArray();
                        AutoMessage autoMess = (AutoMessage) message;
                        request.put(ParamKey.API_NAME, API.AUTO_MESSAGE_VAR);
                        request.put(ParamKey.USER_ID, autoMess.sender);
                        request.put(ParamKey.IP, message.ip);
                        // request to chat server to sent message for user
                        serverIp = Config.ChatServerIP;
                        serverPort = Config.ChatServerPort;
                        //Util.addDebugLog("message " + message);
                        for (User user : listUserFriend) {
                            String content = "";
                            if (user != null && message.content != null) {
                                //Util.addDebugLog("Point " + user.point);
                                if (user.point == null) {
                                    //Util.addDebugLog("user.username " + user.username);
                                    user.point = 0L;
                                }
                                if (user.username != null) {
                                    content = message.content.replace("##NAME##", user.username).replace("##POINT##", user.point.toString());
                                }
                            }
                            AutoMessagePackage autoMessagePackage = new AutoMessagePackage(user.userId, content);
                            autoMessagePackageArray.add(autoMessagePackage.toJsonObject());
                            if (autoMessagePackageArray.size() == 200) {
                                request.put(ParamKey.FRIEND_LIST, autoMessagePackageArray);
                                Util.sendRequest(request.toJSONString(), serverIp, serverPort);
                                autoMessagePackageArray.clear();
                            }
                        }
                        if (!autoMessagePackageArray.isEmpty()) {
                            request.put(ParamKey.FRIEND_LIST, autoMessagePackageArray);
                            Util.sendRequest(request.toJSONString(), serverIp, serverPort);
                        }
                    }
                } else if (message instanceof AutoNotify) {
                    AutoNotify autoNoti = (AutoNotify) message;
                    Util.addDebugLog("AutoNoticheck check");
                    if (isSent) {
                        request.put(ParamKey.API_NAME, API.PUSH_NOTIFICATION_FROM_BACK_END);
                        request.put(ParamKey.CONTENT, autoNoti.content);
                        request.put(ParamKey.URL, autoNoti.url);
                        request.put(ParamKey.IP, autoNoti.ip);
                        request.put("push_id", autoNoti.id);
                        request.put("type", 0L);
                        serverIp = Config.MainServerIp;
                        serverPort = Config.MainServerPort;
                        List<String> sendList = new ArrayList<>();
                        for (String string : listFriend) {
                            sendList.add(string);
                            if (sendList.size() == 200) {
                                request.put(ParamKey.FRIEND_LIST, sendList);
                                Util.sendRequest(request.toJSONString(), serverIp, serverPort);
                                sendList.clear();
                            }
                        }
                        if (!sendList.isEmpty()) {
                            request.put(ParamKey.FRIEND_LIST, sendList);
                            Util.sendRequest(request.toJSONString(), serverIp, serverPort);
                        }
                    }
                } else if (message instanceof AutoNewsNotify) {

                    AutoNewsNotify autoNewsNoti = (AutoNewsNotify) message;
                    Util.addDebugLog("AutoNews check");
                    if (isSent) {
                        request.put(ParamKey.API_NAME, API.PUSH_NOTIFICATION);
                        request.put(ParamKey.CONTENT, autoNewsNoti.content);
                        request.put(ParamKey.URL, autoNewsNoti.url);
                        request.put(ParamKey.IP, autoNewsNoti.ip);
                        request.put("push_id", autoNewsNoti.id);
                        request.put("type", 33L);
                        serverIp = Config.MainServerIp;
                        serverPort = Config.MainServerPort;
                        List<String> sendList = new ArrayList<>();
                        for (String string : listFriend) {
                            sendList.add(string);
                            if (sendList.size() == 200) {
                                request.put(ParamKey.FRIEND_LIST, sendList);
                                Util.sendRequest(request.toJSONString(), serverIp, serverPort);
                                sendList.clear();
                            }
                        }
                        if (!sendList.isEmpty()) {
                            request.put(ParamKey.FRIEND_LIST, sendList);
                            Util.sendRequest(request.toJSONString(), serverIp, serverPort);
                        }
                    }
                } else if (message instanceof AutoQANotify) {

                    AutoQANotify autoNewsNoti = (AutoQANotify) message;
                    Util.addDebugLog("AutoNews check");
                    if (isSent) {
                        request.put(ParamKey.API_NAME, API.PUSH_NOTIFICATION);
                        request.put(ParamKey.CONTENT, autoNewsNoti.content);
                        request.put(ParamKey.URL, autoNewsNoti.url);
                        request.put(ParamKey.IP, autoNewsNoti.ip);
                        request.put("push_id", autoNewsNoti.id);
                        request.put("type", 34L);
                        serverIp = Config.MainServerIp;
                        serverPort = Config.MainServerPort;
                        List<String> sendList = new ArrayList<>();
                        for (String string : listFriend) {
                            sendList.add(string);
                            if (sendList.size() == 200) {
                                request.put(ParamKey.FRIEND_LIST, sendList);
                                Util.sendRequest(request.toJSONString(), serverIp, serverPort);
                                sendList.clear();
                            }
                        }
                        if (!sendList.isEmpty()) {
                            request.put(ParamKey.FRIEND_LIST, sendList);
                            Util.sendRequest(request.toJSONString(), serverIp, serverPort);
                        }
                    }
                }
            }

        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
        return listFriend;
    }

    public void updateFlag(Message message, List<String> receivers) throws EazyException {
        if (message instanceof AutoMessage) {
            AutoMessageChatDAO.updateMessage(message.id, Constant.FLAG.OFF, receivers);
        } else if (message instanceof AutoNotify) {
            AutoNotifyDAO.updateMessage(message.id, Constant.FLAG.OFF, receivers);
        } else if (message instanceof LoginBonusMessage) {
            LoginTrackingDAO.updateMessageSent(message.id, receivers);
            LoginBonusMessageDAO.updateReceivers(message.id, receivers);
        } else if (message instanceof AutoQANotify) {
            AutoQANotifyDAO.updateMessage(message.id, Constant.FLAG.OFF, receivers);
        } else {
            AutoNewsNotifyDAO.updateMessage(message.id, Constant.FLAG.OFF, receivers);
        }

    }
}
