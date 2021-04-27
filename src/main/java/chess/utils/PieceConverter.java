package chess.utils;

import chess.dao.dto.PieceDto;
import chess.domain.location.Location;
import chess.domain.piece.Bishop;
import chess.domain.piece.King;
import chess.domain.piece.Knight;
import chess.domain.piece.Pawn;
import chess.domain.piece.Piece;
import chess.domain.piece.Queen;
import chess.domain.piece.Rook;
import chess.domain.team.Team;
import chess.dto.piece.PieceResponseDto;

public class PieceConverter {

    // TODO : 추후에 삭제 하고 패키지 이동
    public static Piece run(final PieceResponseDto pieceResponseDto) {
        final char pieceLetter = pieceResponseDto.getShape().getValue();
        final Team team = pieceResponseDto.getColor();
        final Location location = Location.of(pieceResponseDto.getX(), pieceResponseDto.getY());
        return createPiece(0L, pieceLetter, location, team);
    }

    public static Piece run(final PieceDto pieceDto) {
        final long id = pieceDto.getId();
        final char pieceLetter = pieceDto.getShape();
        final Team team = Team.from(pieceDto.getColor());
        final Location location = Location.of(pieceDto.getX(), pieceDto.getY());
        return createPiece(id, pieceLetter, location, team);
    }

    private static Piece createPiece(final long id, final char pieceLetter, final Location location,
        final Team team) {

        switch (pieceLetter) {
            case 'k':
                return King.of(id, location, team);
            case 'q':
                return Queen.of(id, location, team);
            case 'b':
                return Bishop.of(id, location, team);
            case 'n':
                return Knight.of(id, location, team);
            case 'r':
                return Rook.of(id, location, team);
            default:
                return Pawn.of(id, location, team);
        }
    }

}
