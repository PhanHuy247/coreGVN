/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.vn.ntsc.backend.entity.impl.user;

/**
 *
 * @author RuAc0n
 */
public class Point {
    public Integer beforePoint;
    public Integer afterPoint;
    public Integer point;


    public Point() {

        this.afterPoint = null;
        this.point = null;
        this.beforePoint = null;
    }

    public Point(Integer beforePoint, Integer point, Integer afterPoint) {
        this.beforePoint = beforePoint;
        this.afterPoint = afterPoint;
        this.point = point;
    }
    
    
}
