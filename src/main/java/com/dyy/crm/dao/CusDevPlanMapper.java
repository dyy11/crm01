package com.dyy.crm.dao;

import com.dyy.crm.base.BaseMapper;
import com.dyy.crm.vo.CusDevPlan;


public interface CusDevPlanMapper extends BaseMapper<CusDevPlan,Integer> {

    int delete(Integer id);


}