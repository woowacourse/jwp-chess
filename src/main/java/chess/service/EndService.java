package chess.service;

import chess.domain.game.ChessGame;
import chess.repository.GameRepository;
import chess.web.dto.MessageDto;
import org.springframework.stereotype.Service;

@Service
public class EndService {

    private final GameRepository gameRepository;

    public EndService(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    public Object end(String gameId) {
        ChessGame chessGame = gameRepository.findByGameIdFromCache(gameId);

        chessGame.end();

        return new MessageDto("finished");
    }

}
