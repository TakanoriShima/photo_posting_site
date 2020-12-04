package contorollers.favorite;

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
 * Servlet implementation class FavoritesCreate
 */
@WebServlet("/favorites/create")
public class FavoritesCreateServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public FavoritesCreateServlet() {
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
        if (_token != null && _token.equals(request.getSession().getId()))
            ;
        EntityManager em = DBUtil.createEntityManager();

        Favorite f = new Favorite();
        //favoriteに現在ログインしているユーザー情報をセットする
        f.setUser((User) request.getSession().getAttribute("login_user"));
        //favoriteに現在見ている投稿の情報をセットする
        Post p = em.find(Post.class, (Integer) request.getSession().getAttribute("post_id"));
        f.setPost(p);

        em.getTransaction().begin();
        em.persist(f);
        em.getTransaction().commit();
        em.close();

        request.getSession().setAttribute("flush", "いいねしました。");
        response.sendRedirect(request.getContextPath() + "/posts/show?id=" + p.getId());
    }

}
