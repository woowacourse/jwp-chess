package chess.service;

import chess.dao.GameDao;
import chess.dao.RoomDao;
import chess.domain.ChessGameManager;
import chess.domain.piece.Color;
import chess.domain.position.Position;
import chess.dto.*;
import chess.exception.HandledException;
import chess.exception.NoSavedGameException;
import org.springframework.stereotype.Service;

@Service
public class ChessService {
    private final GameDao gameDao;
    private final RoomDao roomDao;

    public ChessService(GameDao gameDao, RoomDao roomDao) {
        this.gameDao = gameDao;
        this.roomDao = roomDao;
    }

    public CommonDto<RoomListDto> loadRoomList() {
        return new CommonDto<>("게임 목록을 불러왔습니다.", new RoomListDto(roomDao.loadRoomList()));
    }

    public CommonDto<NewGameDto> saveNewGame() {
        ChessGameManager chessGameManager = new ChessGameManager();
        chessGameManager.start();

        ChessBoardDto chessBoardDto = ChessBoardDto.from(chessGameManager.getBoard());
        SavedGameDto savedGameDto = new SavedGameDto(
                chessBoardDto,
                chessGameManager.getCurrentTurnColor().name());

        int gameId = gameDao.saveGame(savedGameDto);
        savePiecesByGameId(gameId, chessBoardDto);

        return new CommonDto<>("새로운 게임이 생성되었습니다.",
                NewGameDto.from(chessGameManager, gameId));
    }

    private void savePiecesByGameId(int gameId, ChessBoardDto chessBoardDto) {
        gameDao.savePiecesByGameId(gameId, chessBoardDto.board());
    }

    public CommonDto<RunningGameDto> loadGame(int gameId) {
        ChessGameManager chessGameManager = loadChessGameManager(gameId);
        return new CommonDto<>("게임을 불러왔습니다.", toRunningGameDto(chessGameManager));
    }

    private RunningGameDto toRunningGameDto(ChessGameManager chessGameManager) {
        return RunningGameDto.of(
                chessGameManager.getBoard(),
                chessGameManager.getCurrentTurnColor(),
                chessGameManager.isEnd());
    }

    public CommonDto<RunningGameDto> move(int gameId, String startPosition, String endPosition) {
        ChessGameManager chessGameManager = loadChessGameManager(gameId);
        chessGameManager.move(Position.of(startPosition), Position.of(endPosition));

        if (chessGameManager.isEnd()) {
            gameDao.deletePiecesByGameId(gameId);
            gameDao.deleteGameByGameId(gameId);
            return new CommonDto<>("게임이 끝났습니다.", toRunningGameDto(chessGameManager));
        }

        gameDao.updateTurnByGameId(chessGameManager.getCurrentTurnColor(), gameId);
        updatePiece(gameId, startPosition, endPosition);
        return new CommonDto<>("기물을 이동했습니다.", toRunningGameDto(chessGameManager));
    }

    private void updatePiece(int gameId, String from, String to) {
        gameDao.deletePieceByGameId(gameId, to);
        gameDao.updatePiecePositionByGameId(gameId, from, to);
    }

    private ChessGameManager loadChessGameManager(int gameId) {
        String turn = gameDao.selectGameTurnByGameId(gameId);
        if (turn == null) {
            throw new NoSavedGameException("저장된 게임이 없습니다.");
        }

        ChessBoardDto chessBoardDto = gameDao.selectPieceByGameId(gameId);

        ChessGameManager chessGameManager = new ChessGameManager();
        chessGameManager.load(chessBoardDto.toChessBoard(), Color.of(turn));
        return chessGameManager;
    }

    public CommonDto<RoomDto> saveRoom(RoomDto roomDto) {
        String roomName = roomDto.getName();
        int gameId = roomDto.getGameId();

        if (roomDao.countRoomByName(roomName) != 0) {
            throw new HandledException("방이 이미 등록되어있습니다.");
        }

        roomDao.saveRoom(gameId, roomName);
        return new CommonDto<>("방 정보를 가져왔습니다.", new RoomDto(gameId, roomName));
    }

    public String loadRoomName(int gameId) {
        return roomDao.loadRoomName(gameId);
    }
}
