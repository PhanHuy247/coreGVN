/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.vn.ntsc.backend.server.respond.impl.backend.image.reportedimageprocessor;

/**
 *
 * @author Rua
 */
public enum ReportFlagChangeType {
    GOOD_TO_GOOD,
    GOOD_TO_WAITING,
    GOOD_TO_NG,
    
    WAITING_TO_GOOD,
    WAITING_TO_NG,
    WAITING_TO_WAITING,
    
    NG_TO_GOOD,
    NG_TO_NG,
    NG_TO_WAITING
}
