package chess.dao;

import chess.domain.position.Position;
import chess.service.dto.PieceDto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FakePieceDao implements PieceDao {

    private final Map<String, PieceDto> pieces = new HashMap<>();

    @Override
    public void remove(final int id, final Position position) {
        String positionName = position.getName();
        pieces.remove(positionName);
    }

    @Override
    public void removeAll(final int id) {
        pieces.clear();
    }

    @Override
    public void saveAll(final int id, final List<PieceDto> pieceDtos) {
        for (PieceDto pieceDto : pieceDtos) {
            save(id, pieceDto);
        }
    }

    @Override
    public void save(final int id, final PieceDto pieceDto) {
        pieces.put(pieceDto.getPosition(), pieceDto);
    }

    @Override
    public List<PieceDto> findAll(final int id) {
        return new ArrayList<>(pieces.values());
    }

    @Override
    public void modifyPosition(final int id, final Position source, final Position target) {
        PieceDto pieceDto = pieces.get(source.getName());
        pieceDto.setPosition(target.getName());
        pieces.remove(source.getName());
        pieces.put(target.getName(), pieceDto);
    }
}
