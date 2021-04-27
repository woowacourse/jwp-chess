package chess.domain.room;

import chess.domain.TeamColor;

public interface User {

    void sendData(String data);

    boolean isPlayer();

    void setAsPlayer(Long roomId, TeamColor teamColor, String nickname);

    void setAsNotPlayer(String name);

    TeamColor teamColor();

    String name();

    boolean isWhite();

    boolean isBlack();

    Long roomId();
}
