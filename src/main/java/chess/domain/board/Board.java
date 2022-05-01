package chess.domain.board;

import static chess.domain.piece.Team.WHITE;

import chess.domain.board.position.Position;
import chess.domain.piece.Bishop;
import chess.domain.piece.King;
import chess.domain.piece.Knight;
import chess.domain.piece.Pawn;
import chess.domain.piece.Piece;
import chess.domain.piece.Queen;
import chess.domain.piece.Rook;
import chess.domain.piece.Team;
import chess.dto.PieceDto;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Board {

    private static final Map<String, Function<Team, Piece>> PIECE_CREATION_STRATEGY_BY_NAME =
            Map.of("Pawn", Pawn::new, "King", King::new, "Queen", Queen::new,
                    "Rook", Rook::new, "Knight", Knight::new, "Bishop", Bishop::new);

    private final Map<Position, Piece> pieces;
    private final Team currentTurnTeam;

    public Board(final Map<Position, Piece> pieces, final Team currentTurnTeam) {
        this.pieces = pieces;
        this.currentTurnTeam = currentTurnTeam;
    }

    public Board() {
        this(new PieceFactory()
                .generateInitialPieces(), WHITE);
    }

    public static Map<Position, Piece> convertToPiece(final List<PieceDto> savedPieces) {
        final Map<Position, Piece> pieces = new HashMap<>();
        for (PieceDto pieceDto : savedPieces) {
            Position position = Position.from(pieceDto.getPosition());
            Team team = Team.from(pieceDto.getTeam());
            String name = pieceDto.getName();
            Piece piece = PIECE_CREATION_STRATEGY_BY_NAME.get(name)
                    .apply(team);
            pieces.put(position, piece);
        }
        return pieces;
    }

    public Board movePiece(final Position sourcePosition, final Position targetPosition) {
        final Piece sourcePiece = pieces.get(sourcePosition);

        validateGameIsOver();
        validateTurn(sourcePiece);
        validateSameTeamTargetPositionPiece(sourcePiece, targetPosition);
        validateMovement(sourcePosition, targetPosition);

        Map<Position, Piece> movedPieces = new HashMap<>(pieces);
        movedPieces.remove(sourcePosition);
        movedPieces.put(targetPosition, sourcePiece);
        return new Board(movedPieces, currentTurnTeam.turnToNext());
    }

    private void validateGameIsOver() {
        final int countOfKing = (int) pieces.values()
                .stream()
                .filter(Piece::isKing)
                .count();
        if (countOfKing == 1) {
            throw new IllegalStateException("King이 죽어 게임이 종료되었습니다.");
        }
    }

    private void validateMovement(final Position sourcePosition, final Position targetPosition) {
        final Piece sourcePiece = pieces.get(sourcePosition);
        if (!sourcePiece.canMove(sourcePosition, targetPosition, getOtherPositions(sourcePosition))) {
            throw new IllegalArgumentException("기물을 이동시킬 수 없습니다.");
        }
    }

    private void validateSameTeamTargetPositionPiece(final Piece sourcePiece, final Position targetPosition) {
        final Piece pieceInTargetPosition = pieces.get(targetPosition);
        if (pieceInTargetPosition == null) {
            return;
        }
        if (sourcePiece.isSameTeam(pieceInTargetPosition)) {
            throw new IllegalArgumentException("이동하려는 위치에 같은 팀 기물이 있습니다.");
        }
    }

    private void validateTurn(final Piece sourcePiece) {
        if (!sourcePiece.isTeamOf(currentTurnTeam)) {
            throw new IllegalArgumentException("다른 팀 기물은 이동시킬 수 없습니다.");
        }
    }

    private List<Position> getOtherPositions(final Position sourcePosition) {
        return pieces.keySet()
                .stream()
                .filter(position -> position != sourcePosition)
                .collect(Collectors.toList());
    }

    public double getTotalPoint(Team team) {
        final Map<Position, Piece> teamPieces = pieces.entrySet()
                .stream()
                .filter(entry -> entry.getValue()
                        .isTeamOf(team))
                .collect(Collectors.toMap(Entry::getKey, Entry::getValue));
        return TotalScore.getTotalPoint(teamPieces);
    }

    public Map<Position, Piece> getPieces() {
        return pieces;
    }


    public Team getTurn() {
        return currentTurnTeam;
    }
}
