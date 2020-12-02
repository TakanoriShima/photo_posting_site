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
    <c:choose>
      <c:when test="${post != null}">
        <h2>投稿詳細ページ</h2>

        <table border="1">
          <tbody>
            <tr>
              <th>id</th>
              <td><c:out value="${post.id}" /></td>
            </tr>
            <tr>
              <th>アイコン</th>
              <td><img src="/photo_posting_site/uploads/${post.user.icon}"
                style="width: 30%"></td>
            </tr>
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
            <tr>
              <th>
                <c:choose>
                    <c:when test="${count==0}">

                <form
                  action="${pageContext.request.contextPath}/favorites/create"
                  method="POST">
                  <input type="hidden" name="post_id"
                    value="<c:out value='${post.id}'/>"> <input
                    type="hidden" name="_token" value="${_token}" />
                  <button type="submit">いいね!</button>
                </form>&nbsp;
                </c:when>
                <c:otherwise>
                <form
                  action="${pageContext.request.contextPath}/favorites/destroy"
                  method="POST">
                  <input type="hidden" name="post_id"
                    value="<c:out value='${post.id}'/>"> <input
                    type="hidden" name="_token" value="${_token}" />
                  <button type="submit">いいね解除</button>
                </form>&nbsp;
                </c:otherwise>
                </c:choose>
              </th>
              <td><c:out value="${favorites_count}" />人<br />
              <c:forEach var="user" items="${favorited_user_list}">
                <c:out value="${user.name}" />
              </c:forEach>
            </tr>
          </tbody>
        </table>
      </c:when>
      <c:otherwise>
        <h2>お探しのデータは見つかりませんでした。</h2>
      </c:otherwise>
    </c:choose>
    <br />
    <c:forEach var="comment" items="${comment_list}">
        <p><c:out value="${comment.user.name}"></c:out>　<c:out value="${comment.content}"></c:out>　<c:out value="${comment.created_at}"></c:out></p>
    </c:forEach>
    <form method="POST" action="<c:url value='/comments/create' />">
      <input type="hidden" name="_token" value="${_token}" />
      <input type="hidden" name="post_id" value="${post.id}">
      <p>この投稿にコメントする</p>
      <label for="content"></label>
      <textarea name="content" rows="10" cols="50">${report.content}</textarea>
      <button type="submit">コメント投稿</button>
    </form>
    <br />
<a href="<c:url value='/index.html' />">トップへ</a>
  </c:param>
</c:import>
