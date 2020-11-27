<%@ page language="java" contentType="text/html; charset=UTF-8"
  pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<c:if test="${errors!=null}">
  <div id=flush_error>
    入力内容にエラーがあります。<br />
    <c:forEach var="error" items="${errors}">・<c:out
        value="${error}" />
      <br />
    </c:forEach>
  </div>
</c:if>

<label for="name">名前</label>
<br />
<input type="text" name="name" value="${user.name}" />
<br />
<br />

<label for="email">メールアドレス</label>
<br />
<input type="email" name="email" value="${user.mail_address}" />
<br />
<br />

<label for="password">パスワード</label>
<br />
<input type="password" name="password" value="${user.password}" />
<br />
<br />

<label for="image">アイコン画像</label>
<br />
<input type="file" name="image" value="${user.icon}" />
<br />
<br />

<input type="hidden" name="_token" value="${_token}" />
<button type="submit">登録</button>