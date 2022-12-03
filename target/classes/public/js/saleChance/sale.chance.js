layui.use(['table', 'layer'], function () {
    var layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery,
        table = layui.table;

    //用户列表展示
    var tableIns = table.render({
        id: 'table',
        // 容器元素的 id 属性值
        elem: '#saleChanceList',
        // 访问数据的 url (后台的数据接口)
        url: ctx + '/sale_chance/list',
        //  单元格最小宽度
        cellMinWidth: 95,
        // 开启分页
        page: true,
        // 容器高度
        height: "full-125",
        // 分页数可选项
        limits: [10, 15, 20, 25],
        // 默认分页数
        limit: 10,
        toolbar: "#toolbarDemo",
        // 表头
        cols: [[
            {type: "checkbox", fixed: "center"},
            {field: "id", title: '编号', fixed: "true"},
            {field: 'chanceSource', title: '机会来源', align: "center"},
            {field: 'customerName', title: '客户名称', align: 'center'},
            {field: 'cgjl', title: '成功几率', align: 'center'},
            {field: 'overview', title: '概要', align: 'center'},
            {field: 'linkMan', title: '联系人', align: 'center'},
            {field: 'linkPhone', title: '联系电话', align: 'center'},
            {field: 'description', title: '描述', align: 'center'},
            {field: 'createMan', title: '创建人', align: 'center'},
            {field: 'createDate', title: '创建时间', align: 'center'},
            // {field: 'assignMan', title: '指派人', align:'center'},
            {field: 'uname', title: '指派人', align: 'center'},
            {field: 'assignTime', title: '指派时间', align: 'center'},
            {
                field: 'state', title: '分配状态', align: 'center', templet: function (data) {
                    // 调用函数，返回格式化的结果
                    return formatState(data.state);
                }
            },
            {
                field: 'devResult', title: '开发状态', align: 'center', templet: function (data) {
                    // 调用函数，返回格式化的结果
                    return formatDev(data.state);
                }
            },
            {title: '操作', templet: '#saleChanceListBar', fixed: "right", align: "center", minWidth: 150}
        ]]
    });

    /**
     * 格式化分配状态值
     *      0 = 未分配     1 = 已分配     其他 = 未知
     * @param state
     */
    function formatState(state) {
        if (state == 0) {
            return "<div style='color: orange'>未分配</div>";
        } else if (state == 1) {
            return "<div style='color: green'>已分配</div>";
        } else {
            return "<div style='color: orangered'>未知状态</div>";
        }
    }

    /**
     * 格式化开发状态
     *      0 = 未开发     1 = 开发中     2 = 开发成功    3 = 开发失败    其他 = 未知
     * @param state
     * @returns {string}
     */
    function formatDev(dev) {
        if (dev == 0) {
            return "<div style='color: orange'>未开发</div>";
        } else if (dev == 1) {
            return "<div style='color: deepskyblue'>开发中</div>";
        } else if (dev == 2) {
            return "<div style='color: green'>开发成功</div>";
        } else if (dev == 3) {
            return "<div style='color: red'>开发失败</div>";
        } else {
            return "<div style='color: orangered'>未知状态</div>";
        }
    }


    $(".search_btn").click(function (data) {
        /**
         * 表格重载     多条件查询
         */
        var data = data.field;
        tableIns.reload({
            // 设置要传递给后端的参数
            where: { //设定异步数据接口的额外参数
                customerName: $("[name='customerName']").val(),    //客户名称
                createMan: $("[name='creatMan']").val(),       //创建人
                state: $("#state").val()           //状态
            },
            page: {
                curr: 1              //重新从第一页开始
            }
        });
    });


    /**
     * 监听头部工具栏
     */
    table.on('toolbar(saleChances)', function (data) {
        // console.log(data);
        // 判断对应时间类型
        if (data.event == 'add') {
            // 添加操作
            openSalChanceDialog();
        } else if (data.event == "del") {
            // 删除操作
            deleteSaleChance(data);
        }
    });


    /**
     * 监听 操作/行 工具栏
     */
    table.on('tool(saleChances)', function (data) {
        // console.log(data);

        // 获得对应的 id
        var id = data.data.id;

        if (data.event == 'edit') {
            // 更新营销机会
            openSalChanceDialog(id);
        } else if (data.event == 'del') {
            // 是否确定删除
            layer.confirm("确认删除该记录吗", {icon: 3, title: "删除记录"}, function (index) {
                // close
                layer.close(index);
                // 删除营销机会
                $.ajax({
                    type: 'post',
                    url: ctx + '/sale_chance/delete',
                    data: {ids: id},
                    success: function (res) {
                        // 判断是否成功
                        if (res.code == 200) {
                            layer.msg("删除成功", {icon: 6});
                            // 刷新表格
                            tableIns.reload();
                        } else {
                            layer.msg(res.msg, {icon: 5});
                        }
                    }
                });

                // deleteSaleChance(id);
            });
        }
    });


    /**
     * 打开增加营销记录信息的窗口
     */
    function openSalChanceDialog(id) {
        // 根据是否有 id 属性值来判断是增加还是修改操作
        var url;
        if (id != null && id != '') {
            url = ctx + '/sale_chance/toAdd?id=' + id;
            title = '<h3>更新营销机会</h3>';
        } else {
            url = ctx + '/sale_chance/toAdd';
            title = '<h3>增加营销机会</h3>';
        }

        //iframe 层
        layui.layer.open({
            // 类型
            type: 2,
            // 标题
            title: title,
            maxmin: true, //开启最大化最小化按钮
            // 宽高
            area: ['500px', '620px'],
            // url 地址
            content: url
        });
    }


    /**
     * 删除操作,多条
     */
    function deleteSaleChance(data) {
        // 获取数据表格选中的行数据，固定写法     table.checkStatus("table");
        var checked = table.checkStatus("table");
        var data = checked.data;

        // 判断选中行是否大于 0
        if (data.length > 0) {
            // 是否确定删除
            layer.confirm("确认删除该记录吗", {icon: 3, title: "删除记录"}, function (index) {
                // close
                layer.close(index);
                // 传递的参数是数组
                var ids = "ids=";
                for (var i = 0; i < data.length; i++) {
                    if(i == data.length - 1){
                        ids += data[i].id;
                    }else {
                        ids += data[i].id + "&ids=";
                    }
                }
                // 提交数据
                $.ajax({
                    type: 'post',
                    url: ctx + '/sale_chance/delete',
                    data: ids,
                    success: function (res) {
                        // 判断是否成功
                        if (res.code == 200) {
                            layer.msg("删除成功", {icon: 6});
                            // 刷新表格
                            tableIns.reload();
                        } else {
                            layer.msg(res.msg, {icon: 5});
                        }
                    }
                });
            });
        } else {
            layer.msg("请选中要删除的数据")
        }

    }
});
