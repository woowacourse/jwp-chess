package chess.service;

import chess.dao.GameDao;
import chess.dao.PieceDao;
import chess.domain.command.MoveCommand;
import chess.domain.game.ChessGame;
import chess.domain.game.GameResult;
import chess.domain.piece.ChessmenInitializer;
import chess.domain.piece.Pieces;
import chess.domain.room.Room;
import chess.dto.GameResultDto;
import chess.dto.MoveCommandDto;
import chess.dto.PiecesDto;
import chess.dto.RoomDto;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ChessGameService {

    private final PieceDao pieceDao;
    private final GameDao gameDao;

    public ChessGameService(PieceDao pieceDao, GameDao gameDao) {
        this.pieceDao = pieceDao;
        this.gameDao = gameDao;
    }

    public List<RoomDto> getAllGames() {
        List<Room> rooms = gameDao.findAllRoom();
        return rooms.stream()
            .map(RoomDto::toDto)
            .collect(Collectors.toList());
    }

    @Transactional
    public long create(Room room) {

        long gameId = gameDao.createByTitleAndPassword(room);
        Pieces chessmen = ChessmenInitializer.init();
        pieceDao.createAllByGameId(chessmen.getPieces(), gameId);
        return gameId;
    }

    public PiecesDto findCurrentPieces(long gameId) {
        return PiecesDto.toDto(findChessGame(gameId).getChessmen());
    }

    private ChessGame findChessGame(long gameId) {
        Room room = gameDao.findRoomById(gameId);
        Pieces chessmen = pieceDao.findAllByGameId(gameId);
        return new ChessGame(chessmen, room.getTurn());
    }

    public GameResultDto calculateGameResult(long gameId) {
        GameResult gameResult = GameResult.calculate(findChessGame(gameId).getChessmen());
        return GameResultDto.toDto(gameResult);
    }

    @Transactional
    public void move(long gameId, MoveCommandDto moveCommandDto) {
        String from = moveCommandDto.getSource();
        String to = moveCommandDto.getTarget();
        ChessGame chessGame = findChessGame(gameId);

        chessGame.moveChessmen(new MoveCommand(from, to));

        saveMove(gameId, moveCommandDto, chessGame);
    }

    private void saveMove(long gameId, MoveCommandDto moveCommandDto, ChessGame chessGame) {
        if (pieceDao.exists(gameId, moveCommandDto.getTarget())) {
            pieceDao.deleteByGameIdAndPosition(gameId, moveCommandDto.getTarget());
        }
        pieceDao.updateByGameIdAndPosition(gameId, moveCommandDto.getSource(), moveCommandDto.getTarget());
        gameDao.updateTurnById(chessGame.getTurn(), gameId);
        gameDao.updateEndFlagById(chessGame.isEnd(), gameId);
    }

    @Transactional
    public void cleanGameByIdAndPassword(long id, String password) {
        Room room = gameDao.findRoomById(id);
        room.validateDeletable(password);
        gameDao.deleteById(id);
    }

}
