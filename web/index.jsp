<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<html>
<head>
    <title>超星学习通自动健康打卡系统</title>
</head>
<body>
<div>超星学习通自动健康打卡系统</div>
<br/>
<div>
    <form id="f" action="add" method="post">
        <input type="text" id="uname" name="uname" required="required" placeholder="手机号"/>
        <input type="password" id="password" required="required" name="password" placeholder="密码"/>
        <br/>
        <input type="text" id="clazz" name="class" placeholder="年级、专业、班级"/>
        <input type="text" id="locations" name="location" placeholder="当前所在省、市、县（区）"/>
        <br/>
        <p>
            <select name="college" size="1">
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
        <p>
            <input type="button" onclick="check()" value="启动自动打卡"/>
        </p>
    </form>
</div>
<p>
    <h4>使用说明:</h4>
    <h5>1、此应用不作任何非法行为，仅用于测试</h5>
    <h5>2、输入手机号以及学习通密码，单击启动自动打卡即可完成在服务器登记</h5>
    <h5>3、学习通<span style="color: red">修改密码后需要重新在本页面进行登记</span></h5>
    <h5>4、进行登记后,<span style="color: red">服务器会每天开始给登记的学习通账号自动打卡</span></h5>
    <h5>5、<span style="color: red">年级、专业、班级以及当前所在省、市、县（区）可不填</span>,不填则默认选择学习通上一天打卡记录中的值</h5>
</p>
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
