package com.dyy.crm.service;

import com.dyy.crm.base.BaseService;
import com.dyy.crm.dao.CusDevPlanMapper;
import com.dyy.crm.dao.SaleChanceMapper;
import com.dyy.crm.query.CusDevPlanQuery;
import com.dyy.crm.query.SaleChanceQuery;
import com.dyy.crm.utils.AssertUtil;
import com.dyy.crm.vo.CusDevPlan;
import com.dyy.crm.vo.SaleChance;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class CusDecPlanService extends BaseService<CusDevPlan,Integer> {

    @Resource
    private CusDevPlanMapper cusDevPlanMapper;

    @Resource
    private SaleChanceMapper saleChanceMapper;

    /**
     * 多条件分页查询客户开发计划
     */
    public Map<String, Object> selectByParams(CusDevPlanQuery cusDevPlanQuery) {

        HashMap<String, Object> map = new HashMap<>();

        // 开启分页
        PageHelper.startPage(cusDevPlanQuery.getPage(), cusDevPlanQuery.getLimit());
        // 得到对应的分页对象
        PageInfo<CusDevPlan> pageInfo = new PageInfo<>(cusDevPlanMapper.selectByParams(cusDevPlanQuery));

        // 设置 map 对象
        map.put("code", 0);
        map.put("msg", "success");
        map.put("count", pageInfo.getTotal());

        // 设置查询好的分页数据
        map.put("data", pageInfo.getList());

        return map;
    }


    /**
     * 添加客户开发计划数据
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void addCusDevPlan(CusDevPlan cusDevPlan){
        // 参数校验
        checkParams(cusDevPlan);
        // 设置参数的默认值、创建时间、修改时间
        cusDevPlan.setIsValid(1);
        cusDevPlan.setCreateDate(new Date());
        cusDevPlan.setUpdateDate(new Date());
        // 执行添加操作
        AssertUtil.isTrue(cusDevPlanMapper.insertSelective(cusDevPlan) < 1,"添加失败");

    }


    /**
     * 更新客户开发计划
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateCusDevPlan(CusDevPlan cusDevPlan){
        // 参数校验
        checkParams(cusDevPlan);
        // 计划项 id 非空判断数据是否存在
        Integer id = cusDevPlan.getId();
        AssertUtil.isTrue(id == null || cusDevPlanMapper.selectByPrimaryKey(id) == null,
                "数据异常、请重试");
        // 设置默认值    修改时间
        cusDevPlan.setUpdateDate(new Date());
        // 执行更新操作
        AssertUtil.isTrue(cusDevPlanMapper.updateByPrimaryKeySelective(cusDevPlan) < 1,"更新失败");
    }


    /**
     * 参数校验
     * @param cusDevPlan
     */
    private void checkParams(CusDevPlan cusDevPlan) {
        // 营销机会 id 非空，对应数据 非空判断
        Integer id = cusDevPlan.getSaleChanceId();
        AssertUtil.isTrue( id == null ||
                saleChanceMapper.selectByPrimaryKey(id) == null,"数据异常，请重试");
        // 计划项非空判断
        AssertUtil.isTrue(StringUtils.isBlank(cusDevPlan.getPlanItem()),"计划项不可以为空");
        // 计划时间非空判断
        AssertUtil.isTrue(cusDevPlan.getPlanDate() == null,"计划时间不可以为空");
    }


    /**
     * 客户开发计划数据删除
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteCusDevPlan(Integer planId){
        AssertUtil.isTrue(planId == null || cusDevPlanMapper.selectByPrimaryKey(planId) == null,"数据异常，请重试");
        AssertUtil.isTrue(cusDevPlanMapper.delete(planId) < 1,"删除失败");
    }
}
