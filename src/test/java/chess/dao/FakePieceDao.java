package chess.dao;

import chess.domain.board.position.Position;
import chess.domain.piece.Piece;
import chess.dto.PieceDto;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FakePieceDao implements PieceDao {

    private final Map<Integer, FakePieces> values = new HashMap<>();

    @Override
    public List<PieceDto> findPieces(final int roomIndex) {
        final FakePieces fakePieces = values.get(roomIndex);
        return fakePieces.toDtos();
    }

    @Override
    public void saveAllPieces(final int roomId, final Map<Position, Piece> board) {
        final Map<String, FakePiece> pieces = new HashMap<>();
        for (Position position : board.keySet()) {
            final Piece piece = board.get(position);
            final String name = piece.getName();
            final String teamColor = piece.getTeam();
            pieces.put(position.toString(), new FakePiece(name, teamColor));
        }
        values.put(roomId, new FakePieces(pieces));
    }

    @Override
    public void removePiece(final int roomId, final String position) {
        final FakePieces pieces = values.get(roomId);
        pieces.values
                .remove(position);
    }

    @Override
    public void savePiece(final int roomId, final String position, final Piece piece) {
        final FakePieces pieces = values.get(roomId);
        final String name = piece.getName();
        final String teamColor = piece.getTeam();
        pieces.values
                .put(position, new FakePiece(name, teamColor));
    }

    @Override
    public void removeAllPieces(final int roomId) {
        values.put(roomId, new FakePieces(new HashMap<>()));
    }

    class FakePieces {

        private final Map<String, FakePiece> values;

        private FakePieces(final Map<String, FakePiece> values) {
            this.values = values;
        }

        private List<PieceDto> toDtos() {
            final List<PieceDto> dtos = new ArrayList<>();
            for (String position : values.keySet()) {
                FakePiece fakePiece = values.get(position);
                String name = fakePiece.name;
                String teamColor = fakePiece.teamColor;
                dtos.add(new PieceDto(name, position, teamColor));
            }
            return dtos;
        }
    }

    class FakePiece {

        private final String name;
        private final String teamColor;

        private FakePiece(final String name, final String teamColor) {
            this.name = name;
            this.teamColor = teamColor;
        }
    }
}
