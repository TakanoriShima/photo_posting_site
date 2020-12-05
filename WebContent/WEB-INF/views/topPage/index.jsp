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
    <form action="${pageContext.request.contextPath}/users/new"
      method="GET">
      <button type="submit">新規会員登録</button>
    </form>
    <br />
    <form action="${pageContext.request.contextPath}/login" method="GET">
      <button type="submit">ログイン</button>
    </form>
  </c:param>
</c:import>