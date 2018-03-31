/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package usermanagement.api.buzz;

import java.io.IOException;

import org.testng.annotations.Test;
import usermanagement.All;
import DAOTest.ImageDB;
import DAOTest.userdb.Userdb;
import eazycommon.constant.API;
import eazycommon.constant.Constant;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.ParamKey;
import eazycommon.exception.EazyException;
import static org.testng.AssertJUnit.assertEquals;
import org.testng.annotations.AfterMethod;
import usermanagement.Actor;
import com.vn.ntsc.usermanagementserver.entity.impl.image.GetImageData;
import com.vn.ntsc.usermanagementserver.server.respond.APIManager;
import com.vn.ntsc.usermanagementserver.server.respond.result.EntityRespond;

/**
 *
 * @author duyetpt
 */
public class GetImageInforTest {

    EntityRespond respond;
    String imageId;

    public GetImageInforTest() {
    }

    @AfterMethod
    public void teardown() {
        Actor.removeUser();
    }

    public void initData() throws IOException, EazyException {
        Actor.userId = Userdb.registFemale();
        imageId = ImageDB.insertImage(Constant.BUZZ_TYPE_VALUE.IMAGE_BUZZ, Actor.userId);
        All.put(ParamKey.BUZZ_VALUE, imageId);
        All.api = APIManager.getApi(API.GET_IMAGE_INFOR);

    }

    @Test
    public void test() throws IOException, EazyException {
        initData();
        respond = (EntityRespond) All.execute();
        assertEquals("ok", ErrorCode.SUCCESS, respond.code);
        GetImageData data = (GetImageData) respond.data;
        assertEquals("ok", new Long(Constant.REVIEW_STATUS_FLAG.APPROVED), data.autoApproved);
        ImageDB.remove(imageId, Actor.userId);
    }
}
