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

        <div class="box2">
          <div>
            id
            <c:out value="${post.id}" />
          </div>

          <span class="icon_image"><img
            src="https://photo-posting-site.s3.us-east-2.amazonaws.com/uploads/${post.user.icon}"
            style="width: 20%" alt="アイコン画像"></span> <span class="user_name">
            <c:out value="${post.user.name}" />
          </span> <span><fmt:formatDate value="${post.created_at}"
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
        <table>
          <tbody>
            <tr>
              <th><c:choose>
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
                </c:choose></th>
              <td><c:out value="${favorites_count}" />人<br /> <c:forEach
                  var="user" items="${favorited_user_list}">
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
    <c:if test="${errors!=null}">
      <div id=flush_error>
        入力内容にエラーがあります。<br />
        <c:forEach var="error" items="${errors}">・<c:out
            value="${error}" />
          <br />
        </c:forEach>
      </div>
    </c:if>
    <c:forEach var="comment" items="${comment_list}">
        <div class="box2">
      <p>
        <c:out value="${comment.user.name}"></c:out>
        <fmt:formatDate value="${comment.created_at}"
              pattern="yyyy-MM-dd HH:mm:ss" />
        <c:out value="${comment.content}"></c:out>
      </p>
      </div>
    </c:forEach>

    <form method="POST" action="<c:url value='/comments/create' />">
      <input type="hidden" name="_token" value="${_token}" /> <input
        type="hidden" name="post_id" value="${post.id}">
      <p>この投稿にコメントする</p>
      <label for="content"></label>
      <textarea name="content" rows="10" cols="50">${report.content}</textarea>
      <button type="submit">コメント投稿</button>
    </form>
    <br />
    <a href="<c:url value='/index.html' />">トップへ</a>
  </c:param>
</c:import>
