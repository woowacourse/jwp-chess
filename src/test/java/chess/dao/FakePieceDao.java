package chess.dao;

import chess.controller.dto.response.PieceResponse;
import chess.domain.board.Position;
import chess.domain.piece.Piece;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class FakePieceDao implements PieceDao {

    private final Map<Long, Map<Position, Piece>> pieces;

    public FakePieceDao() {
        this.pieces = new HashMap<>();
    }

    @Override
    public void save(Long gameId, Position position, Piece piece) {
        if (!pieces.containsKey(gameId)) {
            pieces.put(gameId, new HashMap<>());
        }
        pieces.get(gameId).put(position, piece);
    }

    @Override
    public List<PieceResponse> findAll(Long gameId) {
        Map<Position, Piece> piecesInGame = pieces.getOrDefault(gameId, new HashMap<>());
        return piecesInGame.entrySet()
                .stream()
                .map(entry -> new PieceResponse(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Piece> find(Long gameId, Position position) {
        Map<Position, Piece> piecesInGame = pieces.getOrDefault(gameId, new HashMap<>());
        if (piecesInGame.containsKey(position)) {
            return Optional.of(piecesInGame.get(position));
        }
        return Optional.empty();
    }

    @Override
    public void updatePosition(Long gameId, Position start, Position target) {
        Map<Position, Piece> piecesInGame = pieces.getOrDefault(gameId, new HashMap<>());
        Piece piece = piecesInGame.get(start);
        piecesInGame.remove(start);
        piecesInGame.put(target, piece);
    }

    @Override
    public void delete(Long gameId, Position position) {
        pieces.get(gameId).remove(position);
    }
}
