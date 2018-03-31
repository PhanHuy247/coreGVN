/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test.user;

import java.io.IOException;
import java.util.List;
import eazycommon.constant.API;
import eazycommon.constant.Constant;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.ParamKey;
import eazycommon.constant.mongokey.UserdbKey;
import eazycommon.exception.EazyException;
import org.json.simple.JSONObject;
import static org.testng.AssertJUnit.assertEquals;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import test.Actor;
import test.All;
import test.Userdb;
import com.vn.ntsc.backend.entity.impl.datarespond.SizedListData;
import com.vn.ntsc.backend.entity.impl.user.User;
import com.vn.ntsc.backend.server.apimanagement.APIManager;
import com.vn.ntsc.backend.server.respond.result.EntityRespond;

/**
 *
 * @author duyetpt
 */
public class SearchConnectionApiTest {

    EntityRespond respond;
    List<String> listFb;

    public SearchConnectionApiTest() {
    }

    @BeforeMethod
    public void setUp() {
    }

    @AfterMethod
    public void tearDown() {
        Userdb.removeUserByTestFlag();
        All.obj = new JSONObject();
    }

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    // @Test
    // public void hello() {}
    public void initData() throws IOException {

        All.put(ParamKey.ORDER, (long) Constant.FLAG.ON);
        All.put(ParamKey.SORT, (long) Constant.FLAG.ON);
        All.put(ParamKey.SKIP, (long) 0);
        All.put(ParamKey.TAKE, (long) 10);
        All.api = APIManager.getApi(API.SEARCH_CONNECTION);
    }

    @Test
    public void test_useID_Specify() throws IOException {
        Actor.userId = Userdb.registMale();
        All.put(ParamKey.ID, Actor.userId);
        initData();
        respond = (EntityRespond) All.execute();
        assertEquals("ok", ErrorCode.SUCCESS, respond.code);

        SizedListData data = (SizedListData) respond.data;
        List<User> llUser = data.ll;
        assertEquals("ok", Actor.userId, llUser.get(0).userId);
    }

    public boolean containtList(List<String> expects, List<User> actuals) {
        for (User user : actuals) {
            if (!expects.contains(user.userId)) {
                return false;
            }
        }
        return true;
    }

    @Test
    public void test_userType_Email() throws IOException {
        Actor.registListUser(7);
        listFb = Actor.registListUserByFb(5);
        All.put(ParamKey.USER_TYPE, (long) Constant.ACCOUNT_TYPE_VALUE.EMAIL_TYPE);
        initData();
        respond = (EntityRespond) All.execute();
        assertEquals("ok", ErrorCode.SUCCESS, respond.code);

        SizedListData data = (SizedListData) respond.data;
        List<User> llUser = data.ll;
        assertEquals("ok", true, containtList((List<String>) Actor.listId, llUser));
        assertEquals("ok", false, containtList((List<String>) listFb, llUser));
    }

    @Test
    public void test_userType_FB() throws IOException {
        Actor.registListUser(7);
        listFb = Actor.registListUserByFb(5);
        All.put(ParamKey.USER_TYPE, (long) Constant.ACCOUNT_TYPE_VALUE.FB_TYPE);
        initData();
        respond = (EntityRespond) All.execute();
        assertEquals("ok", ErrorCode.SUCCESS, respond.code);

        SizedListData data = (SizedListData) respond.data;
        List<User> llUser = data.ll;
        assertEquals("ok", false, containtList((List<String>) Actor.listId, llUser));
        assertEquals("ok", true, containtList(listFb, llUser));
    }

    @Test
    public void test_userType_null() throws IOException {
        Actor.registListUser(7);
        listFb = Actor.registListUserByFb(5);
        initData();
        respond = (EntityRespond) All.execute();
        assertEquals("ok", ErrorCode.SUCCESS, respond.code);

        SizedListData data = (SizedListData) respond.data;
        List<User> llUser = data.ll;
        assertEquals("ok", 10, data.ll.size());
    }

    /**
     * Actor.listId : include user has favoristNum > from listFb : include user
     * has favoristNum < from
     */
    @Test
    public void test_from_favoristNum() throws IOException, EazyException {
        listFb = Actor.registListUserByFb(5);
        Actor.registListUser(7);
        for (String id : Actor.listId) {
            Userdb.updateNumberOf(id, UserdbKey.USER.FAVOURIST_NUMBER, 10);
        }

        All.put("from_fav", (long) 4);
        initData();
        respond = (EntityRespond) All.execute();
        assertEquals("ok", ErrorCode.SUCCESS, respond.code);

        SizedListData data = (SizedListData) respond.data;
        List<User> llUser = data.ll;
        assertEquals("ok", 7, data.ll.size());
        assertEquals("ok", true, containtList((List<String>) Actor.listId, llUser));
        assertEquals("ok", false, containtList((List<String>) listFb, llUser));

    }

