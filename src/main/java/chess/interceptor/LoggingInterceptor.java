package chess.interceptor;

import java.util.UUID;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

@Slf4j
public class LoggingInterceptor implements HandlerInterceptor {

    private static final String REQUEST_ID = "REQUEST_ID";
    private static final String LINE_BREAK = System.lineSeparator();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {

        String requestId = UUID.randomUUID().toString().substring(0, 7);
        request.setAttribute(REQUEST_ID, requestId);

        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            log.info("Request = [{}] [{}]", requestId, handlerMethod.getMethod());
            return true;
        }

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) throws Exception {

        String requestId = (String) request.getAttribute(REQUEST_ID);

        log.info("Response = [{}] modelAndView = [{}]", requestId, modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {

        String requestId = (String) request.getAttribute(REQUEST_ID);

        if (ex != null) {
            log.error("error ! = [{}] [{}]", ex.getClass().getSimpleName(), ex.getMessage());
        }

        log.info("Response : [{}] [{}]" + LINE_BREAK, requestId, handler);
    }
}
