/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package data.provider;

import eazycommon.constant.ErrorCode;
import org.testng.annotations.DataProvider;
import test.All;

/**
 *
 * @author duyetpt
 */
public class ChangePasswordDataPro {

    //String registPass, String oldPass, String newPass, int errorCode
    public static final String DATA_NAME = "data";

    @DataProvider(name = DATA_NAME)
    public static Object[][] provideData() {
        return new Object[][]{
            // case : succ
            All.$("12345678", "12345678", "123456997", ErrorCode.SUCCESS),
            //incorrect pass
            All.$("12345678", "12345678d", "123456997", ErrorCode.INCORRECT_PASSWORD),
            //oldPass is invalid
            All.$("12345678", "", "123456997", 4),
            All.$("12345678", null, "123456997", 4),
            // newPass invalid
            All.$("12345678", "12345678", "", 5),
            All.$("12345678", "12345678", "123", ErrorCode.INVALID_PASSWORD),
            All.$("12345678", "12345678", null, 5),
        };
    }

}
