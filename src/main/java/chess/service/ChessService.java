package chess.service;

import static chess.util.RandomCreationUtils.createUuid;

import chess.dao.BoardPieceDao;
import chess.dao.GameDao;
import chess.domain.board.ChessBoard;
import chess.domain.board.factory.BoardFactory;
import chess.domain.board.factory.PositionToPieceBoardFactory;
import chess.domain.board.factory.RegularBoardFactory;
import chess.domain.board.position.Position;
import chess.domain.entity.BoardPiece;
import chess.domain.entity.Game;
import chess.domain.gameflow.AlternatingGameFlow;
import chess.domain.gameflow.GameFlow;
import chess.dto.request.web.SaveGameRequest;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ChessService {

    private final GameDao gameDao;
    private final BoardPieceDao boardPieceDao;

    public ChessBoard createChessBoard() {
        BoardFactory boardFactory = RegularBoardFactory.getInstance();
        GameFlow gameFlow = new AlternatingGameFlow();
        return new ChessBoard(boardFactory, gameFlow);
    }

    public void movePiece(ChessBoard chessBoard, Position from, Position to) {
        chessBoard.movePiece(from, to);
    }

    @Transactional(readOnly = true)
    public boolean isExistGame() {
        return gameDao.isExistGame();
    }

    @Transactional
    public void saveGame(SaveGameRequest saveGameRequest) {
        String gameId = createUuid();
        gameDao.save(gameId, saveGameRequest.getCurrentTeam(), saveGameRequest.getCreatedAt());
        boardPieceDao.save(gameId, saveGameRequest.getPieces());
    }

    @Transactional
    public void mergeGame(String gameId,
                          String roomName,
                          String encryptedRoomPassword,
                          Map<String, String> pieces,
                          String teamName,
                          LocalDateTime createdAt
                          ) {
        gameDao.createGame(gameId, roomName, encryptedRoomPassword, teamName, createdAt);
        boardPieceDao.save(gameId, pieces);
    }

    @Transactional(readOnly = true)
    public ChessBoard loadLastGame() {
        Game lastGame = gameDao.findLastGame();
        String lastGameId = lastGame.getGameId();
        String lastTeam = lastGame.getLastTeam();
        List<BoardPiece> lastBoardPieces = boardPieceDao.findLastBoardPiece(lastGameId);
        BoardFactory boardFactory = new PositionToPieceBoardFactory(lastBoardPieces);
        return new ChessBoard(boardFactory, lastTeam);
    }
}
