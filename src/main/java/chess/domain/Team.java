package chess.domain;

import chess.domain.piece.Blank;
import chess.domain.piece.Piece;
import chess.domain.position.Position;
import chess.domain.state.BlackTurn;
import chess.domain.state.Finished;
import chess.domain.state.GameState;
import chess.domain.state.WhiteTurn;

import java.util.Locale;
import java.util.Map;

public enum Team {
    BLACK,
    WHITE,
    NONE;

    public String getSymbol(String symbol) {
        if (this == BLACK) {
            return symbol.toUpperCase(Locale.ROOT);
        }
        if (this == WHITE) {
            return symbol.toLowerCase(Locale.ROOT);
        }
        return Blank.SYMBOL;
    }

    public boolean matchTeam(Team team) {
        return this == team;
    }

    public boolean isEnd() {
        return this == NONE;
    }

    public Team nextTurn() {
        if (this == Team.BLACK) {
            return Team.WHITE;
        }
        return Team.BLACK;
    }

    public GameState findStateByTeam(Map<Position, Piece> board) {
        if (this == Team.NONE) {
            return new Finished(this, board);
        }
        if (this == Team.WHITE) {
            return new WhiteTurn(board);
        }
        return new BlackTurn(board);
    }
}
