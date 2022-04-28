package chess.service;

import java.util.List;

import org.springframework.stereotype.Service;

import chess.dto.ChessGameDto;
import chess.entity.ChessGameEntity;
import chess.model.state.State;
import chess.repository.GamesRepository;

@Service
public class GamesService {

    private static final String GAME_IS_NOT_FINISHED_ERROR = "[ERROR] 게임이 종료되지 않았습니다.";
    private static final String PASSWORD_NOT_EQUAL_ERROR = "[ERROR] 비밀번호가 틀렸습니다.";

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

    public void delete(Long id, String password) {
        checkGameState(id);
        checkGamePassword(id, password);
        gamesRepository.delete(id);
    }

    public void checkGamePassword(Long id, String password) {
        ChessGameEntity chessGameEntity = gamesRepository.getGame(id);
        if (!password.equals(chessGameEntity.getPassword())) {
            throw new IllegalArgumentException(PASSWORD_NOT_EQUAL_ERROR);
        }
    }

    public void checkGameState(Long id) {
        State state = gamesRepository.getState(id);
        if (!state.isFinished()) {
            throw new IllegalArgumentException(GAME_IS_NOT_FINISHED_ERROR);
        }
    }
}
