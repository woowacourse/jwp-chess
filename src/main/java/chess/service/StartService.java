package chess.service;

import chess.domain.board.Board;
import chess.domain.game.ChessGame;
import chess.domain.piece.PieceFactory;
import chess.repository.GameRepository;
import chess.web.dto.GameDto;
import chess.web.dto.MessageDto;
import org.springframework.stereotype.Service;
import spark.Response;

@Service
public class StartService {

    private final GameRepository gameRepository;

    public StartService(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    public Object startNewGame(String gameId) {
        ChessGame chessGame = saveGameAndStart(gameId);

        return new GameDto(chessGame);
    }

    private ChessGame saveGameAndStart(String gameId) {
        ChessGame chessGame = new ChessGame(new Board(PieceFactory.createPieces()));
        chessGame.start();

        if (gameRepository.isGameIdExistingInDB(gameId)) {
            throw new IllegalArgumentException("이미 존재하는 게임 아이디 입니다.");
        }

        gameRepository.saveToCache(gameId, chessGame);

        return chessGame;
    }

}
