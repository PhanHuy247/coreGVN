/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.backend.server.respond.impl.backend.log;

import java.util.List;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.ParamKey;
import eazycommon.exception.EazyException;
import eazycommon.util.DateFormat;
import eazycommon.util.Util;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Objects;
import java.util.Set;
import org.json.simple.JSONObject;
import com.vn.ntsc.backend.Config;
import com.vn.ntsc.backend.dao.DBManager;
import com.vn.ntsc.backend.dao.chat.ChatDAO;
import com.vn.ntsc.backend.dao.file.FileDAO;
import com.vn.ntsc.backend.dao.user.FileStfDAO;
import com.vn.ntsc.backend.dao.user.ImageStfDAO;
import com.vn.ntsc.backend.dao.user.UserDAO;
import com.vn.ntsc.backend.entity.IEntity;
import com.vn.ntsc.backend.entity.impl.chat.Message;
import com.vn.ntsc.backend.entity.impl.datarespond.SizedListData;
import com.vn.ntsc.backend.entity.impl.user.User;
import com.vn.ntsc.backend.server.common.LogProcess;
import com.vn.ntsc.backend.server.respond.IApiAdapter;
import com.vn.ntsc.backend.server.respond.Respond;
import com.vn.ntsc.backend.server.respond.result.EntityRespond;
import java.util.Date; 


/**
 *
 * @author RuAc0n
 */
public class SearchLogChatApi implements IApiAdapter {

