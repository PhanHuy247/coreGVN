/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.test.testclient;

import java.net.Socket;
import java.util.List;
import com.vn.ntsc.chatserver.pojos.user.User;
import com.vn.ntsc.chatserver.pojos.user.UserConnection;
import com.vn.ntsc.chatserver.ucstoreengine.impl.XStoreEngine;

/**
 *
 * @author Administrator
 */
public class TestUcStoreEngine {
    public static void main(String[] args) {
        XStoreEngine store = new XStoreEngine();
        UserConnection uc1 = new UserConnection(new Socket(), new User("hieult"));
        UserConnection uc2 = new UserConnection(new Socket(), new User("duongltd"));
        UserConnection uc3 = new UserConnection(new Socket(), new User("hieult"));
        UserConnection uc4 = new UserConnection(new Socket(), new User("sonth"));
        UserConnection uc5 = new UserConnection(new Socket(), new User("duongltd"));
        store.put(uc1);
        store.put(uc2);
        store.put(uc3);
        store.put(uc4);
        store.put(uc5);
        System.out.println("finish put");
        for(int i = 0; i < 15; i++){
            uc1 = store.poll();
            System.out.println(i + " " + uc1);
        }
        
        System.out.println("finish peek");
        List<UserConnection> lUc = store.gets("hieult");
        for(int i = 0; i < lUc.size(); i++){
            System.out.println(lUc.get(i));
        }
        System.out.println("finish gets");
        uc1 = store.get("duongltd");
        System.out.println(uc1);
        System.out.println("finish get");
        store.remove(uc5);
        for(int i = 0; i < 15; i++){
            uc1 = store.poll();
            System.out.println(i + " " + uc1);
        }
        System.out.println("finish remove");
    }
}
