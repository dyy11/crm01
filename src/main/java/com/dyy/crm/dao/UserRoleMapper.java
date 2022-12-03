package com.dyy.crm.dao;

import com.dyy.crm.base.BaseMapper;
import com.dyy.crm.vo.UserRole;

public interface UserRoleMapper extends BaseMapper<UserRole,Integer> {


    // 根据用户 id 删除所有角色信息
    Integer removeUserRoleByUserId(Integer useId);
    // 根据用户 id 查询用户所有的角色信息
    Integer countUserRoleByUserId(Integer id);


}