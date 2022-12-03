package com.dyy.crm.query;

import com.dyy.crm.base.BaseQuery;

import java.util.Date;

public class CusDevPlanQuery extends BaseQuery {

    private Integer saleChanceId;   // 营销机会的主键


    public Integer getSaleChanceId() {
        return saleChanceId;
    }

    public void setSaleChanceId(Integer saleChanceId) {
        this.saleChanceId = saleChanceId;
    }
}