    @Override
    public Respond execute(JSONObject obj) {
        EntityRespond respond = new EntityRespond();
        try {
            Long userType = Util.getLongParam(obj, ParamKey.USER_TYPE);
            String email = Util.getStringParam(obj, ParamKey.EMAIL);
            if (userType == null) {
                if (email != null) {
                    return null;
                }
            }
            String id = Util.getStringParam(obj, ParamKey.ID);
            String cmCode = Util.getStringParam(obj, ParamKey.CM_CODE);
            String userId = Util.getStringParam(obj, ParamKey.REQUEST_USER_ID);

            List<String> listId = UserDAO.getListUser(userType, id, email, cmCode);

            String toTime = Util.getStringParam(obj, ParamKey.TO_TIME);
            String fromTime = Util.getStringParam(obj, ParamKey.FROM_TIME);
            Long to = null, from = null;
            if (toTime != null) {
                to = DateFormat.parse(toTime).getTime();
            }
            if (fromTime != null) {
                from = DateFormat.parse(fromTime).getTime();
            }
            Long sort = Util.getLongParam(obj, ParamKey.SORT);
            Long order = Util.getLongParam(obj, ParamKey.ORDER);
            Long skip = Util.getLongParam(obj, ParamKey.SKIP);
            Long take = Util.getLongParam(obj, ParamKey.TAKE);
            Long by_user_type = Util.getLongParam(obj, ParamKey.BY_USER_TYPE);
            Long message_type = Util.getLongParam(obj, ParamKey.MESSAGE_TYPE);
//            Long firstLoad = Util.getLongParam(obj, ParamKey.FIRST_LOAD);
            if (sort == null || order == null) {
                return null;
            }

            Long csv = Util.getLongParam(obj, ParamKey.CSV);
            Long gender = Util.getLongParam(obj, ParamKey.GENDER);
            boolean isCsv = false;
            if (csv != null) {
                isCsv = true;
            }
            // Add LongLT 11/8/2016 Start
            SizedListData data;
            if (userId == null) {
                data = getAllMessages(null,from, to, sort, order, skip, take, gender, by_user_type);
            } else {
                //data = ChatDAO.getMessages(userId, listId, from, to, sort, order, skip, take, isCsv);
                data = ChatDAO.getMessagesByTypeUser(userId, listId, from, to, sort, order, skip, take, isCsv, by_user_type, message_type);
            }
            // Add LongLT 11/8/2016 Start

            if (isCsv) {
                Message.set(data.ll, null);
                String api = Util.getStringParam(obj, ParamKey.API_NAME);
                return LogProcess.createCSV(api, csv, data.ll, null, new Message());
            } else {
                // get video url 
                for (Object object : data.ll) {
                    Message message = (Message) object;
                    if (message.messageType != null) {
                        if (message.messageType.equals("FILE")) {
                            if (message.content != null) {
                                String[] arr = message.content.split("\\|");
                                if (arr.length >= 3) {
                                    int is_free = 0;
                                    if (arr[1].endsWith("p")) {
                                        is_free = ImageStfDAO.getIsFree(arr[2]);
                                    } else {
                                        is_free = FileStfDAO.getIsFree(arr[2]);
                                    }
                                    message.content = message.content + "|" + is_free;
                                    if (arr[1].endsWith("v")) {
                                        String fileUrl = FileDAO.getFileUrl(arr[2]);
                                        if (fileUrl != null) {
                                            fileUrl = fileUrl.replace(".sh", ".mp4");
                                            String streamingUrl = Config.STREAMING_HOST + fileUrl;
                                            message.url = streamingUrl;
                                        }
                                    }
                                }
                            }
                        } else if(message.messageType.equals("PP")){
//                            Util.addDebugLog("message.content " + message.content);
                            message.content = Util.replaceWordChatBackend(message.content);
                        }
                    }
                }
            }

            LogProcess.getTwoObjectInfor(data.ll);
            respond = new EntityRespond(ErrorCode.SUCCESS, data);
        } catch (EazyException ex) {
            Util.addErrorLog(ex);
            respond = new EntityRespond(ex.getErrorCode());
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
        return respond;
    }

    // LongLT 11/8/2016
//    private SizedListData getAllMessages(List<String> listId, Long from, Long to, Long sort, Long order, Long skip, Long take) {
    private SizedListData getAllMessages(List<String> listId, Long from, Long to,Long sort, Long order, Long skip, Long take, Long gender,// edit Long 22Aug2016
            Long by_user_type) { //thanhdd edit 04/11/2016
        SizedListData data = new SizedListData();
        SizedListData temp =null;

        Set<String> collectionNames = DBManager.getChatExtensionDB().getCollectionNames();
        Calendar now = Calendar.getInstance();
        now.add(Calendar.DAY_OF_MONTH, -7);
        Long sevenDaysAgo = now.getTime().getTime();
        
        Calendar calendar = Calendar.getInstance();
        calendar.get(Calendar.DAY_OF_MONTH);
        Long longNow = calendar.getTimeInMillis();
        
       
        Date dateNow=new Date(longNow);
        Date datesevenDaysAgo = new Date(sevenDaysAgo);
        Date dateTo = null;
        if(null!=to)
        dateTo =new Date(to);
        
        Date dateFrom = null;
        if(null != from)
        dateFrom = new Date(from);
       
        for (String userId : collectionNames) {
            //thanhdd edit
            try {
                //temp = ChatDAO.getMessagesToLog(userId, listId, sevenDaysAgo, null, sort, order, 0L, take, false, gender);// edit LongLT               
                if (from == null && to == null) {
                    temp = ChatDAO.getMessagesToLogOrder(userId, listId, by_user_type, sevenDaysAgo, null, sort, order, 0L, false, gender);// edit ThanhDD 
                } else if(from == null && to != null && (dateTo.compareTo(dateNow) <= 0) && ( dateTo.compareTo(datesevenDaysAgo) >= 0)){
                    //Util.addDebugLog("namIT -- Success.!");
                    temp = ChatDAO.getMessagesToLogOrder(userId, listId, by_user_type, sevenDaysAgo, to, sort, order, 0L, false, gender);// edit ThanhDD
                } else if (from != null && to == null && (dateFrom.compareTo(dateNow) <= 0) &&(dateFrom.compareTo(datesevenDaysAgo) >= 0)){
                     //Util.addDebugLog("namIT -- Success.!");
                    temp = ChatDAO.getMessagesToLogOrder(userId, listId, by_user_type, from, longNow, sort, order, 0L, false, gender);// edit ThanhDD 
                } else {
//                     Util.addDebugLog("thanhdd --------------- total data ->: else.!"
//                             +"\nCheck To: "+(dateTo.compareTo(dateNow) <= 0) 
//                             +"\nCheck Form:"+(dateFrom.compareTo(datesevenDaysAgo) >= 0) 
//                             +"\nform: "+ dateFrom
//                             +"\nsevenDay:"+datesevenDaysAgo);
                    //if ((dateTo.compareTo(dateNow) <= 0) && (dateFrom.compareTo(datesevenDaysAgo) >= 0)){
                        Util.addDebugLog("thanhdd --------------- total data ->: success.!");
                        temp = ChatDAO.getMessagesToLogOrder(userId, listId, by_user_type, from, to, sort, order, 0L, false, gender);
                   // }else{
                        //TODO
                    //}
                }
                    
                if (temp != null) {
//                    if (data.total < (totalWillTake)) {

                        data.ll.addAll(temp.ll);
//                    }
                    if (temp.ll != null) {
                        data.total += temp.ll.size();
                    }
                }
                 //Util.addDebugLog("thanhdd --------------- total data ->: "+data.total);
               //Util.addDebugLog("thanhdd --------------- size data ->: "+data.ll.size());
            } catch (EazyException ex) {
                Util.addErrorLog(ex);
            }
        }
        
        //ThanhDD 30/9/2016 #4662
        if (data.ll != null && data.ll.size()>0) {
        //data.ll = data.ll.subList(skip.intValue(), take.intValue());
            Message msgi;
            Message msgj;
            Message a;
            Message b;
            
            Date datei;
            Date datej;
            
            for (int i=0; i<data.ll.size()-1;i++){
                 if (order==-1){
                      
                    for (int j = 1; j< data.ll.size()-i; j++) {
                        msgi=(Message)data.ll.get(j);
                        datei=DateFormat.parse(msgi.time);
                        msgj=(Message)data.ll.get(j-1);
                        datej=DateFormat.parse(msgj.time);
                        a= (Message)data.ll.get(j);
                        b= (Message)data.ll.get(j-1);

                        if(datej.compareTo(datei)<0){
                            data.ll.set(j-1, a);
                            data.ll.set(j, b);
                        }
                        
                    }
                 } else if (order==1){
                      
                    for (int j = 0; j< data.ll.size()-1; j++) {
                        msgi=(Message)data.ll.get(j);
                        datei=DateFormat.parse(msgi.time);
                        msgj=(Message)data.ll.get(j+1);
                        datej=DateFormat.parse(msgj.time);

                        a= (Message)data.ll.get(j);
                        b= (Message)data.ll.get(j+1);

                        if(datej.compareTo(datei)<0){
                            data.ll.set(j, b);
                            data.ll.set(j+1, a);
                        }
                        
                    } 
                      
                }
            }
        }
        //Util.addDebugLog("thanhdd -- size data complete: "+data.ll.size());
        //Util.addDebugLog("--------------- after sort" + data.toJsonObject().toJSONString());
        int size = data.ll.size();
        int startIndex = skip.intValue();
        int endIndex = skip.intValue() + take.intValue();
        if (endIndex > size) {
            endIndex = size;
        }
        data.ll = data.ll.subList(startIndex, endIndex);
        return data;
    }
}
