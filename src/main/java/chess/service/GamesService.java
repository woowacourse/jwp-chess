package chess.service;

import java.util.List;

import org.springframework.stereotype.Service;

import chess.dto.ChessGameDto;
import chess.entity.ChessGameEntity;
import chess.model.state.State;
import chess.repository.GamesRepository;

@Service
public class GamesService {

    private final GamesRepository gamesRepository;

    public GamesService(final GamesRepository gamesRepository) {
        this.gamesRepository = gamesRepository;
    }

    public void create(ChessGameDto chessGameDto) {
        gamesRepository.save(chessGameDto);
    }

    public List<ChessGameEntity> loadGames() {
        return gamesRepository.getGames();
    }

    public void delete(Long id) {
        gamesRepository.delete(id);
    }

    public boolean checkGamePassword(Long id, String password) {
        ChessGameEntity chessGameEntity = gamesRepository.getGame(id);
        return password.equals(chessGameEntity.getPassword());
    }

    public boolean checkGameState(Long id) {
        State state = gamesRepository.getState(id);
        return state.isFinished();
    }
}
