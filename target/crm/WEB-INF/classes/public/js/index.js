layui.use(['form', 'jquery', 'jquery_cookie'], function () {
    var form = layui.form,
        layer = layui.layer,
        $ = layui.jquery,
        $ = layui.jquery_cookie($);
    // 进行登录操作
    form.on('submit(login)', function (data) {
        //获得表单的数据
        data = data.field;
        //数据非空判断
        if (data.username == "undefined" || data.username == "" || data.username.trim() == "") {
            layer.msg('用户名不能为空');
            return false;
        }
        if (data.password == "undefined" || data.password == "" || data.password.trim() == "") {
            layer.msg('密码不能为空');

            return false;//阻止表单跳转
        }

        //发送 ajax 请求
        $.ajax({
            type: "post",
            url: ctx + "/user/login",
            data: {
                userName: data.username,
                userPwd: data.password
            },
            dataType: "json",
            success: function (data) {
                if (data.code == 200) {
                    layer.msg('登录成功', function () {
                        var result = data.result;
                        // 如果点击记住我 设置cookie 过期时间7天
                        if ($("#rememberMe").prop("checked")) {
                            // 写入cookie 7天
                            $.cookie("userIdStr", result.userIdStr, {expires: 7});
                            $.cookie("userName", result.userName, {expires: 7});
                            $.cookie("trueName", result.trueName, {expires: 7});
                        } else {
                            // 设置信息到 cookie
                            $.cookie("userIdStr", result.userIdStr);
                            $.cookie("userName", result.userName);
                            $.cookie("trueName", result.trueName);
                        }
                        window.location.href = ctx + "/main";
                    });
                } else {
                    layer.msg(data.msg);
                }
            }
        });
        return false;
    });
});