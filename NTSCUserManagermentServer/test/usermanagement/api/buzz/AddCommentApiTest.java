/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package usermanagement.api.buzz;

import java.io.IOException;
import org.testng.annotations.Test;
import usermanagement.All;
import DAOTest.userdb.Userdb;
import eazycommon.constant.API;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.ParamKey;
import static org.testng.AssertJUnit.assertEquals;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import usermanagement.Actor;
import usermanagement.provider.AddCommentDataPro;
import com.vn.ntsc.usermanagementserver.server.pointaction.ActionManager;
import com.vn.ntsc.usermanagementserver.server.pointaction.ActionType;
import com.vn.ntsc.usermanagementserver.server.respond.APIManager;
import com.vn.ntsc.usermanagementserver.server.respond.Respond;
import com.vn.ntsc.usermanagementserver.server.userinformanager.UserInforManager;

/**
 *
 * @author duyetpt
 */
public class AddCommentApiTest {

    Respond respond;

    public AddCommentApiTest() {
    }

    @BeforeMethod
    public void setUp() {
        All.initPrice();
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
    public void initData(int genderU, int genderP) throws IOException {
        Actor.initCouple(genderU, genderP);
        All.put(ParamKey.USER_ID, Actor.userId);
        All.put(ParamKey.BUZZ_OWNER_ID, Actor.partnerId);
        All.put(ParamKey.TIME, All.getTime().getTime());
        All.put(ParamKey.BUZZ_ID, "buzz_id");
        All.put(ParamKey.IP, "127.0.0.1:98");
    }

    @Test(dataProviderClass = AddCommentDataPro.class, dataProvider = AddCommentDataPro.DATA)
    public void test(int genderU, int genderP, int errorCode, int point) throws IOException {
        initData(genderU, genderP);
        UserInforManager.get(Actor.userId).point = point;
        All.api = APIManager.getApi(API.ADD_COMMENT_GET_INFOR);
        respond = All.execute();
        assertEquals("ok", errorCode, respond.code);
        if (respond.code == ErrorCode.SUCCESS) {
            All.api = APIManager.getApi(API.ADD_COMMENT);
            Integer befU = All.getPoint(Actor.userId);
            System.out.println("befU " + befU);
            Integer befP = All.getPoint(Actor.partnerId);
            respond = All.execute();

            int decreaseP = All.getActorPrice(ActionType.comment_buzz, genderU);
            int increaseP = All.getPartnerPrice(ActionType.comment_buzz, genderP);
            System.out.println("decreaseP :" + decreaseP);
            if (errorCode == ErrorCode.SUCCESS) {
                All.doTestPoint(Actor.userId, befU, decreaseP, Actor.partnerId, befP, increaseP, errorCode, respond.code);
            } else {
                All.doTestPoint(Actor.userId, befU, 0, Actor.partnerId, befP, 0, errorCode, respond.code);
            }
        }
    }
}
