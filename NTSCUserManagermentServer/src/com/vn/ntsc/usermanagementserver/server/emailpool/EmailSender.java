/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.usermanagementserver.server.emailpool;

import eazycommon.util.Util;
import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;
import eazycommon.constant.Constant;
import com.vn.ntsc.usermanagementserver.Config;

/**
 *
 * @author tuannxv00804
 */
public class EmailSender extends Thread {

//    private static final String FORGOT_PASS_EMAIL_SUBJECT = "認証コードのメール";
//    private static final String ONLINE_ALERT_EMAIL_SUBJECT = "eazyオンライン通知";
//    
    
    private static final String FORGOT_PASS_EMAIL_SUBJECT = "Khôi phục mật khẩu";
    private static final String ONLINE_ALERT_EMAIL_SUBJECT = "NTS thông báo online"; // still unknow
    
    
    private static final String REVERT_CODE = "Mã khôi phục tài khoản của bạn là : ";
    private static final String GUIDE = "Dùng mã này để khôi phục tài khoản của bạn !";
    private static EmailSender emailSender = null;

    public static void startSendingEmail() {
        if (emailSender == null || !emailSender.isAlive()) {
            emailSender = new EmailSender();
            emailSender.start();
        }
    }

    @Override
    public void run() {
        while (!QueueEmailManager.queue.isEmpty()) {
            try {
                EmailInfor email = QueueEmailManager.pollEmail();
                sendMail(email);
            } catch (Exception ex) {
                Util.addErrorLog(ex);                
               
            }
        }
    }

    public void sendMail(EmailInfor email) {
        Properties props = new Properties();
        props.put("mail.smtp.user", Config.EMAIL);
        props.put("mail.smtp.host", Config.EMAIL_HOST);
        props.put("mail.smtp.port", Config.EMAIL_PORT);
//        props.put("mail.smtp.user", "mail.eazy-app.com");
//        props.put("mail.smtp.host", "10.202.3.100");
//        props.put("mail.smtp.port", 143);
//        props.put("mail.smtp.starttls.enable", "true");
//        props.put("mail.smtp.ssl.enable", "true");
        props.put("mail.smtp.auth", "true");
        // props.put("mail.smtp.debug", "true");
//        props.put("mail.smtp.socketFactory.port", 143);
//        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
//        props.put("mail.smtp.socketFactory.fallback", "false");
//        props.put("mail.transport.protocol.", "smtp");
        try {
            Authenticator auth = new SMTPAuthenticator();
            Session session = Session.getInstance(props, auth);
            session.setDebug(true);
            MimeMessage msg = new MimeMessage(session);
            if (email.getEmailType() == Constant.EMAIL_TYPE_VALUE.FORGOT_PASS_EMAIL) {
                msg.setText(REVERT_CODE + email.getCode() + GUIDE);
                msg.setSubject(FORGOT_PASS_EMAIL_SUBJECT);
            } else {
                msg.setText(email.getName() + "さんがオンラインになりました。ログインして確認してください。");
                msg.setSubject(ONLINE_ALERT_EMAIL_SUBJECT);
            }
            msg.setFrom(new InternetAddress(Config.EMAIL));
            msg.addRecipient(Message.RecipientType.TO, new InternetAddress(email.getEmail()));
//            Transport trnsport;
//            trnsport = session.getTransport("smtp");
//            trnsport.connect();
//            msg.saveChanges(); 
//            trnsport.sendMessage(msg, msg.getAllRecipients());
//            trnsport.close();
            Transport.send(msg);
        } catch (Exception mex) {
            Util.addErrorLog(mex);            
        }
    }

    private class SMTPAuthenticator extends javax.mail.Authenticator {

//        @Override
//        public PasswordAuthentication getPasswordAuthentication() {
//            return new PasswordAuthentication(Config.EMAIL, Config.EMAIL_PASSWORD);
//        }
        @Override
        public PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication( Config.EMAIL, Config.EMAIL_PASSWORD);
        }
    }
    
}
