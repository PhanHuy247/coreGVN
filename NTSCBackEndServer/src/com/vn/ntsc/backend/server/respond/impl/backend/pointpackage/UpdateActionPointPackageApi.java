/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.backend.server.respond.impl.backend.pointpackage;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.ParamKey;
import eazycommon.exception.EazyException;
import eazycommon.util.Util;
import org.json.simple.JSONObject;
import com.vn.ntsc.backend.dao.pointpackage.ActionPointPacketDAO;
import com.vn.ntsc.backend.entity.impl.pointpackage.ActionPointPacket;
import com.vn.ntsc.backend.server.common.Validator;
import com.vn.ntsc.backend.server.respond.IApiAdapter;
import com.vn.ntsc.backend.server.respond.Respond;
import com.vn.ntsc.backend.server.respond.result.EntityRespond;

/**
 *
 * @author RuAc0n
 */
public class UpdateActionPointPackageApi implements IApiAdapter {
    private static final Map<String, Integer> keys = new TreeMap<>();

    static {
        keys.put(ParamKey.PRODUCTION_ID, 4);
        keys.put(ParamKey.PRICE, 5);
        keys.put(ParamKey.POINT, 6);
//        keys.put(ParamKey.DESCRIPTION, 7);
//        keys.put("use_chat", 8);
//        keys.put("use_video_call", 9);
//        keys.put("use_voice_call", 10);
//        keys.put("use_gift", 11);
//        keys.put("use_comment", 12);
//        keys.put("use_sub_comment", 13);
//        keys.put("use_voice_call", 14);
//        keys.put("chat_text", 15);
//        keys.put("video_call_text", 16);
//        keys.put("voice_call_text", 17);
//        keys.put("gift_text", 18);
//        keys.put("comment_text", 19);
//        keys.put("sub_comment_text", 20);
    }
    @Override
    public Respond execute(JSONObject obj) {
        Respond respond = new Respond();
        try {
            String id = Util.getStringParam(obj, ParamKey.ID);
            if (id == null || id.isEmpty()) {
                return null;
            }
//            String des = Util.getStringParam(obj, ParamKey.DESCRIPTION);
            String productionId = Util.getStringParam(obj, ParamKey.PRODUCTION_ID);
            Long point = Util.getLongParam(obj, ParamKey.POINT);
            String first_purchase_description = Util.getStringParam(obj, "first_purchase_description");
            Long first_purchase_point = Util.getLongParam(obj, "first_purchase_point");
            Double price = Util.getDoubleParam(obj, ParamKey.PRICE);
            String videoCallText = Util.getStringParam(obj, "video_call_text");
            String voiceCallText = Util.getStringParam(obj, "voice_call_text");
            String chatText = Util.getStringParam(obj, "chat_text");
            String commentText = Util.getStringParam(obj, "comment_text");
            String subCommentText = Util.getStringParam(obj, "sub_comment_text");
            String giftText = Util.getStringParam(obj, "gift_text");
            String unlockBackstageText = Util.getStringParam(obj, "unlock_backstage_text");
            String saveImageText = Util.getStringParam(obj, "save_image_text");
            String otherText = Util.getStringParam(obj, "other_text");
            Boolean useChat = (Boolean) obj.get("use_chat");
            Boolean useVideoCall = (Boolean) obj.get("use_video_call");
            Boolean useVoiceCall = (Boolean) obj.get("use_voice_call");
            Boolean useGift = (Boolean) obj.get("use_gift");
            Boolean useComment = (Boolean) obj.get("use_comment");
            Boolean useSubComment = (Boolean) obj.get("use_sub_comment");
            Boolean useUnlockBackstage = (Boolean) obj.get("use_unlock_backstage");
            Boolean useSaveImage = (Boolean) obj.get("use_save_image");
            Boolean useOther = (Boolean) obj.get("use_other");
            
            
            
            int error = Validator.validate(obj, keys);
            if (error != ErrorCode.SUCCESS) {
                respond = new EntityRespond(error);
                return respond;
            }
            
            if(point <= 0){
                respond = new EntityRespond(keys.get(ParamKey.POINT));
                return respond;
            }
            
            if(price <= 0){
                respond = new EntityRespond(keys.get(ParamKey.PRICE));
                return respond;
            }
            
            List<ActionPointPacket> packets = ActionPointPacketDAO.getPointPacketByProductId(productionId);
            if(packets != null && packets.size() > 0){
                if(packets.size() > 2){
                    respond = new EntityRespond(4);
                    return respond; 
                }else{
                    ActionPointPacket packet = packets.get(0);
                    if(!packet.id.equals(id)){
                        respond = new EntityRespond(4);
                        return respond; 
                    }
                }
            }
            
            ActionPointPacket actionPointPacket = new ActionPointPacket();
            actionPointPacket.chatText = chatText;
            actionPointPacket.videoCallText = videoCallText;
            actionPointPacket.voiceCallText = voiceCallText;
            actionPointPacket.giftText = giftText;
            actionPointPacket.subCommentText = subCommentText;
            actionPointPacket.commentText = commentText;
            actionPointPacket.unlockBackstageText = unlockBackstageText;
            actionPointPacket.saveImageText = saveImageText;
            actionPointPacket.otherText = otherText;
            actionPointPacket.useChat = useChat;
            actionPointPacket.useVideoCall = useVideoCall;
            actionPointPacket.useVoiceCall = useVoiceCall;
            actionPointPacket.useGift = useGift;
            actionPointPacket.useComment = useComment;
            actionPointPacket.useSubComment = useSubComment;
            actionPointPacket.useUnlockBackstage = useUnlockBackstage;
            actionPointPacket.useSaveImage = useSaveImage;
            actionPointPacket.useOther = useOther;
            actionPointPacket.point = point.intValue();
            actionPointPacket.price = price;
            actionPointPacket.productId = productionId;
            actionPointPacket.firstPurchasePoint = first_purchase_point.intValue();
            actionPointPacket.firstPurchaseDescription = first_purchase_description;
//            actionPointPacket.description = des;
            
            error =  validate(actionPointPacket);
            if( error != ErrorCode.SUCCESS){
                respond = new EntityRespond(error);
                return respond;
            }
            
            ActionPointPacketDAO.update(id, actionPointPacket);
            respond = new Respond(ErrorCode.SUCCESS);
        } catch (EazyException ex) {
            Util.addErrorLog(ex);
            respond = new Respond(ex.getErrorCode());
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
        return respond;
    }

    private static int validate(ActionPointPacket  actionPointPacket){
        if(actionPointPacket.useChat){
            if(actionPointPacket.chatText == null || actionPointPacket.chatText.isEmpty()){
                return 8;
            }
        }
        if(actionPointPacket.useVideoCall){
            if(actionPointPacket.videoCallText == null || actionPointPacket.videoCallText.isEmpty()){
                return 9;
            }
        }
        if(actionPointPacket.useVoiceCall){
            if(actionPointPacket.voiceCallText == null || actionPointPacket.voiceCallText.isEmpty()){
                return 10;
            }
        }
        if(actionPointPacket.useGift){
            if(actionPointPacket.giftText == null || actionPointPacket.giftText.isEmpty()){
                return 11;
            }
        }
        if(actionPointPacket.useComment){
            if(actionPointPacket.commentText == null || actionPointPacket.commentText.isEmpty()){
                return 12;
            }
        }
        if(actionPointPacket.useSubComment){
            if(actionPointPacket.subCommentText == null || actionPointPacket.subCommentText.isEmpty()){
                return 13;
            }
        }
        if (actionPointPacket.useUnlockBackstage){
            if (actionPointPacket.unlockBackstageText == null || actionPointPacket.unlockBackstageText.isEmpty()){
                return 14;
            }
        }
        
        if (actionPointPacket.useSaveImage){
            if (actionPointPacket.saveImageText == null || actionPointPacket.saveImageText.isEmpty()){
                return 15;
            }
        }
        if (actionPointPacket.useOther){
            if (actionPointPacket.otherText == null || actionPointPacket.otherText.isEmpty()){
                return 16;
            }
        } 
        return ErrorCode.SUCCESS;
    }    
    
}
