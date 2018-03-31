/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package usermanagement.provider;

import eazycommon.constant.ErrorCode;
import org.testng.annotations.DataProvider;
import usermanagement.All;

/**
 *
 * @author duyetpt
 */
public class ChangeEmailDataProvider {
     @DataProvider(name = "first")
    public static Object[][] firstData() {
        return new Object[][]{
            All.$("email@gmail.com", "12345678", ErrorCode.SUCCESS),
            //case 2: email null
            All.$(null, "12345678", ErrorCode.INVALID_EMAIL),
            //case 3: invalid email
            All.$("abcdsjlaksj", "12345678", ErrorCode.INVALID_EMAIL),
            //case 4: email registed
            All.$("email1@gmail.com", "12345678", ErrorCode.EMAIL_REGISTED),
            // case 5 : password null
            All.$("email2@gmail.com", null, ErrorCode.INVALID_PASSWORD),
            //case 6 : password invalid (length < 6)
            All.$("email2@gmail.com", "12", ErrorCode.INVALID_PASSWORD)
        };
    }
    
     @DataProvider(name = "second")
    public static Object[][] secondData() {
        return new Object[][]{
            All.$("email@gmail.com", "email2@gmail.com", "12345678", null, ErrorCode.SUCCESS, ErrorCode.SUCCESS),
//            case 2: email null
            All.$("email@gmail.com", null, "12345678", null, ErrorCode.SUCCESS, ErrorCode.INVALID_EMAIL),
//            case : email registed by other
            All.$("email@gmail.com", "email2@gmail.com", "12345678", null, ErrorCode.SUCCESS, ErrorCode.EMAIL_REGISTED),
//            case : email registed by own
            All.$("email@gmail.com", "email@gmail.com", "12345678", null, ErrorCode.SUCCESS, ErrorCode.SUCCESS),
//             case :old_pass fail
            All.$("email@gmail.com", "email2@gmail.com", "12345678", "123456", ErrorCode.SUCCESS, ErrorCode.INCORRECT_PASSWORD),
            //case : new pass invalid
            All.$("email@gmail.com", "email2@gmail.com", "12", null, ErrorCode.SUCCESS, ErrorCode.INVALID_PASSWORD),
            All.$("email@gmail.com", "email2@gmail.com", null, null, ErrorCode.SUCCESS, ErrorCode.INVALID_PASSWORD),
        };
    }

}
