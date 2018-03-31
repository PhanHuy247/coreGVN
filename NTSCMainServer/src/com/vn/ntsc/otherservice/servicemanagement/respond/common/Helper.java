/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.otherservice.servicemanagement.respond.common;

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.X509EncodedKeySpec;
import eazycommon.constant.Constant;
import eazycommon.exception.EazyException;
import eazycommon.util.Util;
import org.json.simple.JSONObject;
import sun.misc.BASE64Encoder;
import com.vn.ntsc.Config;
import com.vn.ntsc.dao.impl.TransactionStatisticDAO;
import com.vn.ntsc.eazyserver.server.request.Request;

/**
 *
 * @author RuAc0n
 */
public class Helper {

    public static String getReceipt(String receipt, String url) {
        String result = " ";

        final BASE64Encoder encoder = new BASE64Encoder();
        final String receiptData = encoder.encode(receipt.getBytes());
        JSONObject json = new JSONObject();
        json.put("receipt-data", receiptData);
        final String jsonData = json.toJSONString();

        try {
            StringBuilder postData = new StringBuilder();
            URL u = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) u.openConnection();

            //post method
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            //data to send
            postData.append(jsonData);
            String encodedData = postData.toString();
            // send data by byte
            conn.setRequestProperty("Content-Language", "en-US");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestProperty("Content-Length", (new Integer(encodedData.length())).toString());
//        byte[] postDataByte = postData.toString().getBytes("UTF-8");
            try {
                OutputStream out = conn.getOutputStream();
                out.write(encodedData.getBytes());
                out.flush();
                out.close();
            } catch (IOException ex) {
                Util.addErrorLog(ex);
                return null;
            }
            //get data from server
            InputStreamReader isr = new InputStreamReader(conn.getInputStream());
            BufferedReader buf = new BufferedReader(isr);

            //write
            String line;
            while ((line = buf.readLine()) != null) {
                result += line;
            }

        } catch (Exception ex) {
            Util.addErrorLog(ex);
            return null;
        }
        return result;
    }

    // HUNGDT change function  Multiapp #6374
//    public static void addTransactionStatistic(String dateTime, Request request, int type, String packetId, double money) throws EazyException {
//        String day = dateTime.substring(0, 8);
//        String hour = dateTime.substring(0, 10);
//        TransactionStatisticDAO.update(day, hour, money, type);
//    }
    
    public static void addTransactionStatistic(String applicationName,String dateTime, Request request, int type, String packetId, double money) throws EazyException {
        String day = dateTime.substring(0, 8);
        String hour = dateTime.substring(0, 10);
        TransactionStatisticDAO.update(day, hour, money, type, applicationName);
    }

    private static final String KEY_FACTORY_ALGORITHM = "RSA";
    private static final String SIGNATURE_ALGORITHM = "SHA1withRSA";
    public static boolean verifyGooglePurchase(String signedData, String signature,String publicKey1) {
        Signature sig;
        try {
            byte[] decodedKey = Base64.decode(publicKey1);
            KeyFactory keyFactory = KeyFactory.getInstance(KEY_FACTORY_ALGORITHM);
            PublicKey publicKey = keyFactory.generatePublic(new X509EncodedKeySpec(decodedKey));
            sig = Signature.getInstance(SIGNATURE_ALGORITHM);
            sig.initVerify(publicKey);
            sig.update(signedData.getBytes());
            byte[] decodedSig = Base64.decode(signature);
            if (!sig.verify(decodedSig)) {
                return false;
            }
            return true;
        } catch (Exception e) {
            Util.addErrorLog(e);
        }
        return false;
    }
    
    public static void main(String[] args) throws Exception {
        String key = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEArkQvl0xCady6dB31xSS+YReSvWf4xT36XpO/KZa+1hVP9YJacWOhQzGiyPOyrznRCs1uY7hnFfQBlUCpt8Rl9EsrSXkTQpcMzeaZRvjHCOnmWELUS+KFSzYwQDtnOhF4tNWsFWcmh0ZEN6xZoHCSuiYFU2PPr9dUKckOgX/8PBBkg9fJ+a4zxsPraNdki9DxfBj4ACXveH0dDmS5TeVwcxtaAb+qa4I+3vIjMWXXIoEJhAifZcj2/b9urvLpYwF0tC0MAaJM9gQjEE1w0nppMnNkqHbFZP+26qB48z7IKw1xS/0hQHTvgVDzwXDQDfoeY9EHAJ4YS9/yyQRHaLFiQQIDAQAB";
        String signedData = "\"orderId\":\"12999763169054705758.1350778913998946\",\"packageName\":\"com.itsherpa.mobileapps.eazy\",\"productId\":\"eazy_point_package1\",\"purchaseTime\":1420447546006,\"purchaseState\":0,\"developerPayload\":\"4fce6fe0-8535-438c-a659-4d77a2d30872\",\"purchaseToken\":\"beimhencphohnjefocllnnfi.AO-J1OyP-c1HvuyvI3_vZlMGJh7QsDu04j_knj49XoXbLhX0vepHJpR68STrSqUicCf1M_mXxSbNz4IfWRNDmTMXy4sfSaWusulIB4fPRtExyoXciXFZJkxWjaOY5tBqg4yQoiH5N-In2tBGP5qhqy8GDGOpcebuHg\"";
        String signature = "rIZqhW/CpZ0vouWR0Xy80yeVzIV6DpCz+x0ZMRin2SQW9+6RRTLRss7sJnUThpDSBSklW0ZZaLRbAeN3qS/M2+5myDJRrYIgoM3Iz89RVxONVHtUxxMNW61db0Zpgw34Xzqgh8oYgD5DXpmwmCuPw7NyNxfhq/NURhMoW/gfM4xBvNZFHtJO8I+kLuTJuOAL8IHKM6dQcbZAQqcR3iJsbOl6tCUKWDUZTNUdmoWvPnIDip3onH16SA9PS4aMmC0CuZELDOvCa6xrUkCD7ftRHGYcRmKTRsaT9+jmvV7sHYiE1eC5mKTDHL0brTzBh2oj39G5/DTymXvavHr15Er8fA\\u003d\\u003d";
        Signature sig;
        byte[] decodedKey = Base64.decode(key);
            KeyFactory keyFactory = KeyFactory.getInstance(KEY_FACTORY_ALGORITHM);
            PublicKey publicKey = keyFactory.generatePublic(new X509EncodedKeySpec(decodedKey));
            sig = Signature.getInstance(SIGNATURE_ALGORITHM);
            sig.initVerify(publicKey);
            sig.update(signedData.getBytes());
            byte[] decodedSig = Base64.decode(signature);
            if (!sig.verify(decodedSig)) {
                System.out.println("teo");
            }
            System.out.println("khong teo");
    }
}
