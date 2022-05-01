package chess.service;

import chess.entity.PieceEntity;
import chess.exception.IllegalDeleteRoomException;
import chess.exception.NoSuchGameException;
import chess.model.board.Board;
import chess.model.board.BoardFactory;
import chess.model.dao.GameDao;
import chess.model.dao.PieceDao;
import chess.model.dto.WebBoardDto;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;

@Service
public class ChessGameService {

    private final PieceDao pieceDao;
    private final GameDao gameDao;

    public ChessGameService(PieceDao pieceDao, GameDao gameDao) {
        this.pieceDao = pieceDao;
        this.gameDao = gameDao;
    }

    public Map<Long, String> getAllGamesWithIdAndTitle() {
        Map<Long, String> games = new LinkedHashMap<>();
        for (Long gameId : gameDao.findAllGameId()) {
            games.put(gameId, gameDao.findTitleByGameId(gameId));
        }

        return games;
    }

    public Long createGame(String title, String password) {
        Long gameId = gameDao.saveGame(title, password);
        pieceDao.savePieces(BoardFactory.create(), gameId);

        return gameId;
    }

    public WebBoardDto continueGame(Long gameId) {
        List<PieceEntity> pieceEntities = pieceDao.findAllPiecesByGameId(gameId);
        Board board = ChessBoardService.toBoard(pieceEntities);

        return WebBoardDto.from(board);
    }

    public String getTurn(Long gameId) {
        return gameDao.findTurnByGameId(gameId)
                .orElseThrow(() -> new NoSuchGameException("잘못된 ID 값입니다."));
    }

    public void deleteGame(Long gameId, String password) {
        validateCanDeleteGame(gameId, password);

        pieceDao.deleteByGameId(gameId);
        gameDao.deleteByGameId(gameId);
    }

    private void validateCanDeleteGame(Long gameId, String password) {
        if (!getTurn(gameId).equals("end")) {
            throw new IllegalDeleteRoomException("게임이 진행중인 방은 삭제할 수 없습니다.");
        }
        if (!gameDao.findPasswordByGameId(gameId).equals(password)) {
            throw new IllegalDeleteRoomException("방 비밀번호가 맞지 않습니다.");
        }
    }

    public void exitGame(Long gameId) {
        gameDao.updateTurnByGameId(gameId, "end");
    }

    public void restartGame(Long gameId) {
        pieceDao.deleteByGameId(gameId);
        pieceDao.savePieces(BoardFactory.create(), gameId);
        gameDao.updateTurnByGameId(gameId, "white");
    }
}
