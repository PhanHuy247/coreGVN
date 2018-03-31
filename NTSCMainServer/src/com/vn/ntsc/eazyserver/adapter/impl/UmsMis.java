/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.eazyserver.adapter.impl;

import java.util.List;
import eazycommon.constant.API;
import eazycommon.constant.Constant;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.ParamKey;
import eazycommon.constant.ResponseMessage;
import eazycommon.util.Util;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import com.vn.ntsc.Config;
import com.vn.ntsc.Setting;
import com.vn.ntsc.dao.impl.UserSessionDAO;
import com.vn.ntsc.dao.impl.UserTokenDAO;
import com.vn.ntsc.eazyserver.adapter.IServiceAdapter;
import com.vn.ntsc.eazyserver.adapter.impl.util.InterCommunicator;
import com.vn.ntsc.eazyserver.server.request.Request;
import com.vn.ntsc.eazyserver.server.session.Session;
import com.vn.ntsc.eazyserver.server.session.SessionManager;
import com.vn.ntsc.otherservice.entity.impl.FileUrl;
import com.vn.ntsc.otherservice.statistic.CmCodeCollector;
import com.vn.ntsc.otherservice.statistic.UserCounter;
import java.util.HashMap;
import java.util.LinkedList;

/**
 *
 * @author tuannxv00804
 */
public class UmsMis {

    public static final LoginAdapter loginAdapter = new LoginAdapter();
    public static final ChangePassCaseForget changePassAdapter = new ChangePassCaseForget();
    public static final ChangePass changePass = new ChangePass();

    static class LoginAdapter implements IServiceAdapter {

        @Override
        public String callService(Request request) {
            int checkCode = checkUsableVersion(request);
            if (checkCode != ErrorCode.SUCCESS) {
                JSONObject json = new JSONObject();
                json.put(ParamKey.ERROR_CODE, checkCode);
                return json.toJSONString();
            }
            String result = UmsMis.callService(request);

            JSONObject jsonResult = new JSONObject();
            try {
                jsonResult = (JSONObject) (new JSONParser().parse(result));
                Long code = (Long) jsonResult.get(ParamKey.ERROR_CODE);
                if (code == ErrorCode.SUCCESS) {
                    JSONObject joData = (JSONObject) jsonResult.get(ParamKey.DATA);
                    String userId = (String) joData.get(ParamKey.USER_ID);
                    //statistic
                    Long deviceType = (Long) request.getParamValue(ParamKey.DEVICE_TYPE);
                    Long gender = (Long) joData.get(ParamKey.GENDER);
                    String avaId = (String) joData.get(ParamKey.AVATAR_ID);
                    UserCounter.count(userId, deviceType.intValue(), gender.intValue(), false);

                    activate(joData, userId);
                    request.put(ParamKey.USER_ID, userId);
                    joData.put(ParamKey.CHAT_NUMBER, getChatNubmer(request));
                    joData.put("switch_safari_version", Setting.turnOffSafaryVersion);
                    joData.put("get_free_point", Setting.turnOffGetFreePoint);
                    joData.put("turn_off_user_info", Setting.turnOffExtendedUserInfo);
                    joData.put("turn_off_show_news", Setting.turnOffShowNews);
                    joData.put("switch_browser_android_version", Setting.turnOffBrowserAndroidVersion);
                    joData.put("get_free_point_android", Setting.turnOffGetFreePointAndroid);
                    joData.put("turn_off_user_info_android", Setting.turnOffExtendedUserInfoAndroid);
                    joData.put("turn_off_show_news_android", Setting.turnOffShowNewsAndroid);
                    joData.remove("hobby");
//                    joData.put( "switch_safari_version_for_enterprise", Setting.enterpriseturnOffSafaryVersion );
                    if (avaId != null){
                        List<String> listImgId = new LinkedList<>();
                        listImgId.add(avaId);
                        HashMap<String, FileUrl> imgMap = InterCommunicator.getImage(listImgId);
                        FileUrl url = imgMap.get(avaId);
                    if (url != null){
                        joData.put(ParamKey.THUMBNAIL_URL, url.getThumbnail());
                        joData.put(ParamKey.ORIGINAL_URL, url.getOriginalUrl());
                    }
                }
                    addNotificationToken(request, userId, joData);

                    addCmCodeLog(joData, request);

//                    Boolean isVerify = (Boolean) joData.get(ParamKey.IS_VERIFY);
//                    if (isVerify != null && isVerify) {
                        String meetResult = updateOnlineStatus(userId);
                        String ip = (String) request.getParamValue(ParamKey.IP);
                        JSONObject meetJson = new JSONObject();
                        try {
                            meetJson = (JSONObject) new JSONParser().parse(meetResult);
                        } catch (ParseException ex) {
                            Util.addErrorLog(ex);
                        }

                        dailyBonus(joData, userId, ip);
                        Boolean isChange = (Boolean) meetJson.get(ParamKey.IS_CHANGE);
                        sendNotification(isChange, userId, ip);
//                    } else {
//                        String token = (String) joData.get(ParamKey.TOKEN_STRING);
//                        Session sess = SessionManager.getSession(token);
//                        sess.normalUser = false;
//                    }
                }
            } catch (Exception ex) {
                Util.addErrorLog(ex);
            }
            return jsonResult.toJSONString();
        }

    }

