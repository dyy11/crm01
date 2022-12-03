layui.use(['table','layer'],function(){
    var layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery,
        table = layui.table;

    //计划项数据展示
    var  tableIns = table.render({
        elem: '#cusDevPlanList',
        url : ctx+'/cusDev/list?saleChanceId='+$("input[name='id']").val(),
        cellMinWidth : 95,
        page : true,
        height : "full-125",
        limits : [10,15,20,25],
        limit : 10,
        toolbar: "#toolbarDemo",
        id : "cusDevPlanListTable",
        cols : [[
            {type: "checkbox", fixed:"center"},
            {field: "id", title:'编号',fixed:"true"},
            {field: 'planItem', title: '计划项',align:"center"},
            {field: 'exeAffect', title: '执行效果',align:"center"},
            {field: 'planDate', title: '执行时间',align:"center"},
            {field: 'createDate', title: '创建时间',align:"center"},
            {field: 'updateDate', title: '更新时间',align:"center"},
            {title: '操作',fixed:"right",align:"center", minWidth:150,templet:"#cusDevPlanListBar"}
        ]]
    });

    //头工具栏事件
    table.on('toolbar(cusDevPlans)', function(obj){
        switch(obj.event){
            // 添加计划项
            case "add":
                openAddOrUpdateCusDevPlanDialog();
                break;
            // 计划成功
            case "success":
                // 顺便将隐藏域中的当前用户营销机会 id 和开发状态码 传入
                updateSaleChanceDevResult($("input[name='id']").val(),2);
                break;
            // 计划失败
            case "failed":
                updateSaleChanceDevResult($("input[name='id']").val(),3);
                break;
        };
    });

    /**
     * 行监听
     */
    table.on("tool(cusDevPlans)", function(obj){

        var layEvent = obj.event;

        if(layEvent === "edit") {
            openAddOrUpdateCusDevPlanDialog(obj.data.id);
        }else if(layEvent === "del") {
            // 删除
            layer.confirm('确定删除当前数据？', {icon: 3, title: "开发计划管理"}, function (index) {
                $.post(ctx+"/cusDev/delete",{id:obj.data.id},function (data) {
                    // 根据后台传回来的数据确定是否执行成功并且返回对应信息
                    if(data.code==200){
                        layer.msg(data.msg,{icon:6});
                        tableIns.reload();
                    }else{
                        layer.msg(data.msg, {icon: 5});
                    }
                });
            })
        }

    });

    // 打开添加计划项数据页面
    function openAddOrUpdateCusDevPlanDialog(id){
        // 该 saleChanceId 是营销机会的 id
        var url  =  ctx+"/cusDev/toAddUpdate?saleChanceId="+$("input[name='id']").val();
        var title="计划项管理-添加计划项";

        // 根据有无 id 判断是新增还是更新
        if(id){
            // 该 id 是计划的 id
            url = url+"&id="+id;
            title="计划项管理-更新计划项";
        }

        layui.layer.open({
            title : title,
            type : 2,
            area:["700px","400px"],
            maxmin:true,
            content : url
        });
    }

    /**
     * 更新营销机会的开发状态
     * @param sid
     * @param devResult
     */
    function updateSaleChanceDevResult(sid,devResult) {
        // 弹出确认层，询问用户是否继续操作
        layer.confirm('确定执行当前操作？', {icon: 3, title: "计划项维护"}, function (index) {
            // 发送请求到更新操作
            $.post(ctx+"/sale_chance/update",
                {
                    id:sid,
                    devResult:devResult
                },function (data) {
                if(data.code==200){
                    layer.msg("操作成功！");
                    layer.closeAll("iframe");
                    //刷新父页面
                    parent.location.reload();
                }else{
                    layer.msg(data.msg, {icon: 5});
                }
            });
        })
    }




});
