/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.jpns;

import static com.vn.ntsc.usermanagementserver.Config.STREAMING_HOST;
import java.io.FileInputStream;
import java.util.Properties;
import eazycommon.util.Util;

/**
 *
 * @author tuannxv00804
 */
public class Config {

    //LongLT add
    public static int ServerPort = 9119;

//    public static String NotificationServerIP = "192.168.6.226";
    public static String NotificationServerIP = "localhost";
    public static int NotificationPort = 3221;

//    public static String UMSServerIP = "192.168.6.226";
    public static String UMSServerIP = "localhost";
    public static int UMSPort = 8090;

    public static String MeetPeopleServerIP = "localhost";
    public static int MeetPeopleServerPort = 2612;

    public static String BuzzServerIP = "localhost";
    public static int BuzzServerPort = 9115;

    public static String ChatServerIP = "localhost";
    public static int ChatServerPort = 9116;

    public static String BackEndServerIp = "localhost";
    public static int BackEndServerPort = 9120;

    public static String StaticFileServerIp = "localhost";
    public static int StaticFileServerPort = 9117;

//    public static String DB_SERVER = "192.168.6.222";
    public static String DB_SERVER = "localhost";
    public static int DB_PORT = 27017;

    public static int GabageCollector_ThreadNumber = 1;
    public static int SessionTimeOut = 180; //minutes

    public static String IOSCerPath = "Meets_Stag_Dis_Push_Cer_20170614.p12";
    public static String IOSCerPath_enterprise = "CertificatesPro.p12";

    public static String IOS_CERTIFICATE_KEY = "-----BEGIN CERTIFICATE-----\n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\n-----END CERTIFICATE-----";
    public static String IOS_PRIVATE_KEY = "-----BEGIN PRIVATE KEY-----\nMIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQC/4Tveb5ECWZPqnfyUC39oMoG7T3uQO0hQNmtba9XeAmgqlAmFjP9Y4ch+NlZIVyzBARW6/eqm/892UuTzUy8Cr8ZVxG6EvKkstgO2Q6H8auYF8Iymt7X5odRpOXfbNOoyXXVDPWhafiLPxPP+7WL/7tVZrY9tBoIkH1aatXoGNtBbvWBI7MXWN85mJK4rEFVtodLZAd2ABMMgbtjgGp2FBCWyUA+QbEGZMtGKJ5WmInSFtDgGmykXQr6G/7DRfRXV1Jx0BAeOyacoXK1zjYbhofiFCi87IrOahVdLxPTDasv+0hANGoH/1DECXYiPeO4Ss4yw4ogSYrgPfqANYow7AgMBAAECggEAFYf2qd0nDnZyzK7xusMXht+0AwrGOMcONEIHBfkxxHnMqop2DxBlTtn3H4KFaBSwJvAyOgtoG1k1NHfUzSEWc1y6CsHCDQ8GhMff3sH0GVj9esklX0faGar5JN2X/3cOmJw4HlVzskPZMCtPndmRdFqegjOhloBWdis2EqIukSe/pAp9zCPz50MRkQX2IdDpLIg8kmIJh9e6d2MMMckWaCmBcR0Aa9VCCKn0jFCVZ9tx9/lAd6CiyZJe3BWYuYzpQVsj5/o4pTDn9zKZZon0OIc3GdQlK3BnF6P0ZLDoLbuiEWYDOQqkoatCdxwjlZcXPbbwCGCnZIMVp9crSa0cUQKBgQDqciN3Q/C5WRaQiv3nUlBdC9lavq00QAh3Jxtz9tnpbW6lkLLhYLp15c0bqKCR5KIkTx9Hy5a6phE5keaTcwzh4ByfALGak7K9HrcGzEAPwmAVYGOlQMLbe0tfuan2VMThBZjPXmkUkDtQRPutnetNX87tdTu/g0+GYGH7MmRiJwKBgQDRhUV24xZdRxHBt4V99dDIx6kZ0Cl/nsRPWLmLUD+nB4EddJaDTg+UxZGrW9BWGnLQDzPwjEwgqszUunXkhHbtoeFNMsco+wkWuzuqX51Ll3HM0+pQ5wjMWrd+i5XYrMHprzZyN29v7nPDw6CPe5cRP98PoHCx9fWrJGVt5O9VzQKBgQCB5xZhqiKDAKrxEiKXftOKits3Xbmo3uYR53hdq+SYY4tQb7m+CZOnadpZPfkifXWd+r1RUaxF5Whb5szE3JXBviT9sSu3g6khAxjUym2gjcohbhQoZ5bQeCOOediD5fpk2G4QULaWr0g+NMrjK0JK0BBfIHsnpYF16oe/4O8gNwKBgHpiN14nbWPU5Ilb8ozN7kOpZJwgXG0gRxj2AYNfv56IIqIa/OP7z9wdv2XxvnLdBh2EE9m5iao3Omgz9jMiMPnDagAW3gCRChEOEt8E9LM35hA35JGXMitz/eTcEg47cxVV9B6pRN2D0nkSJxJIpcg8n8XX+JvqyLJMEzcq16YtAoGAdF4dGIKnzZeCeibZAwEkWs3e0s4pvLDnp3pprJaft9uAVK1nbZeQ2rNtfrCDhoXSU1kl03bn2kawL8Dhh+bq546U6znGObXus3hanqt6fSiPDNiOa4dkYlT86cbW8IWWgAXbbPoXBuQCbDSjl3oHjecbpOKVnQDI3h1orhbG69I=\n-----END PRIVATE KEY-----";

