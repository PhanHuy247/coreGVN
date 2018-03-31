/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eazycommon.constant;

import org.json.simple.JSONObject;

/**
 *
 * @author Rua
 */
public class ResponseMessage {

    public static final String BadResquestMessage;

    static {
        JSONObject obj = new JSONObject();
        obj.put(ParamKey.ERROR_CODE, ErrorCode.WRONG_DATA_FORMAT);
        BadResquestMessage = obj.toJSONString();
    }
    
    public static final String NeedToGetSettingMessage;

    static {
        JSONObject obj = new JSONObject();
        obj.put(ParamKey.ERROR_CODE, ErrorCode.CHANGE_SETTING_TOKEN);
        NeedToGetSettingMessage = obj.toJSONString();
    }

    public static final String InvailidTokenMessage;

    static {
        JSONObject obj = new JSONObject();
        obj.put(ParamKey.ERROR_CODE, ErrorCode.INVALID_TOKEN);
        InvailidTokenMessage = obj.toJSONString();
    }
    
    public static final String TOKENNOTEXISTDB;

    static {
        JSONObject obj = new JSONObject();
        obj.put(ParamKey.ERROR_CODE, ErrorCode.TOKEN_NOT_EXIST_DB);
        TOKENNOTEXISTDB = obj.toJSONString();
    }

    public static final String UnknownError;

    static {
        JSONObject obj = new JSONObject();
        obj.put(ParamKey.ERROR_CODE, ErrorCode.UNKNOWN_ERROR);
        UnknownError = obj.toJSONString();
    }

    public static final String PermissionDenied;

    static {
        JSONObject obj = new JSONObject();
        obj.put(ParamKey.ERROR_CODE, ErrorCode.PERMISSION_DENIED);
        PermissionDenied = obj.toJSONString();
    }

    public static final String SuccessMessage;

    static {
        JSONObject obj = new JSONObject();
        obj.put(ParamKey.ERROR_CODE, ErrorCode.SUCCESS);
        SuccessMessage = obj.toJSONString();
    }
}
