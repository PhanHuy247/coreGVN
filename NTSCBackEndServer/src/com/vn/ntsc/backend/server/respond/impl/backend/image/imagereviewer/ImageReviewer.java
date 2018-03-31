/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.backend.server.respond.impl.backend.image.imagereviewer;

import eazycommon.constant.Constant;
import eazycommon.util.Util;
import com.vn.ntsc.backend.dao.buzz.BuzzDetailDAO;
import com.vn.ntsc.backend.dao.buzz.ReviewingCommentDAO;
import com.vn.ntsc.backend.dao.buzz.ReviewingSubCommentDAO;
import com.vn.ntsc.backend.dao.buzz.UserBuzzDAO;
import com.vn.ntsc.backend.dao.user.BackstageDAO;
import com.vn.ntsc.backend.dao.user.BuzzUserDAO;
import com.vn.ntsc.backend.dao.user.ImageDAO;
import com.vn.ntsc.backend.dao.user.PbImageDAO;
import com.vn.ntsc.backend.dao.user.UserDAO;
import com.vn.ntsc.backend.entity.impl.image.Image;
import com.vn.ntsc.backend.entity.impl.image.ReviewImage;

/**
 *
 * @author Rua
 */
public class ImageReviewer {

//    public void approvedToApproved_goodReport(Image image, ReviewImage ri) {
//
//    }      
    public void approvedToDenied_goodReport(Image image, ReviewImage ri) {
        try {
            Util.addDebugLog("==========approvedToDenied_goodReport==========");
            String imageId = image.imageId;
            if (image.deniedFlag == null || image.deniedFlag == Constant.FLAG.OFF) {
                ri.notify = Constant.FLAG.ON;
            }
            ImageDAO.updateReview(imageId, Constant.REVIEW_STATUS_FLAG.DENIED, Util.getGMTTime().getTime(), ri.userDeny);
//                        ImageDAO.updateAppearFlag(image.imageId, Constant.NO);
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
                    //String buzzId = PbImageDAO.getBuzzId(image.userId, imageId);
                    String buzzId = BuzzDetailDAO.getBuzzId(image.userId, imageId);
                    ReviewingCommentDAO.updateAppearFlagByBuzzId(buzzId, Constant.FLAG.OFF);
                    ReviewingSubCommentDAO.updateAppearFlagByBuzzId(buzzId, Constant.FLAG.OFF);
                    ri.buzzId = buzzId;
                    BuzzDetailDAO.updateApprovedFlag(buzzId, Constant.REVIEW_STATUS_FLAG.DENIED, ri.userDeny);
                    UserBuzzDAO.updateApproveFlag(image.userId, buzzId, Constant.REVIEW_STATUS_FLAG.DENIED);
                }
            }

        } catch (Exception ex) {
            Util.addErrorLog(ex);

        }
    }

    public void approvedToPending_goodReport(Image image, ReviewImage ri) {
        try {
            Util.addDebugLog("==========approvedToPending_goodReport==========");
            String imageId = image.imageId;
            ImageDAO.updateReview(imageId, Constant.REVIEW_STATUS_FLAG.PENDING, Util.getGMTTime().getTime(),ri.userDeny);
//                        ImageDAO.updateAppearFlag(imageId, Constant.NO);
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
                    //String buzzId = PbImageDAO.getBuzzId(image.userId, imageId);
                    String buzzId = BuzzDetailDAO.getBuzzId(image.userId, imageId);
                    ReviewingCommentDAO.updateAppearFlagByBuzzId(buzzId, Constant.FLAG.OFF);
                    ReviewingSubCommentDAO.updateAppearFlagByBuzzId(buzzId, Constant.FLAG.OFF);
                    ri.buzzId = buzzId;
                    BuzzDetailDAO.updateApprovedFlag(buzzId, Constant.REVIEW_STATUS_FLAG.PENDING, ri.userDeny);
                    UserBuzzDAO.updateApproveFlag(image.userId, buzzId, Constant.REVIEW_STATUS_FLAG.PENDING);
                }
            }

        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
    }

    public void deniedToApproved_goodReport(Image image, ReviewImage ri) {
        try {
            Util.addDebugLog("==========deniedToApproved_goodReport==========");
            String imageId = ri.imageId;
            if (image.appFlag == null || image.appFlag == Constant.FLAG.OFF) {
                ri.notify = Constant.FLAG.ON;
                if (image.imageType == Constant.IMAGE_TYPE_VALUE.IMAGE_PUBLIC) {
                    //String buzzId = PbImageDAO.getBuzzId(image.userId, imageId);
                    String buzzId = BuzzDetailDAO.getBuzzId(image.userId, imageId);
                    BuzzUserDAO.add(buzzId, image.userId);
                }

            }
            //update review time, status and change flag avatar = no;
            ImageDAO.updateReview(imageId, Constant.REVIEW_STATUS_FLAG.APPROVED, Util.getGMTTime().getTime(),ri.userDeny);
//            ImageDAO.updateAppearFlag(imageId, Constant.YES);
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
                //String buzzId = PbImageDAO.getBuzzId(image.userId, imageId);
                String buzzId = BuzzDetailDAO.getBuzzId(image.userId, imageId);
                ReviewingCommentDAO.updateAppearFlagByBuzzId(buzzId, Constant.FLAG.ON);
                ReviewingSubCommentDAO.updateAppearFlagByBuzzId(buzzId, Constant.FLAG.ON);
                BuzzDetailDAO.updateApprovedFlag(buzzId, Constant.REVIEW_STATUS_FLAG.APPROVED, ri.userDeny);
                UserBuzzDAO.updateApproveFlag(image.userId, buzzId, Constant.REVIEW_STATUS_FLAG.APPROVED);
                ri.buzzId = buzzId;
                ri.ip = BuzzDetailDAO.getBuzzIp(buzzId);
            }
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
    }

    public void deniedToPending_goodReport(Image image, ReviewImage ri) {
        try {
            Util.addDebugLog("==========deniedToPending_goodReport==========");
            String imageId = image.imageId;
            ImageDAO.updateReview(imageId, Constant.REVIEW_STATUS_FLAG.PENDING, Util.getGMTTime().getTime(),ri.userDeny);
            if (image.imageType == Constant.IMAGE_TYPE_VALUE.IMAGE_PUBLIC) {
                //String buzzId = PbImageDAO.getBuzzId(image.userId, imageId);
                String buzzId = BuzzDetailDAO.getBuzzId(image.userId, imageId);
                ri.buzzId = buzzId;
                BuzzDetailDAO.updateApprovedFlag(buzzId, Constant.REVIEW_STATUS_FLAG.PENDING, ri.userDeny);
                UserBuzzDAO.updateApproveFlag(image.userId, buzzId, Constant.REVIEW_STATUS_FLAG.PENDING);
            }
        } catch (Exception ex) {
            Util.addErrorLog(ex);

        }
    }
