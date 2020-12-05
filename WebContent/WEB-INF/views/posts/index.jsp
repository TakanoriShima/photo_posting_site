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

      <div class="box2">
        <div>
          id. <a href="<c:url value='/posts/show?id=${post.id}' />"><c:out
              value="${post.id}" /></a>
        </div>

        <span class="icon_image"> <img
          src="https://photo-posting-site.s3.us-east-2.amazonaws.com/uploads/${post.user.icon}"
          style="width: 20%" alt="アイコン画像">
        </span> <span class="user_name"> <a
          href="<c:url value='/users/show?id=${post.user.id}' />"><c:out
              value="${post.user.name}" /></a>
        </span> <span> <fmt:formatDate value="${post.created_at}"
            pattern="yyyy-MM-dd HH:mm:ss" />
        </span>
        <div>
          <c:out value="${post.title}" />
        </div>
        <div><pre><c:out value="${post.content}" /> </pre></div>


        <div class="post_image">
          <img
            src="https://photo-posting-site.s3.us-east-2.amazonaws.com/uploads/${post.image}"
            style="width: 60%" alt="投稿画像">
        </div>
      </div>

    </c:forEach>
    <div id="pagination">
        （全 ${post_count} 件）<br />
        <c:forEach var="i" begin="1" end="${((post_count - 1) / 15) + 1}"
          step="1">
          <c:choose>
            <c:when test="${i == page}">
              <c:out value="${i}" />&nbsp;
                    </c:when>
            <c:otherwise>
              <a href="<c:url value='/?page=${i}' />"><c:out value="${i}" /></a>&nbsp;
                    </c:otherwise>
          </c:choose>
        </c:forEach>
      </div>
  </c:param>
</c:import>