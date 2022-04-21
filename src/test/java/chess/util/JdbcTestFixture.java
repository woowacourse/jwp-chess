package chess.util;

import chess.domain.board.Board;
import chess.domain.board.BoardFactory;
import chess.domain.board.Position;
import chess.domain.piece.Blank;
import chess.domain.piece.Pawn;
import chess.domain.piece.Piece;
import chess.domain.piece.Team;
import chess.dto.ChessDto;
import chess.entity.BoardEntity;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class JdbcTestFixture {

    public static List<BoardEntity> getMovedTestBoard() {
        Map<Position, Piece> board = BoardFactory.initialize();
        board.put(Position.valueOf("a2"), new Blank());
        board.put(Position.valueOf("a4"), new Pawn(Team.WHITE));
        return getBoardEntities(board);
    }

    public static List<BoardEntity> getTestBoardForMove() {
        Map<Position, Piece> board = BoardFactory.initialize();
        board.put(Position.valueOf("a4"), new Blank());
        board.put(Position.valueOf("a5"), new Pawn(Team.WHITE));
        return getBoardEntities(board);
    }

    private static List<BoardEntity> getBoardEntities(final Map<Position, Piece> board) {
        return ChessDto.of(new Board(board)).getBoard()
            .entrySet()
            .stream()
            .map(entry -> new BoardEntity(entry.getKey(), entry.getValue()))
            .collect(Collectors.toList());
    }

}
