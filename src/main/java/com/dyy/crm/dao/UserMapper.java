package com.dyy.crm.dao;


import com.dyy.crm.base.BaseMapper;
import com.dyy.crm.query.UserQuery;
import com.dyy.crm.vo.User;

import java.util.List;
import java.util.Map;


public interface UserMapper extends BaseMapper<User,Integer> {

    //根据用户姓名查询用户记录
    User queryUserByName(String userName);

    // 查询所有销售人员
    List<Map<String,Object>> querySales();

    // 删除用户信息
    int removeUser(Integer[] ids);


}