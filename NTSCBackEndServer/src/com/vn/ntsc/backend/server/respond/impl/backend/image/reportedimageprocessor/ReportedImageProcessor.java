/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.backend.server.respond.impl.backend.image.reportedimageprocessor;

import eazycommon.constant.Constant;
import eazycommon.util.Util;
import com.vn.ntsc.backend.dao.buzz.ReviewingCommentDAO;
import com.vn.ntsc.backend.dao.buzz.ReviewingSubCommentDAO;
import com.vn.ntsc.backend.dao.buzz.UserBuzzDAO;
import com.vn.ntsc.backend.dao.user.BackstageDAO;
import com.vn.ntsc.backend.dao.user.ImageDAO;
import com.vn.ntsc.backend.dao.user.PbImageDAO;
import com.vn.ntsc.backend.dao.user.UserDAO;
import com.vn.ntsc.backend.entity.impl.image.Image;
import com.vn.ntsc.backend.entity.impl.image.ReviewImage;

/**
 *
 * @author Rua
 */
public class ReportedImageProcessor {

//    public void goodToGood_approvedStatus(Image image, ReviewImage ri){
//    
//    }
    public void goodToNG_approvedStatus(Image image, ReviewImage ri) {
        try {
            String imageId = image.imageId;
            ImageDAO.updateReport(imageId, Constant.REPORT_STATUS_FLAG.NOT_GOOD);
//            ImageDAO.updateAppearFlag(imageId, Constant.NO);
            if (image.imageType == Constant.IMAGE_TYPE_VALUE.IMAGE_BACKSTAGE) {
                if (BackstageDAO.checkBackStageExist(image.userId, imageId)) {
                    UserDAO.removeBackstage(image.userId);
                    BackstageDAO.updateFlag(image.userId, imageId, Constant.AVAILABLE_FLAG_VALUE.NOT_AVAILABLE_FLAG);
                }
            } else if (image.imageType == Constant.IMAGE_TYPE_VALUE.IMAGE_PUBLIC) {
                if (PbImageDAO.checkPbImageExist(image.userId, imageId)) {
                    UserDAO.removeBuzz(image.userId);
                    UserDAO.removePbImage(image.userId);
                    PbImageDAO.updateFlag(image.userId, imageId, Constant.AVAILABLE_FLAG_VALUE.NOT_AVAILABLE_FLAG);
                    if (UserDAO.isAvatar(image.userId, imageId)) {
                        UserDAO.removeAvatar(image.userId, imageId);
                        ri.avatar = Constant.USER_STATUS_FLAG.DISABLE;
                    }
                    String buzzId = PbImageDAO.getBuzzId(image.userId, imageId);
                    ReviewingCommentDAO.updateAppearFlagByBuzzId(buzzId, Constant.FLAG.OFF);
                    ReviewingSubCommentDAO.updateAppearFlagByBuzzId(buzzId, Constant.FLAG.OFF);
                    ri.buzzId = buzzId;
//                    BuzzDetailDAO.updateApprovedFlag(buzzId, Constant.NO);
                    UserBuzzDAO.updateApproveFlag(image.userId, buzzId, Constant.REVIEW_STATUS_FLAG.DENIED);
                }
            }
        } catch (Exception ex) {
            Util.addErrorLog(ex);

        }
    }

