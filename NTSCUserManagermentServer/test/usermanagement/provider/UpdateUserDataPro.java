/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package usermanagement.provider;

import DAOTest.userdb.Userdb;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import eazycommon.constant.API;
import eazycommon.constant.Constant;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.ParamKey;
import eazycommon.constant.mongokey.UserdbKey;
import org.json.simple.JSONArray;
import org.testng.annotations.DataProvider;
import usermanagement.Actor;
import usermanagement.All;

/**
 *
 * @author duyetpt
 */
public class UpdateUserDataPro {

    public static int count = 0;
    public static final String FIRST_UPDATE = "first";

    public static Map<String, Object> initUser(int gender, String userName, String api) throws IOException {
        Map<String, Object> maps = new HashMap<>();
        String id;
        if (gender == Constant.GENDER.FEMALE) {
            id = Userdb.registFemale();
        } else {
            id = Userdb.registMale();
        }
        Actor.listPartnerId.add(id);
        maps.put(ParamKey.USER_ID, id);
        maps.put(ParamKey.REQUEST_USER_ID, id);
        maps.put(UserdbKey.USER.GENDER, eazycommon.constant.Constant.GENDER.FEMALE);
        maps.put(ParamKey.USER_NAME, userName);
        maps.put(ParamKey.API_NAME, api);
        maps.put("auto_region", (long) 1);
        maps.put("region", (long) 1);
        maps.put("abt", "about");
        maps.put("del_abt", (long) 0);
        maps.put("bir", "19900101");
        return maps;
    }

    public static Map<String, Object> initFemale(String removeKey, String userName, String api) throws IOException {
        Map<String, Object> maps = initUser(Constant.GENDER.FEMALE, userName, api);
        maps.put("fetish", "fetish");
        maps.put("job", (long) 9);
        maps.put("indecent", (long) 4);
        maps.put("cup", (long) 5);
        maps.put("cute_type", (long) 3);
        maps.put(UserdbKey.USER.JOINT_HOURS, (long) 4);
        maps.put(UserdbKey.USER.TYPEOFMANS, "TYPE OF MAN");
        maps.put("voice_call_waiting", false);
        maps.put("video_call_waiting", true);
        JSONArray arr = new JSONArray();
        arr.add(70);
        arr.add(70);
        arr.add(70);

        maps.put("measurements", arr);
        if (removeKey != null) {
            maps.remove(removeKey);
        }
        return maps;
    }

    public static Map<String, Object> adminUpdateEmail(String email, String api) throws IOException {
        Map<String, Object> maps = initFemale(null, null, api);
        maps.put(ParamKey.EMAIL, email);
        return maps;
    }

    public static Map<String, Object> initMale(String removeKey, String api) throws IOException {
        Map<String, Object> maps = initUser(Constant.GENDER.MALE, "mrs.", api);
        if (removeKey != null) {
            maps.remove(removeKey);
        }
        return maps;
    }

