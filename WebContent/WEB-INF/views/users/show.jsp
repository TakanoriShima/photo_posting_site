<%@ page language="java" contentType="text/html; charset=UTF-8"
  pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<c:import url="/WEB-INF/views/layout/app.jsp">
  <c:param name="content">
    <c:choose>
      <c:when test="${user !=null}">
        <h2>${user.name}さんの投稿一覧</h2>

        <div class="icon_image">
          <img
            src="https://photo-posting-site.s3.us-east-2.amazonaws.com/uploads/${user.icon}"
            style="width: 30%">
        </div>
      </c:when>
      <c:otherwise>
        <h2>お探しのデータは見つかりませんでした。</h2>
      </c:otherwise>
    </c:choose>


    <br />
    <c:forEach var="post" items="${post_list}" varStatus="status">
      <div class="box2">

        <span class="user_name"> <c:out value="${post.user.name}" /></span>
        <span> <fmt:formatDate value="${post.created_at}"
            pattern="yyyy-MM-dd HH:mm:ss" /></span>

        <div>
          <c:out value="${post.title}" />
        </div>

        <div>
          <pre><c:out value="${post.content}" /> </pre>
        </div>

        <div class="post_image">
          <img
            src="https://photo-posting-site.s3.us-east-2.amazonaws.com/uploads/${post.image}"
            style="width: 60%">
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
    <br />
    <a href="<c:url value='/posts/index' />">トップへ</a>

  </c:param>

</c:import>
