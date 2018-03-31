/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.vn.ntsc.eazyserver.adapter.impl;

import eazycommon.constant.ErrorCode;
import eazycommon.constant.ParamKey;
import eazycommon.constant.ResponseMessage;
import eazycommon.util.Util;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import com.vn.ntsc.Config;
import com.vn.ntsc.eazyserver.adapter.IServiceAdapter;
import com.vn.ntsc.eazyserver.adapter.impl.util.InterCommunicator;
import com.vn.ntsc.eazyserver.server.request.Request;
import com.vn.ntsc.otherservice.entity.impl.FileUrl;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.json.simple.JSONArray;

/**
 *
 * @author RuAc0n
 */
public class BlockUserAdapter implements IServiceAdapter {

    public static final ListBlock listBlock = new ListBlock();

    static class ListBlock implements IServiceAdapter{

        @Override
        public String callService(Request request) {
            try{
                String umsResult =  InterCommunicator.sendRequest(request.toJson(), Config.UMSServerIP, Config.UMSPort);
                JSONObject json = (JSONObject) new JSONParser().parse(umsResult);
                Long code = (Long) json.get(ParamKey.ERROR_CODE);
                if(code == ErrorCode.SUCCESS){
                    JSONArray arr = (JSONArray) json.get(ParamKey.DATA);
                    List<String> listAvaId = new ArrayList<>();
                    for (Object obj : arr){
                        JSONObject user = (JSONObject) obj;
                        String avaId = (String) user.get(ParamKey.AVATAR_ID);
                        if (avaId != null && !avaId.isEmpty()){
                            listAvaId.add(avaId);
                        }
                        user.put(ParamKey.AVATAR, "");
                    }
                    HashMap<String, FileUrl> mapUrl = InterCommunicator.getImage(listAvaId);
                    for (Object obj : arr){
                        JSONObject user = (JSONObject) obj;
                        String avaId = (String) user.get(ParamKey.AVATAR_ID);
                        if (avaId != null && !avaId.isEmpty()){
                            FileUrl url = mapUrl.get(avaId);
                            if (url != null){
                                user.put(ParamKey.THUMBNAIL_URL, url.getThumbnail());
                                user.put(ParamKey.ORIGINAL_URL, url.getOriginalUrl());
                            }
                        }
                    }
                }
                return json.toJSONString();
            }catch(Exception ex){
                Util.addErrorLog(ex);
                return ResponseMessage.UnknownError;
            }
        }
    }
    @Override
    public String callService(Request request) {
        try {
            String umsString = InterCommunicator.sendRequest(request.toJson(), Config.UMSServerIP, Config.UMSPort);
            JSONObject umsJson = (JSONObject) new JSONParser().parse(umsString);
            Long code = (Long) umsJson.get(ParamKey.ERROR_CODE);
            if(code == ErrorCode.SUCCESS){
                InterCommunicator.sendRequest(request.toJson(), Config.ChatServerIP, Config.ChatServerPort);
            }
            return umsString;
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
        return ResponseMessage.UnknownError;
    }

}
