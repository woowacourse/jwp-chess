package chess.model.board;

import chess.dto.ScoreResult;
import chess.model.piece.*;
import chess.model.square.File;
import chess.model.square.Rank;
import chess.model.square.Square;
import chess.model.status.Ready;
import chess.model.status.Status;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class Board {

    private static final int LINE_RANGE = 8;
    private static final int KING_COUNT = 2;
    private static final int PROPER_KING_COUNT = 2;

    private Map<Square, Piece> board;
    private Status status;
    private int id;
    private Team team;

    public Board() {
        this.status = new Ready();
        this.board = new HashMap<>();
        final List<File> files = Arrays.asList(File.values());

        initMajorPieces(Team.WHITE, Rank.ONE, files);
        initMajorPieces(Team.BLACK, Rank.EIGHT, files);
        initPawns(Team.WHITE, Rank.TWO, files);
        initPawns(Team.BLACK, Rank.SEVEN, files);
        initEmpty();
    }

    public Board(Status status, Team team) {
        this(0, status, team);
    }

    public Board(int id, Status status, Team team) {
        this.id = id;
        this.status = status;
        this.team = team;
    }

    public Board(Map<Square, Piece> allSquaresAndPieces) {
        this.board = allSquaresAndPieces;
    }

    public void move(String source, String target) {
        Square sourceSquare = Square.fromString(source);
        Square targetSquare = Square.fromString(target);
        Piece piece = board.get(sourceSquare);
        Piece targetPiece = board.get(targetSquare);

        if (!piece.movable(targetPiece, sourceSquare, targetSquare)) {
            throw new IllegalArgumentException("해당 위치로 움직일 수 없습니다.");
        }

        List<Square> route = piece.getRoute(sourceSquare, targetSquare);
        if (piece.isPawn() && !route.isEmpty() && !piece.isNotAlly(targetPiece)) {
            throw new IllegalArgumentException("같은 팀이 있는 곳으로 갈 수 없습니다.");
        }

        checkMoveWithoutObstacle(route, piece, targetPiece);
        moveTo(sourceSquare, targetSquare, piece);
    }

    public void checkTurn(Team team, String source) {
        Piece piece = board.get(Square.fromString(source));
        if (!team.isProperTurn(piece.team())) {
            throw new IllegalArgumentException(String.format("현재 %s팀의 차례가 아닙니다.", piece.team().name()));
        }
    }

    private void checkMoveWithoutObstacle(List<Square> route, Piece sourcePiece, Piece targetPiece) {
        for (Square square : route) {
            Piece piece = board.get(square);
            if (piece.equals(targetPiece) && sourcePiece.isNotAlly(targetPiece)) {
                return;
            }
            if (piece.isNotEmpty()) {
                throw new IllegalArgumentException("경로 중 기물이 있습니다.");
            }
        }
    }

    private void moveTo(Square sourceSquare, Square targetSquare, Piece piece) {
        board.put(targetSquare, piece);
        board.put(sourceSquare, new Empty());
    }

    private void checkMovable(Square sourceSquare, Square targetSquare, Piece piece) {
        if (!piece.movable(this, sourceSquare, targetSquare)) {
            throw new IllegalArgumentException("해당 위치로 움직일 수 없습니다.");
        }
        if (!piece.canMoveWithoutObstacle(this, sourceSquare, targetSquare)) {
            throw new IllegalArgumentException("경로 중 기물이 있습니다.");
        }
    }

    public ScoreResult calculateScore() {
        return ScoreResult.from(board);
    }

    private void initEmpty() {
        for (Rank rank : Rank.values()) {
            fillSquareByFile(rank);
        }
    }

    private void fillSquareByFile(Rank rank) {
        for (File file : File.values()) {
            Square square = Square.of(file, rank);
            checkEmpty(square);
        }
    }

    private void checkEmpty(Square square) {
        if (!board.containsKey(square)) {
            board.put(square, new Empty());
        }
    }

    private void initPawns(Team team, Rank rank, List<File> files) {
        for (int i = 0; i < LINE_RANGE; i++) {
            board.put(Square.of(files.get(i), rank), new Pawn(team));
        }
    }

    private void initMajorPieces(Team team, Rank rank, List<File> files) {
        List<Piece> majorPiecesLineup = majorPiecesLineup(team);
        for (int i = 0; i < majorPiecesLineup.size(); i++) {
            board.put(Square.of(files.get(i), rank), majorPiecesLineup.get(i));
        }
    }

    private List<Piece> majorPiecesLineup(final Team team) {
        return List.of(
                new Rook(team),
                new Knight(team),
                new Bishop(team),
                new Queen(team),
                new King(team),
                new Bishop(team),
                new Knight(team),
                new Rook(team)
        );
    }

    public Piece get(Square square) {
        return board.get(square);
    }

    public boolean isKingDead() {
        long kingCount = board.values().stream()
                .filter(Piece::isKing)
                .count();
        return kingCount != PROPER_KING_COUNT;
    }

    public int getId() {
        return id;
    }

    public Status getStatus() {
        return status;
    }

    public Team getTeam() {
        return team;
    }
}
