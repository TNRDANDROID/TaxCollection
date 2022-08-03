package com.nic.taxcollection.model;

import android.graphics.Bitmap;

import java.io.Serializable;

/**
 * Created by AchanthiSundar on 01-11-2017.
 */

public class Tax implements Serializable {

    private String distictCode;
    private String districtName;
    private String blockCode;
    private String taxId;
    private String taxName;

    private String id;
    private String fin;
    private String name;
    private String amount;
    private int payStatus;


    public int getPayStatus() {
        return payStatus;
    }

    public Tax setPayStatus(int payStatus) {
        this.payStatus = payStatus;
        return this;
    }

    ////Payment Type List
    private String paymenttypeid;
    private String paymenttype;

    public String getPaymenttypeid() {
        return paymenttypeid;
    }

    public void setPaymenttypeid(String paymenttypeid) {
        this.paymenttypeid = paymenttypeid;
    }

    public String getPaymenttype() {
        return paymenttype;
    }

    public void setPaymenttype(String paymenttype) {
        this.paymenttype = paymenttype;
    }
    ////////

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

    private String statecode;
    private String state_name;
    private String dcode;
    private String district_name;
    private String lbcode;
    private String bcode;
    private String localbody_name;
    private String lbtype;
    private String assessment_id;
    private String assessment_no;
    private String door_no;
    private String assessment_address;
    private String area_in_sq_feet;
    private String surveyno;
    private String subdivisionno;
    private String wardid;
    private String ward_code;
    private String ward_name_en;
    private String streetid;
    private String building_licence_no;
    private String street_name_en;
    private String street_name_ta;
    private String habitation_name_en;
    private String habitation_name_ta;
    private String hab_code;
    private String property_available_advance;
    private String swm_available_advance;
    private String no_of_demand_available;

    private String installmenttype_group_id;
    private String installment_group_no;
    private String installment_group_name;
    private String from_month;
    private String to_month;
    private String demand;
    private String demandid;
    private String no_of_assessment;
    private String no_of_demand;
    private String demand_count;

    public String getDemand_count() {
        return demand_count;
    }

    public void setDemand_count(String demand_count) {
        this.demand_count = demand_count;
    }

    public String getNo_of_assessment() {
        return no_of_assessment;
    }

    public void setNo_of_assessment(String no_of_assessment) {
        this.no_of_assessment = no_of_assessment;
    }

    public String getNo_of_demand() {
        return no_of_demand;
    }

    public void setNo_of_demand(String no_of_demand) {
        this.no_of_demand = no_of_demand;
    }

    public String getStatecode() {
        return statecode;
    }

    public void setStatecode(String statecode) {
        this.statecode = statecode;
    }

    public String getState_name() {
        return state_name;
    }

    public void setState_name(String state_name) {
        this.state_name = state_name;
    }

    public String getDcode() {
        return dcode;
    }

    public void setDcode(String dcode) {
        this.dcode = dcode;
    }

    public String getDistrict_name() {
        return district_name;
    }

    public void setDistrict_name(String district_name) {
        this.district_name = district_name;
    }

    public String getLbcode() {
        return lbcode;
    }

    public void setLbcode(String lbcode) {
        this.lbcode = lbcode;
    }

    public String getBcode() {
        return bcode;
    }

    public void setBcode(String bcode) {
        this.bcode = bcode;
    }

    public String getLocalbody_name() {
        return localbody_name;
    }

    public void setLocalbody_name(String localbody_name) {
        this.localbody_name = localbody_name;
    }

    public String getLbtype() {
        return lbtype;
    }

    public void setLbtype(String lbtype) {
        this.lbtype = lbtype;
    }

    public String getAssessment_id() {
        return assessment_id;
    }

    public void setAssessment_id(String assessment_id) {
        this.assessment_id = assessment_id;
    }

    public String getAssessment_no() {
        return assessment_no;
    }

    public void setAssessment_no(String assessment_no) {
        this.assessment_no = assessment_no;
    }

    public String getDoor_no() {
        return door_no;
    }

    public void setDoor_no(String door_no) {
        this.door_no = door_no;
    }

