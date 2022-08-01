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

    private String id;
    private String fin;
    private String name;
    private String amount;

    public String getId() {
        return id;
    }

    public Tax setId(String id) {
        this.id = id;
        return this;
    }

    public String getFin() {
        return fin;
    }

    public Tax setFin(String fin) {
        this.fin = fin;
        return this;
    }

    public String getName() {
        return name;
    }

    public Tax setName(String name) {
        this.name = name;
        return this;
    }

    public String getAmount() {
        return amount;
    }

    public Tax setAmount(String amount) {
        this.amount = amount;
        return this;
    }

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