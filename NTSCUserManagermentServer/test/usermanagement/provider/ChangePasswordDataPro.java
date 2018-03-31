/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package usermanagement.provider;

import eazycommon.constant.ErrorCode;
import eazycommon.constant.mongokey.UserdbKey;
import org.testng.annotations.DataProvider;
import usermanagement.All;

/**
 *
 * @author duyetpt
 */
public class ChangePasswordDataPro {

    @DataProvider(name = "data")
    public static Object[][] data() {
        return new Object[][]{
            //newPass, oldPass, errorCode, flagKey, flag(int)
            //case succ
            All.$("12345678", null, ErrorCode.SUCCESS, null, null), 
            // case : locked_user
            All.$("12345678", null, ErrorCode.LOCKED_USER, UserdbKey.USER.FLAG, -1), 
            All.$("12345678", null, ErrorCode.LOCKED_USER, UserdbKey.USER.FINISH_REGISTER_FLAG, 0), 
            All.$("12345678", null, ErrorCode.LOCKED_USER, UserdbKey.USER.UPDATE_EMAIL_FLAG, 0), 
            All.$("12345678", null, ErrorCode.LOCKED_USER, UserdbKey.USER.VERIFICATION_FLAG, 0), 
            // case : newPass invalid
            All.$("12", null, ErrorCode.INVALID_PASSWORD, UserdbKey.USER.VERIFICATION_FLAG, 0), 
            // case : oldPass incorrect
            All.$("12wewwrew", "tyuiokm", ErrorCode.INCORRECT_PASSWORD, UserdbKey.USER.VERIFICATION_FLAG, 0), 
        };
    }
}
