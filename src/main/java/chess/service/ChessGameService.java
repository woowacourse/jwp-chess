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
import chess.entity.ChessGameEntityBuilder;
import chess.service.util.BoardEntitiesToBoardConvertor;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ChessGameService {

    private final ChessGameDao chessGameDao;
    private final BoardDao boardDao;

    public ChessGameService(final ChessGameDao chessGameDao, final BoardDao boardDao) {
        this.chessGameDao = chessGameDao;
        this.boardDao = boardDao;
    }

    @Transactional
    public long createChessGame(final String name, final String password) {
        ChessGame chessGame = ChessGame.createBasic();
        ChessGameEntity chessGameEntity = new ChessGameEntityBuilder()
                .setName(name)
                .setPassword(password)
                .setPower(chessGame.isOn())
                .setTeamValueOfTurn(chessGame.getTurn())
                .build();

        long chessGameId = chessGameDao.save(chessGameEntity).longValue();
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
                new GameSwitch(chessGameEntity.getPower()),
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

    @Transactional
    public void deleteChessGame(final long chessGameId, final String password) {
        validateGamePower(chessGameId);
        ChessGameEntity chessGameEntity = new ChessGameEntityBuilder()
                .setId(chessGameId)
                .setPassword(password)
                .build();

        boardDao.delete(chessGameId);
        chessGameDao.delete(chessGameEntity);
    }

    private void validateGamePower(long chessGameId) {
        ChessGameEntity chessGameEntity = chessGameDao.load(chessGameId);
        chessGameEntity.isPower();
    }

    @Transactional
    public void movePiece(
            final long chessGameId, final char sourceColumn, final int sourceRow,
            final char targetColumn, final int targetRow
    ) {
        ChessGame chessGame = convertChessGame(chessGameId, chessGameDao.load(chessGameId));
        chessGame.move(sourceColumn, sourceRow, targetColumn, targetRow);

        ChessGameEntity chessGameEntity = new ChessGameEntityBuilder()
                .setId(chessGameId)
                .setPower(chessGame.isOn())
                .setTeamValueOfTurn(chessGame.getTurn())
                .build();
        chessGameDao.updatePowerAndTurn(chessGameEntity);
        boardDao.updatePiece(new BoardEntity(chessGameId, sourceColumn, sourceRow, chessGame.getCurrentBoard()));
        boardDao.updatePiece(new BoardEntity(chessGameId, targetColumn, targetRow, chessGame.getCurrentBoard()));
    }

    @Transactional
    public void resetChessGame(final long chessGameId) {
        ChessGameEntity chessGameEntity = new ChessGameEntityBuilder()
                .setId(chessGameId)
                .setPower(true)
                .setTeamValueOfTurn(new Turn(Team.WHITE).getValue())
                .build();
        boardDao.delete(chessGameId);
        boardDao.save(BoardEntity.generateBoardEntities(chessGameId, BoardFactory.createInitChessBoard().getBoard()));
        chessGameDao.updatePowerAndTurn(chessGameEntity);
    }

    @Transactional
    public void endChessGame(final long chessGameId) {
        ChessGameEntity chessGameEntity = new ChessGameEntityBuilder()
                .setId(chessGameId)
                .setPower(false)
                .build();
        chessGameDao.updatePower(chessGameEntity);
    }
}
