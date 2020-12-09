<%@ page language="java" contentType="text/html; charset=UTF-8"
  pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="ja">
<head>
<meta charset="UTF-8">
<title>写真投稿サイト</title>
<link rel="stylesheet" href="<c:url value='/css/reset.css' />">
<link rel="stylesheet" href="<c:url value='/css/style.css' />">
<link rel="shortcut icon"
  href="https://photo-posting-site.s3.us-east-2.amazonaws.com/favicon+(1).ico">


</head>
<body>
  <div id="wrapper">
    <div id="header">
      <h1>写真投稿サイト</h1>
    </div>
    <c:if test="${sessionScope.login_user != null}">
      <div id="user_name">
        <c:out value="${sessionScope.login_user.name}" />
        &nbsp;さん&nbsp;&nbsp;&nbsp; <a href="<c:url value='/logout' />">ログアウト</a>
      </div>
    </c:if>
    <div id="content">${param.content}</div>
    <div id="footer">by Haruka Kawabe.</div>
  </div>
  <!-- プレビューの実装 -->
  <!-- jQuery first, then Popper.js, then Bootstrap JS, then Font Awesome -->
  <script src="https://code.jquery.com/jquery-3.3.1.slim.min.js"></script>
  <script
    src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.6/umd/popper.min.js"></script>
  <script
    src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js"></script>
  <script defer
    src="https://use.fontawesome.com/releases/v5.7.2/js/all.js"></script>
  <script>
    $(function() {
      var file = null; // 選択されるファイル
      var blob = null; // 画像(BLOBデータ)
      const THUMBNAIL_WIDTH = 300; // 画像リサイズ後の横の長さの最大値
      const THUMBNAIL_HEIGHT = 300; // 画像リサイズ後の縦の長さの最大値
      // ファイルが選択されたら
      $('input[type=file]').change(
          function() {
            // ファイルを取得
            file = $(this).prop('files')[0];
            // 選択されたファイルが画像かどうか判定
            if (file.type != 'image/jpeg'
                && file.type != 'image/png') {
              // 画像でない場合は終了
              file = null;
              blob = null;
              return;
            }
            // 画像をリサイズする
            var image = new Image();
            var reader = new FileReader();
            reader.onload = function(e) {
              image.onload = function() {
                var width, height;
                if (image.width > image.height) {
                  // 横長の画像は横のサイズを指定値にあわせる
                  var ratio = image.height / image.width;
                  width = THUMBNAIL_WIDTH;
                  height = THUMBNAIL_WIDTH * ratio;
                } else {
                  // 縦長の画像は縦のサイズを指定値にあわせる
                  var ratio = image.width / image.height;
                  width = THUMBNAIL_HEIGHT * ratio;
                  height = THUMBNAIL_HEIGHT;
                }
                // サムネ描画用canvasのサイズを上で算出した値に変更
                var canvas = $('#canvas').attr('width', width)
                    .attr('height', height);
                var ctx = canvas[0].getContext('2d');
                // canvasに既に描画されている画像をクリア
                ctx.clearRect(0, 0, width, height);
                // canvasにサムネイルを描画
                ctx.drawImage(image, 0, 0, image.width,
                    image.height, 0, 0, width, height);
                // canvasからbase64画像データを取得
                var base64 = canvas.get(0).toDataURL(
                    'image/jpeg');
                // base64からBlobデータを作成
                var barr, bin, i, len;
                bin = atob(base64.split('base64,')[1]);
                len = bin.length;
                barr = new Uint8Array(len);
                i = 0;
                while (i < len) {
                  barr[i] = bin.charCodeAt(i);
                  i++;
                }
                blob = new Blob([ barr ], {
                  type : 'image/jpeg'
                });
                console.log(blob);
              }
              image.src = e.target.result;
            }
            reader.readAsDataURL(file);
          });
    });
  </script>
</body>
</html>
