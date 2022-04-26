package chess.service;

import static chess.util.RandomCreationUtils.createUuid;

import chess.dao.BoardPieceDao;
import chess.dao.GameDao;
import chess.domain.db.BoardPiece;
import chess.domain.db.Game;
import chess.dto.request.web.SaveRequest;
import chess.dto.response.web.LastGameResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ChessService {

    private final GameDao gameDao;
    private final BoardPieceDao boardPieceDao;

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
    public LastGameResponse loadLastGame() {
        Game lastGame = gameDao.findLastGame();
        String lastGameId = lastGame.getGameId();
        String lastTeam = lastGame.getLastTeam();
        List<BoardPiece> lastBoardPieces = boardPieceDao.findLastBoardPiece(lastGameId);
        return new LastGameResponse(lastBoardPieces, lastTeam);
    }
}
