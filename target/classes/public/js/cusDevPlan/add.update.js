layui.use(['form', 'layer'], function () {
    var form = layui.form,
        layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery;

    // 关闭弹出层
    $("#au-close").click(function () {
        //当你在iframe页面关闭自身时
        var index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
        parent.layer.close(index); //执行关闭
    });


    form.on("submit(addOrUpdateCusDevPlan)", function (data) {

        //数据提交时的加载层、弹出loading
        var index = top.layer.msg('数据提交中，请稍候', {icon: 16, time: false, shade: 0.8});

        var url = ctx + "/cusDev/add";
        // 如果隐藏域中的当前用户 id 存在，执行更新操作
        if ($("input[name='id']").val()) {
            url = ctx + "/cusDev/update";
        }

        $.post(url, data.field, function (res) {

            if (res.code == 200) {
                setTimeout(function () {
                    top.layer.close(index);
                    top.layer.msg("操作成功！");
                    layer.closeAll("iframe");
                    //刷新父页面
                    parent.location.reload();
                }, 500);
            } else {
                layer.msg(res.msg, {icon: 5});
            }
        });
        return false;
    });
});