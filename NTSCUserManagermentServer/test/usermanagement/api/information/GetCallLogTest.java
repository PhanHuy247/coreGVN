/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package usermanagement.api.information;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.WriteResult;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.bson.types.ObjectId;
import org.json.simple.JSONObject;
import org.testng.annotations.Test;
import DAOTest.DBTest;
import DAOTest.userdb.Userdb;
import eazycommon.constant.Constant;
import eazycommon.constant.ErrorCode;
import eazycommon.constant.ParamKey;
import eazycommon.constant.mongokey.LogdbKey;
import static org.testng.AssertJUnit.assertEquals;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import usermanagement.All;
import com.vn.ntsc.usermanagementserver.entity.impl.datarespond.LogCallData;
import com.vn.ntsc.usermanagementserver.server.respond.impl.information.GetCallLog;
import com.vn.ntsc.usermanagementserver.server.respond.result.ListEntityRespond;

/**
 *
 * @author Admin
 */
public class GetCallLogTest {

    String requestId;
    String partnerId;
    JSONObject requestObj;
    List<String> list;
    int reqCall;
    int partnerCall;

    DB db = DBTest.mongo.getDB(LogdbKey.DB_NAME);
    DBCollection coll = db.getCollection(LOG_CALL.LOG_CALL_COLLECTION);

    public GetCallLogTest() {
    }

    @BeforeMethod
    public void setUp() throws IOException {
        list = new ArrayList<>();
        requestObj = new JSONObject();
        JSONObject regReq = Userdb.initRegistUser(Long.valueOf(1), Long.valueOf(1));
        JSONObject regPar = Userdb.initRegistUser(Long.valueOf(1), Long.valueOf(1));
        partnerId = Userdb.registUser(regPar);
        requestId = Userdb.registUser(regReq);
        addCallLog();
    }

    @AfterMethod
    public void tearDown() {
        for (String str : list) {
            ObjectId id = new ObjectId(str);
            coll.remove(new BasicDBObject("_id", id));
        }

        Userdb.removeUserById(requestId);
        Userdb.removeUserById(partnerId);
    }

    public void addCallLog() {
        String id;
        id = initCallLog("53bf4c00c4e4fb07a7ab6fab", LOG_CALL.VIDEO_TYPE, requestId, partnerId, 10, "192.179.198.111", LOG_CALL.PARTNER_ANSWER);
        list.add(id);
        id = initCallLog("53bf4c00c4e4fb07a7ab1fab", LOG_CALL.VIDEO_TYPE, partnerId, requestId, 10, "192.179.198.111", LOG_CALL.PARTNER_BUSY);
        list.add(id);
        id = initCallLog("53bf4c00c4e4fb07a7ab2fab", LOG_CALL.VIDEO_TYPE, requestId, partnerId, 10, "192.179.198.111", LOG_CALL.PARTNER_ANSWER);
        list.add(id);
        id = initCallLog("53bf4c00c4e4fb07a7ab3fab", LOG_CALL.VIDEO_TYPE, partnerId, requestId, 10, "192.179.198.111", LOG_CALL.PARTNER_REFUSE);
        list.add(id);
        id = initCallLog("53bf4c00c4e4fb07a7ab4fab", LOG_CALL.VIDEO_TYPE, requestId, partnerId, 10, "192.179.198.111", LOG_CALL.PARTNER_REFUSE);
        list.add(id);

        id = initCallLog("53bf4c00c4e4fb07a7ab4fab", LOG_CALL.VOICE_TYPE, requestId, partnerId, 22, "192.179.198.111", LOG_CALL.PARTNER_REFUSE);
        list.add(id);
        id = initCallLog("53bf4c00c4e4fb07a7ab4fab", LOG_CALL.VOICE_TYPE, partnerId, requestId, 21, "192.179.198.111", LOG_CALL.PARTNER_BUSY);
        list.add(id);
        id = initCallLog("53bf4c00c4e4fb07a7ab4fab", LOG_CALL.VOICE_TYPE, partnerId, requestId, 3323, "192.179.198.111", LOG_CALL.PARTNER_ANSWER);
        list.add(id);
        id = initCallLog("53bf4c00c4e4fb07a7ab4fab", LOG_CALL.VOICE_TYPE, partnerId, requestId, 43, "192.179.198.111", LOG_CALL.PARTNER_REFUSE);
        list.add(id);
        id = initCallLog("53bf4c00c4e4fb07a7ab4fab", LOG_CALL.VOICE_TYPE, requestId, partnerId, 523, "192.179.198.111", LOG_CALL.PARTNER_BUSY);
        list.add(id);

    }

    public String initCallLog(String callId, int callType, String reqId, String partId, int duration, String ip, int partnerRes) {

        BasicDBObject dbObj = new BasicDBObject();

        dbObj.put(LOG_CALL.CALL_ID, callId);
        dbObj.put(LOG_CALL.CALL_TYPE, callType);
        dbObj.put(LOG_CALL.DURATION, duration);
        //ENDTIME
        Date time = All.getTime();
        dbObj.put(LOG_CALL.START_TIME, time.toString());
        time.setMinutes(time.getMinutes() + duration);
        dbObj.put(LOG_CALL.END_TIME, time.toString());
        dbObj.put(LOG_CALL.DURATION, duration);
        dbObj.put(LOG_CALL.REQUEST_ID, reqId);
        dbObj.put(LOG_CALL.PARTNER_ID, partId);
        dbObj.put(LOG_CALL.IP, ip);
        dbObj.put(LOG_CALL.PARTNER_RESPOND, partnerRes);

        if (reqId == requestId) {
            reqCall++;
        } else {
            partnerCall++;
        }
        System.out.println("insert coll");
        WriteResult result = coll.insert(dbObj);
        System.out.println(result.getN());
        return dbObj.get(LOG_CALL.ID).toString();
    }

    @Test
    public void test() {
        long skip = 0;
        long take = 1;
        requestObj.put(ParamKey.USER_ID, partnerId);
        requestObj.put(ParamKey.SKIP, skip);
        requestObj.put(ParamKey.TAKE, take);
        requestObj.put(ParamKey.TYPE, Long.valueOf(Constant.REQUEST_CALL_TYPE.MY_REQUEST_CALL));
        GetCallLog api = new GetCallLog();

        ListEntityRespond resp = (ListEntityRespond) api.execute(requestObj, All.getTime());
        assertEquals("get successfully", ErrorCode.SUCCESS, resp.code);
        assertEquals("number Ok", take, resp.data.size());
        LogCallData data = (LogCallData) resp.data.get(0);
        System.out.println("requestID :" + requestId);
        System.out.println("PARTNERID :" + partnerId);
        System.out.println(data.toJsonObject().toJSONString());
    }
}

class LOG_CALL {

    public static final String LOG_CALL_COLLECTION = "log_call";

    public static final String ID = "_id";
    public static final String CALL_ID = "call_id";
    public static final String CALL_TYPE = "call_type";
    public static final String REQUEST_ID = "req_id";
    public static final String PARTNER_ID = "partner_id";
    public static final String START_TIME = "start_time";
    public static final String END_TIME = "end_time";
    public static final String DURATION = "duration";
    public static final String IP = "ip";
    public static final String PARTNER_RESPOND = "partner_respond";

    public static final int VOICE_TYPE = 1;
    public static final int VIDEO_TYPE = 2;

    public static final int PARTNER_ANSWER = 0;
    public static final int PARTNER_BUSY = 1;
    public static final int PARTNER_REFUSE = 2;
}
