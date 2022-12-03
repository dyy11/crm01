package com.dyy.crm.query;


import com.dyy.crm.base.BaseMapper;
import com.dyy.crm.base.BaseQuery;

public class UserQuery extends BaseQuery {

    // 用户名
    private String userName;
    // 邮箱
    private String email;
    // 手机号码
    private String phone;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
