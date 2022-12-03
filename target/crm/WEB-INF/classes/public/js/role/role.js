layui.use(['table', 'layer'], function () {
    var layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery,
        table = layui.table;

    //角色列表展示
    var tableIns = table.render({
        elem: '#roleList',
        url: ctx + '/role/query',
        cellMinWidth: 95,
        page: true,
        height: "full-125",
        limits: [10, 15, 20, 25],
        limit: 10,
        toolbar: "#toolbarDemo",
        id: "roleListTable",
        cols: [[
            {type: "checkbox", fixed: "left", width: 50},
            {field: "id", title: '编号', fixed: "true", width: 80},
            {field: 'roleName', title: '角色名', minWidth: 50, align: "center"},
            {field: 'roleRemark', title: '角色备注', minWidth: 100, align: 'center'},
            {field: 'createDate', title: '创建时间', align: 'center', minWidth: 150},
            {field: 'updateDate', title: '更新时间', align: 'center', minWidth: 150},
            {title: '操作', minWidth: 150, templet: '#roleListBar', fixed: "right", align: "center"}
        ]]
    });


    // 多条件搜索
    $(".search_btn").on("click", function () {
        table.reload("roleListTable", {
            page: {
                curr: 1
            },
            where: {
                // 角色名
                roleName: $("input[name='roleName']").val()
            }
        })
    });

    // 监听 头部 工具栏事件
    table.on('toolbar(roles)', function (obj) {
        // 匹配按钮事件
        switch (obj.event) {
            case "add":     // 添加视图
                openAddOrUpdateRoleDialog();
                break;
            case "grant":   // 授权视图
                var checkedStatus = table.checkStatus(obj.config.id);
                openGrant(checkedStatus.data);
                break;
            // case 'del':     //删除
            //     console.log(obj);
            //     console.log(table.checkStatus(obj.config.id));
            //     console.log(table.checkStatus(obj.config.id).data);
            //     delRole(table.checkStatus(obj.config.id).data);
            //     break;
        }
    });

    // 监听 行 工具栏
    table.on('tool(roles)', function (obj) {
        var layEvent = obj.event;
        if (layEvent === "edit") {
            // 编辑操作
            openAddOrUpdateRoleDialog(obj.data.id);
        } else if (layEvent === "del") {

            // 删除操作
            layer.confirm("确认删除当前记录?", {icon: 3, title: "角色管理"}, function (index) {
                $.post(ctx + "/role/delete", {id: obj.data.id}, function (data) {
                    if (data.code == 200) {
                        layer.msg("删除成功");
                        tableIns.reload();
                    } else {
                        layer.msg(data.msg);
                    }
                })
            })
        }
    });


    function openAddOrUpdateRoleDialog(id) {
        var title = "角色管理-角色添加";
        var url = ctx + "/role/addOrUpdateRolePage";
        // 有 id 就更新
        if (id) {
            title = "角色管理-角色更新";
            url = url + "?id=" + id;
        }

        layui.layer.open({
            title: title,
            type: 2,
            area: ["700px", "500px"],
            maxmin: true,
            content: url
        })
    }


    function openGrant(data) {
        if (data.length == 0) {
            layer.msg("请选择要授权的数据", {icon: 5});
            return
        }

        if (data.length > 1) {
            layer.msg("不支持批量授权", {icon: 5});
            return
        }

        var url = ctx + "/module/toAddGrantPage?id=" + data[0].id;
        var title = "<h3>角色管理-角色授权</h3>";
        layui.layer.open({
            title: title,
            content: url,
            type:2,
            area: ["600px","600px"],
            maxmin:true,
        });
    }

    // 错误的批量删除
    // function delRole(datas){
    //     if(datas.length < 1){
    //         layer.msg("请选择待删除记录!");
    //         return;
    //     }
    //
    //     layer.confirm("确定删除选中的记录", {
    //         btn: ['确定', '取消']
    //     }, function (index) {
    //         layer.close(index);
    //         // ids=10&ids=20&ids=30
    //         var ids = "ids=";
    //         // 拼接要删除的信息记录 id
    //         for (var i = 0; i < datas.length; i++) {
    //             if (i < datas.length - 1) {
    //                 ids = ids + datas[i].id + "&ids=";
    //             } else {
    //                 ids = ids + datas[i].id;
    //             }
    //         }
    //         $.ajax({
    //             type: "post",
    //             url: ctx + "/role/delete",
    //             data: ids,
    //             dataType: "json",
    //             success: function (data) {
    //                 if (data.code == 200) {
    //                     tableIns.reload();
    //                 } else {
    //                     layer.msg(data.msg);
    //                 }
    //             }
    //         })
    //
    //
    //     })
    // }
});
