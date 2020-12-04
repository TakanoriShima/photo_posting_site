<%@ page language="java" contentType="text/html; charset=UTF-8"
  pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:import url="../layout/app.jsp">
  <c:param name="content">
    <h2>新規会員登録</h2>

    <form method="POST" action="<c:url value='/users/create' />" enctype="multipart/form-data">
      <c:import url="_form.jsp" />
    </form>

    <a href="<c:url value='/top' />">戻る</a>
  </c:param>
</c:import>