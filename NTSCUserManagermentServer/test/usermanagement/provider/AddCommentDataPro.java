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
public class AddCommentDataPro {
    
    public static final String DATA = "data";
    @DataProvider(name = DATA)
    //int genderU, int genderP, int errorCode, int point
    public static Object[][] provideData(){
        return new Object[][]{
          All.$(Constant.GENDER.FEMALE, Constant.GENDER.FEMALE, ErrorCode.SUCCESS, 100),
          All.$(Constant.GENDER.FEMALE, Constant.GENDER.MALE, ErrorCode.SUCCESS, 100),
          All.$(Constant.GENDER.MALE, Constant.GENDER.MALE, ErrorCode.SUCCESS, 100),
          All.$(Constant.GENDER.MALE, Constant.GENDER.FEMALE, ErrorCode.SUCCESS, 100),
//           not enought money
          All.$(Constant.GENDER.FEMALE, Constant.GENDER.MALE, ErrorCode.NOT_ENOUGHT_POINT, 0),
          All.$(Constant.GENDER.MALE, Constant.GENDER.MALE, ErrorCode.NOT_ENOUGHT_POINT, 0),
        };
    }
}
