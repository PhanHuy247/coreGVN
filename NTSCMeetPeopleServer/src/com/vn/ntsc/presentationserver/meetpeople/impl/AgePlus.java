/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.presentationserver.meetpeople.impl;

import static java.lang.Thread.sleep;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import eazycommon.constant.Constant;
import eazycommon.util.Util;
import com.vn.ntsc.presentationserver.meetpeople.dao.DatabaseLoader;
import com.vn.ntsc.presentationserver.meetpeople.pojos.Tool;
import com.vn.ntsc.presentationserver.meetpeople.pojos.entity.Data;
import com.vn.ntsc.presentationserver.meetpeople.pojos.entity.User;

/**
 *
 * @author RuAc0n
 */
public class AgePlus extends Thread {

    public static Map<String, Date> mDate = new ConcurrentHashMap<>();

    private boolean isLeapYear(int year) {
        return (year % 4 == 0 && year % 100 > 0) || year % 400 == 0;
    }

    public static void agePlusRun() {
        AgePlus ap = new AgePlus();
        ap.start();
    }

    @Override
    public void run() {
        while (true) {
            Date now = new Date();
            long startTime = now.getTime();
            Iterator<Data> it = MeetPeopleProcessor.allData.iterator();
            while (it.hasNext()) {
                Data dt = it.next();
                if (dt == null) {
                    MeetPeopleProcessor.allData.remove(dt);
                } else {
                    try {
                        String email = dt.email;
                        Date d = mDate.get(email);

                        int age = Tool.calAge(now, d);
                        User u = MeetPeopleProcessor.allUser.get(email);
                        if (u != null) {
                            if (age != u.age) {
                                int collection = u.calCollection();
                                List<Data> list = MeetPeopleProcessor.datas.get(collection);
                                if (list != null) {
                                    list.remove(dt);
                                }
//                            MeetPeopleProcessor.removeUserByMail(email);

                                u.age = age;
                                MeetPeopleProcessor.allUser.put(email, u);
//                            MeetPeopleProcessor.allData.add(dt);
                                list = MeetPeopleProcessor.datas.get(u.calCollection());
                                if (list == null) {
                                    list = new ArrayList<>();
                                }
                                if (!list.contains(dt)) {
                                    list.add(dt);
                                }
                                MeetPeopleProcessor.datas.put(u.calCollection(), list);
                            }
                        } else { // if not exists, read from db
                            u = DatabaseLoader.getOneUser(email);
                            if (u != null) {

                                dt.isOnline = u.isOnline;
                                dt.lat = u.lat;
                                dt.lon = u.lon;
                                MeetPeopleProcessor.allUser.put(email, u);
                                List<Data> list = MeetPeopleProcessor.datas.get(u.calCollection());
                                if (list == null) {
                                    list = new ArrayList<>();
                                }
                                if (!list.contains(dt)) {
                                    list.add(dt);
                                }
                                MeetPeopleProcessor.datas.put(u.calCollection(), list);
                            }
                        }
                    } catch (Exception ex) {
                        Util.addErrorLog(ex);
                    }
                }
            }
//            for(int i = MeetPeopleProcessor.allData.size() - 1; i >= 0; i--){
//            }

            try {
                sleep(Constant.AN_HOUR + startTime - (new Date()).getTime());
//                sleep(20 * 1000);
            } catch (Exception ex) {
                Util.addErrorLog(ex);

            }
        }
    }
}
