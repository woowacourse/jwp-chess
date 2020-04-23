package spring.chess.piece.type;

import spring.chess.team.Team;

import java.util.HashMap;
import java.util.Map;

public class PieceMapper {
    private static final Map<Character, Piece> pieces = new HashMap<>();

    static {
        pieces.put('p', new Pawn(Team.WHITE));
        pieces.put('r', new Rook(Team.WHITE));
        pieces.put('n', new Knight(Team.WHITE));
        pieces.put('b', new Bishop(Team.WHITE));
        pieces.put('q', new Queen(Team.WHITE));
        pieces.put('k', new King(Team.WHITE));

        pieces.put('P', new Pawn(Team.BLACK));
        pieces.put('R', new Rook(Team.BLACK));
        pieces.put('N', new Knight(Team.BLACK));
        pieces.put('B', new Bishop(Team.BLACK));
        pieces.put('Q', new Queen(Team.BLACK));
        pieces.put('K', new King(Team.BLACK));
    }

    public static Piece of(char name) {
        return pieces.get(name);
    }
}
