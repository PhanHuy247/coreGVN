/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.backend.server.respond.impl.backend.news;

import eazycommon.constant.ErrorCode;
import eazycommon.constant.ParamKey;
import eazycommon.exception.EazyException;
import eazycommon.util.Util;
import org.json.simple.JSONObject;
import com.vn.ntsc.backend.dao.news.NewsDAO;
import com.vn.ntsc.backend.entity.impl.datarespond.SizedListData;
import com.vn.ntsc.backend.server.respond.IApiAdapter;
import com.vn.ntsc.backend.server.respond.Respond;
import com.vn.ntsc.backend.server.respond.result.EntityRespond;

/**
 *
 * @author HuyDX
 */
public class ListNewsApi implements IApiAdapter{

    @Override
    public Respond execute(JSONObject obj) {
        Respond response = new Respond();
        try {
            int skip = Util.getLongParam(obj, ParamKey.SKIP).intValue();
            int take = Util.getLongParam(obj, ParamKey.TAKE).intValue();
            
            SizedListData data = NewsDAO.list(skip, take);

//            if (data==null){
//                List<News> newsList = new ArrayList<>();
//                News news[] = new News[13];
//                news[0] = new News();
//                news[0].setIsShown(true);
//                news[0].setFromStr(DateFormat.format_yyyyMMddHHmm(Util.getGMTTime().getTime()-100000));
//                news[0].setToStr(DateFormat.format_yyyyMMddHHmm(Util.getGMTTime().getTime()-50000));
//                news[0].setUpdateDateStr(DateFormat.format_yyyyMMddHHmm(Util.getGMTTime().getTime()));
//                news[0].setBannerId("57075452e4b079b703265d42");
//                news[0].setContent("mockupContent1");
//                news[0].setUpdateDate(Util.getGMTTime().getTime());
//                news[0].setFrom(Util.getGMTTime().getTime()-100000);
//                news[0].setTo(Util.getGMTTime().getTime()- 50000);
//                news[0].setId("1234");
//                news[0].setStatus(-1);
//                news[0].setDevice_type(1);
//                news[0].setTargetGender(1);
//                news[0].setTitle("mockup1");
//                
//                news[1] = new News();
//                news[1].setIsShown(true);
//                news[1].setFromStr(DateFormat.format_yyyyMMddHHmm(Util.getGMTTime().getTime()-30000));
//                news[1].setToStr(DateFormat.format_yyyyMMddHHmm(Util.getGMTTime().getTime()+ 40000));
//                news[1].setUpdateDateStr(DateFormat.format_yyyyMMddHHmm(Util.getGMTTime().getTime()));
//                news[1].setContent("mockupContent2");
//                news[1].setUpdateDate(Util.getGMTTime().getTime());
//                news[1].setFrom(Util.getGMTTime().getTime());
//                news[1].setTo(Util.getGMTTime().getTime()+ 40000);
//                news[1].setId("5678");
//                news[1].setStatus(0);
//                news[1].setDevice_type(0);
//                news[1].setTargetGender(0);
//                news[1].setTitle("mockup2");
//                
//                news[2] = new News();
//                news[2].setIsShown(true);
//                news[2].setFromStr(DateFormat.format_yyyyMMddHHmm(Util.getGMTTime().getTime()+100000));
//                news[2].setToStr(DateFormat.format_yyyyMMddHHmm(Util.getGMTTime().getTime()+ 200000));
//                news[2].setUpdateDateStr(DateFormat.format_yyyyMMddHHmm(Util.getGMTTime().getTime()));                
//                news[2].setBannerId("57075452e4b079b703265d42");
//                news[2].setContent("mockupContent3");
//                news[2].setUpdateDate(Util.getGMTTime().getTime());
//                news[2].setFrom(Util.getGMTTime().getTime()+100000);
//                news[2].setTo(Util.getGMTTime().getTime()+ 200000);
//                news[2].setId("9101112");
//                news[2].setStatus(1);
//                news[2].setDevice_type(0);
//                news[2].setTargetGender(0);
//                news[2].setTitle("mockup3");
//                
//                news[3] = new News();
//                news[3].setIsShown(false);
//                news[3].setFromStr(DateFormat.format_yyyyMMddHHmm(Util.getGMTTime().getTime()-100000));
//                news[3].setToStr(DateFormat.format_yyyyMMddHHmm(Util.getGMTTime().getTime()-50000));
//                news[3].setUpdateDateStr(DateFormat.format_yyyyMMddHHmm(Util.getGMTTime().getTime()));                
//                news[3].setBannerId("57075452e4b079b703265d42");
//                news[3].setContent("mockupContent4");
//                news[3].setUpdateDate(Util.getGMTTime().getTime());
//                news[3].setFrom(Util.getGMTTime().getTime()-100000);
//                news[3].setTo(Util.getGMTTime().getTime()- 50000);
//                news[3].setId("4");
//                news[3].setStatus(-1);
//                news[3].setDevice_type(1);
//                news[3].setTargetGender(1);
//                news[3].setTitle("mockup4");
//     
//                news[5] = new News();
//                news[5].setIsShown(false);
//                news[5].setFromStr(DateFormat.format_yyyyMMddHHmm(Util.getGMTTime().getTime()-30000));
//                news[5].setToStr(DateFormat.format_yyyyMMddHHmm(Util.getGMTTime().getTime()+ 40000));
//                news[5].setUpdateDateStr(DateFormat.format_yyyyMMddHHmm(Util.getGMTTime().getTime()));                
//                news[5].setContent("mockupContent5");
//                news[5].setUpdateDate(Util.getGMTTime().getTime());
//                news[5].setFrom(Util.getGMTTime().getTime());
//                news[5].setTo(Util.getGMTTime().getTime()+ 40000);
//                news[5].setId("6");
//                news[5].setStatus(0);
//                news[5].setDevice_type(0);
//                news[5].setTargetGender(0);
//                news[5].setTitle("mockup5");
//                
//                news[6] = new News();
//                news[6].setIsShown(true);
//                news[6].setFromStr(DateFormat.format_yyyyMMddHHmm(Util.getGMTTime().getTime()-30000));
//                news[6].setToStr(DateFormat.format_yyyyMMddHHmm(Util.getGMTTime().getTime()+ 40000));
//                news[6].setUpdateDateStr(DateFormat.format_yyyyMMddHHmm(Util.getGMTTime().getTime()));                
//                news[6].setContent("mockupContent13");
//                news[6].setUpdateDate(Util.getGMTTime().getTime());
//                news[6].setFrom(Util.getGMTTime().getTime());
//                news[6].setTo(Util.getGMTTime().getTime()+ 40000);
//                news[6].setId("13");
//                news[6].setStatus(0);
//                news[6].setDevice_type(0);
//                news[6].setTargetGender(0);
//                news[6].setTitle("mockup13");
//                
//                news[4] = new News();
//                news[4].setIsShown(true);
//                news[4].setFromStr(DateFormat.format_yyyyMMddHHmm(Util.getGMTTime().getTime()+100000));
//                news[4].setToStr(DateFormat.format_yyyyMMddHHmm(Util.getGMTTime().getTime()+ 200000));
//                news[4].setUpdateDateStr(DateFormat.format_yyyyMMddHHmm(Util.getGMTTime().getTime()));                
//                news[4].setBannerId("57075452e4b079b703265d42");
//                news[4].setContent("mockupContent6");
//                news[4].setUpdateDate(Util.getGMTTime().getTime());
//                news[4].setFrom(Util.getGMTTime().getTime()+100000);
//                news[4].setTo(Util.getGMTTime().getTime()+ 200000);
//                news[4].setId("5");
//                news[4].setStatus(1);
//                news[4].setDevice_type(0);
//                news[4].setTargetGender(0);
//                news[4].setTitle("mockup6");
//                
//                news[9] = new News();
//                news[9].setIsShown(true);
//                news[9].setFromStr(DateFormat.format_yyyyMMddHHmm(Util.getGMTTime().getTime()-100000));
//                news[9].setToStr(DateFormat.format_yyyyMMddHHmm(Util.getGMTTime().getTime()-50000));
//                news[9].setUpdateDateStr(DateFormat.format_yyyyMMddHHmm(Util.getGMTTime().getTime()));                
//                news[9].setBannerId("57075452e4b079b703265d42");
//                news[9].setContent("mockupContent9");
//                news[9].setUpdateDate(Util.getGMTTime().getTime());
//                news[9].setFrom(Util.getGMTTime().getTime()-100000);
//                news[9].setTo(Util.getGMTTime().getTime()- 50000);
//                news[9].setId("9");
//                news[9].setStatus(-1);
//                news[9].setDevice_type(1);
//                news[9].setTargetGender(1);
//                news[9].setTitle("mockup9");
//                
//                news[8] = new News();
//                news[8].setIsShown(true);
//                news[8].setFromStr(DateFormat.format_yyyyMMddHHmm(Util.getGMTTime().getTime()-30000));
//                news[8].setToStr(DateFormat.format_yyyyMMddHHmm(Util.getGMTTime().getTime()+ 40000));
//                news[8].setUpdateDateStr(DateFormat.format_yyyyMMddHHmm(Util.getGMTTime().getTime()));                
//                news[8].setContent("mockupContent8");
//                news[8].setUpdateDate(Util.getGMTTime().getTime());
//                news[8].setFrom(Util.getGMTTime().getTime());
//                news[8].setTo(Util.getGMTTime().getTime()+ 40000);
//                news[8].setId("8");
//                news[8].setStatus(0);
//                news[8].setDevice_type(0);
//                news[8].setTargetGender(0);
//                news[8].setTitle("mockup8");
//                
//                news[7] = new News();
//                news[7].setIsShown(true);
//                news[7].setFromStr(DateFormat.format_yyyyMMddHHmm(Util.getGMTTime().getTime()+100000));
//                news[7].setToStr(DateFormat.format_yyyyMMddHHmm(Util.getGMTTime().getTime()+ 200000));
//                news[7].setUpdateDateStr(DateFormat.format_yyyyMMddHHmm(Util.getGMTTime().getTime()));                
//                news[7].setBannerId("57075452e4b079b703265d42");
//                news[7].setContent("mockupContent7");
//                news[7].setUpdateDate(Util.getGMTTime().getTime());
//                news[7].setFrom(Util.getGMTTime().getTime()+100000);
//                news[7].setTo(Util.getGMTTime().getTime()+ 200000);
//                news[7].setId("7");
//                news[7].setStatus(1);
//                news[7].setDevice_type(0);
//                news[7].setTargetGender(0);
//                news[7].setTitle("mockup7");
//                
//                news[10] = new News();
//                news[10].setIsShown(true);
//                news[10].setFromStr(DateFormat.format_yyyyMMddHHmm(Util.getGMTTime().getTime()-100000));
//                news[10].setToStr(DateFormat.format_yyyyMMddHHmm(Util.getGMTTime().getTime()-50000));
//                news[10].setUpdateDateStr(DateFormat.format_yyyyMMddHHmm(Util.getGMTTime().getTime()));                
//                news[10].setBannerId("57075452e4b079b703265d42");
//                news[10].setContent("mockupContent10");
//                news[10].setUpdateDate(Util.getGMTTime().getTime());
//                news[10].setFrom(Util.getGMTTime().getTime()-100000);
//                news[10].setTo(Util.getGMTTime().getTime()- 50000);
//                news[10].setId("10");
//                news[10].setStatus(-1);
//                news[10].setDevice_type(1);
//                news[10].setTargetGender(1);
//                news[10].setTitle("mockup10");
//                
//                news[11] = new News();
//                news[11].setIsShown(true);
//                news[11].setFromStr(DateFormat.format_yyyyMMddHHmm(Util.getGMTTime().getTime()-30000));
//                news[11].setToStr(DateFormat.format_yyyyMMddHHmm(Util.getGMTTime().getTime()+ 40000));
//                news[11].setUpdateDateStr(DateFormat.format_yyyyMMddHHmm(Util.getGMTTime().getTime()));                
//                news[11].setContent("mockupContent11");
//                news[11].setUpdateDate(Util.getGMTTime().getTime());
//                news[11].setUpdateDateStr(DateFormat.format_yyyyMMddHHmm(Util.getGMTTime().getTime()));
//                news[11].setFrom(Util.getGMTTime().getTime());
//                news[11].setTo(Util.getGMTTime().getTime()+ 40000);
//                news[11].setId("11");
//                news[11].setStatus(0);
//                news[11].setDevice_type(0);
//                news[11].setTargetGender(0);
//                news[11].setTitle("mockup11");
//                
//                news[12] = new News();
//                news[12].setIsShown(true);
//                news[12].setFromStr(DateFormat.format_yyyyMMddHHmm(Util.getGMTTime().getTime()+100000));
//                news[12].setToStr(DateFormat.format_yyyyMMddHHmm(Util.getGMTTime().getTime()+ 200000));
//                news[12].setUpdateDateStr(DateFormat.format_yyyyMMddHHmm(Util.getGMTTime().getTime()));                
//                news[12].setBannerId("57075452e4b079b703265d42");
//                news[12].setContent("mockupContent12");
//                news[12].setUpdateDate(Util.getGMTTime().getTime());
//                news[12].setFrom(Util.getGMTTime().getTime()+100000);
//                news[12].setTo(Util.getGMTTime().getTime()+ 200000);
//                news[12].setId("12");
//                news[12].setStatus(1);
//                news[12].setDevice_type(0);
//                news[12].setTargetGender(0);
//                news[12].setTitle("mockup12");
//                
//                int MAXSIZE = 13;
//                if (skip>MAXSIZE)
//                    skip =MAXSIZE;
//                if (skip+take>MAXSIZE)
//                    take = MAXSIZE-skip;
//                for (int i=skip; i<skip+take; i++){
//                    newsList.add(news[i]);
//                    data = new SizedListData(MAXSIZE, newsList);
//                }
//            }
            
            response = new EntityRespond(ErrorCode.SUCCESS, data);
        } catch (EazyException ex) {
            Util.addErrorLog(ex);
            response = new EntityRespond(ex.getErrorCode());
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
        
        return response;
    }
    
}
