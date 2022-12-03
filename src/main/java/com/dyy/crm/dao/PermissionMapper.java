package com.dyy.crm.dao;

import com.dyy.crm.base.BaseMapper;
import com.dyy.crm.vo.Permission;

import java.util.List;

public interface PermissionMapper extends BaseMapper<Permission,Integer> {
    // 通过角色 id 查询所有的权限信息
    Integer countAll(Integer roleId);
    // 移除角色 id 对应的所有的权限信息
    Integer removeBatch(Integer roleId);

    // 根据角色 id 查询所有属于的资源
    List<Integer> queryRoleHasModuleIds(Integer roleId);

    //根据用户 id 查询所属的权限资源
    List<String> queryAllById(Integer id);

    // 根据资源 id 查询对应的数据数量
    Integer countByModuleId(Integer id);

    // 根据资源 id 删除对应的权限
    Integer deleteByModuleId(Integer id);
}