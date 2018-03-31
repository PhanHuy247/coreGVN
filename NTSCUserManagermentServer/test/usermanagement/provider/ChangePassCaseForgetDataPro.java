/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package usermanagement.provider;

import eazycommon.constant.Constant;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.ParamKey;
import eazycommon.constant.mongokey.UserdbKey;
import org.testng.annotations.DataProvider;
import usermanagement.All;

/**
 *
 * @author duyetpt
 */
public class ChangePassCaseForgetDataPro {

    @DataProvider(name = "data")
    public static Object[][] dataPro() {
        return new Object[][]{
            // case succ
            //int error_code, int flag, String key, String emailUp,
            //String emailPass, String pass, String verifiCodeSet, String verifiCodePass
            All.$(ErrorCode.SUCCESS, 1, null, "email@gmail.com",
            "email@gmail.com", "123456789", "123456789", "123456789"),
            //LockedUser
            All.$(ErrorCode.LOCKED_USER, Constant.AVAILABLE_FLAG_VALUE.DISABLE_FLAG, ParamKey.FLAG, "email@gmail.com",
            "email@gmail.com", "123456789", "123456789", "123456789"),
            All.$(ErrorCode.LOCKED_USER, Constant.FLAG.OFF, UserdbKey.USER.FINISH_REGISTER_FLAG, "email@gmail.com",
            "email@gmail.com", "123456789", "123456789", "123456789"),
            All.$(ErrorCode.LOCKED_USER, Constant.FLAG.OFF, UserdbKey.USER.UPDATE_EMAIL_FLAG, "email@gmail.com",
            "email@gmail.com", "123456789", "123456789", "123456789"),
            All.$(ErrorCode.LOCKED_USER, Constant.FLAG.OFF, UserdbKey.USER.VERIFICATION_FLAG, "email@gmail.com",
            "email@gmail.com", "123456789", "123456789", "123456789"),
            //invalid email
All.$(ErrorCode.EMAIL_NOT_FOUND, Constant.FLAG.OFF, null, "email@gmail.com",
            "emailcom", "123456789", "123456789", "123456789"),
            // email not found
All.$(ErrorCode.EMAIL_NOT_FOUND, Constant.FLAG.OFF, null, "email@gmail.com",
            "emailcom@gmail.com", "123456789", "123456789", "123456789"),
            // invalid passs
All.$(ErrorCode.INVALID_PASSWORD, Constant.FLAG.OFF, null, "email@gmail.com",
            "email@gmail.com", "wer", "123456789", "123456789"),
            //wrong verification_code
All.$(ErrorCode.WRONG_VERIFICATION_CODE, Constant.FLAG.OFF, null, "email@gmail.com",
            "email@gmail.com", "wesfdsfsfsd", "1234567", "12345689"),
        };

    }
}