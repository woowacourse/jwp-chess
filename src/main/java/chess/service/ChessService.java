package chess.service;

import chess.dao.GameDao;
import chess.domain.ChessGameManager;
import chess.domain.piece.Color;
import chess.domain.position.Position;
import chess.dto.*;
import org.springframework.stereotype.Service;

@Service
public class ChessService {
    private final GameDao gameDAO;

    public ChessService(GameDao gameDAO) {
        this.gameDAO = gameDAO;
    }

    public CommonDto<GameListDto> loadGameList() {
        return new CommonDto<>("게임 목록을 불러왔습니다.", new GameListDto(gameDAO.loadGameList()));
    }

    public CommonDto<NewGameDto> saveNewGame() {
        ChessGameManager chessGameManager = new ChessGameManager();
        chessGameManager.start();

        ChessBoardDto chessBoardDto = ChessBoardDto.from(chessGameManager.getBoard());
        SavedGameDto savedGameDto = new SavedGameDto(
                chessBoardDto,
                chessGameManager.getCurrentTurnColor().name());
        int gameId = gameDAO.saveGame(savedGameDto);
        System.out.println("게임 세이브 ********** ");
        savePiecesByGameId(gameId, chessBoardDto);

        return new CommonDto<>("새로운 게임이 생성되었습니다.",
                NewGameDto.from(chessGameManager, gameId));
    }

    private void savePiecesByGameId(int gameId, ChessBoardDto chessBoardDto) {
        chessBoardDto.board().forEach((key, value) -> gameDAO.savePieceByGameId(gameId, key, value));
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

        updateDatabase(chessGameManager, gameId);

        return new CommonDto<>("기물을 이동했습니다.",
                toRunningGameDto(chessGameManager));
    }

    private void updateDatabase(ChessGameManager chessGameManager, int gameId) {
        if (chessGameManager.isEnd()) {
            gameDAO.deletePiecesByGameId(gameId);
            gameDAO.deleteGameByGameId(gameId);
            return;
        }

        gameDAO.updateTurnByGameId(chessGameManager.getCurrentTurnColor(), gameId);
        gameDAO.deletePiecesByGameId(gameId);
        savePiecesByGameId(gameId, ChessBoardDto.from(chessGameManager.getBoard()));
    }

    private ChessGameManager loadChessGameManager(int gameId) {
        SavedGameDto savedGameDto = gameDAO.loadGame(gameId);

        ChessGameManager chessGameManager = new ChessGameManager();
        chessGameManager.load(
                savedGameDto.getChessBoardDto().toChessBoard(),
                Color.of(savedGameDto.getCurrentTurnColor()));
        return chessGameManager;
    }

    public CommonDto<RoomDto> saveRoom(RoomDto roomDto) {
        String roomName = roomDto.getName();
        int gameId = roomDto.getGameId();
        System.out.println("**** GameID : " + gameId);
        int roomCount = gameDAO.findRoomNameCount(roomName);
        String countedRoomName = addRoomCount(roomName, roomCount);
        System.out.println("카운트한 이름 중복 개수 : " + countedRoomName);
        System.out.println("room Save *** ");
        gameDAO.saveRoom(gameId, roomName);
        return new CommonDto<>("방 정보를 가져왔습니다.", new RoomDto(gameId, countedRoomName));
    }

    private String addRoomCount(String roomName, int roomCount) {
        if (roomCount != 0) {
            roomName = roomName + " (" + roomCount + ")";
        }
        return roomName;
    }
}
