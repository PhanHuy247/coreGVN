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
public class PayChatDataPro {
    
    public static final String DATA_ENOUGHT_POINT = "enought_point";
    @DataProvider(name = DATA_ENOUGHT_POINT)
    public static Object[][] provideEnoughtPoint(){
        return new Object[][]{
          All.$(2, Constant.GENDER.FEMALE, Constant.GENDER.FEMALE, ErrorCode.SUCCESS, 100),
          All.$(2, Constant.GENDER.FEMALE, Constant.GENDER.MALE, ErrorCode.SUCCESS, 100),
          All.$(2, Constant.GENDER.MALE, Constant.GENDER.FEMALE, ErrorCode.SUCCESS, 100),
          All.$(2, Constant.GENDER.MALE, Constant.GENDER.MALE, ErrorCode.SUCCESS, 100),
          //  not enought point
          All.$(2, Constant.GENDER.FEMALE, Constant.GENDER.MALE, ErrorCode.NOT_ENOUGHT_POINT, 0),
          All.$(2, Constant.GENDER.MALE, Constant.GENDER.FEMALE, ErrorCode.NOT_ENOUGHT_POINT, 0),
        };
    }
}
