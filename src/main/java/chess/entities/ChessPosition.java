package chess.entities;

import chess.domain.position.Column;
import chess.domain.position.Row;

public class ChessPosition {

    private final Integer id;
    private final Column column;
    private final Row row;
    private final Integer boardId;

    public ChessPosition(Integer id, Column column, Row row, Integer boardId) {
        this.id = id;
        this.column = column;
        this.row = row;
        this.boardId = boardId;
    }

    public ChessPosition(Column column, Row row, Integer boardId) {
        this(null, column, row, boardId);
    }

    public Integer getId() {
        return id;
    }

    public Column getColumn() {
        return column;
    }

    public Row getRow() {
        return row;
    }

    public Integer getBoardId() {
        return boardId;
    }
}
