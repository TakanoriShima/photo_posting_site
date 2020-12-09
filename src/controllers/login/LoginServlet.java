package controllers.login;

import java.io.IOException;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import models.User;
import utils.DBUtil;
import utils.EncryptUtil;

/**
 * Servlet implementation class LoginServlet
 */
@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public LoginServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
     *      response)
     */
    // ログイン画面を表示
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // CSRF対策
        request.setAttribute("_token", request.getSession().getId());
        // リクエストスコープにfalseをhasErrorとしてセットする
        request.setAttribute("hasError", false);
        // セッションスコープにフラッシュメッセージがあるならば
        if (request.getSession().getAttribute("flush") != null) {
            // リクエストスコープにフラッシュメッセージをセットする
            request.setAttribute("flush", request.getSession().getAttribute("flush"));
            // セッションスコープのフラッシュメッセージを削除する
            request.getSession().removeAttribute("flush");
            // セッションスコープのlogin_userも削除する
            request.getSession().removeAttribute("login_user");
        }
        // 画面遷移
        RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/login/login.jsp");
        rd.forward(request, response);
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
     *      response)
     */
    // ログイン処理を実行
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // 認証結果を格納する変数
        Boolean check_result = false;
        // String型のmail_addressにパラメーターのmail_addressを代入
        String mail_address = request.getParameter("mail_address");
        // String型のplain_passにパラメーターのplain_passを代入
        String plain_pass = request.getParameter("password");

        // Userの初期値をnullにする
        User u = null;
        // mail_addressとplain_passが入力されていたら
        if (mail_address != null && !mail_address.equals("") && plain_pass != null && !plain_pass.equals("")) {
            // DAOインスタンスの生成
            EntityManager em = DBUtil.createEntityManager();

            // passwordの暗号化
            String password = EncryptUtil.getPasswordEncrypt(plain_pass,
                    (String) this.getServletContext().getAttribute("pepper"));

            // メールアドレスとパスワードが正しいかチェックする
            try {
                u = em.createNamedQuery("checkLoginMailAddressAndPassword", User.class)
                        .setParameter("mail_address", mail_address).setParameter("pass", password).getSingleResult();
            } catch (NoResultException ex) {
            }
            // DAOの破棄
            em.close();

            //変数uがnullでないならば
            if (u != null) {
                // 認証結果を格納する変数はtrue
                check_result = true;
            }
        }

        if (!check_result) {
            // 認証できなかったらログイン画面に戻る
            request.setAttribute("_token", request.getSession().getId());
            request.setAttribute("hasError", true);
            request.setAttribute("mail_address", mail_address);

            // 画面遷移
            RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/login/login.jsp");
            rd.forward(request, response);
        } else {
            // 認証できたらログイン状態にしてトップページへリダイレクト
            request.getSession().setAttribute("login_user", u);

            // セッションスコープにフラッシュメッセージをセットする
            request.getSession().setAttribute("flush", "ログインしました。");

            // 画面遷移
            response.sendRedirect(request.getContextPath() + "/posts/index");
        }
    }

}