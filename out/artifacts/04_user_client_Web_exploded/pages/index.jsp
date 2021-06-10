<%--
  Created by IntelliJ IDEA.
  User: Lu
  Date: 2021/6/7
  Time: 14:57
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
  <head>
    <title>$用户列表$</title>

    <%
        String path = request.getScheme() + "://"
                + request.getServerName() + ":" + request.getServerPort()
                + request.getContextPath() + "/";
        pageContext.setAttribute("APP_PATH",path);
    %>

    <link href="${APP_PATH}static/bootstrap-3.3.7-dist/css/bootstrap.min.css" rel="stylesheet">
    <script type="text/javascript" src="${APP_PATH}static/script/jquery-1.12.4.min.js"></script>
    <script src="${APP_PATH}static/bootstrap-3.3.7-dist/js/bootstrap.min.js"></script>

  </head>
  <body>
        <div id="main">
            <table class="table table-striped">
                <tr>
                  <td>编号</td>
                  <td>姓名</td>
                  <td>年龄</td>
                  <td>邮箱</td>
                  <td>部门</td>
                  <td>操作</td>
                </tr>

                <%--遍历输出当前页的数据--%>
                <c:forEach items="${requestScope.page.list}" var="user">
                  <tr>
                    <td>${user.id}</td>
                    <td>${user.name}</td>
                    <td>${user.age}</td>
                    <td>${user.email}</td>
                    <td>${user.departmentName}</td>
                    <td>
                        <button url="${APP_PATH}users?action=getUser&id=${user.id}&name=${user.name}&pageNum=${requestScope.page.pageNum}"
                                type="button" class="btn btn-primary btn-sm edit_btn upDateBtn">
                            <span class="glyphicon glyphicon-pencil" aria-hidden="true"></span>
                            编辑
                        </button>
                    </td>
                  </tr>
                </c:forEach>

            </table>
        </div>

        <%--静态包含分页条--%>
        <%@include file="/pages/page_nav.jsp"%>

        <%--跳转到修改的页面--%>
        <script type="text/javascript">
            $(function () {
                $(".upDateBtn").click(function () {
                    var url = $(this).attr("url");
                    let splitUrl = url.split("?");
                    let  getUserUrl = splitUrl[0];
                    //alert(getUserUrl);
                    let params = splitUrl[1];
                    //alert(params);
                    $.post(getUserUrl, params, function (data) {
                         let result = JSON.parse(data);
                         if (200 === result.responseCode) {
                             location.href = "pages/user_edit.jsp";
                         }
                    });
                })
            })
        </script>

  </body>
</html>
