package chess.service;

import static chess.util.RandomCreationUtils.createUuid;

import chess.dao.BoardPieceDao;
import chess.dao.GameDao;
import chess.domain.board.ChessBoard;
import chess.domain.board.factory.BoardFactory;
import chess.domain.board.factory.RegularBoardFactory;
import chess.domain.board.position.Position;
import chess.domain.db.BoardPiece;
import chess.domain.db.Game;
import chess.dto.request.web.SaveRequest;
import chess.dto.response.web.GameResponse;
import chess.gameflow.AlternatingGameFlow;
import chess.gameflow.GameFlow;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ChessService {

    private final GameDao gameDao;
    private final BoardPieceDao boardPieceDao;
    private Map<HttpSession, ChessBoard> sessionToChessBoard = new HashMap<>();

    public ChessBoard initAndGetChessBoard(HttpSession session) {
        ChessBoard chessBoard = createChessBoard();
        sessionToChessBoard.put(session, chessBoard);
        return chessBoard;
    }

    private ChessBoard createChessBoard() {
        BoardFactory boardFactory = RegularBoardFactory.getInstance();
        GameFlow gameFlow = new AlternatingGameFlow();
        return new ChessBoard(boardFactory.create(), gameFlow);
    }

    public ChessBoard getChessBoard(HttpSession session) {
        return sessionToChessBoard.get(session);
    }

    public void movePiece(HttpSession session,
                          Position from,
                          Position to) {
        ChessBoard chessBoard = sessionToChessBoard.get(session);



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

    @Scheduled(cron = "0/3 * * * * MON-FRI")
    public void scheduled() {
        System.out.println("ChessService.scheduled");
    }
}
