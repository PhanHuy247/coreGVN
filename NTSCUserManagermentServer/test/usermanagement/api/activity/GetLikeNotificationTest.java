/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package usermanagement.api.activity;

import DAOTest.userdb.Userdb;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import eazycommon.constant.API;
import eazycommon.constant.Constant;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.ParamKey;
import eazycommon.exception.EazyException;
import org.bson.types.ObjectId;
import org.json.simple.JSONObject;
import static org.testng.AssertJUnit.assertEquals;
import org.testng.annotations.Test;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import usermanagement.All;
import com.vn.ntsc.usermanagementserver.dao.impl.NotificationDAO;
import com.vn.ntsc.usermanagementserver.entity.impl.notification.Notification;
import com.vn.ntsc.usermanagementserver.server.respond.impl.connection.GetLikeNotification;
import com.vn.ntsc.usermanagementserver.server.respond.result.ListEntityRespond;

/**
 *
 * @author Admin
 */
public class GetLikeNotificationTest {

    public GetLikeNotificationTest() {
    }

    String id;
    JSONObject obj;
    public static List<ObjectId> list = new ArrayList<ObjectId>();

    @BeforeMethod
    public void setUp() {
        obj = new JSONObject();
    }

    @AfterMethod
    public void tearDown() throws EazyException {
        Userdb.removeUserById(id);
        for (ObjectId idO : list) {
            NotificationDAO.removeNotification(idO);
        }
    }

    @Test
    public void test() throws IOException, EazyException {
        obj = Userdb.initRegistUser(Long.valueOf(1), Long.valueOf(1));
        id = Userdb.registUser(obj);
        insertNotiLike();
        obj.put(ParamKey.API_NAME, API.GET_LIKE_NOTIFICATION);
        obj.put(ParamKey.USER_ID, id);
        obj.put(ParamKey.TIME_STAMP, null);
        obj.put(ParamKey.TAKE, Long.valueOf(10));

        GetLikeNotification api = new GetLikeNotification();
        ListEntityRespond en = (ListEntityRespond) api.execute(obj, All.getTime());
        assertEquals(ErrorCode.SUCCESS, en.code);
        List<Notification> data = en.data;

        assertEquals(10, data.size());
    }

    public void insertNotiLike() throws EazyException {
        Notification noti = new Notification();
        noti.notiBuzzId = "uioiolkytioidotueiroihjkkijljfl";
        noti.notiType = Constant.NOTIFICATION_TYPE_VALUE.LIKE_MY_BUZZ_NOTI;
        int i = 0;
        while (i++ < 20) {
            String str = NotificationDAO.addNotification(id, noti, All.getTime().getTime(), Constant.NOTIFICATION_TYPE_VALUE.LIKE_MY_BUZZ_NOTI);
            list.add(new ObjectId(str));
        }
    }

}
