package controllers.post;

import java.io.IOException;
import java.util.List;

import javax.persistence.EntityManager;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import models.Post;
import models.User;
import utils.DBUtil;

/**
 * Servlet implementation class PostsIndexServlet
 */
@WebServlet("/posts/index")
public class PostsIndexServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public PostsIndexServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
     *      response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // TODO Auto-generated method stub
        // DAOインスタンスの生成
        EntityManager em = DBUtil.createEntityManager();

        // セッションスコープから login_user 情報を抜き出す
        User u = (User) request.getSession().getAttribute("login_user");

        // ページネーション
        int page;
        try {
            page = Integer.parseInt(request.getParameter("page"));
        } catch (Exception e) {
            page = 1;
        }

        // トップページに全ユーザーの投稿を一覧表示する
        List<Post> posts = em.createNamedQuery("getAllPosts", Post.class).getResultList();
        // リクエストスコープに全投稿リストをセット
        request.setAttribute("posts", posts);

        // セッションスコープにフラッシュメッセージが残っているならば
        if (request.getSession().getAttribute("flush") != null) {
            // セッションスコープにフラッシュメッセージが「登録が完了しました」または「ログインしました。」または「投稿しました。」ならば
            if (request.getSession().getAttribute("flush").equals("登録が完了しました。")
                    || request.getSession().getAttribute("flush").equals("ログインしました。")
                    || request.getSession().getAttribute("flush").equals("投稿しました。")) {
                // セッションスコープにフラッシュメッセージとしてリクエストスコープにセット
                request.setAttribute("flush", request.getSession().getAttribute("flush"));
            }
            // リクエストスコープにセットされたフラッシュメッセージを削除する
            request.getSession().removeAttribute("flush");
        }

        // 全投稿件数をカウントする
        long post_count = (long) em.createNamedQuery("getPostsCount", Long.class).getSingleResult();
        // DAOの破棄
        em.close();
        // リクエストスコープに各データをセット
        request.setAttribute("user", u);
        request.setAttribute("post_count", post_count);
        request.setAttribute("page", page);

        // セッションスコープにエラーメッセージがあるならば
        if (request.getSession().getAttribute("errors") != null) {
            // エラーメッセージを削除
            request.getSession().removeAttribute("errors");
        }

        // 画面遷移
        RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/posts/index.jsp");
        rd.forward(request, response);
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
     *      response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // TODO Auto-generated method stub
        doGet(request, response);
    }

}
