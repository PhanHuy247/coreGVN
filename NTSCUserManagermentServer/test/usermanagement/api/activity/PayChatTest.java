/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package usermanagement.api.activity;

import DAOTest.userdb.Userdb;
import java.io.IOException;
import eazycommon.constant.API;
import eazycommon.constant.Constant;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.ParamKey;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import usermanagement.Actor;
import usermanagement.All;
import usermanagement.provider.PayChatDataPro;
import com.vn.ntsc.usermanagementserver.server.pointaction.ActionManager;
import com.vn.ntsc.usermanagementserver.server.pointaction.ActionType;
import com.vn.ntsc.usermanagementserver.server.respond.APIManager;
import com.vn.ntsc.usermanagementserver.server.respond.result.EntityRespond;
import com.vn.ntsc.usermanagementserver.server.userinformanager.UserInforManager;

/**
 *
 * @author duyetpt
 */
public class PayChatTest {

    EntityRespond respond;

    public PayChatTest() {
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
    public void initData(int type, int genderU, int genderP) throws IOException {
        Actor.initCouple(genderU, genderP);
        All.put(ParamKey.USER_ID, Actor.userId);
        All.put(ParamKey.PARTNER_ID, Actor.partnerId);
        All.put(ParamKey.IP, "127.0.0.1:89");
        All.put(ParamKey.TYPE, (long) type);
        All.put(ParamKey.TIME, All.getTime().getTime());
        All.api = APIManager.getApi(API.PAY_CHAT);
    }

    @Test(dataProviderClass = PayChatDataPro.class, dataProvider = PayChatDataPro.DATA_ENOUGHT_POINT)
    public void test_wink(int type, int genderU, int genderP, int errorCode, int point) throws IOException {
        initData(type, genderU, genderP);
        UserInforManager.get(Actor.userId).point = point;
        Integer befU = All.getPoint(Actor.userId);
        Integer befP = All.getPoint(Actor.partnerId);
        respond = (EntityRespond) All.execute();

        int decreaseP = All.getActorPrice(ActionType.wink, genderU);
        int increaseP = All.getPartnerPrice(ActionType.wink, genderP);
        
        if (errorCode == ErrorCode.SUCCESS) {
            All.doTestPoint(Actor.userId, befU, decreaseP, Actor.partnerId, befP, increaseP, errorCode, respond.code);
        }else{
            All.doTestPoint(Actor.userId, befU, 0, Actor.partnerId, befP, 0, errorCode, respond.code);
        }
    }
}
