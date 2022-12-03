package com.dyy.crm.dao;

import com.dyy.crm.base.BaseMapper;
import com.dyy.crm.vo.SaleChance;

import java.util.Map;


public interface SaleChanceMapper extends BaseMapper<SaleChance,Integer> {

    int updateCusDev(Map map);


}