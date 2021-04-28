package chess.utils;

import chess.domain.game.board.Board;
import chess.domain.game.board.piece.Bishop;
import chess.domain.game.board.piece.King;
import chess.domain.game.board.piece.Knight;
import chess.domain.game.board.piece.Pawn;
import chess.domain.game.board.piece.Piece;
import chess.domain.game.board.piece.Queen;
import chess.domain.game.board.piece.Rook;
import chess.domain.game.board.piece.location.Location;
import chess.domain.game.team.Team;
import java.util.ArrayList;
import java.util.List;

public class BoardUtil {

    private final static int BOARD_SIZE = 8;
    private final static int DEFAULT_POSITION = 1;
    private final static char EMPTY = '.';

    private static final char[][] INITIAL_BOARD = {
        {'R', 'N', 'B', 'Q', 'K', 'B', 'N', 'R'},
        {'P', 'P', 'P', 'P', 'P', 'P', 'P', 'P'},
        {'.', '.', '.', '.', '.', '.', '.', '.'},
        {'.', '.', '.', '.', '.', '.', '.', '.'},
        {'.', '.', '.', '.', '.', '.', '.', '.'},
        {'.', '.', '.', '.', '.', '.', '.', '.'},
        {'p', 'p', 'p', 'p', 'p', 'p', 'p', 'p'},
        {'r', 'n', 'b', 'q', 'k', 'b', 'n', 'r'}
    };

    private BoardUtil() {
    }

    public static Board generateInitialBoard() {
        return generateBoard(INITIAL_BOARD);
    }

    public static Board generateBoard(char[][] board) {
        List<Piece> pieces = new ArrayList<>();
        for (int y = 0; y < BOARD_SIZE; y++) {
            generateColumn(board[y], pieces, y);
        }
        return Board.of(pieces);
    }

    private static void generateColumn(char[] chars, List<Piece> pieces, int y) {
        for (int x = 0; x < BOARD_SIZE; x++) {
            char pieceLetter = chars[x];
            addIfNotEmpty(pieces, y, x, pieceLetter);
        }
    }

    private static void addIfNotEmpty(List<Piece> pieces, int y, int x, char pieceLetter) {
        if (isNotEmpty(pieceLetter)) {
            pieces.add(generatePiece(pieceLetter, x, y));
        }
    }

    private static boolean isNotEmpty(char pieceLetter) {
        return pieceLetter != EMPTY;
    }

    private static Piece generatePiece(char pieceLetter, int x, int y) {
        Location convertedLocation = convertLocation(x, y);
        Team team = findOutTeam(pieceLetter);
        switch (Character.toLowerCase(pieceLetter)) {
            case 'r':
                return Rook.of(0L, convertedLocation, team);
            case 'n':
                return Knight.of(0L, convertedLocation, team);
            case 'b':
                return Bishop.of(0L, convertedLocation, team);
            case 'q':
                return Queen.of(0L, convertedLocation, team);
            case 'k':
                return King.of(0L, convertedLocation, team);
            default:
                return Pawn.of(0L, convertedLocation, team);
        }
    }

    private static Location convertLocation(int x, int y) {
        int transX = x + DEFAULT_POSITION;
        int transY = BOARD_SIZE - y;
        return Location.of(transX, transY);
    }

    private static Team findOutTeam(char pieceLetter) {
        if (Character.isLowerCase(pieceLetter)) {
            return Team.WHITE;
        }
        return Team.BLACK;
    }

}
