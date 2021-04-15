package chess.service;

import chess.domain.game.ChessGame;
import chess.dto.ChessBoardDTO;
import chess.dto.RoomIdDTO;
import chess.repository.ChessRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ChessService {
    private final ChessRepository chessRepository;

    @Autowired
    public ChessService(ChessRepository chessRepository){
        this.chessRepository = chessRepository;
    }

    public RoomIdDTO newGame(){
        ChessGame chessGame = new ChessGame();
        return chessRepository.addGame(chessGame);
    }

    public ChessBoardDTO loadGame(int gameId) {
        return chessRepository.loadGame(gameId);
    }
}
