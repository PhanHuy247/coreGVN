/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test.user;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import eazycommon.constant.API;
import eazycommon.constant.Constant;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.ParamKey;
import eazycommon.exception.EazyException;
import static org.testng.AssertJUnit.assertEquals;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import test.Actor;
import test.All;
import test.Userdb;
import test.db.cashdb.TransactionLogDB;
import com.vn.ntsc.backend.entity.impl.datarespond.SizedListData;
import com.vn.ntsc.backend.server.apimanagement.APIManager;
import com.vn.ntsc.backend.server.respond.result.EntityRespond;

/**
 *
 * @author duyetpt
 */
public class SearchUserTest {

    List<String> list;
    EntityRespond respond;

    public SearchUserTest() {
    }

    @BeforeMethod
    public void setUp() {
        list = new ArrayList<>();
    }

    @AfterMethod
    public void tearDown() {
        Actor.removeUser();
        for (String id : list) {
            System.out.println("id :" + id);
            Userdb.removeUserById(id);
        }
        TransactionLogDB.remove();
    }

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    // @Test
    // public void hello() {}
    public void initData(int count1, int count2) throws IOException, EazyException {
        Actor.userId = Userdb.registFemale();
        Actor.partnerId = Userdb.registMale();

        TransactionLogDB.addTransactions(Actor.userId, count1);
        TransactionLogDB.addTransactions(Actor.partnerId, count2);
        String id = null;
        for (int i = 0; i < 20; i++) {
            if (i % 3 == 0) {
                id = Userdb.registFemale();
            } else {
                id = Userdb.registMale();
            }
            list.add(id);
        }
    }

    @Test
    public void test_Purchase() throws IOException, EazyException {
        initData(10, 20);

        All.put(ParamKey.CSV, null);

        All.put(ParamKey.IS_PURCHASE, (long) Constant.FLAG.ON);
        All.put("to_money", Double.valueOf("512.5"));
        All.put("from_money", Double.valueOf("1.5"));
        All.put("to_pur_day", "20140101");
        All.put("to_pur_day", "20150101");
        All.put(ParamKey.ORDER, (long) 1);
        All.put(ParamKey.SORT, (long) 1);
        All.api = APIManager.getApi(API.SEARCH_USER);
        respond = (EntityRespond) All.execute();

        assertEquals("ok", ErrorCode.SUCCESS, respond.code);
        SizedListData data = (SizedListData) respond.data;
        assertEquals("ok", Integer.valueOf(2), data.total);
        //  System.out.println(data.ll.size());
    }

    @Test
    public void test_Purchase1() throws IOException, EazyException {
        initData(0, 10);

        All.put(ParamKey.CSV, null);

        All.put(ParamKey.IS_PURCHASE, (long) Constant.FLAG.ON);
//        All.put(ParamKey.to_money, Double.valueOf("512.5"));
//        All.put(ParamKey.from_money, Double.valueOf("12.5"));
        All.put("to_pur_day", "20140101");
        All.put("to_pur_day", "20150101");
        All.put(ParamKey.ORDER, (long) 1);
        All.put(ParamKey.SORT, (long) 1);
        All.api = APIManager.getApi(API.SEARCH_USER);
        respond = (EntityRespond) All.execute();

        assertEquals("ok", ErrorCode.SUCCESS, respond.code);
        SizedListData data = (SizedListData) respond.data;
        assertEquals("ok", Integer.valueOf(1), data.total);
        //  System.out.println(data.ll.size());
    }

    @Test
    public void test_NotPurchase() throws IOException, EazyException {
        initData(0, 10);

        All.put(ParamKey.CSV, null);

        All.put(ParamKey.IS_PURCHASE, (long) Constant.FLAG.OFF);
        All.put("to_money", Double.valueOf("512.5"));
        All.put("from_money", Double.valueOf("12.5"));
        All.put("to_pur_day", "20140101");
        All.put("to_pur_day", "20150101");
        All.put(ParamKey.ORDER, (long) 1);
        All.put(ParamKey.SORT, (long) 1);
        All.put(ParamKey.SKIP, (long) 10);
        All.put(ParamKey.TAKE, (long) 10);
        All.api = APIManager.getApi(API.SEARCH_USER);
        respond = (EntityRespond) All.execute();

        assertEquals("ok", ErrorCode.SUCCESS, respond.code);
        SizedListData data = (SizedListData) respond.data;
//        assertEquals("ok", Integer.valueOf(list.size() + 1), data.total);
        System.out.println("notPurchase" + data.ll.size());
    }
}
