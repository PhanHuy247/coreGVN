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
public class RegistAdminDataPro {

    //String email, String pass, String name, String role_id, int flag
    public static final String DATA_NAME = "data";

    @DataProvider(name = DATA_NAME)
    public static Object[][] provideData() {
        return new Object[][]{
            // success
            All.$("adminNtq", "123456789", "admin_name", "role_id12", (long)1, ErrorCode.SUCCESS),
            //email is invalid
            All.$("adminNtq@gmail.com", "123456789", "admin_name", "role_id12", (long)1, 4),
            All.$("", "123456789", "admin_name", "role_id12", (long)1, 4),
            All.$(null, "123456789", "admin_name", "role_id12", (long)1, 4),
            //name is invalid
            All.$("adminNtq", "123456789", "", "role_id12", (long)1, 6),
            All.$("adminNtq", "123456789", null, "role_id12", (long)1, 6),
            //pass is invalid
            All.$("adminNtq", "", "name_name", "role_id12", (long)1, 5),
            All.$("adminNtqm", null, "name_name", "role_id12", (long)1, 5),
            //role_id is invalid
            All.$("adminNtq", "123dadsa", "name_name", "", (long)1, 7),
            All.$("adminNtqcom", "123dadsa", "name_name", null, (long)1, 7),
            // flag is empty
            All.$("adminNtqcom", "123dadsa", "name_name", "role_id384", null, 8),
//            All.$("adminNtqcom", "123dadsa", "name_name", "role_id384", "", 8),
        };
    }
    
    public static final String DATA_UPDATE = "update";

    @DataProvider(name = DATA_UPDATE)
    public static Object[][] provideDataUpdate() {
        return new Object[][]{
            // success
//            All.$("adminNtq", "123456789", "admin_name", "role_id12", (long)1, ErrorCode.SUCCESS),
            //email registed
            All.$("adminNtq", "123456789", "admin_name", "role_id12", (long)1, ErrorCode.EMAIL_REGISTED),
            //email is invalid
//            All.$("adminNtq@gmail.com", "123456789", "admin_name", "role_id12", (long)1, 4),
//            All.$("", "123456789", "admin_name", "role_id12", (long)1, 4),
//            All.$(null, "123456789", "admin_name", "role_id12", (long)1, 4),
//            //name is invalid
//            All.$("adminNtq", "123456789", "", "role_id12", (long)1, 6),
//            All.$("adminNtq", "123456789", null, "role_id12", (long)1, 6),
//            //pass is invalid
//            All.$("adminNtq", "", "name_name", "role_id12", (long)1, 5),
//            All.$("adminNtqm", null, "name_name", "role_id12", (long)1, 5),
//            //role_id is invalid
//            All.$("adminNtq", "123dadsa", "name_name", "", (long)1, 7),
//            All.$("adminNtqcom", "123dadsa", "name_name", null, (long)1, 7),
//            // flag is empty
//            All.$("adminNtqcom", "123dadsa", "name_name", "role_id384", null, 8),
//            All.$("adminNtqcom", "123dadsa", "name_name", "role_id384", "", 8),
        };
    }
}