//    public void deniedToDenied_goodReport(Image image, ReviewImage ri) {
//
//    }    

    public void pendingToApproved_goodReport(Image image, ReviewImage ri) {
        try {
            Util.addDebugLog("==========pendingToApproved_goodReport==========");
            String imageId = image.imageId;
            if (image.appFlag == null || image.appFlag == Constant.FLAG.OFF) {
                ri.notify = Constant.FLAG.ON;
                if (image.imageType == Constant.IMAGE_TYPE_VALUE.IMAGE_PUBLIC) {
                    //String buzzId = PbImageDAO.getBuzzId(image.userId, imageId);
                    String buzzId = BuzzDetailDAO.getBuzzId(image.userId, imageId);
                    BuzzUserDAO.add(buzzId, image.userId);
                }

            }
            //update review time, status and change flag avatar = no;
            ImageDAO.updateReview(imageId, Constant.REVIEW_STATUS_FLAG.APPROVED, Util.getGMTTime().getTime(),ri.userDeny);
//            ImageDAO.updateAppearFlag(imageId, Constant.YES);
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
                Util.addDebugLog("==========image.userId=========="+image.userId);
                Util.addDebugLog("==========imageId=========="+imageId);
                //String buzzId = PbImageDAO.getBuzzId(image.userId, imageId);
                String buzzId = BuzzDetailDAO.getBuzzId(image.userId, imageId);
                Util.addDebugLog("==========buzzId=========="+buzzId);
                ReviewingCommentDAO.updateAppearFlagByBuzzId(buzzId, Constant.FLAG.ON);
                ReviewingSubCommentDAO.updateAppearFlagByBuzzId(buzzId, Constant.FLAG.ON);
                BuzzDetailDAO.updateApprovedFlag(buzzId, Constant.REVIEW_STATUS_FLAG.APPROVED, ri.userDeny);
                UserBuzzDAO.updateApproveFlag(image.userId, buzzId, Constant.REVIEW_STATUS_FLAG.APPROVED);
                ri.buzzId = buzzId;
                ri.ip = BuzzDetailDAO.getBuzzIp(buzzId);
            }
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
    }

    public void pendingToDenied_goodReport(Image image, ReviewImage ri) {
        try {
            Util.addDebugLog("==========pendingToDenied_goodReport==========");
            String imageId = image.imageId;
            if (image.deniedFlag == null || image.deniedFlag == Constant.FLAG.OFF) {
                ri.notify = Constant.FLAG.ON;
            }
            ImageDAO.updateReview(imageId, Constant.REVIEW_STATUS_FLAG.DENIED, Util.getGMTTime().getTime(),ri.userDeny);
            if (image.imageType == Constant.IMAGE_TYPE_VALUE.IMAGE_PUBLIC) {
                //String buzzId = PbImageDAO.getBuzzId(image.userId, imageId);
                String buzzId = BuzzDetailDAO.getBuzzId(image.userId, imageId);
                ri.buzzId = buzzId;
                BuzzDetailDAO.updateApprovedFlag(buzzId, Constant.REVIEW_STATUS_FLAG.DENIED, ri.userDeny);
                UserBuzzDAO.updateApproveFlag(image.userId, buzzId, Constant.REVIEW_STATUS_FLAG.DENIED);
            }
        } catch (Exception ex) {
            Util.addErrorLog(ex);

        }
    }
