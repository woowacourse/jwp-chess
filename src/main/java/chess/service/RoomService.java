package chess.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import chess.domain.board.BoardDto;
import chess.domain.chess.Chess;
import chess.domain.room.Room;
import chess.repository.RoomRepository;

@Service
public class RoomService {

    private final RoomRepository roomRepository;

    public RoomService(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    public List<Room> findAll() {
        return roomRepository.findAll();
    }

    @Transactional
    public long insert(Room room) {
        BoardDto boardDto = BoardDto.from(Chess.createWithEmptyBoard().start());
        return roomRepository.insert(room, boardDto);
    }
}
