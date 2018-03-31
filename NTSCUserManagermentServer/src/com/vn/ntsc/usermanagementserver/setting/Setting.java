/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.vn.ntsc.usermanagementserver.setting;

import java.util.ArrayList;

/**
 *
 * @author RuAc0n
 */
public class Setting {

    public static double LOCAL_DISTANCE = 200;
    
    public static int BACKSTAGE_TIME = 24;
    public static int VIEW_IMAGE_TIME = 24;
    public static int WATCH_VIDEO_TIME = 24;
    public static int LISTEN_AUDIO_TIME = 24;
    
    public static ArrayList<Double> DISTANCE = new ArrayList<>();
    
    public static int AUTO_HIDE_REPORTED_IMAGE = 0;
    public static int AUTO_APPROVE_REVIEW_USER = 0;
    
    public static int AUTO_HIDE_REPORTED_VIDEO = 0;
    public static int AUTO_HIDE_REPORTED_AUDIO = 0;
}
