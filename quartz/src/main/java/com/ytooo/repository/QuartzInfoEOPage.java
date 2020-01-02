package com.ytooo.repository;

import lombok.Data;

@Data
public class QuartzInfoEOPage {

    private String id;
    private String idOperator = "=";
    private String batchType;
    private String batchTypeOperator = "=";
    private String name;
    private String nameOperator = "=";
    private String executeTime;
    private String executeTimeOperator = "=";
    private String objectName;
    private String objectNameOperator = "=";
    private String objectMethod;
    private String objectMethodOperator = "=";
    private String batchDesc;
    private String batchDescOperator = "=";
    private String executeParameter;
    private String executeParameterOperator = "=";
    private String createdDate;
    private String createdDate1;
    private String createdDate2;
    private String createdDateOperator = "=";
    private String modifiedDate;
    private String modifiedDate1;
    private String modifiedDate2;
    private String modifiedDateOperator = "=";
    private String isDelete;
    private String isDeleteOperator = "=";

}
