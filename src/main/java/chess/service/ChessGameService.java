package chess.service;

import chess.entity.PieceEntity;
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
    private final ChessBoardService chessBoardService;

    public ChessGameService(PieceDao pieceDao, GameDao gameDao, ChessBoardService chessBoardService) {
        this.pieceDao = pieceDao;
        this.gameDao = gameDao;
        this.chessBoardService = chessBoardService;
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
        Board board = chessBoardService.toBoard(pieceEntities);

        return WebBoardDto.from(board);
    }

    public void deleteGame(Long gameId, String password) {
        validateCanDeleteGame(gameId, password);

        pieceDao.deleteByGameId(gameId);
        gameDao.deleteByGameId(gameId);
    }

    private void validateCanDeleteGame(Long gameId, String password) {
        if (!chessBoardService.getTurn(gameId).equals("end")) {
            throw new IllegalStateException("게임이 진행중인 방은 삭제할 수 없습니다.");
        }
        if (!gameDao.findPasswordByGameId(gameId).equals(password)) {
            throw new IllegalArgumentException("방 비밀번호가 맞지 않습니다.");
        }
    }
}
