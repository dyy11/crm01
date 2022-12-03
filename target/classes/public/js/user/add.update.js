layui.use(['form', 'layer', 'formSelects'], function () {
    var form = layui.form,
        layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery;
    var formSelects = layui.formSelects;


    /**
     * 配置远程搜索，请求头，请求参数，请求类型
     * formSelects.config(ID,Options,isJson)
     * ID           xm-select 的值
     * Options      配置项  主要
     * isJson       是否传输 json 格式数据
     */

    var userId = $("input[name='id']").val();
    formSelects.config('selectId', {
        type: "post",
        searchUrl: ctx + "/role/list?id=" + userId,
        //自定义返回数据中name的key, 默认 name
        keyName: 'role_name',
        //自定义返回数据中value的key, 默认 value
        keyVal: 'id'
    }, true);

    // 关闭弹出层
    $("#au-close").click(function () {
        //当你在iframe页面关闭自身时
        var index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
        parent.layer.close(index); //执行关闭
    });

    // 监听表单 submit 事件       确认按钮提交数据
    form.on('submit(addOrUpdateUser)', function (data) {
        var index = top.layer.msg("数据提交中,请稍后...", {icon: 16, time: false, shade: 0.8});
        var url = ctx + "/user/add";

        // 根据隐藏域有没有值决定进行的是新增还是修改操作
        if ($("input[name='id']").val()) {
            url = ctx + "/user/updateUser";
        }

        // console.log(data.field);     其中的 roleIds 为下拉框中值的 id

        $.post(url, data.field, function (res) {
            if (res.code == 200) {
                top.layer.msg("操作成功");
                top.layer.close(index);
                layer.closeAll("iframe");
                // 刷新父页面
                parent.location.reload();
            } else {
                layer.msg(res.msg);
            }
        });
        return false;
    });


});