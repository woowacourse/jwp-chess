package chess.service;

import chess.dao.BoardDao;
import chess.dao.RoomDao;
import chess.domain.board.Board;
import chess.domain.game.ChessGame;
import chess.domain.game.room.Room;
import chess.domain.game.room.RoomId;
import chess.domain.game.room.RoomPassword;
import chess.domain.game.score.Score;
import chess.domain.piece.Piece;
import chess.domain.piece.PieceColor;
import chess.domain.position.Position;
import chess.dto.request.CreateRoomDto;
import chess.dto.request.MovePieceDto;
import chess.dto.response.BoardDto;
import chess.dto.response.PieceColorDto;
import chess.dto.response.RoomDto;
import chess.dto.response.ScoreResultDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map.Entry;

@Service
public class ChessService {
    private final RoomDao roomDao;
    private final BoardDao boardDao;

    @Autowired
    public ChessService(RoomDao roomDao, BoardDao boardDao) {
        this.roomDao = roomDao;
        this.boardDao = boardDao;
    }

    public RoomDto createRoom(CreateRoomDto createRoomDto) {
        Room room = Room.create(createRoomDto.getTitle(), createRoomDto.getPassword());
        roomDao.createRoom(room);

        initializeBoard(room);

        return RoomDto.from(room);
    }

    private void initializeBoard(Room room) {
        Board initializedBoard = Board.createInitializedBoard();
        for (Entry<Position, Piece> entry : initializedBoard.getValue().entrySet()) {
            Position position = entry.getKey();
            Piece piece = entry.getValue();

            boardDao.createPiece(room.getId(), position, piece);
        }
    }

    public void deleteRoom(String id, String password) {
        RoomId roomId = RoomId.from(id);
        RoomPassword roomPassword = RoomPassword.createByPlainText(password);

        if (!generateChessGame(roomId).isEnd()) {
            throw new IllegalStateException("게임이 끝나지 않아서 방을 삭제할 수 없습니다.");
        }

        roomDao.deleteRoom(roomId, roomPassword);
    }

    public List<Room> getRooms() {
        return roomDao.getAllRooms();
    }

    public void movePiece(String id, MovePieceDto movePieceDto) {
        RoomId roomId = RoomId.from(id);
        Position from = movePieceDto.getFromAsPosition();
        Position to = movePieceDto.getToAsPosition();

        ChessGame chessGame = generateChessGame(roomId);
        chessGame.movePiece(from, to);

        updateGameTurn(roomId, chessGame);
        updatePiecePosition(roomId, from, to);
    }

    private void updatePiecePosition(RoomId roomId, Position from, Position to) {
        boardDao.deletePiece(roomId, to);
        boardDao.updatePiecePosition(roomId, from, to);
    }

    private void updateGameTurn(RoomId roomId, ChessGame chessGame) {
        if (chessGame.isWhiteTurn()) {
            roomDao.updateTurnToWhite(roomId);
            return;
        }

        roomDao.updateTurnToBlack(roomId);
    }

    public PieceColorDto getCurrentTurn(String roomId) {
        ChessGame chessGame = generateChessGame(RoomId.from(roomId));
        if (chessGame.isWhiteTurn()) {
            return PieceColorDto.from(PieceColor.WHITE);
        }

        return PieceColorDto.from(PieceColor.BLACK);
    }

    public ScoreResultDto getScore(String roomId) {
        ChessGame chessGame = generateChessGame(RoomId.from(roomId));
        Score whiteScore = chessGame.getStatus().getScoreByPieceColor(PieceColor.WHITE);
        Score blackScore = chessGame.getStatus().getScoreByPieceColor(PieceColor.BLACK);
        return ScoreResultDto.of(whiteScore, blackScore);
    }

    public PieceColorDto getWinColor(String roomId) {
        ChessGame chessGame = generateChessGame(RoomId.from(roomId));
        PieceColor winColor = chessGame.getWinColor();
        return PieceColorDto.from(winColor);
    }

    private ChessGame generateChessGame(RoomId roomId) {
        Board board = boardDao.getBoard(roomId);
        PieceColor currentTurn = roomDao.getCurrentTurn(roomId);
        return ChessGame.of(board, currentTurn);
    }

    public BoardDto getBoard(String roomId) {
        Board board = boardDao.getBoard(RoomId.from(roomId));
        return new BoardDto(board);
    }

    @Override
    public String toString() {
        return "ChessService{" +
                "gameDao=" + roomDao +
                ", boardDao=" + boardDao +
                '}';
    }
}
