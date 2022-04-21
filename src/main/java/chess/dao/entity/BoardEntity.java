package chess.dao.entity;

import chess.domain.board.Position;
import chess.domain.piece.Piece;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BoardEntity {

    private final String name;
    private final String position_column_value;
    private final int position_row_value;
    private final String piece_name;
    private final String piece_team_value;

    public BoardEntity(String name,
                       String position_column_value,
                       int position_row_value,
                       String piece_name,
                       String piece_team_value) {
        this.name = name;
        this.position_column_value = position_column_value;
        this.position_row_value = position_row_value;
        this.piece_name = piece_name;
        this.piece_team_value = piece_team_value;
    }

    public static List<BoardEntity> generateBoardEntities(final String name, final Map<Position, Piece> board) {
        List<BoardEntity> boardEntities = new ArrayList<>();
        for (Position position : board.keySet()) {
            Piece piece = board.get(position);
            BoardEntity boardEntity = new BoardEntity(name,
                    String.valueOf(position.getColumn().getValue()),
                    position.getRow().getValue(),
                    piece.getName(),
                    piece.getTeam().getValue()
            );
            boardEntities.add(boardEntity);
        }
        return boardEntities;
    }

    public String getName() {
        return name;
    }

    public String getPosition_column_value() {
        return position_column_value;
    }

    public int getPosition_row_value() {
        return position_row_value;
    }

    public String getPiece_name() {
        return piece_name;
    }

    public String getPiece_team_value() {
        return piece_team_value;
    }
}
