/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.presentationserver.server.request;

/**
 *
 * @author tuannxv00804
 */
public class InsertPeople {
    
    public int showme;
    public int interested;
    public int age;
    public int ethnic;
    public double lon;
    public double lat;
    public boolean isOnline;
    public String email;
    public String username;
    public String avataID;

    public InsertPeople() {
    }

    
    public InsertPeople(int showme, int interested, int age, int ethnic, double lon, double lat, boolean isOnline, String email, String username, String avataID) {
        this.showme = showme;
        this.interested = interested;
        this.age = age;
        this.ethnic = ethnic;
        this.lon = lon;
        this.lat = lat;
        this.isOnline = isOnline;
        this.email = email;
        this.username = username;
        this.avataID = avataID;
    }
    
}
