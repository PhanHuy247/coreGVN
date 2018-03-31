/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package vn.com.ntsc.staticfileserver.server;

import eazycommon.constant.ParamKey;


/**
 *
 * @author RuAc0n
 */
public class AndGRespond {

    public static final String resultKey = "result";
    private String result;
    public static final String userIdKey = ParamKey.USER_ID;
    private String userId;
    public static final String newTokenKey = "new_token";
    private String newToken;

    public static AndGRespond initRespond(String inputString){
        AndGRespond r = new AndGRespond();
        String [] element = inputString.split("&");
        for (String temp : element) {
            String []map = temp.split("=");
            r.initRequestInfor(map[0], map[1]);
        }
        return r;
    }

    private void initRequestInfor(String key, String value){
        if(key.equalsIgnoreCase(resultKey)){
            this.setResult(value);
        }else if(key.equalsIgnoreCase(userIdKey)){
            this.setUserId(value);
        }else if(key.equalsIgnoreCase("new_token")){
            this.setNewToken(value);
        }
    }

    /**
     * @return the result
     */
    public String getResult() {
        return result;
    }

    /**
     * @param result the result to set
     */
    public void setResult(String result) {
        this.result = result;
    }

    /**
     * @return the userId
     */
    public String getUserId() {
        return userId;
    }

    /**
     * @param userId the userId to set
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getNewToken() {
        return newToken;
    }

    public void setNewToken(String newToken) {
        this.newToken = newToken;
    }
    
    
}
