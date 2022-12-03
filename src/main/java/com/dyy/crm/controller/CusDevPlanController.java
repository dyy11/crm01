package com.dyy.crm.controller;

import com.dyy.crm.base.BaseController;
import com.dyy.crm.base.ResultInfo;
import com.dyy.crm.query.CusDevPlanQuery;

import com.dyy.crm.service.CusDecPlanService;
import com.dyy.crm.service.SaleChanceService;

import com.dyy.crm.vo.CusDevPlan;
import com.dyy.crm.vo.SaleChance;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Controller
@RequestMapping("cusDev")
public class CusDevPlanController extends BaseController {

    @Resource
    private SaleChanceService saleChanceService;

    @Resource
    private CusDecPlanService cusDecPlanService;


    /**
     * 打开客户开发计划页
     * @return
     */
    @RequestMapping("index")
    public String list(){
        return "cusDev/cus_dev_plan";
    }


    /**
     * 打开计划项开发详情页面
     * @param id
     * @param req
     * @return
     */
    @RequestMapping("toCDP")
    public String toCusDevPlan(Integer id, HttpServletRequest req){
        // 查询当前用户的信息
        SaleChance saleChance = saleChanceService.selectByPrimaryKey(id);
        // 将用户信息设置到请求作用域
        req.setAttribute("saleChance",saleChance);

        return "cusDev/cus_dev_plan_data";
    }


    /**
     * 客户开发计划对应数据查询
     * @param cusDevPlanQuery
     * @return
     */
    @RequestMapping("list")
    @ResponseBody
    public Map<String,Object> selectByParams(CusDevPlanQuery cusDevPlanQuery){
        // 判断 flag 的值
//        if(flag !=null && flag == 1){
//            // 设置分配状态
//            cusDevPlanQuery.setState(1);
//            // 设置指派人为当前用户的 id
//            int id = LoginUserUtil.releaseUserIdFromCookie(req);
//            cusDevPlanQuery.setAssignMan(id);
//        }
        return cusDecPlanService.selectByParams(cusDevPlanQuery);
    }

    /**
     * 添加计划项
     * @param cusDevPlan
     * @return
     */
    @PostMapping("add")
    @ResponseBody
    public ResultInfo addCusDevPlan(CusDevPlan cusDevPlan){
        cusDecPlanService.addCusDevPlan(cusDevPlan);
        return success("添加成功");
    }

    /**
     * 更新计划项
     * @param cusDevPlan
     * @return
     */
    @PostMapping("update")
    @ResponseBody
    public ResultInfo updateCusDevPlan(CusDevPlan cusDevPlan){
        cusDecPlanService.updateCusDevPlan(cusDevPlan);
        return success("修改成功");
    }



    /**
     * 进入添加或者修改页面
     */
    @RequestMapping("toAddUpdate")
    public String toAddUpdate(Integer saleChanceId,Integer id,HttpServletRequest req){
        // 将营销机会 id 设置到请求域中，由计划项页面获取
        req.setAttribute("id",saleChanceId);

        // 如果 id 不为空，则查对应的计划并返回给 req 对象
        if(id != null){
            CusDevPlan cusDevPlan = cusDecPlanService.selectByPrimaryKey(id);
            req.setAttribute("cusDevPlan",cusDevPlan);
        }

        return "cusDev/add_update";
    }


    /**
     * 删除操作
     */

    @PostMapping("delete")
    @ResponseBody
    public ResultInfo delete(Integer id){
        cusDecPlanService.deleteCusDevPlan(id);
        return success("删除成功");
    }
}
