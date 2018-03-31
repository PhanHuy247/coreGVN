/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.presentationserver.meetpeople.pojos.entity;

import java.util.Objects;
import eazycommon.util.Util;

/**
 *
 * @author Administrator
 */
public class Data implements Comparable<Data> {
    public double lon;
    public double lat;
    public String email;
    public boolean isOnline;
    
    public Data(){
        lon = 0;
        lat = 0;
        email = null;
        this.isOnline = false;
    }
    
    public Data(String email){
        this.email = email;
    }

    public Data(double lon, double lat, String email, boolean isOnline ){
        this.lon = lon;
        this.lat = lat;
        this.email = email;
        this.isOnline = isOnline;
    }    
    
    @Override
    public boolean equals(Object dataO){
        try{
            Data data = (Data)(dataO);
            if(data == null)
                return false;
            if(data.email == null || email == null)
                return false;
            return email.equals(data.email);
        }catch(Exception ex){
            Util.addErrorLog(ex);
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + Objects.hashCode(this.email);
        return hash;
    }
    
    @Override
    public int compareTo(Data data){
        if(this.isOnline && !data.isOnline)
            return -1;
        else if(!this.isOnline && data.isOnline)
            return 1;
        else
            return 0;
    }

    /**
     * @return the lon
     */
    public double getLon() {
        return lon;
    }

    /**
     * @param lon the lon to set
     */
    public void setLon(double lon) {
        this.lon = lon;
    }

    /**
     * @return the lat
     */
    public double getLat() {
        return lat;
    }

    /**
     * @param lat the lat to set
     */
    public void setLat(double lat) {
        this.lat = lat;
    }

    /**
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email the email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }
    
    
}
