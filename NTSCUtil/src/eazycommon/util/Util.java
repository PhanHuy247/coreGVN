/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eazycommon.util;

//import com.maxmind.geoip.LookupService;
import eazycommon.backlist.BannedWordDAO;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import javax.servlet.http.HttpServletRequest;
import eazycommon.backlist.BannedWordManger;
import eazycommon.backlist.ReplaceWordDAO;
import eazycommon.constant.Constant;
import eazycommon.constant.Format;
import eazycommon.exception.EazyException;
import eazycommon.logger.DailyLogger;
import java.util.HashSet;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 *
 * @author DuongLTD
 */
public class Util {

    private static MessageDigest md;

    static {
        try {
            md = MessageDigest.getInstance("SHA1");
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
    }

    // LongLT 8/8/2016 copy from Eazy
    //check if all args is not null
    public static boolean validate(Object... args) {
        try {
            for (Object arg : args) {
                if (arg == null) {
                    return false;
                }
            }
        } catch (Exception e) {
            Util.addInfoLog(e.getMessage());
            Util.addErrorLog(e);
            return false;
        }
        return true;
    }

    // LongLT 8/8/2016 copy from Eazy
    //check if all args is not null and not empty
    public static boolean validateString(String... args) {
        try {
            for (String arg : args) {
                if (arg == null || arg.isEmpty()) {
                    return false;
                }
            }

        } catch (Exception e) {
            Util.addInfoLog(e.getMessage());
            Util.addErrorLog(e);
            return false;
        }
        return true;
    }

    public static List<String> getListString(JSONObject jsonObject, String param) {
        JSONArray list = (JSONArray) jsonObject.get(param);
        List<String> listRequesters = new ArrayList<>();
        if (list != null) {
            for (Object list1 : list) {
                listRequesters.add(list1.toString());
            }
        }
        return listRequesters;
    }

    public static List<Long> getListLong(JSONObject jsonObject, String param) {
        JSONArray list = (JSONArray) jsonObject.get(param);
        List<Long> listLong = new ArrayList<>();
        if (list != null) {
            for (Object list1 : list) {
                listLong.add(Long.parseLong(list1.toString()));
            }
        }
        return listLong;
    }

    public static long currentTime() {
        TimeZone tz = TimeZone.getDefault();
        return System.currentTimeMillis() - tz.getRawOffset();
    }

    public static Date getGMT(long inputTime) {
        TimeZone tz = TimeZone.getDefault();
        long time = inputTime - tz.getRawOffset();
        Date d = new Date(time);
        return d;
    }

    public static Date getGMTTime() {
        Date d = new Date(currentTime());
        return d;
    }

    public static int convertBirthdayToAge(String time) {
        int age = 0;
        if (time != null) {
            age = 0;
            Date bir = DateFormat.parse_yyyyMMdd(time);
            Date now = new Date();
            age = now.getYear() - bir.getYear();
            int nowMonth = now.getMonth();
            int nowDay = now.getDate();
            int birMonth = bir.getMonth();
            int birDay = bir.getDate();
            if (nowMonth < birMonth || (nowMonth == birMonth && nowDay < birDay)) {
                age--;
            }
        }
        return age;
    }

    public static void addDebugLog(String str) {
        DailyLogger.debug(str);
//        LogMessage log = new LogMessage(time, str, LogType.INFOR);
//        LogContainer.add(log);
    }

    public static void addErrorLog(Exception ex) {
        DailyLogger.error(ex);
//        LogMessage log = new LogMessage(ex, LogType.ERROR);
//        LogContainer.add(log);
    }

    public static void addInfoLog(String str) {
        DailyLogger.info(str);
//        LogMessage log = new LogMessage(str, LogType.DEBUG);
//        LogContainer.add(log);
    }

    public static String createVerificationCode() {
        Random rd = new Random();
        Integer code = rd.nextInt(899999) + 100000;
        return code.toString();
    }

    public static String getOsName() {
        String os = System.getProperty("os.name");
        return os;
    }

    public static boolean isWindows() {
        return getOsName().startsWith("Windows");
    }

    public static Long getLongParam(JSONObject jsonOject, String param) {
        Long number = null;
        if(jsonOject.get(param) != null){
            number = (Long) jsonOject.get(param);
        }
        return number;
    }

    public static Integer getIntParam(JSONObject jsonObject, String param) {     
        Integer number = -1;
        try {
            if(jsonObject.get(param)!=null)
            number = (new Double(jsonObject.get(param).toString())).intValue();
        } catch (Exception e) {
            Util.addErrorLog(e);
            number = -1;
        }
        
        return number;
    }

    public static Double getDoubleParam(JSONObject jsonOject, String param) {
        Object number = jsonOject.get(param);
        if (number == null) {
            return null;
        }
        Double d = Double.parseDouble(number.toString());
        return d;
    }

    public static String getStringParam(JSONObject jsonObject, String param) {
        String str = (String) jsonObject.get(param);
        return str;
    }

    //== ums
    public static String getSHA1String(String input) {
        byte[] b = md.digest(input.getBytes());
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < b.length; i++) {
            sb.append(Integer.toString((b[i] & 0xff) + 0x100, 16).substring(1));
        }
        return sb.toString();
    }

