/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.vn.ntsc.presentationserver.meetpeople.pojos;

/**
 *
 * @author Rua
 */
public class Constants {
    
    public static final int NUMBER_LOCATION = 64;
    public static final int MAX_LOCATION = 64;
    public static final int MAX_AGE = 120;
    public static final int MAX_SHOW_ME = 2;
    public static final int MAX_CALL_WAITING = 2;
    public static final int MUL_AGE = MAX_LOCATION;
    public static final int MUL_CALL_WAITING = MAX_LOCATION * MAX_AGE;
    public static final int MUL_SHOW_ME = MAX_LOCATION * MAX_AGE * MAX_CALL_WAITING;
    
    public static final int SEARCH_BY_REGION = 2;
    public static final int SEARCH_BY_WORLD = 4;

    public static final int SORT_BY_LOGIN_TIME = 1;
    public static final int SORT_BY_REGISTER_TIME = 2;
//    public static final int SORT_BY_CALL_WAITING = 3;
    public static final int FILTER_BY_REGISTER_TIME = 1;
    public static final int FILTER_BY_CALL_WAITING = 2;
}
