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
import chess.dto.RoomDto;
import java.util.HashMap;
import java.util.stream.IntStream;
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

    public ChessGameDto move(final String from, final String to) {
        chessGame.move(Position.from(from), Position.from(to));
        final var nextColor = getColorFromStorage(GAME_ID).next();
        updateBoard(from, to, GAME_ID, nextColor.name());
        return new ChessGameDto(pieceDao.findAllPieceById(GAME_ID), chessGame.status());
    }

    private void updateBoard(String from, String to, int gameId, String turn) {
        pieceDao.deletePieceByIdAndPosition(gameId, to);
        pieceDao.updatePiecePosition(gameId, from, to);
        gameDao.updateTurn(gameId, turn);
    }

    public ChessGameDto loadGame() {
        chessGame = new ChessGame(StateFactory.of(getColorFromStorage(GAME_ID), getBoardFromStorage(GAME_ID)));
        return new ChessGameDto(pieceDao.findAllPieceById(GAME_ID), chessGame.status());
    }

    private Color getColorFromStorage(int gameId) {
        return Color.from(gameDao.findTurnById(gameId));
    }

    private Board getBoardFromStorage(int gameId) {
        return convertToBoard(pieceDao.findAllPieceById(gameId));
    }

    public void createRoom(String roomName, String password) {
        RoomDto roomDto = new RoomDto(nextGameId++, roomName, password);
        deleteOldData(roomDto.getId());
        ChessGame chessGame = makeGame(roomDto.getId(), roomDto);
        chessGames.put(roomDto.getId(), chessGame);
    }

    private void deleteOldData(int gameId) {
        chessGames.remove(gameId);
        pieceDao.deleteAllPieceById(gameId);
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
        return IntStream.range(0, nextGameId)
                .mapToObj(roomDao::findById)
                .collect(Collectors.toList());
    }
}
