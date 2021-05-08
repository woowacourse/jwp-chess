package chess.interceptor;

import java.util.Objects;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.springframework.web.servlet.HandlerInterceptor;

public class LoginInterceptor implements HandlerInterceptor {

    private static final String SESSION_KEY = "AUTHENTICATION";

    @Override
    public boolean preHandle(final HttpServletRequest request, final HttpServletResponse response,
        final Object handler) throws Exception {

        final HttpSession session = request.getSession();
        final Object sessionKey = session.getAttribute(SESSION_KEY);

        if (Objects.isNull(sessionKey)) {
            response.sendRedirect("/");
            return false;
        }
        return true;
    }

}
