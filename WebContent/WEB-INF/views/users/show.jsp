<%@ page language="java" contentType="text/html; charset=UTF-8"
  pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<c:import url="/WEB-INF/views/layout/app.jsp">
  <c:param name="content">
    <c:choose>
      <c:when test="${user !=null}">
        <h2>${user.name}さんの投稿一覧</h2>
        <table border="1">
          <tbody>
            <tr>
              <th>アイコン</th>
              <td><img src="/photo_posting_site/uploads/${user.icon}"
                style="width: 30%"></td>
            </tr>
            <tr>
              <th>名前</th>
              <td><c:out value="${user.name}" /></td>
            </tr>
          </tbody>
        </table>
      </c:when>
      <c:otherwise>
        <h2>お探しのデータは見つかりませんでした。</h2>
      </c:otherwise>
    </c:choose>


    <br />
    <c:forEach var="post" items="${post_list}" varStatus="status">
      <table border="1">
        <tbody>
          <tr>
            <th>名前</th>
            <td><c:out value="${post.user.name}" /></td>
          </tr>
          <tr>
            <th>タイトル</th>
            <td><c:out value="${post.title}" /></td>
          </tr>
          <tr>
            <th>内容</th>
            <td><pre><c:out value="${post.content}" /></pre></td>
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
      </table>
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
<a href="<c:url value='/index.html' />">トップへ</a>

  </c:param>

</c:import>
