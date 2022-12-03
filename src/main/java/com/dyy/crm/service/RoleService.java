package com.dyy.crm.service;

import com.dyy.crm.base.BaseService;
import com.dyy.crm.dao.ModuleMapper;
import com.dyy.crm.dao.PermissionMapper;
import com.dyy.crm.dao.RoleMapper;
import com.dyy.crm.query.RoleQuery;
import com.dyy.crm.utils.AssertUtil;
import com.dyy.crm.vo.Permission;
import com.dyy.crm.vo.Role;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import java.util.*;

@Service
public class RoleService extends BaseService<Role, Integer> {

    @Resource
    private RoleMapper roleMapper;

    @Resource
    private PermissionMapper permissionMapper;

    @Resource
    private ModuleMapper moduleMapper;

    /**
     * 查询所有的角色列表
     *
     * @return
     */
    public List<Map<String, Object>> queryAllRole(Integer id) {
        return roleMapper.queryAllRole(id);
    }

    /**
     * 新增角色
     *
     * @param role
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void insertRole(Role role) {
        // 参数校验
        checkParams(role);
        // 设置默认值
        role.setCreateDate(new Date());
        role.setUpdateDate(new Date());
        role.setIsValid(1);
        // 操作数据库
        AssertUtil.isTrue(roleMapper.insertSelective(role) < 1, "角色新增失败");
    }

    /**
     * 更新操作
     *
     * @param role
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateRole(Role role) {
        // 参数校验，选中数据是否存在，对应参数是否合理
        AssertUtil.isTrue(role.getId() == null, "角色数据不存在");
        AssertUtil.isTrue(roleMapper.selectByPrimaryKey(role.getId()) == null, "角色数据不存在");
        checkParams(role);
        // 设置默认值
        role.setUpdateDate(new Date());
        // 操作数据库
        AssertUtil.isTrue(roleMapper.updateByPrimaryKeySelective(role) < 1, "角色信息更新失败");
    }

    /**
     * 删除单个角色信息操作
     *
     * @param id
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteRole(Integer id) {
        Role role = roleMapper.selectByPrimaryKey(id);
        AssertUtil.isTrue(id == null || role == null, "角色信息不存在");
        // 设置角色信息失效
        role.setIsValid(0);
        role.setUpdateDate(new Date());

        AssertUtil.isTrue(roleMapper.updateByPrimaryKeySelective(role) < 1, "角色信息删除失败");
    }

    /**
     * 授权操作
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void addGrant(Integer[] mIds, Integer roleId) {
        // 1. 根据 roleId 查询对应的角色权限信息
        AssertUtil.isTrue(roleId == null || roleMapper.selectByPrimaryKey(roleId) == null, "角色信息异常");

        Integer count = permissionMapper.countAll(roleId);
        if(count != null && count > 0){
            // 2. 清空所有权限信息
            AssertUtil.isTrue(permissionMapper.removeBatch(roleId) != count,"数据异常");
        }
        // 3. 根据 mIds[] 赋权限
        if(mIds != null && mIds.length > 0){
            // 定义集合
            List<Permission> list = new ArrayList<>();
            // 遍历传进来的权限 id 并且赋默认值
            for (Integer mId : mIds) {
                Permission permission = new Permission();
                permission.setRoleId(roleId);
                permission.setCreateDate(new Date());
                permission.setUpdateDate(new Date());
                permission.setModuleId(mId);
                permission.setAclValue(moduleMapper.selectByPrimaryKey(mId).getOptValue());
                // 将对象添加到集合
                list.add(permission);
            }
            // 执行操作
            AssertUtil.isTrue(permissionMapper.insertBatch(list) != list.size(),"角色授权失败");
        }

    }


    /**
     * 参数校验
     *
     * @param role
     */
    private void checkParams(Role role) {
        AssertUtil.isTrue(StringUtils.isBlank(role.getRoleName()), "角色名称不可以为空");
        Role temp = roleMapper.selectByRoleName(role.getRoleName());
        AssertUtil.isTrue(temp != null && !temp.getId().equals(role.getId()), "该角色已存在");
        AssertUtil.isTrue(StringUtils.isBlank(role.getRoleRemark()), "角色描述不可以为空");
    }


}
