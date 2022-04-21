package chess.service;

import chess.domain.board.BoardFactory;
import chess.domain.board.Position;
import chess.domain.piece.Piece;
import chess.dto.request.RoomRequestDto;
import chess.dto.response.RoomResponseDto;
import chess.entity.BoardEntity;
import chess.entity.RoomEntity;
import chess.repository.BoardRepository;
import chess.repository.RoomRepository;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
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
        boardRepository.batchInsert(createBoards(createdRoom));
        return RoomResponseDto.of(createdRoom);
    }

    private List<BoardEntity> createBoards(final RoomEntity createdRoom) {
        final Map<Position, Piece> board = BoardFactory.initialize();
        return board.entrySet().stream()
            .map(entry -> new BoardEntity(createdRoom.getId(),
                entry.getKey().convertPositionToString(),
                entry.getValue().convertPieceToString()))
            .collect(Collectors.toList());
    }
}
