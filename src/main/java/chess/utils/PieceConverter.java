package chess.utils;

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

    public static Piece run(final PieceResponseDto pieceResponseDto) {
        final char pieceLetter = pieceResponseDto.getShape().getValue();
        final Team team = pieceResponseDto.getColor();
        final Location location = Location.of(pieceResponseDto.getX(), pieceResponseDto.getY());
        return createPiece(pieceLetter, location, team);
    }

    private static Piece createPiece(final char pieceLetter, final Location location,
        final Team team) {

        switch (pieceLetter) {
            case 'k':
                return King.of(location, team);
            case 'q':
                return Queen.of(location, team);
            case 'b':
                return Bishop.of(location, team);
            case 'n':
                return Knight.of(location, team);
            case 'r':
                return Rook.of(location, team);
            default:
                return Pawn.of(location, team);
        }
    }

}
