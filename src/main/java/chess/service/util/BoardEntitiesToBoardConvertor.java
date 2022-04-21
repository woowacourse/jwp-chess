package chess.service.util;

import chess.domain.board.Board;
import chess.domain.board.Position;
import chess.domain.piece.Piece;
import chess.dao.entity.BoardEntity;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BoardEntitiesToBoardConvertor {

    public static Board convert(final List<BoardEntity> boardEntities) {
        Map<Position, Piece> board = new HashMap<>();
        for (BoardEntity boardEntity : boardEntities) {
            Position position = Position.of(
                    boardEntity.getPosition_column_value().charAt(0),
                    boardEntity.getPosition_row_value()
            );
            Piece piece = StringToPieceConvertor.convert(boardEntity.getPiece_name(), boardEntity.getPiece_team_value());
            board.put(position, piece);
        }
        return new Board(board);
    }
}
