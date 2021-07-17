<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>上饶师范学院OA管理系统</title>
    <link href="/oa/resources/layui/css/layui.css" rel="stylesheet">
    <script src="/oa/resources/layui/layui.js"></script>
    <style>
        #login {
            width: 800px;
            height: 800px;
            position: absolute;
            top: 50%;
            left: 50%;
            background-color: #cccccc;
            margin-top: -400px;
            margin-left: -400px;
        }

        .inputLogin {
            width: 450px;
        }

        .formLogin {
            width: 600px;
            height: 600px;
            position: absolute;
            left: 50%;
            margin-top: 60px;
            margin-left: -300px;
        }

        .layui-carousel img {
            width: 800px;
            height: 280px;
        }

        p {
            text-align: center;
            font-size: 30px;
        }
    </style>
</head>
<body>
<div id="login">
    <div class="layui-carousel" id="test1">
        <div carousel-item>
            <div><img src="http://www.sru.edu.cn/_mediafile/srsf/files/20171109100642203001.jpg" alt=""></div>
            <div><img src="http://www.sru.edu.cn/_mediafile/srsf/files/20170907161743457_s.jpg" alt=""></div>
            <div><img src="http://www.sru.edu.cn/_mediafile/srsf/files/20161122173803652003_s.jpg" alt=""></div>
            <div><img src="http://www.sru.edu.cn/_mediafile/srsf/files/20161122173802252001_s.jpg" alt=""></div>
            <div><img src="http://www.sru.edu.cn/_mediafile/srsf/files/20170331084355816_s.jpg" alt=""></div>
        </div>
    </div>

    <div class="formLogin">
        <p>校园OA请假管理系统</p>
        <form class="layui-form">
            <div class="layui-form-item">
                <label class="layui-form-label">用户名</label>
                <div class="layui-input-block inputLogin">
                    <input type="text" name="username" required lay-verify="required" placeholder="请输入用户名"
                           autocomplete="off" class="layui-input">
                </div>
            </div>

            <div class="layui-form-item">
                <label class="layui-form-label">密码</label>
                <div class="layui-input-block inputLogin">
                    <input type="password" name="password" required lay-verify="required" placeholder="请输入密码"
                           autocomplete="off" class="layui-input">
                </div>
            </div>

            <div class="layui-form-item">
                <label class="layui-form-label">学院</label>
                <div class="layui-input-block inputLogin">
                    <select name="departmentId" lay-verify="required">
                        <option value=""></option>
                        <#list departments as department>
                            <option value="${department_index+1}">${department.departmentName}</option>
                        </#list>
                    </select>
                </div>
            </div>

            <div class="layui-form-item">
                <label class="layui-form-label">专业</label>
                <div class="layui-input-block inputLogin">
                    <select name="majorId" lay-verify="required">
                        <option value=""></option>
                        <#list majors as major >
                            <option value="${major_index+1}">${major.majorName}</option>
                        </#list>
                    </select>
                </div>
            </div>

            <div class="layui-form-item">
                <label class="layui-form-label">类别</label>
                <div class="layui-input-block inputLogin">
                    <input type="radio" name="sex" value="学生" title="学生" checked>
                    <input type="radio" name="sex" value="辅导员" title="辅导员">
                    <input type="radio" name="sex" value="领导" title="领导">

                </div>
            </div>

            <div class="layui-form-item">
                <div class="layui-input-block">
                    <button class="layui-btn" lay-submit lay-filter="formLogin">立即提交</button>
                    <button type="reset" class="layui-btn layui-btn-primary">重置</button>
                </div>
            </div>
        </form>

    </div>
</div>
</body>
<script src="/oa/resources/layui/layui.all.js"></script>
<!-- 条目中可以是任意内容，如：<img src=""> -->
<script type="text/javascript">
    layui.use('carousel', function () {
        var carousel = layui.carousel;
        //建造实例
        carousel.render({
            elem: '#test1'
            , width: '100%' //设置容器宽度
            , arrow: 'always' //始终显示箭头
            //,anim: 'updown' //切换动画方式
        });
    });
</script>
<script type="text/javascript">
    layui.use('form', function () {
        var form = layui.form;
        var $ = layui.jquery;
        //监听提交
        form.on('submit(formLogin)', function (data) {
            $.ajax({
                url: "/oa/checkLogin",
                type: "post",
                /*传入前端的数据*/
                data: data.field,
                dataType: "json",
                success: function (json) {
                    if (json.code === 1000) {
                        window.location.href = "/oa"
                    } else {
                        layui.layer.msg(json.msg);
                    }
                }
            });
            return false;
        });
    });
</script>
</html>