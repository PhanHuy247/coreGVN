/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package usermanagement.api.information;

import java.io.IOException;
import java.util.ArrayList;
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
import static org.testng.AssertJUnit.assertNotNull;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import usermanagement.Actor;
import com.vn.ntsc.usermanagementserver.dao.impl.CheckOutDAO;
import com.vn.ntsc.usermanagementserver.dao.impl.FavoritedDAO;
import com.vn.ntsc.usermanagementserver.entity.impl.datarespond.GetAttentionNumberData;
import com.vn.ntsc.usermanagementserver.entity.impl.user.User;
import com.vn.ntsc.usermanagementserver.server.respond.APIManager;
import com.vn.ntsc.usermanagementserver.server.respond.result.EntityRespond;
import com.vn.ntsc.usermanagementserver.server.respond.result.ListEntityRespond;

/**
 *
 * @author duyetpt
 */
public class GetAttentionNumberTest {

    EntityRespond respond;
    List<String> list;

    public GetAttentionNumberTest() {
    }

    @BeforeMethod
    public void setUp() {
        list = new ArrayList<String>();
    }

    @AfterMethod
    public void tearDown() {
        for (String id : list) {
            Userdb.removeUserById(id);
        }
       // Userdb.removeCheckOutById(Actor.userId);
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
            list.add(id);
            CheckOutDAO.addCheckOut(Actor.userId, id, Util.getGMTTime());
            FavoritedDAO.addFavourited(Actor.userId, id, Util.currentTime());
        }

        All.put(ParamKey.USER_ID, Actor.userId);
       All.api = APIManager.getApi(API.GET_ATTENTION_NUMBER);
    }
    @Test
    public void test() throws IOException, EazyException, InterruptedException {
        initData(5);
        All.put(ParamKey.USER_ID, Actor.userId);
        
        respond = (EntityRespond) All.execute();
        assertEquals("ok", ErrorCode.SUCCESS, respond.code);
        GetAttentionNumberData data = (GetAttentionNumberData) respond.data;
        assertNotNull(data.checkoutNum);
        assertEquals("ok", new Integer(5), data.checkoutNum);
        assertEquals("ok", new Integer(5), data.newCheckoutNum);
        assertNotNull(data.favouritedNumber);
         assertEquals("ok", new Integer(5), data.favouritedNumber);
        assertEquals("ok", new Integer(5), data.newFavoritedNumber);
    }
    
    
     @Test
    public void test_get() throws IOException, EazyException, InterruptedException {
        initData(5);
        All.put(ParamKey.USER_ID, Actor.userId);
        All.api = APIManager.getApi(API.LIST_CHECK_OUT);
        All.put(ParamKey.SKIP, (long)0);
        All.put(ParamKey.TAKE, (long)10);
        ListEntityRespond result = (ListEntityRespond) All.execute();
        List<User> data1 = (List<User>) result.data;
        assertEquals("ok", 5, data1.size());
       
        All.api = APIManager.getApi(API.GET_ATTENTION_NUMBER);
        respond = (EntityRespond) All.execute();
        assertEquals("ok", ErrorCode.SUCCESS, respond.code);
        GetAttentionNumberData data = (GetAttentionNumberData) respond.data;
        assertNotNull(data.checkoutNum);
        assertEquals("ok", new Integer(5), data.checkoutNum);
        assertEquals("ok", new Integer(0), data.newCheckoutNum);
        assertNotNull(data.favouritedNumber);
         assertEquals("ok", new Integer(5), data.favouritedNumber);
        assertEquals("ok", new Integer(5), data.newFavoritedNumber);
    }

}
