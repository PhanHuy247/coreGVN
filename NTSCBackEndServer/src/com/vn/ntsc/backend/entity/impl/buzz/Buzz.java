/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.backend.entity.impl.buzz;

import java.util.ArrayList;
import java.util.List;
import eazycommon.constant.ParamKey;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import com.vn.ntsc.backend.entity.IEntity;
import com.vn.ntsc.backend.entity.OneObjectLog;
import com.vn.ntsc.backend.server.respond.impl.csv.HeaderCreator;
import com.vn.ntsc.backend.server.respond.impl.csv.Headers;
import com.vn.ntsc.backend.server.respond.impl.csv.TypeSwitch;
import com.vn.ntsc.backend.server.respond.impl.csv.TypeValue;

/**
 *
 * @author DuongLTD
 */
public class Buzz extends OneObjectLog implements Comparable<Buzz>, IEntity {

    private static final List<String> headers = new ArrayList<String>();
    private static final List<String> japaneseHeader = new ArrayList<String>();
    private static final List<String> englishHeader = new ArrayList<String>();
    private static final JSONObject jsonEnglishType = new JSONObject();
    private static final JSONObject jsonJapaneseType = new JSONObject();    
    
    private static final String buzzIdKey = "buzz_id";
    @TypeSwitch (header = Headers.buzz_id)      
    public String buzzId;    
    
    private static final String buzzTypeKey = "buzz_type";
    @TypeSwitch (value = ParamKey.BUZZ_TYPE, header = Headers.buzz_type)
    public Integer buzzType;    
    
    private static final String buzzValKey = "buzz_val";
    @TypeSwitch ( header = Headers.content_buzz)    
    public String buzzVal;    
    
    private static final String likeNumKey = "like_num";
    @TypeSwitch ( header = Headers.like_number)    
    public Integer likeNum;    
    
    private static final String cmtNumKey = "cmt_num";
    @TypeSwitch (header = Headers.commnent_number)    
    public Integer cmtNum;    
    
    private static final String buzzTimeKey = "buzz_time";
    @TypeSwitch (value = ParamKey.TIME, header = Headers.buzz_time)    
    public String buzzTime;

    private static final String seenNumKey = "seen_num";
    @TypeSwitch (value = ParamKey.NEXT, header = ParamKey.NEXT)    
    public Integer seenNum;

    private static final String commentKey = "comment";
    @TypeSwitch (value = ParamKey.NEXT, header = ParamKey.NEXT)    
    public List<Comment> comment = new ArrayList<>();  
    //Thanhdd add #5214
    private static final String buzzStatusKey = "buzz_status";
    @TypeSwitch (value = ParamKey.BUZZ_STATUS, header = Headers.buzz_status)
    public Integer buzzStatus; 
    
    //Linh add
    private static final String isDeletedKey = "is_deleted";
    public Integer isDeleted = 0;
    
    // namhv
    private static final String isDenyUserKey = "user_deny";
    public String isDenyUser;
    private static final String isDenyUserNameKey = "user_deny_name";
    public String isDenyUserName;
    static{
        initHeader();
        initType();
    }
   
    private static void initType(){
        
//        jsonEnglishType = new JSONObject();
//        jsonJapaneseType = new JSONObject();
        
        JSONObject value = new JSONObject();
        //user type
        value.putAll(TypeValue.en_user_type);
        jsonEnglishType.put(ParamKey.USER_TYPE, value);
        value = new JSONObject();
        value.putAll(TypeValue.jp_user_type);
        jsonJapaneseType.put(ParamKey.USER_TYPE, value);
        
        //buzz type
        value = new JSONObject();
        value.putAll(TypeValue.en_buzz_type);
        jsonEnglishType.put(ParamKey.BUZZ_TYPE, value);
        value = new JSONObject();
        value.putAll(TypeValue.jp_buzz_type);
        jsonJapaneseType.put(ParamKey.BUZZ_TYPE, value);        
        
    }
    
    private static void initHeader(){
        
//        japaneseHeader = new ArrayList<String>();
//        englishHeader = new ArrayList<String>();
                
        List<String> keys = new ArrayList<>();
        keys.add(Headers.number);
        keys.add(Headers.buzz_id);
        keys.add(Headers.buzz_type);
        keys.add(Headers.user_id);
        keys.add(Headers.user_name);
        keys.add(Headers.user_type);
        keys.add(Headers.email);
        keys.add(Headers.group);
        keys.add(Headers.cm_code);
        keys.add(Headers.content_buzz);
        keys.add(Headers.like_number);
        keys.add(Headers.commnent_number);
        keys.add(Headers.buzz_time);
        keys.add(Headers.buzz_status);//thanhdd add
        headers.addAll(keys);
//        headers = keys;
        
        HeaderCreator.createHeader(japaneseHeader, englishHeader, keys);
      
    }

    @Override
    public JSONObject toJsonObject() {
        JSONObject jo = new JSONObject();

        if (this.userId != null) {
            jo.put(userIdKey, this.userId);
        }
        if (this.userName != null) {
            jo.put(userNameKey, this.userName);
        }
        if (this.buzzTime != null) {
            jo.put(buzzTimeKey, this.buzzTime);
        }
        if (this.seenNum != null) {
            jo.put(seenNumKey, this.seenNum);
        }
        if (this.likeNum != null) {
            jo.put(likeNumKey, this.likeNum);
        }
        if (cmtNum != null) {
            jo.put(cmtNumKey, this.cmtNum);
        }
        if (this.buzzId != null) {
            jo.put(buzzIdKey, this.buzzId);
        }
        if (this.buzzVal != null) {
            jo.put(buzzValKey, this.buzzVal);
        }
        if (this.buzzType != null) {
            jo.put(buzzTypeKey, this.buzzType);
        }
//        if (this.lastAct != null) {
//            jo.put(lastActKey, this.lastAct);
//        }
         if (this.buzzStatus != null) {
            jo.put(buzzStatusKey, this.buzzStatus);
        }
         if (this.isDeleted != null) {
            jo.put(isDeletedKey, this.isDeleted);
        }
         if (this.isDenyUser != null) {
            jo.put(isDenyUserKey, this.isDenyUser);
        }
         if (this.isDenyUserName != null) {
            jo.put(isDenyUserNameKey, this.isDenyUserName);
        }

        if (this.comment != null) {
            JSONArray arr = new JSONArray();
            for (Comment comment1 : this.comment) {
                arr.add(comment1.toJsonObject());
            }
            jo.put(commentKey, arr);
        }       
        
        return jo;
    }

    @Override
    public List<String> getHeaders(Integer type) {
        if(type != null && type == 1)
            return englishHeader;
        else
            return japaneseHeader;
    }
    
    // GET LIST KEY OF SUBCLASS
    @Override
    public List<String> getKeys() {
        return headers;
    }    
    // get user type follow english or japanese
    @Override
    public JSONObject getJsonType(Integer type) {
        if(type != null && type == 1)
            return jsonEnglishType;
        else
            return jsonJapaneseType;
    }    
    
    @Override
    public int compareTo(Buzz obj) {
        if (this.buzzTime.compareTo(obj.buzzTime) > 0) {
            return -1;
        } else if (this.buzzTime.compareTo(obj.buzzTime) < 0) {
            return 1;
        } else {
            return 0;
        }
    }

}
