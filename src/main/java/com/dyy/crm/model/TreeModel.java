package com.dyy.crm.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TreeModel {

    private Integer id;
    private Integer pId;
    private String name;
    // 角色权限复选框是否被勾选，默认不被勾选
    private boolean checked = false;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }


    @JsonProperty("pId")
    public Integer getPId() {
        return pId;
    }

    public void setPId(Integer pId) {
        this.pId = pId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }
}