    @Test
    public void test_to_favoristNum() throws IOException, EazyException {
        listFb = Actor.registListUserByFb(5);
        Actor.registListUser(7);
        for (String id : Actor.listId) {
            Userdb.updateNumberOf(id, UserdbKey.USER.FAVOURIST_NUMBER, 3);
        }
        for (String id : listFb) {
            Userdb.updateNumberOf(id, UserdbKey.USER.FAVOURIST_NUMBER, 10);
        }
        All.put("to_fav", (long) 4);
        initData();
        respond = (EntityRespond) All.execute();
        assertEquals("ok", ErrorCode.SUCCESS, respond.code);

        SizedListData data = (SizedListData) respond.data;
        List<User> llUser = data.ll;
        assertEquals("ok", 7, data.ll.size());
        assertEquals("ok", true, containtList((List<String>) Actor.listId, llUser));
        assertEquals("ok", false, containtList((List<String>) listFb, llUser));
    }

    @Test
    public void test_toFrom_favoristNum() throws IOException, EazyException {
        listFb = Actor.registListUserByFb(5);
        Actor.registListUser(7);
        for (String id : Actor.listId) {
            Userdb.updateNumberOf(id, UserdbKey.USER.FAVOURIST_NUMBER, 3);
        }
        for (String id : listFb) {
            Userdb.updateNumberOf(id, UserdbKey.USER.FAVOURIST_NUMBER, 7);
        }
        All.put("to_fav", (long) 10);
        All.put("from_fav", (long) 4);
        initData();
        respond = (EntityRespond) All.execute();
        assertEquals("ok", ErrorCode.SUCCESS, respond.code);

        SizedListData data = (SizedListData) respond.data;
        List<User> llUser = data.ll;
        assertEquals("ok", 5, data.ll.size());
        assertEquals("ok", false, containtList((List<String>) Actor.listId, llUser));
        assertEquals("ok", true, containtList((List<String>) listFb, llUser));
    }

    @Test
    public void test_from_favoristedNum() throws IOException, EazyException {
        listFb = Actor.registListUserByFb(5);
        Actor.registListUser(7);
        for (String id : Actor.listId) {
            Userdb.updateNumberOf(id, UserdbKey.USER.FAVOURITED_NUMBER, 10);
        }

        All.put("from_fvt", (long) 4);
        initData();
        respond = (EntityRespond) All.execute();
        assertEquals("ok", ErrorCode.SUCCESS, respond.code);

        SizedListData data = (SizedListData) respond.data;
        List<User> llUser = data.ll;
        assertEquals("ok", 7, data.ll.size());
        assertEquals("ok", true, containtList((List<String>) Actor.listId, llUser));
        assertEquals("ok", false, containtList((List<String>) listFb, llUser));

    }

    @Test
    public void test_to_favoristedNum() throws IOException, EazyException {
        listFb = Actor.registListUserByFb(5);
        Actor.registListUser(7);
        for (String id : Actor.listId) {
            Userdb.updateNumberOf(id, UserdbKey.USER.FAVOURITED_NUMBER, 3);
        }
        for (String id : listFb) {
            Userdb.updateNumberOf(id, UserdbKey.USER.FAVOURITED_NUMBER, 10);
        }
        All.put("to_fvt", (long) 4);
        initData();
        respond = (EntityRespond) All.execute();
        assertEquals("ok", ErrorCode.SUCCESS, respond.code);

        SizedListData data = (SizedListData) respond.data;
        List<User> llUser = data.ll;
        assertEquals("ok", 7, data.ll.size());
        assertEquals("ok", true, containtList((List<String>) Actor.listId, llUser));
        assertEquals("ok", false, containtList((List<String>) listFb, llUser));
    }

    @Test
    public void test_toFrom_favoristedNum() throws IOException, EazyException {
        listFb = Actor.registListUserByFb(5);
        Actor.registListUser(7);
        for (String id : Actor.listId) {
            Userdb.updateNumberOf(id, UserdbKey.USER.FAVOURITED_NUMBER, 3);
        }
        for (String id : listFb) {
            Userdb.updateNumberOf(id, UserdbKey.USER.FAVOURITED_NUMBER, 7);
        }
        All.put("to_fvt", (long) 10);
        All.put("from_fvt", (long) 4);
        initData();
        respond = (EntityRespond) All.execute();
        assertEquals("ok", ErrorCode.SUCCESS, respond.code);

        SizedListData data = (SizedListData) respond.data;
        List<User> llUser = data.ll;
        assertEquals("ok", 5, data.ll.size());
        assertEquals("ok", false, containtList((List<String>) Actor.listId, llUser));
        assertEquals("ok", true, containtList((List<String>) listFb, llUser));
    }

    @Test
    public void test_name() throws IOException {
        Actor.registListUser(7);
        listFb = Actor.registListUserByFb(5);
        All.put(ParamKey.USER_NAME, "fb_name");
        initData();
        respond = (EntityRespond) All.execute();
        assertEquals("ok", ErrorCode.SUCCESS, respond.code);

        SizedListData data = (SizedListData) respond.data;
        List<User> llUser = data.ll;
        assertEquals("ok", 5, llUser.size());
        assertEquals("ok", true, containtList(listFb, llUser));
        assertEquals("ok", false, containtList((List<String>) Actor.listId, llUser));
    }
}
