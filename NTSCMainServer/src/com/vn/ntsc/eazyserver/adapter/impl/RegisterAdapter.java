/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.eazyserver.adapter.impl;

import eazycommon.constant.API;
import eazycommon.constant.Constant;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.ParamKey;
import eazycommon.constant.ResponseMessage;
import eazycommon.util.Util;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import com.vn.ntsc.Config;
import com.vn.ntsc.Setting;
import com.vn.ntsc.dao.impl.UserSessionDAO;
import com.vn.ntsc.dao.impl.UserTokenDAO;
import com.vn.ntsc.eazyserver.adapter.IServiceAdapter;
import com.vn.ntsc.eazyserver.adapter.impl.util.InterCommunicator;
import com.vn.ntsc.eazyserver.server.request.Request;
import com.vn.ntsc.eazyserver.server.session.Session;
import com.vn.ntsc.eazyserver.server.session.SessionManager;
import com.vn.ntsc.otherservice.statistic.UserCounter;

/**
 *
 * @author RuAc0n
 */
public class RegisterAdapter {

    public static final Register register = new Register();
    public static final RegisterByWeb registerByWeb = new RegisterByWeb();
    
    static class Register implements IServiceAdapter {

        @Override
        public String callService(Request request) {
            int checkCode = UmsMis.checkUsableVersion(request);
            String ip1 = (String) request.getParamValue(ParamKey.IP);
            if (checkCode != ErrorCode.SUCCESS) {
                JSONObject json = new JSONObject();
                json.put(ParamKey.ERROR_CODE, checkCode);
                return json.toJSONString();
            }
            String result = null;
            try {
                try {
                    result = InterCommunicator.sendRequest(request.toJson(), Config.UMSServerIP, Config.UMSPort);
                } catch (Exception ex) {
                    Util.addErrorLog(ex);
                    return ResponseMessage.UnknownError;
                }
                JSONObject jsonResult = (JSONObject) (new JSONParser().parse(result));
                Long code = (Long) jsonResult.get(ParamKey.ERROR_CODE);
                JSONObject joData = (JSONObject) jsonResult.get(ParamKey.DATA);
                if (joData != null) {
                    String userid = (String) joData.get(ParamKey.USER_ID);
                    String applicationId = "1";
                    if ((Long) request.getParamValue("application") != null) {
                        applicationId = ((Long) request.getParamValue("application")).toString();
                    }
                    if (code == ErrorCode.SUCCESS) {
                        // statistic
                        Long deviceType = (Long) request.getParamValue(ParamKey.DEVICE_TYPE);
                        //
                        Long gender = (Long) joData.get(ParamKey.GENDER);
                        UserCounter.count(userid, deviceType.intValue(), gender.intValue(), true);

                        String notiToken = (String) request.getParamValue(ParamKey.NOTIFY_TOKEN);
                        boolean normalUser = false;

                        Boolean isVerify = (Boolean) joData.get(ParamKey.IS_VERIFY);
                        if (isVerify != null && isVerify) {
                            Boolean isVideo = (Boolean) joData.get(ParamKey.VIDEO_CALL_WAITING);
                            Boolean isVoice = (Boolean) joData.get(ParamKey.VOICE_CALL_WAITING);
                            request.reqObj.put(ParamKey.VOICE_CALL_WAITING, isVoice);
                            request.reqObj.put(ParamKey.VIDEO_CALL_WAITING, isVideo);
                            request.put(ParamKey.USER_ID, userid);
                            request.put(ParamKey.EMAIL, userid);
                            String regDate = (String) joData.get(ParamKey.REGISTER_DATE);
                            request.put(ParamKey.REGISTER_DATE, regDate);
                            InterCommunicator.sendRequest(request.toJson(), Config.MeetPeopleServerIP, Config.MeetPeopleServerPort);

                            updateOnlineStatus(userid);
                            normalUser = true;
                        }

                        String applicationVersion = (String) request.getParamValue("application_version");
                        String deviceId = (String) request.getParamValue("device_id");
                        Long applicationTypeL = (Long) request.getParamValue("applicaton_type");
                        int applicationType = Constant.APPLICATION_TYPE.ANDROID_APPLICATION;
                        if (applicationTypeL != null) {
                            applicationType = applicationTypeL.intValue();
                        } else if (deviceType == Constant.DEVICE_TYPE.IOS) {
                            applicationType = Constant.APPLICATION_TYPE.IOS_ENTERPRISE_APPLICATION;
                        }
                        Session session = new Session(userid, normalUser, applicationVersion, applicationType,deviceId);
                        request.token = session.token;
                        SessionManager.putSession(session);
                        UserSessionDAO.add(session);
                        UserTokenDAO.add(session);
                        joData.put(ParamKey.TOKEN_STRING, session.token);
//                    joData.put("switch_safari", Setting.turnOffSafary);
                        joData.put("switch_safari", true);
//                    joData.put("switch_safari_version", Setting.turnOffSafaryVersion);
                        joData.put("switch_safari_version", "0.0");
                        joData.put("get_free_point", Setting.turnOffGetFreePoint);
                        joData.put("turn_off_user_info", Setting.turnOffExtendedUserInfo);
                        joData.put("turn_off_show_news", Setting.turnOffShowNews);
                        joData.put("switch_browser_android", Setting.turnOffBrowserAndroid);
                        joData.put("switch_browser_android_version", Setting.turnOffBrowserAndroidVersion);
                        joData.put("get_free_point_android", Setting.turnOffGetFreePointAndroid);
                        joData.put("turn_off_user_info_android", Setting.turnOffExtendedUserInfoAndroid);
                        joData.put("turn_off_show_news_android", Setting.turnOffShowNewsAndroid);
//                    joData.put( "switch_safari_for_enterprise", Setting.enterpriseTurnOffSafary );
//                    joData.put( "switch_safari_version_for_enterprise", Setting.enterpriseturnOffSafaryVersion );

                        String userName = (String) request.getParamValue(ParamKey.USER_NAME);

                        joData.put(ParamKey.CHAT_NUMBER, 0);
                        joData.remove(ParamKey.BIRTHDAY);
                        joData.remove(ParamKey.INTEREST);
                        joData.remove(ParamKey.ETHNIC);
                        result = jsonResult.toJSONString();

                        JSONObject notiRequest = new JSONObject();
                        notiRequest.put(ParamKey.API_NAME, API.LOGIN);
                        notiRequest.put(ParamKey.NOTI_USER_ID, userid);
                        notiRequest.put(ParamKey.NOTI_USERNAME, userName);
                        notiRequest.put(ParamKey.NOFI_DEVICE_TOKEN, notiToken);
                        notiRequest.put(ParamKey.NOFI_DEVICE_TYPE, deviceType);
                        notiRequest.put("device_id", deviceId);
                        notiRequest.put("applicaton_type", applicationType);

//                    if (deviceType == 0) {
//                        String idfa = (String) request.getParamValue("idfa");
//                        notiRequest.put("idfa", idfa);
//                        Util.addDebugLog("idfa: " + idfa);
//
//                    } else if (deviceType == 1) {
//                        String gps_adid = (String) request.getParamValue("gps_adid");
//                        notiRequest.put("gps_adid", gps_adid);
//                        Util.addDebugLog("gps_adid1: " + gps_adid);
//                    }
                        // LongLT 13Sep2016 /////////////////////////// START #4484
                        String adid = (String) request.getParamValue("adid");
                        if (adid != null) {
                            notiRequest.put("adid", adid);
                            Util.addDebugLog("adid: " + adid);
                        }
                        // LongLT 13Sep2016 /////////////////////////// END #4484

                        //String deviceId = (String) request.getParamValue("device_id");
                        //notiRequest.put("device_id", deviceId);
                        Util.addDebugLog("device_id: " + deviceId);
                        notiRequest.put("application_id", applicationId);
                        Util.addDebugLog("application_id : " + applicationId);

                        //HUNGDT add callkit
                        String voip_notify_token = (String) request.getParamValue(ParamKey.VOIP_NOTI_TOKEN);
                        notiRequest.put("voip_notify_token", voip_notify_token);

                        InterCommunicator.sendRequest(notiRequest.toJSONString(), Config.NotificationServerIP, Config.NotificationPort);

//                     add daily bonus Notification
                        Long addPoint = (Long) joData.get(ParamKey.ADD_POINT);
                        if (addPoint != null && addPoint > 0) {
                            notiRequest.clear();
                            notiRequest.put(ParamKey.API_NAME, API.NOTI_DAILY_BONUS);
                            notiRequest.put(ParamKey.NOTI_USER_ID, userid);
                            notiRequest.put(ParamKey.POINT, addPoint.toString());
                            String ip = (String) request.getParamValue(ParamKey.IP);
                            notiRequest.put(ParamKey.IP, ip);

                            InterCommunicator.sendRequest(notiRequest.toJSONString(), Config.NotificationServerIP, Config.NotificationPort);
                            joData.remove(ParamKey.ADD_POINT);
                        }
                    }
                }
            } catch (Exception ex) {
                Util.addErrorLog(ex);
            }
            return result;
        }

    }
    
