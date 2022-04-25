package chess.service;

import static chess.util.RandomCreationUtils.createUuid;

import chess.dao.ChessBoardDao;
import chess.dao.ChessGameDao;
import chess.dto.request.web.SaveRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ChessService {

    private final ChessGameDao chessGameDao;
    private final ChessBoardDao chessBoardDao;

    public ChessService(ChessGameDao chessGameDao, ChessBoardDao chessBoardDao) {
        this.chessGameDao = chessGameDao;
        this.chessBoardDao = chessBoardDao;
    }

    @Transactional(readOnly = true)
    public boolean isExistGame() {
        return chessGameDao.isExistGame();
    }

    @Transactional
    public void saveGame(SaveRequest saveRequest) {
        String gameId = createUuid();
        chessGameDao.save(gameId, saveRequest.getCurrentTeam());
        chessBoardDao.save(gameId, saveRequest.getPieces());
    }
}
