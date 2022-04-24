package chess.service;

import chess.domain.game.ChessGame;
import chess.domain.game.GameSwitch;
import chess.domain.game.Turn;
import chess.domain.piece.Team;
import chess.repository.entity.BoardEntity;
import chess.repository.entity.ChessGameEntity;
import chess.repository.spring.BoardDao;
import chess.repository.spring.ChessGameDao;
import chess.service.util.BoardEntitiesToBoardConvertor;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class SpringChessService {

    private final ChessGameDao chessGameDao;
    private final BoardDao boardDao;

    public SpringChessService(ChessGameDao chessGameDao, BoardDao boardDao) {
        this.chessGameDao = chessGameDao;
        this.boardDao = boardDao;
    }

    public void createChessGame(final String name) {
        ChessGame chessGame = ChessGame.createBasic(name);
        saveChessGame(chessGame);
    }

    private void saveChessGame(final ChessGame chessGame) {
        String name = chessGame.getName();
        chessGameDao.delete(name);
        chessGameDao.save(new ChessGameEntity(chessGame));
        boardDao.delete(name);
        boardDao.save(BoardEntity.generateBoardEntities(name, chessGame.getCurrentBoard()));
    }

    public ChessGame loadChessGame(final String name) {
        ChessGameEntity chessGameEntity = chessGameDao.load(name);
        return new ChessGame(
                chessGameEntity.getName(),
                BoardEntitiesToBoardConvertor.convert(boardDao.load(name)),
                new GameSwitch(chessGameEntity.getIsOn()),
                new Turn(Team.of(chessGameEntity.getTeamValueOfTurn()))
        );
    }

    public List<ChessGame> loadAllChessGames() {
        List<ChessGame> chessGames = new ArrayList<>();
        List<ChessGameEntity> chessGameEntities = chessGameDao.loadAll();
        for (ChessGameEntity chessGameEntity : chessGameEntities) {
            ChessGame chessGame = new ChessGame(
                    chessGameEntity.getName(),
                    BoardEntitiesToBoardConvertor.convert(boardDao.load(chessGameEntity.getName())),
                    new GameSwitch(chessGameEntity.getIsOn()),
                    new Turn(Team.of(chessGameEntity.getTeamValueOfTurn()))
            );
            chessGames.add(chessGame);
        }
        return chessGames;
    }

    public void deleteChessGame(final String name) {
        chessGameDao.delete(name);
        boardDao.delete(name);
    }

    public void movePiece(
            final String name,
            final char sourceColumnValue,
            final int sourceRowValue,
            final char targetColumnValue,
            final int targetRowValue
    ) {
        ChessGame chessGame = loadChessGame(name);
        chessGame.move(sourceColumnValue, sourceRowValue, targetColumnValue, targetRowValue);
        chessGameDao.updateIsOnAndTurn(new ChessGameEntity(chessGame));
        boardDao.updatePiece(new BoardEntity(name, sourceColumnValue, sourceRowValue, chessGame.getCurrentBoard()));
        boardDao.updatePiece(new BoardEntity(name, targetColumnValue, targetRowValue, chessGame.getCurrentBoard()));
    }
}
