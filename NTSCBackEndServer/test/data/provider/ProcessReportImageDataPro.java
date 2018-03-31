/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package data.provider;

import eazycommon.constant.Constant;
import eazycommon.constant.ErrorCode;
import org.testng.annotations.DataProvider;
import test.All;

/**
 *
 * @author duyetpt
 */
public class ProcessReportImageDataPro {

    public static final String DATA_NAME = "data";

    @DataProvider(name = DATA_NAME)
    public static Object[][] provideData() {
        int accept = 1;
        int deny = -1;
        int pendding = 0;
        //int type,   int imageFlag,  int reportFlag,
        //int appFlag, int appearFlag, int imageType,
        //int avatarFlag, int ErrorCode
        return new Object[][]{
            //Accept
            //case flag = NO
All.$(accept, Constant.FLAG.OFF, 1, 1, 1, 1, 1, ErrorCode.SUCCESS),
            //             case flag = YES, reportFlag = GOOD
All.$(accept, Constant.FLAG.ON, Constant.FLAG.ON, 1, 1, 1, 1, ErrorCode.SUCCESS),
            //            case: flag = Yes, reportFlag = PENDING, appearFlag = yes
All.$(accept, Constant.FLAG.ON, Constant.REVIEW_STATUS_FLAG.PENDING, 1, Constant.FLAG.ON, 1, 1, ErrorCode.SUCCESS),
            //            case : flag = Yes, reportFlag = PENDING, appearFlag = No, imageType = backstage
All.$(accept, Constant.FLAG.ON, Constant.REVIEW_STATUS_FLAG.PENDING, 1, Constant.FLAG.OFF, Constant.IMAGE_TYPE_VALUE.IMAGE_BACKSTAGE, 1, ErrorCode.SUCCESS),
            //case : flag = Yes, reportFlag = PENDING, appearFlag = No, imageType = public, avatarFlag = NO
All.$(accept, Constant.FLAG.ON, Constant.REVIEW_STATUS_FLAG.PENDING, 1, Constant.FLAG.OFF, Constant.IMAGE_TYPE_VALUE.IMAGE_PUBLIC, Constant.FLAG.OFF, ErrorCode.SUCCESS),
            //case : flag = Yes, reportFlag = PENDING, appearFlag = No, imageType = public, avatarFlag = YES
All.$(accept, Constant.FLAG.ON, Constant.REVIEW_STATUS_FLAG.PENDING, 1, Constant.FLAG.OFF, Constant.IMAGE_TYPE_VALUE.IMAGE_PUBLIC, Constant.FLAG.ON, ErrorCode.SUCCESS),
            // case : falg = YES, reportFlag =  NOT_GOOD, imageType = backstage
All.$(accept, Constant.FLAG.ON, Constant.REPORT_STATUS_FLAG.NOT_GOOD, 1, Constant.FLAG.OFF, Constant.IMAGE_TYPE_VALUE.IMAGE_BACKSTAGE, 1, ErrorCode.SUCCESS),
            //case : flag = Yes, reportFlag = NOT_GOOD , imageType = public, avatarFlag = NO
All.$(accept, Constant.FLAG.ON, Constant.REPORT_STATUS_FLAG.NOT_GOOD, 1, Constant.FLAG.OFF, Constant.IMAGE_TYPE_VALUE.IMAGE_PUBLIC, Constant.FLAG.OFF, ErrorCode.SUCCESS),
            //case : flag = Yes, reportFlag = NOT_GOOD, imageType = public, avatarFlag = YES
All.$(accept, Constant.FLAG.ON, Constant.REPORT_STATUS_FLAG.NOT_GOOD, 1, Constant.FLAG.OFF, Constant.IMAGE_TYPE_VALUE.IMAGE_PUBLIC, Constant.FLAG.ON, ErrorCode.SUCCESS),

             //deny
All.$(deny, Constant.FLAG.OFF, 1, 1, 1, 1, 1, ErrorCode.SUCCESS),
            //             case flag = YES, reportFlag = GOOD
All.$(deny, Constant.FLAG.ON, Constant.FLAG.ON, 1, 1, 1, 1, ErrorCode.SUCCESS),
            //            case: flag = Yes, reportFlag = PENDING, appearFlag = yes
All.$(deny, Constant.FLAG.ON, Constant.REVIEW_STATUS_FLAG.PENDING, 1, Constant.FLAG.ON, 1, 1, ErrorCode.SUCCESS),
            //            case : flag = Yes, reportFlag = PENDING, appearFlag = No, imageType = backstage
All.$(deny, Constant.FLAG.ON, Constant.REVIEW_STATUS_FLAG.PENDING, 1, Constant.FLAG.OFF, Constant.IMAGE_TYPE_VALUE.IMAGE_BACKSTAGE, 1, ErrorCode.SUCCESS),
            //case : flag = Yes, reportFlag = PENDING, appearFlag = No, imageType = public, avatarFlag = NO
All.$(deny, Constant.FLAG.ON, Constant.REVIEW_STATUS_FLAG.PENDING, 1, Constant.FLAG.OFF, Constant.IMAGE_TYPE_VALUE.IMAGE_PUBLIC, Constant.FLAG.OFF, ErrorCode.SUCCESS),
            //case : flag = Yes, reportFlag = PENDING, appearFlag = No, imageType = public, avatarFlag = YES
All.$(deny, Constant.FLAG.ON, Constant.REVIEW_STATUS_FLAG.PENDING, 1, Constant.FLAG.OFF, Constant.IMAGE_TYPE_VALUE.IMAGE_PUBLIC, Constant.FLAG.ON, ErrorCode.SUCCESS),
            // case : falg = YES, reportFlag =  NOT_GOOD, imageType = backstage
All.$(deny, Constant.FLAG.ON, Constant.REPORT_STATUS_FLAG.NOT_GOOD, 1, Constant.FLAG.OFF, Constant.IMAGE_TYPE_VALUE.IMAGE_BACKSTAGE, 1, ErrorCode.SUCCESS),
            //case : flag = Yes, reportFlag = NOT_GOOD , imageType = public, avatarFlag = NO
All.$(deny, Constant.FLAG.ON, Constant.REPORT_STATUS_FLAG.NOT_GOOD, 1, Constant.FLAG.OFF, Constant.IMAGE_TYPE_VALUE.IMAGE_PUBLIC, Constant.FLAG.OFF, ErrorCode.SUCCESS),
            //case : flag = Yes, reportFlag = NOT_GOOD, imageType = public, avatarFlag = YES
All.$(deny, Constant.FLAG.ON, Constant.REPORT_STATUS_FLAG.NOT_GOOD, 1, Constant.FLAG.OFF, Constant.IMAGE_TYPE_VALUE.IMAGE_PUBLIC, Constant.FLAG.ON, ErrorCode.SUCCESS),
            
            // pendding
All.$(pendding, Constant.FLAG.OFF, 1, 1, 1, 1, 1, ErrorCode.SUCCESS),
            //             case flag = YES, reportFlag = GOOD
All.$(pendding, Constant.FLAG.ON, Constant.FLAG.ON, 1, 1, 1, 1, ErrorCode.SUCCESS),
            //            case: flag = Yes, reportFlag = PENDING, appearFlag = yes
All.$(pendding, Constant.FLAG.ON, Constant.REVIEW_STATUS_FLAG.PENDING, 1, Constant.FLAG.ON, 1, 1, ErrorCode.SUCCESS),
            //            case : flag = Yes, reportFlag = PENDING, appearFlag = No, imageType = backstage
All.$(pendding, Constant.FLAG.ON, Constant.REVIEW_STATUS_FLAG.PENDING, 1, Constant.FLAG.OFF, Constant.IMAGE_TYPE_VALUE.IMAGE_BACKSTAGE, 1, ErrorCode.SUCCESS),
            //case : flag = Yes, reportFlag = PENDING, appearFlag = No, imageType = public, avatarFlag = NO
All.$(pendding, Constant.FLAG.ON, Constant.REVIEW_STATUS_FLAG.PENDING, 1, Constant.FLAG.OFF, Constant.IMAGE_TYPE_VALUE.IMAGE_PUBLIC, Constant.FLAG.OFF, ErrorCode.SUCCESS),
            //case : flag = Yes, reportFlag = PENDING, appearFlag = No, imageType = public, avatarFlag = YES
All.$(pendding, Constant.FLAG.ON, Constant.REVIEW_STATUS_FLAG.PENDING, 1, Constant.FLAG.OFF, Constant.IMAGE_TYPE_VALUE.IMAGE_PUBLIC, Constant.FLAG.ON, ErrorCode.SUCCESS),
            // case : falg = YES, reportFlag =  NOT_GOOD, imageType = backstage
All.$(pendding, Constant.FLAG.ON, Constant.REPORT_STATUS_FLAG.NOT_GOOD, 1, Constant.FLAG.OFF, Constant.IMAGE_TYPE_VALUE.IMAGE_BACKSTAGE, 1, ErrorCode.SUCCESS),
            //case : flag = Yes, reportFlag = NOT_GOOD , imageType = public, avatarFlag = NO
All.$(pendding, Constant.FLAG.ON, Constant.REPORT_STATUS_FLAG.NOT_GOOD, 1, Constant.FLAG.OFF, Constant.IMAGE_TYPE_VALUE.IMAGE_PUBLIC, Constant.FLAG.OFF, ErrorCode.SUCCESS),
            //case : flag = Yes, reportFlag = NOT_GOOD, imageType = public, avatarFlag = YES
All.$(pendding, Constant.FLAG.ON, Constant.REPORT_STATUS_FLAG.NOT_GOOD, 1, Constant.FLAG.OFF, Constant.IMAGE_TYPE_VALUE.IMAGE_PUBLIC, Constant.FLAG.ON, ErrorCode.SUCCESS),
        };
    }
}
