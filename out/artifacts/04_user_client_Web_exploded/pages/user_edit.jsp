<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title>编辑图书</title>
		<%
			String path = request.getScheme() + "://"
					+ request.getServerName() + ":" + request.getServerPort()
					+ request.getContextPath() + "/";
			pageContext.setAttribute("APP_PATH",path);
		%>
		<link type="text/css" rel="stylesheet" href="${APP_PATH}static/css/style.css" >
		<script type="text/javascript" src="${APP_PATH}static/script/jquery-1.12.4.min.js"></script>
		<style type="text/css">
		h1 {
			text-align: center;
			margin-top: 200px;
		}

		h1 a {
			color:red;
		}

		input {
			text-align: center;
		}
		</style>
		
		<script type="text/javascript">
			$(function () {
				$("#updateUserBtn").click(function () {

					//不要加斜杠，jsp页面中“/”代表http:localhost:8080 会导致post请求出错
					/*$.post("/users", {"action":$("#action").val()}, function (result) {*/
					//正确方式
					/*$.post("users", {"action":$("#action").val()}, function (result) {*/
					//最好的处理方式
					var url = $(this).attr("url");
                    var params = {
                    	"action" : $("#action").val(),
						"id" : $("#userId").val(),
						"pageNum" : $("#pageNum").val(),
						"name" : $("#username").val(),
						"age" : $("#userAge").val(),
						"email" : $("#userEmail").val(),
						"departmentName" : $("#userDepartmentName").val()
					};
					$.post(url,params, function (result) {
						var parse = JSON.parse(result);
						//alert(parse.indexUrl);
						if (200 === parse.responseCode) {
							//跳转到修改完成后用户所在的页
							location.href = parse.indexUrl;
						} else {
							alert("处理失败");

						}
					});

				})
			});
		</script>

	</head>
	<body>
			<div id="main">
					<input type="hidden" name="action" id="action" value="updateUser" />
					<%--<input type="hidden" name="id" id="userId" value="${requestScope.user.id}" />--%>
					<input type="hidden" name="id" id="userId" value="${sessionScope.user.id}" />
					<%--<input type="hidden" name="pageNum" id="pageNum" value="${requestScope.pageNum}">--%>
					<input type="hidden" name="pageNum" id="pageNum" value="${sessionScope.pageNum}">
					<table>
						<tr>
							<td>姓名</td>
							<td>年龄</td>
							<td>邮箱</td>
							<td>部门</td>
							<td colspan="2">操作</td>
						</tr>
						<tr>
							<%--
							<td><input name="name" id="username" type="text" value="${requestScope.user.name}"/></td>
							<td><input name="age" id="userAge" type="text" value="${requestScope.user.age}"/></td>
							<td><input name="email" id="userEmail" type="text" value="${requestScope.user.email}"/></td>
							<td><input name="departmentName" id="userDepartmentName" type="text" value="${requestScope.user.departmentName}"/></td>
							--%>
							<td><input name="name" id="username" type="text" value="${sessionScope.user.name}"/></td>
							<td><input name="age" id="userAge" type="text" value="${sessionScope.user.age}"/></td>
							<td><input name="email" id="userEmail" type="text" value="${sessionScope.user.email}"/></td>
							<td><input name="departmentName" id="userDepartmentName" type="text" value="${sessionScope.user.departmentName}"/></td>
							<td>
								<button url="${APP_PATH}users" id="updateUserBtn">
									提交
								</button>
							</td>
						</tr>
					</table>
			</div>
	</body>
</html>