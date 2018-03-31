/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.vn.ntsc.backend.entity.impl.setting;

import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import eazycommon.constant.ErrorCode;
import eazycommon.util.Util;
import org.json.simple.JSONObject;
import com.vn.ntsc.backend.entity.IEntity;

/**
 *
 * @author RuAc0n
 */
public class VersionSetting implements IEntity{
    
    private static final String iosEnterpriseVersionKey = "ios_enterprise_usable_version";
    public String iosEnterpriseVersion; 
    
    private static final String iosNonEnterpriseVersionKey = "ios_non_enterprise_usable_version";
    public String iosNonEnterpriseVersion;  
    
    private static final String androidUsableVersionKey = "android_usable_version";
    public String androidUsableVersion;  
    
    private static final Map<String, Integer> keys = new TreeMap<>();
    static{
        keys.put(androidUsableVersionKey, ErrorCode.WRONG_DATA_FORMAT);
        keys.put(iosEnterpriseVersionKey, ErrorCode.WRONG_DATA_FORMAT);
        keys.put(iosNonEnterpriseVersionKey, ErrorCode.WRONG_DATA_FORMAT);
    }        
    
    @Override
    public JSONObject toJsonObject(){
        JSONObject jo = new JSONObject();
        jo.put(androidUsableVersionKey, this.androidUsableVersion);
        jo.put(iosEnterpriseVersionKey, this.iosEnterpriseVersion);
        jo.put(iosNonEnterpriseVersionKey, this.iosNonEnterpriseVersion);
        
        return jo;
    }
    
    private static final String REGEX = "^(([0-9a-z.])+)$";
    private static final Pattern pattern = Pattern.compile(REGEX);
    public static int validate(JSONObject obj){
        for(Map.Entry<String, Integer> pair : keys.entrySet()){
            try{
                String key = pair.getKey();
                String str = Util.getStringParam(obj, key);
                if(str != null && !str.isEmpty() && !validate(str))
                    return pair.getValue();
            }catch(Exception ex){
                return pair.getValue();
            }
        }
        return 0;
    }    
    
    private static boolean validate(String string) {
        Matcher matcher = pattern.matcher(string);
        return matcher.matches();
    }    
    
    public static VersionSetting createSetting (JSONObject obj){
        VersionSetting os = new VersionSetting();
        
        String androidUsableVersion = Util.getStringParam(obj, androidUsableVersionKey);
        os.androidUsableVersion = androidUsableVersion;  
        System.out.println(os.androidUsableVersion);
        
        String iosEnterpriseUsableVersion = (String) obj.get(iosEnterpriseVersionKey);
        os.iosEnterpriseVersion = iosEnterpriseUsableVersion; 
        System.out.println(os.iosEnterpriseVersion);
        
        String iosNonEnterpriseUsableVersion = (String) obj.get(iosNonEnterpriseVersionKey);
        os.iosNonEnterpriseVersion = iosNonEnterpriseUsableVersion; 
        System.out.println(os.iosNonEnterpriseVersion);
        
        return os;
    }
}