    public void goodToWaiting_approvedStatus(Image image, ReviewImage ri) {
        try {
            String imageId = image.imageId;
            ImageDAO.updateReport(imageId, Constant.REPORT_STATUS_FLAG.WAITING);
//            ImageDAO.updateAppearFlag(imageId, Constant.NO);

            if (image.imageType == Constant.IMAGE_TYPE_VALUE.IMAGE_BACKSTAGE) {
                if (BackstageDAO.checkBackStageExist(image.userId, imageId)) {
                    UserDAO.removeBackstage(image.userId);
                    BackstageDAO.updateFlag(image.userId, imageId, Constant.AVAILABLE_FLAG_VALUE.NOT_AVAILABLE_FLAG);
                }
            } else if (image.imageType == Constant.IMAGE_TYPE_VALUE.IMAGE_PUBLIC) {
                if (PbImageDAO.checkPbImageExist(image.userId, imageId)) {
                    UserDAO.removePbImage(image.userId);
                    UserDAO.removeBuzz(image.userId);
                    PbImageDAO.updateFlag(image.userId, imageId, Constant.AVAILABLE_FLAG_VALUE.NOT_AVAILABLE_FLAG);
                    if (UserDAO.isAvatar(image.userId, imageId)) {
                        UserDAO.removeAvatar(image.userId, imageId);
                        ri.avatar = Constant.USER_STATUS_FLAG.DISABLE;
                    }
                    String buzzId = PbImageDAO.getBuzzId(image.userId, imageId);
                    ReviewingCommentDAO.updateAppearFlagByBuzzId(buzzId, Constant.FLAG.OFF);
                    ReviewingSubCommentDAO.updateAppearFlagByBuzzId(buzzId, Constant.FLAG.OFF);
                    ri.buzzId = buzzId;
//                    BuzzDetailDAO.updateApprovedFlag(buzzId, Constant.NO);
                    UserBuzzDAO.updateApproveFlag(image.userId, buzzId, Constant.REVIEW_STATUS_FLAG.DENIED);
                }
            }
            

        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
    }

    public void NGToGood_approvedStatus(Image image, ReviewImage ri) {
        try {
            String imageId = image.imageId;
            ImageDAO.updateReport(imageId, Constant.REPORT_STATUS_FLAG.GOOD);
//            ImageDAO.updateAppearFlag(imageId, Constant.NO);
            if (image.imageType == Constant.IMAGE_TYPE_VALUE.IMAGE_BACKSTAGE) {
                UserDAO.addBackstage(image.userId);
                BackstageDAO.updateFlag(image.userId, imageId, Constant.AVAILABLE_FLAG_VALUE.AVAILABE_FLAG);
            } else if (image.imageType == Constant.IMAGE_TYPE_VALUE.IMAGE_PUBLIC) {
                UserDAO.addPbImage(image.userId);
                UserDAO.addBuzz(image.userId);
                PbImageDAO.updateFlag(image.userId, imageId, Constant.AVAILABLE_FLAG_VALUE.AVAILABE_FLAG);
                if (image.avatarFlag == Constant.FLAG.ON) {
                    UserDAO.updateAvatar(image.userId, imageId);
                    ri.avatar = Constant.FLAG.ON;
                }
                String buzzId = PbImageDAO.getBuzzId(image.userId, imageId);
                ReviewingCommentDAO.updateAppearFlagByBuzzId(buzzId, Constant.FLAG.ON);
                ReviewingSubCommentDAO.updateAppearFlagByBuzzId(buzzId, Constant.FLAG.ON);
////                BuzzDetailDAO.updateApprovedFlag(buzzId, Constant.YES);
                UserBuzzDAO.updateApproveFlag(image.userId, buzzId, Constant.REVIEW_STATUS_FLAG.APPROVED);
//                ri.buzzId = buzzId;
//                ri.ip = BuzzDetailDAO.getBuzzIp(buzzId);
            }

        } catch (Exception ex) {
            Util.addErrorLog(ex);

        }
    }

    public void NGToWaiting_approvedStatus(Image image, ReviewImage ri) {
        try {
            String imageId = image.imageId;
            ImageDAO.updateReport(imageId, Constant.REPORT_STATUS_FLAG.WAITING);
//            ImageDAO.updateAppearFlag(imageId, Constant.NO);
//            if (image.imageType == Constant.IMAGE_PUBLIC) {
//                String buzzId = PbImageDAO.getBuzzId(image.userId, imageId);
//                UserBuzzDAO.updateApproveFlag(image.userId, buzzId, Constant.PENDING);
//            }
        } catch (Exception ex) {
            Util.addErrorLog(ex);

        }
    }

//    public void NGToNG_approvedStatus(Image image, ReviewImage ri) {
//
//    }
    public void WaitingToGood_approvedStatus(Image image, ReviewImage ri) {
        try {
            String imageId = image.imageId;
            ImageDAO.updateReport(imageId, Constant.REPORT_STATUS_FLAG.GOOD);
            ImageDAO.updateAppearFlag(imageId, Constant.FLAG.OFF);
            if (image.appearFlag == Constant.FLAG.OFF) {
                if (image.imageType == Constant.IMAGE_TYPE_VALUE.IMAGE_BACKSTAGE) {
                    UserDAO.addBackstage(image.userId);
                    BackstageDAO.updateFlag(image.userId, imageId, Constant.AVAILABLE_FLAG_VALUE.AVAILABE_FLAG);
                } else if (image.imageType == Constant.IMAGE_TYPE_VALUE.IMAGE_PUBLIC) {
                    UserDAO.addPbImage(image.userId);
                    UserDAO.addBuzz(image.userId);
                    PbImageDAO.updateFlag(image.userId, imageId, Constant.AVAILABLE_FLAG_VALUE.AVAILABE_FLAG);
                    if (image.avatarFlag == Constant.FLAG.ON) {
                        UserDAO.updateAvatar(image.userId, imageId);
                        ri.avatar = Constant.FLAG.ON;
                    }
                    String buzzId = PbImageDAO.getBuzzId(image.userId, imageId);
                    ReviewingCommentDAO.updateAppearFlagByBuzzId(buzzId, Constant.FLAG.ON);
                    ReviewingSubCommentDAO.updateAppearFlagByBuzzId(buzzId, Constant.FLAG.ON);
                    UserBuzzDAO.updateApproveFlag(image.userId, buzzId, Constant.REVIEW_STATUS_FLAG.APPROVED);
//                    ri.buzzId = buzzId;
//                    ri.ip = BuzzDetailDAO.getBuzzIp(buzzId);
                }

            }else{
                if (image.imageType == Constant.IMAGE_TYPE_VALUE.IMAGE_PUBLIC) {
                    String buzzId = PbImageDAO.getBuzzId(image.userId, imageId);
//                    ri.buzzId = buzzId;
                    UserBuzzDAO.updateApproveFlag(image.userId, buzzId, Constant.REVIEW_STATUS_FLAG.APPROVED);
                }
            }
        } catch (Exception ex) {
            Util.addErrorLog(ex);

        }
    }

    public void WaitingToNG_approvedStatus(Image image, ReviewImage ri) {
        try {
            String imageId = image.imageId;
            ImageDAO.updateReport(imageId, Constant.REPORT_STATUS_FLAG.NOT_GOOD);
            if (image.appearFlag == Constant.FLAG.ON) {
                ImageDAO.updateAppearFlag(imageId, Constant.FLAG.OFF);
                if (image.imageType == Constant.IMAGE_TYPE_VALUE.IMAGE_BACKSTAGE) {
                    if (BackstageDAO.checkBackStageExist(image.userId, imageId)) {
                        UserDAO.removeBackstage(image.userId);
                        BackstageDAO.updateFlag(image.userId, imageId, Constant.AVAILABLE_FLAG_VALUE.NOT_AVAILABLE_FLAG);
                    }
                } else if (image.imageType == Constant.IMAGE_TYPE_VALUE.IMAGE_PUBLIC) {
                    if (PbImageDAO.checkPbImageExist(image.userId, imageId)) {
                        UserDAO.removeBuzz(image.userId);
                        UserDAO.removePbImage(image.userId);
                        PbImageDAO.updateFlag(image.userId, imageId, Constant.AVAILABLE_FLAG_VALUE.NOT_AVAILABLE_FLAG);
                        if (UserDAO.isAvatar(image.userId, imageId)) {
                            UserDAO.removeAvatar(image.userId, imageId);
                            ri.avatar = Constant.USER_STATUS_FLAG.DISABLE;
                        }
                    }
                    String buzzId = PbImageDAO.getBuzzId(image.userId, imageId);
                    ReviewingCommentDAO.updateAppearFlagByBuzzId(buzzId, Constant.FLAG.OFF);
                    ReviewingSubCommentDAO.updateAppearFlagByBuzzId(buzzId, Constant.FLAG.OFF);
//                    ri.buzzId = buzzId;
                    UserBuzzDAO.updateApproveFlag(image.userId, buzzId, Constant.REVIEW_STATUS_FLAG.DENIED);
//                    BuzzDetailDAO.updateApprovedFlag(buzzId, Constant.NO);
                }
            }else{
                if (image.imageType == Constant.IMAGE_TYPE_VALUE.IMAGE_PUBLIC) {
                    String buzzId = PbImageDAO.getBuzzId(image.userId, imageId);
//                    ri.buzzId = buzzId;
//                    BuzzDetailDAO.updateApprovedFlag(buzzId, Constant.NO);
                   UserBuzzDAO.updateApproveFlag(image.userId, buzzId, Constant.REVIEW_STATUS_FLAG.DENIED);
                }
            }
        } catch (Exception ex) {
            Util.addErrorLog(ex);

        }
    }

//    public void WaitingToWaiting_approvedStatus(Image image, ReviewImage ri) {
//
//    }
    
//    public void GoodToGood_pendingStatus(Image image, ReviewImage ri) {
//
//    }
    
    public void GoodToNG_pendingStatus(Image image, ReviewImage ri) {
        try {
            String imageId = image.imageId;
            ImageDAO.updateReport(imageId, Constant.REPORT_STATUS_FLAG.NOT_GOOD);
            if (image.imageType == Constant.IMAGE_TYPE_VALUE.IMAGE_PUBLIC) {
                String buzzId = PbImageDAO.getBuzzId(image.userId, imageId);
//                    ri.buzzId = buzzId;
//                    BuzzDetailDAO.updateApprovedFlag(buzzId, Constant.NO);
               UserBuzzDAO.updateApproveFlag(image.userId, buzzId, Constant.REVIEW_STATUS_FLAG.DENIED);
            }            
//            ImageDAO.updateAppearFlag(imageId, Constant.NO);
        } catch (Exception ex) {
            Util.addErrorLog(ex);

        }
    }
    
    public void GoodToWaiting_pendingStatus(Image image, ReviewImage ri) {
        try {
            String imageId = image.imageId;
            ImageDAO.updateReport(imageId, Constant.REPORT_STATUS_FLAG.WAITING);
            if (image.imageType == Constant.IMAGE_TYPE_VALUE.IMAGE_PUBLIC) {
                String buzzId = PbImageDAO.getBuzzId(image.userId, imageId);
//                    ri.buzzId = buzzId;
//                    BuzzDetailDAO.updateApprovedFlag(buzzId, Constant.NO);
               UserBuzzDAO.updateApproveFlag(image.userId, buzzId, Constant.REVIEW_STATUS_FLAG.DENIED);
            }  

        } catch (Exception ex) {
            Util.addErrorLog(ex);

        }
    }
    
    public void NGToGood_pendingStatus(Image image, ReviewImage ri) {
        try {
            String imageId = image.imageId;
            ImageDAO.updateReport(imageId, Constant.REPORT_STATUS_FLAG.GOOD);
            if (image.imageType == Constant.IMAGE_TYPE_VALUE.IMAGE_PUBLIC) {
                String buzzId = PbImageDAO.getBuzzId(image.userId, imageId);
//                    ri.buzzId = buzzId;
//                    BuzzDetailDAO.updateApprovedFlag(buzzId, Constant.NO);
               UserBuzzDAO.updateApproveFlag(image.userId, buzzId, Constant.REVIEW_STATUS_FLAG.PENDING);
            }
        } catch (Exception ex) {
            Util.addErrorLog(ex);

        }
    }
    
    public void NGToWaiting_pendingStatus(Image image, ReviewImage ri) {
        try {
            String imageId = image.imageId;
            ImageDAO.updateReport(imageId, Constant.REPORT_STATUS_FLAG.WAITING);
        } catch (Exception ex) {
            Util.addErrorLog(ex);

        }
    }
    
//    public void NGToNG_pendingStatus(Image image, ReviewImage ri) {
//
//    }
    
    public void WaitingToGood_pendingStatus(Image image, ReviewImage ri) {
        try {
            String imageId = image.imageId;
            ImageDAO.updateReport(imageId, Constant.REPORT_STATUS_FLAG.GOOD);
            if(image.appearFlag == Constant.FLAG.ON)
                ImageDAO.updateAppearFlag(imageId, Constant.FLAG.OFF);
            if (image.imageType == Constant.IMAGE_TYPE_VALUE.IMAGE_PUBLIC) {
                String buzzId = PbImageDAO.getBuzzId(image.userId, imageId);
//                    ri.buzzId = buzzId;
//                    BuzzDetailDAO.updateApprovedFlag(buzzId, Constant.NO);
               UserBuzzDAO.updateApproveFlag(image.userId, buzzId, Constant.REVIEW_STATUS_FLAG.PENDING);
            }
        } catch (Exception ex) {
            Util.addErrorLog(ex);

        }
    }
    
    public void WaitingToNG_pendingStatus(Image image, ReviewImage ri) {
        try {
            String imageId = image.imageId;
            ImageDAO.updateReport(imageId, Constant.REPORT_STATUS_FLAG.NOT_GOOD);
            if(image.appearFlag == Constant.FLAG.ON)
                ImageDAO.updateAppearFlag(imageId, Constant.FLAG.OFF);
            if (image.imageType == Constant.IMAGE_TYPE_VALUE.IMAGE_PUBLIC) {
                String buzzId = PbImageDAO.getBuzzId(image.userId, imageId);
//                    ri.buzzId = buzzId;
//                    BuzzDetailDAO.updateApprovedFlag(buzzId, Constant.NO);
               UserBuzzDAO.updateApproveFlag(image.userId, buzzId, Constant.REVIEW_STATUS_FLAG.DENIED);
            } 
        } catch (Exception ex) {
            Util.addErrorLog(ex);

        }
    }
    
//    public void WaitingToWaiting_pendingStatus(Image image, ReviewImage ri) {
//
//    }
    
//    public void GoodToGood_deniedStatus(Image image, ReviewImage ri) {
//
//    }
    
    public void GoodToNG_deniedStatus(Image image, ReviewImage ri) {
        try {
            String imageId = image.imageId;
            ImageDAO.updateReport(imageId, Constant.REPORT_STATUS_FLAG.NOT_GOOD);
//            ImageDAO.updateAppearFlag(imageId, Constant.NO);
            if (image.imageType == Constant.IMAGE_TYPE_VALUE.IMAGE_PUBLIC) {
                String buzzId = PbImageDAO.getBuzzId(image.userId, imageId);
//                    ri.buzzId = buzzId;
//                    BuzzDetailDAO.updateApprovedFlag(buzzId, Constant.NO);
               UserBuzzDAO.updateApproveFlag(image.userId, buzzId, Constant.REVIEW_STATUS_FLAG.DENIED);
            } 
        } catch (Exception ex) {
            Util.addErrorLog(ex);

        }
    }
    
    public void GoodToWaiting_deniedStatus(Image image, ReviewImage ri) {
        try {
            
            String imageId = image.imageId;
            ImageDAO.updateReport(imageId, Constant.REPORT_STATUS_FLAG.WAITING);
            if (image.imageType == Constant.IMAGE_TYPE_VALUE.IMAGE_PUBLIC) {
                String buzzId = PbImageDAO.getBuzzId(image.userId, imageId);
//                    ri.buzzId = buzzId;
//                    BuzzDetailDAO.updateApprovedFlag(buzzId, Constant.NO);
               UserBuzzDAO.updateApproveFlag(image.userId, buzzId, Constant.REVIEW_STATUS_FLAG.DENIED);
            } 

        } catch (Exception ex) {
            Util.addErrorLog(ex);

        }
    }
    
    public void NGToGood_deniedStatus(Image image, ReviewImage ri) {
        try {
            String imageId = image.imageId;
            ImageDAO.updateReport(imageId, Constant.REPORT_STATUS_FLAG.GOOD);
        } catch (Exception ex) {
            Util.addErrorLog(ex);

        }
    }
    
    public void NGToWaiting_deniedStatus(Image image, ReviewImage ri) {
        try {
            String imageId = image.imageId;
            ImageDAO.updateReport(imageId, Constant.REPORT_STATUS_FLAG.WAITING);
        } catch (Exception ex) {
            Util.addErrorLog(ex);

        }
    }
    
//    public void NGToNG_deniedStatus(Image image, ReviewImage ri) {
//
//    }
    
    public void WaitingToGood_deniedStatus(Image image, ReviewImage ri) {
        try {
            String imageId = image.imageId;
            ImageDAO.updateReport(imageId, Constant.REPORT_STATUS_FLAG.GOOD);
            if(image.appearFlag == Constant.FLAG.ON)
                ImageDAO.updateAppearFlag(imageId, Constant.FLAG.OFF);
        } catch (Exception ex) {
            Util.addErrorLog(ex);

        }
    }
    
    public void WaitingToNG_deniedStatus(Image image, ReviewImage ri) {
        try {
            String imageId = image.imageId;
            ImageDAO.updateReport(imageId, Constant.REPORT_STATUS_FLAG.NOT_GOOD);
            if(image.appearFlag == Constant.FLAG.ON)
                ImageDAO.updateAppearFlag(imageId, Constant.FLAG.OFF);
        } catch (Exception ex) {
            Util.addErrorLog(ex);

        }
    }
    
//    public void WaitingToWaiting_deniedStatus(Image image, ReviewImage ri) {
//
//    }
}