    private static final Pattern EMAIL_PATTERN = Pattern.compile(Format.EMAIL_REGEX);

    public static boolean validateEmail(String email) {
        if (email == null) {
            return false;
        }

        Matcher matcher = EMAIL_PATTERN.matcher(email);
        return matcher.matches();
    }

    public static JSONObject toJSONObject(String jsonString) {
        JSONObject jsonObject;
        try {
            JSONParser parser = new JSONParser();
            Object obj = parser.parse(jsonString);
            jsonObject = (JSONObject) obj;
        } catch (org.json.simple.parser.ParseException ex) {
            jsonObject = null;
        }
        return jsonObject;
    }

    public static String byteToString(byte[] b) {
        int n = b.length;
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < n; i++) {
            builder.append((char) b[i]);
        }
        return builder.toString();
    }

    public static String convertPasswordToMD5(String password) {
        String result = "";
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            byte[] b = messageDigest.digest(password.getBytes());
            result = byteToString(b);
        } catch (NoSuchAlgorithmException ex) {
        } catch (Exception ex) {
        }
        return result;
    }

    public static Map sortByComparator(Map unsortMap) {

        List list = new LinkedList(unsortMap.entrySet());

        // sort list based on comparator
        Collections.sort(list, new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {
                return ((Comparable) ((Map.Entry) (o2)).getValue())
                        .compareTo(((Map.Entry) (o1)).getValue());
            }
        });

        // put sorted list into map again
        //LinkedHashMap make sure order in which keys were inserted
        Map sortedMap = new LinkedHashMap();
        for (Iterator it = list.iterator(); it.hasNext();) {
            Map.Entry entry = (Map.Entry) it.next();
            sortedMap.put(entry.getKey(), entry.getValue());
        }
        return sortedMap;
    }

    public static String createEmailField(String emailId, String fbId) {
        String email = null;
        if (emailId == null && fbId != null) {
            email = String.format(Format.FACEBOOK_EMAIL_FORMAT, fbId);
        } else {
            email = emailId;
        }
        return email;
    }

    public static double calculateDistance(double userLat, double userLng, double venueLat, double venueLng) {
        double latDistance = Math.toRadians(userLat - venueLat);
        double lngDistance = Math.toRadians(userLng - venueLng);
        double a = sqr(Math.sin(latDistance / 2))
                + (Math.cos(Math.toRadians(userLat)))
                * (Math.cos(Math.toRadians(venueLat)))
                * sqr(Math.sin(lngDistance / 2));
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return Math.round(Constant.R_EARTH * c);
    }

    private static double sqr(double x) {
        return x * x;
    }

    // main
    public static String getStringValue(String original, String key, String spilit) {
        String value = null;
        try {
            StringBuilder sb = new StringBuilder(key);
            sb.append(spilit);
            Pattern pattern = Pattern.compile(sb.toString());
            Matcher matcher = pattern.matcher(original);
            if (matcher.find()) {
                value = matcher.group();
                int end = matcher.end();
                value = original.substring(end);
            }
        } catch (Exception ex) {
            Util.addErrorLog(ex);

        }
        return value;
    }

    public static String readFile(String filename) {
        try {
            File f = new File(filename);
            BufferedReader in = new BufferedReader(new FileReader(f));
            String content = "";
            String s;
            while ((s = in.readLine()) != null) {
                content += s + "\n";
            }
            in.close();
            return content;
        } catch (IOException e) {
            Util.addErrorLog(e);
            return e.toString();
        }
    }

    public static byte[] getFile(String url) throws Exception {
        byte[] result = null;
        try {
            FileInputStream fis = new FileInputStream(new File(url));
            BufferedInputStream bis = new BufferedInputStream(fis);
            int bytesAvailable = bis.available();
            byte[] buffer = new byte[bytesAvailable];
            bis.read(buffer, 0, bytesAvailable);
            bis.close();
            result = buffer;
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new Exception();
        }
        return result;
    }

    public static boolean validate(Object arg) {
        return arg != null;
    }

    public static boolean validateString(String arg) {
        return arg != null && !arg.trim().isEmpty();
    }

    public static void deleteFile(String url) throws Exception {
        try {
            File file = new File(url);
            if (file.exists()) {
                file.delete();
            }
        } catch (Exception ex) {
            Util.addErrorLog(ex);
            throw new Exception();
        }
    }

    public static String getClientIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    public static List<Long> getListStickerCode(JSONObject reqObj) {
        JSONArray list = (JSONArray) reqObj.get("lst_stk_code");
        List<Long> listCode = new ArrayList<>();
        for (Object list1 : list) {
            listCode.add((Long) list1);
        }
        return listCode;
    }

    public static void createZipFile(String zipFile, Map<String, String> map, String suffix) throws Exception {
        byte[] buffer = new byte[1024];
        try {
            File file = new File(zipFile);
            if (file.exists()) {
                file.delete();
            }
            file.getParentFile().mkdirs();
            FileOutputStream fos = new FileOutputStream(zipFile);
            ZipOutputStream zos = new ZipOutputStream(fos);
            for (Map.Entry<String, String> entry : map.entrySet()) {
                String fileName = entry.getKey() + suffix;
                ZipEntry ze = new ZipEntry(fileName);
                zos.putNextEntry(ze);
                FileInputStream in = new FileInputStream(entry.getValue());
                BufferedInputStream bis = new BufferedInputStream(in);
                int len;
                while ((len = bis.read(buffer)) > 0) {
                    zos.write(buffer, 0, len);
                }
                bis.close();
            }
            zos.closeEntry();
            //remember close it
            zos.close();
        } catch (IOException ex) {
            addErrorLog(ex);
            throw new IOException();
        }
    }

    public static void writeFlie(String pathFile, byte[] arrByteInput) throws FileNotFoundException, IOException {
        File file = new File(pathFile);
        if (file.exists()) {
            file.delete();
        }
        file.getParentFile().mkdirs();
        FileOutputStream fos = new FileOutputStream(file);
        BufferedOutputStream boss = new BufferedOutputStream(fos);
        boss.write(arrByteInput);
        boss.flush();
        boss.close();
    }

    public static void writeFile(String fileName, String content, String charset) {
        try {

            File fileDir = new File(fileName);
            fileDir.getParentFile().mkdirs();
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(fileDir), charset));
            out.append(content);
            out.flush();
            out.close();

        } catch (Exception e) {
            addErrorLog(e);
        }
    }

    public static void writeFile(String fileName, String content) {
        try {
            FileOutputStream fos = new FileOutputStream(fileName);
            BufferedOutputStream bos = new BufferedOutputStream(fos);
            bos.write(content.getBytes());
            bos.flush();
            bos.close();

        } catch (Exception e) {
            addErrorLog(e);
        }
    }

    //backend
    public static String sendRequest(String requestString, String serverIP, int serverPort) {
        String result = " ";
        try {

            StringBuilder postData = new StringBuilder();
            String urlStr = "http://" + serverIP + ":" + serverPort + "/";
            URL u = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) u.openConnection();

            //post method
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            //data to send
            postData.append(requestString);
            String encodedData = postData.toString();
            // send data by byte
            conn.setRequestProperty("Content-Language", "en-US");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestProperty("Content-Length", (new Integer(encodedData.length())).toString());
            byte[] postDataByte = postData.toString().getBytes("UTF-8");
            try {
                OutputStream out = conn.getOutputStream();
                out.write(postDataByte);
                out.close();
            } catch (IOException ex) {
                addErrorLog(ex);

            }
            //get data from server
            InputStreamReader isr = new InputStreamReader(conn.getInputStream());
            BufferedReader buf = new BufferedReader(isr);

            //write
            result = buf.readLine();

        } catch (Exception ex) {
            addErrorLog(ex);

        }
        return result;

    }

    public static String toBirthday(Long age) {
        String bir = null;
        if (age != null) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(new Date());
            cal.add(Calendar.YEAR, (0 - age.intValue()));
            bir = DateFormat.format_yyyyMMdd(cal.getTime());
        }
        return bir;
    }

    public static String encodeString(String input) throws Exception {
        MessageDigest messageDigest = MessageDigest.getInstance("MD5");
        messageDigest.update(input.getBytes());
        byte byteData[] = messageDigest.digest();
        //
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < byteData.length; i++) {
            sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
        }
        return sb.toString();
    }

    private static final String REPLACE_WORD = "※※※";

    private static Integer last_serverVersion = 0;
    private static Integer last_serverVersion2 = 0;

    private static Collection<String> bannedWords = new HashSet<>();
    private static final ReentrantReadWriteLock rwl = new ReentrantReadWriteLock();

    public static String replaceBannedWord(String originalString) {
        String pattern = "ぱちんこ";
        if (originalString != null && originalString.equals(pattern)) {
            return originalString;
        }
        if (originalString == null || originalString.isEmpty()) {
            return "";
        }

        boolean reload_flg = false;
        try {
            Integer serverVersion = BannedWordDAO.getLastestVersion();
            if (last_serverVersion.compareTo(serverVersion) != 0) {
                last_serverVersion = serverVersion;
                reload_flg = true;
            }
            Integer serverVersion2 = ReplaceWordDAO.getLastestVersion();
            if (last_serverVersion2.compareTo(serverVersion2) != 0) {
                last_serverVersion2 = serverVersion2;
                reload_flg = true;
            }
        } catch (EazyException ex) {
            Util.addErrorLog(ex);
        }
        if (reload_flg) {
            rwl.writeLock().lock();
            try {
                if (bannedWords != null) {
                    bannedWords.clear();
                }
                Collection<String> bannedWords_add = BannedWordManger.toCollection();
                if (bannedWords_add != null) {
                    for (String bannedWord : bannedWords_add) {
                        bannedWords.add(bannedWord);
                    }
                }

                List<String> bannedword;
                try {
                    bannedword = ReplaceWordDAO.getList(1);
                    if (bannedword != null) {
                        for (int i = 0; i < bannedword.size(); i++) {
                            bannedWords.add(bannedword.get(i).toString());
                        }
                    }
                } catch (EazyException ex) {
                    Util.addErrorLog(ex);
                }
                List<String> word;
                try {
                    word = BannedWordDAO.getList(1);
                    for (int i = 0; i < word.size(); i++) {
                        bannedWords.add(word.get(i));
                    }
                } catch (EazyException ex) {
                    Util.addErrorLog(ex);
                }
            } finally {
                rwl.writeLock().unlock();
            }
        }
        String result = originalString;
        rwl.readLock().lock();
        try {
            if (bannedWords != null) {
                for (String bannedWord : bannedWords) {
                    try {
                        result = result.replaceAll(bannedWord, REPLACE_WORD);
                    } catch (Exception ex) {
                        Util.addErrorLog(ex);
                    }
                }
            }
        } finally {
            rwl.readLock().unlock();
        }
        return result;
    }

    public static String replaceBannedWordBackend(String originalString) {
        String pattern = "ぱちんこ";
        if (originalString != null && originalString.equals(pattern)) {
            return originalString;
        }
        if (originalString == null || originalString.isEmpty()) {
            return "";
        }
        boolean reload_flg = false;
        try {
            Integer serverVersion = BannedWordDAO.getLastestVersion();
            if (last_serverVersion.compareTo(serverVersion) != 0) {
                last_serverVersion = serverVersion;
                reload_flg = true;
            }
            Integer serverVersion2 = ReplaceWordDAO.getLastestVersion();
            if (last_serverVersion2.compareTo(serverVersion2) != 0) {
                last_serverVersion2 = serverVersion2;
                reload_flg = true;
            }
        } catch (EazyException ex) {
            Util.addErrorLog(ex);
        }
        if (reload_flg) {
            rwl.writeLock().lock();
            try {
                if (bannedWords != null) {
                    bannedWords.clear();
                }
                Collection<String> bannedWords_add = BannedWordManger.toCollection();
                if (bannedWords_add != null) {
                    for (String bannedWord : bannedWords_add) {
                        bannedWords.add(bannedWord);
                    }
                }

                List<String> bannedword;
                try {
                    bannedword = ReplaceWordDAO.getList(1);
                    if (bannedword != null) {
                        for (int i = 0; i < bannedword.size(); i++) {
                            bannedWords.add(bannedword.get(i).toString());
                        }
                    }
                } catch (EazyException ex) {
                    Util.addErrorLog(ex);
                }
                List<String> word;
                try {
                    word = BannedWordDAO.getList(1);
                    for (int i = 0; i < word.size(); i++) {
                        bannedWords.add(word.get(i));
                    }
                } catch (EazyException ex) {
                    Util.addErrorLog(ex);
                }
            } finally {
                rwl.writeLock().unlock();
            }
        }
        String result = originalString;
        rwl.readLock().lock();
        try {
            if (bannedWords != null) {
                for (String bannedWord : bannedWords) {
                    try {
                        result = result.replaceAll(bannedWord, REPLACE_WORD + "(" + bannedWord + ")");
                    } catch (Exception ex) {
                        Util.addErrorLog(ex);
                    }
                }
            }
        } finally {
            rwl.readLock().unlock();
        }
        return result;
    }

    public static String replaceWordChat(String originalString) {
        String result = originalString;

        if (result != null && !result.isEmpty()) {

            List<String> word = new ArrayList<String>();
//            word.add("http://");
            word.add("crea-tv.jp");
            word.add("gran-tv.jp");
            word.add("girls-chat.tv");
            word.add("famu.jp");
            word.add("mocom.tv");
            word.add("ガルチャ");
            word.add("モコム");
            word.add("ファム");
//            word.add(".com");
            word.add(".net");
            word.add(".jp");
            word.add(".tv");

            if (word != null) {
                for (String bannedWord : word) {
                    try {
                        result = result.replaceAll(bannedWord, REPLACE_WORD);
                    } catch (Exception ex) {
                        Util.addErrorLog(ex);
                    }
                }
            }
        }
//        }
        return result;
    }

    public static String replaceWordChatBackend(String originalString) {
        String result = originalString;

        if (result != null && !result.isEmpty()) {

            List<String> word = new ArrayList<String>();
            word.add("http://");
            word.add("crea-tv.jp");
            word.add("gran-tv.jp");
            word.add("girls-chat.tv");
            word.add("famu.jp");
            word.add("mocom.tv");
            word.add("ガルチャ");
            word.add("モコム");
            word.add("ファム");
            word.add(".com");
            word.add(".net");
            word.add(".jp");
            word.add(".tv");

            if (word != null) {
                for (String bannedWord : word) {
                    try {
                        Util.addDebugLog("replaceWordChatBackend originalString " + originalString);
                        if (originalString.contains(bannedWord)) {
                            Util.addDebugLog("replaceWordChatBackend " + bannedWord);
                            result = result.replaceAll(bannedWord, REPLACE_WORD + "(" + bannedWord + ")");
                        }
                    } catch (Exception ex) {
                        Util.addErrorLog(ex);
                    }
                }
            }
        }
//        }
        return result;
    }

    public static Integer getIntValue(Object o) {
        Integer result = 0; // DONT CHANGE DEFAULT VALUE IN ANY CASE
        if (o != null)
            result = (Integer) o;
        return result;
    }
    
    public static void getDuration(String action, long startTime){
        long endTime = System.nanoTime();
        long duration = (endTime - startTime);
        DailyLogger.debug("**@$==$@** "+action+" ================= "+duration);
    }
    //HUNGDT
//    public static String GeoIPGetCountry(String ip) {
//        String result = "JP";
//        String sep = System.getProperty("file.separator");
//        String dir = "/usr/local/share/GeoIP";
//        String dbfile = dir + sep + "GeoIP.dat";
//        try {
//
//            LookupService cl = new LookupService(dbfile, LookupService.GEOIP_MEMORY_CACHE);
//            result = cl.getCountry(ip).getCode();
//            cl.close();
//        } catch (IOException e) {
//
//            Util.addErrorLog(e);
//        }
//        return result;
//
//    }

}