    private static void dailyBonus(JSONObject joData, String userId, String ip) {
        Long isNoti = (Long) joData.get(ParamKey.IS_NOTI);
        if (isNoti != null && isNoti == Constant.FLAG.ON) {
            Long addPoint = (Long) joData.get(ParamKey.ADD_POINT);
            JSONObject notiRequest = new JSONObject();
            notiRequest.put(ParamKey.API_NAME, API.NOTI_DAILY_BONUS);
            notiRequest.put(ParamKey.NOTI_USER_ID, userId);
            notiRequest.put(ParamKey.POINT, addPoint.toString());
            notiRequest.put(ParamKey.IP, ip);
            InterCommunicator.sendRequest(notiRequest.toJSONString(), Config.NotificationServerIP, Config.NotificationPort);
        }
    }

    private static void sendNotification(Boolean isChange, String userId, String ip) {
        if (isChange) {
            JSONObject umsRequest = new JSONObject();
            umsRequest.put(ParamKey.API_NAME, API.NOTI_LOGIN);
            umsRequest.put(ParamKey.USER_ID, userId);
            umsRequest.put(ParamKey.IP, ip);
            String umsString = InterCommunicator.sendRequest(umsRequest.toJSONString(), Config.UMSServerIP, Config.UMSPort);
            JSONObject umsJson = new JSONObject();
            try {
                umsJson = (JSONObject) new JSONParser().parse(umsString);
            } catch (ParseException ex) {
                Util.addErrorLog(ex);

            }
            Long umsCode = (Long) umsJson.get(ParamKey.ERROR_CODE);
            if (umsCode == ErrorCode.SUCCESS) {
                JSONObject umsData = (JSONObject) umsJson.get(ParamKey.DATA);
                JSONArray umsArr = (JSONArray) umsData.get(ParamKey.LIST_EMAIL);
                JSONObject notiRequest = new JSONObject();
                notiRequest.put(ParamKey.TO_LIST_USER_ID, umsArr);
                notiRequest.put(ParamKey.API_NAME, API.NOTI_ONLINE_ALERT);
                notiRequest.put(ParamKey.FROM_USER_ID, userId);
                notiRequest.put(ParamKey.IP, ip);
                InterCommunicator.sendRequest(notiRequest.toJSONString(), Config.NotificationServerIP, Config.NotificationPort);
            }
        }
    }

    private static void addCmCodeLog(JSONObject joData, Request request) {
        Long deviceType = (Long) request.getParamValue(ParamKey.DEVICE_TYPE);
        String cmCode = (String) joData.get(ParamKey.CM_CODE);
        CmCodeCollector.loginLog(cmCode, deviceType.intValue());
    }

    private static void activate(JSONObject joData, String userId) {
        Long IsActive = (Long) joData.get(ParamKey.IS_ACTIVE_USER);
        if (IsActive != null && IsActive == Constant.FLAG.ON) {
            joData.put(ParamKey.EMAIL, userId);
            joData.put(ParamKey.API_NAME, API.REGISTER);
            InterCommunicator.sendRequest(joData.toJSONString(), Config.MeetPeopleServerIP, Config.MeetPeopleServerPort);

            joData.put(ParamKey.API_NAME, API.ACTIVATE);
//            InterCommunicator.sendRequest(joData.toJSONString(), Config.FreeSwitchServerIp, Config.FreeSwitchServerPort);
            InterCommunicator.sendRequest(joData.toJSONString(), Config.ChatServerIP, Config.ChatServerPort);
            InterCommunicator.sendRequest(joData.toJSONString(), Config.BuzzServerIP, Config.BuzzServerPort);
        }
//        joData.remove(ParamKey.EMAIL);
        joData.remove(ParamKey.API_NAME);
        joData.remove(ParamKey.IS_ACTIVE_USER);
        joData.remove(ParamKey.INTEREST);
//        joData.remove(ParamKey.BIRTHDAY);
        joData.remove(ParamKey.ETHNIC);
        joData.remove(ParamKey.PASSWORD);
    }

