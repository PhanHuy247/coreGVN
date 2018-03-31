/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.vn.ntsc.backend.entity.impl.pointpackage;

import org.json.simple.JSONObject;

/**
 *
 * @author RuAc0n
 */
public class ActionPointPacket extends PointPacket {
    
    private static final String useChatKey = "use_chat";
    public Boolean useChat;
    
    private static final String chatTextKey = "chat_text";
    public String chatText;
    
    private static final String useVideoCallKey = "use_video_call";
    public Boolean useVideoCall;
    
    private static final String videoCallTextKey = "video_call_text";
    public String videoCallText;
    
    private static final String useVoiceCallKey = "use_voice_call";
    public Boolean useVoiceCall;
    
    private static final String voiceCallTextKey = "voice_call_text";
    public String voiceCallText;
    
    private static final String useCommentKey = "use_comment";
    public Boolean useComment;
    
    private static final String commentTextKey = "comment_text";
    public String commentText;
    
    private static final String useSubCommentKey = "use_sub_comment";
    public Boolean useSubComment;
    
    private static final String subCommentTextKey = "sub_comment_text";
    public String subCommentText;
    
    private static final String useGiftKey = "use_gift";
    public Boolean useGift;
    
    private static final String giftTextKey = "gift_text";
    public String giftText;
    
    private static final String useUnlockBackstageKey="use_unlock_backstage";
    public Boolean useUnlockBackstage;
    
    private static final String unlockBackstageTextKey = "unlock_backstage_text";
    public String unlockBackstageText;
    
    private static final String useSaveImageKey = "use_save_image";
    public Boolean useSaveImage;
    
    private static final String saveImageTextKey = "save_image_text";
    public String saveImageText;

    private static final String useOtherKey = "use_other";
    public Boolean useOther;
    
    private static final String otherTextKey = "other_text";
    public String otherText; 
    
    @Override
    public JSONObject toJsonObject(){
        JSONObject jo = new JSONObject();

        if(this.id != null)
            jo.put(idKey, this.id);
        if(this.price != null)
            jo.put(priceKey, this.price);
        if(this.point != null)
            jo.put(pointKey, this.point);
        if (this.firstPurchasePoint!=null)
            jo.put(firstPurchasePointKey, firstPurchasePoint);
        if (this.firstPurchaseDescription!=null)
            jo.put(firstPurchaseDescriptionKey, firstPurchaseDescription);
//        if(this.description != null)
//            jo.put(desKey, this.description);
        if(this.productId != null)
            jo.put(productIdKey, this.productId);
        if(this.useChat != null)
            jo.put(useChatKey, this.useChat);
        if(this.chatText != null)
            jo.put(chatTextKey, this.chatText);
        if(this.useVideoCall != null)
            jo.put(useVideoCallKey, this.useVideoCall);
        if(this.videoCallText != null)
            jo.put(videoCallTextKey, this.videoCallText);
        if(this.useVoiceCall!= null)
            jo.put(useVoiceCallKey, this.useVoiceCall);
        if(this.voiceCallText != null)
            jo.put(voiceCallTextKey, this.voiceCallText);
        if(this.useGift!= null)
            jo.put(useGiftKey, this.useGift);
        if(this.giftText != null)
            jo.put(giftTextKey, this.giftText);
        if(this.useSubComment!= null)
            jo.put(useSubCommentKey, this.useSubComment);
        if(this.subCommentText != null)
            jo.put(subCommentTextKey, this.subCommentText);
        if(this.useComment!= null)
            jo.put(useCommentKey, this.useComment);
        if(this.commentText != null)
            jo.put(commentTextKey, this.commentText);
        if (this.useUnlockBackstage!=null)
            jo.put(useUnlockBackstageKey, this.useUnlockBackstage);
        if (this.unlockBackstageText!=null)
            jo.put(unlockBackstageTextKey, this.unlockBackstageText);
        if (this.useSaveImage!=null)
            jo.put(useSaveImageKey, this.useSaveImage);
        if (this.saveImageText!=null)
            jo.put(saveImageTextKey, this.saveImageText);
        if (this.useOther!=null)
            jo.put(useOtherKey, this.useOther);
        if (this.otherText!=null)
            jo.put(otherTextKey, this.otherText);
        
        return jo;
    }
}
