package vo;

import chess.piece.type.Pawn;
import chess.piece.type.Piece;
import chess.piece.type.PieceMapper;
import chess.piece.type.Rook;

import java.util.ArrayList;
import java.util.List;

public class PieceVo {
    private final int gameId;
    private final String name;
    private final int row;
    private final String col;

    public PieceVo(int gameId, String name, int row, String col) {
        this.gameId = gameId;
        this.name = name;
        this.row = row;
        this.col = col;
    }

    public Piece toPiece() {
        return PieceMapper.of(name.charAt(0));
    }

    public int getGameId() {
        return gameId;
    }

    public String getName() {
        return name;
    }

    public int getRow() {
        return row;
    }

    public String getCol() {
        return col;
    }
}
