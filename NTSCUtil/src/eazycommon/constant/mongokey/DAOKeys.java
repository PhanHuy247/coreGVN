/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eazycommon.constant.mongokey;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author RuAc0n
 */
public class DAOKeys {

    public static final String MALE_REQUEST_POINT = "male_req_point";
    public static final String FEMALE_REQUEST_POINT = "female_req_point";
    public static final String MALE_PARTNER_POINT = "male_partner_point";
    public static final String FEMALE_PARTNER_POINT = "female_partner_point"; 
    
    public static final String POTENTIAL_CUSTOMER_MALE_REQUEST_POINT = "potential_male_customer_req_point";
    public static final String POTENTIAL_CUSTOMER_FEMALE_REQUEST_POINT = "potential_female_customer_req_point";
    public static final String POTENTIAL_CUSTOMER_MALE_PARTNER_POINT = "potential_male_customer_partner_point";
    public static final String POTENTIAL_CUSTOMER_FEMALE_PARTNER_POINT = "potential_female_customer_partner_point"; 
    
    public static final String register = "register";
    public static final String daily_bonus = "daily_bonus";
    public static final String unlock_backstage = "unlock_backstage";
    public static final String save_image = "save_image";
    public static final String view_image = "view_image";
    public static final String listen_audio = "listen_audio";
    public static final String watch_video = "watch_video";
    public static final String reply_comment = "reply_comment";
    public static final String online_alert = "online_alert";
    public static final String comment_buzz = "comment_buzz";
    public static  final String receive_gift = "receive_gift";

    public static final String voice_call = "voice_call";
    public static final String video_call = "video_call";
    public static final String chat = "chat";
    public static final String wink = "wink";
    public static final String advertsement = "advertsement";

    public static final String voice_call_type = "voice_call";
    public static final String video_call_type = "video_call";

    public static final String male_male = "male_male";
    public static final String male_female = "male_female";
    public static final String female_male = "female_male";
    public static final String female_female = "female_female";

    public static final List<String> one_object_add_list = new ArrayList<>();
    public static final List<String> one_object_minus_list = new ArrayList<>();
//    public static final List<String> two_object_list = new ArrayList();

    public static final List<String> communication_type_list = new ArrayList<>();
    public static final List<String> connection_type_list = new ArrayList<>();
    public static final List<String> pairs_list = new ArrayList<>();

    
    public static final String name = "name";
    public static final String value = "value";     
    
    static {
        one_object_add_list.add(daily_bonus);
        one_object_add_list.add(advertsement);
        one_object_add_list.add(register);
        one_object_add_list.add(receive_gift); 

        one_object_minus_list.add(online_alert);

//        connection_type_list.add(wink);
        connection_type_list.add(unlock_backstage);
        connection_type_list.add(comment_buzz);
        connection_type_list.add(save_image);
        connection_type_list.add(chat);
        connection_type_list.add(view_image);
        connection_type_list.add(watch_video);
        connection_type_list.add(listen_audio);
        connection_type_list.add(reply_comment);


        communication_type_list.add(voice_call_type);
        communication_type_list.add(video_call_type);

        pairs_list.add(male_male);
        pairs_list.add(male_female);
        pairs_list.add(female_male);
        pairs_list.add(female_female);

    }

}
