/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.usermanagementserver.server.emailpool;

import java.util.LinkedList;
import java.util.Queue;

/**
 *
 * @author RuAc0n
 */
public class QueueEmailManager {

    //public static Map<String, Session> SS = new HashMap<String, Session>( Config.MaxConcurrent );
    public static Queue<EmailInfor> queue = new LinkedList<EmailInfor>();

    public static EmailInfor pollEmail() {
        return queue.poll();
    }

    public static void addEmail(EmailInfor emailInfor) {
        queue.add(emailInfor);
    }

}
