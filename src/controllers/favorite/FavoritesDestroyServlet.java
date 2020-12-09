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
        // String型の _tokenにパラメーターの_tokenを代入する
        String _token = (String) request.getParameter("_token");
        // _tokenがnullではなく、且つセッションIDと等しいならば
        if (_token != null && _token.equals(request.getSession().getId())) {
            // DAOインスタンスの生成
            EntityManager em = DBUtil.createEntityManager();

            // Userインスタンスを生成し、変数uに現在ログインしているuser情報をセットする
            User u = (User) request.getSession().getAttribute("login_user");
            // Postインスタンスを生成し、変数pに現在見ているpostの情報をセットする
            Integer post_id = Integer.parseInt(request.getParameter("post_id"));
            Post p = em.find(Post.class, post_id);

            // 現在ログインしているuserが現在みているpostにいいねしているか判別
            Favorite f = em.createNamedQuery("getFavoritesOfAttenssion", Favorite.class).setParameter("user", u)
                    .setParameter("post", p).getSingleResult();
            // favoriteを削除する
            em.getTransaction().begin();
            em.remove(f);
            em.getTransaction().commit();
            // DAOの破棄
            em.close();

            // セッションスコープにフラッシュメッセージをセットする
            request.getSession().setAttribute("flush", "いいねを削除しました。");

            // 画面遷移
            response.sendRedirect(request.getContextPath() + "/posts/show?id=" + post_id);
        }
    }

}
