package com.dyy.crm.dao;

import com.dyy.crm.base.BaseMapper;
import com.dyy.crm.vo.Role;

import java.util.List;
import java.util.Map;

public interface RoleMapper extends BaseMapper<Role,Integer> {

    List<Map<String,Object>> queryAllRole(Integer id);

    Role selectByRoleName(String roleName);
}