package chess.model.domain.board;

import chess.model.domain.piece.Bishop;
import chess.model.domain.piece.King;
import chess.model.domain.piece.Knight;
import chess.model.domain.piece.Piece;
import chess.model.domain.piece.Queen;
import chess.model.domain.piece.Rook;
import chess.model.domain.piece.Team;
import chess.model.domain.state.MoveInfo;
import chess.model.domain.state.MoveOrder;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public enum CastlingSetting {

    WHITE_ROOK_LEFT_BEFORE(Square.of("a1"), Rook.getPieceInstance(Team.WHITE), true),
    WHITE_KING_BEFORE(Square.of("e1"), King.getPieceInstance(Team.WHITE), true),
    WHITE_ROOK_RIGHT_BEFORE(Square.of("h1"), Rook.getPieceInstance(Team.WHITE), true),

    BLACK_ROOK_LEFT_BEFORE(Square.of("h8"), Rook.getPieceInstance(Team.BLACK), true),
    BLACK_ROOK_RIGHT_BEFORE(Square.of("a8"), Rook.getPieceInstance(Team.BLACK), true),
    BLACK_KING_BEFORE(Square.of("e8"), King.getPieceInstance(Team.BLACK), true),

    WHITE_KING_RIGHT_AFTER(Square.of("g1"), Knight.getPieceInstance(Team.WHITE), false),
    BLACK_KING_LEFT_AFTER(Square.of("g8"), Knight.getPieceInstance(Team.BLACK), false),
    WHITE_KING_LEFT_AFTER(Square.of("c1"), Bishop.getPieceInstance(Team.WHITE), false),
    BLACK_KING_RIGHT_AFTER(Square.of("c8"), Bishop.getPieceInstance(Team.BLACK), false),

    WHITE_ROOK_RIGHT_AFTER(Square.of("f1"), Bishop.getPieceInstance(Team.WHITE), false),
    BLACK_ROOK_LEFT_AFTER(Square.of("f8"), Bishop.getPieceInstance(Team.BLACK), false),
    WHITE_ROOK_LEFT_AFTER(Square.of("d1"), Queen.getPieceInstance(Team.WHITE), false),
    BLACK_ROOK_RIGHT_AFTER(Square.of("d8"), Queen.getPieceInstance(Team.BLACK), false);

    private static final Set<Map<String, CastlingSetting>> TOTALS;
    private static final String KEYS_KING_BEFORE = "KING_BEFORE";
    private static final String KEYS_KING_AFTER = "KING_AFTER";
    private static final String KEYS_ROOK_BEFORE = "ROOK_BEFORE";
    private static final String KEYS_ROOK_AFTER = "ROOK_AFTER";

    static {
        Map<String, CastlingSetting> blackLeft = new HashMap<>();
        blackLeft.put(KEYS_KING_BEFORE, BLACK_KING_BEFORE);
        blackLeft.put(KEYS_KING_AFTER, BLACK_KING_LEFT_AFTER);
        blackLeft.put(KEYS_ROOK_BEFORE, BLACK_ROOK_LEFT_BEFORE);
        blackLeft.put(KEYS_ROOK_AFTER, BLACK_ROOK_LEFT_AFTER);

        Map<String, CastlingSetting> blackRight = new HashMap<>();
        blackRight.put(KEYS_KING_BEFORE, BLACK_KING_BEFORE);
        blackRight.put(KEYS_KING_AFTER, BLACK_KING_RIGHT_AFTER);
        blackRight.put(KEYS_ROOK_BEFORE, BLACK_ROOK_RIGHT_BEFORE);
        blackRight.put(KEYS_ROOK_AFTER, BLACK_ROOK_RIGHT_AFTER);

        Map<String, CastlingSetting> whiteLeft = new HashMap<>();
        whiteLeft.put(KEYS_KING_BEFORE, WHITE_KING_BEFORE);
        whiteLeft.put(KEYS_KING_AFTER, WHITE_KING_LEFT_AFTER);
        whiteLeft.put(KEYS_ROOK_BEFORE, WHITE_ROOK_LEFT_BEFORE);
        whiteLeft.put(KEYS_ROOK_AFTER, WHITE_ROOK_LEFT_AFTER);

        Map<String, CastlingSetting> whiteRight = new HashMap<>();
        whiteRight.put(KEYS_KING_BEFORE, WHITE_KING_BEFORE);
        whiteRight.put(KEYS_KING_AFTER, WHITE_KING_RIGHT_AFTER);
        whiteRight.put(KEYS_ROOK_BEFORE, WHITE_ROOK_RIGHT_BEFORE);
        whiteRight.put(KEYS_ROOK_AFTER, WHITE_ROOK_RIGHT_AFTER);

        Set<Map<String, CastlingSetting>> totals = new HashSet<>();
        totals.add(blackLeft);
        totals.add(blackRight);
        totals.add(whiteLeft);
        totals.add(whiteRight);
        TOTALS = Collections.unmodifiableSet(new HashSet<>(totals));
    }

    private final Square square;
    private final Piece piece;
    private final boolean castlingPiece;

    CastlingSetting(Square square, Piece piece, boolean castlingPiece) {
        this.square = square;
        this.piece = piece;
        this.castlingPiece = castlingPiece;
    }

    public static MoveInfo getMoveCastlingRook(MoveInfo moveInfo) {
        Square moveSquareAfter = moveInfo.get(MoveOrder.TO);
        Map<String, CastlingSetting> selectCastling = TOTALS.stream()
            .filter(total -> moveSquareAfter == total.get(KEYS_KING_AFTER).square)
            .findFirst()
            .orElseThrow(IllegalAccessError::new);
        return new MoveInfo(selectCastling.get(KEYS_ROOK_BEFORE).square,
            selectCastling.get(KEYS_ROOK_AFTER).square);
    }

    public static boolean canCastling(Set<CastlingSetting> elements, MoveInfo moveInfo) {
        return TOTALS.stream()
            .filter(total -> elements.contains(total.get(KEYS_KING_BEFORE)))
            .filter(total -> elements.contains(total.get(KEYS_ROOK_BEFORE)))
            .filter(total -> total.get(KEYS_KING_BEFORE).square
                == moveInfo.get(MoveOrder.FROM))
            .anyMatch(total -> total.get(KEYS_KING_AFTER).square
                == moveInfo.get(MoveOrder.TO));
    }

    public static CastlingSetting of(String castlingSettingName) {
        return Arrays.stream(CastlingSetting.values())
            .filter(castlingSetting -> castlingSetting.name().equalsIgnoreCase(castlingSettingName))
            .findFirst()
            .orElseThrow(IllegalArgumentException::new);
    }

    public static CastlingSetting of(Square boardsquare, Piece piece) {
        return Arrays.stream(CastlingSetting.values())
            .filter(castlingSetting -> castlingSetting.square == boardsquare)
            .filter(castlingSetting -> castlingSetting.piece == piece)
            .findFirst()
            .orElseThrow(IllegalArgumentException::new);
    }

    public static Set<CastlingSetting> getCastlingElements() {
        return Arrays.stream(CastlingSetting.values())
            .filter(castlingElement -> castlingElement.castlingPiece)
            .collect(Collectors.toSet());
    }

    public static Set<Square> getCastlingMovableAreas(
        Set<CastlingSetting> castlingElements) {
        return Collections.unmodifiableSet(TOTALS.stream()
            .filter(total -> castlingElements.contains(total.get(KEYS_ROOK_BEFORE)))
            .filter(total -> castlingElements.contains(total.get(KEYS_KING_BEFORE)))
            .map(total -> total.get(KEYS_KING_AFTER).square)
            .collect(Collectors.toSet()));
    }

    public Piece getPiece() {
        return piece;
    }

    public boolean isEqualSquare(Square square) {
        return this.square.equals(square);
    }

    public boolean isSameColor(Piece piece) {
        return this.piece.isSameTeam(piece);
    }

    public boolean isContains(Square moveSquare) {
        return this.square == moveSquare;
    }

    public boolean isCastlingBefore(Square square, Piece piece) {
        return Arrays.stream(CastlingSetting.values())
            .filter(castlingSetting -> castlingSetting.square == square)
            .filter(castlingSetting -> castlingSetting.piece == piece)
            .findFirst()
            .map(castlingSetting -> castlingSetting.castlingPiece)
            .orElse(false);
    }
}
