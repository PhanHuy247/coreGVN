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
public class AddFavoristDataPro {
    
    public static final String DATA = "data";
    @DataProvider (name = DATA)
    //int genderU, int genderF, int errorCode, int isFavoristed
    public static Object[][] provideData(){
        return new Object[][]{
          All.$(Constant.GENDER.FEMALE, Constant.GENDER.MALE, ErrorCode.SUCCESS, Constant.FLAG.ON, 1),
          All.$(Constant.GENDER.FEMALE, Constant.GENDER.FEMALE, ErrorCode.SUCCESS, Constant.FLAG.ON, 1),
          All.$(Constant.GENDER.MALE, Constant.GENDER.MALE, ErrorCode.SUCCESS, Constant.FLAG.ON, 1),
          All.$(Constant.GENDER.MALE, Constant.GENDER.FEMALE, ErrorCode.SUCCESS, Constant.FLAG.ON, 1),
          All.$(Constant.GENDER.MALE, 5, ErrorCode.UNKNOWN_ERROR, Constant.FLAG.OFF, 0),
        };
    }
    
    public static final String DATA1 = "data1";
    @DataProvider (name = DATA1)
    //int genderU, int genderF, int errorCode, int isFavoristed
    public static Object[][] provideData1(){
        return new Object[][]{
          All.$(Constant.GENDER.FEMALE, Constant.GENDER.MALE, ErrorCode.SUCCESS),
          All.$(Constant.GENDER.FEMALE, Constant.GENDER.FEMALE, ErrorCode.BLOCK_USER),
          All.$(Constant.GENDER.MALE, Constant.GENDER.MALE, ErrorCode.BLOCK_USER),
          All.$(Constant.GENDER.MALE, Constant.GENDER.FEMALE, ErrorCode.SUCCESS),
        };
    }
}
