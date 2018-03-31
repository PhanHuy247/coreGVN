/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.vn.ntsc.backend.server.respond.impl.backend.image.imagereviewer;

/**
 *
 * @author Rua
 */
public enum ImageStatusChangeType {

    APPROVED_TO_APPROVED,
    APPROVED_TO_PENDING,
    APPROVED_TO_DENIED,
    
    PENDING_TO_APPROVED,
    PENDING_TO_DENIED,
    PENDING_TO_PENDING,
    
    DENIED_TO_APPROVED,
    DENIED_TO_DENIED,
    DENIED_TO_PENDING
    
}
