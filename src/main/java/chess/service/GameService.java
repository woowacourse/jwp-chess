package chess.service;

import chess.domain.board.Position;
import chess.domain.game.ChessGame;
import chess.domain.piece.Color;
import chess.dto.MoveDto;
import chess.repository.ChessRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class GameService {
    private final ChessRepository chessRepository;

    public GameService(ChessRepository chessRepository) {
        this.chessRepository = chessRepository;
    }

    public String loadGame(String gameId) {
        return chessRepository.loadGame(gameId);
    }

    public String turn(String gameId) {
        return chessRepository.turn(gameId);
    }

    public void move(String gameId, MoveDto moveDTO) {
        ChessGame chessGame = chessRepository.loadGameById(gameId);
        Position sourcePosition = Position.of(moveDTO.getSource());
        Position targetPosition = Position.of(moveDTO.getTarget());
        chessGame.move(sourcePosition, targetPosition);
        checkGameOver(gameId, chessGame);
        chessRepository.saveGame(gameId, chessGame);
    }

    private void checkGameOver(String gameId, ChessGame chessGame) {
        if (chessGame.isOver()) {
            finish(gameId);
        }
    }

    public boolean isFinished(String gameId) {
        return chessRepository.isFinishedById(gameId);
    }

    public void finish(String gameId) {
        chessRepository.finish(gameId);
    }

    public List<Double> score(String gameId) {
        ChessGame chessGame = chessRepository.loadGameById(gameId);
        return new ArrayList(Arrays.asList(chessGame.getScore(Color.BLACK), chessGame.getScore(Color.WHITE)));
    }

    public String restart(String gameId) {
        ChessGame chessGame = new ChessGame();
        chessRepository.restart(gameId, chessGame);
        return chessRepository.loadGame(gameId);
    }
}
