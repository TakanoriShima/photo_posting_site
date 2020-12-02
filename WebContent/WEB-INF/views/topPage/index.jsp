<%@ page language="java" contentType="text/html; charset=UTF-8"
  pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<c:import url="../layout/app.jsp">
  <c:param name="content">
    <c:if test="${flush!=null}">
      <div id="flush_success">
        <c:out value="${flush}"></c:out>
      </div>
    </c:if>
    <h2>写真投稿サイトへようこそ</h2>
    <form action="${pageContext.request.contextPath}/posts/new"
      method="GET">
      <button type="submit">投稿する</button>
    </form>
    <br />

    <c:forEach var="post" items="${posts}" varStatus="status">
      <table border="1">
        <tbody>
          <tr>
            <th>id</th>
            <td><a href="<c:url value='/posts/show?id=${post.id}' />"><c:out
                  value="${post.id}" /></a></td>
          </tr>
          <tr>
            <th>アイコン</th>
            <td><img src="/photo_posting_site/uploads/${post.user.icon}"
              style="width: 30%"></td>
          </tr>
          <tr>
            <th>名前</th>
            <td><a href="<c:url value='/users/show?id=${post.user.id}' />"><c:out value="${post.user.name}" /></a></td>
          </tr>
          <tr>
            <th>タイトル</th>
            <td><c:out value="${post.title}" /></td>
          </tr>
          <tr>
            <th>内容</th>
            <td><pre><c:out value="${post.content}" /> </pre></td>
          </tr>
          <tr>
            <th>画像</th>
            <td><img src="/photo_posting_site/uploads/${post.image}"
              style="width: 30%"></td>
          </tr>
          <tr>
            <th>投稿日時</th>
            <td><fmt:formatDate value="${post.created_at}"
                pattern="yyyy-MM-dd HH:mm:ss" /></td>
          </tr>

        </tbody>
      </table>
    </c:forEach>


  </c:param>


</c:import>