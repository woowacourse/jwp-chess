package chess.domain.manager;

import chess.controller.web.dto.PieceDto;
import chess.domain.board.InitBoardInitializer;
import chess.domain.piece.attribute.Color;
import chess.domain.position.Position;
import chess.domain.statistics.ChessGameStatistics;
import chess.exception.InvalidChessStatusException;

import java.util.Map;

public abstract class NotRunningGameManager implements ChessGameManager {
    private final long id;
    private final String title;

    public NotRunningGameManager(long id, String title) {
        this.id = id;
        this.title = title;
    }

    @Override
    public ChessGameManager start() {
        return ChessGameManagerFactory.createRunningGame(id, title);
    }

    @Override
    public ChessGameManager end() {
        return ChessGameManagerFactory.createEndGame(id, title, ChessGameStatistics.createNotStartGameResult(), InitBoardInitializer.getBoard());
    }

    @Override
    public void move(Position from, Position to) {
        throw new InvalidChessStatusException("게임이 진행중이지 않아 실행할 수 없습니다.");
    }

    @Override
    public Color nextColor() {
        return Color.BLANK;
    }

    @Override
    public boolean isKingDead() {
        return false;
    }

    @Override
    public long getId() {
        return id;
    }

    @Override
    public Map<String, PieceDto> getPieces() {
        throw new InvalidChessStatusException("게임이 진행중이지 않아 실행할 수 없습니다.");
    }

    @Override
    public String getTitle() {
        return title;
    }
}
