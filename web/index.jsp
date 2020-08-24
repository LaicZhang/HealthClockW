<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<html>
<head>
    <title>超星学习通自动健康打卡系统</title>
    <script src="https://how2j.cn/study/js/jquery/2.0.0/jquery.min.js"></script>
    <link href="https://how2j.cn/study/css/bootstrap/3.3.6/bootstrap.min.css" rel="stylesheet">
    <script src="https://how2j.cn/study/js/bootstrap/3.3.6/bootstrap.min.js"></script>
    <style>
        td{
            width: 300px;
            border-bottom: 5px solid rgb(255, 255, 255);
        }
    </style>
</head>
<body>

    <br/>
    <div id="con" style="text-align: center;">
        <form id="f" action="add" method="post">
            <table id="table" style="margin: auto;padding: 1.8%;">
                <thead>
                    <div>攀大超星学习通自动打卡</div>
                </thead>
                <tr>
                    <td>
                        <input class="form-control" type="text" id="uname" name="uname" required="required" placeholder="手机号"/>
                    </td>
                </tr>

                <tr>
                    <td>
                        <input class="form-control" type="password" id="password" required="required" name="password" placeholder="密码"/>
                    </td>
                </tr>

                <tr>
                    <td>
                        <input class="form-control" type="text" id="clazz" name="class" placeholder="年级、专业、班级"/>
                    </td>
                </tr>
            <tr>
                <td>
                    <input class="form-control" type="text" id="locations" name="location" placeholder="当前所在省、市、县（区）"/>
                </td>
            </tr>
            <tr>
                <td>
                    <p>
                        <select class="form-control" name="college" size="1">
                            <option value="钒钛学院">钒钛学院</option>
                            <option value="经济与管理学院">经济与管理学院</option>
                            <option value="交通与汽车工程学院">交通与汽车工程学院</option>
                            <option value="康养学院">康养学院</option>
                            <option value="人文社科学院">人文社科学院</option>
                            <option value="生物与化学工程学院">生物与化学工程学院</option>
                            <option selected="selected" value="数学与计算机学院">数学与计算机学院</option>
                            <option value="土木与建筑工程学院">土木与建筑工程学院</option>
                            <option value="外国语学院">外国语学院</option>
                            <option value="艺术学院">艺术学院</option>
                            <option value="医学院">医学院</option>
                            <option value="智能制造学院">智能制造学院</option>
                            <option value="研究生处">研究生处</option>
                        </select>
                    </p>
                </td>
            </tr>
            <tr>
                <td>
                    <input class="btn btn-success" type="button" onclick="check()" value="启动自动打卡"/>
                </td>
            </tr>
            </table>
        </form>
    </div>
    <textarea rows="10" class="form-control">
        使用说明:
        0、GitHub地址：https://github.com/LaicZhang/HealthClockW
        1、此应用不作任何非法行为，仅用于测试
        2、输入手机号以及学习通密码，单击启动自动打卡即可完成在服务器登记
        3、学习通修改密码后需要重新在本页面进行登记
        4、进行登记后,服务器会每天开始给登记的学习通账号自动打卡
        5、年级、专业、班级以及当前所在省、市、县（区）可不填,不填则默认选择学习通上一天打卡记录中的值
    </textarea>
    <script type="text/javascript">
        function check() {
            if (document.getElementById("uname").value.trim() === '' ||
                document.getElementById("password").value.trim() === '') {
                alert("手机号、学习通密码不可为空!!");
            } else {
                document.getElementById("f").submit();
            }
        }
    </script>
</body>
</html>
