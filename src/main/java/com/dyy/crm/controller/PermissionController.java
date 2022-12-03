package com.dyy.crm.controller;

import com.dyy.crm.base.BaseController;
import com.dyy.crm.service.PermissionService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;

@RequestMapping("per")
@Controller
public class PermissionController extends BaseController {

    @Resource
    private PermissionService permissionService;
}
