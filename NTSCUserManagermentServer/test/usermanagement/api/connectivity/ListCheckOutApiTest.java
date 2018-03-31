/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package usermanagement.api.connectivity;

import java.io.IOException;
import java.util.List;
import org.testng.annotations.Test;
import usermanagement.All;
import DAOTest.userdb.Userdb;
import eazycommon.constant.API;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.ParamKey;
import eazycommon.exception.EazyException;
import eazycommon.util.Util;
import static org.testng.AssertJUnit.assertEquals;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import usermanagement.Actor;
import com.vn.ntsc.usermanagementserver.dao.impl.CheckOutDAO;
import com.vn.ntsc.usermanagementserver.entity.impl.user.FollowingUser;
import com.vn.ntsc.usermanagementserver.server.respond.APIManager;
import com.vn.ntsc.usermanagementserver.server.respond.result.ListEntityRespond;

/**
 *
 * @author duyetpt
 */
public class ListCheckOutApiTest {

    ListEntityRespond respond;

    public ListCheckOutApiTest() {
    }

    @BeforeMethod
    public void setUp() {
    }

    @AfterMethod
    public void tearDown() {
        Userdb.removeCheckOutById(Actor.userId);
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
            Actor.listPartnerId.add(id);
            CheckOutDAO.addCheckOut(Actor.userId, id, Util.getGMTTime());
            Thread.sleep(10);
        }

        All.put(ParamKey.USER_ID, Actor.userId);
        All.api = APIManager.getApi(API.LIST_CHECK_OUT);
    }
    
    @Test
    public void test_EmptyData() throws IOException, EazyException, InterruptedException {
        int count = 0;
        initData(count);
        int skip = 0;
        int take = 24;
        All.put(ParamKey.SKIP, (long) skip);
        All.put(ParamKey.TAKE, (long) take);
        respond = (ListEntityRespond) All.execute();
        assertEquals("ok", ErrorCode.SUCCESS, respond.code);
        List<FollowingUser> data = respond.data;
        int max = take + skip;
        if(take + skip > count ) max = count;
        for (int i = skip; i < max; i++) {
            System.out.println("i :" + i);
            assertEquals("ok", Actor.listPartnerId.get(count - i - 1), data.get(i - skip).userId);
        }
    }
    
    @Test
    public void test() throws IOException, EazyException, InterruptedException {
        int count = 30;
        initData(count);
        int skip = 20;
        int take = 27;
        All.put(ParamKey.SKIP, (long) skip);
        All.put(ParamKey.TAKE, (long) take);
        respond = (ListEntityRespond) All.execute();
        assertEquals("ok", ErrorCode.SUCCESS, respond.code);
        List<FollowingUser> data = respond.data;
        int max = take + skip;
        if(take + skip > count ) max = count;
        for (int i = skip; i < max; i++) {
            System.out.println("i :" + i);
            assertEquals("ok", Actor.listPartnerId.get(count - i - 1), data.get(i - skip).userId);
        }
    }
}
