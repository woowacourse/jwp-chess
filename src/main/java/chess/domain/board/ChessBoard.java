package chess.domain.board;

import static chess.domain.piece.PieceTeam.EMPTY;

import chess.constant.TargetType;
import chess.domain.board.position.File;
import chess.domain.board.position.Position;
import chess.domain.board.position.Rank;
import chess.domain.piece.EmptySpace;
import chess.domain.piece.Piece;
import chess.domain.piece.PieceTeam;
import chess.exception.IncorrectTeamSelectionException;
import chess.exception.NonMovableException;
import chess.domain.gameflow.GameFlow;
import java.util.Arrays;
import java.util.Map;
import java.util.function.Predicate;

public class ChessBoard {

    static final String SOURCE_POSITION_SHOULD_HAVE_PIECE_MESSAGE = "[ERROR] 출발 위치에는 말이 있어야 합니다.";
    private static final EmptySpace EMPTY_SPACE = new EmptySpace(EMPTY);

    private final Map<Position, Piece> board;
    private final GameFlow gameFlow;

    public ChessBoard(Map<Position, Piece> board, GameFlow gameFlow) {
        this.board = board;
        this.gameFlow = gameFlow;
    }

    public void movePiece(Position source, Position target) {
        validateWhiteBlackTeamTurn(source);
        validateSourceNotEmpty(source);
        boolean isGameFinished = isTargetKing(target);
        changePieces(source, target);
        gameFlow.nextState(isGameFinished);
    }

    public double calculateScoreByGameFlow() {
        return calculateScore(gameFlow::isCorrectTurn, gameFlow.currentPieceTeam());
    }

    public double calculateScoreByTeam(PieceTeam pieceTeam) {
        return calculateScore(piece -> piece.isPieceTeam(pieceTeam), pieceTeam);
    }

    private double calculateScore(Predicate<Piece> condition, PieceTeam pieceTeam) {
        return board.values()
                .stream()
                .filter(condition)
                .mapToDouble(Piece::getScore)
                .sum() - adjustPawnScoreByTeam(pieceTeam);
    }

    public boolean isGamePlaying() {
        return gameFlow.isRunning();
    }

    private boolean isTargetKing(Position position) {
        return board.get(position).isKing();
    }

    private void validateWhiteBlackTeamTurn(Position position) {
        if (!gameFlow.isCorrectTurn(board.get(position))) {
            throw new IncorrectTeamSelectionException();
        }
    }

    private void changePieces(Position sourcePosition, Position targetPosition) {
        Piece sourcePiece = board.get(sourcePosition);
        Piece targetPiece = board.get(targetPosition);

        validateMovable(sourcePosition, targetPosition, sourcePiece, targetPiece);

        board.put(targetPosition, sourcePiece);
        board.put(sourcePosition, EMPTY_SPACE);
    }

    private void validateMovable(Position from, Position to, Piece sourcePiece, Piece targetPiece) {
        TargetType targetType = decideMoveType(targetPiece);
        if (!sourcePiece.isMovable(from, to, targetType) ||
                isBlocked(from, to) ||
                targetPiece.isMyTeam(sourcePiece)) {
            throw new NonMovableException();
        }
    }

    private TargetType decideMoveType(Piece piece) {
        if (piece.equals(EMPTY_SPACE)) {
            return TargetType.EMPTY;
        }
        if (gameFlow.isCorrectTurn(piece)) {
            return TargetType.FRIENDLY;
        }
        return TargetType.ENEMY;
    }

    private boolean isBlocked(Position sourcePosition, Position targetPosition) {
        if (board.get(sourcePosition).isKnight()) {
            return false;
        }

        return sourcePosition.findPositionsToMove(targetPosition)
                .stream()
                .anyMatch(position -> !isEmpty(position));
    }

    private boolean isEmpty(Position position) {
        return board.get(position).equals(EMPTY_SPACE);
    }

    private void validateSourceNotEmpty(Position position) {
        if (isEmpty(position)) {
            throw new IllegalArgumentException(SOURCE_POSITION_SHOULD_HAVE_PIECE_MESSAGE);
        }
    }

    private double adjustPawnScoreByTeam(PieceTeam pieceTeam) {
        return File.stream()
                .map(file -> duplicatePieceCountByRankAndTeam(file, pieceTeam))
                .filter(count -> count >= 2)
                .mapToDouble(point -> point * 0.5)
                .sum();
    }

    private long duplicatePieceCountByRankAndTeam(File file, PieceTeam pieceTeam) {
        return Arrays.stream(Rank.values())
                .map(rank -> Position.of(file, rank))
                .filter(position -> {
                    Piece piece = board.get(position);
                    return piece.isPawn() && piece.isPieceTeam(pieceTeam);
                }).count();
    }

    public String currentStateName() {
        return gameFlow.currentStateName();
    }

    public Map<Position, Piece> getBoard() {
        return board;
    }
}
