package chess.service;

import chess.model.dto.CreateRoomDto;
import chess.model.dto.DeleteRoomDto;
import chess.model.dto.RoomsDto;
import chess.model.repository.RoomDao;
import org.springframework.stereotype.Service;

@Service
public class RoomService {

    private static final RoomDao ROOM_DAO = RoomDao.getInstance();

    public RoomsDto getUsedRooms() {
        return new RoomsDto(ROOM_DAO.selectUsedOnly());
    }

    public void addRoom(CreateRoomDto createRoomDto) {
        ROOM_DAO.insert(createRoomDto.getRoomName(), createRoomDto.getRoomPassword());
    }

    public void deleteRoom(DeleteRoomDto deleteRoomDto) {
        ROOM_DAO.updateUsedN(deleteRoomDto.getRoomId());
        new ChessGameService().closeGamesOf(deleteRoomDto.getRoomId());
    }
}
