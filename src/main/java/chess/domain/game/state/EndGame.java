package chess.domain.game.state;

import java.util.HashMap;
import java.util.Map;

import chess.domain.piece.Piece;
import chess.domain.piece.position.Position;
import chess.domain.piece.property.Color;

public class EndGame implements GameState {

    private final ChessBoard board;

    public EndGame(ChessBoard board) {
        this.board = board;
    }

    @Override
    public GameState start() {
        throw new UnsupportedOperationException("이미 종료된 게임입니다.");
    }

    @Override
    public GameState move(Position source, Position target) {
        throw new UnsupportedOperationException("이미 종료된 게임입니다.");
    }

    @Override
    public Map<Color, Double> status() {
        return board.computeScore();
    }

    @Override
    public GameState end() {
        throw new UnsupportedOperationException("이미 종료된 게임입니다.");
    }

    @Override
    public boolean isFinished() {
        return true;
    }

    @Override
    public Map<Position, Piece> getBoard() {
        return new HashMap<>();
    }

    @Override
    public String getTurn() {
        throw new UnsupportedOperationException("이미 종료된 게임입니다.");
    }
}
