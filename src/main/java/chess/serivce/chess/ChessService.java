package chess.serivce.chess;

import chess.domain.board.Board;
import chess.domain.dto.PieceDto;
import chess.domain.dto.RoomDto;
import chess.domain.dto.RoomsDto;
import chess.domain.dto.move.MoveResponseDto;
import chess.domain.game.Room;
import chess.repository.RoomRepository;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class ChessService {

    private final RoomRepository roomRepository;

    public ChessService(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    public void createRoom(String roomName) {
        roomRepository.create(roomName);
    }

    @Transactional
    public MoveResponseDto move(String roomName, String source, String target) {
        Room room = roomRepository.findByName(roomName);

        room.play("move " + source + " " + target);
        roomRepository.updatePieceMove(room, source, target);
        return new MoveResponseDto(
                pieceDtos(room.getBoard()),
                room.getCurrentTeam().getValue(),
                room.judgeResult()
        );
    }

    @Transactional
    public MoveResponseDto end(String roomName) {
        Room room = roomRepository.findByName(roomName);

        room.play("end");
        roomRepository.update(room);
        return new MoveResponseDto(
            pieceDtos(room.getBoard()),
            room.getCurrentTeam().getValue(),
            room.judgeResult()
        );
    }

    public MoveResponseDto findPiecesInRoom(String roomName) {
        Room room = roomRepository.findByName(roomName);
        return new MoveResponseDto(
            pieceDtos(room.getBoard()),
            room.getCurrentTeam().getValue(),
            room.judgeResult()
        );
    }

    public RoomsDto findAllRooms() {
        List<Room> rooms = roomRepository.findAll();
        List<RoomDto> roomDtos = rooms.stream()
                .map(Room::getName)
                .map(RoomDto::new)
                .collect(Collectors.toList());
        return new RoomsDto(roomDtos);
    }


    private List<PieceDto> pieceDtos(Board board) {
        return board.getPieces()
                .stream()
                .map(piece -> PieceDto.from(piece))
                .collect(Collectors.toList());
    }
}
