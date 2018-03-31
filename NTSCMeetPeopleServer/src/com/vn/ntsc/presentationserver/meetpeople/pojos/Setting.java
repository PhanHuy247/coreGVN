/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.presentationserver.meetpeople.pojos;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author RuAc0n
 */
public class Setting {

    public static class Distance {

        public static ArrayList<Double> Value = new ArrayList<Double>();

        public static void init() {
            Setting.Distance.Value = new ArrayList<Double>();
            while (Setting.Distance.Value.size() < 5) {
                if ((int) Setting.Distance.Value.size() == 0) {
                    Setting.Distance.Value.add(50.0);
                }
                if ((int) Setting.Distance.Value.size() == 1) {
                    Setting.Distance.Value.add(200.1);
                }
                if ((int) Setting.Distance.Value.size() == 2) {
                    Setting.Distance.Value.add(600.2);
                }
                if ((int) Setting.Distance.Value.size() == 3) {
                    Setting.Distance.Value.add(2000.3);
                }
                if ((int) Setting.Distance.Value.size() == 4) {
                    Setting.Distance.Value.add(50000.4);
                }
            }
        }

    }

}
