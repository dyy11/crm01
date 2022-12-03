layui.use(['form','jquery','jquery_cookie'], function () {
    var form = layui.form,
        layer = layui.layer,
        $ = layui.jquery,
        $ = layui.jquery_cookie($);


    /**
     * 表单的 submit 监听
     *      固定写法
     *      form.on('submit(按钮的 lay-filter 属性值)',function (data){

            });
     */
    form.on('submit(saveBtn)',function (data){
        // 所有表单元素的值
        var data = data.field;

        $.ajax({
            type:"post",
            url:ctx+"/user/update",
            data:{
                oldPwd:data.old_password,
                newPwd:data.new_password,
                againPwd:data.again_password
            },
            success:function (result){
                //判断是否修改成功
                if(result.code == 200){
                    layer.msg("修改成功,将在3s后退出",function (){
                        //清空 cookie
                        $.removeCookie("userIdStr",{domain:"localhost",path:"/"})
                        $.removeCookie("userName",{domain:"localhost",path:"/"})
                        $.removeCookie("trueName",{domain:"localhost",path:"/"})
                        // 跳转到登录页面
                        window.parent.location.href = ctx + "/index";
                    })
                }else{
                    layer.msg(result.msg,{icon:5});
                }
            }
        });
    });

})