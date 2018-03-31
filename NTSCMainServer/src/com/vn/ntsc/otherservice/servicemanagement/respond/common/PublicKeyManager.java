/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.otherservice.servicemanagement.respond.common;

import eazycommon.util.Util;
import java.util.HashMap;
import java.util.Map;
import com.vn.ntsc.Config;


/**
 *
 * @author HUNGDT
 */
public class PublicKeyManager {

    private static final Map<String, String> map = new HashMap<>();

    public static void init() {
        String meets1 = "Meets";
        map.put(meets1, Config.PUBLIC_KEY1);
        String meets2 = "Betty";
        map.put(meets2, Config.PUBLIC_KEY2);
        Util.addDebugLog("PublicKeyManager ============" + map.get(meets1));
        String meets3 = "Bisser";
        map.put(meets3, Config.PUBLIC_KEY3);
        String meets4 = "meets4";
        map.put(meets4, Config.PUBLIC_KEY2);
        String meets5 = "meets5";
        map.put(meets5, Config.PUBLIC_KEY2);
        String meets6 = "meets6";
        map.put(meets6, Config.PUBLIC_KEY2);
        String meets7 = "meets7";
        map.put(meets7, Config.PUBLIC_KEY2);
        String meets8 = "meets8";
        map.put(meets8, Config.PUBLIC_KEY2);
        String meets9 = "meets9";
        map.put(meets9, Config.PUBLIC_KEY2);
        String meets10 = "meets10";
        map.put(meets10, Config.PUBLIC_KEY2);
        String meets11 = "meets11";
        map.put(meets11, Config.PUBLIC_KEY2);
        String meets12 = "meets12";
        map.put(meets12, Config.PUBLIC_KEY2);
        String meets13 = "meets13";
        map.put(meets13, Config.PUBLIC_KEY2);
        String meets14 = "meets14";
        map.put(meets14, Config.PUBLIC_KEY2);
        String meets15 = "meets15";
        map.put(meets15, Config.PUBLIC_KEY2);
        String meets16 = "meets16";
        map.put(meets16, Config.PUBLIC_KEY2);
        String meets17 = "meets17";
        map.put(meets17, Config.PUBLIC_KEY2);
        String meets18 = "meets18";
        map.put(meets18, Config.PUBLIC_KEY2);
        String meets19 = "meets19";
        map.put(meets19, Config.PUBLIC_KEY2);
        String meets20 = "meets20";
        map.put(meets20, Config.PUBLIC_KEY2);
        
        
    }

    public static String getPublicKey(String applicationName) {
        return map.get(applicationName);
    }

}
