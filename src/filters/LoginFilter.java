package filters;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import models.User;

/**
 * Servlet Filter implementation class LoginFilter
 */
@WebFilter("/*")
public class LoginFilter implements Filter {

    /**
     * Default constructor.
     */
    public LoginFilter() {
        // TODO Auto-generated constructor stub
    }

    /**
     * @see Filter#destroy()
     */
    public void destroy() {
        // TODO Auto-generated method stub
    }

    /**
     * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
     */
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        String context_path = ((HttpServletRequest) request).getContextPath();
        String servlet_path = ((HttpServletRequest) request).getServletPath();

        if (!servlet_path.matches("/css.*")) { // CSSフォルダ内は認証処理から除外する
            HttpSession session = ((HttpServletRequest) request).getSession();

            // セッションスコープに保存されたユーザ（ログインユーザ）情報を取得
            User u = (User) session.getAttribute("login_user");

            // ログインしていない状態で"/posts/index"にアクセスした場合、トップページへ戻す
            if (u == null && servlet_path.equals("/posts/index")) {
                ((HttpServletResponse) response).sendRedirect(context_path + "/index.html");
                return;
            }
            // ログインしていない状態で"/posts/new"にアクセスした場合、トップページへ戻す
            if (u == null && servlet_path.equals("/posts/new")) {
                ((HttpServletResponse) response).sendRedirect(context_path + "/index.html");
                return;
            }
            // ログインしていない状態で"/posts/show"にアクセスした場合、トップページへ戻す
            if (u == null && servlet_path.equals("/posts/show")) {
                ((HttpServletResponse) response).sendRedirect(context_path + "/index.html");
                return;
            }
            // ログインしていない状態で"/users/show"にアクセスした場合、トップページへ戻す
            if (u == null && servlet_path.equals("/users/show")) {
                ((HttpServletResponse) response).sendRedirect(context_path + "/index.html");
                return;
            }

        }

        chain.doFilter(request, response);
    }

    /**
     * @see Filter#init(FilterConfig)
     */
    public void init(FilterConfig fConfig) throws ServletException {
        // TODO Auto-generated method stub
    }

}