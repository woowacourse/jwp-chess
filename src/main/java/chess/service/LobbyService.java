package chess.service;

import chess.domain.game.ChessGame;
import chess.repository.ChessRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LobbyService {
    ChessRepository chessRepository;

    @Autowired
    public LobbyService(ChessRepository chessRepository) {
        this.chessRepository = chessRepository;
    }

    public String newGame(String title) {
        ChessGame chessGame = new ChessGame();
        return chessRepository.addGame(chessGame, title);
    }

    public Optional<String> findRoomId(String title) {
        return chessRepository.findRoomId(title);
    }

    public Boolean isDuplicate(String title) {
        Optional<String> foundRoom = chessRepository.findRoomId(title);
        if(foundRoom.isPresent()) {
            return true;
        }
        return false;
    }
}
