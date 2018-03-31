/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.jpns.server.workers;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClient;
import com.amazonaws.services.sns.model.CreatePlatformApplicationRequest;
import com.amazonaws.services.sns.model.CreatePlatformApplicationResult;
import com.amazonaws.services.sns.model.CreatePlatformEndpointRequest;
import com.amazonaws.services.sns.model.CreatePlatformEndpointResult;
import com.amazonaws.services.sns.model.DeletePlatformApplicationRequest;
import com.amazonaws.services.sns.model.PublishRequest;
import com.amazonaws.services.sns.model.PublishResult;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.HashMap;
import java.util.Map;
import eazycommon.constant.Constant;
import eazycommon.util.Util;
import org.json.simple.JSONObject;
import com.vn.ntsc.jpns.Config;

public class SNSMobilePush {

    private final static ObjectMapper objectMapper = new ObjectMapper();
    private static final BasicAWSCredentials awsCreds = new BasicAWSCredentials(Config.AWS_ACCESS_KEY, Config.AWS_SECRET_KEY);
    private static final AmazonSNS snsClient = new AmazonSNSClient(awsCreds);
//    private static final CreatePlatformApplicationResult apnsSandboxPlatform = createPlatformApplication(Constant.IOS_APPLICATION_NAME, Platform.APNS_SANDBOX, Config.IOS_ENTERPRISE_CERTIFICATE_KEY, Config.IOS_ENTERPRISE_PRIVATE_KEY);
//    private static final CreatePlatformApplicationResult apnsPlatform = createPlatformApplication(Constant.IOS_APPLICATION_NAME, Platform.APNS, Config.IOS_CERTIFICATE_KEY, Config.IOS_PRIVATE_KEY);
//    private static final CreatePlatformApplicationResult gcmPlatform = createPlatformApplication(Constant.ANDROID_APPLICATION_NAME, Platform.GCM, "", Constant.GOOGLE_API_KEY_PRODCUTION);


    public static void sendSandBoxAppleAppNotification(String deviceToken, String certificate, String privateKey, String applicationName, JSONObject data) {
        sendNotification(Platform.APNS_SANDBOX, certificate, privateKey, deviceToken,
                applicationName, data);
    }

    public static void sendAppleAppNotification(String deviceToken, String certificate, String privateKey, String applicationName, JSONObject data) {
        sendNotification(Platform.APNS, certificate, privateKey, deviceToken,
                applicationName, data);
    }

    public static void sendAndroidAppNotification(String registrationId, String serverAPIKey, String applicationName, JSONObject data) {
        String principal = ""; // principal is not applicable for GCM
        sendNotification(Platform.GCM, principal, serverAPIKey, registrationId,
                applicationName, data);
    }

    private static void sendNotification(Platform platform, String principal,
            String credential, String platformToken, String applicationName, JSONObject data) {
        // Create Platform Application. This corresponds to an app on a platform.
        CreatePlatformApplicationResult platformApplicationResult = createPlatformApplication(
                applicationName, platform, principal, credential);

        // The Platform Application Arn can be used to uniquely identify the Platform Application.
        String platformApplicationArn = platformApplicationResult.getPlatformApplicationArn();

        // Create an Endpoint. This corresponds to an app on a device.
        CreatePlatformEndpointResult platformEndpointResult = createPlatformEndpoint(
                "CustomData - Useful to store endpoint specific data", platformToken, platformApplicationArn);

        // Publish a push notification to an Endpoint.
        publish(platformEndpointResult.getEndpointArn(), platform, data);
        // Delete the Platform Application since we will no longer be using it.
        deletePlatformApplication(platformApplicationArn);
    }
    
//    private static void sendNotification(Platform platform, String principal,
//            String credential, String platformToken, String applicationName, JSONObject data) {
//        // Create Platform Application. This corresponds to an app on a platform.
//        CreatePlatformApplicationResult platformApplicationResult = getPlatformApplication(platform);
//
//        // The Platform Application Arn can be used to uniquely identify the Platform Application.
//        String platformApplicationArn = platformApplicationResult.getPlatformApplicationArn();
//
//        // Create an Endpoint. This corresponds to an app on a device.
//        CreatePlatformEndpointResult platformEndpointResult = createPlatformEndpoint(
//                "CustomData - Useful to store endpoint specific data", platformToken, platformApplicationArn);
//
//        // Publish a push notification to an Endpoint.
//        publish(platformEndpointResult.getEndpointArn(), platform, data);
//        // Delete the Platform Application since we will no longer be using it.
////        deletePlatformApplication(platformApplicationArn);
//    }

//    private static CreatePlatformApplicationResult getPlatformApplication(Platform platform){
//        switch (platform) {
//            case APNS:
//                return apnsPlatform;
//            case APNS_SANDBOX:
//                return apnsSandboxPlatform;
//            case GCM:
//                return gcmPlatform;
//            default:
//                throw new IllegalArgumentException("Platform Not supported : " + platform.name());
//        }
//    }
    
