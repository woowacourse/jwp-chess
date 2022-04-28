package chess.dao;

import chess.domain.position.Position;
import chess.dto.PieceDto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FakePieceDao implements PieceDao {

    private Map<String, PieceDto> pieces = new HashMap<>();

    @Override
    public void removeByPosition(Position position) {
        String positionName = position.getName();
        pieces.remove(positionName);
    }

    @Override
    public void removeAll() {
        pieces.clear();
    }

    @Override
    public void saveAll(List<PieceDto> pieceDtos) {
        for (PieceDto pieceDto : pieceDtos) {
            save(pieceDto);
        }
    }

    @Override
    public void save(PieceDto pieceDto) {
        pieces.put(pieceDto.getPosition(), pieceDto);
    }

    @Override
    public List<PieceDto> findAll() {
        return new ArrayList<>(pieces.values());
    }

    @Override
    public List<PieceDto> findPiecesById(Long gameId) {
        return new ArrayList<>(pieces.values());
    }

    @Override
    public void updatePosition(Position position, Position updatedPosition) {
        PieceDto pieceDto = pieces.get(position.getName());
        pieceDto.setPosition(updatedPosition.getName());
        pieces.remove(position.getName());
        pieces.put(updatedPosition.getName(), pieceDto);
    }
}
