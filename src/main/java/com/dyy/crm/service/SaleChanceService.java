package com.dyy.crm.service;

import com.dyy.crm.base.BaseService;
import com.dyy.crm.dao.SaleChanceMapper;
import com.dyy.crm.query.SaleChanceQuery;
import com.dyy.crm.utils.AssertUtil;
import com.dyy.crm.utils.PhoneUtil;
import com.dyy.crm.vo.SaleChance;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.validation.constraints.Max;
import java.awt.print.PrinterJob;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class SaleChanceService extends BaseService<SaleChance, Integer> {

    @Resource
    private SaleChanceMapper scm;


    /**
     * 多条件分页查询营销机会
     *
     * @param saleChanceQuery
     * @return
     */
    public Map<String, Object> selectByParams(SaleChanceQuery saleChanceQuery) {

        HashMap<String, Object> map = new HashMap<>();

        // 开启分页
        PageHelper.startPage(saleChanceQuery.getPage(), saleChanceQuery.getLimit());
        // 得到对应的分页对象
        PageInfo<SaleChance> pageInfo = new PageInfo<>(scm.selectByParams(saleChanceQuery));

        // 设置 map 对象
        map.put("code", 0);
        map.put("msg", "success");
        map.put("count", pageInfo.getTotal());

        // 设置查询好的分页数据
        map.put("data", pageInfo.getList());

        return map;
    }


    /**
     * 增加营销机会信息
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void addSalChance(SaleChance saleChance) {
        // 1. 参数校验
        checkParams(saleChance);
        // 1.设置相关字段默认值
        // 是否有效、创建时间、更新时间
        saleChance.setIsValid(1);
        saleChance.setCreateDate(new Date());
        saleChance.setUpdateDate(new Date());
        // 判断是否有指派人
        if (StringUtils.isNotBlank(saleChance.getAssignMan())) {
            // 无指派人，设置 未分配、无指派时间、未开发
            saleChance.setState(0);
            saleChance.setAssignTime(null);
            saleChance.setDevResult(0);
        } else {
            // 有指派人，设置 已分配、指派时间、开发中
            saleChance.setState(1);
            saleChance.setAssignTime(new Date());
            saleChance.setDevResult(1);
        }

        AssertUtil.isTrue(scm.insertSelective(saleChance) < 1, "添加失败");
    }


    /**
     * 更新营销机会
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateSalChance(SaleChance saleChance) {
        // 营销机会 id 非空，数据库中对应记录存在
        Integer id = saleChance.getId();
        SaleChance old = scm.selectByPrimaryKey(id);
        AssertUtil.isTrue(id == null || old == null, "待更新记录不存在");
        //参数校验
        checkParams(saleChance);
        //设置参数的默认值
        saleChance.setUpdateDate(new Date());

        //原来的数据中指派人是否为空
        if (StringUtils.isBlank(old.getAssignMan())) {    // 不存在
            if (!StringUtils.isBlank(saleChance.getAssignMan())) {    //原来没值，现在有值
                saleChance.setAssignTime(new Date());
                saleChance.setState(1);
                saleChance.setDevResult(1);
            }
        } else {    // 存在
            if (StringUtils.isBlank(saleChance.getAssignMan())) {     //原来有指派人，现在没
                saleChance.setAssignTime(null);
                saleChance.setState(0);
                saleChance.setDevResult(0);
            } else {  //原来有，现在有
                if (!old.getAssignMan().equals(saleChance.getAssignMan())) {       //原来与现在不是同一人
                    saleChance.setAssignTime(new Date());
                }
            }
        }

        AssertUtil.isTrue(scm.updateByPrimaryKeySelective(saleChance) < 1, "更新失败");

    }


    /**
     * 参数校验
     *
     * @param saleChance
     */
    public void checkParams(SaleChance saleChance) {
        AssertUtil.isTrue(StringUtils.isBlank(saleChance.getCustomerName()), "客户名称不能为空");
        AssertUtil.isTrue(StringUtils.isBlank(saleChance.getLinkMan()), "联系人不能为空");
        AssertUtil.isTrue(StringUtils.isBlank(saleChance.getLinkPhone()), "联系方式不能为空");
        AssertUtil.isTrue(!PhoneUtil.isMobile(saleChance.getLinkPhone()), "联系方式不正确");
    }


    /**
     * 删除数据
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteBatches(Integer[] ids) {
        // 判断数组是否为空
        AssertUtil.isTrue(ids == null || ids.length < 1, "待删除记录不存在");
        // 判断删除是否成功
        AssertUtil.isTrue(scm.deleteBatch(ids) != ids.length, "删除失败");

    }


    /**
     * 开发成功或者开发失败
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateCusDev(Integer id, Integer code) {
        HashMap<String, Integer> map = new HashMap<>();
        map.put("id", id);
        map.put("devResult", code);
        AssertUtil.isTrue(scm.updateCusDev(map) < 1, "操作失败，请稍后重试");
    }


}
