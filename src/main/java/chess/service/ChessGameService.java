package chess.service;

import chess.dao.BoardDao;
import chess.dao.ChessGameDao;
import chess.domain.board.BoardFactory;
import chess.domain.game.ChessGame;
import chess.domain.game.GameSwitch;
import chess.domain.game.Turn;
import chess.domain.piece.Team;
import chess.dto.ChessGameDto;
import chess.dto.ChessRoomDto;
import chess.entity.BoardEntity;
import chess.entity.ChessGameEntity;
import chess.service.util.BoardEntitiesToBoardConvertor;
import java.util.ArrayList;
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

    public long createChessGame(final String name, final String password) {
        ChessGame chessGame = ChessGame.createBasic();
        long chessGameId = chessGameDao.save(new ChessGameEntity(name, password, chessGame)).longValue();
        boardDao.save(BoardEntity.generateBoardEntities(chessGameId, chessGame.getCurrentBoard()));
        return chessGameId;
    }

    public ChessGameDto loadChessGame(final long chessGameId) {
        ChessGameEntity chessGameEntity = chessGameDao.load(chessGameId);
        ChessGame chessGame = convertChessGame(chessGameId, chessGameEntity);
        return new ChessGameDto(chessGameEntity.getName(), chessGame);
    }

    private ChessGame convertChessGame(long chessGameId, ChessGameEntity chessGameEntity) {
        return new ChessGame(
                BoardEntitiesToBoardConvertor.convert(boardDao.load(chessGameId)),
                new GameSwitch(chessGameEntity.getIsOn()),
                new Turn(Team.of(chessGameEntity.getTeamValueOfTurn()))
        );
    }

    public List<ChessRoomDto> loadAllChessGames() {
        return convertChessRoomDto(chessGameDao.loadAll());
    }

    private List<ChessRoomDto> convertChessRoomDto(List<ChessGameEntity> chessGameEntities) {
        List<ChessRoomDto> chessRoomDto = new ArrayList<>();
        for (ChessGameEntity chessGameEntity : chessGameEntities) {
            chessRoomDto.add(new ChessRoomDto(chessGameEntity));
        }
        return chessRoomDto;
    }

    public void deleteChessGame(final long chessGameId, final String password) {
        boardDao.delete(chessGameId);
        chessGameDao.delete(new ChessGameEntity(chessGameId, password));
    }

    public void movePiece(
            final long chessGameId,
            final char sourceColumnValue, final int sourceRowValue,
            final char targetColumnValue, final int targetRowValue
    ) {
        ChessGame chessGame = convertChessGame(chessGameId, chessGameDao.load(chessGameId));
        chessGame.move(sourceColumnValue, sourceRowValue, targetColumnValue, targetRowValue);
        chessGameDao.updateIsOnAndTurn(new ChessGameEntity(chessGameId, chessGame.isOn(), chessGame.getTurn()));
        boardDao.updatePiece(
                new BoardEntity(chessGameId, sourceColumnValue, sourceRowValue, chessGame.getCurrentBoard()));
        boardDao.updatePiece(
                new BoardEntity(chessGameId, targetColumnValue, targetRowValue, chessGame.getCurrentBoard()));
    }

    public void resetChessGame(final long chessGameId) {
        boardDao.delete(chessGameId);
        chessGameDao.updateIsOnAndTurn(new ChessGameEntity(chessGameId, true, new Turn(Team.WHITE)));
        boardDao.save(BoardEntity.generateBoardEntities(chessGameId, BoardFactory.createInitChessBoard().getBoard()));
    }
}
