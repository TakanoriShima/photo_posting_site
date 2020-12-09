package controllers.toppage;

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
import utils.DBUtil;

/**
 * Servlet implementation class TopPageIndexServlet
 */
@WebServlet("/index.html")
public class TopPageIndexServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public TopPageIndexServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
     *      response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // DAOインスタンスの生成
        EntityManager em = DBUtil.createEntityManager();

        // 全ユーザの投稿リスト取得
        List<Post> posts = em.createNamedQuery("getAllPosts", Post.class).getResultList();
        // リクエストスコープに、全ユーザの投稿リストをセット
        request.setAttribute("posts", posts);
        // DAOの破棄
        em.close();
        // フラッシュメッセージ処理
        // セッションスコープにflushメッセージがセットされているならば
        if (request.getSession().getAttribute("flush") != null) {
            // そのflushメッセージをリクエストスコープにセット
            request.setAttribute("flush", request.getSession().getAttribute("flush"));
            // セッションスコープに保存されたflushメッセージ削除
            request.getSession().removeAttribute("flush");
        }
        // ビューへの画面遷移
        RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/topPage/index.jsp");
        rd.forward(request, response);
    }

}
