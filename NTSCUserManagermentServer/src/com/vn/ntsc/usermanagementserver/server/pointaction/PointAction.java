/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.usermanagementserver.server.pointaction;

/**
 *
 * @author RuAc0n
 */
public class PointAction {

    public Integer beforePoint;
    public Integer afterPoint;
    public Integer point;

    public PointAction() {
        beforePoint = null;
        afterPoint = null;
        point = null;
    }

    public PointAction(Integer beforePoint, Integer afterPoint, Integer point) {
        this.beforePoint = beforePoint;
        this.afterPoint = afterPoint;
        this.point = point;
    }

    @Override
    public String toString() {
        return "PointAction{" + "beforePoint=" + beforePoint + ", afterPoint=" + afterPoint + ", point=" + point + '}';
    }
    
}