    public static String IOS_ENTERPRISE_CERTIFICATE_KEY = "-----BEGIN CERTIFICATE-----\n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\n-----END CERTIFICATE-----";
    public static String IOS_ENTERPRISE_PRIVATE_KEY = "-----BEGIN PRIVATE KEY-----\nMIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCtlGogG8YiK07LrYdMtEkH8bnIiZ/OZqFZv3QPmloERduAqflUFZys9y/yTWY3j+YypxxLxD8D85saN8jPY7cJQifSUMFoh4I8VSQ+dLIJYew2W6gYH2XoaPbzQSu66hqhaEnC/XlUA0C9a3SdnHNpgax+nJ3YZvvFXW0Ko90/s+VUZyEcZdFPixYxIhifSCJMKptIwE1uVQlYQsWkEkcqg67cdFaLaiM7OD3v2WdmXYNs6bWR/P9GM5/Q5Ppz8zYy+GVoupUjlxn5hYVOPPvlBgcdnXr+DTK273evupLJR8M6K75BkeaQXzOcTqFS94PSjbZi54x1EUm2nmzilkYfAgMBAAECggEALo3UnMH2NOnam0PyOr9LqwbI5iELLEdYVQLFNP4X0HeP9IUh6sxVFgrB4EGj/Zjpi4JIU4XRxNSML87wW7cJu0Dz/zNLfCaD5nyLWPjhWyTZPob8oWOiaF/mg71h5iKte8bFqbdj2AV7UpJEV5VDWvO9eMO2jfio4/k3qpfiWhgLbupjdeahuOUDRsrAP50UITA93Y1ca4O+VrhttvS9Cg72lfVtuyhIOm3HCl4eRZkIeCmPTcMsGUwdlf1eCsldKlZBMwC09F4HWxagstFJm5/xea8hXyndO2COTHiKRctuWGTQ01F/y39LwLArbU+nOJA9SaMz8MkthCJPFRsC2QKBgQDaiSw7vGTuwuTkgy/Dv76o9XbrRF5FKo+o2o7BPfMzZklnDE72xpu+yQz7yoXBKMFyBUOTHvtGW92PCcYmYVG3Cj+Q1RzaBaghMVmeyNdxJBTBu0SirH9YYcF7WBhzmVxRuoX4/elL4bNxtJJw8CrUAOzepAnHP4zocNllwf+FXQKBgQDLVkPn62nT3FmVFb9PwXicrswNnIyl/EHJ9dWgLNFvGnbFowZmYxfG9uFo2yl9jTb12JTiKCreeGjet9fMBDEHyvqUy4QZGbl+utSl0bNAfZpR0CeLG6IIkkuZm/ji3uqU/q05cXlIVbH1UW5EbynkSqNOazcddJgcRqkef0zlqwKBgDWduAOD6kOF/4BtCzf8gDuV1bJNGM/hbFOiI6oXl+aBbvrgUzNKcQWy8AOKoZxNcoCV+AlNbZrKiSZB4KKj99dvpT7UenqnLqPOJs3FOs5gatNJLcK3jZrSiRTI/V7HPpYyrBH1wdpDKrJ3+gHJesh+3Z6WImwHFHwmy7+ihB1lAoGACKv+K2br1YBBRc1uok0jArOlujLiALY4bA+S88jVicJ8LEOeo0E4o2CmDl3CcDtUi7mX5+lEosZJ2q3Gg0nueWGbvDrNwHBLa+814HiIB4kTysdw2EruZyUuGjf/rlVJdd9Crf9756zX8Rlu9XY0OCtCubZy3Eu7rQEKd2WAdw8CgYEAwGIkXz5HJlbxAudKQPDq5nA+YXP4ov1w7HMnar3/DRPdvwB54JvMXBf2o4Gq+QmKI96NeVUgUbrwoFoc3Dpqt6AiXSZTvxU4ST2uO/HOJUuDPggo4swxawg/l9ihXG5n8apxqNes3vOj7ZtbnZaDlCBjb1TZsf4l/Da1YbDXLZ4=\n-----END PRIVATE KEY-----";

