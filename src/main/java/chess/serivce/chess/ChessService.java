package chess.serivce.chess;

import chess.domain.board.Board;
import chess.domain.dto.PieceDto;
import chess.domain.dto.move.MoveResponseDto;
import chess.domain.game.Room;
import chess.domain.piece.Piece;
import chess.repository.room.RoomRepository;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ChessService {

    private final RoomRepository roomRepository;

    public ChessService(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    @Transactional
    public MoveResponseDto start(String roomName) {
        Room room = roomRepository.findRoomByName(roomName);

        room.play("start");
        roomRepository.save(room);

        Board board = room.getBoard();
        return new MoveResponseDto(
            generatePieceDtosFromPieces(board.getPieces()),
            room.getCurrentTeam().getValue(),
            room.judgeResult()
        );
    }

    @Transactional
    public MoveResponseDto end(String roomName) {
        Room room = roomRepository.findRoomByName(roomName);

        room.play("end");
        roomRepository.save(room);

        Board board = room.getBoard();
        return new MoveResponseDto(
            generatePieceDtosFromPieces(board.getPieces()),
            room.getCurrentTeam().getValue(),
            room.judgeResult()
        );
    }

    @Transactional
    public MoveResponseDto move(String roomName, String source, String target) {
        Room room = roomRepository.findRoomByName(roomName);

        room.play("move " + source + " " + target);
        roomRepository.saveAfterMove(room, source, target);

        Board board = room.getBoard();
        return new MoveResponseDto(
            generatePieceDtosFromPieces(board.getPieces()),
            room.getCurrentTeam().getValue(),
            room.judgeResult()
        );
    }

    @Transactional(readOnly = true)
    public MoveResponseDto findPiecesByRoomName(String roomName) {
        Room room = roomRepository.findRoomByName(roomName);

        Board board = room.getBoard();
        return new MoveResponseDto(
            generatePieceDtosFromPieces(board.getPieces()),
            room.getCurrentTeam().getValue(),
            room.judgeResult()
        );
    }

    public void createRoom(String roomName) {
        if (!roomRepository.exists(roomName)) {
            throw new IllegalArgumentException("[ERROR] 이미 존재하는 방입니다. 다른 이름을 사용해주세요.");
        }

        roomRepository.insert(roomName);
    }

    private List<PieceDto> generatePieceDtosFromPieces(List<Piece> pieces) {
        return pieces
            .stream()
            .map(piece -> PieceDto.from(piece))
            .collect(Collectors.toList());
    }
}
