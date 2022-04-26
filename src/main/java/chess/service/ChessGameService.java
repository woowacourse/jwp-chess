package chess.service;

import chess.domain.board.BoardFactory;
import chess.domain.game.ChessGame;
import chess.domain.game.GameSwitch;
import chess.domain.game.Turn;
import chess.domain.piece.Team;
import chess.entity.BoardEntity;
import chess.entity.ChessGameEntity;
import chess.dao.BoardDao;
import chess.dao.ChessGameDao;
import chess.service.util.BoardEntitiesToBoardConvertor;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class ChessGameService {

    private final ChessGameDao chessGameDao;
    private final BoardDao boardDao;

    public ChessGameService(final ChessGameDao chessGameDao, final BoardDao boardDao) {
        this.chessGameDao = chessGameDao;
        this.boardDao = boardDao;
    }

    public long createChessGame(final String name) {
        ChessGame chessGame = ChessGame.createBasic(name);
        return saveChessGame(chessGame);
    }

    private long saveChessGame(final ChessGame chessGame) {
        long chessGameId = chessGameDao.save(new ChessGameEntity(chessGame)).longValue();
        boardDao.save(BoardEntity.generateBoardEntities(chessGameId, chessGame.getCurrentBoard()));
        return chessGameId;
    }

    public ChessGame loadChessGame(final long chessGameId) {
        ChessGameEntity chessGameEntity = chessGameDao.load(chessGameId);
        return new ChessGame(
                chessGameEntity.getName(),
                BoardEntitiesToBoardConvertor.convert(boardDao.load(chessGameId)),
                new GameSwitch(chessGameEntity.getIsOn()),
                new Turn(Team.of(chessGameEntity.getTeamValueOfTurn()))
        );
    }

    public List<ChessGameEntity> loadAllChessGames() {
        return chessGameDao.loadAll();
    }

    public void deleteChessGame(final long chessGameId) {
        boardDao.delete(chessGameId);
        chessGameDao.delete(chessGameId);
    }

    public void movePiece(
            final long chessGameId,
            final char sourceColumnValue,
            final int sourceRowValue,
            final char targetColumnValue,
            final int targetRowValue
    ) {
        ChessGame chessGame = loadChessGame(chessGameId);
        chessGame.move(sourceColumnValue, sourceRowValue, targetColumnValue, targetRowValue);
        chessGameDao.updateIsOnAndTurn(new ChessGameEntity(chessGame));
        boardDao.updatePiece(new BoardEntity(chessGameId, sourceColumnValue, sourceRowValue, chessGame.getCurrentBoard()));
        boardDao.updatePiece(new BoardEntity(chessGameId, targetColumnValue, targetRowValue, chessGame.getCurrentBoard()));
    }

    public void resetChessGame(final long chessGameId) {
        boardDao.delete(chessGameId);
        boardDao.save(BoardEntity.generateBoardEntities(chessGameId, BoardFactory.createInitChessBoard().getBoard()));
    }
}
