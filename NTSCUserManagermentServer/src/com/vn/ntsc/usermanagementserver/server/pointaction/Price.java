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
public class Price {

    public int malePrice;
    public int potentialCustomerMalePrice;
    public int femalePrice;
    public int potentialCustomerFemalePrice;

    public int malePartnerPrice;
//    public int potentialCustomerMalePartnerPrice;
    public int femalePartnerPrice;
//    public int potentialCustomerFemalePartnerPrice;

    public Price(Integer malePrice, Integer femalePrice, Integer malePartnerPrice, Integer femalePartnerPrice,
                Integer potentialCustomerMalePrice, Integer potentialCustomerFemalePrice ) {
        if (malePrice != null) {
            this.malePrice = malePrice;
        }
        if (potentialCustomerMalePrice != null) {
            this.potentialCustomerMalePrice = potentialCustomerMalePrice;
        }
        
        if (femalePrice != null) {
            this.femalePrice = femalePrice;
        }
        if (potentialCustomerFemalePrice != null) {
            this.potentialCustomerFemalePrice = potentialCustomerFemalePrice;
        }
        
        if (malePartnerPrice != null) {
            this.malePartnerPrice = malePartnerPrice;
        }
//        if (potentialCustomerMalePartnerPrice != null) {
//            this.malePartnerPrice = potentialCustomerMalePartnerPrice;
//        }
        
        if (femalePartnerPrice != null) {
            this.femalePartnerPrice = femalePartnerPrice;
        }
//        if (potentialCustomerFemalePartnerPrice != null) {
//            this.potentialCustomerFemalePartnerPrice = potentialCustomerFemalePartnerPrice;
//        }
    }

    public Price(Long malePrice, Long femalePrice, Long malePartnerPrice, Long femalePartnerPrice,
            Long potentialCustomerMalePrice, Long potentialCustomerFemalePrice) {
        if (malePrice != null) {
            this.malePrice = malePrice.intValue();
        }
        if (potentialCustomerMalePrice != null) {
            this.potentialCustomerMalePrice = potentialCustomerMalePrice.intValue();
        }
        
        if (femalePrice != null) {
            this.femalePrice = femalePrice.intValue();
        }
        if (potentialCustomerFemalePrice != null) {
            this.potentialCustomerFemalePrice = potentialCustomerFemalePrice.intValue();
        }
        
        if (malePartnerPrice != null) {
            this.malePartnerPrice = malePartnerPrice.intValue();
        }
//        if (potentialCustomerMalePartnerPrice != null) {
//            this.potentialCustomerMalePartnerPrice = potentialCustomerMalePartnerPrice.intValue();
//        }
        
        if (femalePartnerPrice != null) {
            this.femalePartnerPrice = femalePartnerPrice.intValue();
        }
//        if (potentialCustomerFemalePartnerPrice != null) {
//            this.potentialCustomerFemalePartnerPrice = potentialCustomerFemalePartnerPrice.intValue();
//        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("male Request Point : ");
        sb.append(this.malePrice);
        sb.append("\n");

        sb.append("female Request Point : ");
        sb.append(this.femalePrice);
        sb.append("\n");

        return sb.toString();
    }
}
