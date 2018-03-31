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
public class SaveImageDataPro {
    
    public static final String DATA_NAME = "data";
    //int genderU, int genderP, String imageId, int errorCode
    @DataProvider(name = DATA_NAME)
    public static Object[][] provideData(){
        return new Object[][]{
          All.$(Constant.GENDER.FEMALE, Constant.GENDER.FEMALE, ErrorCode.SUCCESS),
          All.$(Constant.GENDER.FEMALE, Constant.GENDER.MALE, ErrorCode.SUCCESS),
          All.$(Constant.GENDER.MALE, Constant.GENDER.FEMALE, ErrorCode.SUCCESS),
          All.$(Constant.GENDER.MALE, Constant.GENDER.MALE, ErrorCode.SUCCESS),
        };
    }
    
    public static final String DATA_NOT_ENOUGHT = "not_enought";
    //int genderU, int genderP, String imageId, int errorCode
    @DataProvider(name = DATA_NOT_ENOUGHT)
    public static Object[][] provideDataNotEnought(){
        return new Object[][]{
          All.$(Constant.GENDER.FEMALE, Constant.GENDER.FEMALE, ErrorCode.NOT_ENOUGHT_POINT),
          All.$(Constant.GENDER.MALE, Constant.GENDER.FEMALE, ErrorCode.NOT_ENOUGHT_POINT),
        };
    }
}