    static class RegisterByWeb implements IServiceAdapter {

        @Override
        public String callService(Request request) {
            int checkCode = UmsMis.checkUsableVersion(request);
            String ip1 = (String) request.getParamValue(ParamKey.IP);
            if (checkCode != ErrorCode.SUCCESS) {
                JSONObject json = new JSONObject();
                json.put(ParamKey.ERROR_CODE, checkCode);
                return json.toJSONString();
            }
            String result = null;
            try {
                try {
                    result = InterCommunicator.sendRequest(request.toJson(), Config.UMSServerIP, Config.UMSPort);
                } catch (Exception ex) {
                    Util.addErrorLog(ex);
                    return ResponseMessage.UnknownError;
                }
                JSONObject jsonResult = (JSONObject) (new JSONParser().parse(result));
                Long code = (Long) jsonResult.get(ParamKey.ERROR_CODE);
                JSONObject joData = (JSONObject) jsonResult.get(ParamKey.DATA);
                if (joData != null) {
                    String userid = (String) joData.get(ParamKey.USER_ID);
                    if (code == ErrorCode.SUCCESS) {
                        // statistic
                        Long deviceType = (Long) request.getParamValue(ParamKey.DEVICE_TYPE);
                        //
                        Long gender = (Long) joData.get(ParamKey.GENDER);
                        UserCounter.count(userid, deviceType.intValue(), gender.intValue(), true);

                        String notiToken = (String) request.getParamValue(ParamKey.NOTIFY_TOKEN);

                        Boolean isVideo = (Boolean) joData.get(ParamKey.VIDEO_CALL_WAITING);
                        Boolean isVoice = (Boolean) joData.get(ParamKey.VOICE_CALL_WAITING);
                        request.reqObj.put(ParamKey.VOICE_CALL_WAITING, isVoice);
                        request.reqObj.put(ParamKey.VIDEO_CALL_WAITING, isVideo);
                        request.put(ParamKey.USER_ID, userid);
                        request.put(ParamKey.EMAIL, userid);
                        String regDate = (String) joData.get(ParamKey.REGISTER_DATE);
                        request.put(ParamKey.REGISTER_DATE, regDate);
                        InterCommunicator.sendRequest(request.toJson(), Config.MeetPeopleServerIP, Config.MeetPeopleServerPort);
                        updateOnlineStatus(userid);

                        boolean normalUser = true;
                        String applicationVersion = (String) request.getParamValue("application_version");
                        String deviceId = (String) request.getParamValue("device_id");
                        Long applicationTypeL = (Long) request.getParamValue("applicaton_type");
                        int applicationType = Constant.APPLICATION_TYPE.ANDROID_APPLICATION;
                        if (applicationTypeL != null) {
                            applicationType = applicationTypeL.intValue();
                        }
                        
                        Session session = new Session(userid, normalUser, applicationVersion, applicationType,deviceId);
                        request.token = session.token;
                        SessionManager.putSession(session);
                        UserSessionDAO.add(session);
                        UserTokenDAO.add(session);
                        
                        if (applicationType != Constant.APPLICATION_TYPE.WEB_APPLICATION){
                            joData.put(ParamKey.TOKEN_STRING, session.token);
                            joData.put("switch_safari", true);
                            joData.put("switch_safari_version", "0.0");
                            joData.put("get_free_point", Setting.turnOffGetFreePoint);
                            joData.put("turn_off_user_info", Setting.turnOffExtendedUserInfo);
                            joData.put("turn_off_show_news", Setting.turnOffShowNews);
                            joData.put("switch_browser_android", Setting.turnOffBrowserAndroid);
                            joData.put("switch_browser_android_version", Setting.turnOffBrowserAndroidVersion);
                            joData.put("get_free_point_android", Setting.turnOffGetFreePointAndroid);
                            joData.put("turn_off_user_info_android", Setting.turnOffExtendedUserInfoAndroid);
                            joData.put("turn_off_show_news_android", Setting.turnOffShowNewsAndroid);

                            String userName = (String) request.getParamValue(ParamKey.USER_NAME);

                            joData.put(ParamKey.CHAT_NUMBER, 0);
                            result = jsonResult.toJSONString();

                            JSONObject notiRequest = new JSONObject();
                            notiRequest.put(ParamKey.API_NAME, API.LOGIN);
                            notiRequest.put(ParamKey.NOTI_USER_ID, userid);
                            notiRequest.put(ParamKey.NOTI_USERNAME, userName);
                            notiRequest.put(ParamKey.NOFI_DEVICE_TOKEN, notiToken);
                            notiRequest.put(ParamKey.NOFI_DEVICE_TYPE, deviceType);
                            notiRequest.put("device_id", deviceId);
                            notiRequest.put("applicaton_type", applicationType);

                            // LongLT 13Sep2016 /////////////////////////// START #4484
                            String adid = (String) request.getParamValue("adid");
                            if (adid != null) {
                                notiRequest.put("adid", adid);
                                Util.addDebugLog("adid: " + adid);
                            }
                            String voip_notify_token = (String) request.getParamValue(ParamKey.VOIP_NOTI_TOKEN);
                            notiRequest.put("voip_notify_token", voip_notify_token);

                            InterCommunicator.sendRequest(notiRequest.toJSONString(), Config.NotificationServerIP, Config.NotificationPort);

                            Long addPoint = (Long) joData.get(ParamKey.ADD_POINT);
                            if (addPoint != null && addPoint > 0) {
                                notiRequest.clear();
                                notiRequest.put(ParamKey.API_NAME, API.NOTI_DAILY_BONUS);
                                notiRequest.put(ParamKey.NOTI_USER_ID, userid);
                                notiRequest.put(ParamKey.POINT, addPoint.toString());
                                String ip = (String) request.getParamValue(ParamKey.IP);
                                notiRequest.put(ParamKey.IP, ip);

                                InterCommunicator.sendRequest(notiRequest.toJSONString(), Config.NotificationServerIP, Config.NotificationPort);
                                joData.remove(ParamKey.ADD_POINT);
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

    private static String updateOnlineStatus(String userId) {
        JSONObject requestPre = new JSONObject();
        requestPre.put(ParamKey.API_NAME, API.LOGIN);
        requestPre.put(ParamKey.USER_ID, userId);
        String meetResult = InterCommunicator.sendRequest(requestPre.toJSONString(), Config.MeetPeopleServerIP, Config.MeetPeopleServerPort);
        return meetResult;
    }
    
}
