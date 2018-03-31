package usermanagement.api.backend;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import DAOTest.userdb.Userdb;
import java.io.IOException;
import eazycommon.constant.ParamKey;
import static org.testng.AssertJUnit.assertEquals;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import usermanagement.Actor;
import usermanagement.All;
import usermanagement.provider.AddFreePointData;
import com.vn.ntsc.usermanagementserver.server.respond.Respond;
import com.vn.ntsc.usermanagementserver.server.respond.impl.information.AddFreePointApi;

/**
 *
 * @author duyetpt
 */
public class AddFreePointTest {

    Respond respond;

    public AddFreePointTest() {
    }

    @BeforeMethod
    public void setup() throws IOException {
        Actor.userId = Userdb.registMale();
    }

    @AfterMethod
    @SuppressWarnings("empty-statement")
    public void teardown() {
        Actor.removeUser();
    }

    @Test(dataProviderClass = AddFreePointData.class, dataProvider = AddFreePointData.DATA_NAME)
    public void test(int typeId, int point, int errorCode) {
        All.put(ParamKey.USER_ID, Actor.userId);
        All.put("type_id", (long)typeId);
        All.put("point", (long)point);

        int befPoint = All.getPoint(Actor.userId);
        All.api = new AddFreePointApi();
        respond = All.execute();
        assertEquals(errorCode, respond.code);
        int actualPoint = All.getPoint(Actor.userId);
        int expectPoint = befPoint;
        if (point > 0) {
            expectPoint += point;
        }
        assertEquals(expectPoint, actualPoint);
    }
}
