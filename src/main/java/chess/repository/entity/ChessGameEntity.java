package chess.repository.entity;

import chess.domain.game.ChessGame;

public class ChessGameEntity {

    private final String gameRoomId;
    private final boolean isOn;
    private final String teamValueOfTurn;

    public ChessGameEntity(String gameRoomId, boolean isOn, String teamValueOfTurn) {
        this.gameRoomId = gameRoomId;
        this.isOn = isOn;
        this.teamValueOfTurn = teamValueOfTurn;
    }

    public ChessGameEntity(String gameRoomId, ChessGame chessGame) {
        this.gameRoomId = gameRoomId;
        this.isOn = chessGame.isOn();
        this.teamValueOfTurn = chessGame.getTurn().getNow().getValue();
    }

    public String getGameRoomId() {
        return gameRoomId;
    }

    public boolean getIsOn() {
        return isOn;
    }

    public String getTeamValueOfTurn() {
        return teamValueOfTurn;
    }
}
