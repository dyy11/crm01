layui.use(['element', 'layer', 'layuimini','jquery','jquery_cookie'], function () {
    var $ = layui.jquery,
        layer = layui.layer,
        $ = layui.jquery_cookie($);

    // 菜单初始化
    $('#layuiminiHomeTabIframe').html('<iframe width="100%" height="100%" frameborder="0"  src="welcome"></iframe>')
    layuimini.initTab();


    $(".login-out").click(function (){
        // 弹出提示框询问用户
        layer.confirm("确认退出吗",{icon:3,title:'系统提示'},function(index){
            // 关闭询问框
            layer.close(index);

            // 清空 cookie
            $.removeCookie("userIdStr",{domain:"localhost",path:"/"})
            $.removeCookie("userName",{domain:"localhost",path:"/"})
            $.removeCookie("trueName",{domain:"localhost",path:"/"})

            // 跳转到登录页面
            window.parent.location.href = ctx + "/index";
        });
    });
});