//    public void pendingToPending_goodReport(Image image, ReviewImage ri) {
//
//    }    

//    public void approvedToApproved_waitingReport(Image image, ReviewImage ri) {
//
//    }    
    public void approvedToDenied_waitingReport(Image image, ReviewImage ri) {
        try {
            Util.addDebugLog("==========approvedToDenied_waitingReport==========");
            String imageId = image.imageId;
            ImageDAO.updateReview(imageId, Constant.REVIEW_STATUS_FLAG.DENIED, Util.getGMTTime().getTime(),ri.userDeny);
            if (image.appearFlag == Constant.FLAG.ON) {
//                ImageDAO.updateAppearFlag(imageId, Constant.NO);
                if (image.imageType == Constant.IMAGE_TYPE_VALUE.IMAGE_BACKSTAGE) {
                    if (BackstageDAO.checkBackStageExist(image.userId, imageId)) {
                        UserDAO.removeBackstage(image.userId);
                        BackstageDAO.updateFlag(image.userId, imageId, Constant.AVAILABLE_FLAG_VALUE.NOT_AVAILABLE_FLAG);
                    }
                } else if (image.imageType == Constant.IMAGE_TYPE_VALUE.IMAGE_PUBLIC) {
                    if (PbImageDAO.checkPbImageExist(image.userId, imageId)) {
                        UserDAO.removeBuzz(image.userId);
                        UserDAO.removePbImage(image.userId);
                        //String buzzId = PbImageDAO.getBuzzId(image.userId, imageId);
                        String buzzId = BuzzDetailDAO.getBuzzId(image.userId, imageId);
                        ReviewingCommentDAO.updateAppearFlagByBuzzId(buzzId, Constant.FLAG.OFF);
                        ReviewingSubCommentDAO.updateAppearFlagByBuzzId(buzzId, Constant.FLAG.OFF);
                        PbImageDAO.updateFlag(image.userId, imageId, Constant.AVAILABLE_FLAG_VALUE.NOT_AVAILABLE_FLAG);
                        if (UserDAO.isAvatar(image.userId, imageId)) {
                            UserDAO.removeAvatar(image.userId, imageId);
                            ri.avatar = Constant.USER_STATUS_FLAG.DISABLE;
                        }
                    }
                }
            }

            if (image.imageType == Constant.IMAGE_TYPE_VALUE.IMAGE_PUBLIC) {
                //String buzzId = PbImageDAO.getBuzzId(image.userId, imageId);
                String buzzId = BuzzDetailDAO.getBuzzId(image.userId, imageId);
                ri.buzzId = buzzId;
                BuzzDetailDAO.updateApprovedFlag(buzzId, Constant.REVIEW_STATUS_FLAG.DENIED, ri.userDeny);
                UserBuzzDAO.updateApproveFlag(image.userId, buzzId, Constant.REVIEW_STATUS_FLAG.DENIED);
            }
        } catch (Exception ex) {
            Util.addErrorLog(ex);

        }
    }

    public void approvedToPending_waitingReport(Image image, ReviewImage ri) {
        try {
            Util.addDebugLog("==========approvedToPending_waitingReport==========");
            String imageId = image.imageId;
            ImageDAO.updateReview(imageId, Constant.REVIEW_STATUS_FLAG.PENDING, Util.getGMTTime().getTime(),ri.userDeny);
            if (image.appearFlag == Constant.FLAG.ON) {
//                        ImageDAO.updateAppearFlag(imageId, Constant.NO);
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
                    //String buzzId = PbImageDAO.getBuzzId(image.userId, imageId);
                    String buzzId = BuzzDetailDAO.getBuzzId(image.userId, imageId);
                    ReviewingCommentDAO.updateAppearFlagByBuzzId(buzzId, Constant.FLAG.OFF);
                    ReviewingSubCommentDAO.updateAppearFlagByBuzzId(buzzId, Constant.FLAG.OFF);
                    ri.buzzId = buzzId;
                    BuzzDetailDAO.updateApprovedFlag(buzzId, Constant.REVIEW_STATUS_FLAG.PENDING, ri.userDeny);
                    UserBuzzDAO.updateApproveFlag(image.userId, buzzId, Constant.REVIEW_STATUS_FLAG.PENDING); 
//                        String buzzId = PbImageDAO.getBuzzId(image.userId, imageId);
//                        ri.buzzId = buzzId;
//                        BuzzDetailDAO.updateApprovedFlag(buzzId, Constant.NO);
//                        UserBuzzDAO.updateApproveFlag(image.userId, buzzId, Constant.PENDING);
                    }
                }
            }else{
                if(image.imageType == Constant.BUZZ_TYPE_VALUE.IMAGE_BUZZ){
                    //String buzzId = PbImageDAO.getBuzzId(image.userId, imageId);
                    String buzzId = BuzzDetailDAO.getBuzzId(image.userId, imageId);
                    ri.buzzId = buzzId;
                    BuzzDetailDAO.updateApprovedFlag(buzzId, Constant.REVIEW_STATUS_FLAG.DENIED, ri.userDeny);
                    UserBuzzDAO.updateApproveFlag(image.userId, buzzId, Constant.REVIEW_STATUS_FLAG.DENIED);    
                }
            }
        } catch (Exception ex) {
            Util.addErrorLog(ex);

        }
    }

    public void deniedToApproved_waitingReport(Image image, ReviewImage ri) {
        try {
            Util.addDebugLog("==========deniedToApproved_waitingReport==========");
            String imageId = image.imageId;
            //update review time, status and change flag avatar = no;
            ImageDAO.updateReview(imageId, Constant.REVIEW_STATUS_FLAG.APPROVED, Util.getGMTTime().getTime(),ri.userDeny);
            if (image.appearFlag == Constant.FLAG.ON) {
//                        ImageDAO.updateAppearFlag(imageId, Constant.YES);
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
                    //String buzzId = PbImageDAO.getBuzzId(image.userId, imageId);
                    String buzzId = BuzzDetailDAO.getBuzzId(image.userId, imageId);
                    ReviewingCommentDAO.updateAppearFlagByBuzzId(buzzId, Constant.FLAG.ON);
                    ReviewingSubCommentDAO.updateAppearFlagByBuzzId(buzzId, Constant.FLAG.ON);
                    BuzzDetailDAO.updateApprovedFlag(buzzId, Constant.REVIEW_STATUS_FLAG.APPROVED, ri.userDeny);
                    UserBuzzDAO.updateApproveFlag(image.userId, buzzId, Constant.REVIEW_STATUS_FLAG.APPROVED);
                    ri.buzzId = buzzId;
                    ri.ip = BuzzDetailDAO.getBuzzIp(buzzId);
                }
            }else{
                if (image.imageType == Constant.IMAGE_TYPE_VALUE.IMAGE_PUBLIC) {
                    //String buzzId = PbImageDAO.getBuzzId(image.userId, imageId);
                    String buzzId = BuzzDetailDAO.getBuzzId(image.userId, imageId);
                    BuzzDetailDAO.updateApprovedFlag(buzzId, Constant.REVIEW_STATUS_FLAG.APPROVED, ri.userDeny);
//                    UserBuzzDAO.updateApproveFlag(image.userId, buzzId, Constant.APPROVED);
                }                
            }
            
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
    }

    public void deniedToPending_waitingReport(Image image, ReviewImage ri) {
        try {
            Util.addDebugLog("==========deniedToPending_waitingReport==========");
            String imageId = image.imageId;
            ImageDAO.updateReview(imageId, Constant.REVIEW_STATUS_FLAG.PENDING, Util.getGMTTime().getTime(),ri.userDeny);

            if(image.imageType == Constant.BUZZ_TYPE_VALUE.IMAGE_BUZZ){
                //String buzzId = PbImageDAO.getBuzzId(image.userId, imageId);
                String buzzId = BuzzDetailDAO.getBuzzId(image.userId, imageId);
                BuzzDetailDAO.updateApprovedFlag(buzzId, Constant.REVIEW_STATUS_FLAG.PENDING, ri.userDeny);
                if(image.appearFlag == Constant.FLAG.ON)
                    UserBuzzDAO.updateApproveFlag(image.userId, buzzId, Constant.REVIEW_STATUS_FLAG.PENDING);    
            }
            
        } catch (Exception ex) {
            Util.addErrorLog(ex);

        }
    }

