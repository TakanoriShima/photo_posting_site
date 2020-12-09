package controllers.post;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import models.Post;

/**
 * Servlet implementation class PostsNewServlet
 */
@WebServlet("/posts/new")
public class PostsNewServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public PostsNewServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
     *      response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // CSRF対策
        request.setAttribute("_token", request.getSession().getId());
        // Postクラスの生成
        Post p = new Post();
        // リクエストスコープにpostのデータをセット
        request.setAttribute("post", p);

        // セッションスコープにエラーメッセージがあるならば
        if (request.getSession().getAttribute("errors") != null) {
            // エラーメッセージをリクエストスコープにセットする
            request.setAttribute("errors", request.getSession().getAttribute("errors"));
            // セッションスコープのエラーメッセージを削除する
            request.getSession().removeAttribute("errors");
        }

        // 画面遷移
        RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/posts/new.jsp");
        rd.forward(request, response);
    }

}
