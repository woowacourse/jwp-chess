package chess.service;

import chess.domain.game.ChessGame;
import chess.repository.ChessRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LobbyService {

    private final ChessRepository chessRepository;

    public LobbyService(ChessRepository chessRepository) {
        this.chessRepository = chessRepository;
    }

    public List<ChessGame> findAllGames() {
        return chessRepository.findAllGames();
    }

    public String newGame(String title) {
        ChessGame chessGame = new ChessGame();
        return chessRepository.addGame(chessGame, title);
    }

    public Optional<String> findGame(String title) {
        return chessRepository.findGame(title);
    }

    public void verifyDuplicateTitleInGames(String title) {
        if (chessRepository.findGame(title).isPresent()) {
            throw new IllegalArgumentException("같은 이름으로 등록된 방이 있습니다.");
        }
    }
}
