/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.test.testclient;

import com.test.testclient.handlers.AuthenMessageHandler;
import com.test.testclient.handlers.ChatMessageHandler;
import com.test.testclient.handlers.MessageStatusHandler;
import com.test.testclient.handlers.PresenceMessageHandler;
import com.vn.ntsc.Config;
import com.vn.ntsc.chatclient.Client;
import com.vn.ntsc.chatserver.messageio.MessageIO;
import com.vn.ntsc.chatserver.pojos.message.Message;
import com.vn.ntsc.chatserver.pojos.message.messagetype.MessageType;

/**
 *
 * @author Administrator
 */
class ThreadX extends Thread {

    public static final String server = "localhost";
    public Client client;

    public ThreadX(String username) {
        final int port = Config.ChatServerPort;

        client = new Client(server, port);

//        client.addGenericMessageListener( new GenericMessageHandler() );
//        client.addAuthenListener(new AuthenMessageHandler());
        client.addChatListener(new ChatMessageHandler());
//        client.addPresenceListener(new PresenceMessageHandler());
//        client.addMessgeStatusListener(new MessageStatusHandler());

        String password = username;
        client.sendAuthenMessage(username, password);

    }

    @Override
    public void run() {
        try {
            sleep(3000);
            for (int i = 0; i < TestSendMessage.number; i++) {
                if (!client.username.equals(TestSendMessage.s[i])) {
                    Message msg = new Message(client.username, TestSendMessage.s[i], MessageType.PP, "hello");
                    MessageIO.sendMessage(client, msg);
                }
//                sleep( 1 );
            }
        } catch (Exception ex) {
           
        }
    }
}

class ThreadCal extends Thread {

    @Override
    public void run() {

        long timeStart = System.currentTimeMillis();
//        System.out.println("time = " + (System.currentTimeMillis() - timeStart));
        while (TestSendMessage.numberSend < TestSendMessage.number * (TestSendMessage.number - 1)) {
            try {
                sleep(2);
            } catch (Exception ex) {
               
            }
        }
        System.out.println("Collapsed time: " + (System.currentTimeMillis() - timeStart));
    }
}

public class TestSendMessage {

    public static String[] s;
    public static ThreadX[] T;
    public static int numberSend;
    public static int number = 100;

    public static void main(String[] args) {
        s = new String[number];
        T = new ThreadX[number];
        numberSend = 0;
        for (int i = 0; i < number; i++) {
            s[i] = randString(i, 7);
            T[i] = new ThreadX(s[i]);
        }

        for (int i = 0; i < number; i++) {
            T[i].start();
        }

        ThreadCal cal = new ThreadCal();
        cal.start();
    }

    static String randString(int x, int len) {
        String res = "";
        while (x > 0) {
            res += (x % 10);
            x /= 10;
        }
        while (res.length() < len) {
            res += '0';
        }
        return res;
    }
}
