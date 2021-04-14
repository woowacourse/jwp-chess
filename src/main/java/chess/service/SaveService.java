package chess.service;

import chess.domain.game.ChessGame;
import chess.repository.GameRepository;
import chess.web.dto.MessageDto;
import org.springframework.stereotype.Service;
import spark.Response;

@Service
public class SaveService {

    private final GameRepository gameRepository;

    public SaveService(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    public Object save(String gameId) {
        ChessGame chessGame = gameRepository.findByGameIdFromCache(gameId);
        saveGameToDB(gameId, chessGame);

        return new MessageDto("저장 완료");
    }

    private void saveGameToDB(String gameId, ChessGame chessGame) {
        if (gameRepository.isGameIdExistingInDB(gameId)) {
            gameRepository.updateToDB(gameId, chessGame);
            return;
        }

        gameRepository.saveToDB(gameId, chessGame);
    }

}
