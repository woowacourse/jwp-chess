package chess.domain.room;

import chess.domain.TeamColor;

public interface User {

    void sendData(String data);

    boolean isPlayer();

    void setAsPlayer(TeamColor teamColor);

    void setAsNotPlayer();
}
