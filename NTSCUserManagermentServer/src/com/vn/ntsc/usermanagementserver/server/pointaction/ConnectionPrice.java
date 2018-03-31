/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.vn.ntsc.usermanagementserver.server.pointaction;

import eazycommon.constant.Constant;
import eazycommon.constant.mongokey.DAOKeys;
import eazycommon.constant.mongokey.SettingdbKey;
import eazycommon.util.Util;
import org.json.simple.JSONObject;
import com.vn.ntsc.usermanagementserver.server.userinformanager.UserInforManager;

/**
 *
 * @author Rua
 */
public class ConnectionPrice {
    public int senderPrice;
    public int receiverPrice;
    
    public ConnectionPrice(){
    }
    
    public ConnectionPrice( int senderPrice , int receiverPrice){
        this.senderPrice = senderPrice;
        this.receiverPrice = receiverPrice;
    }
    
    private static String getPair(int senderGender, int receiverGender){
        String sender = "male";
        if(senderGender == Constant.GENDER.FEMALE){
            sender = "female";
        }
        String receiver = "male";
        if(receiverGender == Constant.GENDER.FEMALE){
            receiver = "female";
        }
        
        StringBuilder pair = new StringBuilder(sender);
        pair.append("_");
        pair.append(receiver);
        
        return pair.toString();
    } 
    
    public static ConnectionPrice getBacstageBonusPrice(String userId ){
         
        JSONObject actionJson = (JSONObject) ActionManager.connectionPointSetting.get(String.valueOf(ActionType.unlock_backstage));
        int receiverGender = UserInforManager.getGender(userId);
        
        String pair = "female_male";
        if(receiverGender == Constant.GENDER.FEMALE)
            pair = "male_female";
        JSONObject pairJson = (JSONObject) actionJson.get(pair);
        
        String receiverKey = SettingdbKey.CONNECTION_POINT_SETTING.RECEIVER;
        String senderKey = SettingdbKey.CONNECTION_POINT_SETTING.SENDER;
        
        Object sPrice = ((JSONObject)pairJson.get(senderKey)).get(DAOKeys.value);
        Object rPrice = ((JSONObject)pairJson.get(receiverKey)).get(DAOKeys.value);
        
        ConnectionPrice price = new ConnectionPrice(Integer.parseInt(sPrice.toString()), Integer.parseInt(rPrice.toString()));
        
        return price;
    }    
    
    public static ConnectionPrice getConnectionPrice(String action, String senderId, String receiverId){
        JSONObject actionJson = (JSONObject) ActionManager.connectionPointSetting.get(action);
        int senderGender = UserInforManager.getGender(senderId);
        int receiverGender = UserInforManager.getGender(receiverId);
        
        String pair = getPair(senderGender, receiverGender);
        JSONObject pairJson = (JSONObject) actionJson.get(pair);
        
        String receiverKey = SettingdbKey.CONNECTION_POINT_SETTING.RECEIVER;
        String senderKey = SettingdbKey.CONNECTION_POINT_SETTING.SENDER;
        
        if(!pair.equals(DAOKeys.female_female)){
            boolean havePurchasePrice;
            if(!pair.equals(DAOKeys.female_male)){
                havePurchasePrice = UserInforManager.havePurchased(senderId);
            }else{
                havePurchasePrice = UserInforManager.havePurchased(receiverId);
            }
            
            if(!havePurchasePrice){
                receiverKey = SettingdbKey.CONNECTION_POINT_SETTING.POTENTIAL_CUSTOMER_RECEIVER;
                senderKey = SettingdbKey.CONNECTION_POINT_SETTING.POTENTIAL_CUSTOMER_SENDER;
            }
        }
        
        Object sPrice = ((JSONObject)pairJson.get(senderKey)).get(DAOKeys.value);
        Object rPrice = ((JSONObject)pairJson.get(receiverKey)).get(DAOKeys.value);
        if (sPrice == null){
            sPrice = 0;
        }
        if (rPrice == null){
            rPrice = 0;
        }

        
        ConnectionPrice price = new ConnectionPrice(Integer.parseInt(sPrice.toString()), Integer.parseInt(rPrice.toString()));
        
        return price;
    }

