/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.vn.ntsc.eazyserver.adapter.impl;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.ParamKey;
import eazycommon.constant.mongokey.SettingdbKey;
import eazycommon.exception.EazyException;
import eazycommon.util.Util;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.simple.JSONObject;
import com.vn.ntsc.Setting;
import com.vn.ntsc.eazyserver.adapter.IServiceAdapter;
import com.vn.ntsc.eazyserver.server.request.Request;
import com.vn.ntsc.dao.impl.OtherSettingAppDAO;

/**
 *
 * @author RuAc0n
 */
public class GetInformationForApplicationAdapter implements IServiceAdapter {

    @Override
    public String callService(Request request) {
        Util.addDebugLog("=================================== GET INFO FOR APP" + request.toString());
        JSONObject obj = new JSONObject();
        obj.put(ParamKey.ERROR_CODE, ErrorCode.SUCCESS);
        JSONObject data = new JSONObject();
//        data.put( "switch_safari", Setting.turnOffSafary );
        data.put( "switch_safari", true );
//        data.put( "switch_safari_version", Setting.turnOffSafaryVersion );
        data.put( "switch_safari_version", "0.0" );
        data.put( "login_by_mocom", Setting.turnOffLoginByMocom );
        data.put( "turn_off_user_info", Setting.turnOffExtendedUserInfo );
        data.put( "turn_off_show_news", Setting.turnOffShowNews );
        data.put( "get_free_point", Setting.turnOffGetFreePoint );
        
        data.put( "switch_browser_android", Setting.turnOffBrowserAndroid );
        data.put( "switch_browser_android_version", Setting.turnOffBrowserAndroidVersion );
        data.put( "login_by_mocom_android", Setting.turnOffLoginByMocomAndroid );
        data.put( "turn_off_user_info_android", Setting.turnOffExtendedUserInfoAndroid );
        data.put( "turn_off_show_news_android", Setting.turnOffShowNewsAndroid );
        data.put( "get_free_point_android", Setting.turnOffGetFreePointAndroid );
//        data.put( "switch_safari_for_enterprise", Setting.enterpriseTurnOffSafary );
//        data.put( "switch_safari_version_for_enterprise", Setting.enterpriseturnOffSafaryVersion );
//        data.put( "login_by_mocom_for_enterprise", Setting.enterpriseTurnOffLoginByMocom );

        Long applicationId = (Long) request.getParamValue("application");
        Util.addDebugLog("=======Main GetInformationForApplicationAdapter applicationId:"+applicationId);
        DBObject objDB = new BasicDBObject();
        try {
            objDB = OtherSettingAppDAO.getOtherSettingApp(applicationId);
            Boolean force =true;
            String urlWeb ="";
            String appVer ="";
            if (objDB != null){
                force = (Boolean) objDB.get(SettingdbKey.OTHER_SETTING_APP.FORCE_UPDATING);
                urlWeb = (String) objDB.get(SettingdbKey.OTHER_SETTING_APP.URL_WEB);
                appVer = (String) objDB.get(SettingdbKey.OTHER_SETTING_APP.APP_VERSION);
                data.put(SettingdbKey.OTHER_SETTING_APP.FORCE_UPDATING,force);
                data.put(SettingdbKey.OTHER_SETTING_APP.URL_WEB,urlWeb);
                data.put(SettingdbKey.OTHER_SETTING_APP.APP_VERSION,appVer);
            }
        } catch (EazyException ex) {
            Logger.getLogger(GetInformationForApplicationAdapter.class.getName()).log(Level.SEVERE, null, ex);
        }

        obj.put(ParamKey.DATA, data);
        Util.addDebugLog("=================================== GET INFO FOR APP" + data.toString());
        return obj.toJSONString();
    }

}
