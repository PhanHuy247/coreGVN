/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.vn.ntsc.presentationserver.meetpeople.pojos;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import eazycommon.constant.Constant;

/**
 *
 * @author RuAc0n
 */
public class Tool {

    
    public static int calAge(Date dnow, Date dbir){
        int age = (dnow).getYear() - dbir.getYear();
        int ndate = dnow.getDate();
        int nmonth = dnow.getMonth();
        int date = dbir.getDate();
        int month = dbir.getMonth();
        if(nmonth < month || (nmonth == month && ndate < date)) age--;
        return age;
    }
    
    public static int convertShowme(String showme){
        if(showme.compareTo("male") == 0) return 0;
        if(showme.compareTo("female") == 0) return 1;
        return -1;
    }
    
    public static int convertInterested(String interested){
        if(interested.compareTo("male") == 0) return 0;
        if(interested.compareTo("female") == 0) return 1;
        return -1;
    }
    
    public static int convertEthnicity(List<Long> ethnics){
        int res = 0;
        Iterator it = ethnics.iterator();
        while(it.hasNext()){
            res += (1 << Integer.parseInt(it.next().toString()));
        }
        return res;
    }
    
    private static final double PI_DEGREE = 180.0;
    private static final double PI = 3.141592653589793;
    public static double convertDegreeByDistance(int numberd){
        double res = Setting.Distance.Value.get(numberd) / Constant.R_EARTH * PI_DEGREE / PI;
        return res;
    }    
    
}
