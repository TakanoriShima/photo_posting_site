package controllers.post;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;

import javax.persistence.EntityManager;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

import models.Post;
import models.User;
import models.validators.PostValidator;
import utils.DBUtil;
import utils.EncryptUtil;

/**
 * Servlet implementation class PostsCreateServlet
 */
@MultipartConfig // 画像アップ機能時は必ず必要
@WebServlet("/posts/create")
public class PostsCreateServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public PostsCreateServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
     *      response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // String型の _tokenにパラメーターの_tokenを代入する
        String _token = (String) request.getParameter("_token");
        // _tokenがnullではなく、且つセッションIDと等しいならば
        if (_token != null && _token.equals(request.getSession().getId())) {
            // DAOインスタンスの生成
            EntityManager em = DBUtil.createEntityManager();

            // 画像アップロード
            Part part = request.getPart("image");
            if (part.getSize() != 0) {
                String filename = getFileName(part);
                String filePath = getServletContext().getRealPath("/uploads/") + filename;
                System.out.println("filePath!!!" + filePath);
                File uploadDir = new File(getServletContext().getRealPath("/uploads/"));
                if (!uploadDir.exists()) {
                    uploadDir.mkdir();
                }
                part.write(filePath);
                try {
                    /* S3 */
                    String region = (String) this.getServletContext().getAttribute("region");
                    String awsAccessKey = (String) this.getServletContext().getAttribute("awsAccessKey");
                    String awsSecretKey = (String) this.getServletContext().getAttribute("awsSecretKey");
                    String bucketName = (String) this.getServletContext().getAttribute("bucketName");
                    // 認証情報を用意
                    AWSCredentials credentials = new BasicAWSCredentials(
                            // アクセスキー
                            awsAccessKey,
                            // シークレットキー
                            awsSecretKey);
                    // クライアントを生成
                    AmazonS3 s3 = AmazonS3ClientBuilder.standard()
                            // 認証情報を設定
                            .withCredentials(new AWSStaticCredentialsProvider(credentials))
                            // リージョンを AP_NORTHEAST_1(東京) に設定
                            .withRegion(region).build();
                    // === ファイルから直接アップロードする場合 ===
                    // アップロードするファイル
                    File file = new File(filePath);
                    // ファイルをアップロード
                    s3.putObject(
                            // アップロード先バケット名
                            bucketName,
                            // アップロード後のキー名
                            "uploads/" + filename,
                            // ファイルの実体
                            file);
                } catch (Exception e) {
                    System.out.println("S3失敗");

                }
                // Postのインスタンスを生成
                Post p = new Post();
                // 変数pに現在ログインしているユーザーの情報をセットする
                p.setUser((User) request.getSession().getAttribute("login_user"));
                // 変数pに入力したタイトルをセットする
                p.setTitle(request.getParameter("title"));
                // 変数pに入力した内容をセットする
                p.setContent(request.getParameter("content"));
                // 変数pに画像名をセットする
                p.setImage(filename);

                // 現在日時を取得して変数pに作成日時にセットする
                Timestamp currentTime = new Timestamp(System.currentTimeMillis());
                p.setCreated_at(currentTime);

                // バリデーター の呼び出し
                List<String> errors = PostValidator.validate(p);
                // errorsリストに1つでも追加されていたら
                if (errors.size() > 0) {
                    // DAOの破棄
                    em.close();
                    // リクエストスコープに各データをセット
                    request.setAttribute("_token", _token);
                    request.setAttribute("post", p);
                    request.setAttribute("errors", errors);
                    // 画面遷移
                    RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/posts/new.jsp");
                    rd.forward(request, response);

                } else {
                    // データベースに保存する
                    em.getTransaction().begin();
                    em.persist(p);
                    em.getTransaction().commit();
                    em.close();
                    // セッションスコープにフラッシュメッセージをセットする
                    request.getSession().setAttribute("flush", "投稿しました。");
                    // 画面遷移
                    response.sendRedirect(request.getContextPath() + "/posts/index");
                }
            } else { // 画像が選択されなかった場合
                // Postのインスタンスを生成
                Post p = new Post();
                // 変数pに現在ログインしているユーザーの情報をセットする
                p.setUser((User) request.getSession().getAttribute("login_user"));
                // 変数pに入力したタイトルをセットする
                p.setTitle(request.getParameter("title"));
                // 変数pに入力した内容をセットする
                p.setContent(request.getParameter("content"));

                // 現在日時を取得して変数pに作成日時にセットする
                Timestamp currentTime = new Timestamp(System.currentTimeMillis());
                p.setCreated_at(currentTime);
                // バリデーター の呼び出し
                List<String> errors = PostValidator.validate(p);
                // errorsリストに1つでも追加されていたら
                if (errors.size() > 0) {
                    // DAOの破棄
                    em.close();
                    // リクエストスコープに各データをセット
                    request.setAttribute("_token", _token);
                    request.setAttribute("post", p);
                    request.setAttribute("errors", errors);
                    // 画面遷移
                    RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/posts/new.jsp");
                    rd.forward(request, response);

                }
            }
        }
    }

    // 拡張子を変えずに、ランダムな名前のファイルを生成する
    private String getFileName(Part part) {
        String[] headerArrays = part.getHeader("Content-Disposition").split(";");
        String fileName = null;
        for (String head : headerArrays) {
            if (head.trim().startsWith("filename")) {
                fileName = head.substring(head.indexOf('"')).replaceAll("\"", "");
            }
        }
        Timestamp currentTime = new Timestamp(System.currentTimeMillis());
        String randName = EncryptUtil.getWordEncrypt(currentTime.toString());
        String extension = fileName.substring(fileName.lastIndexOf("."));
        String rndFileName = randName + extension;
        return rndFileName;
    }
}
