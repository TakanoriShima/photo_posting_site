package contorollers.post;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import models.Post;
import models.User;
import models.validators.PostValidator;
import utils.DBUtil;
import utils.EncryptUtil;

/**
 * Servlet implementation class PostsCreateServlet
 */
@MultipartConfig // 画像アップ機能時は必ず必要
@WebServlet("/posts/create")
public class PostsCreateServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public PostsCreateServlet() {
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

            // 画像アップロード
            Part part = request.getPart("image");
            if (part.getSize() != 0) {
                String filename = getFileName(part);
                String filePath = getServletContext().getRealPath("/uploads/") + filename;
                System.out.println("filePath!!!" + filePath);
                File uploadDir = new File(getServletContext().getRealPath("/uploads/"));
                if (!uploadDir.exists()) {
                    uploadDir.mkdir();
                }
                part.write(filePath);

                Post p = new Post();

                p.setUser((User) request.getSession().getAttribute("login_user"));

                p.setTitle(request.getParameter("title"));
                p.setContent(request.getParameter("content"));
                p.setImage(filename);

                Timestamp currentTime = new Timestamp(System.currentTimeMillis());
                p.setCreated_at(currentTime);

                List<String> errors = PostValidator.validate(p);
                if (errors.size() > 0) {
                    em.close();

                    request.setAttribute("_token", _token);
                    request.setAttribute("post", p);
                    request.setAttribute("errors", errors);

                    RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/posts/new.jsp");
                    rd.forward(request, response);

                } else {
                    em.getTransaction().begin();
                    em.persist(p);
                    em.getTransaction().commit();
                    em.close();

                    request.getSession().setAttribute("flush", "投稿しました。");

                    response.sendRedirect(request.getContextPath() + "/");
                }

            } else {
                List<String> errors = new ArrayList<>();
                errors.add("画像を選択してください");
                request.getSession().setAttribute("errors", errors);
                RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/posts/new.jsp");
                rd.forward(request, response);
            }

        }

    }

    // 拡張子を変えずに、ランダムな名前のファイルを生成する
    private String getFileName(Part part) {
        String[] headerArrays = part.getHeader("Content-Disposition").split(";");
        String fileName = null;
        for (String head : headerArrays) {
            if (head.trim().startsWith("filename")) {
                fileName = head.substring(head.indexOf('"')).replaceAll("\"", "");
            }
        }
        Timestamp currentTime = new Timestamp(System.currentTimeMillis());
        String randName = EncryptUtil.getWordEncrypt(currentTime.toString());
        String extension = fileName.substring(fileName.lastIndexOf("."));
        String rndFileName = randName + extension;
        return rndFileName;
    }
}