    public static String IOSPass = "gvn12345";
    public static String IOSPass_enterprise = "gvn12345";

    public static int JPNSPort = 3221;
    public static boolean production = true;

    public static int RunnerNumber = 5;
    public static String AWS_ACCESS_KEY = "AKIAI2EPTFG6D6GLUG4Q";
    public static String AWS_SECRET_KEY = "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";

    public static String GCM_DEVELOPMENT_KEY = "AIzaSyC9qz9W7qDJ4ijPYUKgKw1OFOiINDDtFmY";
    public static String GCM_PRODUCTION_KEY = "AIzaSyC9qz9W7qDJ4ijPYUKgKw1OFOiINDDtFmY";

    public static String GCM_DEVELOPMENT_KEYNEW = "AIzaSyDDqMeLS9BbD12pYVy4pLGqDsYV_v7KFYg";
    public static String GCM_PRODUCTION_KEYNEW = "AIzaSyDDqMeLS9BbD12pYVy4pLGqDsYV_v7KFYg";
    
    public static String NTS_FCM = "AIzaSyDDqMeLS9BbD12pYVy4pLGqDsYV_v7KFYg";
    
    public static String STREAMING_HOST = "http://210.148.155.5:81/";

    public static void initConfig() {
        try {
            FileInputStream fis = new FileInputStream("Config.properties");
            Properties prop = new Properties();
            prop.load(fis);

            IOSCerPath = prop.getProperty("IOSCerPath").trim();
            IOSCerPath_enterprise = prop.getProperty("IOSCerPath_enterprise").trim();
            IOSPass = prop.getProperty("IOSPass").trim();
            IOSPass_enterprise = prop.getProperty("IOSPass_enterprise").trim();
            JPNSPort = Integer.parseInt(prop.getProperty("JPNSPort").trim());
            production = Boolean.parseBoolean(prop.getProperty("production").trim());
            RunnerNumber = Integer.parseInt(prop.getProperty("RunnerNumber").trim());
            AWS_ACCESS_KEY = prop.getProperty("AWS_ACCESS_KEY").trim();
            AWS_SECRET_KEY = prop.getProperty("AWS_SECRET_KEY").trim();
            IOS_CERTIFICATE_KEY = prop.getProperty("IOS_CERTIFICATE_KEY").trim();
            IOS_PRIVATE_KEY = prop.getProperty("IOS_PRIVATE_KEY").trim();
            IOS_ENTERPRISE_CERTIFICATE_KEY = prop.getProperty("IOS_ENTERPRISE_CERTIFICATE_KEY").trim();
            IOS_ENTERPRISE_PRIVATE_KEY = prop.getProperty("IOS_ENTERPRISE_PRIVATE_KEY").trim();
            IOS_ENTERPRISE_PRIVATE_KEY = prop.getProperty("IOS_ENTERPRISE_PRIVATE_KEY").trim();
            NTS_FCM = prop.getProperty("NTS_FCM").trim();
            STREAMING_HOST = prop.getProperty("STREAMING_HOST").trim();         
        } catch (Exception ex) {
            Util.addErrorLog(ex);

        }
    }

}