    private static String updateOnlineStatus(String userId) {
        JSONObject requestPre = new JSONObject();
        requestPre.put(ParamKey.API_NAME, API.LOGIN);
        requestPre.put(ParamKey.USER_ID, userId);
        String meetResult = InterCommunicator.sendRequest(requestPre.toJSONString(), Config.MeetPeopleServerIP, Config.MeetPeopleServerPort);
        return meetResult;
    }

    static class ChangePassCaseForget implements IServiceAdapter {

        @Override
        public String callService(Request request) {
            int checkCode = checkUsableVersion(request);
            if (checkCode != ErrorCode.SUCCESS) {
                JSONObject json = new JSONObject();
                json.put(ParamKey.ERROR_CODE, checkCode);
                return json.toJSONString();
            }
            String result = UmsMis.callService(request);
            JSONObject jsonResult = new JSONObject();
            try {
                jsonResult = (JSONObject) (new JSONParser().parse(result));
                Long code = (Long) jsonResult.get(ParamKey.ERROR_CODE);
                if (code == ErrorCode.SUCCESS) {
                    JSONObject joData = (JSONObject) jsonResult.get(ParamKey.DATA);
                    String userId = (String) joData.get(ParamKey.USER_ID);
                    String token = (String) joData.get(ParamKey.TOKEN_STRING);
                    request.put(ParamKey.USER_ID, userId);

                    //statistic
                    Long deviceType = (Long) request.getParamValue(ParamKey.DEVICE_TYPE);
                    Long gender = (Long) joData.get(ParamKey.GENDER);
                    UserCounter.count(userId, deviceType.intValue(), gender.intValue(), false);

                    activate(joData, userId);
                    changePass(token);

//                    update freeswitch service
//                    if(!Config.IsDebug){
//                    String newPassword = (String) request.getParamValue(ParamKey.NewPassword);
//                    JSONObject freeSwitchRequest = new JSONObject();
//                    freeSwitchRequest.put(ParamKey.UserID, userId);
//                    freeSwitchRequest.put(ParamKey.Password, newPassword);
//                    InterCommunicator.sendRequest( freeSwitchRequest.toJSONString(), Config.FreeSwitchServerIp, Config.FreeSwitchServerPort );
//                    }
//                    
                    joData.put(ParamKey.CHAT_NUMBER, getChatNubmer(request));
                    joData.put("switch_safari_version", Setting.turnOffSafaryVersion);
                    joData.put("get_free_point", Setting.turnOffGetFreePoint);
                    joData.put("turn_off_user_info", Setting.turnOffExtendedUserInfo);
                    joData.put("turn_off_show_news", Setting.turnOffShowNews);
                    joData.put("switch_browser_android_version", Setting.turnOffBrowserAndroidVersion);
                    joData.put("get_free_point_android", Setting.turnOffGetFreePointAndroid);
                    joData.put("turn_off_user_info_android", Setting.turnOffExtendedUserInfoAndroid);
                    joData.put("turn_off_show_news_android", Setting.turnOffShowNewsAndroid);
//                    joData.put( "switch_safari_version_for_enterprise", Setting.enterpriseturnOffSafaryVersion );

                    jsonResult.put(ParamKey.DATA, joData);

                    addNotificationToken(request, userId, joData);
                    String ip = (String) request.getParamValue(ParamKey.IP);
                    addCmCodeLog(joData, request);
                    dailyBonus(joData, userId, ip);
                    String meetResult = updateOnlineStatus(userId);
                    JSONObject meetJson = new JSONObject();
                    try {
                        meetJson = (JSONObject) new JSONParser().parse(meetResult);
                    } catch (ParseException ex) {
                        Util.addErrorLog(ex);
                    }
                    Boolean isChange = (Boolean) meetJson.get(ParamKey.IS_CHANGE);
                    sendNotification(isChange, userId, ip);
                }
            } catch (Exception ex) {
                Util.addErrorLog(ex);
            }
            return jsonResult.toJSONString();
        }

    }

    static class ChangePass implements IServiceAdapter {

