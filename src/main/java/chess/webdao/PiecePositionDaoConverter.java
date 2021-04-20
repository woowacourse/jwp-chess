package chess.webdao;

import chess.domain.Position;
import chess.domain.piece.Piece;
import chess.webdto.PieceDto;

import java.util.HashMap;
import java.util.Map;

public class PiecePositionDaoConverter {
    private static final String FIELD_SEPARATOR = ",";
    private static final String PIECE_SEPARATOR = "/";

    private PiecePositionDaoConverter() {
    }

    public static String asDAO(final Map<Position, Piece> teamPiecePosition) {
        StringBuilder pieceInfo = new StringBuilder();
        for (Position position : teamPiecePosition.keySet()) {
            pieceInfo.append(position.getPositionInitial());
            pieceInfo.append(FIELD_SEPARATOR);
            final Piece piece = teamPiecePosition.get(position);
            pieceInfo.append(PieceDto.convert(piece));
            pieceInfo.append(FIELD_SEPARATOR);
            pieceInfo.append(Boolean.valueOf(piece.isFirstMove()));
            pieceInfo.append(PIECE_SEPARATOR);
        }
        return pieceInfo.toString();
    }

    public static Map<Position, Piece> asPiecePosition(final String teamPieceInfo, final String team) {
        final Map<Position, Piece> piecePosition = new HashMap<>();
        final String[] teamPieceInfos = teamPieceInfo.split(PIECE_SEPARATOR);
        for (String singlePieceInfo : teamPieceInfos) {
            final String[] SinglePieceInfos = singlePieceInfo.split(FIELD_SEPARATOR);
            final Position position = Position.of(SinglePieceInfos[0]);
            final Piece piece = DaoToPiece.generatePiece(team, SinglePieceInfos[1], Boolean.valueOf(SinglePieceInfos[2]));
            piecePosition.put(position, piece);
        }
        return piecePosition;
    }

}
