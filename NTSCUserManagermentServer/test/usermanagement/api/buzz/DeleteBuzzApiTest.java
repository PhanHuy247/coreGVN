package usermanagement.api.buzz;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.io.IOException;
import org.testng.annotations.Test;
import usermanagement.All;
import DAOTest.userdb.Userdb;
import eazycommon.constant.API;
import eazycommon.constant.Constant;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.ParamKey;
import static org.testng.AssertJUnit.assertEquals;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import usermanagement.Actor;
import com.vn.ntsc.usermanagementserver.server.respond.APIManager;
import com.vn.ntsc.usermanagementserver.server.respond.Respond;

/**
 *
 * @author duyetpt
 */
public class DeleteBuzzApiTest {

    Respond respond;

    public DeleteBuzzApiTest() {
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
    public void initData() throws IOException {
        Actor.userId = Userdb.registFemale();
        All.put(ParamKey.USER_ID, Actor.userId);
        All.api = APIManager.getApi(API.DELETE_BUZZ);
    }

    @Test
    public void test_delNormalBuzz() throws IOException {
        initData();
        All.put(ParamKey.BUZZ_TYPE, Long.valueOf(Constant.BUZZ_TYPE_VALUE.STATUS_BUZZ));
        All.put(ParamKey.IS_STATUS, new Long(Constant.FLAG.OFF));

        respond = All.execute();
        assertEquals("ok", ErrorCode.SUCCESS, respond.code);
    }

    @Test
    public void test_delStatusBuzz() throws IOException {
        Actor.userId = Userdb.registFemale();
        All.put(ParamKey.USER_ID, Actor.userId);
        All.put(ParamKey.TIME, All.getTime().getTime());
        All.put(ParamKey.IP, "127.0.0.1:9804");
        All.put(ParamKey.BUZZ_TYPE, Long.valueOf(Constant.BUZZ_TYPE_VALUE.STATUS_BUZZ));
        All.put(ParamKey.BUZZ_ID, "buzzId");
        All.put(ParamKey.BUZZ_VALUE, "buzz_value");
        All.api = APIManager.getApi(API.ADD_BUZZ);

        respond = All.execute();
        if (respond.code == ErrorCode.SUCCESS) {
            initData();
            All.put(ParamKey.BUZZ_TYPE, new Long(Constant.BUZZ_TYPE_VALUE.STATUS_BUZZ));
            All.put(ParamKey.IS_STATUS, new Long(Constant.FLAG.ON));

            respond = All.execute();
            assertEquals("ok", ErrorCode.SUCCESS, respond.code);
        }
    }
}
