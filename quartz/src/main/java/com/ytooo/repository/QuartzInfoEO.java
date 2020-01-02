package com.ytooo.repository;

import lombok.Data;

import java.util.Date;

@Data
public class QuartzInfoEO {

    private Integer id;
    private String batchType;
    private String name;
    private String executeTime;
    private String objectName;
    private String objectMethod;
    private String batchDesc;
    private String executeParameter;
    @org.springframework.format.annotation.DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date createdDate;
    @org.springframework.format.annotation.DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date modifiedDate;
    private Integer isDelete;

    /**
     * java字段名转换为原始数据库列名。<b>如果不存在则返回null</b><br>
     * <p>字段列表：</p>
     * <li>id -> id</li>
     * <li>batchType -> batch_type</li>
     * <li>name -> name</li>
     * <li>executeTime -> execute_time</li>
     * <li>objectName -> object_name</li>
     * <li>objectMethod -> object_method</li>
     * <li>batchDesc -> batch_desc</li>
     * <li>executeParameter -> execute_parameter</li>
     * <li>createdDate -> created_date</li>
     * <li>modifiedDate -> modified_date</li>
     * <li>isDelete -> is_delete</li>
     */
    public static String fieldToColumn(String fieldName) {
        if (fieldName == null) {return null;}
        switch (fieldName) {
            case "id": return "id";
            case "batchType": return "batch_type";
            case "name": return "name";
            case "executeTime": return "execute_time";
            case "objectName": return "object_name";
            case "objectMethod": return "object_method";
            case "batchDesc": return "batch_desc";
            case "executeParameter": return "execute_parameter";
            case "createdDate": return "created_date";
            case "modifiedDate": return "modified_date";
            case "isDelete": return "is_delete";
            default: return null;
        }
    }

    /**
     * 原始数据库列名转换为java字段名。<b>如果不存在则返回null</b><br>
     * <p>字段列表：</p>
     * <li>id -> id</li>
     * <li>batch_type -> batchType</li>
     * <li>name -> name</li>
     * <li>execute_time -> executeTime</li>
     * <li>object_name -> objectName</li>
     * <li>object_method -> objectMethod</li>
     * <li>batch_desc -> batchDesc</li>
     * <li>execute_parameter -> executeParameter</li>
     * <li>created_date -> createdDate</li>
     * <li>modified_date -> modifiedDate</li>
     * <li>is_delete -> isDelete</li>
     */
    public static String columnToField(String columnName) {
        if (columnName == null) {return null;}
        switch (columnName) {
            case "id": return "id";
            case "batch_type": return "batchType";
            case "name": return "name";
            case "execute_time": return "executeTime";
            case "object_name": return "objectName";
            case "object_method": return "objectMethod";
            case "batch_desc": return "batchDesc";
            case "execute_parameter": return "executeParameter";
            case "created_date": return "createdDate";
            case "modified_date": return "modifiedDate";
            case "is_delete": return "isDelete";
            default: return null;
        }
    }

}
