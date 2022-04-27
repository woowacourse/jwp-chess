package chess.controller;

import chess.dao.PieceDao;
import chess.domain.board.position.Position;
import chess.domain.piece.Piece;
import chess.dto.PieceDto;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class FakePieceDao implements PieceDao {

    private List<PieceDto> pieces = new ArrayList<>();

    @Override
    public void saveAllPieces(int roomNumber, final Map<Position, Piece> board) {
        for (Position position : board.keySet()) {
            final Piece piece = board.get(position);
            final PieceDto pieceDto = new PieceDto(position.toString(), piece.getTeam(), piece.getName());
            pieces.add(pieceDto);
        }
    }

    @Override
    public List<PieceDto> findAllPieces(int roomNumber) {
        return pieces;
    }

    @Override
    public void removePieceByPosition(int roomNumber, final String position) {
        final Optional<PieceDto> any = pieces.stream()
                .filter(savedPiece -> savedPiece.getPosition().equals(position))
                .findAny();
        any.ifPresent(dto -> pieces.remove(dto));
    }

    @Override
    public void savePiece(int roomNumber, final String position, final Piece piece) {
        PieceDto pieceDto = new PieceDto(position, piece.getTeam(), piece.getName());
        removePieceByPosition(roomNumber, position);
        pieces.add(pieceDto);
    }

    @Override
    public void removeAllPieces(int roomNumber) {
        pieces = new ArrayList<>();
    }
}