<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2021/1/27
  Time: 16:18
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%--分页条的开始--%>
<div id="page_nav" class="col-md-offset-3">
    <%--如果不是首页，则显示首页和上一页--%>
    <c:if test="${requestScope.page.pageNum > 1}">
        <a href="${APP_PATH}users?action=listsUser&pageNum=1">首页</a>
        <a href="${APP_PATH}users?action=listsUser&pageNum=${requestScope.page.pageNum-1}">上一页</a>
    </c:if>

    <%--页码输出的开始--%>
    <c:choose>
        <%--第一种情况：如果总页码小于等于5的情况，页码的范围是1到总页码--%>
        <c:when test="${requestScope.page.pages <= 5}">
            <c:set var="begin" value="1"/>
            <c:set var="end" value="${requestScope.page.pages}"/>
        </c:when>

        <%--第二种情况：总页码大于5.假设一共有十页--%>
        <c:when test="${requestScope.page.pages > 5}">
            <c:choose>
                <%--小情况：当前页码为前面3个：1,2,3的情况，页码的范围是：1到5--%>
                <c:when test="${requestScope.page.pageNum <= 3}">
                    <c:set var="begin" value="1"/>
                    <c:set var="end" value="5"/>
                </c:when>
                <%--小情况2：当前页码为最后3个，8,9,10,页码的范围是：总页码减4到总页码--%>
                <c:when test="${requestScope.page.pageNum > requestScope.page.pages-3}">
                    <c:set var="begin" value="${requestScope.page.pages-4}"/>
                    <c:set var="end" value="${requestScope.page.pages}"/>
                </c:when>
                <%--小情况3:4,5,6,7 页码范围是当前页码减2到当前页码加2--%>
                <c:otherwise>
                    <c:set var="begin" value="${requestScope.page.pageNum-2}"/>
                    <c:set var="end" value="${requestScope.page.pageNum+2}"/>
                </c:otherwise>
            </c:choose>
        </c:when>
    </c:choose>

    <c:forEach begin="${begin}" end="${end}" var="i">
        <%--当前页不跳转--%>
        <c:if test="${i==requestScope.page.pageNum}">
            【${i}】
        </c:if>
        <%--其他页跳转--%>
        <c:if test="${i!=requestScope.page.pageNum}">
            <a href="${APP_PATH}users?action=listsUser&pageNum=${i}">${i}</a>
        </c:if>
    </c:forEach>

    <%--页码输出的结束--%>

    <%--如果不是末页，则显示末页也下一页--%>
    <c:if test="${requestScope.page.pageNum < requestScope.page.pages}">
        <a href="${APP_PATH}users?action=listsUser&pageNum=${requestScope.page.pageNum + 1}">下一页</a>
        <a href="${APP_PATH}users?action=listsUser&pageNum=${requestScope.page.pages}">末页</a>
    </c:if>
    共${requestScope.page.pages}页，${requestScope.page.total}条记录
    到第<input name="pn" id="pn_input"/>页

    <input type="button" value="确定" id="searchPageBtn">
    <script type="text/javascript">
        $(function () {
            //跳到指定的页码
            $("#searchPageBtn").click(function () {
                var pageNum = $("#pn_input").val();

                //这样只是避免在点确定按钮的时候，页数有效，
                //但是如果在浏览器地址栏上输入的是非法的页面，还是有bug的
                //更加严谨应该在后端也要进行页码的校验
                if (pageNum < 1) {
                    pageNum = 1;
                }

                if (pageNum > ${requestScope.page.pages}) {
                    pageNum = ${requestScope.page.pages};
                }

                location.href = "${APP_PATH}users?action=listsUser&pageNum="+pageNum;
            })
        })
    </script>
    <%--分页条的结束--%>
</div>
