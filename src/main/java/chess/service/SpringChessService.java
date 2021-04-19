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

    public Optional<Integer> createRoom(String name) {
        try {
            ChessGame chessGame = initializeChessBoard();
            chessGame.start(Collections.singletonList("start"));
            roomDao.addRoom(new SaveRoomDto(name, chessGame));
            return Optional.of(roomDao.getLastInsertId());
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return Optional.empty();
        }
    }

    public Optional<ChessGameDto> movePiece(MoveRequestDto moveRequestDto) {
        try {
            int roomNo = moveRequestDto.getRoomNo();
            RoomDto roomDto = roomDao.findRoomByRoomNo(roomNo);
            List<String> input = Arrays.asList(moveRequestDto.getCommand().split(" "));
            ChessGame chessGame = setChessGame(roomDto);
            chessGame.play(input);
            roomDao.updateRoom(new RoomDto(roomNo, roomDto.getRoomName(), chessGame));
            return Optional.of(new ChessGameDto(roomNo, roomDto.getRoomName(), chessGame));
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return Optional.empty();
        }
    }

    private ChessGame initializeChessBoard() {
        ChessBoard chessBoard = new ChessBoard();
        return new ChessGame(chessBoard, Color.WHITE, new Ready());
    }

    public Optional<ChessGameDto> loadRoom(int roomNo) {
        try {
            RoomDto roomDto = roomDao.findRoomByRoomNo(roomNo);
            ChessGame chessGame = setChessGame(roomDto);
            return Optional.of(new ChessGameDto(roomNo, roomDto.getRoomName(), chessGame));
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return Optional.empty();
        }
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
        try {
            return roomDao.getAllRoom();
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    public boolean isGameEnd(int roomNo){
        try {
            RoomDto roomDto = roomDao.findRoomByRoomNo(roomNo);
            ChessGame chessGame = setChessGame(roomDto);
            return !chessGame.isOngoing();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public void deleteGame(int roomNo) {
        try {
            roomDao.deleteRoomByRoomNo(roomNo);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public Optional<ResultDto> getResult(int roomNo) {
        try {
            RoomDto roomDto = roomDao.findRoomByRoomNo(roomNo);
            ChessGame chessGame = setChessGame(roomDto);
            return Optional.of(new ResultDto(chessGame.result()));
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return Optional.empty();
        }
    }
}
