package chess.entity;

import chess.domain.game.ChessGame;
import chess.domain.game.Turn;

public class ChessGameEntity {

    private long id;
    private String name;
    private String password;
    private boolean isOn;
    private String teamValueOfTurn;

    public ChessGameEntity(final long id, final String name, final String password, final boolean isOn,
                           final String teamValueOfTurn) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.isOn = isOn;
        this.teamValueOfTurn = teamValueOfTurn;
    }

    public ChessGameEntity(final String name, final String password, final ChessGame chessGame) {
        this.name = name;
        this.password = password;
        this.isOn = chessGame.isOn();
        this.teamValueOfTurn = chessGame.getTurn().getNow().getValue();
    }

    public ChessGameEntity(final long id, final boolean isOn, final Turn turn) {
        this.id = id;
        this.isOn = isOn;
        this.teamValueOfTurn = turn.getNow().getValue();
    }

    public ChessGameEntity(final long id, final String password) {
        this.id = id;
        this.password = password;
    }

    public long getId() {
        return id;
    }

    public String getPassword() {
        return password;
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
