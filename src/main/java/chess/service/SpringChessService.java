package chess.service;

import chess.dto.*;
import chess.database.dao.SpringRoomDao;
import chess.domain.board.ChessBoard;
import chess.domain.board.Position;
import chess.domain.feature.Color;
import chess.domain.feature.Type;
import chess.domain.game.ChessGame;
import chess.domain.gamestate.Ready;
import chess.domain.gamestate.Running;
import chess.domain.piece.Piece;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class SpringChessService {
    private final SpringRoomDao roomDao;

    public SpringChessService(SpringRoomDao roomDao) {
        this.roomDao = roomDao;
    }

    public int createRoom(String name) {
        ChessGame chessGame = initializeChessBoard();
        chessGame.start(Collections.singletonList("start"));
        return roomDao.addRoom(new SaveRoomDto(name, chessGame));
    }

    public ChessGameDto movePiece(int roomNo, MoveRequestDto moveRequestDto) {
        RoomDto roomDto = roomDao.findRoomByRoomNo(roomNo);
        List<String> input = Arrays.asList(moveRequestDto.getCommand().split(" "));
        ChessGame chessGame = setChessGame(roomDto);
        chessGame.play(input);
        roomDao.updateRoom(new RoomDto(roomNo, roomDto.getRoomName(), chessGame));
        return new ChessGameDto(roomNo, roomDto.getRoomName(), chessGame);
    }

    private ChessGame initializeChessBoard() {
        ChessBoard chessBoard = new ChessBoard();
        return new ChessGame(chessBoard, Color.WHITE, new Ready());
    }

    public ChessGameDto loadRoom(int roomNo) {
        RoomDto roomDto = roomDao.findRoomByRoomNo(roomNo);
        ChessGame chessGame = setChessGame(roomDto);
        return new ChessGameDto(roomNo, roomDto.getRoomName(), chessGame);
    }

    private ChessGame setChessGame(RoomDto roomDto) {
        ChessBoard chessBoard = new ChessBoard();
        Color turn = Color.convert(roomDto.getTurn());
        String[] splitBoard = roomDto.getBoard().split(",");
        for (String board : splitBoard) {
            String[] pieceInfo = board.split(" ");
            Piece piece = getPiece(pieceInfo);
            chessBoard.replace(Position.of(pieceInfo[0]), piece);
        }
        return new ChessGame(chessBoard, turn, new Running());
    }

    private Piece getPiece(String[] pieceInfo) {
        String type = pieceInfo[1];
        String color = pieceInfo[2];

        Type pieceType = Type.convert(type);
        return pieceType.createPiece(Position.of(pieceInfo[0]), Color.convert(color));
    }

    public List<RoomDto> getAllSavedRooms() {
        return roomDao.getAllRoom();
    }

    public void deleteRoom(int roomNo) {
        roomDao.deleteRoomByRoomNo(roomNo);
    }

    public ChessGameDto resetRoom(int roomNo) {
        ChessGame chessGame = initializeChessBoard();
        chessGame.start(Collections.singletonList("start"));
        RoomDto roomDto = roomDao.findRoomByRoomNo(roomNo);
        roomDao.updateRoom(new RoomDto(roomNo, roomDto.getRoomName(), chessGame));
        return new ChessGameDto(roomNo, roomDto.getRoomName(), chessGame);
    }
}
