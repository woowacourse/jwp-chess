package chess.service;

import chess.model.board.Board;
import chess.model.board.MoveResult;
import chess.model.board.Square;
import chess.model.game.ChessGame;
import chess.model.piece.Piece;
import chess.repository.ChessGameRepository;
import chess.repository.BoardRepository;
import chess.repository.dao.GameDao;
import chess.service.dto.response.BoardDto;
import chess.service.dto.response.EndGameResponse;
import chess.service.dto.response.GameResultDto;
import chess.service.dto.response.MoveResponse;
import java.util.Map;
import org.springframework.stereotype.Service;

@Service
public class ChessService {
    private final ChessGameRepository chessGameRepository;
    private final BoardRepository boardRepository;

    public ChessService(ChessGameRepository chessGameRepository, BoardRepository boardRepository) {
        this.chessGameRepository = chessGameRepository;
        this.boardRepository = boardRepository;
    }

    public void initGame(Integer gameId) {
        ChessGame chessGame = ChessGame.getReadyInstance();
        chessGame.start();
        boardRepository.initBoard(gameId);
        chessGameRepository.update(gameId, chessGame);
    }

    public MoveResponse move(Integer gameId, String from, String to) {
        ChessGame chessGame = chessGameRepository.findById(gameId);
        MoveResult movedResult = chessGame.move(Square.of(from), Square.of(to));
        updatePiece(gameId, movedResult);
        chessGameRepository.update(gameId, chessGame);
        return new MoveResponse(movedResult);
    }

    private void updatePiece(Integer id, MoveResult movedResult) {
        Map<Square, Piece> affectedPiece = movedResult.getAffectedPiece();
        for (Square square : affectedPiece.keySet()) {
            boardRepository.update(id, square, affectedPiece.get(square));
        }
    }

    public EndGameResponse endGame(Integer gameId) {
        ChessGame game = chessGameRepository.findById(gameId);
        game.end();
        chessGameRepository.update(gameId, game);
        return new EndGameResponse(gameId);
    }

    public GameResultDto getResult(Integer id) {
        ChessGame chessGame = chessGameRepository.findById(id);
        return GameResultDto.of(chessGame.getResult());
    }

    public BoardDto getBoard(Integer gameId) {
        Board board = boardRepository.findById(gameId);
        return new BoardDto(board);
    }
}
