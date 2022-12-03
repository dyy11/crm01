package com.dyy.crm.service;

import com.dyy.crm.base.BaseService;
import com.dyy.crm.dao.ModuleMapper;

import com.dyy.crm.dao.PermissionMapper;
import com.dyy.crm.model.TreeModel;

import com.dyy.crm.utils.AssertUtil;
import com.dyy.crm.vo.Module;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ModuleService extends BaseService<Module, Integer> {

    @Resource
    private ModuleMapper moduleMapper;

    @Resource
    private PermissionMapper permissionMapper;


    /**
     * 查询所有信息
     */
    public List<TreeModel> queryAllModule(Integer roleId) {

        // 查询所有的资源列表
        List<TreeModel> treeModels = moduleMapper.queryAllModule();
        // 查询角色已经授权的资源
        List<Integer> permissionIds = permissionMapper.queryRoleHasModuleIds(roleId);
        // 判断角色是否拥有资源
        if (permissionIds != null && permissionIds.size() > 0) {
            treeModels.forEach(treeModel -> {
                // 如果资源属于已授权，则设置被选择为 true
                if (permissionIds.contains(treeModel.getId())) {
                    treeModel.setChecked(true);
                }
            });
        }
        return treeModels;
    }


    /**
     * 查询资源数据
     */
    public Map<String, Object> queryModuleList() {
        Map<String, Object> map = new HashMap<>();
        // 查询资源列表
        List<Module> modules = moduleMapper.queryModuleList();
        map.put("code", 0);
        map.put("msg", "");
        map.put("count", modules.size());
        map.put("data", modules);
        return map;
    }


    /**
     * 添加资源
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void addModule(Module module) {
        // 参数校验
        Integer grade = module.getGrade();
        AssertUtil.isTrue(grade == null || !(grade == 0 || grade == 1 || grade == 2), "菜单层级不合理");

        // 模块名称 module 非空
        AssertUtil.isTrue(StringUtils.isBlank(module.getModuleName()), "模块名称不能为空");
        // 模块名称 moduleName 同一层级下模块名称唯一
        AssertUtil.isTrue(moduleMapper.queryModuleByGradeAndModuleName(grade, module.getModuleName()) != null, "该层级下模块名称已存在");
        // 地址 url 二级菜单 非空，不可重复
        if (grade == 1) {
            AssertUtil.isTrue(StringUtils.isBlank(module.getUrl()), "地址不可以为空");
            AssertUtil.isTrue(moduleMapper.queryModuleByGradeAndUrl(grade, module.getUrl()) != null, "地址不可以重复");
        }
        // 父级菜单
        AssertUtil.isTrue(module.getParentId() == null, "父级菜单不可以为空");
        if (module.getParentId() != -1) {
            AssertUtil.isTrue(moduleMapper.selectByPrimaryKey(module.getParentId()) == null, "未指定父级菜单");
        }
        // 授权码,非空，不可重复
        AssertUtil.isTrue(StringUtils.isBlank(module.getOptValue()), "授权码不可以为空");
        AssertUtil.isTrue(moduleMapper.queryModuleByOptValue(module.getOptValue()) != null, "授权码不可以重复");

        // 设置对象的默认值
        module.setIsValid((byte) 1);
        module.setCreateDate(new Date());
        module.setUpdateDate(new Date());


        // 操作数据库
        AssertUtil.isTrue(moduleMapper.insertSelective(module) < 1, "添加模块失败");

    }


    /**
     * 修改资源
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateModule(Module module) {
        // id 和 根据 id 查询的对象非空判断
        Integer id = module.getId();
        AssertUtil.isTrue(id == null || moduleMapper.selectByPrimaryKey(module.getId()) == null, "待更新记录不存在");

        // 层级校验     非空，非 0，1，2 校验
        Integer grade = module.getGrade();
        AssertUtil.isTrue(grade == null || !(grade == 0 || grade == 1 || grade == 2), "菜单层级不合法");

        // 名称校验     非空，不存在重复名称
        AssertUtil.isTrue(StringUtils.isBlank(module.getModuleName()), "模块名称不能为空");
        Module temp = moduleMapper.queryModuleByGradeAndModuleName(id, module.getModuleName());
        if (temp != null) {
            AssertUtil.isTrue(!temp.getId().equals(module.getId()), "同一层级下模块名称不可以重复");
        }

        // url 校验   非空且路径唯一
        if (grade == 1) {
            AssertUtil.isTrue(StringUtils.isBlank(module.getUrl()), "二级菜单的路径不能为空");
            temp = moduleMapper.queryModuleByGradeAndUrl(id, module.getUrl());
            if (temp != null) {
                AssertUtil.isTrue(!temp.getId().equals(module.getId()), "模块路径不可以重复");
            }
        }

        // 权限码的校验
        AssertUtil.isTrue(StringUtils.isBlank(module.getOptValue()), "权限码不可以为空");
        temp = moduleMapper.queryModuleByOptValue(module.getOptValue());
        if (temp != null) {
            AssertUtil.isTrue(!temp.getId().equals(module.getId()), "模块权限码不可以重复");
        }

        // 设置默认值
        module.setUpdateDate(new Date());

        // 操作数据库更新
        AssertUtil.isTrue(moduleMapper.updateByPrimaryKeySelective(module) < 1, "更新资源失败");
    }


    /**
     * 删除资源
     * @param id
     */
    public void deleteModule(Integer id) {
        Module module = moduleMapper.selectByPrimaryKey(id);
        // 判断 id 和 id 对应的对象是否非空
        AssertUtil.isTrue(id == null || module == null,"待删除信息不存在");
        // 有子记录的对象无法删除
        Integer count = moduleMapper.queryChild(id);
        AssertUtil.isTrue(count > 0,"该记录存在子记录，无法删除");

        // 判断资源对应的权限是否存在
        count = permissionMapper.countByModuleId(id);
        if(count > 0){
            permissionMapper.deleteByModuleId(id);
        }

        module.setIsValid((byte) 0);
        module.setUpdateDate(new Date());

        AssertUtil.isTrue(moduleMapper.updateByPrimaryKeySelective(module) < 1,"删除失败");
    }
}
