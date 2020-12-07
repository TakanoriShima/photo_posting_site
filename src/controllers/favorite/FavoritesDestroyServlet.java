package controllers.favorite;

import java.io.IOException;

import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import models.Favorite;
import models.Post;
import models.User;
import utils.DBUtil;

/**
 * Servlet implementation class FavoritesDestroyServlet
 */
@WebServlet("/favorites/destroy")
public class FavoritesDestroyServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public FavoritesDestroyServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
     *      response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String _token = (String) request.getParameter("_token");
        if (_token != null && _token.equals(request.getSession().getId())) {
            EntityManager em = DBUtil.createEntityManager();

            // favoriteに現在ログインしているユーザー情報をセットする
            User u = (User) request.getSession().getAttribute("login_user");
            // favoriteに現在見ている投稿の情報をセットする
            Integer post_id = Integer.parseInt(request.getParameter("post_id"));
            Post p = em.find(Post.class, post_id);

            Favorite f = em.createNamedQuery("getFavoritesOfAttenssion", Favorite.class).setParameter("user", u)
                    .setParameter("post", p).getSingleResult();
            em.getTransaction().begin();
            // favoriteを削除する
            em.remove(f);
            em.getTransaction().commit();
            em.close();

            request.getSession().setAttribute("flush", "いいねを削除しました。");
            response.sendRedirect(request.getContextPath() + "/posts/show?id=" + post_id);
        }
    }

}
