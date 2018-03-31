/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package usermanagement;

import DAOTest.userdb.Userdb;
import java.util.Date;
import eazycommon.constant.Constant;
import eazycommon.exception.EazyException;
import eazycommon.util.Util;
import org.bson.types.ObjectId;
import org.json.simple.JSONObject;
import static org.testng.AssertJUnit.assertEquals;
import com.vn.ntsc.usermanagementserver.server.pointaction.ActionManager;
import com.vn.ntsc.usermanagementserver.server.pointaction.ActionType;
import com.vn.ntsc.usermanagementserver.server.pointaction.Price;
import com.vn.ntsc.usermanagementserver.server.respond.IApiAdapter;
import com.vn.ntsc.usermanagementserver.server.respond.Respond;
import com.vn.ntsc.usermanagementserver.server.userinformanager.UserInforManager;

/**
 *
 * @author Admin
 */
public class All {

    public static JSONObject obj;
    public static IApiAdapter api;

    static {
        obj = new JSONObject();
    }

    public static Respond execute() {
        return api.execute(obj, getTime());
    }

    public static void put(Object key, Object value) {
        obj.put(key, value);
    }

    public static void remove(String key) {
        obj.remove(key);
    }

    public static Date getTime() {
        Date time = Util.getGMTTime();
        return time;
    }

    public static String createId() {
        ObjectId id = new ObjectId(new Date());
        return id.toString();
    }

    public static void initPrice() {
        Price p = new Price(2, 1, 2, 1, 1, 1);
        for (ActionType type : ActionType.values()) {
            ActionManager.put(type, p);
        }
    }

    public static Object[] $(Object... objs) {
        return objs;
    }

    public static final int UPDATE_FOR_FLAG = 1;
    public static final int UPDATE_FOR_FINISH_REGISTED = 2;
    public static final int UPDATE_FOR_EMAIL_FLAG = 3;
    public static final int UPDATE_FOR_VERIFICATION_FLAG = 4;
    public static final int UPDATE_ALL = 0;

    public static void updateFlag(int typeFlag, int flag) throws EazyException {
        switch (typeFlag) {
            case UPDATE_FOR_FLAG:
                Userdb.updateFlag(Actor.userId, flag);
                break;
            case UPDATE_FOR_FINISH_REGISTED:
                Userdb.updateFinishFlag(Actor.userId, flag);
                break;
            case UPDATE_FOR_EMAIL_FLAG:
                Userdb.updateEmailFlag(Actor.userId, flag);
                break;
            case UPDATE_FOR_VERIFICATION_FLAG:
                Userdb.updateVerificationFlag(Actor.userId, flag);
                break;
            case UPDATE_ALL:
                Userdb.updateFlag(Actor.userId, flag);
                Userdb.updateFinishFlag(Actor.userId, flag);
                Userdb.updateEmailFlag(Actor.userId, flag);
                Userdb.updateVerificationFlag(Actor.userId, flag);
                break;
        }
    }
    
    public static int getActorPrice(ActionType type, int genderU){
        return genderU == Constant.GENDER.FEMALE? ActionManager.get(type).femalePrice : ActionManager.get(ActionType.wink).malePrice;
    }
    
    public static int getPartnerPrice(ActionType type, int genderP){
       return genderP == Constant.GENDER.FEMALE ?ActionManager.get(type).femalePartnerPrice : ActionManager.get(type).malePartnerPrice;
    }
    
    public static int getPoint(String userId) {
        int pointB = UserInforManager.get(userId).point;
        return pointB;
    }

    public static void doTestPoint(String userId, int befU, int reducePointU, String partnerId, int befP, int increatePointP, int expectCode, int respondCode) {
        assertEquals("ok", expectCode, respondCode);
        // after
        Integer afU = getPoint(userId) + reducePointU;
        Integer afP = getPoint(partnerId) - increatePointP;

        assertEquals("ok", new Integer(befU), afU);
        assertEquals("ok", new Integer(befP), afP);
    }

}
