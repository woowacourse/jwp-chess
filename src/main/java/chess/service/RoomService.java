package chess.service;

import chess.domain.board.Score;
import chess.domain.chessgame.ChessGame;
import chess.domain.chessgame.Room;
import chess.domain.piece.Color;
import chess.domain.piece.Piece;
import chess.domain.position.Position;
import chess.exception.DataBaseException;
import chess.exception.NotExistPieceDataException;
import chess.exception.NotExistRoomDataException;
import chess.repository.RoomRepository;
import java.util.List;
import java.util.Map;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RoomService {

    private final RoomRepository roomRepository;

    public RoomService(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    @Transactional
    public List<Integer> getRooms() {
        List<Integer> roomIds;

        try {
            roomIds = roomRepository.findAllPlayingRoomId();
        } catch (DataAccessException e) {
            throw new DataBaseException(e);
        }

        if (roomIds.isEmpty()) {
            throw new NotExistRoomDataException();
        }
        return roomIds;
    }

    @Transactional
    public Room postRoom(int roomId) {
        Room room;
        try {
            room = roomRepository.findRoomByRoomId(roomId);
        } catch (DataAccessException e) {
            room = new Room(roomId);
            roomRepository.insertRoom(room);
            roomRepository.insertPieces(room);
            return room;
        }

        if (room.pieces().isEmpty()) {
            throw new NotExistPieceDataException();
        }

        return room;
    }

    @Transactional
    public ChessGame putPieces(int roomId, Position source, Position target) {
        try {
            ChessGame chessGame = this.chessGame(roomId);
            chessGame.move(source, target);
            Room updatedRoom = new Room(roomId, chessGame);

            roomRepository.updateChessGameByRoom(updatedRoom);
            roomRepository.updatePiecesByRoom(updatedRoom);

            return chessGame;
        } catch (DataAccessException e) {
            throw new DataBaseException(e);
        }
    }

    private ChessGame chessGame(int roomId) {
        Map<Position, Piece> pieces = roomRepository.findPiecesByRoomId(roomId);
        if (pieces.isEmpty()) {
            throw new NotExistPieceDataException();
        }

        Boolean isPlaying = roomRepository.findPlayingFlagByRoomId(roomId);
        Color turn = roomRepository.findTurnByRoomId(roomId);

        return new ChessGame(pieces, isPlaying, turn);
    }

    @Transactional
    public Score getScore(int roomId, Color color) {
        try {
            ChessGame chessGame = this.chessGame(roomId);
            return chessGame.score(color);
        } catch (DataAccessException e) {
            throw new DataBaseException(e);
        }
    }

}