    public static ConnectionPrice getBadConnectionPrice(String action, String userId){
         
        JSONObject actionJson = (JSONObject) ActionManager.connectionPointSetting.get(action);
        
        int sPrice = 0;
        int rPrice = 1000001;
        for(String pair: DAOKeys.pairs_list){
            JSONObject pairJson = (JSONObject) actionJson.get(pair);
            Object sPriObject = ((JSONObject) pairJson.get(SettingdbKey.CONNECTION_POINT_SETTING.POTENTIAL_CUSTOMER_SENDER)).get(DAOKeys.value);
            if(sPriObject != null){
                Integer sPri = Integer.parseInt(sPriObject.toString());
                if( sPri < sPrice){
                    sPrice = sPri;
                }
            }
            sPriObject = ((JSONObject) pairJson.get(SettingdbKey.CONNECTION_POINT_SETTING.SENDER)).get(DAOKeys.value);
            Integer sPri = Integer.parseInt(sPriObject.toString());
            if(sPri < sPrice){
                sPrice = sPri;
            }
            Object rPriObject = ((JSONObject) pairJson.get(SettingdbKey.CONNECTION_POINT_SETTING.POTENTIAL_CUSTOMER_RECEIVER)).get(DAOKeys.value);
            if(rPriObject != null){
                Integer rPri = Integer.parseInt(rPriObject.toString());
                if(rPri < rPrice){
                    rPrice = rPri;
                }
            }
            rPriObject =((JSONObject) pairJson.get(SettingdbKey.CONNECTION_POINT_SETTING.RECEIVER)).get(DAOKeys.value);
            Integer rPri = Integer.parseInt(rPriObject.toString());
            if(rPri < rPrice){
                rPrice = rPri;
            }
        }
        if(rPrice == 1000001)
            rPrice = 0;
        ConnectionPrice price = new ConnectionPrice(sPrice, rPrice);
        return price;
    }    
    
    public static ConnectionPrice getCommunicationPrice(String action, String callerId, String receiverId){
         
        JSONObject actionJson = (JSONObject) ActionManager.communicationPointSetting.get(action);
        int callerGender = UserInforManager.getGender(callerId);
        int receiverGender = UserInforManager.getGender(receiverId);
        
        String pair = getPair(callerGender, receiverGender);
        JSONObject pairJson = (JSONObject) actionJson.get(pair);
        
        String receiverKey = SettingdbKey.COMMUNICATION_SETTING.RECEIVER;
        String callerKey = SettingdbKey.COMMUNICATION_SETTING.CALLER;
        
        if(!pair.equals(DAOKeys.female_female)){
            boolean havePurchasePrice;
            if(!pair.equals(DAOKeys.female_male)){
                havePurchasePrice = UserInforManager.havePurchased(callerId);
            }else{
                havePurchasePrice = UserInforManager.havePurchased(receiverId);
            }
            
            if(!havePurchasePrice){
                receiverKey = SettingdbKey.COMMUNICATION_SETTING.POTENTIAL_CUSTOMER_RECEIVER;
                callerKey = SettingdbKey.COMMUNICATION_SETTING.POTENTIAL_CUSTOMER_CALLER;
            }
        }
        
        Object callerPrice = ((JSONObject)pairJson.get(callerKey)).get(DAOKeys.value);
        Object rPrice = ((JSONObject)pairJson.get(receiverKey)).get(DAOKeys.value);
        if (callerPrice == null){
            callerPrice = 0;
        }
        if (rPrice == null){
            rPrice = 0;
        }

        
        ConnectionPrice price = new ConnectionPrice(Integer.parseInt(callerPrice.toString()), Integer.parseInt(rPrice.toString()));
        
        return price;
    }    

    @Override
    public String toString() {
        return "ConnectionPrice{" + "senderPrice=" + senderPrice + ", receiverPrice=" + receiverPrice + '}';
    }
}
