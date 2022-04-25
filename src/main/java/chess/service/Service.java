package chess.service;

import chess.domain.game.ChessGame;
import chess.domain.game.GameSwitch;
import chess.domain.game.Turn;
import chess.domain.piece.Team;
import chess.repository.entity.BoardEntity;
import chess.repository.entity.ChessGameEntity;
import chess.repository.BoardDao;
import chess.repository.ChessGameDao;
import chess.service.util.BoardEntitiesToBoardConvertor;
import java.util.ArrayList;
import java.util.List;

@org.springframework.stereotype.Service
public class Service {

    private final ChessGameDao chessGameDao;
    private final BoardDao boardDao;

    public Service(ChessGameDao chessGameDao, BoardDao boardDao) {
        this.chessGameDao = chessGameDao;
        this.boardDao = boardDao;
    }

    public void createChessGame(final String name) {
        saveChessGame(ChessGame.createBasic(name));
    }

    private void saveChessGame(final ChessGame chessGame) {
        String name = chessGame.getName();
        chessGameDao.delete(name);
        chessGameDao.save(new ChessGameEntity(chessGame));
        int chessGameId = chessGameDao.findChessGameIdByName(name);
        boardDao.save(BoardEntity.generateBoardEntities(chessGameId, chessGame.getCurrentBoard()));
    }

    public ChessGame loadChessGame(final String name) {
        ChessGameEntity chessGameEntity = chessGameDao.load(name);
        int chessGameId = chessGameDao.findChessGameIdByName(name);
        return new ChessGame(
                chessGameEntity.getName(),
                BoardEntitiesToBoardConvertor.convert(boardDao.load(chessGameId)),
                new GameSwitch(chessGameEntity.getIsOn()),
                new Turn(Team.of(chessGameEntity.getTeamValueOfTurn()))
        );
    }

    public List<ChessGame> loadAllChessGames() {
        List<ChessGame> chessGames = new ArrayList<>();
        List<ChessGameEntity> chessGameEntities = chessGameDao.loadAll();
        for (ChessGameEntity chessGameEntity : chessGameEntities) {
            int chessGameId = chessGameDao.findChessGameIdByName(chessGameEntity.getName());
            ChessGame chessGame = new ChessGame(
                    chessGameEntity.getName(),
                    BoardEntitiesToBoardConvertor.convert(boardDao.load(chessGameId)),
                    new GameSwitch(chessGameEntity.getIsOn()),
                    new Turn(Team.of(chessGameEntity.getTeamValueOfTurn()))
            );
            chessGames.add(chessGame);
        }
        return chessGames;
    }

    public void deleteChessGame(final String name) {
        chessGameDao.delete(name);
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
        chessGameDao.updateChessGame(new ChessGameEntity(chessGame));
        int chessGameId = chessGameDao.findChessGameIdByName(name);
        boardDao.updatePiece(
                new BoardEntity(chessGameId, sourceColumnValue, sourceRowValue, chessGame.getCurrentBoard())
        );
        boardDao.updatePiece(
                new BoardEntity(chessGameId, targetColumnValue, targetRowValue, chessGame.getCurrentBoard())
        );
    }
}
