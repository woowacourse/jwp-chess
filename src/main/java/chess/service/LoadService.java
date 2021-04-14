package chess.service;

import chess.domain.game.ChessGame;
import chess.repository.GameRepository;
import chess.web.dto.GameDto;
import chess.web.dto.MessageDto;
import org.springframework.stereotype.Service;
import spark.Response;

@Service
public class LoadService {

    private final GameRepository gameRepository;

    public LoadService(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    public Object loadByGameId(String gameId) {
        ChessGame chessGame = gameRepository.findByGameIdFromDB(gameId);
        gameRepository.saveToCache(gameId, chessGame);

        return new GameDto(chessGame);
    }

}