    private static CreatePlatformApplicationResult createPlatformApplication(
            String applicationName, Platform platform, String principal, String credential) {
        CreatePlatformApplicationRequest platformApplicationRequest = new CreatePlatformApplicationRequest();
        Map<String, String> attributes = new HashMap<>();
        attributes.put("PlatformPrincipal", principal);
        attributes.put("PlatformCredential", credential);
        platformApplicationRequest.setAttributes(attributes);
        platformApplicationRequest.setName(applicationName);
        platformApplicationRequest.setPlatform(platform.name());
        return snsClient.createPlatformApplication(platformApplicationRequest);
    }

    private static CreatePlatformEndpointResult createPlatformEndpoint(
            String customData, String platformToken, String applicationArn) {
        CreatePlatformEndpointRequest platformEndpointRequest = new CreatePlatformEndpointRequest();
        platformEndpointRequest.setCustomUserData(customData);
        platformEndpointRequest.setToken(platformToken);
        platformEndpointRequest.setPlatformApplicationArn(applicationArn);
        return snsClient.createPlatformEndpoint(platformEndpointRequest);
    }

    private static PublishResult publish(String endpointArn, Platform platform, JSONObject data) {
        PublishRequest publishRequest = new PublishRequest();
        Map<String, String> messageMap = new HashMap<>();
        String message;
        messageMap.put(platform.name(), getPlatformMessage(platform, data));

        // For direct publish to mobile end points, topicArn is not relevant.
        publishRequest.setTargetArn(endpointArn);
        publishRequest.setMessageStructure("json");
        message = jsonify(messageMap);

        publishRequest.setMessage(message);
        return snsClient.publish(publishRequest);
    }

    private static String getPlatformMessage(Platform platform, JSONObject data) {
        switch (platform) {
            case APNS:
                return getAppleMessage(data);
            case APNS_SANDBOX:
                return getAppleMessage(data);
            case GCM:
                return getAndroidMessage(data);
            default:
                throw new IllegalArgumentException("Platform Not supported : " + platform.name());
        }
    }

    private static String getAppleMessage(JSONObject data) {
        return data.toJSONString();
    }

    private static String getAndroidMessage(JSONObject data) {
        Map<String, Object> androidMessageMap = new HashMap<>();
        androidMessageMap.put("data", data);
        //HUNGDT update delay_while_idle = false
        androidMessageMap.put("delay_while_idle", false);
        androidMessageMap.put("time_to_live", Constant.GCM_TIME_TO_LIVE);
        return jsonify(androidMessageMap);
    }

    private static void deletePlatformApplication(String applicationArn) {
        DeletePlatformApplicationRequest request = new DeletePlatformApplicationRequest();
        request.setPlatformApplicationArn(applicationArn);
        snsClient.deletePlatformApplication(request);
    }

    private static String jsonify(Object message) {
        try {
            return objectMapper.writeValueAsString(message);
        } catch (Exception e) {
            Util.addErrorLog(e);
        }
        return null;
    }

}
