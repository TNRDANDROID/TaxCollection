package com.nic.taxcollection.model;

import android.graphics.Bitmap;

/**
 * Created by AchanthiSundar on 01-11-2017.
 */

public class Tax {

    private String distictCode;
    private String districtName;
    private String blockCode;
    private String taxId;
    private String taxName;


    public String getDistictCode() {
        return distictCode;
    }

    public Tax setDistictCode(String distictCode) {
        this.distictCode = distictCode;
        return this;
    }

    public String getDistrictName() {
        return districtName;
    }

    public Tax setDistrictName(String districtName) {
        this.districtName = districtName;
        return this;
    }

    public String getBlockCode() {
        return blockCode;
    }

    public Tax setBlockCode(String blockCode) {
        this.blockCode = blockCode;
        return this;
    }

    public String getTaxId() {
        return taxId;
    }

    public Tax setTaxId(String taxId) {
        this.taxId = taxId;
        return this;
    }

    public String getTaxName() {
        return taxName;
    }

    public Tax setTaxName(String taxName) {
        this.taxName = taxName;
        return this;
    }
}