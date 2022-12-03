package com.dyy.crm.service;

import com.dyy.crm.base.BaseService;
import com.dyy.crm.dao.PermissionMapper;
import com.dyy.crm.vo.Permission;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;


@Service
public class PermissionService extends BaseService<Permission,Integer> {

    @Resource
    private PermissionMapper permissionMapper;

    /**
     * 根据用户 id 查询所属的权限资源
     * @param id
     * @return
     */
    public List<String> queryAllById(Integer id) {
        return permissionMapper.queryAllById(id);
    }
}
