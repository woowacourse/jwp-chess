package spring.vo;

import spring.chess.piece.type.Piece;
import spring.chess.piece.type.PieceMapper;

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
