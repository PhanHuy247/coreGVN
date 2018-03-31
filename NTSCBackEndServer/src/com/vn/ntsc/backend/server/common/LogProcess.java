/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.vn.ntsc.backend.server.common;

import eazycommon.constant.ErrorCode;
import eazycommon.constant.FilesAndFolders;
import eazycommon.exception.EazyException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import com.vn.ntsc.backend.Config;
import com.vn.ntsc.backend.dao.user.UserDAO;
import com.vn.ntsc.backend.entity.IEntity;
import com.vn.ntsc.backend.entity.OneObjectLog;
import com.vn.ntsc.backend.entity.TwoObjectLog;
import com.vn.ntsc.backend.entity.impl.datarespond.CSVData;
import com.vn.ntsc.backend.entity.impl.user.BaseUser;
import com.vn.ntsc.backend.entity.impl.user.User;
import com.vn.ntsc.backend.server.respond.Respond;
import com.vn.ntsc.backend.server.respond.impl.csv.CSVCreator;
import com.vn.ntsc.backend.server.respond.result.EntityRespond;

/**
 *
 * @author RuAc0n
 */
public class LogProcess {
    private static final int timezone_error = 4;    
   
    public static <T> void getOneObjectInfor(List<T> ll) throws EazyException {
        List<String> list = new ArrayList<>();
        List<String> partnerList = new ArrayList<>();
        for (T l : ll) {
            list.add(((OneObjectLog) l).userId);
            String partnerId = ((OneObjectLog) l).partnerId;
            if (partnerId != null) {
                partnerList.add(partnerId);
            }
        }
        Map<String, IEntity> mapName = UserDAO.getMapLogInfor(list);
        //Thanhdd fixbug #4884 
        Map<String, String> partnerName = null;
        if (partnerList.size()>0){
            partnerName=    UserDAO.getUserName(partnerList); 
        }
        for (T l : ll) {
            IEntity user = mapName.get(((OneObjectLog) l).userId);
            if(user == null){
                System.out.println(((OneObjectLog) l).userId);
            }
            ((OneObjectLog) l).email = ((User) user).email;
            ((OneObjectLog) l).userName = ((User) user).username;
            ((OneObjectLog) l).cmCode = ((User) user).cmCode;
            ((OneObjectLog) l).userType = ((User) user).userType.intValue();
            String partnerId = ((OneObjectLog) l).partnerId;
            if (partnerId != null) {
                String partName = partnerName.get(partnerId);
                ((OneObjectLog) l).partnerName = partName;
            }            
        }
    }

    public static <T> void getTwoObjectInfor(List<T> ll) throws EazyException {
        List<String> listCheck = new ArrayList<>();
        List<String> listPartner = new ArrayList<>();
        for (T l : ll) {
            String requestId = ((TwoObjectLog) l).getReqId();
            if(!listCheck.contains(requestId))
                listCheck.add(requestId);
            String partnerId = ((TwoObjectLog)l).getPartnerId();
            if(!listPartner.contains(partnerId))
                listPartner.add(partnerId);
            
        }
        List<IEntity> removeList = new ArrayList<IEntity>();        
        Map<String, IEntity> mapRequestName = UserDAO.getMapLogInfor(listCheck);
        Map<String, IEntity> mapPartnerName = UserDAO.getMapLogInfor(listPartner);
        for (T l : ll) {
            IEntity requestUser = mapRequestName.get(((TwoObjectLog) l).getReqId());
            IEntity partnerUser = mapPartnerName.get(((TwoObjectLog) l).getPartnerId());
            if(requestUser != null && partnerUser != null){
                User request = (User) requestUser;
                ((TwoObjectLog) l).setReqeusetInfor(request.username, request.userType.intValue(), request.email, request.cmCode);
                User partner = (User) partnerUser;
                ((TwoObjectLog) l).setPartnerInfor(partner.username, partner.userType.intValue(), partner.email, partner.cmCode);
            }else{
                removeList.add((IEntity)l);
            }
        }
        if (!removeList.isEmpty()) {
            for (IEntity en : removeList) {
                ll.remove(en);
            }
        }        
    }    
 
    public static <T> void getShakeChatInfor(List<T> ll) throws EazyException {
        List<String> listCheck = new ArrayList<String>();
        List<String> listPartner = new ArrayList<String>();
        for (T l : ll) {
            String requestId = ((TwoObjectLog) l).getReqId();
            if(!listCheck.contains(requestId))
                listCheck.add(requestId);
            String partnerId = ((TwoObjectLog)l).getPartnerId();
            if(partnerId != null){
                if(!listPartner.contains(partnerId))
                    listPartner.add(partnerId);
            }
        }    
        Map<String, IEntity> mapRequestName = UserDAO.getMapLogInfor(listCheck);
        Map<String, IEntity> mapPartnerName = UserDAO.getMapLogInfor(listPartner);
        for (T l : ll) {
            TwoObjectLog shakeLog = (TwoObjectLog) l;
            IEntity requestUser = mapRequestName.get(shakeLog.getReqId());
            User request = (User) requestUser;
            ((TwoObjectLog) l).setReqeusetInfor(request.username, request.userType.intValue(), request.email, request.cmCode);
            String partnerId = shakeLog.getPartnerId();
            if(partnerId != null){
                IEntity partnerUser = mapPartnerName.get(partnerId);
                User partner = (User) partnerUser;
                ((TwoObjectLog) l).setPartnerInfor(partner.username, partner.userType.intValue(), partner.email, partner.cmCode);
            }
        }        
    }     
    
    public static <T>Respond createCSV(String api, Long csv, List<T> ll, OneObjectLog oneLog, TwoObjectLog twoLog ) throws Exception {
        if (csv < -12 || csv > 12) {
            return new Respond(timezone_error);
        }
        String csvName = FileNameCreator.getFileName(csv.intValue(), api);
        String path = FilesAndFolders.FOLDERS.CSV_FILES_FOLDER + csvName;
        if(oneLog != null){
            boolean isBaseUser = false;
            if(oneLog instanceof BaseUser)
                isBaseUser = true;
            if(!isBaseUser)
                getOneObjectInfor(ll);
            CSVCreator.createCSV(ll, oneLog.getKeys(), oneLog.getHeaders(null), oneLog.getJsonType(null), path, csv.intValue());            
        }else{
            getTwoObjectInfor(ll);
            CSVCreator.createCSV(ll, twoLog.getKeys(), twoLog.getHeaders(null), twoLog.getJsonType(null), path, csv.intValue());                    
        }
        

        String csvUrl = Config.SERVER_ADDRESS + "eazy/" + csvName;
        return new EntityRespond(ErrorCode.SUCCESS, new CSVData(csvName, csvUrl));
    }    
}
