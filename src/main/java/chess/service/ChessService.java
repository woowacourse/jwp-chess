package chess.service;

import chess.dto.request.RoomRequestDto;
import chess.dto.response.RoomResponseDto;
import chess.entity.RoomEntity;
import chess.repository.BoardRepository;
import chess.repository.RoomRepository;
import org.springframework.stereotype.Service;

@Service
public class ChessService {

    private final RoomRepository roomRepository;
    private final BoardRepository boardRepository;

    public ChessService(final RoomRepository roomRepository, final BoardRepository boardRepository) {
        this.roomRepository = roomRepository;
        this.boardRepository = boardRepository;
    }

    public RoomResponseDto createRoom(final RoomRequestDto roomRequestDto) {
        final RoomEntity room = new RoomEntity(roomRequestDto.getName(), "white", false);
        final RoomEntity createdRoom = roomRepository.insert(room);
        return RoomResponseDto.of(createdRoom);
    }
}
