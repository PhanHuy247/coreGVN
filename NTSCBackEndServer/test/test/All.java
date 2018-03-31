package test;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.util.Date;
import eazycommon.util.Util;
import org.bson.types.ObjectId;
import org.json.simple.JSONObject;
import com.vn.ntsc.backend.server.respond.IApiAdapter;
import com.vn.ntsc.backend.server.respond.Respond;

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
        return api.execute(obj);
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

    public static Object[] $(Object... objs) {
        return objs;
    }
}
