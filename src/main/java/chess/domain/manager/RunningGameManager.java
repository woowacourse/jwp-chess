package chess.domain.manager;

import chess.controller.web.dto.PieceDto;
import chess.domain.board.Board;
import chess.domain.piece.attribute.Color;
import chess.domain.position.Position;
import chess.domain.statistics.ChessGameStatistics;
import chess.domain.statistics.MatchResult;
import chess.exception.InvalidChessArgumentException;
import chess.exception.InvalidMoveException;

import java.util.Map;

import static java.util.stream.Collectors.toMap;

public class RunningGameManager implements ChessGameManager {
    private final long id;
    private final Board board;
    private final String title;
    private Color currentColor;

    public RunningGameManager(long id, Board board, String title, Color currentColor) {
        this.id = id;
        this.board = board;
        this.title = title;
        this.currentColor = currentColor;
    }

    @Override
    public ChessGameManager start() {
        return ChessGameManagerFactory.createRunningGame(id, title);
    }

    @Override
    public ChessGameManager end() {
        return ChessGameManagerFactory.createEndGame(id, title, getStatistics(), board);
    }

    @Override
    public void move(Position from, Position to) {
        if (board.findColorByPosition(from) != currentColor) {
            throw new InvalidMoveException("현재 움직일 수 있는 진영의 기물이 아닙니다.");
        }
        board.move(from, to);
        turnOver();
    }

    private void turnOver() {
        currentColor = currentColor.opposite();
    }

    @Override
    public boolean isKingDead() {
        return board.isKingDead();
    }

    @Override
    public long getId() {
        return id;
    }

    @Override
    public Map<String, PieceDto> getPieces() {
        return board.getAliveSquares().stream()
                .collect(toMap(square -> square.getPosition().toString()
                        , square -> new PieceDto(square.getNotationText(), square.getColor().name())));
    }

    @Override
    public ChessGameStatistics getStatistics() {
        Map<Color, Double> scoreMap = board.getScoreMap();
        if (isKingDead()) {
            return new ChessGameStatistics(scoreMap, MatchResult.generateMatchResultByKingAliveColor(board.kingAliveColor()));
        }
        return new ChessGameStatistics(scoreMap, MatchResult.generateMatchResult(scoreMap.get(Color.WHITE), scoreMap.get(Color.BLACK)));
    }

    public Board getBoard() {
        return this.board;
    }

    @Override
    public Color nextColor() {
        return currentColor;
    }

    @Override
    public boolean isNotEnd() {
        return true;
    }

    @Override
    public boolean isEnd() {
        return false;
    }

    @Override
    public boolean isStart() {
        return true;
    }

    @Override
    public String getTitle() {
        return title;
    }
}
