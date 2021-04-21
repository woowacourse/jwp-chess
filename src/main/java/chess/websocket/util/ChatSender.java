package chess.websocket.util;

import static j2html.TagCreator.article;
import static j2html.TagCreator.p;

import java.io.IOException;
import org.json.JSONObject;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

public class ChatSender {

    public void sendMessage(WebSocketSession session, String name, String message) {
        try {
            session.sendMessage(new TextMessage(String.valueOf(new JSONObject()
                .put("userMessage", createHtmlMessage(name, message))
            )));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String createHtmlMessage(String name, String message) {
        return article().with(
            p(name + " : " + message)
        ).render();
    }
}
