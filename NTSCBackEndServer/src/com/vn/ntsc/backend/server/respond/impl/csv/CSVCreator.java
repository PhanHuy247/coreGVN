/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.backend.server.respond.impl.csv;

import java.lang.reflect.Field;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import eazycommon.constant.ParamKey;
import eazycommon.util.DateFormat;
import eazycommon.util.Util;
import org.json.simple.JSONObject;

/**
 *
 * @author RuAc0n
 */
public class CSVCreator {

    private static final String commas = ",";
    private static final String end_line = "\n";
    private static final String blank = "--";
    
    public static <T> void createCSV(List<T> objs, List<String> keys, List<String> headers, JSONObject jsonType, String path, int timezone) {
        String content = createContentFile(objs, keys, headers, jsonType, timezone);
        Util.writeFile(path, content, "Shift_JIS");
    }

    private static String addHeader(List<String> headers) {
        StringBuilder sb = new StringBuilder();
        Iterator<String> it = headers.iterator();
        int size = headers.size();
        while (it.hasNext()) {
            size --;
            sb.append(it.next());
            if(size > 0)
                sb.append(commas);
        }
        sb.append(end_line);
        return sb.toString();
    }
/**
 * 
 * @param <T>
 * @param objs : content data
 * @param keys : list header : english, japan
 * @param headers : japan or english
 * @param jsonType : japan or english
 * @param timezone
 * @return 
 */
    private static <T> String createContentFile(List<T> objs, List<String> keys, List<String> headers, JSONObject jsonType, int timezone) {
        StringBuilder sb = new StringBuilder();
        try {
            sb.append(addHeader(headers));
            Iterator<T> iterator = objs.iterator();
            int row = 0;
            while (iterator.hasNext()) {
                row ++;
                Object element = iterator.next();
//                System.out.println("vao " + row);
                try{
                    sb.append(addOne(element, keys, jsonType, row, timezone));
                }catch(Exception ex){
                    Util.addErrorLog(ex);
                    ex.printStackTrace();
                }
//                System.out.println("chet " + row);
            }
        } catch (Exception e) {
            Util.addErrorLog(e);            
        }
//        System.out.println("sb.toString : " + sb.toString());
        return sb.toString();
    }
    
    private static String addOne(Object element, List<String> keys, JSONObject jsonType, int row, int timezone) throws Exception {
        Map<String, String> map = new TreeMap<>();
        map.put(Headers.number, String.valueOf(row));
//        sb.append(commas);
        Class<?> objectType = element.getClass();
//
        Field[] fields = objectType.getFields();
//        int size = fields.length;
        for (Field field : fields) {
//            size --;
            field.setAccessible(true);
            TypeSwitch type = field.getAnnotation(TypeSwitch.class);
            if(type == null || type.value().equals(ParamKey.NEXT)){                
                continue;
            }
            String header = type.header();
            Object fieldValue = field.get(element);
            String value;
            if (fieldValue != null) {
                if(type.value().equals(ParamKey.PASS_VALUE)){
                    value = fieldValue.toString();
                }else{
                    if(type.value().equals(ParamKey.TIME)){
                        Date date = null;
                        date = DateFormat.parse(fieldValue.toString());
                        
                        date = convertDate(timezone, date);
                        String time = DateFormat.formatCsv(date);
                        value = time;
                    }else{
                        JSONObject json = (JSONObject) jsonType.get(type.value());
                        if(json != null){
                            if(fieldValue instanceof Integer){
                                value = (String) json.get(fieldValue);
                            }else if(fieldValue instanceof Long){
                                Integer key = Integer.parseInt(fieldValue.toString());
                                value = (String) json.get(key);                                
                            }else{
                                value = blank;
                            }
                        }else{
                            value = blank;
                        }
                    }
                }
            }else{
                value = blank;
            }
            
            map.put(header, value);
        }
        return getContent(map, keys);
    } 
    
    private static String getContent(Map<String, String> map, List<String> list){
        StringBuilder sb = new StringBuilder();
        try{
            int i = 0;
            for(String str : list){
                sb.append(map.get(str));
                i++;
                if(i < list.size())
                    sb.append(commas);
            }
            sb.append(end_line);
        }catch(Exception ex){
            Util.addErrorLog(ex);
            ex.printStackTrace();
        }
        return sb.toString();
    }
    
    private static Date convertDate (int timezone, Date date){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.HOUR, timezone);
        Date d = cal.getTime();
        return d;
    }

}
