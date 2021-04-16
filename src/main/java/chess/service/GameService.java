package chess.service;

import chess.domain.board.Position;
import chess.domain.game.ChessGame;
import chess.domain.piece.Color;
import chess.dto.*;
import chess.repository.ChessRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class GameService {
    private final ChessRepository chessRepository;

    @Autowired
    public GameService(ChessRepository chessRepository) {
        this.chessRepository = chessRepository;
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
            Position sourcePosition = Position.of(moveDTO.getSource());
            Position targetPosition = Position.of(moveDTO.getTarget());
            chessGame.move(sourcePosition, targetPosition);
            checkGameOver(gameId, chessGame);
            chessRepository.saveGame(gameId, chessGame);
            return new ResponseEntity(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }

    private void checkGameOver(String gameId, ChessGame chessGame) {
        if (chessGame.isOver()) {
            finish(gameId);
        }
    }

    public FinishDTO isFinished(String gameId) {
        return chessRepository.isFinished(gameId);
    }

    public void finish(String gameId) {
        chessRepository.finish(gameId);
    }

    public ResultDTO result(String gameId) {
        ChessGame chessGame = chessRepository.loadGame(gameId);
        return new ResultDTO(chessGame.getResult(Color.BLACK), chessGame.getResult(Color.WHITE));
    }
}
