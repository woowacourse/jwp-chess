package chess.entity;

import chess.domain.game.ChessGame;

public class ChessGameEntity {

    private long id;
    private final String name;
    private final boolean isOn;
    private final String teamValueOfTurn;

    public ChessGameEntity(final long id, final String name, final boolean isOn, final String teamValueOfTurn) {
        this.id =id;
        this.name = name;
        this.isOn = isOn;
        this.teamValueOfTurn = teamValueOfTurn;
    }

    public ChessGameEntity(final ChessGame chessGame) {
        this.name = chessGame.getName();
        this.isOn = chessGame.isOn();
        this.teamValueOfTurn = chessGame.getTurn().getNow().getValue();
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public boolean getIsOn() {
        return isOn;
    }

    public String getTeamValueOfTurn() {
        return teamValueOfTurn;
    }
}
