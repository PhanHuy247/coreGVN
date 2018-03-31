/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package usermanagement.provider;

import eazycommon.constant.Constant;
import org.testng.annotations.DataProvider;
import usermanagement.All;

/**
 *
 * @author duyetpt
 */
public class LoginDataPro {

    public static final String UNFINISH_REGIST = "unfinishRe";

    @DataProvider(name = UNFINISH_REGIST)
    public static Object[][] provideDataUnfinish() {
        return new Object[][]{
            All.$(Constant.GENDER.MALE, null, null),
            All.$(Constant.GENDER.FEMALE, null, null),
            All.$(Constant.GENDER.MALE, "fbId2345", null),};
    }
    //int gender, String fbId, int flagType, boolean isVerify, Long lastOnline
    public static final String FINISHED_REGISTER = "finished_register";

    @DataProvider(name = FINISHED_REGISTER)
    public static Object[][] provideDatafinished() {
        return new Object[][]{
            // first login in day
            All.$(Constant.GENDER.MALE, null, All.UPDATE_FOR_FINISH_REGISTED, true, System.currentTimeMillis()),
            // second login in day
            All.$(Constant.GENDER.MALE, null, All.UPDATE_FOR_FINISH_REGISTED, true, null),
            // only finished regist, not verification
            All.$(Constant.GENDER.FEMALE, null, All.UPDATE_FOR_FLAG, false, null),
            // first login, all is finished and verification
            All.$(Constant.GENDER.FEMALE, null, All.UPDATE_ALL, true, System.currentTimeMillis()),
            // second login, all is finished and verification
            All.$(Constant.GENDER.FEMALE, null, All.UPDATE_ALL, true, null),};
    }

}
