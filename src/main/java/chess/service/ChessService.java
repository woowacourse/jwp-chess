package chess.service;

import chess.domain.RoomRepository;
import chess.domain.game.ChessGame;
import chess.domain.piece.Position;
import chess.domain.room.Room;
import chess.web.dto.GameDto;
import chess.web.dto.MessageDto;
import chess.web.dto.StatusDto;
import org.springframework.stereotype.Service;

@Service
public class ChessService {

    private final RoomRepository roomRepository;

    public ChessService(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    public MessageDto end(Long roomId) {
        Room room = roomRepository.findByRoomId(roomId);
        ChessGame chessGame = room.getChessGame();

        chessGame.end();

        roomRepository.update(room);

        return new MessageDto("finished");
    }

    public GameDto loadByGameId(Long roomId) {
        Room room = roomRepository.findByRoomId(roomId);
        ChessGame chessGame = room.getChessGame();

        return new GameDto(chessGame);
    }

    public StatusDto getStatus(Long roomId) {
        Room room = roomRepository.findByRoomId(roomId);
        ChessGame chessGame = room.getChessGame();

        double whiteScore = chessGame.getWhiteScore();
        double blackScore = chessGame.getBlackScore();

        return new StatusDto(whiteScore, blackScore);
    }

    public GameDto move(Long roomId, String source, String target) {
        Room room = roomRepository.findByRoomId(roomId);
        ChessGame chessGame = room.getChessGame();

        chessGame.move(Position.ofChessPiece(source), Position.ofChessPiece(target));
        roomRepository.update(room);

        return new GameDto(chessGame);
    }

}
