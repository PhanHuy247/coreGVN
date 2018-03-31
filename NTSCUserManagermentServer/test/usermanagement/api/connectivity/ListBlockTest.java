/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package usermanagement.api.connectivity;

import java.io.IOException;
import org.testng.annotations.Test;
import usermanagement.All;
import DAOTest.userdb.Userdb;
import eazycommon.constant.API;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.ParamKey;
import eazycommon.exception.EazyException;
import static org.testng.AssertJUnit.assertEquals;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import usermanagement.Actor;
import com.vn.ntsc.usermanagementserver.dao.impl.BlockDAO;
import com.vn.ntsc.usermanagementserver.entity.impl.user.User;
import com.vn.ntsc.usermanagementserver.server.respond.APIManager;
import com.vn.ntsc.usermanagementserver.server.respond.result.ListEntityRespond;

/**
 *
 * @author duyetpt
 */
public class ListBlockTest {

    ListEntityRespond respond;

    public ListBlockTest() {
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
    public void initData(int skip, int take) throws IOException {
        Actor.userId = Userdb.registFemale();

        All.put(ParamKey.USER_ID, Actor.userId);
        All.put(ParamKey.SKIP, new Long(skip));
        All.put(ParamKey.TAKE, new Long(take));

        All.api = APIManager.getApi(API.LIST_BLOCK);
    }

    @Test
    public void test_empty() throws IOException {
        initData(0, 0);
        respond = (ListEntityRespond) All.execute();
        assertEquals("ok", ErrorCode.SUCCESS, respond.code);
        assertEquals("ok", 0, respond.data.size());
    }

    @Test
    public void test() throws IOException, EazyException {
        initData(0, 10);
        String id = null;
        for (int i = 0; i < 15; i++) {
            id = Userdb.registFemale();
            Actor.listBlack.add(id);
            BlockDAO.addBlockList(Actor.userId, id);
        }

        respond = (ListEntityRespond) All.execute();
        assertEquals("ok", ErrorCode.SUCCESS, respond.code);
        assertEquals("ok", 10, respond.data.size());

        int i = 0;
        for (Object obj : respond.data) {
            User user = (User) obj;
            assertEquals("ok", Actor.listBlack.get(i), user.userId);
            i++;
        }

        for (String id1 : Actor.listBlack) {
            BlockDAO.removeBlock(Actor.userId, id1);
        }
    }

    @Test
    public void test1() throws IOException, EazyException {
        initData(2, 10);
        String id = null;
        for (int i = 0; i < 15; i++) {
            id = Userdb.registFemale();
            Actor.listBlack.add(id);
            BlockDAO.addBlockList(Actor.userId, id);
        }

        respond = (ListEntityRespond) All.execute();
        assertEquals("ok", ErrorCode.SUCCESS, respond.code);
        assertEquals("ok", 10, respond.data.size());

        int i = 2;
        for (Object obj : respond.data) {
            User user = (User) obj;
            assertEquals("ok", Actor.listBlack.get(i), user.userId);
            i++;
        }
        for (String id1 : Actor.listBlack) {
            BlockDAO.removeBlock(Actor.userId, id1);
        }
    }
}
