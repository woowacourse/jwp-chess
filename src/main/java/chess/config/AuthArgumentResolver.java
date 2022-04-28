package chess.config;

import chess.domain.auth.AuthCredentials;
import chess.domain.auth.EncryptedAuthCredentials;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class AuthArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        return methodParameter.getParameterType().equals(EncryptedAuthCredentials.class);
    }

    @Override
    public Object resolveArgument(MethodParameter methodParameter,
                                  ModelAndViewContainer modelAndViewContainer,
                                  NativeWebRequest nativeWebRequest,
                                  WebDataBinderFactory webDataBinderFactory) throws IOException {

        HttpServletRequest request = (HttpServletRequest) nativeWebRequest.getNativeRequest();
        String requestBody = request.getReader()
                .lines()
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("필요한 정보가 입력되지 않았습니다."));

        AuthCredentials authCredentials = AuthCredentials.of(requestBody);
        return authCredentials.toEncrypted();
    }
}