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
public class AddFreePointData {
    
    public static final String DATA_NAME = "data";
    //type, point
    @DataProvider(name = DATA_NAME)
    public static Object[][] provideData(){
        return new Object[][]{
//          All.$(1, - 100, ErrorCode.WRONG_DATA_FORMAT),
//          All.$(1, 100, ErrorCode.SUCCESS),
          All.$(2, 100, ErrorCode.SUCCESS),
//          All.$(1, 0, ErrorCode.SUCCESS),
        };
    }
}
