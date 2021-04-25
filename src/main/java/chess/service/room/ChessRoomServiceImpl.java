package chess.service.room;

import chess.domain.game.ChessGame;
import chess.domain.room.Room;
import chess.domain.team.BlackTeam;
import chess.domain.team.WhiteTeam;
import chess.domain.game.ChessGameRepository;
import chess.domain.room.ChessRoomRepository;
import dto.ChessGameDto;
import dto.RoomDto;
import dto.RoomRequestDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ChessRoomServiceImpl implements ChessRoomService {
    private final ChessRoomRepository chessRoomRepository;
    private final ChessGameRepository chessGameRepository;

    @Autowired
    public ChessRoomServiceImpl(final ChessRoomRepository chessRoomRepository, final ChessGameRepository ChessGameRepository) {
        this.chessRoomRepository = chessRoomRepository;
        this.chessGameRepository = ChessGameRepository;
    }

    @Override
    public Long create(final RoomRequestDto roomRequestDto) {
        Room room = new Room(roomRequestDto.getId(), roomRequestDto.getName(), roomRequestDto.getPw(), roomRequestDto.getGameId());
        Long gameId = chessGameRepository.create(new ChessGame(new WhiteTeam(), new BlackTeam()));
        return chessRoomRepository.create(room, gameId);
    }

    @Override
    public boolean enterable(final RoomRequestDto roomRequestDto) {
        Room room = new Room(roomRequestDto.getId(), roomRequestDto.getName(), roomRequestDto.getPw(), roomRequestDto.getGameId());
        Room savedRoom = chessRoomRepository.room(room.getId());
        return savedRoom.checkPassword(room);
    }

    @Override
    public List<RoomDto> rooms() {
        List<Room> rooms = chessRoomRepository.rooms();
        List<RoomDto> roomDtos = new ArrayList<>();

        for (Room room : rooms) {
            roomDtos.add(new RoomDto(room));
        }
        return roomDtos;
    }

    @Override
    public ChessGameDto enter(final RoomRequestDto roomRequestDto) {
        if (enterable(roomRequestDto)) {
            Room room = chessRoomRepository.room(roomRequestDto.getId());
            Long gameId = room.getGameId();
            return new ChessGameDto(gameId, chessGameRepository.chessGame(gameId));
        }

        throw new IllegalArgumentException();
    }
}