    public String getAssessment_address() {
        return assessment_address;
    }

    public void setAssessment_address(String assessment_address) {
        this.assessment_address = assessment_address;
    }

    public String getArea_in_sq_feet() {
        return area_in_sq_feet;
    }

    public void setArea_in_sq_feet(String area_in_sq_feet) {
        this.area_in_sq_feet = area_in_sq_feet;
    }

    public String getSurveyno() {
        return surveyno;
    }

    public void setSurveyno(String surveyno) {
        this.surveyno = surveyno;
    }

    public String getSubdivisionno() {
        return subdivisionno;
    }

    public void setSubdivisionno(String subdivisionno) {
        this.subdivisionno = subdivisionno;
    }

    public String getWardid() {
        return wardid;
    }

    public void setWardid(String wardid) {
        this.wardid = wardid;
    }

    public String getWard_code() {
        return ward_code;
    }

    public void setWard_code(String ward_code) {
        this.ward_code = ward_code;
    }

    public String getWard_name_en() {
        return ward_name_en;
    }

    public void setWard_name_en(String ward_name_en) {
        this.ward_name_en = ward_name_en;
    }

    public String getStreetid() {
        return streetid;
    }

    public void setStreetid(String streetid) {
        this.streetid = streetid;
    }

    public String getBuilding_licence_no() {
        return building_licence_no;
    }

    public void setBuilding_licence_no(String building_licence_no) {
        this.building_licence_no = building_licence_no;
    }

    public String getStreet_name_en() {
        return street_name_en;
    }

    public void setStreet_name_en(String street_name_en) {
        this.street_name_en = street_name_en;
    }

    public String getStreet_name_ta() {
        return street_name_ta;
    }

    public void setStreet_name_ta(String street_name_ta) {
        this.street_name_ta = street_name_ta;
    }

    public String getHabitation_name_en() {
        return habitation_name_en;
    }

    public void setHabitation_name_en(String habitation_name_en) {
        this.habitation_name_en = habitation_name_en;
    }

    public String getHabitation_name_ta() {
        return habitation_name_ta;
    }

    public void setHabitation_name_ta(String habitation_name_ta) {
        this.habitation_name_ta = habitation_name_ta;
    }

    public String getHab_code() {
        return hab_code;
    }

    public void setHab_code(String hab_code) {
        this.hab_code = hab_code;
    }

    public String getProperty_available_advance() {
        return property_available_advance;
    }

    public void setProperty_available_advance(String property_available_advance) {
        this.property_available_advance = property_available_advance;
    }

    public String getSwm_available_advance() {
        return swm_available_advance;
    }

    public void setSwm_available_advance(String swm_available_advance) {
        this.swm_available_advance = swm_available_advance;
    }

    public String getNo_of_demand_available() {
        return no_of_demand_available;
    }

    public void setNo_of_demand_available(String no_of_demand_available) {
        this.no_of_demand_available = no_of_demand_available;
    }

    public String getInstallmenttype_group_id() {
        return installmenttype_group_id;
    }

    public void setInstallmenttype_group_id(String installmenttype_group_id) {
        this.installmenttype_group_id = installmenttype_group_id;
    }

    public String getInstallment_group_no() {
        return installment_group_no;
    }

    public void setInstallment_group_no(String installment_group_no) {
        this.installment_group_no = installment_group_no;
    }

    public String getInstallment_group_name() {
        return installment_group_name;
    }

    public void setInstallment_group_name(String installment_group_name) {
        this.installment_group_name = installment_group_name;
    }

    public String getFrom_month() {
        return from_month;
    }

    public void setFrom_month(String from_month) {
        this.from_month = from_month;
    }

    public String getTo_month() {
        return to_month;
    }

    public void setTo_month(String to_month) {
        this.to_month = to_month;
    }

    public String getDemand() {
        return demand;
    }

    public void setDemand(String demand) {
        this.demand = demand;
    }

    public String getDemandid() {
        return demandid;
    }

    public void setDemandid(String demandid) {
        this.demandid = demandid;
    }
}