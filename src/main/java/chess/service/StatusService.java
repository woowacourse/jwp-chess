package chess.service;

import chess.domain.game.ChessGame;
import chess.repository.GameRepository;
import chess.web.dto.MessageDto;
import chess.web.dto.StatusDto;
import org.springframework.stereotype.Service;

@Service
public class StatusService {

    private final GameRepository gameRepository;

    public StatusService(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    public Object getStatus(String gameId) {
        ChessGame chessGame = gameRepository.findByGameIdFromCache(gameId);

        double whiteScore = chessGame.getWhiteScore();
        double blackScore = chessGame.getBlackScore();

        return new StatusDto(whiteScore, blackScore);
    }

}
