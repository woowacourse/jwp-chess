package chess.domain;

import chess.piece.Piece;
import chess.state.Finish;
import chess.state.Ready;
import chess.state.State;
import chess.state.StateFactory;
import chess.utils.InitializedChessboardGenerator;
import chess.utils.UnicodeConverter;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ChessGame {

    private final Turn turn;
    private State state;
    private Chessboard chessboard;

    public ChessGame() {
        state = new Ready();
        turn = new Turn();
        chessboard = new Chessboard();
    }

    public ChessGame(String state, String turn, Map<Position, Piece> boards) {
        this.state = StateFactory.valueOf(state).create();
        this.turn = new Turn(turn);
        this.chessboard = new Chessboard(boards);
    }

    public void start() {
        state = state.start();
        chessboard = new Chessboard(InitializedChessboardGenerator.generate());
    }

    public void move(MovingPosition movingPosition) {
        state = state.move(chessboard, movingPosition, turn);
    }

    public void end() {
        state = new Finish();
    }

    public boolean isFinished() {
        return state.isFinished();
    }

    public List<String> getPiecesByUnicode() {
        return chessboard.getBoard()
                .values()
                .stream()
                .map(piece -> UnicodeConverter.convert(piece.getSymbolByColor()))
                .collect(Collectors.toList());
    }

    public Chessboard getChessBoard() {
        return chessboard;
    }

    public String getStateToString() {
        return state.getStateToString();
    }

    public String getColorOfTurn() {
        return turn.getColor();
    }

}
