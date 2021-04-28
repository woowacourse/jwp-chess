package chess.domain.game.utils;

import chess.dao.dto.PieceDto;
import chess.domain.game.board.piece.Bishop;
import chess.domain.game.board.piece.King;
import chess.domain.game.board.piece.Knight;
import chess.domain.game.board.piece.Pawn;
import chess.domain.game.board.piece.Piece;
import chess.domain.game.board.piece.Queen;
import chess.domain.game.board.piece.Rook;
import chess.domain.game.board.piece.location.Location;
import chess.domain.game.team.Team;

public class PieceConverter {

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
