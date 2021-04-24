package chess.chessgame.domain.room.game.board.piece;

import chess.chessgame.domain.room.game.board.Board;
import chess.chessgame.domain.room.game.board.InitBoardInitializer;
import chess.chessgame.domain.room.game.board.piece.attribute.Color;

public class Fixture {
    public static final Rook whiteRook = new Rook(Color.WHITE);
    public static final Rook blackRook = new Rook(Color.BLACK);
    public static final Bishop whiteBishop = new Bishop(Color.WHITE);
    public static final Queen whiteQueen = new Queen(Color.WHITE);
    public static final Knight whiteKnight = new Knight(Color.WHITE);
    public static final King whiteKing = new King(Color.WHITE);
    public static Board mockBoard = InitBoardInitializer.getBoard();
}
