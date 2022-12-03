package com.dyy.crm.dao;


import com.dyy.crm.base.BaseMapper;
import com.dyy.crm.model.TreeModel;
import com.dyy.crm.vo.Module;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ModuleMapper extends BaseMapper<Module, Integer> {

    // 查询所有的资源列表
    List<TreeModel> queryAllModule();

    // 查询所有的资源数据
    List<Module> queryModuleList();

    // 根据模块层级、模块名称查询模块对象
    Module queryModuleByGradeAndModuleName(@Param("grade") Integer grade, @Param("moduleName") String moduleName);

    // 根据模块层级、模块地址查询模块对象
    Module queryModuleByGradeAndUrl(@Param("grade")Integer grade,@Param("url") String url);

    // 根据权限码查询模块对象
    Module queryModuleByOptValue(String optValue);

    // 查询子记录的数量
    Integer queryChild(Integer id);

    // 虚假的删除
    Integer deleteModule(Integer id);
}