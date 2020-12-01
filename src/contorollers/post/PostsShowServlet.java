package contorollers.post;

import java.io.IOException;
import java.util.List;

import javax.persistence.EntityManager;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import models.Comment;
import models.Favorite;
import models.Post;
import models.User;
import utils.DBUtil;

/**
 * Servlet implementation class PostsShowServlet
 */
@WebServlet("/posts/show")
public class PostsShowServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public PostsShowServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
     *      response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        EntityManager em = DBUtil.createEntityManager();

        Post p = em.find(Post.class, Integer.parseInt(request.getParameter("id")));

        // いいね数をカウントするクエリを実行
        long favorites_count = (long) em.createNamedQuery("getFavoritesCount", Long.class).setParameter("post", p)
                .getSingleResult();
        // この投稿にいいねした人たちを取得
        List<User> favorited_user_list = p.getFavorited_user_list();

        // ログインユーザーがこの投稿にいいねしたか判別
        User u = (User) request.getSession().getAttribute("login_user");
        List<Favorite> favoritesOfAttension = em.createNamedQuery("getFavoritesOfAttenssion", Favorite.class)
                .setParameter("user", u).setParameter("post", p).getResultList();
        int count = favoritesOfAttension.size();

        // 投稿されたコメントの表示
        List<Comment> comment_list = em.createNamedQuery("getAllCommentsForAttensionPost", Comment.class)
                .setParameter("post", p).getResultList();
        em.close();
        request.getSession().setAttribute("post_id", p.getId());
        request.setAttribute("post", p);
        request.setAttribute("_token", request.getSession().getId());
        request.setAttribute("favorites_count", favorites_count);
        request.setAttribute("favorited_user_list", favorited_user_list);
        request.setAttribute("count", count);
        request.setAttribute("comment_list", comment_list);

        RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/posts/show.jsp");
        rd.forward(request, response);

    }

}
