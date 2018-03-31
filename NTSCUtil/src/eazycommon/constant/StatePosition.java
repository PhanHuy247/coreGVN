/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package eazycommon.constant;

/**
 *
 * @author Admin
 */
public class StatePosition {
    public int stateCode;
    public double latitude;
    public double longtitude;

    public StatePosition(int cityCode, double latetitude, double longtitude) {
        this.stateCode = cityCode;
        this.latitude = latetitude;
        this.longtitude = longtitude;
    }

    @Override
    public String toString() {
        return " posMap.put(" + stateCode + ", new CityPositions(" + stateCode + "," + latitude + "," + longtitude + "));";
    }
    
    
}
