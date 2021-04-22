package chess.service;

import chess.domain.board.Score;
import chess.domain.chessgame.ChessGame;
import chess.domain.chessgame.Room;
import chess.domain.piece.Color;
import chess.domain.piece.Piece;
import chess.domain.position.Position;
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
        return roomRepository.findAllPlayingRoomId();
    }

    @Transactional
    public Room postRoom(int roomId) {
        try {
            return roomRepository.findRoomByRoomId(roomId);
        } catch (DataAccessException e) {
            Room newRoom = new Room(roomId);
            roomRepository.insertRoom(newRoom);
            roomRepository.insertPieces(newRoom);

            return newRoom;
        }
    }

    @Transactional
    public ChessGame putPieces(int roomId, Position source, Position target) {
        ChessGame chessGame = this.chessGame(roomId);
        chessGame.move(source, target);
        Room updatedRoom = new Room(roomId, chessGame);

        roomRepository.updateChessGameByRoom(updatedRoom);
        roomRepository.updatePiecesByRoom(updatedRoom);

        return chessGame;
    }

    private ChessGame chessGame(int roomId) {
        Map<Position, Piece> pieces = roomRepository.findPiecesByRoomId(roomId);
        boolean isPlaying = roomRepository.findPlayingFlagByRoomId(roomId);
        Color turn = roomRepository.findTurnByRoomId(roomId);

        return new ChessGame(pieces, isPlaying, turn);
    }

    @Transactional
    public Score getScore(int roomId, Color color) {
        ChessGame chessGame = this.chessGame(roomId);

        return chessGame.score(color);
    }

}
