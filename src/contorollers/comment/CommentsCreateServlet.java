package contorollers.comment;

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
        String _token = (String) request.getParameter("_token");
        if (_token != null && _token.equals(request.getSession().getId())) {
            EntityManager em = DBUtil.createEntityManager();

            Comment c = new Comment();
            c.setUser((User) request.getSession().getAttribute("login_user"));

            Integer post_id = Integer.parseInt(request.getParameter("post_id"));
            Post p = em.find(Post.class, post_id);
            c.setPost(p);

            c.setContent(request.getParameter("content"));
            Timestamp currentTime = new Timestamp(System.currentTimeMillis());
            c.setCreated_at(currentTime);

            List<String> errors = CommentValidator.validate(c);
            if (errors.size() > 0) {
                em.close();

                request.setAttribute("_token", _token);
                request.setAttribute("comment", c);
                request.setAttribute("errors", errors);
                RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/posts/show.jsp");
                rd.forward(request, response);


            }

                em.getTransaction().begin();
            em.persist(c);
            em.getTransaction().commit();
            em.close();

            request.getSession().setAttribute("flush", "コメントを投稿しました。");
            response.sendRedirect(request.getContextPath() + "/posts/show?id=" + p.getId());
        }
    }

}
