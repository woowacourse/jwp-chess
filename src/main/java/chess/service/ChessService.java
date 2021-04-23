package chess.service;

import chess.domain.ChessRepository;
import chess.domain.board.Board;
import chess.domain.game.ChessGame;
import chess.domain.piece.PieceFactory;
import chess.domain.piece.Position;
import chess.exception.AlreadyExistingGameIdException;
import chess.web.dto.GameDto;
import chess.web.dto.MessageDto;
import chess.web.dto.StatusDto;
import org.springframework.stereotype.Service;

@Service
public class ChessService {

    private final ChessRepository chessRepository;

    public ChessService(ChessRepository chessRepository) {
        this.chessRepository = chessRepository;
    }

    public MessageDto end(String gameId) {
        ChessGame chessGame = chessRepository.findByGameId(gameId);

        chessGame.end();

        chessRepository.update(chessGame);

        return new MessageDto("finished");
    }

    public GameDto loadByGameId(String gameId) {
        ChessGame chessGame = chessRepository.findByGameId(gameId);

        return new GameDto(chessGame);
    }

    public GameDto startNewGame(String gameId) {
        ChessGame chessGame = saveGameAndStart(gameId);

        return new GameDto(chessGame);
    }

    private ChessGame saveGameAndStart(String gameId) {
        if (chessRepository.containsByGameId(gameId)) {
            throw new AlreadyExistingGameIdException();
        }

        ChessGame chessGame = new ChessGame(
                gameId,
                new Board(PieceFactory.createPieces())
        );
        chessGame.start();

        chessRepository.save(chessGame);

        return chessGame;
    }

    public StatusDto getStatus(String gameId) {
        ChessGame chessGame = chessRepository.findByGameId(gameId);

        double whiteScore = chessGame.getWhiteScore();
        double blackScore = chessGame.getBlackScore();

        return new StatusDto(whiteScore, blackScore);
    }

    public GameDto move(String gameId, String source, String target) {
        ChessGame chessGame = chessRepository.findByGameId(gameId);

        chessGame.move(Position.ofChessPiece(source), Position.ofChessPiece(target));
        chessRepository.update(chessGame);

        return new GameDto(chessGame);
    }

}
