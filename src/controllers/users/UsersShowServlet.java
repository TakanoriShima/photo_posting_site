package controllers.users;

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
 * Servlet implementation class UsersShowServlet
 */
@WebServlet("/users/show")
public class UsersShowServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public UsersShowServlet() {
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

        // 該当のIDのuserをデータベースから取得
        User u = em.find(User.class, Integer.parseInt(request.getParameter("id")));
        // ページネーション
        int page;
        try {
            page = Integer.parseInt(request.getParameter("page"));
        } catch (Exception e) {
            page = 1;
        }
        // 該当ユーザーの投稿を全て表示する
        List<Post> post_list = em.createNamedQuery("getMyAllPosts", Post.class).setParameter("user", u)
                .setFirstResult(15 * (page - 1)).setMaxResults(15).getResultList();
        // 該当ユーザーの投稿件数をカウントする
        long post_count = (long) em.createNamedQuery("getMyPostsCount", Long.class).setParameter("user", u)
                .getSingleResult();
        // DAOの破棄
        em.close();

        // リクエストスコープに各データをセット
        request.setAttribute("user", u);
        request.setAttribute("post_list", post_list);
        request.setAttribute("post_count", post_count);
        request.setAttribute("page", page);

        // 画面遷移
        RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/users/show.jsp");
        rd.forward(request, response);
    }

}