    /**
     * int gender, String apiName, Map<String, Object> maps, int errorCode, int
     * updateEmailFlag, int finishFlag
     *
     * @return
     */
    @DataProvider(name = FIRST_UPDATE)
    public static Object[][] provideData() throws IOException {
        return new Object[][]{
            // case : male, full
            All.$(initMale(null, API.UPDATE_USER_INFOR), ErrorCode.SUCCESS, 0, 1),
            // case : male not full
            All.$(initMale(UserdbKey.USER.HOBBY, API.UPDATE_USER_INFOR), ErrorCode.SUCCESS, 0, 1),
            // case : male full
            All.$(initFemale(null, "n", API.UPDATE_USER_INFOR), ErrorCode.SUCCESS, 0, 1),
            // case : male not full
            All.$(initFemale(UserdbKey.USER.JOB, "n1", API.UPDATE_USER_INFOR), ErrorCode.WRONG_DATA_FORMAT, 0, 0),
            // case : name is null
            All.$(initFemale(null, null, API.UPDATE_USER_INFOR), ErrorCode.WRONG_DATA_FORMAT, 0, 0),
            // not hase region
            All.$(initFemale(UserdbKey.USER.REGION, null, API.UPDATE_USER_INFOR), ErrorCode.WRONG_DATA_FORMAT, 0, 0),
            // case : name is registed
            All.$(initFemale(null, "n", API.UPDATE_USER_INFOR), ErrorCode.DUPLICATE_USER_NAME, 0, 0),
           
            //admin
            //             case : male, full
            All.$(initMale(null, API.UPDATE_USER_INF_BY_ADMIN), ErrorCode.SUCCESS, 0, 1),
            //            // case : male not full
            All.$(initMale(UserdbKey.USER.HOBBY, API.UPDATE_USER_INF_BY_ADMIN), ErrorCode.SUCCESS, 0, 1),
            // case : male full
            All.$(initFemale(null, "admin", API.UPDATE_USER_INF_BY_ADMIN), ErrorCode.SUCCESS, 0, 1), //            // case : male not full
            All.$( initFemale(UserdbKey.USER.JOB, "n1", API.UPDATE_USER_INF_BY_ADMIN), ErrorCode.SUCCESS, 0, 0),
            // case : name is null
            All.$(initFemale(null, null, API.UPDATE_USER_INF_BY_ADMIN), ErrorCode.SUCCESS, 0, 0),
            // not hase region
            All.$(initFemale(UserdbKey.USER.REGION, null, API.UPDATE_USER_INF_BY_ADMIN), ErrorCode.SUCCESS, 0, 0),
            // case : name is registed
            All.$(initFemale(null, "admin", API.UPDATE_USER_INF_BY_ADMIN), ErrorCode.INVALID_USER_NAME, 0, 0),
            //  admin : update email successfully
            All.$(adminUpdateEmail("email@gmail.com", API.UPDATE_USER_INF_BY_ADMIN), ErrorCode.SUCCESS, 1, 0),
            // admin : email is registed
            All.$(adminUpdateEmail("email@gmail.com", API.UPDATE_USER_INF_BY_ADMIN), ErrorCode.EMAIL_REGISTED, 0, 0),
            // admin : update email invalid
            All.$(adminUpdateEmail("emailail.com", API.UPDATE_USER_INF_BY_ADMIN), ErrorCode.WRONG_DATA_FORMAT, 0, 0),
            All.$(adminUpdateEmail("", API.UPDATE_USER_INF_BY_ADMIN), ErrorCode.WRONG_DATA_FORMAT, 0, 0),};

    }
    public static final String UPDATE_MORE = "more";

    @DataProvider(name = UPDATE_MORE)
    public static Object[][] provideMore() throws IOException {

        return new Object[][]{
            // case : male full
            All.$("name1", "name1", initFemale(null, "2n", API.UPDATE_USER_INFOR), ErrorCode.SUCCESS, 0, 1),
            //user name dupli name
            All.$("name", "name1", initFemale(null, "2n", API.UPDATE_USER_INFOR), ErrorCode.DUPLICATE_USER_NAME, 0, 1),
            // admin
            All.$("ad", "ad", initFemale(null, "2n", API.UPDATE_USER_INF_BY_ADMIN), ErrorCode.SUCCESS, 0, 1),
            // invalid user name
            All.$("ad1", "ad", initFemale(null, "2n", API.UPDATE_USER_INF_BY_ADMIN), ErrorCode.INVALID_USER_NAME, 0, 1),};
    }

    public static final String UPDATE_MORE_ADMIN = "more_admin";

    @DataProvider(name = UPDATE_MORE_ADMIN)
    public static Object[][] provideMoreAd() throws IOException {

        return new Object[][]{
            //
            All.$("ad2", "job", adminUpdateEmail("emailemail@gmail.com", API.UPDATE_USER_INF_BY_ADMIN), ErrorCode.WRONG_DATA_FORMAT, 1, 1),
            All.$("ad3", ParamKey.USER_NAME, adminUpdateEmail("emailemail1@gmail.com", API.UPDATE_USER_INF_BY_ADMIN), 6, 1, 1),};
    }
}
