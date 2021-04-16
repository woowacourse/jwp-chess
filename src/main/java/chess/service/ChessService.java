package chess.service;

import chess.domain.board.Position;
import chess.domain.game.ChessGame;
import chess.dto.ChessBoardDTO;
import chess.dto.MoveDTO;
import chess.dto.RoomIdDTO;
import chess.dto.TurnDTO;
import chess.repository.ChessRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class ChessService {
    private final ChessRepository chessRepository;

    @Autowired
    public ChessService(ChessRepository chessRepository) {
        this.chessRepository = chessRepository;
    }

    public RoomIdDTO newGame() {
        ChessGame chessGame = new ChessGame();
        return chessRepository.addGame(chessGame);
    }

    public ChessBoardDTO loadGame(String gameId) {
        return chessRepository.loadGameAsDTO(gameId);
    }

    public TurnDTO turn(String gameId) {
        return chessRepository.turn(gameId);
    }

    public ResponseEntity move(String gameId, MoveDTO moveDTO) {
        try {
            ChessGame chessGame = chessRepository.loadGame(gameId);
            System.out.println("position ##############");
            System.out.println(moveDTO.getSource());
            chessGame.move(Position.of(moveDTO.getSource()), Position.of(moveDTO.getTarget()));
            chessRepository.saveGame(gameId, chessGame);
            return new ResponseEntity(HttpStatus.OK);
        } catch (Exception e) {
            System.out.println("@@@@@");
            System.out.println(e.getMessage());
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }
}
