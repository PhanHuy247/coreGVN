/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.backend.server.respond.impl.backend.setting;

import eazycommon.constant.mongokey.DAOKeys;
import org.json.simple.JSONObject;
import eazycommon.constant.ErrorCode;
import eazycommon.exception.EazyException;
import com.vn.ntsc.backend.dao.setting.PointSettingDAO;
import com.vn.ntsc.backend.server.respond.IApiAdapter;
import com.vn.ntsc.backend.server.respond.Respond;
import eazycommon.util.Util;

/**
 *
 * @author RuAc0n
 */
public class SetPointSettingApi implements IApiAdapter {

    @Override
    public Respond execute(JSONObject obj) {
        Respond respond = new Respond();
        try {
            int error = validate(obj);
//            int error = 0;
            if (error != ErrorCode.SUCCESS) {
                respond = new Respond(error);
                return respond;
            }
            PointSettingDAO.update(obj);
            respond = new Respond(ErrorCode.SUCCESS);
        } catch (EazyException ex) {
            Util.addErrorLog(ex);
            respond = new Respond(ex.getErrorCode());
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
        return respond;
    }

    private int validate(JSONObject obj) {
        int plusCode = 3;
        for (int i = 0; i < DAOKeys.one_object_add_list.size(); i++) {
            String key = DAOKeys.one_object_add_list.get(i);
            JSONObject value = (JSONObject) obj.get(key);
            if (value == null) {
//                if(!key.equals(DAOKeys.receive_gift))
                return calculate(i, 2, 1, plusCode);
//                else
//                    return calculate(i, 4, 3, plusCode);
            } else {
                int min = 0;
                int max = 1000000000;
                if(key.equals(DAOKeys.receive_gift)){
                    max = 100;
                    Long maleRequestPoint = (Long) value.get(DAOKeys.MALE_REQUEST_POINT);
                    if (!isValid(maleRequestPoint, min, max)) {
                        return calculate(i, 4, 3, plusCode);
                    }
                    Long potentialMaleRequestPoint = (Long) value.get(DAOKeys.POTENTIAL_CUSTOMER_MALE_REQUEST_POINT);
                    if (!isValid(potentialMaleRequestPoint, min, max)) {
                        return calculate(i, 4, 2, plusCode);
                    }
                    Long femaleRequestPoint = (Long) value.get(DAOKeys.FEMALE_REQUEST_POINT);
                    if (!isValid(femaleRequestPoint, min, max)) {
                        return calculate(i, 4, 1, plusCode);
                    } 
                    Long potentialFemaleRequestPoint = (Long) value.get(DAOKeys.POTENTIAL_CUSTOMER_FEMALE_REQUEST_POINT);
                    if (!isValid(potentialFemaleRequestPoint, min, max)) {
                        return calculate(i, 4, 0, plusCode);
                    } 
                }else{
                    Long maleRequestPoint = (Long) value.get(DAOKeys.MALE_REQUEST_POINT);
                    if (!isValid(maleRequestPoint, min, max)) {
                        return calculate(i, 2, 1, plusCode);
                    }
                    Long femaleRequestPoint = (Long) value.get(DAOKeys.FEMALE_REQUEST_POINT);
                    if (!isValid(femaleRequestPoint, min, max)) {
                        return calculate(i, 2, 0, plusCode);
                    }    
                }
                
            }
        }
        
        plusCode = plusCode +  DAOKeys.one_object_add_list.size()* 2;
        for (int i = 0; i < DAOKeys.one_object_minus_list.size(); i++) {
            String key = DAOKeys.one_object_minus_list.get(i);
            JSONObject value = (JSONObject) obj.get(key);
            if (value == null) {
                return calculate(i, 2, 1, plusCode);
            } else {
                int min = 0;
                int max = 1000000;
                Long maleRequestPoint = (Long) value.get(DAOKeys.MALE_REQUEST_POINT);
                if (!isValid(maleRequestPoint, min, max)) {
                    return calculate(i, 2, 1, plusCode);
                }
                Long femaleRequestPoint = (Long) value.get(DAOKeys.FEMALE_REQUEST_POINT);
                if (!isValid(femaleRequestPoint, min, max)) {
                    return calculate(i, 2, 0, plusCode);
                }  
            }
        }        
        
//        plusCode = plusCode +  DAOKeys.one_object_minus_list.size()* 2;
//        for (int i = 0; i < DAOKeys.two_object_list.size(); i++) {
//            String key = DAOKeys.two_object_list.get(i);
//            JSONObject value = (JSONObject) obj.get(key);
//            if (value == null) {
//                return calculate(i, 4, 3, plusCode);
//            } else {
//                int min = 0;
//                int max = 1000000;                
//                Long point = (Long) value.get(DAOKeys.MALE_REQUEST_POINT);
//                if (!isValid(point, min, max)) {
//                    return calculate(i, 4,3 , plusCode);
//                }
//                point = (Long) value.get(DAOKeys.FEMALE_REQUEST_POINT);
//                if (!isValid(point, min, max)) {
//                    return calculate(i, 4, 2, plusCode);
//                }                             
//                point = (Long) value.get(DAOKeys.MALE_PARTNER_POINT);
//                if (!isValid(point, min, max)) {
//                    return calculate(i, 4, 1, plusCode);
//                }
//                point = (Long) value.get(DAOKeys.FEMALE_PARTNER_POINT);
//                if (!isValid(point, min, max)) {
//                    return calculate(i, 4, 0, plusCode);
//                }                             
//            }
//        }
        return ErrorCode.SUCCESS;
    }

    private int calculate(int number, int mutiple, int minus, int plus) {
        return (number + 1) * mutiple - minus + plus;
    }

    private boolean isValid(Long number, int min, int max) {
        return number != null && number >= min && number <= max;
    }

}
