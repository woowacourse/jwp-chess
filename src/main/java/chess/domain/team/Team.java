package chess.domain.team;

import static chess.domain.piece.Bishop.SCORE_BISHOP;
import static chess.domain.piece.King.SCORE_KING;
import static chess.domain.piece.Knight.SCORE_KNIGHT;
import static chess.domain.piece.Pawn.DIRECTION_BLACK;
import static chess.domain.piece.Pawn.DIRECTION_WHITE;
import static chess.domain.piece.Pawn.SCORE_PAWN;
import static chess.domain.piece.Queen.SCORE_QUEEN;
import static chess.domain.piece.Rook.SCORE_ROOK;

import chess.domain.Position;
import chess.domain.piece.Bishop;
import chess.domain.piece.King;
import chess.domain.piece.Knight;
import chess.domain.piece.Pawn;
import chess.domain.piece.Piece;
import chess.domain.piece.Queen;
import chess.domain.piece.Rook;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public abstract class Team {
    private static final Map<Piece, Double> scoreByPiece = new HashMap<>();
    private static final int CHESS_SIZE = 8;

    private Team enemy;
    private final String name;
    private boolean isCurrentTurn;

    static {
        scoreByPiece.put(new King(), SCORE_KING);
        scoreByPiece.put(new Queen(), SCORE_QUEEN);
        scoreByPiece.put(new Knight(), SCORE_KNIGHT);
        scoreByPiece.put(new Bishop(), SCORE_BISHOP);
        scoreByPiece.put(new Rook(), SCORE_ROOK);
        scoreByPiece.put(new Pawn(DIRECTION_WHITE), SCORE_PAWN);
        scoreByPiece.put(new Pawn(DIRECTION_BLACK), SCORE_PAWN);
    }

    protected final Map<Position, Piece> piecePosition;

    protected Team(String name) {
        this(name, false, new HashMap<>());
    }

    protected Team(String name, boolean isCurrentTurn) {
        this(name, isCurrentTurn, new HashMap<>());
    }

    protected Team(String name, boolean isCurrentTurn, Map<Position, Piece> piecePosition) {
        this.name = name;
        this.isCurrentTurn = isCurrentTurn;
        this.piecePosition = new HashMap<>(piecePosition);
    }

    public static Team of(final String name, final boolean isTurn) {
        if (WhiteTeam.DEFAULT_NAME.equals(name)) {
            return new WhiteTeam(isTurn);
        }
        return new BlackTeam(isTurn);
    }

    protected void initializePawn(final int pawnColumn, final int pawnDirection) {
        for (int i = 0; i < CHESS_SIZE; i++) {
            piecePosition.put(new Position(i, pawnColumn), new Pawn(pawnDirection));
        }
    }

    protected void initializePiece(final int pieceColumn) {
        piecePosition.put(new Position(0, pieceColumn), new Rook());
        piecePosition.put(new Position(1, pieceColumn), new Knight());
        piecePosition.put(new Position(2, pieceColumn), new Bishop());
        piecePosition.put(new Position(3, pieceColumn), new Queen());
        piecePosition.put(new Position(4, pieceColumn), new King());
        piecePosition.put(new Position(5, pieceColumn), new Bishop());
        piecePosition.put(new Position(6, pieceColumn), new Knight());
        piecePosition.put(new Position(7, pieceColumn), new Rook());
    }

    public void move(final Position current, final Position destination) {
        final Piece chosenPiece = piecePosition.get(current);
        piecePosition.remove(current);
        piecePosition.put(destination, chosenPiece);
    }

    public Piece choosePiece(final Position position) {
        if (havePiece(position)) {
            return piecePosition.get(position);
        }
        throw new IllegalArgumentException("해당 위치에 기물이 없습니다.");
    }

    public Piece killPiece(Position destination) {
        return piecePosition.remove(destination);
    }

    public boolean havePiece(final Position position) {
        return piecePosition.containsKey(position);
    }

    public Map<Position, Piece> getPiecePosition() {
        return new HashMap<>(piecePosition);
    }

    final public void setEnemy(Team enemy) {
        this.enemy = enemy;
    }

    public Team getEnemy() {
        return enemy;
    }

    public String getName() {
        return name;
    }

    public void startTurn() {
        this.isCurrentTurn = true;
    }

    public void endTurn() {
        this.isCurrentTurn = false;
    }

    public boolean isCurrentTurn() {
        return isCurrentTurn;
    }

    public double calculateTotalScore() {
        double totalScore = 0;
        for (int x = 0; x < CHESS_SIZE; x++) {
            List<Piece> pieces = getPiecesInYaxis(x);
            totalScore += calculateScore(pieces);
        }
        return totalScore;
    }

    private List<Piece> getPiecesInYaxis(int x) {
        return piecePosition.keySet().stream()
                .filter(position -> x == position.getX())
                .map(piecePosition::get)
                .collect(Collectors.toList());
    }

    private double calculateScore(final List<Piece> pieces) {
        final double scoreWithoutPawn = calculateScoreByIsPawn(pieces, false);
        final double pawnScore = calculateScoreByIsPawn(pieces, true);
        if (pawnScore > SCORE_PAWN) {
            return scoreWithoutPawn + (pawnScore / 2.0);
        }
        return scoreWithoutPawn + pawnScore;
    }

    private double calculateScoreByIsPawn(final List<Piece> pieces, final boolean isPawn) {
        return pieces.stream()
                .filter(piece -> piece.isPawn() == isPawn)
                .mapToDouble(scoreByPiece::get)
                .sum();
    }
}
