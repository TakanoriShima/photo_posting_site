package controllers.users;

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

import models.User;
import models.validators.UserValidator;
import utils.DBUtil;
import utils.EncryptUtil;

/**
 * Servlet implementation class UsersCreateServlet
 */
@MultipartConfig // 画像アップ機能時は必ず必要
@WebServlet("/users/create")
public class UsersCreateServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public UsersCreateServlet() {
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
                // Userインスタンスの生成
                User u = new User();
                // 変数uに入力された名前をセットする
                u.setName(request.getParameter("name"));
                // 変数uに入力されたアドレスをセットする
                u.setMail_address(request.getParameter("mail_address"));
                // 変数uに暗号化されたパスワードをセットする
                u.setPassword(EncryptUtil.getPasswordEncrypt(request.getParameter("password"),
                        (String) this.getServletContext().getAttribute("pepper")));
                // 変数uに入力されたアイコン画像名をセットする
                u.setIcon(filename);

                // バリデーター の呼び出し
                List<String> errors = UserValidator.validate(u, true, true);
                // errorsリストに1つでも追加されていたら
                if (errors.size() > 0) {
                    // 変数uに暗号化されていないパスワードをセットする
                    u.setPassword(request.getParameter("password"));
                    // DAOの破棄
                    em.close();

                    // リクエストスコープに各データをセット
                    request.setAttribute("_token", request.getSession().getId());
                    request.setAttribute("user", u);
                    request.setAttribute("errors", errors);

                    // 画面遷移
                    RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/users/new.jsp");
                    rd.forward(request, response);
                } else {
                    // データベースに保存
                    em.getTransaction().begin();
                    em.persist(u);
                    em.getTransaction().commit();
                    // DAOの破棄
                    em.close();
                    // セッションスコープにフラッシュメッセージをセットする
                    request.getSession().setAttribute("flush", "登録が完了しました。");
                    // セッションスコープにをlogin_userセットする
                    request.getSession().setAttribute("login_user", u);
                    // 画面遷移
                    response.sendRedirect(request.getContextPath() + "/posts/index");

                }
            } else { // アイコン画像が選択されなかった場合
                // Userインスタンスの生成
                User u = new User();
                // 変数uに入力された名前をセットする
                u.setName(request.getParameter("name"));
                // 変数uに入力されたアドレスをセットする
                u.setMail_address(request.getParameter("mail_address"));
                // 変数uに暗号化されたパスワードをセットする
                u.setPassword(EncryptUtil.getPasswordEncrypt(request.getParameter("password"),
                        (String) this.getServletContext().getAttribute("pepper")));

                // バリデーターの呼び出し
                List<String> errors = UserValidator.validate(u, true, true);
                // errorsリストに1つでも追加されていたら
                if (errors.size() > 0) {
                    // 変数uに暗号化されていないパスワードをセットする
                    u.setPassword(request.getParameter("password"));
                    // DAOの破棄
                    em.close();

                    // セッションスコープに各データをセットする
                    request.setAttribute("_token", request.getSession().getId());
                    request.setAttribute("user", u);
                    request.setAttribute("errors", errors);

                    // 画面遷移
                    RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/users/new.jsp");
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
