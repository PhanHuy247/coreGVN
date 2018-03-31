/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test.user;

import java.io.IOException;
import java.util.List;
import eazycommon.constant.API;
import test.Actor;
import test.db.userdb.FavoristDB;
import test.db.userdb.FavoristedDB;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.ParamKey;
import eazycommon.exception.EazyException;
import static org.testng.AssertJUnit.assertEquals;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import test.All;
import test.Userdb;
import com.vn.ntsc.backend.entity.impl.user.ListSimpleUserData;
import com.vn.ntsc.backend.entity.impl.user.SimpleUser;
import com.vn.ntsc.backend.server.apimanagement.APIManager;
import com.vn.ntsc.backend.server.respond.result.EntityRespond;

/**
 *
 * @author duyetpt
 */
public class ListConnectionApiTest {

    EntityRespond respond;

    public ListConnectionApiTest() {
    }

    @BeforeMethod
    public void setUp() {
    }

    @AfterMethod
    public void tearDown() {
        FavoristDB.removeFavoristById(Actor.userId);
        FavoristedDB.removeFavoristedById(Actor.userId);
        Actor.removeUser();
    }

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    // @Test
    // public void hello() {}
    /**
     *
     * @param count
     * @param type : 2-favorist, 3-favoristed
     * @throws IOException
     */
    public void initData(int count, int type) throws IOException, EazyException {
        Actor.userId = Userdb.registMale();
        Actor.registListUser(count);
        if (type == 2) {
            for (String id : Actor.listId) {
                FavoristDB.addFavorist(Actor.userId, id);
            }
        } else {
            for (String id : Actor.listId) {
                FavoristedDB.addFavoristed(Actor.userId, id);
            }
        }

        All.put(ParamKey.ID, Actor.userId);
        All.put(ParamKey.TYPE, (long) type);
        All.api = APIManager.getApi(API.LIST_CONNECTION);
    }

    public boolean containData(List<SimpleUser> list) {
        for (Object user : list) {
            SimpleUser u = (SimpleUser) user;
            if (!Actor.listId.contains(u.userId)) {
                return false;
            }
        }
        return true;
    }

    @Test
    public void test_getFavoristList() throws IOException, EazyException {
        initData(10, 2);
        respond = (EntityRespond) All.execute();
        assertEquals("ok", ErrorCode.SUCCESS, respond.code);
        ListSimpleUserData data = (ListSimpleUserData) respond.data;
        assertEquals("ok", true, containData(data.ll));
    }

    @Test
    public void test_getFavoristedList() throws IOException, EazyException {
        initData(10, 3);
        respond = (EntityRespond) All.execute();
        assertEquals("ok", ErrorCode.SUCCESS, respond.code);
        ListSimpleUserData data = (ListSimpleUserData) respond.data;
        assertEquals("ok", true, containData(data.ll));
    }
}
