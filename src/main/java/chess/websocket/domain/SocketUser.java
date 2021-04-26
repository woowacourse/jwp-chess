package chess.websocket.domain;

import chess.domain.TeamColor;
import chess.domain.room.User;
import java.io.IOException;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

public class SocketUser implements User {

    private final WebSocketSession session;
    private boolean player;
    private String nickname;
    private TeamColor teamColor;
    private Long roomId;


    public SocketUser(WebSocketSession session) {
        this.session = session;
        player = false;
        nickname = "사용자";
        teamColor = TeamColor.NONE;
    }

    @Override
    public void sendData(String data) {
        try {
            session.sendMessage(new TextMessage(data));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean isPlayer() {
        return player;
    }

    @Override
    public void setAsPlayer(TeamColor teamColor) {
        player = true;
        this.teamColor = teamColor;
    }

    @Override
    public void setAsNotPlayer() {
        player = false;
        this.teamColor = TeamColor.NONE;
    }

    @Override
    public TeamColor teamColor() {
        return teamColor;
    }

    @Override
    public String name() {
        return nickname;
    }

    @Override
    public boolean isWhite() {
        return teamColor == TeamColor.WHITE;
    }

    @Override
    public boolean isBlack() {
        return teamColor == TeamColor.BLACK;
    }

    @Override
    public void enterRoom(Long roomId, String nickname) {
        this.roomId = roomId;
        this.nickname = nickname;
    }

    @Override
    public Long roomId() {
        return roomId;
    }


}
