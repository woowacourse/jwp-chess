package chess.service;

import chess.dao.GameDao;
import chess.dao.PieceDao;
import chess.dao.RoomDao;
import chess.domain.ChessGame;
import chess.domain.board.Board;
import chess.domain.board.BoardInitializer;
import chess.domain.piece.Color;
import chess.domain.piece.Piece;
import chess.domain.piece.PieceFactory;
import chess.domain.position.Position;
import chess.domain.state.Running;
import chess.domain.state.StateFactory;
import chess.dto.BoardElementDto;
import chess.dto.ChessGameDto;
import chess.dto.DeleteRequestDto;
import chess.dto.DeleteResponseDto;
import chess.dto.RoomDto;
import java.util.HashMap;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public final class ChessService {
    private static final int GAME_ID = 0;

    private final GameDao gameDao;
    private final RoomDao roomDao;
    private final PieceDao pieceDao;
    private int nextGameId = 0;
    private final Map<Integer, ChessGame> chessGames;
    private ChessGame chessGame;

    public ChessService(GameDao gameDao, RoomDao roomDao, PieceDao pieceDao) {
        this.gameDao = gameDao;
        this.roomDao = roomDao;
        this.pieceDao = pieceDao;
        this.chessGames = new HashMap<>();
    }

    private void saveAllPieceToStorage(int gameId, Map<Position, Piece> boardElements) {
        List<BoardElementDto> boardElementDtos = toBoardDtos(boardElements);
        for (BoardElementDto boardElementDto : boardElementDtos) {
            pieceDao.savePiece(gameId, boardElementDto);
        }
    }

    private List<BoardElementDto> toBoardDtos(Map<Position, Piece> board) {
        return board.entrySet()
                .stream()
                .map(it -> toBoardDto(it.getKey(), it.getValue()))
                .collect(Collectors.toList());
    }

    private BoardElementDto toBoardDto(Position position, Piece piece) {
        return new BoardElementDto(position, piece);
    }

    private Board convertToBoard(final List<BoardElementDto> boardDatas) {
        Map<Position, Piece> board = boardDatas.stream()
                .collect(Collectors.toMap(it -> Position.from(it.getPosition()),
                        it -> PieceFactory.of(it.getPieceName(), it.getPieceColor())));
        return new Board(() -> board);
    }

    public ChessGameDto move(int gameId, final String from, final String to) {
        chessGame.move(Position.from(from), Position.from(to));
        final var nextColor = getColorFromStorage(gameId).next();
        updateBoard(from, to, gameId, nextColor.name());
        return new ChessGameDto(pieceDao.findAllPieceById(gameId), chessGame.status());
    }

    private void updateBoard(String from, String to, int gameId, String turn) {
        pieceDao.deletePieceByIdAndPosition(gameId, to);
        pieceDao.updatePiecePosition(gameId, from, to);
        gameDao.updateTurn(gameId, turn);
    }

    public ChessGameDto newGame(int gameId) {
        initGame(gameId);
        chessGame = new ChessGame(new Running(getColorFromStorage(gameId), getBoardFromStorage(gameId)));
        return new ChessGameDto(pieceDao.findAllPieceById(gameId), chessGame.status());
    }

    private void initGame(int gameId) {
        pieceDao.deleteAllPieceById(gameId);
        saveAllPieceToStorage(gameId, new BoardInitializer().init());
        gameDao.updateTurn(gameId, "WHITE");
        roomDao.updateStatus(gameId, "PLAY");
    }

    public ChessGameDto loadGame(int gameId) {
        chessGame = new ChessGame(StateFactory.of(getColorFromStorage(gameId), getBoardFromStorage(gameId)));
        return new ChessGameDto(pieceDao.findAllPieceById(gameId), chessGame.status());
    }

    private Color getColorFromStorage(int gameId) {
        return Color.from(gameDao.findTurnById(gameId));
    }

    private Board getBoardFromStorage(int gameId) {
        return convertToBoard(pieceDao.findAllPieceById(gameId));
    }

    public void createRoom(String roomName, String password) {
        RoomDto roomDto = new RoomDto(nextGameId++, roomName, password, "STOP");
        deleteOldData(roomDto.getId());
        ChessGame chessGame = makeGame(roomDto.getId(), roomDto);
        chessGames.put(roomDto.getId(), chessGame);
    }

    private void deleteOldData(int gameId) {
        chessGames.remove(gameId);
        pieceDao.deleteAllPieceById(gameId);
        roomDao.deleteRoom(gameId);
        gameDao.deleteGame(gameId);
    }

    private ChessGame makeGame(int gameId, RoomDto roomDto) {
        initNewChessGame(gameId, roomDto);
        return new ChessGame(new Running(getColorFromStorage(gameId),
                getBoardFromStorage(gameId)));
    }

    private void initNewChessGame(int gameId, RoomDto roomDto) {
        gameDao.insertGame(gameId, "WHITE");
        roomDao.saveRoom(roomDto);
        saveAllPieceToStorage(gameId, new BoardInitializer().init());
    }

    public List<RoomDto> loadRooms() {
        return roomDao.findAll();
    }

    public void endGame(int gameId) {
        pieceDao.deleteAllPieceById(gameId);
        roomDao.updateStatus(gameId, "STOP");
    }

    public DeleteResponseDto deleteGame(DeleteRequestDto deleteRequestDto) {
        String password = roomDao.findPasswordById(deleteRequestDto.getId());
        if (password.equals(deleteRequestDto.getPassword())) {
            roomDao.deleteRoom(deleteRequestDto.getId());
            gameDao.deleteGame(deleteRequestDto.getId());
            return DeleteResponseDto.success();
        }
        return DeleteResponseDto.fail();
    }
}
