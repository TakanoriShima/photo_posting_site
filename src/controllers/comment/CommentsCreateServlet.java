package controllers.comment;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;

import javax.persistence.EntityManager;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import models.Comment;
import models.Post;
import models.User;
import models.validators.CommentValidator;
import utils.DBUtil;

/**
 * Servlet implementation class CommentsCreateServlet
 */
@WebServlet("/comments/create")
public class CommentsCreateServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public CommentsCreateServlet() {
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
            // Commentのインスタンスを生成
            Comment c = new Comment();
            // 変数cに現在のログインユーザーの情報をセットする
            c.setUser((User) request.getSession().getAttribute("login_user"));
            // 変数cに現在見ている投稿の情報をセットする
            Integer post_id = Integer.parseInt(request.getParameter("post_id"));
            Post p = em.find(Post.class, post_id);
            c.setPost(p);
            // 変数cに内容に入力したコメントの内容をセットする
            c.setContent(request.getParameter("content"));
            // 変数cに現在日時を取得し、コメント作成日時にセットする
            Timestamp currentTime = new Timestamp(System.currentTimeMillis());
            c.setCreated_at(currentTime);

            // いいね数をカウントするクエリを実行
            long favorites_count = (long) em.createNamedQuery("getFavoritesCount", Long.class).setParameter("post", p)
                    .getSingleResult();
            // この投稿にいいねした人たちを取得
            List<User> favorited_user_list = p.getFavorited_user_list();
            // バリデーター の呼び出し
            List<String> errors = CommentValidator.validate(c);
            // errorsリストに1つでも追加されていたら
            if (errors.size() > 0) {
                // DAOの破棄
                em.close();

                // リクエストスコープに各データをセット
                request.setAttribute("_token", _token);
                request.setAttribute("comment", c);
                request.setAttribute("errors", errors);
                request.setAttribute("post", p);
                request.setAttribute("favorites_count", favorites_count);
                request.setAttribute("favorited_user_list", favorited_user_list);
                request.getSession().removeAttribute("flush");
                // 画面遷移
                RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/posts/show.jsp");
                rd.forward(request, response);

            }
            // データベースに保存
            em.getTransaction().begin();
            em.persist(c);
            em.getTransaction().commit();
            // セッションスコープにフラッシュメッセージをセットする
            request.getSession().setAttribute("flush", "コメントを投稿しました。");
            // DAOの破棄
            em.close();

            // 画面遷移
            response.sendRedirect(request.getContextPath() + "/posts/show?id=" + p.getId());
        }
    }

}
