package chess.service;

import chess.dao.GameRepository;
import chess.domain.pieces.Color;
import chess.domain.pieces.Piece;
import chess.domain.position.Position;
import chess.entities.GameEntity;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;

@Service
public final class GameService {

    private final GameRepository gameRepository;

    public GameService(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    public List<GameEntity> findAllBoard() {
        return gameRepository.findAll();
    }

    public GameEntity createBoard(final GameEntity board) {
        return gameRepository.save(board);
    }

    public GameEntity getBoard(int roomId) {
        return gameRepository.getRoomById(roomId);
    }

    public Map<String, Piece> getPieces(int roomId) {
        return gameRepository.getPiecesByRoomId(roomId);
    }

    public void move(final int roomId, final Position sourceRawPosition, final Position targetRawPosition) {
        GameEntity gameEntity = gameRepository.getRoomById(roomId);
        gameEntity.move(sourceRawPosition, targetRawPosition);
        gameRepository.update(gameEntity, sourceRawPosition, targetRawPosition);
    }

    public Map<String, Double> status(int roomId) {
        GameEntity board = gameRepository.getRoomById(roomId);
        return Map.of(Color.WHITE.name(), board.calculateScore(Color.WHITE),
                Color.BLACK.name(), board.calculateScore(Color.BLACK));
    }

    public boolean isEnd(int roomId) {
        GameEntity board = gameRepository.getRoomById(roomId);
        return board.isEnd();
    }

    public boolean delete(int roomId, String password) {
        return gameRepository.deleteByRoomIdAndPassword(roomId, password);
    }
}
