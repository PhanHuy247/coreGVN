/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.vn.ntsc.usermanagementserver.server.pointaction;

import java.util.Map;
import java.util.TreeMap;

/**
 *
 * @author RuAc0n
 */
public class LogValue {
    
    private static final Map<ActionType, Integer> m = new TreeMap<>();
    
    static{
        m.put(ActionType.register, 1);
        m.put(ActionType.daily_bonus, 2);
        m.put(ActionType.cash, 4);
        m.put(ActionType.unlock_backstage_bonus, 5);
        m.put(ActionType.unlock_backstage, 8);
        m.put(ActionType.save_image, 9);
        m.put(ActionType.send_gift, 11);
        m.put(ActionType.online_alert, 12);
        m.put(ActionType.admistrator, 14);
        m.put(ActionType.voice_call, 15);
        m.put(ActionType.video_call, 16);
        m.put(ActionType.chat, 17);
        m.put(ActionType.buy_sticker, 18);
        m.put(ActionType.wink, 19);
//        m.put(ActionType.view_image, 20);
//        m.put(ActionType.view_image_bonus, 21);
        m.put(ActionType.receive_gift, 22);
//        m.put(ActionType.send_sticker, 23);
//        m.put(ActionType.receive_sticker, 24);
        m.put(ActionType.save_image_bonus, 25);
        m.put(ActionType.advertsement, 26);
        m.put(ActionType.receive_wink, 36);
        m.put(ActionType.receive_chat, 37);
        m.put(ActionType.trade_point_to_money, 38);
        m.put(ActionType.comment_buzz, 39);
        m.put(ActionType.comment_bonus, 40);
        m.put(ActionType.free_point, 41);
        m.put(ActionType.add_sale, 42);
        m.put(ActionType.view_image, 43);
        m.put(ActionType.view_image_bonus, 44);
        m.put(ActionType.watch_video, 45);
        m.put(ActionType.watch_video_bonus, 46);
        m.put(ActionType.listen_audio, 47);
        m.put(ActionType.listen_audio_bonus, 48);
        m.put(ActionType.add_point_by_administrator_in_list, 49);
        m.put(ActionType.reply_comment, 50);
        m.put(ActionType.reply_comment_bonus, 51);
        m.put(ActionType.return_failed_upload_point, 52);
        m.put(ActionType.refund_comment, 53);
        m.put(ActionType.refund_reply_comment, 54);
    }

    public static int getValue(ActionType type){
        return m.get(type);
    }
    
    public static ActionType getKey(int value){
        ActionType resutl = null;
        for(Map.Entry<ActionType, Integer> pairs: m.entrySet()){
            Integer val = pairs.getValue();
            if(val == value){
                resutl = pairs.getKey();
                break;
            }
        }
        return resutl;
    }
}
