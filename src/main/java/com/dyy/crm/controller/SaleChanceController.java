package com.dyy.crm.controller;


import com.dyy.crm.annoation.RequiredPermission;
import com.dyy.crm.base.BaseController;
import com.dyy.crm.base.ResultInfo;
import com.dyy.crm.query.SaleChanceQuery;
import com.dyy.crm.service.SaleChanceService;
import com.dyy.crm.utils.CookieUtil;
import com.dyy.crm.utils.LoginUserUtil;
import com.dyy.crm.vo.SaleChance;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("sale_chance")
public class SaleChanceController extends BaseController {

    @Resource
    private SaleChanceService scs;

    /**
     * 营销机会对应数据查询   101001
     * @param saleChanceQuery
     * @return
     */
    @RequiredPermission(code = "101001")
    @RequestMapping("list")
    @ResponseBody
    public Map<String,Object> selectByParams(SaleChanceQuery saleChanceQuery,Integer flag,HttpServletRequest req){
        // 判断 flag 的值
        if(flag !=null && flag == 1){
            // 设置分配状态
            saleChanceQuery.setState(1);
            // 设置指派人为当前用户的 id
            int id = LoginUserUtil.releaseUserIdFromCookie(req);
            saleChanceQuery.setAssignMan(id);
        }
        return scs.selectByParams(saleChanceQuery);
    }


    /**
     * 进入营销管理页面     1010
     * @return
     */
    @RequiredPermission(code = "1010")
    @RequestMapping("index")
    public String index(){
        return "saleChance/sale_chance";
    }


    /**
     * 添加营销记录       101002
     * @param saleChance
     * @return
     */
    @RequiredPermission(code = "101002")
    @RequestMapping("add")
    @ResponseBody
    public ResultInfo add(SaleChance saleChance, HttpServletRequest req){
        // 从 cookie 中获取到当前用户名称的值
        String userName = CookieUtil.getCookieValue(req, "userName");
        // 设置创建人名称到记录对象
        saleChance.setCreateMan(userName);

        // 调用方法增加记录
        scs.addSalChance(saleChance);
        return success("添加成功");
    }


    /**
     * 更新营销记录   101004
     * @param saleChance
     * @return
     */
    @RequiredPermission(code = "101004")
    @PostMapping("update")
    @ResponseBody
    public ResultInfo update(SaleChance saleChance,Integer id,Integer devResult){
//        SaleChance chance = scs.selectByPrimaryKey(id);
        if(id == null || id == 0){
            // 调用方法增加记录
            scs.updateSalChance(saleChance);
        }else{ // 计划成功
            scs.updateCusDev(id,devResult);
        }
        return success("修改成功");
    }


    /**
     * 进入添加或者修改界面
     * @return
     */
    @RequestMapping("toAdd")
    public String toAdd(Integer id,HttpServletRequest req){
        // 如果 id 不为空，则查询对应的数据返回给请求对象，便于前端回显
        if(id != null){
            SaleChance saleChance = scs.selectByPrimaryKey(id);
            req.setAttribute("saleChance",saleChance);
        }

        return "saleChance/add_update";
    }


    /**
     * 删除       101003
     * @param ids
     * @return
     */
    @RequiredPermission(code = "101003")
    @PostMapping("delete")
    @ResponseBody
    public ResultInfo deleteBatches(Integer[] ids){
        // 调用 service  方法
       scs.deleteBatches(ids);
       return success("删除成功");
    }

}
