package chess.service;

import static chess.util.RandomCreationUtils.createUuid;

import chess.dao.BoardPieceDao;
import chess.dao.GameDao;
import chess.domain.board.ChessBoard;
import chess.domain.board.position.Position;
import chess.domain.db.BoardPiece;
import chess.domain.db.Game;
import chess.dto.request.web.SaveRequest;
import chess.dto.response.web.GameResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ChessService {

    private final GameDao gameDao;
    private final BoardPieceDao boardPieceDao;

    private final ObjectProvider<ChessBoard> beanProvider;

    public ChessBoard getChessBoard() {
        ChessBoard chessBoard = beanProvider.getObject();

        System.out.println("getChessBoard chessBoard = " + chessBoard);

        return chessBoard;
    }

    public void movePiece(Position from, Position to) {
        ChessBoard chessBoard = beanProvider.getObject();

        System.out.println("movePiece chessBoard = " + chessBoard);

        chessBoard.movePiece(from, to);
    }

    @Transactional(readOnly = true)
    public boolean isExistGame() {
        return gameDao.isExistGame();
    }

    @Transactional
    public void saveGame(SaveRequest saveRequest) {
        String gameId = createUuid();
        gameDao.save(gameId, saveRequest.getCurrentTeam());
        boardPieceDao.save(gameId, saveRequest.getPieces());
    }

    @Transactional(readOnly = true)
    public GameResponse loadLastGame() {
        Game lastGame = gameDao.findLastGame();
        String lastGameId = lastGame.getGameId();
        String lastTeam = lastGame.getLastTeam();
        List<BoardPiece> lastBoardPieces = boardPieceDao.findLastBoardPiece(lastGameId);
        return new GameResponse(lastBoardPieces, lastTeam);
    }
}
