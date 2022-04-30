package chess.domain;

import chess.piece.Piece;
import chess.utils.InitializedChessboardGenerator;
import chess.utils.UnicodeConverter;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ChessGame {
    private CurrentStatus currentStatus;
    private Chessboard chessboard;

    public ChessGame() {
        currentStatus = new CurrentStatus();
        chessboard = new Chessboard();
    }

    public ChessGame(String state, String turn, Map<Position, Piece> boards) {
        this.currentStatus = new CurrentStatus(state, turn);
        this.chessboard = new Chessboard(boards);
    }

    public ChessGame(CurrentStatus currentStatus, Chessboard chessboard) {
        this.currentStatus = currentStatus;
        this.chessboard = chessboard;
    }

    public void start() {
        currentStatus.start();
        chessboard = new Chessboard(InitializedChessboardGenerator.generate());
    }

    public void move(MovingPosition movingPosition) {
        currentStatus.move(chessboard, movingPosition);
    }

    public void end() {
        currentStatus.end();
    }

    public boolean isFinished() {
        return currentStatus.isFinished();
    }

    public List<String> getBoardByUnicode() {
        return chessboard.getBoard()
                .values()
                .stream()
                .map(piece -> UnicodeConverter.convert(piece.getSymbolByColor()))
                .collect(Collectors.toList());
    }

    public Map<Position,Piece> getChessBoard() {
        return chessboard.getBoard();
    }

    public CurrentStatus getCurrentStatus() {
        return currentStatus;
    }

    public String getStateToString() {
        return currentStatus.getStateToString();
    }

    public String getColorOfTurn() {
        return currentStatus.getTurnToString();
    }

}
