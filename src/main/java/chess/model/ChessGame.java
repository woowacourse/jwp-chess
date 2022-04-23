package chess.model;

import chess.model.board.Board;
import chess.model.board.BoardInitializer;
import chess.model.board.ChessInitializer;
import chess.model.board.EmptyBoardInitializer;
import chess.model.board.Square;
import chess.model.piece.King;
import chess.model.piece.Piece;
import chess.service.dto.GameResultDto;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ChessGame {
    private Board board;
    private Color turn;
    private Status status;

    public ChessGame() {
        init();
    }

    public ChessGame(BoardInitializer initializer, Status status) {
        this.board = new Board(initializer);
        this.turn = Color.WHITE;
        this.status = status;
    }

    public ChessGame(Board board, Color turn, Status status) {
        this.board = board;
        this.turn = turn;
        this.status = status;
    }

    public void move(Square from, Square to) {
        status = status.changeStatus(GameCommand.MOVE);
        if (board.findPieceBySquare(from).isSameColor(turn)) {
            board.move(from, to);
            turn = turn.changeToOpposite();
            changeStatusWhenKingDead();
            return;
        }
        throw new IllegalArgumentException(String.format("해당 기물을 움직일 권한이 없습니다. 현재 %s의 차례입니다.", turn.name()));
    }

    private void changeStatusWhenKingDead() {
        if (!board.bothKingAlive()) {
            status = status.changeStatus(GameCommand.STATUS);
        }
    }

    public Map<Color, Double> getPlayersScore() {

        return Color.getPlayerColors().stream()
                .collect(Collectors.toMap(Function.identity(), board::calculatePoint));
    }

    public boolean isPlaying() {
        return status.isPlaying();
    }

    public boolean isEmpty() {
        return status.isEmpty();
    }

    public Piece findPieceBySquare(Square square) {
        return board.findPieceBySquare(square);
    }

    //TODO DTO대신 뭘 반환하는게 좋은가
    public GameResultDto getResult() {
        status = status.changeStatus(GameCommand.STATUS);
        Color winner = findWinner();
        if (winner.equals(Color.NOTHING)) {
            return new GameResultDto(getScores(), winner.name(), true);
        }
        return new GameResultDto(getScores(), winner.name(), false);
    }

    private Map<String, Double> getScores() {
        return getPlayersScore().entrySet()
                .stream()
                .collect(Collectors.toMap(entry -> entry.getKey().name(), Entry::getValue));
    }

    private Color findWinner() {
        if (board.bothKingAlive()) {
            return findByPoint();
        }
        return findByKing();
    }

    private Color findByKing() {
        if (board.bothKingAlive()) {
            return Color.NOTHING;
        }
        return getColorHasKing();
    }

    private Color getColorHasKing() {
        return Color.getPlayerColors().stream()
                .filter(color -> board.has(new King(color)))
                .findFirst()
                .orElse(Color.NOTHING);
    }

    private Color findByPoint() {
        double blackPoint = board.calculatePoint(Color.BLACK);
        double whitePoint = board.calculatePoint(Color.WHITE);
        if (blackPoint < whitePoint) {
            return Color.WHITE;
        }
        if (blackPoint > whitePoint) {
            return Color.BLACK;
        }
        return Color.NOTHING;
    }

    public void end() {
        this.status = Status.EMPTY;
        this.board.remove();
    }

    public Board getBoard() {
        return board;
    }

    public Color getTurn() {
        return this.turn;
    }

    public Status getStatus() {
        return status;
    }

    public void init() {
        this.board = new Board(new ChessInitializer());
        this.status = Status.PLAYING;
        this.turn = Color.WHITE;
    }
}
