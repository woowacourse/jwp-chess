package chess.service;

import chess.domain.ChessRepository;
import chess.domain.board.Board;
import chess.domain.game.ChessGame;
import chess.domain.piece.PieceFactory;
import chess.domain.piece.Position;
import chess.domain.room.Room;
import chess.web.dto.GameDto;
import chess.web.dto.MessageDto;
import chess.web.dto.RoomDto;
import chess.web.dto.StatusDto;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
public class ChessService {

    private final ChessRepository chessRepository;

    public ChessService(ChessRepository chessRepository) {
        this.chessRepository = chessRepository;
    }

    public MessageDto end(Long roomId) {
        Room room = chessRepository.findByRoomId(roomId);
        ChessGame chessGame = room.getChessGame();

        chessGame.end();

        chessRepository.update(room);

        return new MessageDto("finished");
    }

    public GameDto loadByGameId(Long roomId) {
        Room room = chessRepository.findByRoomId(roomId);
        ChessGame chessGame = room.getChessGame();

        return new GameDto(chessGame);
    }

    public RoomDto createNewRoom(String roomName) {
        Long roomId = saveGameToDB(roomName);

        return new RoomDto(new Room(roomId, roomName, null));
    }

    private Long saveGameToDB(String roomName) {
        ChessGame chessGame = new ChessGame(
                null,
                new Board(PieceFactory.createPieces())
        );
        chessGame.start();

        return chessRepository.save(
                new Room(
                        null,
                        roomName,
                        chessGame
                )
        );
    }

    public StatusDto getStatus(Long roomId) {
        Room room = chessRepository.findByRoomId(roomId);
        ChessGame chessGame = room.getChessGame();

        double whiteScore = chessGame.getWhiteScore();
        double blackScore = chessGame.getBlackScore();

        return new StatusDto(whiteScore, blackScore);
    }

    public GameDto move(Long roomId, String source, String target) {
        Room room = chessRepository.findByRoomId(roomId);
        ChessGame chessGame = room.getChessGame();

        chessGame.move(Position.ofChessPiece(source), Position.ofChessPiece(target));
        chessRepository.update(room);

        return new GameDto(chessGame);
    }

    public List<RoomDto> getAllRooms() {
        return chessRepository.allRooms().stream()
                .map(room -> new RoomDto(room))
                .collect(toList());
    }

}
