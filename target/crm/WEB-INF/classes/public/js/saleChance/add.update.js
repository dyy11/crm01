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

    // 监听表单 submit 事件       确认按钮提交数据
    form.on('submit(addOrUpdateSaleChance)', function (data) {
        // 数据提交时的加载层
        var index = layer.msg('数据提交中，请稍候', {
            icon: 16,       // 图标
            time: false,    // 不关闭
            shade: 0.8      // 设置遮罩的透明度
        });

        var url;
        var id = data.field.id;
        // 根据有无 id 属性值来决定执行 添加还是更新 操作
        /**
         * 视频使用  var id = $("[name='id']").val()    来获取
         */
        if(id == null || id == ''){
            url = ctx + "/sale_chance/add";
        }else {
            url = ctx + "/sale_chance/update";
        }



        $.post(url, data.field, function (result) {
            if (result.code == 200) {
                // 添加成功
                layer.msg("操作成功", {icon: 6});
                // 关闭加载层
                layer.close(index);
                // 关闭弹出层
                layer.closeAll("iframe")
                // 刷新父窗口。重新加载数据
                parent.location.reload();
            } else {
                // 添加失败
                layer.msg(result.msg, {icon: 5});
                // 关闭加载层
                layer.close(index);
            }
        });

        //阻止表单提交
        return false;

    });


    /**
     * 加载指派人的下拉框
     */
    $.ajax({
        type:"get",
        url:ctx + "/user/querySales",
        data:{},
        success:function (result){
            // console.log(result);
            // 如果返回的数据不为空
            if(result != null){
                var id = $("[name='man']").val()
                // 循环遍历
                for(var i = 0; i < result.length;i++){
                    var opt;
                    // 设置下拉选项
                    if(result[i].id == id){
                        opt = "<option selected value='" + result[i].id + "'>" + result[i].uname +"</option>";
                    }else{
                        opt = "<option value='" + result[i].id + "'>" + result[i].uname +"</option>";
                    }

                    // 将下拉选项设置到下拉框中
                    $("#assignMan").append(opt);
                }
            }
            // 重新渲染下拉框
            layui.form.render("select");
        }
    });
});