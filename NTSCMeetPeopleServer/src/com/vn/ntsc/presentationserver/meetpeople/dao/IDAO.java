/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.vn.ntsc.presentationserver.meetpeople.dao;

import com.vn.ntsc.presentationserver.meetpeople.pojos.entity.QuerySetting;

/**
 *
 * @author RuAc0n
 */
public interface IDAO {

    public QuerySetting getMeetPeopleSetting(String email);
    
    public void updateMeetPeopleSetting(String email, QuerySetting query);
}
