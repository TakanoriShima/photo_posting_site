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
        EntityManager em = DBUtil.createEntityManager();

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

        request.setAttribute("posts", posts);

        //セッションスコープに残っている「フラッシュメッセージ」と「エラーメッセージ」を削除する。
        if (request.getSession().getAttribute("flush") != null) {
            request.setAttribute("flush", request.getSession().getAttribute("flush"));
            request.getSession().removeAttribute("flush");
        }

        // 全投稿件数をカウントする
        long post_count = (long) em.createNamedQuery("getPostsCount", Long.class).getSingleResult();

        em.close();

        request.setAttribute("user", u);
        request.setAttribute("post_count", post_count);
        request.setAttribute("page", page);
        if (request.getSession().getAttribute("flush") != null) {
            request.getSession().removeAttribute("flush");
        }

        if (request.getSession().getAttribute("errors") != null) {
            request.getSession().removeAttribute("errors");

        }
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
