package chess.config;

import chess.repository.room.Room;
import chess.util.JsonConverter;
import com.google.gson.JsonObject;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;
import java.util.stream.Collectors;

public class RoomResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(ChessRoom.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        HttpServletRequest request
                = (HttpServletRequest) webRequest.getNativeRequest();

        String body = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));

        JsonObject roomJson = JsonConverter.toJsonObject(body);
        String name = roomJson.get("name").getAsString();
        String turn = roomJson.get("turn").getAsString();
        JsonObject state = roomJson.get("state").getAsJsonObject();
        return new Room(name, turn, state);
    }
}
