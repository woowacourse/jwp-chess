package chess.chessgame.domain.room.game;

import chess.chessgame.domain.room.game.board.InitBoardInitializer;
import chess.chessgame.domain.room.game.board.piece.attribute.Color;
import chess.chessgame.domain.room.game.board.position.Position;
import chess.chessgame.domain.room.game.statistics.ChessGameStatistics;
import chess.controller.web.dto.PieceDto;

import java.util.Map;

public abstract class NotRunningGameManager implements ChessGameManager {
    private final long id;

    public NotRunningGameManager(long id) {
        this.id = id;
    }

    @Override
    public ChessGameManager start() {
        return ChessGameManagerFactory.createRunningGame(id);
    }

    @Override
    public ChessGameManager end() {
        return ChessGameManagerFactory.createEndGame(id, ChessGameStatistics.createNotStartGameResult(), InitBoardInitializer.getBoard());
    }

    @Override
    public void move(Position from, Position to) {
        throw new UnsupportedOperationException("게임이 진행중이지 않아 실행할 수 없습니다.");
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
        throw new UnsupportedOperationException("게임이 진행중이지 않아 실행할 수 없습니다.");
    }
}
