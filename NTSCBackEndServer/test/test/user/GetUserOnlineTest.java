/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test.user;

import java.io.IOException;
import java.util.List;
import eazycommon.constant.API;
import org.json.simple.JSONArray;
import test.Actor;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.ParamKey;
import static org.testng.AssertJUnit.assertEquals;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import test.All;
import test.Userdb;
import com.vn.ntsc.backend.entity.impl.datarespond.SizedListData;
import com.vn.ntsc.backend.entity.impl.user.SimpleUser;
import com.vn.ntsc.backend.server.apimanagement.APIManager;
import com.vn.ntsc.backend.server.respond.result.EntityRespond;

/**
 *
 * @author duyetpt
 */
public class GetUserOnlineTest {

    EntityRespond respond;

    public GetUserOnlineTest() {
    }

    @BeforeMethod
    public void setUp() {
    }

    @AfterMethod
    public void tearDown() {
        Actor.removeUser();
    }

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    // @Test
    // public void hello() {}
    public void initData(int total) throws IOException {
        Actor.userId = Userdb.registMale();
        String id = null;
        for (int i = 0; i < 10; i++) {
            id = Userdb.registMale();
            Actor.listId.add(id);
        }

        All.put(ParamKey.TOTAL, (long) total);
        JSONArray arr = new JSONArray();
        arr.addAll(Actor.listId);

        All.put(ParamKey.FRIEND_LIST, arr);
        All.api = APIManager.getApi(API.GET_USER_ONLINE);
    }

    public boolean containData(List<Object> list) {
        for (Object user : list) {
            SimpleUser u = (SimpleUser) user;
            if (!Actor.listId.contains(u.userId)) {
                return false;
            }
        }
        return true;
    }

    @Test
    public void test() throws IOException {
        initData(8);

        respond = (EntityRespond) All.execute();
        assertEquals("ok", ErrorCode.SUCCESS, respond.code);
        SizedListData data = (SizedListData) respond.data;
        assertEquals("ok", true, containData(data.ll));
    }
}
