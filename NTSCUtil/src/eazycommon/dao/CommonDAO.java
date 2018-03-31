/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eazycommon.dao;

import com.mongodb.Mongo;
import com.mongodb.MongoOptions;
import com.mongodb.ServerAddress;
import eazycommon.CommonConfig;
import eazycommon.util.Util;

/**
 *
 * @author RuAc0n
 */
public class CommonDAO {

    public static Mongo mongo;

    public static void init() {
        try {
            ServerAddress sa = new ServerAddress(CommonConfig.DB_SERVER);
            MongoOptions mo = new MongoOptions();
            mo.setConnectionsPerHost(CommonConfig.CONNECTION_PER_HOST);
            mongo = new Mongo(sa, mo);
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
    }
    
}
