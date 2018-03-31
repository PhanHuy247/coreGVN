/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package usermanagement.api.connectivity;

import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import org.testng.annotations.Test;
import usermanagement.All;
import DAOTest.userdb.Userdb;
import eazycommon.constant.API;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.ParamKey;
import eazycommon.exception.EazyException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import static org.testng.AssertJUnit.assertEquals;
import usermanagement.Actor;
import com.vn.ntsc.usermanagementserver.dao.impl.FavoristDAO;
import com.vn.ntsc.usermanagementserver.entity.impl.user.User;
import com.vn.ntsc.usermanagementserver.server.respond.APIManager;
import com.vn.ntsc.usermanagementserver.server.respond.result.ListEntityRespond;

/**
 *
 * @author duyetpt
 */
public class ListFavoristTest {

    ListEntityRespond respond;

    public ListFavoristTest() {
    }

    @BeforeMethod
    public void setUp() {
    }

    @AfterMethod
    public void tearDown() throws EazyException {
        Userdb.removeFavoristById(Actor.userId);
        Actor.removeUser();
    }

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    // @Test
    // public void hello() {}
    public void initData(int count) throws IOException, EazyException, InterruptedException {
        Actor.userId = Userdb.registFemale();
        String id = null;
        for (int i = 0; i < count; i++) {
            id = Userdb.registMale();
            Userdb.updateUserName(id, "user" + i % 11);
            Actor.listPartnerId.add(id);
            FavoristDAO.addFavorist(Actor.userId, id);
            Thread.sleep(10);
        }

        All.put(ParamKey.USER_ID, Actor.userId);
        All.api = APIManager.getApi(API.LIST_FAVOURIST);
    }

    @Test
    public void test() throws IOException, EazyException, InterruptedException {
        int count = 20;
        initData(count);
        All.put(ParamKey.SKIP, (long) 0);
        All.put(ParamKey.TAKE, (long) 10);

        respond = (ListEntityRespond) All.execute();
        assertEquals("ok", ErrorCode.SUCCESS, respond.code);
        List<User> data = respond.data;
        List<User> result = respond.data;
        Collections.sort(result, new CompareByName());
        assertEquals(result.toArray(), data.toArray());
        for (User fvt : data) {
            System.out.println(fvt.toJsonObject().toJSONString());
        }
    }
}

class CompareByName implements Comparator<User> {

    public int compare(User o1, User o2) {
        return o1.username.compareTo(o2.username);
    }

}
