<%@ page language="java" contentType="text/html; charset=UTF-8"
  pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>
<html lang="ja">
<head>
<meta charset="UTF-8">
<title>写真投稿サイト</title>
</head>
<body>
  <div id="wrapper">
    <div id="header">
      <h1>写真投稿サイト</h1>
    </div>
    <c:if test="${sessionScope.login_user != null}">
                    <div id="user_name">
                        <c:out value="${sessionScope.login_user.name}" />&nbsp;さん&nbsp;&nbsp;&nbsp;
                        <a href="<c:url value='/logout' />">ログアウト</a>
                    </div>
                </c:if>
    <div id="content">${param.content}</div>
    <div id="footer">by Haruka Kawabe.</div>
  </div>
</body>
</html>
