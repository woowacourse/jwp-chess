package chess.service;

import chess.dao.GameDao;
import chess.dao.PieceDao;
import chess.domain.command.MoveCommand;
import chess.domain.game.ChessGame;
import chess.domain.game.GameResult;
import chess.domain.piece.ChessmenInitializer;
import chess.domain.piece.Color;
import chess.domain.piece.Pieces;
import chess.domain.room.Room;
import chess.dto.GameResultDto;
import chess.dto.MoveCommandDto;
import chess.dto.PiecesDto;
import chess.dto.RoomDto;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

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

    public long create(String title, String password) {
        long gameId = gameDao.createByTitleAndPassword(title, password);
        Pieces chessmen = ChessmenInitializer.init();
        pieceDao.createAllByGameId(chessmen.getPieces(), gameId);
        return gameId;
    }

    private ChessGame findChessGame(long gameId) {
        Room room = gameDao.findRoomById(gameId);
        Pieces chessmen = pieceDao.findAllByGameId(gameId);
        return new ChessGame(room.getEndFlag(), chessmen, room.getTurn());
    }

    public PiecesDto findCurrentPieces(long gameId) {
        return PiecesDto.toDto(findChessGame(gameId).getChessmen());
    }

    public GameResultDto calculateGameResult(long gameId) {
        GameResult gameResult = GameResult.calculate(findChessGame(gameId).getChessmen());
        return GameResultDto.toDto(gameResult);
    }

    public void cleanGame(long gameId) {
        pieceDao.deleteAllByGameId(gameId);
        gameDao.deleteById(gameId);
    }

    public void move(long gameId, MoveCommandDto moveCommandDto) {
        String from = moveCommandDto.getSource();
        String to = moveCommandDto.getTarget();
        findChessGame(gameId).moveChessmen(new MoveCommand(from, to));
        saveMove(gameId, moveCommandDto);
        if (findChessGame(gameId).isEnd()) {
            gameDao.updateEndFlagById(true, gameId);
        }
    }

    private void saveMove(long gameId, MoveCommandDto moveCommandDto) {
        ChessGame chessGame = findChessGame(gameId);
        chessGame.moveChessmen(moveCommandDto.toEntity());
        boolean endFlag = chessGame.getEndFlag();
        Color turn = chessGame.getTurn();

        if (pieceDao.exists(gameId, moveCommandDto.getTarget())) {
            pieceDao.deleteByGameIdAndPosition(gameId, moveCommandDto.getTarget());
        }
        pieceDao.updateByGameIdAndPosition(gameId, moveCommandDto.getSource(), moveCommandDto.getTarget());
        gameDao.updateTurnById(turn, gameId);
        gameDao.updateEndFlagById(endFlag, gameId);
    }

    public void cleanGameByIdAndPassword(long id, String password) {
        Room room = gameDao.findRoomById(id);
        room.validateDeletable(password);
        cleanGame(id);
    }

}
