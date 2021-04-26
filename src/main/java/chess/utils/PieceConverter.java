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
import chess.dto.piece.PieceDto;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PieceConverter {

    public static Piece run(final PieceDto pieceDto) {
        final char pieceLetter = pieceDto.getShape().getValue();
        final Team team = pieceDto.getColor();
        final Location location = Location.of(pieceDto.getX(), pieceDto.getY());
        return createPiece(pieceLetter, location, team);
    }

    public static Piece run(final ResultSet resultSet) throws SQLException {
        final char pieceLetter = resultSet.getString("shape").charAt(0);
        final Team team = Team.from(resultSet.getString("color"));
        final Location location = Location.of(
            resultSet.getInt("x"),
            resultSet.getInt("y")
        );
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