//    public void deniedToDenied_waitingReport(Image image, ReviewImage ri) {
//
//    }

    public void pendingToApproved_waitingReport(Image image, ReviewImage ri) {
        try {
            Util.addDebugLog("==========pendingToApproved_waitingReport==========");
            String imageId = image.imageId;
                    //update review time, status and change flag avatar = no;
            ImageDAO.updateReview(imageId, Constant.REVIEW_STATUS_FLAG.APPROVED, Util.getGMTTime().getTime(),ri.userDeny);
            if(image.appearFlag == Constant.FLAG.ON){
//                ImageDAO.updateAppearFlag(imageId, Constant.YES);
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
                    //String buzzId = PbImageDAO.getBuzzId(image.userId, imageId);
                    String buzzId = BuzzDetailDAO.getBuzzId(image.userId, imageId);
                    ReviewingCommentDAO.updateAppearFlagByBuzzId(buzzId, Constant.FLAG.ON);
                    ReviewingSubCommentDAO.updateAppearFlagByBuzzId(buzzId, Constant.FLAG.ON);
                    BuzzDetailDAO.updateApprovedFlag(buzzId, Constant.REVIEW_STATUS_FLAG.APPROVED, ri.userDeny);
                    UserBuzzDAO.updateApproveFlag(image.userId, buzzId, Constant.REVIEW_STATUS_FLAG.APPROVED);
                    ri.buzzId = buzzId;
                    ri.ip = BuzzDetailDAO.getBuzzIp(buzzId);
                }
            }else{
                if(image.imageType == Constant.IMAGE_TYPE_VALUE.IMAGE_PUBLIC) {
                    //String buzzId = PbImageDAO.getBuzzId(image.userId, imageId);
                    String buzzId = BuzzDetailDAO.getBuzzId(image.userId, imageId);
                    BuzzDetailDAO.updateApprovedFlag(buzzId, Constant.REVIEW_STATUS_FLAG.APPROVED, ri.userDeny);
//                    UserBuzzDAO.updateApproveFlag(image.userId, buzzId, Constant.APPROVED);  
                }
            }
                
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
    }

    public void pendingToDenied_waitingReport(Image image, ReviewImage ri) {
        try {
            Util.addDebugLog("==========pendingToDenied_waitingReport==========");
            String imageId = image.imageId;
            ImageDAO.updateReview(imageId, Constant.REVIEW_STATUS_FLAG.DENIED, Util.getGMTTime().getTime(),ri.userDeny);
            if (image.imageType == Constant.IMAGE_TYPE_VALUE.IMAGE_PUBLIC) {
                //String buzzId = PbImageDAO.getBuzzId(image.userId, imageId);
                String buzzId = BuzzDetailDAO.getBuzzId(image.userId, imageId);
                ri.buzzId = buzzId;
                BuzzDetailDAO.updateApprovedFlag(buzzId, Constant.REVIEW_STATUS_FLAG.DENIED, ri.userDeny);
                UserBuzzDAO.updateApproveFlag(image.userId, buzzId, Constant.REVIEW_STATUS_FLAG.DENIED);
            }

        } catch (Exception ex) {
            Util.addErrorLog(ex);

        }
    }

