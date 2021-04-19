package chess.repository;

import chess.dao.ChessDAO;
import chess.domain.game.ChessGame;
import chess.exception.NoGameIdExistenceException;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Repository
public class GameRepository {

    private final ChessDAO chessDAO;
    private final Map<String, ChessGame> repository = new HashMap<>();

    public GameRepository(ChessDAO chessDAO) {
        this.chessDAO = chessDAO;
    }

    public void saveToCache(String gameId, ChessGame chessGame) {
        repository.put(gameId, chessGame);
    }

    public void saveToDB(String gameId, ChessGame chessGame) {
        chessDAO.addChessGame(
                gameId,
                ChessGameConvertor.chessGameToJson(chessGame)
        );
    }

    public void updateToDB(String gameId, ChessGame chessGame) {
        chessDAO.updateChessGame(
                gameId,
                ChessGameConvertor.chessGameToJson(chessGame)
        );
    }

    public ChessGame findByGameIdFromCache(String gameId) {
        Optional<ChessGame> chessGame = Optional.ofNullable(repository.getOrDefault(gameId, null));

        if (!chessGame.isPresent()) {
            throw new NoGameIdExistenceException();
        }

        return chessGame.get();
    }

    public ChessGame findByGameIdFromDB(String gameId) {
        String chessGameJson = chessDAO.findChessGameByGameId(gameId);

        return ChessGameConvertor.jsonToChessGame(chessGameJson);
    }

    public boolean isGameIdExistingInDB(String gameId) {
        return chessDAO.isGameIdExisting(gameId);
    }
}
