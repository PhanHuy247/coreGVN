/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package usermanagement.provider;

import eazycommon.constant.Constant;
import eazycommon.constant.ErrorCode;
import org.testng.annotations.DataProvider;
import usermanagement.All;

/**
 *
 * @author duyetpt
 */
public class RegistDataPro {
    
    public static final String DATA_NAME = "data";
    @DataProvider(name = DATA_NAME)
    public static Object[][] provideData(){
        return new Object[][]{
            //fbID, gender, bir = "yyyyMMdd"
            // regist by fbId
          All.$("fbId", Constant.GENDER.MALE, "19930101", ErrorCode.SUCCESS),
          All.$("fbId", Constant.GENDER.FEMALE, "19930101", ErrorCode.SUCCESS),
          //regist by email
          All.$(null, Constant.GENDER.FEMALE, "19930101", ErrorCode.SUCCESS),
          All.$(null, Constant.GENDER.MALE, "19930101", ErrorCode.SUCCESS),
          // age < 16
          All.$(null, Constant.GENDER.MALE, "20000101", ErrorCode.INVALID_BIRTHDAY),
          // age > 120
          All.$(null, Constant.GENDER.MALE, "18000101", ErrorCode.INVALID_BIRTHDAY),
          
        };
    }
}
