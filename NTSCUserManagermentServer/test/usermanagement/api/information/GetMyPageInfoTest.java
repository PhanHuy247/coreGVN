/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package usermanagement.api.information;

import java.io.IOException;
import org.json.simple.JSONObject;
import org.testng.annotations.Test;
import DAOTest.userdb.Userdb;
import eazycommon.constant.API;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.ParamKey;
import static org.testng.AssertJUnit.assertEquals;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import usermanagement.All;
import com.vn.ntsc.usermanagementserver.entity.impl.datarespond.MyPageData;
import com.vn.ntsc.usermanagementserver.server.respond.impl.information.GetMyPageInfor;
import com.vn.ntsc.usermanagementserver.server.respond.result.EntityRespond;

/**
 *
 * @author Admin
 */
public class GetMyPageInfoTest {

    String id;
    JSONObject obj;

    public GetMyPageInfoTest() {
    }

    @BeforeMethod
    public void setUp() {
        obj = new JSONObject();
    }

    @AfterMethod
    public void tearDown() {
        Userdb.removeUserById(id);
    }

    @Test
    public void test() throws IOException {
        obj = Userdb.initRegistUser(Long.valueOf(1), Long.valueOf(1));
        obj.put("bckstg_num", 10);
        id = Userdb.registUser(obj);
        obj.put(ParamKey.API_NAME, API.GET_MY_PAGE_INFOR);
        obj.put(ParamKey.USER_ID, id);

        GetMyPageInfor api = new GetMyPageInfor();
        EntityRespond en = (EntityRespond) api.execute(obj, All.getTime());
        assertEquals(ErrorCode.SUCCESS, en.code);
        MyPageData data = (MyPageData) en.data;

        assertEquals(Long.valueOf(0), data.backstageNumber);
        assertEquals(Long.valueOf(0), data.buzzNumber);
        assertEquals(Integer.valueOf(0), data.checkoutNum);
        assertEquals(Long.valueOf(0), data.favouritedNumber);
        assertEquals(Long.valueOf(0), data.notiLikeNumber);
    }
}
