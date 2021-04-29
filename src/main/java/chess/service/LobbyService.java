package chess.service;

import chess.domain.game.ChessGame;
import chess.repository.ChessRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LobbyService {
    ChessRepository chessRepository;

    public LobbyService(ChessRepository chessRepository) {
        this.chessRepository = chessRepository;
    }

    public List<ChessGame> findAllGames() {
        return chessRepository.findAllGames();
    }

    public Long newGame(String title) {
        ChessGame chessGame = new ChessGame();
        return chessRepository.addGame(chessGame, title);
    }

    public Optional<Long> findGame(String title) {
        return chessRepository.findGame(title);
    }
}
