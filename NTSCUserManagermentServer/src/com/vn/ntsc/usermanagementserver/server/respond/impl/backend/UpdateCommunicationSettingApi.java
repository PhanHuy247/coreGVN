/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.usermanagementserver.server.respond.impl.backend;

import com.vn.ntsc.usermanagementserver.server.respond.Respond;
import com.vn.ntsc.usermanagementserver.server.respond.IApiAdapter;
import eazycommon.util.Util;
import java.util.Date;
import eazycommon.constant.ErrorCode;
import org.json.simple.JSONObject;
import eazycommon.constant.mongokey.DAOKeys;
import com.vn.ntsc.usermanagementserver.server.pointaction.ActionManager;

/**
 *
 * @author RuAc0n
 */
public class UpdateCommunicationSettingApi implements IApiAdapter {

    @Override
    public Respond execute(JSONObject obj, Date time) {
        Respond result = new Respond();
        try {
            JSONObject videoCall = (JSONObject) obj.get(DAOKeys.video_call);
            JSONObject voiceCall = (JSONObject) obj.get(DAOKeys.voice_call);
            
            ActionManager.communicationPointSetting.put(DAOKeys.video_call, videoCall);
            ActionManager.communicationPointSetting.put(DAOKeys.voice_call, voiceCall);
            result.code = ErrorCode.SUCCESS;
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
        return result;
    }

}
