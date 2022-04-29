package chess.config;

import chess.domain.auth.NullCookie;
import chess.domain.auth.PlayerCookie;
import chess.util.CookieUtil;
import java.util.Arrays;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class CookieArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        return methodParameter.getParameterType().equals(PlayerCookie.class);
    }

    @Override
    public Object resolveArgument(MethodParameter methodParameter,
                                  ModelAndViewContainer modelAndViewContainer,
                                  NativeWebRequest nativeWebRequest,
                                  WebDataBinderFactory webDataBinderFactory) {

        HttpServletRequest request = (HttpServletRequest) nativeWebRequest.getNativeRequest();
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return new NullCookie();
        }
        return Arrays.stream(cookies)
                .filter(cookie -> CookieUtil.KEY.equals(cookie.getName()))
                .map(PlayerCookie::of)
                .findFirst()
                .orElseGet(NullCookie::new);
    }
}
