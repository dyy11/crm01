package com.dyy.crm.service;


import com.dyy.crm.base.BaseService;
import com.dyy.crm.dao.UserRoleMapper;
import com.dyy.crm.vo.UserRole;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class UserRoleService extends BaseService<UserRole,Integer> {

    @Resource
    private UserRoleMapper userRoleMapper;


}