//    public void pendingToPending_waitingReport(Image image, ReviewImage ri) {
//
//    }
//    public void approvedToApproved_NGReport(Image image, ReviewImage ri) {
//
//    }
    public void approvedToDenied_NGReport(Image image, ReviewImage ri) {
        try {
            Util.addDebugLog("==========approvedToDenied_NGReport==========");
            String imageId = image.imageId;
            ImageDAO.updateReview(imageId, Constant.REVIEW_STATUS_FLAG.DENIED, Util.getGMTTime().getTime(),ri.userDeny);
            if (image.imageType == Constant.IMAGE_TYPE_VALUE.IMAGE_PUBLIC) {
                //String buzzId = PbImageDAO.getBuzzId(image.userId, imageId);
                String buzzId = BuzzDetailDAO.getBuzzId(image.userId, imageId);
                BuzzDetailDAO.updateApprovedFlag(buzzId, Constant.REVIEW_STATUS_FLAG.DENIED, ri.userDeny);
                UserBuzzDAO.updateApproveFlag(image.userId, buzzId, Constant.REVIEW_STATUS_FLAG.DENIED);
            }

        } catch (Exception ex) {
            Util.addErrorLog(ex);

        }
    }
    
    public void approvedToPending_NGReport(Image image, ReviewImage ri) {
        try {
            Util.addDebugLog("==========approvedToPending_NGReport==========");
            String imageId = image.imageId;
            ImageDAO.updateReview(imageId, Constant.REVIEW_STATUS_FLAG.PENDING, Util.getGMTTime().getTime(),ri.userDeny);
            if (image.imageType == Constant.IMAGE_TYPE_VALUE.IMAGE_PUBLIC) {
                //String buzzId = PbImageDAO.getBuzzId(image.userId, imageId);
                String buzzId = BuzzDetailDAO.getBuzzId(image.userId, imageId);
                BuzzDetailDAO.updateApprovedFlag(buzzId, Constant.REVIEW_STATUS_FLAG.PENDING, ri.userDeny);
//                UserBuzzDAO.updateApproveFlag(image.userId, buzzId, Constant.PENDING);
            }
                 
        } catch (Exception ex) {
            Util.addErrorLog(ex);

        }
    }
    
    public void deniedToApproved_NGReport(Image image, ReviewImage ri) {
        try {
            Util.addDebugLog("==========deniedToApproved_NGReport==========");
            String imageId = image.imageId;
            //update review time, status and change flag avatar = no;
            ImageDAO.updateReview(imageId, Constant.REVIEW_STATUS_FLAG.APPROVED, Util.getGMTTime().getTime(),ri.userDeny);
            if (image.imageType == Constant.IMAGE_TYPE_VALUE.IMAGE_PUBLIC) {
                //String buzzId = PbImageDAO.getBuzzId(image.userId, imageId);
                String buzzId = BuzzDetailDAO.getBuzzId(image.userId, imageId);
                BuzzDetailDAO.updateApprovedFlag(buzzId, Constant.REVIEW_STATUS_FLAG.APPROVED, ri.userDeny);
//                UserBuzzDAO.updateApproveFlag(image.userId, buzzId, Constant.APPROVED);
            }
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
    }
    
    public void deniedToPending_NGReport(Image image, ReviewImage ri) {
        try {
            Util.addDebugLog("==========deniedToPending_NGReport==========");
            String imageId = image.imageId;
            //update review time, status and change flag avatar = no;
            ImageDAO.updateReview(imageId, Constant.REVIEW_STATUS_FLAG.PENDING, Util.getGMTTime().getTime(),ri.userDeny);
            if (image.imageType == Constant.IMAGE_TYPE_VALUE.IMAGE_PUBLIC) {
                //String buzzId = PbImageDAO.getBuzzId(image.userId, imageId);
                String buzzId = BuzzDetailDAO.getBuzzId(image.userId, imageId);
                BuzzDetailDAO.updateApprovedFlag(buzzId, Constant.REVIEW_STATUS_FLAG.PENDING, ri.userDeny);
//                UserBuzzDAO.updateApproveFlag(image.userId, buzzId, Constant.PENDING);
            }            
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
    }
//    public void deniedToDenied_NGReport(Image image, ReviewImage ri) {
//
//    }
    public void pendingToApproved_NGReport(Image image, ReviewImage ri) {
        try {
            Util.addDebugLog("==========pendingToApproved_NGReport==========");
            String imageId = image.imageId;
            //update review time, status and change flag avatar = no;
            ImageDAO.updateReview(imageId, Constant.REVIEW_STATUS_FLAG.APPROVED, Util.getGMTTime().getTime(),ri.userDeny);
            if (image.imageType == Constant.IMAGE_TYPE_VALUE.IMAGE_PUBLIC) {
                //String buzzId = PbImageDAO.getBuzzId(image.userId, imageId);
                String buzzId = BuzzDetailDAO.getBuzzId(image.userId, imageId);
                BuzzDetailDAO.updateApprovedFlag(buzzId, Constant.REVIEW_STATUS_FLAG.APPROVED, ri.userDeny);
//                UserBuzzDAO.updateApproveFlag(image.userId, buzzId, Constant.APPROVED);
            }
        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
    }
    
    public void pendingToDenied_NGReport(Image image, ReviewImage ri) {
        try {
            Util.addDebugLog("==========pendingToDenied_NGReport==========");
            String imageId = image.imageId;
            ImageDAO.updateReview(imageId, Constant.REVIEW_STATUS_FLAG.DENIED, Util.getGMTTime().getTime(),ri.userDeny);
            if (image.imageType == Constant.IMAGE_TYPE_VALUE.IMAGE_PUBLIC) {
                //String buzzId = PbImageDAO.getBuzzId(image.userId, imageId);
                String buzzId = BuzzDetailDAO.getBuzzId(image.userId, imageId);
                ri.buzzId = buzzId;
                BuzzDetailDAO.updateApprovedFlag(buzzId, Constant.REVIEW_STATUS_FLAG.DENIED, ri.userDeny);
                UserBuzzDAO.updateApproveFlag(image.userId, buzzId, Constant.REVIEW_STATUS_FLAG.DENIED);
            }

        } catch (Exception ex) {
            Util.addErrorLog(ex);
        }
    }
//    public void pendingToPending_NGReport(Image image, ReviewImage ri) {
//
//    }
}
