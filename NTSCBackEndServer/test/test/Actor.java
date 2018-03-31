/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.eclipse.jetty.util.ArrayQueue;

/**
 *
 * @author Admin
 */
public class Actor {

    public static Queue<String> listId;
    public static List<String> blackList;
    public static String userId;
    public static String partnerId;
    public static final String TEST_FLAG = "test_flag";

    static {
        listId = new ArrayQueue<String>();
        blackList = new ArrayList<>();

    }

    public static void resetUser() {
        userId = null;
        partnerId = null;
        listId = new ArrayQueue<>();
        blackList = new ArrayList<>();
    }

    public static void removeUser() {
        if (userId != null) {
            Userdb.removeUserById(userId);
        }
        if (partnerId != null) {
            Userdb.removeUserById(partnerId);
        }
        if (!listId.isEmpty()) {
            String id = null;
            while ((id = listId.poll()) != null) {
                Userdb.removeUserById(id);
            }
        }
        resetUser();
    }

    // regist by email

    public static void registListUser(int count) throws IOException {
        String id;
        for (int i = 0; i < count; i++) {
            id = Userdb.registMale();
            listId.add(id);
        }
    }

    public static List<String> registListUserByFb(int count) throws IOException {
        List<String> list = new ArrayList<>();
        String id;
        for (int i = 0; i < count; i++) {
            id = Userdb.registUserByFb();
            list.add(id);
            try {
                Thread.sleep(1);
            } catch (InterruptedException ex) {
                Logger.getLogger(Actor.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return list;
    }

}