        @Override
        public String callService(Request request) {
            String result = UmsMis.callService(request);
            JSONObject jsonResult;
            try {
                jsonResult = (JSONObject) (new JSONParser().parse(result));
                Long code = (Long) jsonResult.get(ParamKey.ERROR_CODE);
                if (code == ErrorCode.SUCCESS) {
                    changePass(request.token);
//                    update freeswitch service
//                    if(!Config.IsDebug){
//                    String newPassword = (String) request.getParamValue(ParamKey.NewPassword);
//                    JSONObject freeSwitchRequest = new JSONObject();
//                    String userId = (String) request.getParamValue(ParamKey.UserID);
//                    freeSwitchRequest.put(ParamKey.UserID, userId);
//                    freeSwitchRequest.put(ParamKey.Password, newPassword);
//                    InterCommunicator.sendRequest( freeSwitchRequest.toJSONString(), Config.FreeSwitchServerIp, Config.FreeSwitchServerPort );
//                    }
                }
            } catch (ParseException ex) {
                Util.addErrorLog(ex);
            }
            return result;
        }
    }

    public static void changePass(String token) {
        List<Session> listSession = SessionManager.removeSessionsOfUserExcudeToken(token);
        try {
            UserSessionDAO.remove(listSession);
            UserTokenDAO.remove(listSession);
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
    }

    public static void changePassInBackend(String token, String userId) {
        List<Session> listSession = SessionManager.removeSessionsOfUserExcudeTokenInBackend(token, userId);
        try {
            UserSessionDAO.remove(listSession);
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
    }

    public static int getChatNubmer(Request request) {
        try {
            request.put(ParamKey.API_NAME, API.TOTAL_UNREAD);
            String resultChatLog = InterCommunicator.sendRequest(request.toJson(), Config.ChatServerIP, Config.ChatServerPort);
            int number = Integer.parseInt(resultChatLog);
            return number;
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            return 0;
        }
    }

    private static void addNotificationToken(Request request, String userId, JSONObject joData) {
        request.put(ParamKey.API_NAME, API.LOGIN);
        String notiToken = (String) request.getParamValue(ParamKey.NOTIFY_TOKEN);
        Long deviceType = (Long) request.getParamValue(ParamKey.DEVICE_TYPE);
        request.put(ParamKey.NOTI_USER_ID, userId);
        String userName = (String) joData.get(ParamKey.USER_NAME);
        request.put(ParamKey.NOTI_USERNAME, userName);
        request.put(ParamKey.NOFI_DEVICE_TOKEN, notiToken);
        request.reqObj.put(ParamKey.NOFI_DEVICE_TYPE, deviceType);
        //HUNGDT add 16-04-2016
        String deviceId = (String) request.getParamValue("device_id");
        request.put("device_id", deviceId);

//        if (deviceType == 0) {
//            String idfa = (String) request.getParamValue("idfa");
//            request.put("idfa", idfa);
//            Util.addDebugLog("idfa: " + idfa);
//
//        } else if (deviceType == 1) {
//            String gps_adid = (String) request.getParamValue("gps_adid");
//            request.put("gps_adid", gps_adid);
//            Util.addDebugLog("gps_adid: " + gps_adid);
//        }
        //END
        // LongLT 13Sep2016 /////////////////////////// START #4484
        String adid = (String) request.getParamValue("adid");
        if (adid != null) {
            request.put("adid", adid);
            Util.addDebugLog("adid: " + adid);
        }
        String applicationId = "1";
        if ((Long) request.getParamValue("application") != null) {
            applicationId = ((Long) request.getParamValue("application")).toString();
        }
        request.put("application_id", applicationId);
        Util.addDebugLog("application_id : " + applicationId);

        String voip_notify_token = (String) request.getParamValue(ParamKey.VOIP_NOTI_TOKEN);
        request.put("voip_notify_token", voip_notify_token);
        
        // LongLT 13Sep2016 /////////////////////////// END #4484
        InterCommunicator.sendRequest(request.toJson(), Config.NotificationServerIP, Config.NotificationPort);
    }

    public static int checkUsableVersion(Request request) {
        int code = ErrorCode.OUT_OF_DATE_API;
        String applicationVersion = (String) request.getParamValue("application_version");
        if (applicationVersion != null && !applicationVersion.isEmpty()) {
            String usableVersion;
            Long deviceType = (Long) request.getParamValue(ParamKey.DEVICE_TYPE);
            if (deviceType == Constant.DEVICE_TYPE.IOS) {
                Long applicationType = (Long) request.getParamValue("applicaton_type");
                if (applicationType != null && applicationType == Constant.APPLICATION_TYPE.IOS_PRODUCTION_APPLICATION) {
                    usableVersion = Setting.ios_non_enterprise_usable_version;
                } else {
                    usableVersion = Setting.ios_enterprise_usable_version;
                }
            } else {
                usableVersion = Setting.android_usable_version;
            }
            if (usableVersion != null && !usableVersion.isEmpty()) {
                String[] usalbeVersionElements = usableVersion.split("\\.");
                String[] applicationVersionElements = applicationVersion.split("\\.");
                if (usalbeVersionElements.length <= applicationVersionElements.length) {
                    for (int i = 0; i < usalbeVersionElements.length; i++) {
                        String usalbeVersionElement = "." + usalbeVersionElements[i];
                        String applicationVersionElement = "." + applicationVersionElements[i];
                        try {
                            double usableVersionE = Double.parseDouble(usalbeVersionElement);
                            double applicationVersionE = Double.parseDouble(applicationVersionElement);
                            if (applicationVersionE < usableVersionE) {
                                return ErrorCode.UNUSABLE_VERSION_APPLICATION;
                            } else if (applicationVersionE > usableVersionE) {
                                return ErrorCode.SUCCESS;
                            }
                        } catch (NumberFormatException ex) {
                            int compare = applicationVersionElement.compareTo(usalbeVersionElement);
                            if (compare < 0) {
                                return ErrorCode.UNUSABLE_VERSION_APPLICATION;
                            } else if (compare > 0) {
                                return ErrorCode.SUCCESS;
                            }
                        }
                    }
                    return ErrorCode.SUCCESS;
                } else {
                    for (int i = 0; i < applicationVersionElements.length; i++) {
                        String usalbeVersionElement = "." + usalbeVersionElements[i];
                        String applicationVersionElement = "." + applicationVersionElements[i];
                        try {
                            double usableVersionE = Double.parseDouble(usalbeVersionElement);
                            double applicationVersionE = Double.parseDouble(applicationVersionElement);
                            if (applicationVersionE < usableVersionE) {
                                return ErrorCode.UNUSABLE_VERSION_APPLICATION;
                            } else if (applicationVersionE > usableVersionE) {
                                return ErrorCode.SUCCESS;
                            }
                        } catch (NumberFormatException ex) {
                            int compare = applicationVersionElement.compareTo(usalbeVersionElement);
                            if (compare < 0) {
                                return ErrorCode.UNUSABLE_VERSION_APPLICATION;
                            } else if (compare > 0) {
                                return ErrorCode.SUCCESS;
                            }
                        }
                    }
                    return ErrorCode.UNUSABLE_VERSION_APPLICATION;
                }
            } else {
                return ErrorCode.SUCCESS;
            }
        }
        return code;
    }

    public static String callService(Request request) {
        String result;
        try {
            result = InterCommunicator.sendRequest(request.toJson(), Config.UMSServerIP, Config.UMSPort);
            JSONObject jsonResult = (JSONObject) (new JSONParser().parse(result));
            Long code = (Long) jsonResult.get(ParamKey.ERROR_CODE);

            JSONObject joData = (JSONObject) jsonResult.get(ParamKey.DATA);
            if (joData != null) {
                String userid = (String) joData.get(ParamKey.USER_ID);
                if (code == ErrorCode.SUCCESS) {
                    String applicationVersion = (String) request.getParamValue("application_version");
                    String deviceId = (String) request.getParamValue("device_id");
                    Long applicationTypeL = (Long) request.getParamValue("applicaton_type");
                    int applicationType = Constant.APPLICATION_TYPE.ANDROID_APPLICATION;
                    if (applicationTypeL != null) {
                        applicationType = applicationTypeL.intValue();
                    } else {
                        Long deviceType = (Long) request.getParamValue(ParamKey.DEVICE_TYPE);
                        if (deviceType == Constant.DEVICE_TYPE.IOS) {
                            applicationType = Constant.APPLICATION_TYPE.IOS_ENTERPRISE_APPLICATION;
                        }
                    }
                    Session session = new Session(userid, true, applicationVersion, applicationType,deviceId);
                    request.token = session.token;
                    SessionManager.putSession(session);
                    UserSessionDAO.add(session);
                    UserTokenDAO.add(session);
                    joData.put(ParamKey.TOKEN_STRING, session.token);
                    jsonResult.put(ParamKey.DATA, joData);
                    result = jsonResult.toJSONString();
                }
            }

        } catch (Exception ex) {
            Util.addErrorLog(ex);
            result = ResponseMessage.UnknownError;
        }
        return result;
    }
}
